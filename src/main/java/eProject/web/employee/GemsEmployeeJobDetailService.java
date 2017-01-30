package eProject.web.employee;

import org.springframework.stereotype.Controller;

@Controller
public class GemsEmployeeJobDetailService {
	/*
	 * @Autowired private EmployeeService employeeService;
	 * 
	 * @Autowired private MasterService masterService;
	 * 
	 * @Autowired private LeaveManagementService leaveManagementService;
	 * 
	 * protected final Log logger =
	 * LogFactory.getLog(GemsEmployeeJobDetailService.class);
	 * 
	 * 
	 * 
	 * @RequestMapping(value="/employee/viewEmployeeJobDetailList", method =
	 * RequestMethod.GET) public @ResponseBody Map<String, Object>
	 * viewEmployeeJobDetailList(HttpServletRequest request) {
	 * 
	 * 
	 * try{ int start = 0; int limit = 20;
	 * 
	 * GemsUserMaster loggedInUser = (GemsUserMaster)
	 * WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
	 * 
	 * String startValue = request.getParameter("start"); if (startValue != null
	 * && startValue.isEmpty() == false) { start =
	 * Integer.parseInt(request.getParameter("start")); } String limitValue =
	 * request.getParameter("limit"); if (limitValue != null &&
	 * limitValue.isEmpty() == false) { limit =
	 * Integer.parseInt(request.getParameter("limit")); }
	 * 
	 * GemsEmployeeJobDetail gemsEmployeeJobDetail = new
	 * GemsEmployeeJobDetail();
	 * 
	 * String searchEmpCode =
	 * request.getParameter("selectedGemsEmployeeMasterId"); if (searchEmpCode
	 * != null && searchEmpCode.isEmpty() == false) {
	 * gemsEmployeeJobDetail.setGemsEmployeeMaster(employeeService.
	 * getGemsEmployeeMaster(Integer.parseInt(searchEmpCode))); }
	 * 
	 * int totalResults =
	 * employeeService.getGemsEmployeeJobDetailFilterCount(gemsEmployeeJobDetail
	 * ); List<GemsEmployeeJobDetail> list =
	 * employeeService.getGemsEmployeeJobDetailList(start, limit,
	 * gemsEmployeeJobDetail);
	 * 
	 * 
	 * logger.info("Returned list size" + list.size());
	 * 
	 * return getModelMapEmployeeJobDetailList(list, totalResults);
	 * 
	 * } catch (Exception e) {
	 * 
	 * return getModelMapError("Error trying to List."+e.getMessage()); } }
	 * 
	 * @RequestMapping(value="/employee/saveGemsEmployeeJobDetail",method=
	 * RequestMethod.POST) public @ResponseBody Map<String, Object>
	 * saveGemsEmployeeJobDetail(HttpServletRequest request) { logger.info(
	 * "Insert Method Strarted.,"); Map<String, Object> modelMap = new
	 * HashMap<String, Object>(2); try { Calendar currentDate =
	 * Calendar.getInstance(); SimpleDateFormat formatter = new
	 * SimpleDateFormat("MM/dd/yyyy"); Date todayDate = (Date)
	 * formatter.parse(formatter.format(currentDate .getTime()));
	 * 
	 * GemsEmployeeJobDetail gemsEmployeeJobDetail = new
	 * GemsEmployeeJobDetail(); GemsUserMaster loggedInUser = (GemsUserMaster)
	 * WebUtils.getRequiredSessionAttribute(request, "loggedInUser"); if
	 * ((StringUtils.isNotBlank(request.getParameter(
	 * "selectedGemsEmployeeMasterId"))) ||
	 * (StringUtils.isNotEmpty(request.getParameter(
	 * "selectedGemsEmployeeMasterId")))) {
	 * gemsEmployeeJobDetail.setGemsEmployeeMaster(employeeService.
	 * getGemsEmployeeMaster(Integer.parseInt(request.getParameter(
	 * "selectedGemsEmployeeMasterId")))); }
	 * 
	 * String id_value = ""; if
	 * ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeJobDetailId"))
	 * ) ||
	 * (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeJobDetailId")))
	 * ) { id_value = request.getParameter("gemsEmployeeJobDetailId");
	 * gemsEmployeeJobDetail =
	 * employeeService.getGemsEmployeeJobDetail(Integer.parseInt(id_value)); }
	 * else { gemsEmployeeJobDetail.setCreatedOn(todayDate);
	 * gemsEmployeeJobDetail.setCreatedBy(loggedInUser.getCreatedBy()); }
	 * 
	 * String contractStartDateString =
	 * request.getParameter("contactStartDate"); if
	 * ((StringUtils.isNotBlank(contractStartDateString)) ||
	 * (StringUtils.isNotEmpty(contractStartDateString))) { Date
	 * contactStartDate = formatter.parse(contractStartDateString);
	 * gemsEmployeeJobDetail.setContactStartDate(contactStartDate); }
	 * 
	 * String contractEndDateString = request.getParameter("contractEndDate");
	 * if ((StringUtils.isNotBlank(contractEndDateString)) ||
	 * (StringUtils.isNotEmpty(contractEndDateString))) { Date contractEndDate =
	 * formatter.parse(contractEndDateString);
	 * gemsEmployeeJobDetail.setContractEndDate(contractEndDate); }
	 * 
	 * String joinedDateString = request.getParameter("joinedDate"); if
	 * ((StringUtils.isNotBlank(joinedDateString)) ||
	 * (StringUtils.isNotEmpty(joinedDateString))) { Date joinedDate =
	 * formatter.parse(joinedDateString);
	 * gemsEmployeeJobDetail.setJoinedDate(joinedDate); }
	 * 
	 * String confirmationDateString = request.getParameter("confirmationDate");
	 * if ((StringUtils.isNotBlank(confirmationDateString)) ||
	 * (StringUtils.isNotEmpty(confirmationDateString))) { Date confirmationDate
	 * = formatter.parse(confirmationDateString);
	 * gemsEmployeeJobDetail.setConfirmationDate(confirmationDate); }
	 * 
	 * 
	 * int gemsDesignationId = 0;
	 * 
	 * if
	 * ((StringUtils.isNotBlank(request.getParameter("selected_designation")))
	 * ||
	 * (StringUtils.isNotEmpty(request.getParameter("selected_designation")))) {
	 * try { gemsDesignationId =
	 * Integer.parseInt(request.getParameter("selected_designation"));
	 * 
	 * gemsEmployeeJobDetail.setGemsDesignation(masterService.getGemsDesignation
	 * (gemsDesignationId)); } catch(NumberFormatException ex) { // this will be
	 * called when the drop down value does not changed
	 * gemsEmployeeJobDetail.setGemsDesignation(gemsEmployeeJobDetail.
	 * getGemsDesignation()); } }
	 * 
	 * int gemsDepartmentId = 0; if
	 * ((StringUtils.isNotBlank(request.getParameter("selected_department"))) ||
	 * (StringUtils.isNotEmpty(request.getParameter("selected_department")))) {
	 * try { gemsDepartmentId =
	 * Integer.parseInt(request.getParameter("selected_department"));
	 * 
	 * gemsEmployeeJobDetail.setGemsDepartment(masterService.getGemsDepartment(
	 * gemsDepartmentId)); } catch(NumberFormatException ex) { // this will be
	 * called when the drop down value does not changed
	 * gemsEmployeeJobDetail.setGemsDepartment(gemsEmployeeJobDetail.
	 * getGemsDepartment()); } }
	 * 
	 * int currentStatusFlag = 0; int currentReportingFlag = 0;
	 * 
	 * int gemsEmploymentStatusId = 0; if
	 * ((StringUtils.isNotBlank(request.getParameter("selected_employeestatus"))
	 * ) ||
	 * (StringUtils.isNotEmpty(request.getParameter("selected_employeestatus")))
	 * ) { try { gemsEmploymentStatusId =
	 * Integer.parseInt(request.getParameter("selected_employeestatus"));
	 * 
	 * gemsEmployeeJobDetail.setGemsEmploymentStatus(masterService.
	 * getGemsEmploymentStatus(gemsEmploymentStatusId));
	 * 
	 * currentStatusFlag = 1; } catch(NumberFormatException ex) { // this will
	 * be called when the drop down value does not changed
	 * gemsEmployeeJobDetail.setGemsEmploymentStatus(gemsEmployeeJobDetail.
	 * getGemsEmploymentStatus()); } }
	 * 
	 * int gemsEmploymentReportingId = 0; if
	 * ((StringUtils.isNotBlank(request.getParameter("selected_reporting"))) ||
	 * (StringUtils.isNotEmpty(request.getParameter("selected_reporting")))) {
	 * try { gemsEmploymentReportingId =
	 * Integer.parseInt(request.getParameter("selected_reporting"));
	 * 
	 * gemsEmployeeJobDetail.setReportingTo(employeeService.
	 * getGemsEmployeeMaster(gemsEmploymentReportingId)); currentReportingFlag =
	 * 1; } catch(NumberFormatException ex) { // this will be called when the
	 * drop down value does not changed
	 * gemsEmployeeJobDetail.setReportingTo(gemsEmployeeJobDetail.getReportingTo
	 * ()); } }
	 * 
	 * String isActive = request.getParameter("activeStatus"); if ((isActive ==
	 * "on") || (isActive.equalsIgnoreCase("on"))) {
	 * gemsEmployeeJobDetail.setActiveStatus(1); } else {
	 * gemsEmployeeJobDetail.setActiveStatus(0); }
	 * 
	 * employeeService.saveGemsEmployeeJobDetail(gemsEmployeeJobDetail);
	 * 
	 * 
	 * Code need to be reviewwed later....................
	 * 
	 * 
	 * if (currentReportingFlag ==1) { GemsEmployeeMaster gemsEmployeeMaster =
	 * gemsEmployeeJobDetail.getGemsEmployeeMaster();
	 * gemsEmployeeMaster.setCurrentReportingTo(gemsEmployeeJobDetail.
	 * getReportingTo().getGemsEmployeeMasterId());
	 * employeeService.saveGemsEmployeeMaster(gemsEmployeeMaster); }
	 * 
	 * 
	 * // Changing current status of emplyee in case of change of employee
	 * status at job detail if (currentStatusFlag == 1) { GemsEmployeeMaster
	 * gemsEmployeeMaster = gemsEmployeeJobDetail.getGemsEmployeeMaster();
	 * gemsEmployeeMaster.setCurrentEmployeeStatus(gemsEmployeeJobDetail.
	 * getGemsEmploymentStatus());
	 * employeeService.saveGemsEmployeeMaster(gemsEmployeeMaster);
	 * 
	 * 
	 * Leave Assignment to employee
	 * 
	 * 
	 * LeaveSummayMaster leaveSummayMaster = new LeaveSummayMaster();
	 * leaveSummayMaster.setGemsEmploymentStatus(gemsEmployeeMaster.
	 * getCurrentEmployeeStatus());
	 * leaveSummayMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation())
	 * ; List<LeaveSummayMaster> leaveSummaryMasterList =
	 * leaveManagementService.getAllLeaveSummayMasteList(leaveSummayMaster);
	 * 
	 * 
	 * get the number of days between leaveperiod start date and end date
	 * 
	 * GemsLeavePeriodMaster gemsLeavePeriodMaster = new
	 * GemsLeavePeriodMaster(); gemsLeavePeriodMaster.setIsCurrent(1);
	 * gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation()); GemsLeavePeriodMaster activeGemsLeavePeriodMaster
	 * = leaveManagementService.getActiveGemsLeavePeriodMaster(
	 * gemsLeavePeriodMaster); int number_of_leaveperiod_days = (int)(
	 * (activeGemsLeavePeriodMaster.getToDate().getTime() -
	 * activeGemsLeavePeriodMaster.getFromDate().getTime()) / (1000 * 60 * 60 *
	 * 24)); double number_of_leaveperiod_hours =
	 * (number_of_leaveperiod_days*24); int employee_eligible_days = 0; double
	 * quotient = 0.0; double employee_no_of_eligible_hours = 0.0;
	 * 
	 * 
	 * if (gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusCode().
	 * equalsIgnoreCase("CO")) { if
	 * (gemsEmployeeJobDetail.getConfirmationDate().before(
	 * activeGemsLeavePeriodMaster.getToDate())) { employee_eligible_days =
	 * (int)( (activeGemsLeavePeriodMaster.getToDate().getTime() -
	 * todayDate.getTime()) / (1000 * 60 * 60 * 24));
	 * employee_no_of_eligible_hours = (employee_eligible_days*24); if
	 * (employee_eligible_days != 0) { quotient =
	 * Math.round(number_of_leaveperiod_hours/employee_no_of_eligible_hours);
	 * Iterator leaveSummaryMasterIterator = leaveSummaryMasterList.iterator();
	 * while (leaveSummaryMasterIterator.hasNext()) { LeaveSummayMaster
	 * employeeLeaveSummayMaster = (LeaveSummayMaster)
	 * leaveSummaryMasterIterator.next(); int leaveTypeDaysCount =
	 * employeeLeaveSummayMaster.getTotalDays(); int
	 * employee_entitled_leave_days = (int)
	 * Math.round(leaveTypeDaysCount/quotient); GemsEmplyeeLeaveSummary
	 * gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
	 * gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
	 * activeGemsLeavePeriodMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(employeeLeaveSummayMaster.
	 * getGemsLeaveTypeMaster());
	 * gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation());
	 * gemsEmplyeeLeaveSummary.setLeaveEntitled(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveBalance(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
	 * gemsEmplyeeLeaveSummary.setLeaveTaken(0);
	 * gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setActiveStatus(1);
	 * employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	 * 
	 * } } } else { employee_eligible_days = (int)(
	 * (activeGemsLeavePeriodMaster.getToDate().getTime() -
	 * gemsEmployeeJobDetail.getConfirmationDate().getTime()) / (1000 * 60 * 60
	 * * 24)); employee_no_of_eligible_hours = employee_eligible_days/24; if
	 * (employee_eligible_days != 0) { quotient =
	 * Math.round(number_of_leaveperiod_hours/employee_no_of_eligible_hours);
	 * Iterator leaveSummaryMasterIterator = leaveSummaryMasterList.iterator();
	 * while (leaveSummaryMasterIterator.hasNext()) { LeaveSummayMaster
	 * employeeLeaveSummayMaster = (LeaveSummayMaster)
	 * leaveSummaryMasterIterator.next(); int leaveTypeDaysCount =
	 * employeeLeaveSummayMaster.getTotalDays(); int
	 * employee_entitled_leave_days = (int)
	 * Math.round(leaveTypeDaysCount/quotient); GemsEmplyeeLeaveSummary
	 * gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
	 * gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
	 * activeGemsLeavePeriodMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(employeeLeaveSummayMaster.
	 * getGemsLeaveTypeMaster());
	 * gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation());
	 * gemsEmplyeeLeaveSummary.setLeaveEntitled(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveBalance(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
	 * gemsEmplyeeLeaveSummary.setLeaveTaken(0);
	 * gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setActiveStatus(1);
	 * employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	 * 
	 * } } }
	 * 
	 * 
	 * 
	 * } else if
	 * ((gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusCode().
	 * equalsIgnoreCase("PP")) ||
	 * (gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusCode().
	 * equalsIgnoreCase("CT"))) { if
	 * ((gemsEmployeeJobDetail.getContactStartDate().before(
	 * activeGemsLeavePeriodMaster.getToDate())) &&
	 * (gemsEmployeeJobDetail.getContractEndDate().before(
	 * activeGemsLeavePeriodMaster.getToDate()))) { Iterator
	 * leaveSummaryMasterIterator = leaveSummaryMasterList.iterator(); while
	 * (leaveSummaryMasterIterator.hasNext()) { LeaveSummayMaster
	 * employeeLeaveSummayMaster = (LeaveSummayMaster)
	 * leaveSummaryMasterIterator.next(); int leaveTypeDaysCount =
	 * employeeLeaveSummayMaster.getTotalDays(); GemsEmplyeeLeaveSummary
	 * gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
	 * gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
	 * activeGemsLeavePeriodMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(employeeLeaveSummayMaster.
	 * getGemsLeaveTypeMaster());
	 * gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation());
	 * gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveTypeDaysCount);
	 * gemsEmplyeeLeaveSummary.setLeaveBalance(leaveTypeDaysCount);
	 * gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
	 * gemsEmplyeeLeaveSummary.setLeaveTaken(0);
	 * gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setActiveStatus(1);
	 * employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	 * 
	 * } } else if (gemsEmployeeJobDetail.getContractEndDate().before(
	 * activeGemsLeavePeriodMaster.getToDate())) { employee_eligible_days =
	 * (int)( (activeGemsLeavePeriodMaster.getToDate().getTime() -
	 * gemsEmployeeJobDetail.getContractEndDate().getTime()) / (1000 * 60 * 60 *
	 * 24)); int sub_from_leaveperiod_value = number_of_leaveperiod_days -
	 * employee_eligible_days; employee_no_of_eligible_hours =
	 * (sub_from_leaveperiod_value*24); quotient =
	 * Math.round(employee_no_of_eligible_hours/(90*24)); Iterator
	 * leaveSummaryMasterIterator = leaveSummaryMasterList.iterator(); while
	 * (leaveSummaryMasterIterator.hasNext()) { LeaveSummayMaster
	 * employeeLeaveSummayMaster = (LeaveSummayMaster)
	 * leaveSummaryMasterIterator.next(); int leaveTypeDaysCount =
	 * employeeLeaveSummayMaster.getTotalDays(); int
	 * employee_entitled_leave_days = (int)
	 * Math.round((leaveTypeDaysCount)*(quotient)); GemsEmplyeeLeaveSummary
	 * gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
	 * gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
	 * activeGemsLeavePeriodMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(employeeLeaveSummayMaster.
	 * getGemsLeaveTypeMaster());
	 * gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation());
	 * gemsEmplyeeLeaveSummary.setLeaveEntitled(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveBalance(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
	 * gemsEmplyeeLeaveSummary.setLeaveTaken(0);
	 * gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setActiveStatus(1);
	 * employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	 * 
	 * } } else if (gemsEmployeeJobDetail.getContractEndDate().after(
	 * activeGemsLeavePeriodMaster.getToDate())) { employee_eligible_days =
	 * (int)( (gemsEmployeeJobDetail.getContractEndDate().getTime() -
	 * activeGemsLeavePeriodMaster.getToDate().getTime()) / (1000 * 60 * 60 *
	 * 24)); employee_no_of_eligible_hours = (employee_eligible_days*24);
	 * quotient = Math.round(employee_no_of_eligible_hours/(90*24)); Iterator
	 * leaveSummaryMasterIterator = leaveSummaryMasterList.iterator(); while
	 * (leaveSummaryMasterIterator.hasNext()) { LeaveSummayMaster
	 * employeeLeaveSummayMaster = (LeaveSummayMaster)
	 * leaveSummaryMasterIterator.next(); int leaveTypeDaysCount =
	 * employeeLeaveSummayMaster.getTotalDays(); int
	 * employee_entitled_leave_days = (int)
	 * Math.round((leaveTypeDaysCount)*(quotient)); GemsEmplyeeLeaveSummary
	 * gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
	 * gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(
	 * activeGemsLeavePeriodMaster);
	 * gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(employeeLeaveSummayMaster.
	 * getGemsLeaveTypeMaster());
	 * gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.
	 * getGemsOrganisation());
	 * gemsEmplyeeLeaveSummary.setLeaveEntitled(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveBalance(employee_entitled_leave_days);
	 * gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
	 * gemsEmplyeeLeaveSummary.setLeaveTaken(0);
	 * gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
	 * gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
	 * gemsEmplyeeLeaveSummary.setActiveStatus(1);
	 * employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	 * 
	 * } }
	 * 
	 * } //End of checking of employeee status Contract or Probation
	 * 
	 * 
	 * 
	 * 
	 * }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * logger.info("Insert Method Executed.,"); modelMap.put("success", true);
	 * modelMap.put("message", "Saved Successfully"); return modelMap; }
	 * catch(Exception ex) { String msg = "Sorry problem in saving data";
	 * modelMap.put("success", false); modelMap.put("message", msg); return
	 * modelMap; }
	 * 
	 * 
	 * 
	 * }
	 * 
	 * @RequestMapping(value="/employee/deleteGemsEmployeeJobDetail",method=
	 * RequestMethod.GET) public @ResponseBody Map<String, Object>
	 * deleteGemsEmployeeJobDetail(HttpServletRequest request) {
	 * 
	 * 
	 * logger.info("Delete Method Strarted.,"); Map<String, Object> modelMap =
	 * new HashMap<String, Object>(2);
	 * 
	 * int gemsEmployeeJobDetailId =
	 * Integer.parseInt(request.getParameter("objectId")); try {
	 * GemsEmployeeJobDetail gemsEmployeeJobDetail =
	 * employeeService.getGemsEmployeeJobDetail(gemsEmployeeJobDetailId);
	 * employeeService.removeGemsEmployeeJobDetail(gemsEmployeeJobDetail);
	 * logger.info("Delete Method Completed.,"); modelMap.put("success", true);
	 * modelMap.put("message", "Deleted Successfully"); return modelMap;
	 * 
	 * } catch(Exception ex) { modelMap.put("success", false);
	 * modelMap.put("message", "Error in deletion"); return modelMap; }
	 * 
	 * 
	 * } private Map<String, Object>
	 * getModelMapEmployeeJobDetailList(List<GemsEmployeeJobDetail> list, int
	 * totalResults) {
	 * 
	 * Map<String, Object> modelMap = new HashMap<String, Object>(3);
	 * modelMap.put("total", totalResults); JsonConfig jsonConfig = new
	 * JsonConfig();
	 * jsonConfig.registerJsonBeanProcessor(GemsEmployeeJobDetail.class, new
	 * JsonBeanProcessor() { public JSONObject processBean(Object bean,
	 * JsonConfig jsonConfig) { if (!(bean instanceof GemsEmployeeJobDetail)) {
	 * return new JSONObject(true); }
	 * 
	 * GemsEmployeeJobDetail gemsEmployeeJobDetail = (GemsEmployeeJobDetail)
	 * bean;
	 * 
	 * int selectedGemsDesignationId= 0; String selected_designationdesc = "";
	 * String selected_designation = ""; if
	 * (gemsEmployeeJobDetail.getGemsDesignation() != null) {
	 * selectedGemsDesignationId =
	 * gemsEmployeeJobDetail.getGemsDesignation().getGemsDesignationId();
	 * selected_designation =
	 * ""+gemsEmployeeJobDetail.getGemsDesignation().getGemsDesignationCode()+
	 * "-"+gemsEmployeeJobDetail.getGemsDesignation().
	 * getGemsDesignationDescription()+""; selected_designationdesc =
	 * ""+gemsEmployeeJobDetail.getGemsDesignation().
	 * getGemsDesignationDescription(); }
	 * 
	 * int selectedGemsDepartmentId= 0; String selected_department = ""; String
	 * selected_departmentdesc = ""; if
	 * (gemsEmployeeJobDetail.getGemsDepartment() != null) {
	 * selectedGemsDepartmentId =
	 * gemsEmployeeJobDetail.getGemsDepartment().getGemsDepartmentId();
	 * selected_department =
	 * gemsEmployeeJobDetail.getGemsDepartment().getDepartmentCode()+"-"+
	 * gemsEmployeeJobDetail.getGemsDepartment().getDepartmentDescription()+"";
	 * selected_departmentdesc =
	 * ""+gemsEmployeeJobDetail.getGemsDepartment().getDepartmentDescription()+
	 * "";
	 * 
	 * 
	 * }
	 * 
	 * int selectedGemsEmploymentStatusId= 0; String selected_employeestatus =
	 * ""; String selected_employeestatusdesc = ""; if
	 * (gemsEmployeeJobDetail.getGemsEmploymentStatus() != null) {
	 * selectedGemsEmploymentStatusId =
	 * gemsEmployeeJobDetail.getGemsEmploymentStatus().getGemsEmploymentStatusId
	 * (); selected_employeestatus =
	 * ""+gemsEmployeeJobDetail.getGemsEmploymentStatus().getStatusCode()+"-"+
	 * gemsEmployeeJobDetail.getGemsEmploymentStatus().getStatusDescription()+
	 * ""; selected_employeestatusdesc =
	 * gemsEmployeeJobDetail.getGemsEmploymentStatus().getStatusDescription();
	 * 
	 * }
	 * 
	 * int selectedReportingId= 0; String selected_reporting = ""; String
	 * selected_reportingdesc = ""; if (gemsEmployeeJobDetail.getReportingTo()
	 * != null) { selectedReportingId =
	 * gemsEmployeeJobDetail.getReportingTo().getGemsEmployeeMasterId();
	 * selected_reporting =
	 * ""+gemsEmployeeJobDetail.getReportingTo().getEmployeeCode()+"-"+
	 * gemsEmployeeJobDetail.getReportingTo().getEmployeeLastName()+" "
	 * +gemsEmployeeJobDetail.getReportingTo().getEmployeeFirstName()+"";
	 * selected_reportingdesc =
	 * ""+gemsEmployeeJobDetail.getReportingTo().getEmployeeLastName()+" "
	 * +gemsEmployeeJobDetail.getReportingTo().getEmployeeFirstName()+""; }
	 * 
	 * int selectedGemsEmployeeMasterId = 0; String
	 * selectedGemsEmployeeMasterName = ""; if
	 * (gemsEmployeeJobDetail.getGemsEmployeeMaster() != null) {
	 * selectedGemsEmployeeMasterId =
	 * gemsEmployeeJobDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId();
	 * 
	 * if (gemsEmployeeJobDetail.getGemsEmployeeMaster().getEmployeeFirstName()
	 * != null) { selectedGemsEmployeeMasterName =
	 * selectedGemsEmployeeMasterName+gemsEmployeeJobDetail.
	 * getGemsEmployeeMaster().getEmployeeFirstName(); } if
	 * (gemsEmployeeJobDetail.getGemsEmployeeMaster().getEmployeeLastName() !=
	 * null) { selectedGemsEmployeeMasterName =
	 * selectedGemsEmployeeMasterName+gemsEmployeeJobDetail.
	 * getGemsEmployeeMaster().getEmployeeLastName(); } }
	 * 
	 * String DATE_FORMAT = "MM/dd/yyyy"; SimpleDateFormat sdf = new
	 * SimpleDateFormat(DATE_FORMAT);
	 * 
	 * String contactStartDate = ""; if
	 * (gemsEmployeeJobDetail.getContactStartDate() != null){ contactStartDate =
	 * sdf.format(gemsEmployeeJobDetail.getContactStartDate()); }
	 * 
	 * String contractEndDate = ""; if
	 * (gemsEmployeeJobDetail.getContractEndDate() != null){ contractEndDate =
	 * sdf.format(gemsEmployeeJobDetail.getContractEndDate()); }
	 * 
	 * String joinedDate = ""; if (gemsEmployeeJobDetail.getJoinedDate() !=
	 * null){ joinedDate = sdf.format(gemsEmployeeJobDetail.getJoinedDate()); }
	 * 
	 * String confirmationDate = ""; if
	 * (gemsEmployeeJobDetail.getConfirmationDate() != null){ confirmationDate =
	 * sdf.format(gemsEmployeeJobDetail.getConfirmationDate()); }
	 * 
	 * return new JSONObject()
	 * .element("gemsEmployeeJobDetailId",gemsEmployeeJobDetail.
	 * getGemsEmployeeJobDetailId()) .element("contactStartDate",
	 * contactStartDate) .element("contractEndDate",contractEndDate)
	 * .element("joinedDate", joinedDate)
	 * .element("confirmationDate",confirmationDate)
	 * .element("selectedGemsDesignationId",selectedGemsDesignationId)
	 * .element("selected_designation", selected_designation)
	 * .element("selected_designationdesc",selected_designationdesc)
	 * .element("activeStatus", gemsEmployeeJobDetail.getActiveStatus())
	 * .element("selectedGemsDepartmentId", selectedGemsDepartmentId)
	 * .element("selected_department",selected_department)
	 * .element("selected_departmentdesc",selected_departmentdesc)
	 * .element("selectedGemsEmploymentStatusId",
	 * selectedGemsEmploymentStatusId) .element("selected_employeestatus",
	 * selected_employeestatus) .element("selected_employeestatusdesc",
	 * selected_employeestatusdesc) .element("selectedReportingId",
	 * selectedReportingId) .element("selected_reporting",selected_reporting)
	 * .element("selected_reportingdesc",selected_reportingdesc)
	 * .element("selectedGemsEmployeeMasterId",selectedGemsEmployeeMasterId)
	 * .element("selectedGemsEmployeeMasterName",
	 * selectedGemsEmployeeMasterName)
	 * 
	 * ; } });
	 * 
	 * JSON json = JSONSerializer.toJSON(list, jsonConfig);
	 * 
	 * --- modelMap.put("data", json); modelMap.put("success", true);
	 * 
	 * return modelMap; }
	 * 
	 * 
	 * Common json methds
	 * 
	 * 
	 * private ModelAndView getModelMap(){
	 * 
	 * Map<String,Object> modelMap = new HashMap<String,Object>(1);
	 * modelMap.put("success", true); return new ModelAndView("jsonView",
	 * modelMap); } private Map<String, Object> getModelMapError(String msg){
	 * 
	 * Map<String,Object> modelMap = new HashMap<String,Object>(2);
	 * modelMap.put("message", msg); modelMap.put("success", false);
	 * modelMap.put("data", "");
	 * 
	 * return modelMap; }
	 */
}
