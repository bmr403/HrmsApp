package eProject.web.leavemanagement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.master.MasterService;

@Controller
public class GemsLeaveManagementService {
	@Autowired
	private LeaveManagementService leaveManagementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsLeaveManagementService.class);

	@RequestMapping(value = "/leavemanagement/checkLeaveTypeByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkLeaveTypeByCode(HttpServletRequest request) {

		String leaveTypeCode = request.getParameter("leaveTypeCode");

		GemsLeaveTypeMaster gemsLeaveTypeMaster = new GemsLeaveTypeMaster();
		gemsLeaveTypeMaster.setLeaveTypeCode(leaveTypeCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsLeaveTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsLeaveTypeMaster returnedGemsLeaveTypeMaster = leaveManagementService
				.getGemsLeaveTypeMasterByCode(gemsLeaveTypeMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsLeaveTypeMaster.getLeaveTypeCode()))
				|| (StringUtils.isNotEmpty(returnedGemsLeaveTypeMaster.getLeaveTypeCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/leavemanagement/viewLeaveTypeList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewLeaveTypeList(HttpServletRequest request) {

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

			GemsLeaveTypeMaster gemsLeaveTypeMaster = new GemsLeaveTypeMaster();

			String searchLeaveTypeCode = request.getParameter("searchLeaveTypeCode");
			if (searchLeaveTypeCode != null && searchLeaveTypeCode.isEmpty() == false) {
				gemsLeaveTypeMaster.setLeaveTypeCode(searchLeaveTypeCode);
			}

			String searchLeaveTypeDescription = request.getParameter("searchLeaveTypeDescription");
			if (searchLeaveTypeDescription != null && searchLeaveTypeDescription.isEmpty() == false) {
				gemsLeaveTypeMaster.setLeaveTypeDescription(searchLeaveTypeDescription);
			}
			gemsLeaveTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = leaveManagementService.getGemsLeaveTypeMasterFilterCount(gemsLeaveTypeMaster);
			List<GemsLeaveTypeMaster> list = leaveManagementService.getGemsLeaveTypeMasterList(start, limit,
					gemsLeaveTypeMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapLeaveTypeList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/leavemanagement/viewLeaveTypeListNotAttachedToEmpStatus", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewLeaveTypeListNotAttachedToEmpStatus(HttpServletRequest request) {

		try {

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			GemsLeaveTypeMaster gemsLeaveTypeMaster = new GemsLeaveTypeMaster();
			gemsLeaveTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			List<GemsLeaveTypeMaster> list = leaveManagementService.getAllLeaveTypeList(gemsLeaveTypeMaster);

			LeaveSummayMaster leaveSummayMaster = new LeaveSummayMaster();
			leaveSummayMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			String searchEmployeeStatusCode = request.getParameter("searchEmployeeStatusCode");
			if (searchEmployeeStatusCode != null && searchEmployeeStatusCode.isEmpty() == false) {
				leaveSummayMaster.setGemsEmploymentStatus(
						masterService.getGemsEmploymentStatus(Integer.parseInt(searchEmployeeStatusCode)));
			}

			List<LeaveSummayMaster> gemsLeaveSummaryMasterList = leaveManagementService
					.getAllLeaveSummayMasteList(leaveSummayMaster);

			List<Integer> leaveTypeListFromSummary = new ArrayList<Integer>();

			for (LeaveSummayMaster leaveSummayMasterObject : gemsLeaveSummaryMasterList) {
				leaveTypeListFromSummary
						.add(leaveSummayMasterObject.getGemsLeaveTypeMaster().getGemsLeaveTypeMasterId());
			}

			List<GemsLeaveTypeMaster> filteredLeaveTypeMasterList = new ArrayList<GemsLeaveTypeMaster>();

			for (GemsLeaveTypeMaster gemsLeaveTypeMasterObject : list) {
				if (!(leaveTypeListFromSummary.contains(gemsLeaveTypeMasterObject.getGemsLeaveTypeMasterId()))) {
					filteredLeaveTypeMasterList.add(gemsLeaveTypeMasterObject);
				}
			}

			logger.info("Returned list size" + filteredLeaveTypeMasterList.size());

			return getModelMapLeaveTypeList(filteredLeaveTypeMasterList, filteredLeaveTypeMasterList.size());

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/leavemanagement/saveLeaveType", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveLeaveType(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsLeaveTypeMaster gemsLeaveTypeMaster = new GemsLeaveTypeMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsLeaveTypeMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsLeaveTypeMaster.setUpdatedOn(todayDate);
			gemsLeaveTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsLeaveTypeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsLeaveTypeMasterId")))) {
				id_value = request.getParameter("gemsLeaveTypeMasterId");
				gemsLeaveTypeMaster = leaveManagementService.getGemsLeaveTypeMaster(Integer.parseInt(id_value));
			} else {
				gemsLeaveTypeMaster.setCreatedOn(todayDate);
				gemsLeaveTypeMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String leaveTypeCode = request.getParameter("leaveTypeCode");
			gemsLeaveTypeMaster.setLeaveTypeCode(leaveTypeCode);

			String leaveTypeDescription = request.getParameter("leaveTypeDescription");
			gemsLeaveTypeMaster.setLeaveTypeDescription(leaveTypeDescription);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsLeaveTypeMaster.setActiveStatus(1);
			} else {
				gemsLeaveTypeMaster.setActiveStatus(0);
			}

			leaveManagementService.saveGemsLeaveTypeMaster(gemsLeaveTypeMaster);
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

	@RequestMapping(value = "/leavemanagement/deleteLeaveType", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteLeaveType(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsLeaveTypeMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsLeaveTypeMaster gemsLeaveTypeMaster = leaveManagementService
					.getGemsLeaveTypeMaster(gemsLeaveTypeMasterId);
			leaveManagementService.removeGemsLeaveTypeMaster(gemsLeaveTypeMaster);
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

	private Map<String, Object> getModelMapLeaveTypeList(List<GemsLeaveTypeMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsLeaveTypeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsLeaveTypeMaster)) {
					return new JSONObject(true);
				}

				GemsLeaveTypeMaster gemsLeaveTypeMaster = (GemsLeaveTypeMaster) bean;

				String activeStatus = "";
				if (gemsLeaveTypeMaster.getActiveStatus() == 1) {
					activeStatus = "Yes";
				} else {
					activeStatus = "No";
				}

				return new JSONObject().element("gemsLeaveTypeMasterId", gemsLeaveTypeMaster.getGemsLeaveTypeMasterId())
						.element("leaveTypeCode", gemsLeaveTypeMaster.getLeaveTypeCode())
						.element("leaveTypeCodeDescription",
								"" + gemsLeaveTypeMaster.getLeaveTypeCode() + " - "
										+ gemsLeaveTypeMaster.getLeaveTypeDescription() + "")
						.element("leaveTypeDescription", gemsLeaveTypeMaster.getLeaveTypeDescription())
						.element("activeStatus", activeStatus);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Leave Summary Methods
	 */
	@RequestMapping(value = "/leavemanagement/viewLeaveSummaryList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewLeaveSummaryList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

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

			LeaveSummayMaster leaveSummayMaster = new LeaveSummayMaster();
			leaveSummayMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			String searchEmployeeStatusCode = request.getParameter("searchEmployeeStatusCode");
			if (searchEmployeeStatusCode != null && searchEmployeeStatusCode.isEmpty() == false) {
				leaveSummayMaster.setGemsEmploymentStatus(
						masterService.getGemsEmploymentStatus(Integer.parseInt(searchEmployeeStatusCode)));
			}

			int totalResults = leaveManagementService.getLeaveSummayMasterFilterCount(leaveSummayMaster);
			List<LeaveSummayMaster> list = leaveManagementService.getLeaveSummayMasteList(start, limit,
					leaveSummayMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapLeaveSummaryList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/leavemanagement/saveLeaveSummary", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveLeaveSummary(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			LeaveSummayMaster leaveSummayMaster = new LeaveSummayMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			leaveSummayMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			leaveSummayMaster.setUpdatedOn(todayDate);
			leaveSummayMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("leaveSummayMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("leaveSummayMasterId")))) {
				id_value = request.getParameter("leaveSummayMasterId");
				leaveSummayMaster = leaveManagementService.getLeaveSummayMaster(Integer.parseInt(id_value));
			} else {
				leaveSummayMaster.setCreatedOn(todayDate);
				leaveSummayMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String totalDays = request.getParameter("totalDays");
			leaveSummayMaster.setTotalDays(Integer.parseInt(totalDays));

			int leaveTypeMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_leavetype")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_leavetype")))) {
				try {
					leaveTypeMasterId = Integer.parseInt(request.getParameter("selected_leavetype"));

					leaveSummayMaster
							.setGemsLeaveTypeMaster(leaveManagementService.getGemsLeaveTypeMaster(leaveTypeMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					leaveSummayMaster.setGemsLeaveTypeMaster(leaveSummayMaster.getGemsLeaveTypeMaster());
				}
			}

			int employeeStatusId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_employeestatus")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_employeestatus")))) {
				try {
					employeeStatusId = Integer.parseInt(request.getParameter("selected_employeestatus"));

					leaveSummayMaster.setGemsEmploymentStatus(masterService.getGemsEmploymentStatus(employeeStatusId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					leaveSummayMaster.setGemsEmploymentStatus(leaveSummayMaster.getGemsEmploymentStatus());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				leaveSummayMaster.setActiveStatus(1);
			} else {
				leaveSummayMaster.setActiveStatus(0);
			}

			leaveManagementService.saveLeaveSummayMaster(leaveSummayMaster);
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

	@RequestMapping(value = "/leavemanagement/getLeaveManagementInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getLeaveManagementInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String leaveSummayMasterIdString = request.getParameter("leaveSummayMasterId");

		LeaveSummayMaster returnedLeaveSummayMaster = new LeaveSummayMaster();

		try {
			if (leaveSummayMasterIdString != null) {

				Integer leaveSummayMasterId = new Integer(leaveSummayMasterIdString);

				returnedLeaveSummayMaster = leaveManagementService.getLeaveSummayMaster(leaveSummayMasterId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapLeaveSummayInfo(returnedLeaveSummayMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/leavemanagement/deleteLeaveSummary", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteLeaveSummary(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int leaveSummayMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			LeaveSummayMaster leaveSummayMaster = leaveManagementService.getLeaveSummayMaster(leaveSummayMasterId);
			leaveManagementService.removeLeaveSummayMaster(leaveSummayMaster);
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

	private Map<String, Object> getModelMapLeaveSummaryList(List<LeaveSummayMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(LeaveSummayMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof LeaveSummayMaster)) {
					return new JSONObject(true);
				}

				LeaveSummayMaster leaveSummayMaster = (LeaveSummayMaster) bean;

				int selectedGemsEmploymentStatusId = 0;
				String selected_employeestatus = "";
				if (leaveSummayMaster.getGemsEmploymentStatus() != null) {
					selectedGemsEmploymentStatusId = leaveSummayMaster.getGemsEmploymentStatus()
							.getGemsEmploymentStatusId();
					selected_employeestatus = "" + leaveSummayMaster.getGemsEmploymentStatus().getStatusCode() + " - "
							+ leaveSummayMaster.getGemsEmploymentStatus().getStatusDescription() + "";
				}

				int selectedGemsLeaveTypeMasterId = 0;
				String selected_leavetype = "";
				if (leaveSummayMaster.getGemsLeaveTypeMaster() != null) {
					selectedGemsLeaveTypeMasterId = leaveSummayMaster.getGemsLeaveTypeMaster()
							.getGemsLeaveTypeMasterId();
					selected_leavetype = "" + leaveSummayMaster.getGemsLeaveTypeMaster().getLeaveTypeCode() + " - "
							+ leaveSummayMaster.getGemsLeaveTypeMaster().getLeaveTypeDescription() + "";
				}

				return new JSONObject().element("leaveSummayMasterId", leaveSummayMaster.getLeaveSummayMasterId())
						.element("totalDays", leaveSummayMaster.getTotalDays())
						.element("selectedGemsEmploymentStatusId", selectedGemsEmploymentStatusId)
						.element("selected_employeestatus", selected_employeestatus)
						.element("selectedGemsLeaveTypeMasterId", selectedGemsLeaveTypeMasterId)
						.element("selected_leavetype", selected_leavetype)
						.element("activeStatus", leaveSummayMaster.getActiveStatus());
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

	private Map<String, Object> getModelMapLeaveSummayInfo(LeaveSummayMaster leaveSummayMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.registerJsonBeanProcessor(LeaveSummayMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof LeaveSummayMaster)) {
					return new JSONObject(true);
				}

				LeaveSummayMaster leaveSummayMaster = (LeaveSummayMaster) bean;

				int selectedGemsEmploymentStatusId = 0;
				String selected_employeestatus = "";
				if (leaveSummayMaster.getGemsEmploymentStatus() != null) {
					selectedGemsEmploymentStatusId = leaveSummayMaster.getGemsEmploymentStatus()
							.getGemsEmploymentStatusId();
					selected_employeestatus = "" + leaveSummayMaster.getGemsEmploymentStatus().getStatusCode() + " - "
							+ leaveSummayMaster.getGemsEmploymentStatus().getStatusDescription() + "";
				}

				int selectedGemsLeaveTypeMasterId = 0;
				String selected_leavetype = "";
				if (leaveSummayMaster.getGemsLeaveTypeMaster() != null) {
					selectedGemsLeaveTypeMasterId = leaveSummayMaster.getGemsLeaveTypeMaster()
							.getGemsLeaveTypeMasterId();
					selected_leavetype = "" + leaveSummayMaster.getGemsLeaveTypeMaster().getLeaveTypeCode() + " - "
							+ leaveSummayMaster.getGemsLeaveTypeMaster().getLeaveTypeDescription() + "";
				}

				return new JSONObject().element("leaveSummayMasterId", leaveSummayMaster.getLeaveSummayMasterId())
						.element("totalDays", leaveSummayMaster.getTotalDays())
						.element("selectedGemsEmploymentStatusId", selectedGemsEmploymentStatusId)
						.element("selected_employeestatus", selected_employeestatus)
						.element("selectedGemsLeaveTypeMasterId", selectedGemsLeaveTypeMasterId)
						.element("selected_leavetype", selected_leavetype)
						.element("activeStatus", leaveSummayMaster.getActiveStatus());
			}
		});

		JSON json = JSONSerializer.toJSON(leaveSummayMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

}
