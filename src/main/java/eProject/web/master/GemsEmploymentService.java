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
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsEmploymentService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmploymentService.class);

	@RequestMapping(value = "/master/checkEmploymentStatusByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkEmploymentStatusByCode(HttpServletRequest request) {

		String statusCode = request.getParameter("statusCode");

		GemsEmploymentStatus gemsEmploymentStatus = new GemsEmploymentStatus();
		gemsEmploymentStatus.setStatusCode(statusCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsEmploymentStatus.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsEmploymentStatus returnedGemsEmploymentStatus = masterService
				.getGemsEmploymentStatusByCode(gemsEmploymentStatus);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsEmploymentStatus.getStatusCode()))
				|| (StringUtils.isNotEmpty(returnedGemsEmploymentStatus.getStatusCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewEmploymentStatusList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmploymentStatusList(HttpServletRequest request) {

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

			GemsEmploymentStatus gemsEmploymentStatus = new GemsEmploymentStatus();

			String searchStatusCode = request.getParameter("searchStatusCode");
			if (searchStatusCode != null && searchStatusCode.isEmpty() == false) {
				gemsEmploymentStatus.setStatusCode(searchStatusCode);
			}

			String searchStatusDescription = request.getParameter("searchStatusDescription");
			if (searchStatusDescription != null && searchStatusDescription.isEmpty() == false) {
				gemsEmploymentStatus.setStatusDescription(searchStatusDescription);
			}
			gemsEmploymentStatus.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsEmploymentStatusFilterCount(gemsEmploymentStatus);
			List<GemsEmploymentStatus> list = masterService.getGemsEmploymentStatusList(start, limit,
					gemsEmploymentStatus);

			logger.info("Returned list size" + list.size());

			return getModelMapEmploymentStatusList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveEmploymentStatus", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmploymentStatus(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmploymentStatus gemsEmploymentStatus = new GemsEmploymentStatus();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEmploymentStatus.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmploymentStatus.setUpdatedOn(todayDate);
			gemsEmploymentStatus.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmploymentStatusId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmploymentStatusId")))) {
				id_value = request.getParameter("gemsEmploymentStatusId");
				gemsEmploymentStatus = masterService.getGemsEmploymentStatus(Integer.parseInt(id_value));
			} else {
				gemsEmploymentStatus.setCreatedOn(todayDate);
				gemsEmploymentStatus.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String statusCode = request.getParameter("statusCode");
			gemsEmploymentStatus.setStatusCode(statusCode);
			String statusDescription = request.getParameter("statusDescription");
			gemsEmploymentStatus.setStatusDescription(statusDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmploymentStatus.setActiveStatus(1);
			} else {
				gemsEmploymentStatus.setActiveStatus(0);
			}

			masterService.saveGemsEmploymentStatus(gemsEmploymentStatus);
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

	@RequestMapping(value = "/master/deleteEmploymentStatus", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteEmploymentStatus(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmploymentStatusId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmploymentStatus gemsEmploymentStatus = masterService.getGemsEmploymentStatus(gemsEmploymentStatusId);
			masterService.removeGemsEmploymentStatus(gemsEmploymentStatus);
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

	private Map<String, Object> getModelMapEmploymentStatusList(List<GemsEmploymentStatus> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmploymentStatus.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmploymentStatus)) {
					return new JSONObject(true);
				}

				GemsEmploymentStatus gemsEmploymentStatus = (GemsEmploymentStatus) bean;

				return new JSONObject()
						.element("gemsEmploymentStatusId", gemsEmploymentStatus.getGemsEmploymentStatusId())
						.element("statusCode", gemsEmploymentStatus.getStatusCode())
						.element("statusDescription", gemsEmploymentStatus.getStatusDescription())
						.element("statusCodeDescription",
								"" + gemsEmploymentStatus.getStatusCode() + " - "
										+ gemsEmploymentStatus.getStatusDescription() + "")
						.element("activeStatus", gemsEmploymentStatus.getActiveStatus());
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
