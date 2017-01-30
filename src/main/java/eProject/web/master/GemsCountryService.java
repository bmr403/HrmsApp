package eProject.web.master;

import java.text.ParseException;
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
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsCountryService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCountryService.class);

	@RequestMapping(value = "/master/checkCountryByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkCountryByCode(HttpServletRequest request) {

		String gemsCountryCode = request.getParameter("gemsCountryCode");

		GemsCountryMaster gemsCountryMaster = new GemsCountryMaster();
		gemsCountryMaster.setGemsCountryCode(gemsCountryCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsCountryMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsCountryMaster returnedGemsCountryMaster = masterService.getGemsCountryMasterByCode(gemsCountryMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsCountryMaster.getGemsCountryCode()))
				|| (StringUtils.isNotEmpty(returnedGemsCountryMaster.getGemsCountryCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewCountryList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCountryList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 10;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsCountryMaster gemsCountryMaster = new GemsCountryMaster();

			String searchCode = request.getParameter("searchCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsCountryMaster.setGemsCountryCode(searchCode);
			}

			String searchDescription = request.getParameter("searchDescription");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsCountryMaster.setGemsCountryDescription(searchDescription);
			}
			gemsCountryMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsCountryMasterListFilterCount(gemsCountryMaster);
			List<GemsCountryMaster> list = masterService.getGemsCountryMasterList(start, limit, gemsCountryMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapCountryList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveGemsCountryMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsCountryMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCountryMaster gemsCountryMaster = new GemsCountryMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsCountryMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsCountryMaster.setUpdatedOn(todayDate);
			gemsCountryMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsCountryMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCountryMasterId")))) {
				id_value = request.getParameter("gemsCountryMasterId");
				gemsCountryMaster = masterService.getGemsCountryMaster(Integer.parseInt(id_value));

			} else {
				gemsCountryMaster.setCreatedOn(todayDate);
				gemsCountryMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String gemsCountryCode = request.getParameter("gemsCountryCode");
			gemsCountryMaster.setGemsCountryCode(gemsCountryCode);
			String gemsCountryDescription = request.getParameter("gemsCountryDescription");
			gemsCountryMaster.setGemsCountryDescription(gemsCountryDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCountryMaster.setActiveStatus(1);
			} else {
				gemsCountryMaster.setActiveStatus(0);
			}

			masterService.saveGemsCountryMaster(gemsCountryMaster);
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

	@RequestMapping(value = "/master/deleteGemsCountryMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsCountryMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCountryMasterId = Integer.parseInt(request.getParameter("gemsCountryMasterId"));
		try {
			GemsCountryMaster gemsCountryMaster = masterService.getGemsCountryMaster(gemsCountryMasterId);
			masterService.removeGemsCountryMaster(gemsCountryMaster);
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

	@RequestMapping(value = "/country/getCountryInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsCountryMasterId_Str = request.getParameter("gemsCountryMasterId");
		GemsCountryMaster gemsCountryMaster = new GemsCountryMaster();
		try {
			if (gemsCountryMasterId_Str != null) {
				gemsCountryMaster = masterService.getGemsCountryMaster(Integer.parseInt(gemsCountryMasterId_Str));

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapCountryInfo(gemsCountryMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapCountryList(List<GemsCountryMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCountryMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCountryMaster)) {
					return new JSONObject(true);
				}

				GemsCountryMaster gemsCountryMaster = (GemsCountryMaster) bean;
				SimpleDateFormat importDateFormat = new SimpleDateFormat("MM/dd/yyyy");

				String lastModifiedString = "";
				if (gemsCountryMaster.getUpdatedOn() != null) {
					lastModifiedString = importDateFormat.format(gemsCountryMaster.getUpdatedOn());

				} else {
					lastModifiedString = importDateFormat.format(gemsCountryMaster.getCreatedOn());
				}

				return new JSONObject().element("gemsCountryMasterId", gemsCountryMaster.getGemsCountryMasterId())
						.element("gemsCountryCode", gemsCountryMaster.getGemsCountryCode())
						.element("gemsCountryDescription", gemsCountryMaster.getGemsCountryDescription())
						.element("lastModifiedString", lastModifiedString)
						.element("activeStatus", gemsCountryMaster.getActiveStatus());
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

	// JSon Construction
	private Map<String, Object> getModelMapCountryInfo(GemsCountryMaster gemsCountryMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCountryMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCountryMaster)) {
					return new JSONObject(true);
				}
				GemsCountryMaster gemsCountryMaster = (GemsCountryMaster) bean;

				return new JSONObject().element("gemsCountryMasterId", gemsCountryMaster.getGemsCountryMasterId())
						.element("gemsCountryCode", gemsCountryMaster.getGemsCountryCode())
						.element("gemsCountryDescription", gemsCountryMaster.getGemsCountryDescription())
						.element("activeStatus", gemsCountryMaster.getActiveStatus());

			}
		});

		JSON json = JSONSerializer.toJSON(gemsCountryMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}
}
