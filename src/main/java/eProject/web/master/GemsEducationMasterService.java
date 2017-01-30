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
import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsEducationMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsEducationMasterService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEducationMasterService.class);

	@RequestMapping(value = "/master/checkEducationByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkEducationByCode(HttpServletRequest request) {

		String educationCode = request.getParameter("educationCode");

		GemsEducationMaster gemsEducationMaster = new GemsEducationMaster();
		gemsEducationMaster.setEducationCode(educationCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsEducationMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsEducationMaster returnedGemsEducationMaster = masterService
				.getGemsEducationMasterByCode(gemsEducationMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsEducationMaster.getEducationCode()))
				|| (StringUtils.isNotEmpty(returnedGemsEducationMaster.getEducationCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewEducationMasterList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEducationMasterList(HttpServletRequest request) {

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

			GemsEducationMaster gemsEducationMaster = new GemsEducationMaster();

			String searchEducationCode = request.getParameter("searchEducationCode");
			if (searchEducationCode != null && searchEducationCode.isEmpty() == false) {
				gemsEducationMaster.setEducationCode(searchEducationCode);
			}

			String searchEducationDescription = request.getParameter("searchEducationDescription");
			if (searchEducationDescription != null && searchEducationDescription.isEmpty() == false) {
				gemsEducationMaster.setEducationDescription(searchEducationDescription);
			}
			gemsEducationMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsEducationMasterFilterCount(gemsEducationMaster);
			List<GemsEducationMaster> list = masterService.getGemsEducationMasterList(start, limit,
					gemsEducationMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapEducationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveEducationMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEducationMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEducationMaster gemsEducationMaster = new GemsEducationMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEducationMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEducationMaster.setUpdatedOn(todayDate);
			gemsEducationMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEducationMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEducationMasterId")))) {
				id_value = request.getParameter("gemsEducationMasterId");
				gemsEducationMaster = masterService.getGemsEducationMaster(Integer.parseInt(id_value));
			} else {
				gemsEducationMaster.setCreatedOn(todayDate);
				gemsEducationMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String educationCode = request.getParameter("educationCode");
			gemsEducationMaster.setEducationCode(educationCode);
			String educationDescription = request.getParameter("educationDescription");
			gemsEducationMaster.setEducationDescription(educationDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEducationMaster.setActiveStatus(1);
			} else {
				gemsEducationMaster.setActiveStatus(0);
			}

			masterService.saveGemsEducationMaster(gemsEducationMaster);
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

	@RequestMapping(value = "/master/deleteEducationMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteEducationMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEducationMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEducationMaster gemsEducationMaster = masterService.getGemsEducationMaster(gemsEducationMasterId);
			masterService.removeGemsEducationMaster(gemsEducationMaster);
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

	private Map<String, Object> getModelMapEducationList(List<GemsEducationMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEducationMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEducationMaster)) {
					return new JSONObject(true);
				}

				GemsEducationMaster gemsEducationMaster = (GemsEducationMaster) bean;

				return new JSONObject().element("gemsEducationMasterId", gemsEducationMaster.getGemsEducationMasterId())
						.element("educationCode", gemsEducationMaster.getEducationCode())
						.element("educationDescription", gemsEducationMaster.getEducationDescription())
						.element("activeStatus", gemsEducationMaster.getActiveStatus());
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
