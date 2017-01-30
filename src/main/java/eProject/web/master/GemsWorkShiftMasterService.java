package eProject.web.master;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.master.GemsWorkShiftMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsWorkShiftMasterService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsWorkShiftMasterService.class);

	@RequestMapping(value = "/master/checkShiftByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkShiftByCode(HttpServletRequest request) {

		String workShiftCode = request.getParameter("workShiftCode");

		GemsWorkShiftMaster gemsWorkShiftMaster = new GemsWorkShiftMaster();
		gemsWorkShiftMaster.setWorkShiftCode(workShiftCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsWorkShiftMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsWorkShiftMaster returnedGemsWorkShiftMaster = masterService
				.getGemsWorkShiftMasterByCode(gemsWorkShiftMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsWorkShiftMaster.getWorkShiftCode()))
				|| (StringUtils.isNotEmpty(returnedGemsWorkShiftMaster.getWorkShiftCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewWorkShiftList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewWorkShiftList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsWorkShiftMaster gemsWorkShiftMaster = new GemsWorkShiftMaster();

			String searchWorkShiftCode = request.getParameter("searchWorkShiftCode");
			if (searchWorkShiftCode != null && searchWorkShiftCode.isEmpty() == false) {
				gemsWorkShiftMaster.setWorkShiftCode(searchWorkShiftCode);
			}

			String searchWorkShiftDescription = request.getParameter("searchWorkShiftDescription");
			if (searchWorkShiftDescription != null && searchWorkShiftDescription.isEmpty() == false) {
				gemsWorkShiftMaster.setWorkShiftDescription(searchWorkShiftDescription);
			}
			gemsWorkShiftMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsWorkShiftMasterFilterCount(gemsWorkShiftMaster);
			List<GemsWorkShiftMaster> list = masterService.getGemsWorkShiftMasterList(start, limit,
					gemsWorkShiftMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapWorkShiftList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveWorkShift", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveWorkShift(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsWorkShiftMaster gemsWorkShiftMaster = new GemsWorkShiftMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsWorkShiftMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsWorkShiftMaster.setUpdatedOn(todayDate);
			gemsWorkShiftMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsWorkShiftMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsWorkShiftMasterId")))) {
				id_value = request.getParameter("gemsWorkShiftMasterId");
				gemsWorkShiftMaster = masterService.getGemsWorkShiftMaster(Integer.parseInt(id_value));
			} else {
				gemsWorkShiftMaster.setCreatedOn(todayDate);
				gemsWorkShiftMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String workShiftCode = request.getParameter("workShiftCode");
			gemsWorkShiftMaster.setWorkShiftCode(workShiftCode);

			String workShiftDescription = request.getParameter("workShiftDescription");
			gemsWorkShiftMaster.setWorkShiftDescription(workShiftDescription);

			String shiftHours = request.getParameter("shiftHours");
			gemsWorkShiftMaster.setShiftHours(new Double(shiftHours));

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsWorkShiftMaster.setActiveStatus(1);
			} else {
				gemsWorkShiftMaster.setActiveStatus(0);
			}

			masterService.saveGemsWorkShiftMaster(gemsWorkShiftMaster);
			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/deleteWorkShift", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteWorkShift(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsWorkShiftMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsWorkShiftMaster gemsWorkShiftMaster = masterService.getGemsWorkShiftMaster(gemsWorkShiftMasterId);
			masterService.removeGemsWorkShiftMaster(gemsWorkShiftMaster);
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapWorkShiftList(List<GemsWorkShiftMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsWorkShiftMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsWorkShiftMaster)) {
					return new JSONObject(true);
				}

				GemsWorkShiftMaster gemsWorkShiftMaster = (GemsWorkShiftMaster) bean;

				return new JSONObject().element("gemsWorkShiftMasterId", gemsWorkShiftMaster.getGemsWorkShiftMasterId())
						.element("workShiftCode", gemsWorkShiftMaster.getWorkShiftCode())
						.element("workShiftDescription", gemsWorkShiftMaster.getWorkShiftDescription())
						.element("shiftHours", gemsWorkShiftMaster.getShiftHours())
						.element("activeStatus", gemsWorkShiftMaster.getActiveStatus());
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Common json methds
	 */

	private ModelAndView getModelMap() {

		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		modelMap.put("success", true);
		return new ModelAndView("jsonView", modelMap);
	}

	private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}
}
