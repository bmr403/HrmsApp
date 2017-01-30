package eProject.web.employee;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
import eProject.domain.employee.GemsEmployeeLeaveLine;
import eProject.domain.employee.GemsEmployeeLeaveMaster;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmplyeeLeaveSummary;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsRoleTransactionApproval;
import eProject.domain.master.GemsTransactionApprovalMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.mail.MailService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsEmployeeLeaveMasterService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private LeaveManagementService leaveManagementService;

	@Autowired
	private MailService mailService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeLeaveMasterService.class);

	@RequestMapping(value = "/employee/viewEmployeeLeaveList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeLeaveList(HttpServletRequest request) {

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

			GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = new GemsEmployeeLeaveMaster();

			if ((loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.HR)) || (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE))) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");

				gemsEmployeeLeaveMaster.setGemsEmployeeMaster(userEmployee);
			}

			int totalResults = employeeService.getGemsEmployeeLeaveMasterFilterCount(gemsEmployeeLeaveMaster);
			List<GemsEmployeeLeaveMaster> list = employeeService.getGemsEmployeeLeaveMasterList(start, limit,
					gemsEmployeeLeaveMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeLeaveList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/viewEmployeeLeaveHistoryList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeLeaveHistoryList(HttpServletRequest request) {

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

			GemsEmployeeLeaveLine gemsEmployeeLeaveLine = new GemsEmployeeLeaveLine();

			String selectedGemsEmployeeLeaveId = request.getParameter("selectedGemsEmployeeLeaveId");
			gemsEmployeeLeaveLine.setGemsEmployeeLeaveMaster(
					employeeService.getGemsEmployeeLeaveMaster(Integer.parseInt(selectedGemsEmployeeLeaveId)));

			int totalResults = employeeService.getGemsEmployeeLeaveLineFilterCount(gemsEmployeeLeaveLine);
			List<GemsEmployeeLeaveLine> list = employeeService.getGemsEmployeeLeaveLineList(start, limit,
					gemsEmployeeLeaveLine);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeLeaveHistoryList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/viewEmployeeLeaveApprovalList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeLeaveApprovalList(HttpServletRequest request) {

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

			GemsEmployeeLeaveLine gemsEmployeeLeaveLine = new GemsEmployeeLeaveLine();

			if ((loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE))
					|| (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.HR))) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");

				gemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.IN_PROGRESS);
				gemsEmployeeLeaveLine.setApprover(userEmployee);
			}

			int totalResults = employeeService.getGemsEmployeeLeaveLineFilterCount(gemsEmployeeLeaveLine);
			List<GemsEmployeeLeaveLine> list = employeeService.getGemsEmployeeLeaveLineList(start, limit,
					gemsEmployeeLeaveLine);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeLeaveApprovalList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmployeeLeave", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmployeeLeave(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = new GemsEmployeeLeaveMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			String leaveCode = "";
			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsEmployeeLeaveMaster.setGemsEmployeeMaster(userEmployee);
				leaveCode = "" + userEmployee.getEmployeeLastName() + " - ";
			}

			gemsEmployeeLeaveMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeLeaveMaster.setUpdatedOn(todayDate);
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveId")))) {
				id_value = request.getParameter("gemsEmployeeLeaveId");
				gemsEmployeeLeaveMaster = employeeService.getGemsEmployeeLeaveMaster(Integer.parseInt(id_value));
			} else {
				gemsEmployeeLeaveMaster.setCreatedOn(todayDate);
				gemsEmployeeLeaveMaster.setCreatedBy(loggedInUser.getCreatedBy());

			}

			if ((StringUtils.isNotBlank(request.getParameter("leave_submit_type")))
					|| (StringUtils.isNotEmpty(request.getParameter("leave_submit_type")))) {
				gemsEmployeeLeaveMaster.setCurrentStatus(request.getParameter("leave_submit_type"));
			} else {
				gemsEmployeeLeaveMaster.setCurrentStatus("Draft");
			}

			String fromDateString = request.getParameter("fromDate");
			if ((StringUtils.isNotBlank(fromDateString)) || (StringUtils.isNotEmpty(fromDateString))) {
				Date fromDate = formatter.parse(fromDateString);
				gemsEmployeeLeaveMaster.setFromDate(fromDate);
				leaveCode = "" + leaveCode + fromDateString + " - ";
			}

			String toDateString = request.getParameter("toDate");
			if ((StringUtils.isNotBlank(toDateString)) || (StringUtils.isNotEmpty(toDateString))) {
				Date toDate = formatter.parse(toDateString);
				gemsEmployeeLeaveMaster.setToDate(toDate);
				leaveCode = "" + leaveCode + toDateString + "";
			}

			String gemsEmployeeCompOffFromDateString = request.getParameter("gemsEmployeeCompOffFrom");
			if ((StringUtils.isNotBlank(gemsEmployeeCompOffFromDateString))
					|| (StringUtils.isNotEmpty(gemsEmployeeCompOffFromDateString))) {
				Date gemsEmployeeCompOffFromDate = formatter.parse(gemsEmployeeCompOffFromDateString);
				gemsEmployeeLeaveMaster.setGemsEmployeeCompOffFrom(gemsEmployeeCompOffFromDate);
			}

			String gemsEmployeeCompOffToDateString = request.getParameter("gemsEmployeeCompOffTo");
			if ((StringUtils.isNotBlank(gemsEmployeeCompOffToDateString))
					|| (StringUtils.isNotEmpty(gemsEmployeeCompOffToDateString))) {
				Date gemsEmployeeCompOffToDate = formatter.parse(gemsEmployeeCompOffToDateString);
				gemsEmployeeLeaveMaster.setGemsEmployeeCompOffTo(gemsEmployeeCompOffToDate);
			}

			gemsEmployeeLeaveMaster.setLeaveCode(leaveCode);

			String employeeDutyResumeDateString = request.getParameter("employeeDutyResumeDate");
			if ((StringUtils.isNotBlank(employeeDutyResumeDateString))
					|| (StringUtils.isNotEmpty(employeeDutyResumeDateString))) {
				Date employeeDutyResumeDate = formatter.parse(employeeDutyResumeDateString);
				gemsEmployeeLeaveMaster.setEmployeeDutyResumeDate(employeeDutyResumeDate);
			}

			String addressDuringLeave = request.getParameter("addressDuringLeave");
			if ((StringUtils.isNotBlank(addressDuringLeave)) || (StringUtils.isNotEmpty(addressDuringLeave))) {
				gemsEmployeeLeaveMaster.setAddressDuringLeave(addressDuringLeave);
			}

			String contactNumber = request.getParameter("contactNumber");
			if ((StringUtils.isNotBlank(contactNumber)) || (StringUtils.isNotEmpty(contactNumber))) {
				gemsEmployeeLeaveMaster.setContactNumber(contactNumber);
			}

			String reasonForLeave = request.getParameter("reasonForLeave");
			if ((StringUtils.isNotBlank(reasonForLeave)) || (StringUtils.isNotEmpty(reasonForLeave))) {
				gemsEmployeeLeaveMaster.setReasonForLeave(reasonForLeave);
			}

			int gemsEmployeeLeaveSummaryId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_leavesummarytype")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_leavesummarytype")))) {
				try {
					gemsEmployeeLeaveSummaryId = Integer.parseInt(request.getParameter("selected_leavesummarytype"));

					gemsEmployeeLeaveMaster.setGemsEmplyeeLeaveSummary(
							employeeService.getGemsEmplyeeLeaveSummary(gemsEmployeeLeaveSummaryId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsEmployeeLeaveMaster
							.setGemsEmplyeeLeaveSummary(gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary());
				}
			}

			gemsEmployeeLeaveMaster.setActiveStatus(1);
			employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);

			if (gemsEmployeeLeaveMaster.getCurrentStatus().equalsIgnoreCase(ConstantVariables.IN_PROGRESS)) {
				GemsEmployeeLeaveLine gemsEmployeeLeaveAppliedLine = new GemsEmployeeLeaveLine();
				gemsEmployeeLeaveAppliedLine.setLineDescription(ConstantVariables.APPLIED);
				gemsEmployeeLeaveAppliedLine.setLineComments(ConstantVariables.APPLIED);
				gemsEmployeeLeaveAppliedLine.setActiveStatus(1);
				gemsEmployeeLeaveAppliedLine.setApprovedStatus(ConstantVariables.APPLIED);
				gemsEmployeeLeaveAppliedLine.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeLeaveAppliedLine.setCreatedOn(todayDate);
				gemsEmployeeLeaveAppliedLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeLeaveAppliedLine.setUpdatedOn(todayDate);
				gemsEmployeeLeaveAppliedLine.setGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
				gemsEmployeeLeaveAppliedLine.setCurrentLeavel(0);
				employeeService.saveGemsEmployeeLeaveLine(gemsEmployeeLeaveAppliedLine);
				gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.IN_PROGRESS);
				employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);

				GemsEmployeeLeaveLine gemsEmployeeLeaveLine = new GemsEmployeeLeaveLine();
				gemsEmployeeLeaveLine.setLineDescription(ConstantVariables.IN_PROGRESS);
				gemsEmployeeLeaveLine.setLineComments(ConstantVariables.IN_PROGRESS);
				gemsEmployeeLeaveLine.setActiveStatus(1);
				gemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.IN_PROGRESS);
				gemsEmployeeLeaveLine.setGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);

				gemsEmployeeLeaveLine = this.updateGemsEmployeeLeaveLine(1, gemsEmployeeLeaveLine, loggedInUser);

				gemsEmployeeLeaveLine.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeLeaveLine.setCreatedOn(todayDate);
				gemsEmployeeLeaveLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeLeaveLine.setUpdatedOn(todayDate);

				employeeService.saveGemsEmployeeLeaveLine(gemsEmployeeLeaveLine);
				gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.IN_PROGRESS);
				employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);

				GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = employeeService.getGemsEmplyeeLeaveSummary(
						gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsEmployeeLeaveSummaryId());
				int leave_duration_days = 0;
				if (gemsEmployeeLeaveMaster.getToDate().compareTo(gemsEmployeeLeaveMaster.getFromDate()) == 0) {
					leave_duration_days = 1;
				} else {
					leave_duration_days = (int) ((gemsEmployeeLeaveMaster.getToDate().getTime()
							- gemsEmployeeLeaveMaster.getFromDate().getTime()) / (1000 * 60 * 60 * 24))+1;
				}
				if (gemsEmployeeLeaveMaster.getFromDate().before(todayDate)) {
					if (gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster().getLeaveTypeCode()
							.equalsIgnoreCase("CO")) {
						gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
						gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
						double leaveBalance = 0.0;
						gemsEmplyeeLeaveSummary.setLeaveBalance(leaveBalance);
					} else {
						gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
						gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
						double leaveBalance = gemsEmplyeeLeaveSummary.getLeaveBalance() - leave_duration_days;
						gemsEmplyeeLeaveSummary.setLeaveBalance(leaveBalance);
					}

				} else {
					gemsEmplyeeLeaveSummary.setLeaveScheduled(leave_duration_days);
				}
				employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

				String firstName = "";
				if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster() != null) {
					firstName = "" + gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeFirstName() + " "
							+ gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getEmployeeLastName() + "";
				}
				String approver = "";
				if (gemsEmployeeLeaveLine.getApprover() != null) {
					approver = "" + gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() + " "
							+ gemsEmployeeLeaveLine.getApprover().getEmployeeLastName() + "";
				}

				try {
					Properties properties_mail = new Properties();
					String propFileName_mail = "/properties/mail.properties";
					InputStream stream_mail = getClass().getClassLoader().getResourceAsStream(propFileName_mail);
					properties_mail.load(stream_mail);

					String scheme = request.getScheme();
					String serverName = request.getServerName();
					int port = request.getServerPort();
					String contextpath = request.getContextPath();
					String appURL = scheme + "://" + serverName + ":" + port + contextpath;

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("from", properties_mail.getProperty("javaMailSender.username"));
					map.put("to", gemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getGemsUserMaster().getUserName());
					map.put("subject", "Leave Application Submitted for Approval");
					map.put("firstName", firstName);
					map.put("approver", approver);
					map.put("appURL", appURL);
					mailService.leaveApplicationMail(map);

					Map<String, Object> approvalmap = new HashMap<String, Object>();
					approvalmap.put("from", properties_mail.getProperty("javaMailSender.username"));
					approvalmap.put("to", gemsEmployeeLeaveLine.getApprover().getGemsUserMaster().getUserName());
					approvalmap.put("subject", "Leave Application Submitted for Approval");
					approvalmap.put("firstName", firstName);
					approvalmap.put("approver", approver);
					approvalmap.put("appURL", appURL);
					mailService.leaveApprovalMail(approvalmap);
				} catch (Exception ex) {

				}

			}

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

	@RequestMapping(value = "/employee/deleteGemsEmployeeLeave", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmployeeLeave(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeLeaveId = 0;

		if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveId")))
				|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveId")))) {
			gemsEmployeeLeaveId = Integer.parseInt(request.getParameter("gemsEmployeeLeaveId"));
		}
		try {
			GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = employeeService
					.getGemsEmployeeLeaveMaster(gemsEmployeeLeaveId);

			GemsEmployeeLeaveLine searchGemsEmployeeLeaveLine = new GemsEmployeeLeaveLine();
			searchGemsEmployeeLeaveLine.setGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
			List<GemsEmployeeLeaveLine> gemsEmployeeLeaveLines = employeeService
					.getAllGemsEmployeeLeaveLineList(searchGemsEmployeeLeaveLine);
			for (GemsEmployeeLeaveLine gemsEmployeeLeaveLine : gemsEmployeeLeaveLines) {

				employeeService.removeGemsEmployeeLeaveLine(gemsEmployeeLeaveLine);
			}

			employeeService.removeGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
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

	@RequestMapping(value = "/employee/getEmployeeLeaveById", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEmployeeLeaveById(HttpServletRequest request) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeLeaveId = 0;

		if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveId")))
				|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveId")))) {
			gemsEmployeeLeaveId = Integer.parseInt(request.getParameter("gemsEmployeeLeaveId"));
		}
		try {
			GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = employeeService
					.getGemsEmployeeLeaveMaster(gemsEmployeeLeaveId);
			return getModelMapEmployeeLeaveDetail(gemsEmployeeLeaveMaster);

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	public GemsEmployeeLeaveLine updateGemsEmployeeLeaveLine(int currentLevel,
			GemsEmployeeLeaveLine gemsEmployeeLeaveLine, GemsUserMaster loggedInUser) {
		GemsTransactionApprovalMaster gemsTransactionApprovalMaster = new GemsTransactionApprovalMaster();
		gemsTransactionApprovalMaster.setTransactionCode("LR");
		gemsTransactionApprovalMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		gemsTransactionApprovalMaster = masterService
				.getGemsTransactionApprovalMasterByCode(gemsTransactionApprovalMaster);
		int total_approval_levels = gemsTransactionApprovalMaster.getApprovalLevels();

		boolean continueFlag = true;
		for (int i = currentLevel; i <= total_approval_levels; i++) {

			GemsRoleTransactionApproval gemsRoleTransactionApproval = new GemsRoleTransactionApproval();
			gemsRoleTransactionApproval.setGemsTransactionApprovalMaster(gemsTransactionApprovalMaster);
			gemsRoleTransactionApproval.setApprovalLevel(i);
			gemsRoleTransactionApproval.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			gemsRoleTransactionApproval = masterService
					.getGemsRoleTransactionApprovalByRoleTransaction(gemsRoleTransactionApproval);

			if (gemsRoleTransactionApproval != null) {
				GemsRoleMaster approverRole = gemsRoleTransactionApproval.getGemsRoleMaster();
				if (approverRole.getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
					GemsEmployeeLeaveLine maxLeaveLine = new GemsEmployeeLeaveLine();
					maxLeaveLine.setGemsEmployeeLeaveMaster(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster());
					maxLeaveLine = employeeService.getMaxEmployeeLeaveLine(maxLeaveLine);
					if (maxLeaveLine != null) {
						if (maxLeaveLine.getApprover() != null) {
							GemsEmployeeMaster approverEmp = maxLeaveLine.getApprover().getCurrentReportingTo();
							if (approverEmp != null) {
								gemsEmployeeLeaveLine.setCurrentLeavel(i);
								gemsEmployeeLeaveLine.setApprover(approverEmp);
								continueFlag = false;
								break;
							}
						} else {
							GemsEmployeeMaster approverEmp = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
									.getGemsEmployeeMaster().getCurrentReportingTo();
							if (approverEmp != null) {
								gemsEmployeeLeaveLine.setCurrentLeavel(i);
								gemsEmployeeLeaveLine.setApprover(approverEmp);
								continueFlag = false;
								break;
							}
						}
					} else {
						GemsEmployeeMaster approverEmp = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
								.getGemsEmployeeMaster().getCurrentReportingTo();
						if (approverEmp != null) {
							gemsEmployeeLeaveLine.setCurrentLeavel(i);
							gemsEmployeeLeaveLine.setApprover(approverEmp);
							continueFlag = false;
							break;
						}
					}
				} else if (approverRole.getRoleCode().equalsIgnoreCase(ConstantVariables.HR)) {
					GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
					gemsRoleMaster.setRoleCode("HR");
					gemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					gemsRoleMaster = masterService.getGemsRoleMasterByCode(gemsRoleMaster);

					if (gemsRoleMaster != null) {
						GemsUserMaster gemsUserMaster = new GemsUserMaster();
						gemsUserMaster.setGemsRoleMaster(gemsRoleMaster);
						gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
						gemsUserMaster = masterService.getGemsUserMasterByCode(gemsUserMaster);

						if (gemsUserMaster != null) {
							GemsEmployeeMaster hrEmployeeMaster = new GemsEmployeeMaster();
							hrEmployeeMaster.setGemsUserMaster(gemsUserMaster);
							hrEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							hrEmployeeMaster = employeeService.getGemsEmployeeMasterByUser(hrEmployeeMaster);

							if (hrEmployeeMaster != null) {
								gemsEmployeeLeaveLine.setApprover(hrEmployeeMaster);
								gemsEmployeeLeaveLine.setCurrentLeavel(i);
								continueFlag = false;
								break;
							}
						}

					}

				}

			}

		}
		if (continueFlag == true) {
			gemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.APPROVED);
			gemsEmployeeLeaveLine.setCurrentLeavel(total_approval_levels);

		}

		return gemsEmployeeLeaveLine;

	}

	@RequestMapping(value = "/employee/getEmployeeLeaveLineById", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEmployeeLeaveByLineId(HttpServletRequest request) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeLeaveLineId = 0;

		if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveLineId")))
				|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveLineId")))) {
			gemsEmployeeLeaveLineId = Integer.parseInt(request.getParameter("gemsEmployeeLeaveLineId"));
		}
		try {
			GemsEmployeeLeaveLine gemsEmployeeLeaveLine = employeeService
					.getGemsEmployeeLeaveLine(gemsEmployeeLeaveLineId);
			return getModelMapEmployeeLeaveLineDetail(gemsEmployeeLeaveLine);

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/approveGemsEmployeeLeave", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> approveGemsEmployeeLeave(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			Properties properties_mail = new Properties();
			String propFileName_mail = "/properties/mail.properties";
			InputStream stream_mail = getClass().getClassLoader().getResourceAsStream(propFileName_mail);
			properties_mail.load(stream_mail);

			GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = new GemsEmployeeLeaveMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			GemsTransactionApprovalMaster gemsTransactionApprovalMaster = new GemsTransactionApprovalMaster();
			gemsTransactionApprovalMaster.setTransactionCode("LR");
			gemsTransactionApprovalMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			gemsTransactionApprovalMaster = masterService
					.getGemsTransactionApprovalMasterByCode(gemsTransactionApprovalMaster);
			int total_approval_levels = gemsTransactionApprovalMaster.getApprovalLevels();

			String leaveCode = "";
			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsEmployeeLeaveMaster.setGemsEmployeeMaster(userEmployee);

			}

			gemsEmployeeLeaveMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeLeaveMaster.setUpdatedOn(todayDate);
			String id_value1 = "";
			String approvedStatus = request.getParameter("approvedStatus");
			String lineComments = request.getParameter("approver_remarks");

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsEmployeeLeaveId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsEmployeeLeaveId")))) {
				id_value = request.getParameter("selectedGemsEmployeeLeaveId");
				gemsEmployeeLeaveMaster = employeeService.getGemsEmployeeLeaveMaster(Integer.parseInt(id_value));
			}

			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeLeaveLineId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeLeaveLineId")))) {
				id_value1 = request.getParameter("gemsEmployeeLeaveLineId");
				GemsEmployeeLeaveLine currentGemsEmployeeLeaveLine = employeeService
						.getGemsEmployeeLeaveLine(Integer.parseInt(id_value1));
				currentGemsEmployeeLeaveLine.setLineComments(lineComments);
				currentGemsEmployeeLeaveLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
				currentGemsEmployeeLeaveLine.setUpdatedOn(todayDate);
				if (approvedStatus.equalsIgnoreCase(ConstantVariables.APPROVED)) {
					currentGemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.APPROVED);
					currentGemsEmployeeLeaveLine.setLineDescription(ConstantVariables.APPROVED);
				} else {
					currentGemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.REJECTED);
					currentGemsEmployeeLeaveLine.setLineDescription(ConstantVariables.REJECTED);
					gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.REJECTED);
				}
				employeeService.saveGemsEmployeeLeaveLine(currentGemsEmployeeLeaveLine);

				String firstName = "";
				if (currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster() != null) {
					firstName = ""
							+ currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
									.getEmployeeFirstName()
							+ " " + currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
									.getEmployeeLastName()
							+ "";
				}
				String approver = "";
				if (currentGemsEmployeeLeaveLine.getApprover() != null) {
					approver = "" + currentGemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() + " "
							+ currentGemsEmployeeLeaveLine.getApprover().getEmployeeLastName() + "";
				}
				String scheme = request.getScheme();
				String serverName = request.getServerName();
				int port = request.getServerPort();
				String contextpath = request.getContextPath();
				String appURL = scheme + "://" + serverName + ":" + port + contextpath;
				Map<String, Object> map = new HashMap<String, Object>();

				if (approvedStatus.equalsIgnoreCase(ConstantVariables.APPROVED)) {
					if (currentGemsEmployeeLeaveLine.getCurrentLeavel() == total_approval_levels) {
						gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.APPROVED);
						employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
						GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = employeeService.getGemsEmplyeeLeaveSummary(
								gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsEmployeeLeaveSummaryId());
						int leave_duration_days = 0;
						if (gemsEmployeeLeaveMaster.getToDate().compareTo(gemsEmployeeLeaveMaster.getFromDate()) == 0) {
							leave_duration_days = 1;
						} else {
							leave_duration_days = (int) ((gemsEmployeeLeaveMaster.getToDate().getTime()
									- gemsEmployeeLeaveMaster.getFromDate().getTime()) / (1000 * 60 * 60 * 24))+1;
						}
						if (gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
								.getLeaveTypeCode().equalsIgnoreCase("CO")) {
							double balance = 0.0;
							double lopDays = 0.0;
							gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
							gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
							// leaveBalance =
							// gemsEmplyeeLeaveSummary.getLeaveEntitled()-((double)leave_duration_days);
							gemsEmplyeeLeaveSummary.setLeaveBalance(balance);
							gemsEmplyeeLeaveSummary.setLopDays(lopDays);
						} else {
							double leaveBalance = gemsEmplyeeLeaveSummary.getLeaveBalance();
							double balance = leaveBalance - leave_duration_days;
							double lopDays = 0.0;
							if (balance < 0) {
								lopDays = leave_duration_days - leaveBalance;
								balance = 0.0;
							}
							gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
							gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
							// leaveBalance =
							// gemsEmplyeeLeaveSummary.getLeaveEntitled()-((double)leave_duration_days);
							gemsEmplyeeLeaveSummary.setLeaveBalance(balance);
							gemsEmplyeeLeaveSummary.setLopDays(lopDays);
						}

						employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

					} else {
						int currentLevel = currentGemsEmployeeLeaveLine.getCurrentLeavel();
						currentLevel++;
						GemsEmployeeLeaveLine gemsEmployeeLeaveLine = new GemsEmployeeLeaveLine();
						gemsEmployeeLeaveLine.setLineDescription(ConstantVariables.IN_PROGRESS);
						gemsEmployeeLeaveLine.setLineComments(ConstantVariables.IN_PROGRESS);
						gemsEmployeeLeaveLine.setActiveStatus(1);
						gemsEmployeeLeaveLine.setApprovedStatus(ConstantVariables.IN_PROGRESS);

						gemsEmployeeLeaveLine = this.updateGemsEmployeeLeaveLine(currentLevel, gemsEmployeeLeaveLine,
								loggedInUser);

						if (gemsEmployeeLeaveLine.getApprovedStatus().equalsIgnoreCase(ConstantVariables.APPROVED)) {
							gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.IN_PROGRESS);
							employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
							GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = employeeService
									.getGemsEmplyeeLeaveSummary(gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary()
											.getGemsEmployeeLeaveSummaryId());
							int leave_duration_days = 0;
							if (gemsEmployeeLeaveMaster.getToDate()
									.compareTo(gemsEmployeeLeaveMaster.getFromDate()) == 0) {
								leave_duration_days = 1;
							} else {
								leave_duration_days = (int) ((gemsEmployeeLeaveMaster.getToDate().getTime()
										- gemsEmployeeLeaveMaster.getFromDate().getTime()) / (1000 * 60 * 60 * 24))+1;
							}
							if (gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
									.getLeaveTypeCode().equalsIgnoreCase("CO")) {
								double balance = 0.0;
								double lopDays = 0.0;

								gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
								gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
								// double leaveBalance =
								// gemsEmplyeeLeaveSummary.getLeaveEntitled()-((double)leave_duration_days);
								gemsEmplyeeLeaveSummary.setLeaveBalance(balance);
								gemsEmplyeeLeaveSummary.setLopDays(lopDays);
							} else {
								double leaveBalance = gemsEmplyeeLeaveSummary.getLeaveBalance();
								double balance = leaveBalance - leave_duration_days;
								double lopDays = 0.0;
								if (balance < 0) {
									lopDays = leave_duration_days - leaveBalance;
									balance = 0.0;
								}
								gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
								gemsEmplyeeLeaveSummary.setLeaveTaken(leave_duration_days);
								// double leaveBalance =
								// gemsEmplyeeLeaveSummary.getLeaveEntitled()-((double)leave_duration_days);
								gemsEmplyeeLeaveSummary.setLeaveBalance(balance);
								gemsEmplyeeLeaveSummary.setLopDays(lopDays);
							}

							employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

							String toMailfirstName = "";
							if (currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster() != null) {
								firstName = ""
										+ currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
												.getGemsEmployeeMaster().getEmployeeFirstName()
										+ " " + currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
												.getGemsEmployeeMaster().getEmployeeLastName()
										+ "";
							}
							String toApprover = "";
							if (currentGemsEmployeeLeaveLine.getApprover() != null) {
								approver = "" + currentGemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() + " "
										+ currentGemsEmployeeLeaveLine.getApprover().getEmployeeLastName() + "";
							}
							Map<String, Object> approvalmap = new HashMap<String, Object>();
							approvalmap.put("from", properties_mail.getProperty("javaMailSender.username"));
							approvalmap.put("to",
									currentGemsEmployeeLeaveLine.getApprover().getGemsUserMaster().getUserName());
							approvalmap.put("subject", "Leave Application Submitted for Approval");
							approvalmap.put("firstName", toMailfirstName);
							approvalmap.put("approver", toApprover);
							approvalmap.put("appURL", appURL);
							mailService.leaveApprovalMail(approvalmap);

						} else {
							gemsEmployeeLeaveLine.setCreatedBy(loggedInUser.getGemsUserMasterId());
							gemsEmployeeLeaveLine.setCreatedOn(todayDate);
							gemsEmployeeLeaveLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
							gemsEmployeeLeaveLine.setUpdatedOn(todayDate);
							gemsEmployeeLeaveLine.setGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
							employeeService.saveGemsEmployeeLeaveLine(gemsEmployeeLeaveLine);
							gemsEmployeeLeaveMaster.setCurrentStatus(ConstantVariables.IN_PROGRESS);
							employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
						}

					}

				} else {
					employeeService.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
					GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = employeeService
							.getGemsEmplyeeLeaveSummary(gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary()
									.getGemsEmployeeLeaveSummaryId());
					double totalleaveScheduled = gemsEmplyeeLeaveSummary.getLeaveScheduled();
					
					int leave_duration_days = 0;
					if (gemsEmployeeLeaveMaster.getToDate().compareTo(gemsEmployeeLeaveMaster.getFromDate()) == 0) {
						leave_duration_days = 1;
					} else {
						leave_duration_days = (int) ((gemsEmployeeLeaveMaster.getToDate().getTime()
								- gemsEmployeeLeaveMaster.getFromDate().getTime()) / (1000 * 60 * 60 * 24))+1;
					}
					
					
					double leaveScheduled = 0.0;
					if (totalleaveScheduled >= leave_duration_days)
					{
						leaveScheduled= totalleaveScheduled - leave_duration_days;
					}
					else
					{
						leaveScheduled = 0.0;
					}
					gemsEmplyeeLeaveSummary.setLeaveScheduled(leaveScheduled);
					employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
					
					
				}

				map.put("from", properties_mail.getProperty("javaMailSender.username"));
				map.put("to", currentGemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
						.getGemsUserMaster().getUserName());
				map.put("subject", "Leave Application Submitted for Approval");
				map.put("firstName", firstName);
				map.put("approver", approver);
				map.put("status", currentGemsEmployeeLeaveLine.getApprovedStatus());
				map.put("appURL", appURL);
				mailService.leaveApplicationStatusMail(map);

			}

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", ConstantVariables.UPDATED_SUCCESS);
			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapEmployeeLeaveDetail(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeLeaveMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeLeaveMaster)) {
					return new JSONObject(true);
				}

				GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = (GemsEmployeeLeaveMaster) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				if (gemsEmployeeLeaveMaster.getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeLeaveMaster.getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeLeaveMaster.getToDate() != null) {
					toDate = sdf.format(gemsEmployeeLeaveMaster.getToDate());
				}

				String employeeDutyResumeDate = "";
				if (gemsEmployeeLeaveMaster.getEmployeeDutyResumeDate() != null) {
					employeeDutyResumeDate = sdf.format(gemsEmployeeLeaveMaster.getEmployeeDutyResumeDate());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeLeaveMaster.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				int selectedGemsEmployeeLeaveSummaryId = 0;
				String selected_leavesummarytype = "";
				String selected_leavetype = "";

				if (gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary() != null) {
					selectedGemsEmployeeLeaveSummaryId = gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary()
							.getGemsEmployeeLeaveSummaryId();
					selected_leavesummarytype = ""
							+ gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
									.getLeaveTypeDescription()
							+ "  (" + gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getLeaveBalance() + ")";
					selected_leavetype = gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
							.getLeaveTypeDescription();
				}

				String gemsEmployeeCompOffFrom = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeCompOffFrom() != null) {
					gemsEmployeeCompOffFrom = sdf.format(gemsEmployeeLeaveMaster.getGemsEmployeeCompOffFrom());
				}

				String gemsEmployeeCompOffTo = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeCompOffTo() != null) {
					gemsEmployeeCompOffTo = sdf.format(gemsEmployeeLeaveMaster.getGemsEmployeeCompOffTo());
				}

				return new JSONObject().element("gemsEmployeeLeaveId", gemsEmployeeLeaveMaster.getGemsEmployeeLeaveId())
						.element("leaveCode", gemsEmployeeLeaveMaster.getLeaveCode()).element("fromDate", fromDate)
						.element("toDate", toDate).element("gemsEmployeeCompOffFrom", gemsEmployeeCompOffFrom)
						.element("gemsEmployeeCompOffTo", gemsEmployeeCompOffTo)
						.element("employeeDutyResumeDate", employeeDutyResumeDate)
						.element("reasonForLeave", gemsEmployeeLeaveMaster.getReasonForLeave())
						.element("addressDuringLeave", gemsEmployeeLeaveMaster.getAddressDuringLeave())
						.element("contactNumber", gemsEmployeeLeaveMaster.getContactNumber())
						.element("currentStatus", gemsEmployeeLeaveMaster.getCurrentStatus())
						.element("activeStatus", gemsEmployeeLeaveMaster.getActiveStatus())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("selectedGemsEmployeeLeaveSummaryId", selectedGemsEmployeeLeaveSummaryId)
						.element("selected_leavesummarytype", selected_leavesummarytype)
						.element("selected_leavetype", selected_leavetype);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeLeaveMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeLeaveLineDetail(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeLeaveLine.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeLeaveLine)) {
					return new JSONObject(true);
				}

				GemsEmployeeLeaveLine gemsEmployeeLeaveLine = (GemsEmployeeLeaveLine) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String statusDate = "";
				if (gemsEmployeeLeaveLine.getCreatedOn() != null) {
					statusDate = sdf.format(gemsEmployeeLeaveLine.getUpdatedOn());
				}

				int selectedApproverId = 0;
				String selectedApproverName = "";
				if (gemsEmployeeLeaveLine.getApprover() != null) {
					selectedApproverId = gemsEmployeeLeaveLine.getApprover().getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() != null) {
						selectedApproverName = selectedApproverName
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveLine.getApprover().getEmployeeLastName() != null) {
						selectedApproverName = selectedApproverName + " "
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeLastName();
					}
				}

				String fromDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getToDate() != null) {
					toDate = sdf.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getToDate());
				}

				String employeeDutyResumeDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getEmployeeDutyResumeDate() != null) {
					employeeDutyResumeDate = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getEmployeeDutyResumeDate());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
							.getGemsEmployeeMaster().getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
							.getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName + gemsEmployeeLeaveLine
								.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
							.getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName + gemsEmployeeLeaveLine
								.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				int selectedGemsEmployeeLeaveSummaryId = 0;
				String selected_leavesummarytype = "";
				String selected_leavetype = "";

				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary() != null) {
					selectedGemsEmployeeLeaveSummaryId = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
							.getGemsEmplyeeLeaveSummary().getGemsEmployeeLeaveSummaryId();
					selected_leavesummarytype = ""
							+ gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
									.getGemsLeaveTypeMaster().getLeaveTypeDescription()
							+ "  (" + gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
									.getLeaveBalance()
							+ ")";
					selected_leavetype = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
							.getGemsLeaveTypeMaster().getLeaveTypeDescription();
				}
				String gemsEmployeeCompOffFrom = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffFrom() != null) {
					gemsEmployeeCompOffFrom = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffFrom());
				}

				String gemsEmployeeCompOffTo = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffTo() != null) {
					gemsEmployeeCompOffTo = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffTo());
				}

				return new JSONObject()
						.element("gemsEmployeeLeaveLineId", gemsEmployeeLeaveLine.getGemsEmployeeLeaveLineId())
						.element("approvedStatus", gemsEmployeeLeaveLine.getApprovedStatus())
						.element("currentLeavel", gemsEmployeeLeaveLine.getCurrentLeavel())
						.element("statusDate", statusDate)
						.element("lineComments", gemsEmployeeLeaveLine.getLineComments())
						.element("lineDescription", gemsEmployeeLeaveLine.getLineDescription())
						.element("selectedApproverId", selectedApproverId)
						.element("selectedApproverName", selectedApproverName)
						.element("selectedGemsEmployeeLeaveId",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeLeaveId())
						.element("leaveCode", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getLeaveCode())
						.element("fromDate", fromDate).element("toDate", toDate)
						.element("gemsEmployeeCompOffFrom", gemsEmployeeCompOffFrom)
						.element("gemsEmployeeCompOffTo", gemsEmployeeCompOffTo)
						.element("employeeDutyResumeDate", employeeDutyResumeDate)
						.element("reasonForLeave",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getReasonForLeave())
						.element("addressDuringLeave",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getAddressDuringLeave())
						.element("contactNumber", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getContactNumber())
						.element("currentStatus", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getCurrentStatus())
						.element("activeStatus", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getActiveStatus())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("selectedGemsEmployeeLeaveSummaryId", selectedGemsEmployeeLeaveSummaryId)
						.element("selected_leavesummarytype", selected_leavesummarytype)
						.element("selected_leavetype", selected_leavetype);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeLeaveLine, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeLeaveList(List<GemsEmployeeLeaveMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeLeaveMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeLeaveMaster)) {
					return new JSONObject(true);
				}

				GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster = (GemsEmployeeLeaveMaster) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				if (gemsEmployeeLeaveMaster.getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeLeaveMaster.getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeLeaveMaster.getToDate() != null) {
					toDate = sdf.format(gemsEmployeeLeaveMaster.getToDate());
				}

				String gemsEmployeeCompOffFrom = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeCompOffFrom() != null) {
					gemsEmployeeCompOffFrom = sdf.format(gemsEmployeeLeaveMaster.getGemsEmployeeCompOffFrom());
				}

				String gemsEmployeeCompOffTo = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeCompOffTo() != null) {
					gemsEmployeeCompOffTo = sdf.format(gemsEmployeeLeaveMaster.getGemsEmployeeCompOffTo());
				}

				String employeeDutyResumeDate = "";
				if (gemsEmployeeLeaveMaster.getEmployeeDutyResumeDate() != null) {
					employeeDutyResumeDate = sdf.format(gemsEmployeeLeaveMaster.getEmployeeDutyResumeDate());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeLeaveMaster.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeLeaveMaster.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				int selectedGemsEmployeeLeaveSummaryId = 0;
				String selected_leavesummarytype = "";
				String selected_leavetype = "";

				if (gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary() != null) {
					selectedGemsEmployeeLeaveSummaryId = gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary()
							.getGemsEmployeeLeaveSummaryId();
					selected_leavesummarytype = ""
							+ gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
									.getLeaveTypeDescription()
							+ "  (" + gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getLeaveBalance() + ")";
					selected_leavetype = gemsEmployeeLeaveMaster.getGemsEmplyeeLeaveSummary().getGemsLeaveTypeMaster()
							.getLeaveTypeDescription();
				}

				return new JSONObject().element("gemsEmployeeLeaveId", gemsEmployeeLeaveMaster.getGemsEmployeeLeaveId())
						.element("leaveCode", gemsEmployeeLeaveMaster.getLeaveCode())
						.element("gemsEmployeeCompOffFrom", gemsEmployeeCompOffFrom)
						.element("gemsEmployeeCompOffTo", gemsEmployeeCompOffTo).element("fromDate", fromDate)
						.element("toDate", toDate).element("employeeDutyResumeDate", employeeDutyResumeDate)
						.element("reasonForLeave", gemsEmployeeLeaveMaster.getReasonForLeave())
						.element("addressDuringLeave", gemsEmployeeLeaveMaster.getAddressDuringLeave())
						.element("contactNumber", gemsEmployeeLeaveMaster.getContactNumber())
						.element("currentStatus", gemsEmployeeLeaveMaster.getCurrentStatus())
						.element("activeStatus", gemsEmployeeLeaveMaster.getActiveStatus())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("selectedGemsEmployeeLeaveSummaryId", selectedGemsEmployeeLeaveSummaryId)
						.element("selected_leavesummarytype", selected_leavesummarytype)
						.element("selected_leavetype", selected_leavetype)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeLeaveApprovalList(List<GemsEmployeeLeaveLine> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeLeaveLine.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeLeaveLine)) {
					return new JSONObject(true);
				}

				GemsEmployeeLeaveLine gemsEmployeeLeaveLine = (GemsEmployeeLeaveLine) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String statusDate = "";
				if (gemsEmployeeLeaveLine.getCreatedOn() != null) {
					statusDate = sdf.format(gemsEmployeeLeaveLine.getUpdatedOn());
				}

				int selectedApproverId = 0;
				String selectedApproverName = "";
				if (gemsEmployeeLeaveLine.getApprover() != null) {
					selectedApproverId = gemsEmployeeLeaveLine.getApprover().getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() != null) {
						selectedApproverName = selectedApproverName
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveLine.getApprover().getEmployeeLastName() != null) {
						selectedApproverName = selectedApproverName + " "
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeLastName();
					}
				}

				String fromDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getToDate() != null) {
					toDate = sdf.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getToDate());
				}

				String employeeDutyResumeDate = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getEmployeeDutyResumeDate() != null) {
					employeeDutyResumeDate = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getEmployeeDutyResumeDate());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
							.getGemsEmployeeMaster().getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
							.getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName + gemsEmployeeLeaveLine
								.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster()
							.getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName + gemsEmployeeLeaveLine
								.getGemsEmployeeLeaveMaster().getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				int selectedGemsEmployeeLeaveSummaryId = 0;
				String selected_leavesummarytype = "";
				String selected_leavetype = "";

				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary() != null) {
					selectedGemsEmployeeLeaveSummaryId = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()
							.getGemsEmplyeeLeaveSummary().getGemsEmployeeLeaveSummaryId();
					selected_leavesummarytype = ""
							+ gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
									.getGemsLeaveTypeMaster().getLeaveTypeDescription()
							+ "  (" + gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
									.getLeaveBalance()
							+ ")";
					selected_leavetype = gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmplyeeLeaveSummary()
							.getGemsLeaveTypeMaster().getLeaveTypeDescription();
				}

				String gemsEmployeeCompOffFrom = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffFrom() != null) {
					gemsEmployeeCompOffFrom = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffFrom());
				}

				String gemsEmployeeCompOffTo = "";
				if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffTo() != null) {
					gemsEmployeeCompOffTo = sdf
							.format(gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeCompOffTo());
				}

				return new JSONObject()
						.element("gemsEmployeeLeaveLineId", gemsEmployeeLeaveLine.getGemsEmployeeLeaveLineId())
						.element("approvedStatus", gemsEmployeeLeaveLine.getApprovedStatus())
						.element("currentLeavel", gemsEmployeeLeaveLine.getCurrentLeavel())
						.element("statusDate", statusDate)
						.element("lineComments", gemsEmployeeLeaveLine.getLineComments())
						.element("lineDescription", gemsEmployeeLeaveLine.getLineDescription())
						.element("selectedApproverId", selectedApproverId)
						.element("selectedApproverName", selectedApproverName)
						.element("selectedGemsEmployeeLeaveId",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeLeaveId())
						.element("leaveCode", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getLeaveCode())
						.element("fromDate", fromDate).element("toDate", toDate)
						.element("gemsEmployeeCompOffFrom", gemsEmployeeCompOffFrom)
						.element("gemsEmployeeCompOffTo", gemsEmployeeCompOffTo)
						.element("duration", "" + fromDate + "-" + toDate + "")
						.element("employeeDutyResumeDate", employeeDutyResumeDate)
						.element("reasonForLeave",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getReasonForLeave())
						.element("addressDuringLeave",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getAddressDuringLeave())
						.element("contactNumber", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getContactNumber())
						.element("currentStatus", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getCurrentStatus())
						.element("activeStatus", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getActiveStatus())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("selectedGemsEmployeeLeaveSummaryId", selectedGemsEmployeeLeaveSummaryId)
						.element("selected_leavesummarytype", selected_leavesummarytype)
						.element("selected_leavetype", selected_leavetype);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeLeaveHistoryList(List<GemsEmployeeLeaveLine> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeLeaveLine.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeLeaveLine)) {
					return new JSONObject(true);
				}

				GemsEmployeeLeaveLine gemsEmployeeLeaveLine = (GemsEmployeeLeaveLine) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String statusDate = "";
				if (gemsEmployeeLeaveLine.getCreatedOn() != null) {
					statusDate = sdf.format(gemsEmployeeLeaveLine.getCreatedOn());
				}

				int selectedApproverId = 0;
				String selectedApproverName = "";
				if (gemsEmployeeLeaveLine.getApprover() != null) {
					selectedApproverId = gemsEmployeeLeaveLine.getApprover().getGemsEmployeeMasterId();

					if (gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName() != null) {
						selectedApproverName = selectedApproverName
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeFirstName();
					}
					if (gemsEmployeeLeaveLine.getApprover().getEmployeeLastName() != null) {
						selectedApproverName = selectedApproverName + " "
								+ gemsEmployeeLeaveLine.getApprover().getEmployeeLastName();
					}
				}

				return new JSONObject()
						.element("gemsEmployeeLeaveLineId", gemsEmployeeLeaveLine.getGemsEmployeeLeaveLineId())
						.element("approvedStatus", gemsEmployeeLeaveLine.getApprovedStatus())
						.element("statusDate", statusDate)
						.element("lineComments", gemsEmployeeLeaveLine.getLineComments())
						.element("lineDescription", gemsEmployeeLeaveLine.getLineDescription())
						.element("selectedApproverId", selectedApproverId)
						.element("selectedApproverName", selectedApproverName).element("selectedGemsEmployeeLeaveId",
								gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster().getGemsEmployeeLeaveId());
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
