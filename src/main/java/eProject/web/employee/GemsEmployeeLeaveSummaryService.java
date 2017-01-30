package eProject.web.employee;

import java.text.SimpleDateFormat;
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

import eProject.domain.employee.GemsEmpEducationDetail;
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmplyeeLeaveSummary;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsEmployeeLeaveSummaryService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private LeaveManagementService leaveManagementService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeLeaveSummaryService.class);

	@RequestMapping(value = "/employee/viewEmployeeLeaveSummaryList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeLeaveSummaryList(HttpServletRequest request) {

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

			GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();

			String searchEmpCode = request.getParameter("selectedGemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmplyeeLeaveSummary
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(userEmployee);
				GemsLeavePeriodMaster gemsLeavePeriodMaster = new GemsLeavePeriodMaster();
				gemsLeavePeriodMaster.setIsCurrent(1);
				gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
				GemsLeavePeriodMaster activeGemsLeavePeriodMaster = leaveManagementService
						.getActiveGemsLeavePeriodMaster(gemsLeavePeriodMaster);
				gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(activeGemsLeavePeriodMaster);

			}

			String searchLeavePeriod = request.getParameter("searchLeavePeriod");
			if (searchLeavePeriod != null && searchLeavePeriod.isEmpty() == false) {
				gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
						leaveManagementService.getGemsLeavePeriodMaster(Integer.parseInt(searchLeavePeriod)));
			}

			gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			int totalResults = employeeService.getGemsEmplyeeLeaveSummaryFilterCount(gemsEmplyeeLeaveSummary);
			List<GemsEmplyeeLeaveSummary> list = employeeService.getGemsEmplyeeLeaveSummaryList(start, limit,
					gemsEmplyeeLeaveSummary);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeLeaveSummaryList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmplyeeLeaveSummary", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmplyeeLeaveSummary(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsEmployeeMasterId")))) {
				gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("selectedGemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveSummaryId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveSummaryId")))) {
				id_value = request.getParameter("gemsEmployeeLeaveSummaryId");
				gemsEmplyeeLeaveSummary = employeeService.getGemsEmplyeeLeaveSummary(Integer.parseInt(id_value));
			} else {
				gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
				gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String leaveEntitled = request.getParameter("leaveEntitled");
			gemsEmplyeeLeaveSummary.setLeaveEntitled(Integer.parseInt(leaveEntitled));

			String leaveScheduled = request.getParameter("leaveScheduled");
			gemsEmplyeeLeaveSummary.setLeaveScheduled(Integer.parseInt(leaveScheduled));

			String leaveTaken = request.getParameter("leaveTaken");
			gemsEmplyeeLeaveSummary.setLeaveTaken(Integer.parseInt(leaveTaken));

			String leaveBalance = request.getParameter("leaveBalance");
			gemsEmplyeeLeaveSummary.setLeaveBalance(Integer.parseInt(leaveBalance));

			gemsEmplyeeLeaveSummary.setLopDays(0.0);

			int gemsLeavePeriodMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_leaveperiod")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_leaveperiod")))) {
				try {
					gemsLeavePeriodMasterId = Integer.parseInt(request.getParameter("selected_leaveperiod"));

					gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
							leaveManagementService.getGemsLeavePeriodMaster(gemsLeavePeriodMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsEmplyeeLeaveSummary
							.setGemsLeavePeriodMaster(gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster());
				}
			}

			int gemsLeaveTypeMasterId = 0;
			if ((StringUtils.isNotBlank(request.getParameter("selected_leavetype")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_leavetype")))) {
				try {
					gemsLeaveTypeMasterId = Integer.parseInt(request.getParameter("selected_leavetype"));

					gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(
							leaveManagementService.getGemsLeaveTypeMaster(gemsLeaveTypeMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmplyeeLeaveSummary.setActiveStatus(1);
			} else {
				gemsEmplyeeLeaveSummary.setActiveStatus(0);
			}

			employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
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

	@RequestMapping(value = "/employee/getGemsLeaveSummaryByEmployee", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getGemsLeaveSummaryByEmployee(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
				"userEmployee");
		try {
			GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
			gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);

			List<GemsEmplyeeLeaveSummary> employeeLeaveSummaryList = employeeService
					.getAllLeaveSummaryByEmployee(gemsEmplyeeLeaveSummary);
			return getModelMapEmployeeLeaveSummaryList(employeeLeaveSummaryList, employeeLeaveSummaryList.size());

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/deleteGemsEmplyeeLeaveSummary", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmplyeeLeaveSummary(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeLeaveSummaryId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = employeeService
					.getGemsEmplyeeLeaveSummary(gemsEmployeeLeaveSummaryId);
			employeeService.removeGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
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

	private Map<String, Object> getModelMapEmployeeLeaveSummaryList(List<GemsEmplyeeLeaveSummary> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmplyeeLeaveSummary.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmplyeeLeaveSummary)) {
					return new JSONObject(true);
				}

				GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = (GemsEmplyeeLeaveSummary) bean;

				int selectedGemsLeaveTypeMasterId = 0;
				String selected_leavetype = "";
				String leaveTypeCode = "";
				if (gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster() != null) {
					selectedGemsLeaveTypeMasterId = gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster()
							.getGemsLeaveTypeMasterId();
					selected_leavetype = "" + gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster().getLeaveTypeCode()
							+ " - " + gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster().getLeaveTypeDescription() + "";
					leaveTypeCode = gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster().getLeaveTypeCode();

				}

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				String toDate = "";

				if (gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster().getFromDate() != null) {
					fromDate = sdf.format(gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster().getFromDate());
				}

				if (gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster().getToDate() != null) {
					toDate = sdf.format(gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster().getToDate());
				}

				int selectedGemsLeavePeriodMasterId = 0;
				String selected_leaveperiod = "";
				if (gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster() != null) {
					selectedGemsLeavePeriodMasterId = gemsEmplyeeLeaveSummary.getGemsLeavePeriodMaster()
							.getGemsLeavePeriodMasterId();
					selected_leaveperiod = "" + fromDate + " - " + toDate + "";

				}
				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				String employeeStatus = "N/A";
				if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmplyeeLeaveSummary.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeLastName();
					}
					if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getCurrentEmployeeStatus() != null) {
						employeeStatus = "" + gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getCurrentEmployeeStatus()
								.getStatusDescription();
					}

				}
				return new JSONObject()
						.element("gemsEmployeeLeaveSummaryId", gemsEmplyeeLeaveSummary.getGemsEmployeeLeaveSummaryId())
						.element("leaveEntitled", gemsEmplyeeLeaveSummary.getLeaveEntitled())
						.element("leaveScheduled", gemsEmplyeeLeaveSummary.getLeaveScheduled())
						.element("leaveTaken", gemsEmplyeeLeaveSummary.getLeaveTaken())
						.element("leaveBalance", gemsEmplyeeLeaveSummary.getLeaveBalance())
						.element("lopDays", gemsEmplyeeLeaveSummary.getLopDays())
						.element("selectedGemsLeavePeriodMasterId", selectedGemsLeavePeriodMasterId)
						.element("selected_leaveperiod", selected_leaveperiod)
						.element("selectedGemsLeaveTypeMasterId", selectedGemsLeaveTypeMasterId)
						.element("selected_leavetype", selected_leavetype)
						.element("activeStatus", gemsEmplyeeLeaveSummary.getActiveStatus())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("employeeStatus", employeeStatus).element("leaveTypeCode", leaveTypeCode)
						.element("leaveSummaryCodeBalance",
								"" + gemsEmplyeeLeaveSummary.getGemsLeaveTypeMaster().getLeaveTypeDescription() + " ("
										+ gemsEmplyeeLeaveSummary.getLeaveBalance() + ")")
						.element("selectedGemsEmployeeMasterName",
								"" + selectedGemsEmployeeMasterName + " - " + employeeStatus + "");
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
