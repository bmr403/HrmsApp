package eProject.web.leavemanagement;

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

import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsEducationMaster;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsUserMaster;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.master.MasterService;

@Controller
public class GemsLeavePeriodMasterService {
	@Autowired
	private LeaveManagementService leaveManagementService;

	protected final Log logger = LogFactory.getLog(GemsLeavePeriodMasterService.class);

	@RequestMapping(value = "/leavemanagement/viewGemsLeavePeriodMasterList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsLeavePeriodMasterList(HttpServletRequest request) {

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

			GemsLeavePeriodMaster gemsLeavePeriodMaster = new GemsLeavePeriodMaster();

			gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = leaveManagementService.getGemsLeavePeriodMasterFilterCount(gemsLeavePeriodMaster);
			List<GemsLeavePeriodMaster> list = leaveManagementService.getGemsLeavePeriodMasterList(start, limit,
					gemsLeavePeriodMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapLeavePeriodList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/leavemanagement/saveGemsLeavePeriodMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsLeavePeriodMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsLeavePeriodMaster gemsLeavePeriodMaster = new GemsLeavePeriodMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsLeavePeriodMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsLeavePeriodMaster.setUpdatedOn(todayDate);
			gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsLeavePeriodMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsLeavePeriodMasterId")))) {
				id_value = request.getParameter("gemsLeavePeriodMasterId");
				gemsLeavePeriodMaster = leaveManagementService.getGemsLeavePeriodMaster(Integer.parseInt(id_value));
			} else {
				gemsLeavePeriodMaster.setCreatedOn(todayDate);
				gemsLeavePeriodMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String fromDateString = request.getParameter("fromDate");
			if ((StringUtils.isNotBlank(fromDateString)) || (StringUtils.isNotEmpty(fromDateString))) {
				Date fromDate = formatter.parse(fromDateString);
				gemsLeavePeriodMaster.setFromDate(fromDate);
			}
			String toDateString = request.getParameter("toDate");
			if ((StringUtils.isNotBlank(toDateString)) || (StringUtils.isNotEmpty(toDateString))) {
				Date toDate = formatter.parse(toDateString);
				gemsLeavePeriodMaster.setToDate(toDate);
			}

			String isCurrent = request.getParameter("isCurrent");
			if ((isCurrent == "on") || (isCurrent.equalsIgnoreCase("on"))) {
				gemsLeavePeriodMaster.setIsCurrent(1);
			} else {
				gemsLeavePeriodMaster.setIsCurrent(0);
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsLeavePeriodMaster.setActiveStatus(1);
			} else {
				gemsLeavePeriodMaster.setActiveStatus(0);
			}

			leaveManagementService.saveGemsLeavePeriodMaster(gemsLeavePeriodMaster);
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

	@RequestMapping(value = "/leavemanagement/deleteGemsLeavePeriodMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsLeavePeriodMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsLeavePeriodMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsLeavePeriodMaster gemsLeavePeriodMaster = leaveManagementService
					.getGemsLeavePeriodMaster(gemsLeavePeriodMasterId);
			leaveManagementService.removeGemsLeavePeriodMaster(gemsLeavePeriodMaster);
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

	private Map<String, Object> getModelMapLeavePeriodList(List<GemsLeavePeriodMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsLeavePeriodMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsLeavePeriodMaster)) {
					return new JSONObject(true);
				}

				GemsLeavePeriodMaster gemsLeavePeriodMaster = (GemsLeavePeriodMaster) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				String fromDateString = "";
				String toDateString = "";
				if (gemsLeavePeriodMaster.getFromDate() != null) {
					fromDateString = sdf.format(gemsLeavePeriodMaster.getFromDate());
				}
				if (gemsLeavePeriodMaster.getToDate() != null) {
					toDateString = sdf.format(gemsLeavePeriodMaster.getToDate());
				}

				return new JSONObject()
						.element("gemsLeavePeriodMasterId", gemsLeavePeriodMaster.getGemsLeavePeriodMasterId())
						.element("fromDate", fromDateString).element("toDate", toDateString)
						.element("isCurrent", gemsLeavePeriodMaster.getIsCurrent())
						.element("activeStatus", gemsLeavePeriodMaster.getActiveStatus());
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
