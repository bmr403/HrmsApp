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
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsBusinessUnitService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsBusinessUnitService.class);

	@RequestMapping(value = "/master/checkBUByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkBUByCode(HttpServletRequest request) {

		String gemsBuCode = request.getParameter("gemsBuCode");

		GemsBusinessUnit gemsBusinessUnit = new GemsBusinessUnit();
		gemsBusinessUnit.setGemsBuCode(gemsBuCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsBusinessUnit.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsBusinessUnit returnedGemsBusinessUnit = masterService.getGemsBusinessUnitByCode(gemsBusinessUnit);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsBusinessUnit.getGemsBuCode()))
				|| (StringUtils.isNotEmpty(returnedGemsBusinessUnit.getGemsBuCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewBusinessUnitList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewBusinessUnitList(HttpServletRequest request) {

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

			GemsBusinessUnit gemsBusinessUnit = new GemsBusinessUnit();

			String searchCode = request.getParameter("searchBUCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsBusinessUnit.setGemsBuCode(searchCode);
			}

			String searchDescription = request.getParameter("searchBUDescription");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsBusinessUnit.setGemsBuDescription(searchDescription);
			}
			if (gemsBusinessUnit.getGemsOrganisation() != null) {
				gemsBusinessUnit.setGemsOrganisation(gemsBusinessUnit.getGemsOrganisation());
			} else {
				gemsBusinessUnit.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			}

			int totalResults = masterService.getGemsBusinessUnitFilterCount(gemsBusinessUnit);
			List<GemsBusinessUnit> list = masterService.getGemsBusinessUnitList(start, limit, gemsBusinessUnit);

			logger.info("Returned list size" + list.size());

			return getModelMapBusinessUnitList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveBusinessUnit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveBusinessUnit(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsBusinessUnit gemsBusinessUnit = new GemsBusinessUnit();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsBusinessUnit.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsBusinessUnit.setUpdatedOn(todayDate);
			gemsBusinessUnit.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsBusinessUnitId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsBusinessUnitId")))) {
				id_value = request.getParameter("gemsBusinessUnitId");
				gemsBusinessUnit = masterService.getGemsBusinessUnit(Integer.parseInt(id_value));
			} else {
				gemsBusinessUnit.setCreatedOn(todayDate);
				gemsBusinessUnit.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String gemsBuCode = request.getParameter("gemsBuCode");
			gemsBusinessUnit.setGemsBuCode(gemsBuCode);
			String gemsBuDescription = request.getParameter("gemsBuDescription");
			gemsBusinessUnit.setGemsBuDescription(gemsBuDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsBusinessUnit.setActiveStatus(1);
			} else {
				gemsBusinessUnit.setActiveStatus(0);
			}

			masterService.saveGemsBusinessUnit(gemsBusinessUnit);
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

	@RequestMapping(value = "/master/deleteBusinessUnit", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteBusinessUnit(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsBusinessUnitId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsBusinessUnit gemsBusinessUnit = masterService.getGemsBusinessUnit(gemsBusinessUnitId);
			masterService.removeGemsBusinessUnit(gemsBusinessUnit);
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

	private Map<String, Object> getModelMapBusinessUnitList(List<GemsBusinessUnit> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsBusinessUnit.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsBusinessUnit)) {
					return new JSONObject(true);
				}

				GemsBusinessUnit gemsBusinessUnit = (GemsBusinessUnit) bean;

				return new JSONObject().element("gemsBusinessUnitId", gemsBusinessUnit.getGemsBusinessUnitId())
						.element("gemsBuCode", gemsBusinessUnit.getGemsBuCode())
						.element("gemsBuDescription", gemsBusinessUnit.getGemsBuDescription())
						.element("activeStatus", gemsBusinessUnit.getActiveStatus());
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
