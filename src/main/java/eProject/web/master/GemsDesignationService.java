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
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsDesignationService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsDesignationService.class);

	@RequestMapping(value = "/master/checkDesignationByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDesignationByCode(HttpServletRequest request) {

		String code = request.getParameter("gemsDesignationCode");

		GemsDesignation gemsDesignation = new GemsDesignation();
		gemsDesignation.setGemsDesignationCode(code);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsDesignation.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsDesignation returnedGemsDesignation = masterService.getGemsDesignationByCode(gemsDesignation);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsDesignation.getGemsDesignationCode()))
				|| (StringUtils.isNotEmpty(returnedGemsDesignation.getGemsDesignationCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewDesignationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewDesignationList(HttpServletRequest request) {

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

			GemsDesignation gemsDesignation = new GemsDesignation();

			String searchCode = request.getParameter("searchDesignationCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsDesignation.setGemsDesignationCode(searchCode);
			}

			String searchName = request.getParameter("searchDesignationName");
			if (searchName != null && searchName.isEmpty() == false) {
				gemsDesignation.setGemsDesignationName(searchName);
			}
			gemsDesignation.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsDesignationFilterCount(gemsDesignation);
			List<GemsDesignation> list = masterService.getGemsDesignationList(start, limit, gemsDesignation);

			logger.info("Returned list size" + list.size());

			return getModelMapDesignationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveDesignation", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDesignation(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsDesignation gemsDesignation = new GemsDesignation();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsDesignation.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsDesignation.setUpdatedOn(todayDate);
			gemsDesignation.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsDesignationId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsDesignationId")))) {
				id_value = request.getParameter("gemsDesignationId");
				gemsDesignation = masterService.getGemsDesignation(Integer.parseInt(id_value));
			} else {
				gemsDesignation.setCreatedOn(todayDate);
				gemsDesignation.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String gemsDesignationCode = request.getParameter("gemsDesignationCode");
			gemsDesignation.setGemsDesignationCode(gemsDesignationCode);

			String gemsDesignationName = request.getParameter("gemsDesignationName");
			gemsDesignation.setGemsDesignationName(gemsDesignationName);

			String gemsDesignationDescription = request.getParameter("gemsDesignationDescription");
			gemsDesignation.setGemsDesignationDescription(gemsDesignationDescription);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsDesignation.setActiveStatus(1);
			} else {
				gemsDesignation.setActiveStatus(0);
			}

			masterService.saveGemsDesignation(gemsDesignation);
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

	@RequestMapping(value = "/master/deleteDesignation", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteDesignation(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsDesignationId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsDesignation gemsDesignation = masterService.getGemsDesignation(gemsDesignationId);
			masterService.removeGemsDesignation(gemsDesignation);
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

	private Map<String, Object> getModelMapDesignationList(List<GemsDesignation> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsDesignation.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsDesignation)) {
					return new JSONObject(true);
				}

				GemsDesignation gemsDesignation = (GemsDesignation) bean;

				return new JSONObject().element("gemsDesignationId", gemsDesignation.getGemsDesignationId())
						.element("gemsDesignationCode", gemsDesignation.getGemsDesignationCode())
						.element("gemsDesignationName", gemsDesignation.getGemsDesignationName())
						.element("gemsDesignationCodeName",
								"" + gemsDesignation.getGemsDesignationCode() + " - "
										+ gemsDesignation.getGemsDesignationName() + "")
						.element("gemsDesignationDescription", gemsDesignation.getGemsDesignationDescription())
						.element("activeStatus", gemsDesignation.getActiveStatus());
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
