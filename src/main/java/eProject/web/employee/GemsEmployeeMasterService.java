package eProject.web.employee;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

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
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeImmigrationDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmplyeeLeaveSummary;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.mail.MailService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;
import eProject.utility.HrKeyStoreUtility;

@Controller
public class GemsEmployeeMasterService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private MailService mailService;

	@Autowired
	private LeaveManagementService leaveManagementService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeMasterService.class);

	@RequestMapping(value = "/employee/checkeEmployeeByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkeEmployeeByCode(HttpServletRequest request) {

		String employeeCode = request.getParameter("employeeCode");

		GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
		gemsEmployeeMaster.setEmployeeCode(employeeCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsEmployeeMaster returnedGemsEmployeeMaster = employeeService.getGemsEmployeeMasterByCode(gemsEmployeeMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsEmployeeMaster.getEmployeeCode()))
				|| (StringUtils.isNotEmpty(returnedGemsEmployeeMaster.getEmployeeCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/viewEmployeeList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 10;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			
			boolean showAllEmployeesFlag = false;
			String showAllEmployees = request.getParameter("showAllEmployees");
			if (showAllEmployees != null && showAllEmployees.isEmpty() == false) {
				showAllEmployeesFlag = Boolean.valueOf(showAllEmployees);
			}

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
			if (gemsEmployeeMaster.getGemsOrganisation() != null) {
				gemsEmployeeMaster.setGemsOrganisation(gemsEmployeeMaster.getGemsOrganisation());
			} else {
				gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			}

			String searchEmpCode = request.getParameter("searchEmpCode");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmployeeMaster.setEmployeeCode(searchEmpCode);
			}

			String searchFirstName = request.getParameter("searchEmpFirstName");
			if (searchFirstName != null && searchFirstName.isEmpty() == false) {
				gemsEmployeeMaster.setEmployeeFirstName(searchFirstName);
			}

			String searchLastName = request.getParameter("searchEmpLastName");
			if (searchLastName != null && searchLastName.isEmpty() == false) {
				gemsEmployeeMaster.setEmployeeLastName(searchLastName);
			}

			String searchEmpMobile = request.getParameter("searchEmpMobile");
			if (searchEmpMobile != null && searchEmpMobile.isEmpty() == false) {
				gemsEmployeeMaster.setOfficeContactNumber(searchEmpMobile);
			}

			if ((StringUtils.isNotBlank(request.getParameter("dropdown_currentstatus")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_currentstatus")))) {
				String employeeCurrrentStatus = request.getParameter("dropdown_currentstatus");
				if (!((employeeCurrrentStatus == "0") || (employeeCurrrentStatus.equalsIgnoreCase("0")))) {
					GemsEmploymentStatus gemsEmploymentStatus = masterService
							.getGemsEmploymentStatus(Integer.parseInt(request.getParameter("dropdown_currentstatus")));
					gemsEmployeeMaster.setCurrentEmployeeStatus(gemsEmploymentStatus);
				}

			}

			if ((StringUtils.isNotBlank(request.getParameter("dropdown_department")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_department")))) {
				String employeeDepartment = request.getParameter("dropdown_department");
				if (!((employeeDepartment == "0") || (employeeDepartment.equalsIgnoreCase("0")))) {
					GemsEmployeeJobDetail gemsEmployeeJobDetail = new GemsEmployeeJobDetail();
					GemsDepartment gemsDepartment = masterService
							.getGemsDepartment(Integer.parseInt(request.getParameter("dropdown_department")));
					gemsEmployeeJobDetail.setGemsDepartment(gemsDepartment);
					gemsEmployeeMaster.setEmployeeJobDetails(gemsEmployeeJobDetail);
				}

			}

			String searchEmpEmail = request.getParameter("searchEmpEmail");
			if (searchEmpEmail != null && searchEmpEmail.isEmpty() == false) {
				gemsEmployeeMaster.setOfficialEmailid(searchEmpEmail);
				gemsEmployeeMaster.setPersonalEmailId(searchEmpEmail);
			}

			/*
			 * String searchEmpPassportNo =
			 * request.getParameter("searchEmpPassportNo"); if
			 * (searchEmpPassportNo != null && searchEmpPassportNo.isEmpty() ==
			 * false) { GemsEmployeeImmigrationDetail
			 * gemsEmployeeImmigrationDetail = new
			 * GemsEmployeeImmigrationDetail();
			 * gemsEmployeeImmigrationDetail.setDocumentType("PASSPORT");
			 * gemsEmployeeImmigrationDetail.setDocumentNumber(
			 * searchEmpPassportNo); }
			 */

			if ((StringUtils.isNotBlank(request.getParameter("searchEmpActive")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchEmpActive")))) {
				String isActive = request.getParameter("searchEmpActive");
				if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
					gemsEmployeeMaster.setActiveStatus(1);
				} else {
					gemsEmployeeMaster.setActiveStatus(0);

				}

			} else {
				gemsEmployeeMaster.setActiveStatus(1); // By Default it will
														// show only Active
														// employees
			}

			if ((StringUtils.isNotBlank(request.getParameter("searchEmployeeLocation")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchEmployeeLocation")))) {
				String searchEmployeeLocation = request.getParameter("searchEmployeeLocation");
				if (!((searchEmployeeLocation == "0") || (searchEmployeeLocation.equalsIgnoreCase("0"))
						|| (searchEmployeeLocation == "Select Work Location"))) {
					gemsEmployeeMaster.setEmployeeLocation(searchEmployeeLocation);
				}

			}

			if ((StringUtils.isNotBlank(request.getParameter("employeeFlag")))
					|| (StringUtils.isNotEmpty(request.getParameter("employeeFlag")))) {
				GemsUserMaster gemsUserMaster = new GemsUserMaster();
				GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
				gemsRoleMaster.setRoleCode("Employee");
				gemsUserMaster.setGemsRoleMaster(gemsRoleMaster);
				gemsEmployeeMaster.setGemsUserMaster(gemsUserMaster);
			}

			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsEmployeeMaster.setGemsEmployeeMasterId(userEmployee.getGemsEmployeeMasterId());
				gemsEmployeeMaster.setCurrentReportingTo(userEmployee);
			}
			
			int totalResults = 0;
			List<GemsEmployeeMaster> list = new ArrayList<GemsEmployeeMaster>();
			
			if (showAllEmployeesFlag == true)
			{
				list = employeeService.getAllGemsEmployeeMasterList(gemsEmployeeMaster);
				totalResults = list.size();
				
			}
			else
			{
				totalResults = employeeService.getGemsEmployeeMasterFilterCount(gemsEmployeeMaster);
				list = employeeService.getGemsEmployeeMasterList(start, limit, gemsEmployeeMaster);
			}

			

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveEmployee", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmployee(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
			GemsEmployeeContactDetail gemsEmployeeContactDetail = new GemsEmployeeContactDetail();
			GemsEmployeeJobDetail gemsEmployeeJobDetail = new GemsEmployeeJobDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEmployeeMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeMaster.setUpdatedOn(todayDate);
			gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			gemsEmployeeContactDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeContactDetail.setUpdatedOn(todayDate);
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				id_value = request.getParameter("gemsEmployeeMasterId");
				gemsEmployeeMaster = employeeService.getGemsEmployeeMaster(Integer.parseInt(id_value));
				gemsEmployeeContactDetail = gemsEmployeeMaster.getGemsEmployeeContactDetail();
				gemsEmployeeJobDetail = gemsEmployeeMaster.getEmployeeJobDetails();

			} else {
				gemsEmployeeMaster.setCreatedOn(todayDate);
				gemsEmployeeMaster.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeContactDetail.setCreatedOn(todayDate);
				gemsEmployeeContactDetail.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeMaster.setLeaveAllocationStatus(2);
			}

			gemsEmployeeMaster.setUpdatedOn(todayDate);
			gemsEmployeeMaster.setUpdatedBy(loggedInUser.getCreatedBy());

			gemsEmployeeContactDetail.setUpdatedOn(todayDate);
			gemsEmployeeContactDetail.setUpdatedBy(loggedInUser.getCreatedBy());

			gemsEmployeeJobDetail.setUpdatedOn(todayDate);
			gemsEmployeeJobDetail.setUpdatedBy(loggedInUser.getCreatedBy());

			String employeeFirstName = request.getParameter("employeeFirstName");
			gemsEmployeeMaster.setEmployeeFirstName(employeeFirstName);

			String employeeLastName = request.getParameter("employeeLastName");
			gemsEmployeeMaster.setEmployeeLastName(employeeLastName);

			String employeeLocation = request.getParameter("employeeLocation");
			gemsEmployeeMaster.setEmployeeLocation(employeeLocation);

			String ssnNumber = request.getParameter("ssnNumber");
			if ((StringUtils.isNotBlank(ssnNumber)) || (StringUtils.isNotEmpty(ssnNumber))) {
				String encryptedSSNValue = hrKeyStoreUtility.getEncryptedStringValue(ssnNumber, "E");
				gemsEmployeeMaster.setSsnNumber(encryptedSSNValue);
			}
			String panCardNumber = request.getParameter("panCardNumber");
			if ((StringUtils.isNotBlank(panCardNumber)) || (StringUtils.isNotEmpty(panCardNumber))) {
				String encryptedPanCardValue = hrKeyStoreUtility.getEncryptedStringValue(panCardNumber, "E");
				gemsEmployeeMaster.setPanCardNumber(encryptedPanCardValue);
			}

			String employeeMiddleName = request.getParameter("employeeMiddleName");
			gemsEmployeeMaster.setEmployeeMiddleName(employeeMiddleName);

			String employeeCode = request.getParameter("employeeCode");
			gemsEmployeeMaster.setEmployeeCode(employeeCode);

			String employeeDobString = request.getParameter("employeeDob");
			if ((StringUtils.isNotBlank(employeeDobString)) || (StringUtils.isNotEmpty(employeeDobString))) {
				Date employeeDob = formatter.parse(employeeDobString);
				gemsEmployeeMaster.setEmployeeDob(employeeDob);
			}

			if ((StringUtils.isNotBlank(request.getParameter("dropdown_currentstatus")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_currentstatus")))) {
				GemsEmploymentStatus gemsEmploymentStatus = masterService
						.getGemsEmploymentStatus(Integer.parseInt(request.getParameter("dropdown_currentstatus")));
				gemsEmployeeMaster.setCurrentEmployeeStatus(gemsEmploymentStatus);
			}
			if ((StringUtils.isNotBlank(request.getParameter("dropdown_reportingmanager")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_reportingmanager")))) {
				GemsEmployeeMaster reportingManager = employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("dropdown_reportingmanager")));
				gemsEmployeeMaster.setCurrentReportingTo(reportingManager);
			}

			String confirmationDateString = request.getParameter("confirmationDate");
			if ((StringUtils.isNotBlank(confirmationDateString)) || (StringUtils.isNotEmpty(confirmationDateString))) {
				Date confirmationDate = formatter.parse(confirmationDateString);
				gemsEmployeeJobDetail.setConfirmationDate(confirmationDate);
			}

			String joinedDateString = request.getParameter("joinedDate");
			if ((StringUtils.isNotBlank(joinedDateString)) || (StringUtils.isNotEmpty(joinedDateString))) {
				Date joinedDate = formatter.parse(joinedDateString);
				gemsEmployeeJobDetail.setJoinedDate(joinedDate);
			}

			String contactStartDateString = request.getParameter("contractStartDate");
			if ((StringUtils.isNotBlank(contactStartDateString)) || (StringUtils.isNotEmpty(contactStartDateString))) {
				Date contactStartDate = formatter.parse(contactStartDateString);
				gemsEmployeeJobDetail.setContactStartDate(contactStartDate);
			}

			String contractEndDateString = request.getParameter("contractEndDate");
			if ((StringUtils.isNotBlank(contractEndDateString)) || (StringUtils.isNotEmpty(contractEndDateString))) {
				Date contractEndDate = formatter.parse(contractEndDateString);
				gemsEmployeeJobDetail.setContractEndDate(contractEndDate);
			}

			if ((StringUtils.isNotBlank(request.getParameter("dropdown_designation")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_designation")))) {
				GemsDesignation gemsDesignation = masterService
						.getGemsDesignation(Integer.parseInt(request.getParameter("dropdown_designation")));
				gemsEmployeeJobDetail.setGemsDesignation(gemsDesignation);
			}
			
			if ((StringUtils.isNotBlank(request.getParameter("dropdown_department")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_department")))) {
				GemsDepartment gemsDepartment = masterService
						.getGemsDepartment(Integer.parseInt(request.getParameter("dropdown_department")));
				gemsEmployeeJobDetail.setGemsDepartment(gemsDepartment);
			}

			gemsEmployeeMaster.setEmployeeJobDetails(gemsEmployeeJobDetail);

			gemsEmployeeJobDetail.setGemsEmployeeMaster(gemsEmployeeMaster);

			// Address

			String permanentAddressStreet1 = request.getParameter("permanentAddressStreet1");
			gemsEmployeeContactDetail.setPermanentAddressStreet1(permanentAddressStreet1);

			String permanentAddressStreet2 = request.getParameter("permanentAddressStreet2");
			gemsEmployeeContactDetail.setPermanentAddressStreet2(permanentAddressStreet2);

			String permanentAddressCity = request.getParameter("permanentAddressCity");
			gemsEmployeeContactDetail.setPermanentAddressCity(permanentAddressCity);

			String permanentAddressState = request.getParameter("permanentAddressState");
			gemsEmployeeContactDetail.setPermanentAddressState(permanentAddressState);

			String permanentAddressCountry = request.getParameter("permanentAddressCountry");
			gemsEmployeeContactDetail.setPermanentAddressCountry(permanentAddressCountry);

			String permanentAddressZipCode = request.getParameter("permanentAddressZipCode");
			gemsEmployeeContactDetail.setPermanentAddressZipCode(permanentAddressZipCode);

			String correspondenseAddressStreet1 = request.getParameter("correspondenseAddressStreet1");
			gemsEmployeeContactDetail.setCorrespondenseAddressStreet1(correspondenseAddressStreet1);

			String correspondenseAddressStreet2 = request.getParameter("correspondenseAddressStreet2");
			gemsEmployeeContactDetail.setCorrespondenseAddressStreet2(correspondenseAddressStreet2);

			String correspondenseAddressCity = request.getParameter("correspondenseAddressCity");
			gemsEmployeeContactDetail.setCorrespondenseAddressCity(correspondenseAddressCity);

			String correspondenseAddressState = request.getParameter("correspondenseAddressState");
			gemsEmployeeContactDetail.setCorrespondenseAddressState(correspondenseAddressState);

			String correspondenseAddressCountry = request.getParameter("correspondenseAddressCountry");
			gemsEmployeeContactDetail.setCorrespondenseAddressCountry(correspondenseAddressCountry);

			String correspondenseAddressZipCode = request.getParameter("correspondenseAddressZipCode");
			gemsEmployeeContactDetail.setCorrespondenseAddressZipCode(correspondenseAddressZipCode);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmployeeMaster.setActiveStatus(1);
			} else {
				gemsEmployeeMaster.setActiveStatus(0);
			}

			gemsEmployeeMaster.setGemsEmployeeContactDetail(gemsEmployeeContactDetail);
			gemsEmployeeContactDetail.setGemsEmployeeMaster(gemsEmployeeMaster);

			String personalContactNumber = request.getParameter("personalContactNumber");
			gemsEmployeeMaster.setPersonalContactNumber(personalContactNumber);

			String officeContactNumber = request.getParameter("officeContactNumber");
			gemsEmployeeMaster.setOfficeContactNumber(officeContactNumber);

			String personalEmailId = request.getParameter("personalEmailId");
			gemsEmployeeMaster.setPersonalEmailId(personalEmailId);

			String officialEmailid = request.getParameter("officialEmailid");
			gemsEmployeeMaster.setOfficialEmailid(officialEmailid);

			String employeeGender = request.getParameter("employeeGender");
			gemsEmployeeMaster.setEmployeeGender(employeeGender);

			String pfAccountNumber = request.getParameter("pfAccountNumber");
			gemsEmployeeMaster.setPfAccountNumber(pfAccountNumber);

			// String maritalStatus = request.getParameter("maritalStatus");
			// gemsEmployeeMaster.setMaritalStatus(maritalStatus);

			boolean newUserFlag = false;
			if (gemsEmployeeMaster.getGemsUserMaster() != null) {
				GemsUserMaster gemsUserMaster = new GemsUserMaster();
				gemsUserMaster = gemsEmployeeMaster.getGemsUserMaster();
				gemsUserMaster.setUserName(officialEmailid);
				gemsEmployeeMaster.setGemsUserMaster(gemsUserMaster);
				if ((StringUtils.isNotBlank(request.getParameter("dropdown_role")))
						|| (StringUtils.isNotEmpty(request.getParameter("dropdown_role")))) {
					GemsRoleMaster gemsRoleMaster = masterService
							.getGemsRoleMaster(Integer.parseInt(request.getParameter("dropdown_role")));
					gemsUserMaster.setGemsRoleMaster(gemsRoleMaster);
				}

				masterService.saveGemsUserMaster(gemsUserMaster);
			} else {
				GemsUserMaster gemsUserMaster = new GemsUserMaster();
				gemsUserMaster.setUserName(officialEmailid);

				String userPassWord = generateRandomPassword();

				String encryptedPasswordString = hrKeyStoreUtility.getEncryptedStringValue(userPassWord, "E");
				gemsUserMaster.setUserPassword(encryptedPasswordString);
				gemsUserMaster.setUserDecryptPassword(userPassWord);

				gemsUserMaster.setActiveStatus(1);
				gemsUserMaster.setCreatedBy(loggedInUser.getCreatedBy());
				gemsUserMaster.setCreatedOn(todayDate);
				gemsUserMaster.setUpdatedBy(loggedInUser.getCreatedBy());
				gemsUserMaster.setUpdatedOn(todayDate);
				gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

				/*
				 * GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
				 * gemsRoleMaster.setRoleCode("Employee");
				 * gemsRoleMaster.setGemsOrganisation(loggedInUser.
				 * getGemsOrganisation()); gemsRoleMaster =
				 * masterService.getGemsRoleMasterByCode(gemsRoleMaster);
				 */

				if ((StringUtils.isNotBlank(request.getParameter("dropdown_role")))
						|| (StringUtils.isNotEmpty(request.getParameter("dropdown_role")))) {
					GemsRoleMaster gemsRoleMaster = masterService
							.getGemsRoleMaster(Integer.parseInt(request.getParameter("dropdown_role")));
					gemsUserMaster.setGemsRoleMaster(gemsRoleMaster);
				}
				masterService.saveGemsUserMaster(gemsUserMaster);

				newUserFlag = true;

				gemsEmployeeMaster.setGemsUserMaster(gemsUserMaster);

			}

			employeeService.saveGemsEmployeeMaster(gemsEmployeeMaster);

			// String emergencyContactNumber =
			// request.getParameter("emergencyContactNumber");
			// gemsEmployeeMaster.setEmergencyContactNumber(emergencyContactNumber);

			if (gemsEmployeeMaster.getGemsEmployeeMasterId() != 0) {
				gemsEmployeeContactDetail.setGemsEmployeeMaster(gemsEmployeeMaster);
				gemsEmployeeJobDetail.setGemsEmployeeMaster(gemsEmployeeMaster);
			}

			/*
			 * Saving contact info
			 */

			employeeService.saveGemsEmployeeJobDetail(gemsEmployeeJobDetail);
			employeeService.saveGemsEmployeeContactDetail(gemsEmployeeContactDetail);

			if (newUserFlag == true) {
				// Reading From EMail ID from Properties File
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader().getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				String firstName = "";
				if (gemsEmployeeMaster != null) {
					firstName = "" + gemsEmployeeMaster.getEmployeeFirstName() + " "
							+ gemsEmployeeMaster.getEmployeeLastName() + "";
				}

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from", properties_mail.getProperty("javaMailSender.username"));
				map.put("to", gemsEmployeeMaster.getGemsUserMaster().getUserName());
				map.put("subject", "HRMS Application Login Details");
				map.put("firstName", firstName);
				map.put("userEmail", loggedInUser.getUserName());
				map.put("userPassword", loggedInUser.getUserDecryptPassword());
				mailService.sendLoginDetailsEmail(map);
			}

			logger.info("Insert Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return getModelMapEmployeeInfo(gemsEmployeeMaster);
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/deleteEmployee", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteEmployee(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeMaster gemsEmployeeMaster = employeeService.getGemsEmployeeMaster(gemsEmployeeMasterId);
			employeeService.removeGemsEmployeeMaster(gemsEmployeeMaster);
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

	@RequestMapping(value = "/employee/runEmployeeLeaveSummary", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> runemployeeLeaveSummary(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");

		/*
		 * need to check the whole code again in urgency writting the same sorry
		 */

		try {

			GemsEmployeeMaster gemsEmployeeMasterObj = new GemsEmployeeMaster();
			gemsEmployeeMasterObj.setLeaveAllocationStatus(0);
			gemsEmployeeMasterObj.setActiveStatus(1);
			gemsEmployeeMasterObj.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			List<GemsEmployeeMaster> gemsEmployeeMasterList = employeeService
					.getAllGemsEmployeeMasterList(gemsEmployeeMasterObj);

			for (GemsEmployeeMaster gemsEmployeeMaster : gemsEmployeeMasterList) {

				// check if leave allotment done if not then proceed otherwise
				// stop further process

				GemsEmplyeeLeaveSummary existingGemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
				existingGemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
				existingGemsEmplyeeLeaveSummary.setGemsOrganisation(gemsEmployeeMaster.getGemsOrganisation());
				int totalRecords = employeeService
						.getGemsEmplyeeLeaveSummaryFilterCount(existingGemsEmplyeeLeaveSummary);

				if (totalRecords == 0) {

					Date joiningDate = gemsEmployeeMaster.getEmployeeJobDetails().getJoinedDate();
					Date contractStartDate = gemsEmployeeMaster.getEmployeeJobDetails().getContactStartDate();
					Date contractEndDate = gemsEmployeeMaster.getEmployeeJobDetails().getContractEndDate();
					Date confirmationDate = gemsEmployeeMaster.getEmployeeJobDetails().getConfirmationDate();

					Calendar currentDate = Calendar.getInstance();
					int currentMonth = (currentDate.get(Calendar.MONTH) + 1);
					int currentDay = currentDate.get(Calendar.DATE);
					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
					Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

					// Getting Active leave period
					GemsLeavePeriodMaster gemsLeavePeriodMaster = new GemsLeavePeriodMaster();
					gemsLeavePeriodMaster.setActiveStatus(1);
					gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					gemsLeavePeriodMaster = leaveManagementService
							.getActiveGemsLeavePeriodMaster(gemsLeavePeriodMaster);

					LeaveSummayMaster leaveSummayMaster = new LeaveSummayMaster();
					leaveSummayMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					leaveSummayMaster.setGemsEmploymentStatus(gemsEmployeeMaster.getCurrentEmployeeStatus());
					List<LeaveSummayMaster> leaveSummaryList = leaveManagementService
							.getAllLeaveSummayMasteList(leaveSummayMaster);

					for (LeaveSummayMaster leaveSummayMasterObj : leaveSummaryList) {
						GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
						gemsEmplyeeLeaveSummary.setActiveStatus(1);
						gemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
						gemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
						gemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
						gemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
						gemsEmplyeeLeaveSummary.setGemsEmployeeMaster(gemsEmployeeMaster);
						gemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(gemsLeavePeriodMaster);
						gemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(leaveSummayMasterObj.getGemsLeaveTypeMaster());
						gemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());

						if (confirmationDate != null) {
							if (confirmationDate.compareTo(todayDate) < 0) {
								// if current day is above 15th of month then
								// leave balances should be added to next month

								if (leaveSummayMasterObj.getGemsLeaveTypeMaster().getLeaveTypeCode()
										.equalsIgnoreCase("CO")) 
								{
									gemsEmplyeeLeaveSummary.setLeaveBalance(0.0);
									gemsEmplyeeLeaveSummary.setLeaveEntitled(0);
									gemsEmplyeeLeaveSummary.setLeaveScheduled(0);
									gemsEmplyeeLeaveSummary.setLeaveTaken(0);
									employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
								} else {
									if (currentDay > 15) {
										int remainingMonths = 0;
										currentMonth++;
										if (currentMonth < 12) {
											remainingMonths = 12 - currentMonth;
										}

										if (remainingMonths > 0) {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths + 1));

											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										}
										employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
									} else {
										int remainingMonths = 0;
										currentMonth++;
										if (currentMonth < 12) {
											remainingMonths = 12 - currentMonth;
										}
										if (remainingMonths > 0) {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths));
											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										} else {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (1));
											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										}
										employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

									}
								}

							} else {
								if ((contractStartDate.compareTo(todayDate) >= 0)
										&& (todayDate.compareTo(contractEndDate) < 0)) {
									if (currentDay > 15) {
										int remainingMonths = 0;
										currentMonth++;
										if (currentMonth < 12) {
											remainingMonths = 12 - currentMonth;
										}
										if (remainingMonths > 0) {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths + 1));
											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										}
										employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
									} else {
										int remainingMonths = 0;
										currentMonth++;
										if (currentMonth < 12) {
											remainingMonths = 12 - currentMonth;
										}
										if (remainingMonths > 0) {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths));
											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										} else {
											double leaveEntitled = leaveSummayMasterObj.getTotalDays();
											double leaveAllottedDouble = ((leaveEntitled / 12) * (1));
											Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
													.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
											gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
											gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
											gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
										}
										employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

									}
								}
							}
						} else {
							if ((contractStartDate.compareTo(todayDate) >= 0)
									&& (todayDate.compareTo(contractEndDate) < 0)) {
								if (currentDay > 15) {
									int remainingMonths = 0;
									currentMonth++;
									if (currentMonth < 12) {
										remainingMonths = 12 - currentMonth;
									}
									if (remainingMonths > 0) {
										double leaveEntitled = leaveSummayMasterObj.getTotalDays();
										double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths + 1));
										Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
												.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
										gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
										gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
									}

									employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
								} else {
									int remainingMonths = 0;
									currentMonth++;
									if (currentMonth < 12) {
										remainingMonths = 12 - currentMonth;
									}
									if (remainingMonths > 0) {
										double leaveEntitled = leaveSummayMasterObj.getTotalDays();
										double leaveAllottedDouble = ((leaveEntitled / 12) * (remainingMonths));
										Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
												.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
										gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
										gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
									} else {
										double leaveEntitled = leaveSummayMasterObj.getTotalDays();
										double leaveAllottedDouble = ((leaveEntitled / 12) * (1));
										Double leaveAllotted = new BigDecimal(new Double(leaveAllottedDouble))
												.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
										gemsEmplyeeLeaveSummary.setLeaveBalance(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveEntitled(leaveAllotted);
										gemsEmplyeeLeaveSummary.setLeaveScheduled(0.0);
										gemsEmplyeeLeaveSummary.setLeaveTaken(0.0);
									}
									employeeService.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);

								}
							}
						}
					}
				}

				gemsEmployeeMaster.setLeaveAllocationStatus(1);
				employeeService.saveGemsEmployeeMaster(gemsEmployeeMaster);

			}

			logger.info("Leave Allotment Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Allotted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in leave allocation");
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/getEmployeeInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEmployeeInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeMasterIdString = request.getParameter("gemsEmployeeMasterId");

		GemsEmployeeMaster returnedGemsEmployeeMaster = new GemsEmployeeMaster();

		try {
			if (gemsEmployeeMasterIdString != null) {

				Integer gemsEmployeeMasterId = new Integer(gemsEmployeeMasterIdString);

				returnedGemsEmployeeMaster = employeeService.getGemsEmployeeMaster(gemsEmployeeMasterId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapEmployeeInfo(returnedGemsEmployeeMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/myEmploymentInfo.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> myEmploymentInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		try {
			GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			return getModelMapEmployeeInfo(userEmployee);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	// JSon Construction
	private Map<String, Object> getModelMapEmployeeInfo(GemsEmployeeMaster gemsEmployeeMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeMaster)) {
					return new JSONObject(true);
				}

				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) bean;

				HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
				String decryptedSSNValue = "";
				if (gemsEmployeeMaster.getSsnNumber() != null) {
					decryptedSSNValue = hrKeyStoreUtility.getEncryptedStringValue(gemsEmployeeMaster.getSsnNumber(),
							"D");
				}
				String decryptedPANValue = "";
				if (gemsEmployeeMaster.getPanCardNumber() != null) {
					decryptedPANValue = hrKeyStoreUtility.getEncryptedStringValue(gemsEmployeeMaster.getPanCardNumber(),
							"D");
				}
				String employeeDOBString = "";
				if (gemsEmployeeMaster.getEmployeeDob() != null) {
					employeeDOBString = dateFormatter.format(gemsEmployeeMaster.getEmployeeDob());
				}

				// employee Permanent Address
				String dateOfJoining = "";
				String confirmationDate = "";
				String contractStartDate = "";
				String contractEndDate = "";

				if (gemsEmployeeMaster.getEmployeeJobDetails() != null) {

					if (gemsEmployeeMaster.getEmployeeJobDetails().getJoinedDate() != null) {
						dateOfJoining = dateFormatter
								.format(gemsEmployeeMaster.getEmployeeJobDetails().getJoinedDate());
					}
					if (gemsEmployeeMaster.getEmployeeJobDetails().getConfirmationDate() != null) {
						confirmationDate = dateFormatter
								.format(gemsEmployeeMaster.getEmployeeJobDetails().getConfirmationDate());
					}
					if (gemsEmployeeMaster.getEmployeeJobDetails().getContactStartDate() != null) {
						contractStartDate = dateFormatter
								.format(gemsEmployeeMaster.getEmployeeJobDetails().getContactStartDate());
					}
					if (gemsEmployeeMaster.getEmployeeJobDetails().getContactStartDate() != null) {
						contractEndDate = dateFormatter
								.format(gemsEmployeeMaster.getEmployeeJobDetails().getContractEndDate());
					}

				}
				String currentEmployeeStatusName = "";
				Integer currentEmployeeStatusId = new Integer(0);
				if (gemsEmployeeMaster.getCurrentEmployeeStatus() != null) {
					currentEmployeeStatusName = gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusDescription();
					currentEmployeeStatusId = gemsEmployeeMaster.getCurrentEmployeeStatus().getGemsEmploymentStatusId();
				}

				String currentReportingToName = "";
				Integer currentReportingToId = new Integer(0);
				if ((gemsEmployeeMaster.getCurrentReportingTo() != null)
						&& (gemsEmployeeMaster.getCurrentReportingTo().getGemsEmployeeMasterId() != 0)) {

					currentReportingToName = "" + gemsEmployeeMaster.getCurrentReportingTo().getEmployeeLastName() + " "
							+ gemsEmployeeMaster.getCurrentReportingTo().getEmployeeFirstName() + "";
					currentReportingToId = gemsEmployeeMaster.getCurrentReportingTo().getGemsEmployeeMasterId();
				}

				String currentDepartmentName = "";
				Integer currentDepartmentId = new Integer(0);
				if ((gemsEmployeeMaster.getEmployeeJobDetails().getGemsDepartment() != null) && (gemsEmployeeMaster
						.getEmployeeJobDetails().getGemsDepartment().getGemsDepartmentId() != 0)) {

					currentDepartmentName = ""
							+ gemsEmployeeMaster.getEmployeeJobDetails().getGemsDepartment().getDepartmentCode() + "";
					currentDepartmentId = gemsEmployeeMaster.getEmployeeJobDetails().getGemsDepartment()
							.getGemsDepartmentId();
				}

				String currentDesignationName = "";
				Integer currentDesignationId = new Integer(0);
				if ((gemsEmployeeMaster.getEmployeeJobDetails().getGemsDesignation() != null) && (gemsEmployeeMaster
						.getEmployeeJobDetails().getGemsDesignation().getGemsDesignationId() != 0)) {

					currentDesignationName = ""
							+ gemsEmployeeMaster.getEmployeeJobDetails().getGemsDesignation().getGemsDesignationCode()
							+ "";
					currentDesignationId = gemsEmployeeMaster.getEmployeeJobDetails().getGemsDesignation()
							.getGemsDesignationId();
				}

				int selectedRoleId = 0;
				String selectedRole = "";
				if (gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster() != null) {
					selectedRoleId = gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster().getGemsRoleMasterId();
					selectedRole = gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster().getRoleName();
				}

				String pfAccountNumber = "";
				if (gemsEmployeeMaster.getPfAccountNumber() != null) {
					pfAccountNumber = gemsEmployeeMaster.getPfAccountNumber();
				}
				boolean activeStatus = false;

				if (gemsEmployeeMaster.getActiveStatus() == 1) {
					activeStatus = true;
				}
				return new JSONObject().element("gemsEmployeeMasterId", gemsEmployeeMaster.getGemsEmployeeMasterId())
						.element("employeeCode", gemsEmployeeMaster.getEmployeeCode())
						.element("employeeFirstName", gemsEmployeeMaster.getEmployeeFirstName())
						.element("employeeLocation", gemsEmployeeMaster.getEmployeeLocation())
						.element("ssnNumber", decryptedSSNValue)
						.element("employeeLastName", gemsEmployeeMaster.getEmployeeLastName())
						.element("employeeDob", employeeDOBString).element("joinedDate", dateOfJoining)
						.element("confirmationDate", confirmationDate).element("contractStartDate", contractStartDate)
						.element("contractEndDate", contractEndDate)
						.element("currentEmployeeStatusId", currentEmployeeStatusId)
						.element("currentEmployeeStatusName", currentEmployeeStatusName)
						.element("currentReportingToName", currentReportingToName)
						.element("currentReportingToId", currentReportingToId)
						.element("personalEmailId", gemsEmployeeMaster.getPersonalEmailId())
						.element("officialEmailid", gemsEmployeeMaster.getOfficialEmailid())
						.element("personalContactNumber", gemsEmployeeMaster.getPersonalContactNumber())
						.element("officeContactNumber", gemsEmployeeMaster.getOfficeContactNumber())
						.element("emergencyContactNumber", gemsEmployeeMaster.getEmergencyContactNumber())
						.element("currentDesignationName", currentDesignationName)
						.element("currentDesignationId", currentDesignationId)
						.element("currentDepartmentId", currentDepartmentId)
						.element("currentDepartmentName", currentDepartmentName)
						.element("panCardNumber", decryptedPANValue).element("pfAccountNumber", pfAccountNumber)
						.element("correspondenseAddressStreet1",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressCity())
						.element("correspondenseAddressStreet2",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressState())
						.element("correspondenseAddressCity",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressStreet1())
						.element("correspondenseAddressState",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressStreet2())
						.element("correspondenseAddressCountry",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressCity())
						.element("correspondenseAddressZipCode",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressZipCode())
						.element("activeStatus", activeStatus)
						.element("permanentAddressStreet1",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressStreet1())
						.element("permanentAddressStreet2",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressStreet2())
						.element("permanentAddressCity",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressCity())
						.element("permanentAddressState",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressState())
						.element("permanentAddressCountry",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressCountry())
						.element("permanentAddressZipCode",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getPermanentAddressZipCode())
						.element("selectedRoleId", selectedRoleId).element("selectedRole", selectedRole);

			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeList(List<GemsEmployeeMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeMaster)) {
					return new JSONObject(true);
				}

				GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) bean;
				String empName = " ";
				if (gemsEmployeeMaster.getEmployeeFirstName() != null) {
					empName = empName + gemsEmployeeMaster.getEmployeeFirstName();
				}
				if (gemsEmployeeMaster.getEmployeeLastName() != null) {
					empName = empName + " ";
					empName = empName + gemsEmployeeMaster.getEmployeeLastName();
				}
				String employeeDOB = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsEmployeeMaster.getEmployeeDob() != null) {
					employeeDOB = sdf.format(gemsEmployeeMaster.getEmployeeDob());
				}
				HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
				String decryptedSSNValue = "";
				if (gemsEmployeeMaster.getSsnNumber() != null) {
					decryptedSSNValue = hrKeyStoreUtility.getEncryptedStringValue(gemsEmployeeMaster.getSsnNumber(),
							"D");
				}
				String decryptedPANValue = "";
				if (gemsEmployeeMaster.getPanCardNumber() != null) {
					decryptedPANValue = hrKeyStoreUtility.getEncryptedStringValue(gemsEmployeeMaster.getPanCardNumber(),
							"D");
				}
				String pfAccountNumber = "";
				if (gemsEmployeeMaster.getPfAccountNumber() != null) {
					pfAccountNumber = gemsEmployeeMaster.getPfAccountNumber();
				}
				int selectedGemsEmploymentStatusId = 0;
				String selected_employeestatus = "";
				String selected_employeestatusdesc = "";
				if (gemsEmployeeMaster.getCurrentEmployeeStatus() != null) {
					selectedGemsEmploymentStatusId = gemsEmployeeMaster.getCurrentEmployeeStatus()
							.getGemsEmploymentStatusId();
					selected_employeestatus = "" + gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusCode() + "-"
							+ gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusDescription() + "";
					selected_employeestatusdesc = gemsEmployeeMaster.getCurrentEmployeeStatus().getStatusDescription();

				}

				int selectedRoleId = 0;
				String selectedRole = "";
				if (gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster() != null) {
					selectedRoleId = gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster().getGemsRoleMasterId();
					selectedRole = gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster().getRoleName();
				}

				String contactNumber = "Home : ";
				if (gemsEmployeeMaster.getPersonalContactNumber() != null) {
					contactNumber += gemsEmployeeMaster.getPersonalContactNumber();
				}
				contactNumber += "<br>Off : ";
				if (gemsEmployeeMaster.getOfficeContactNumber() != null) {
					contactNumber += gemsEmployeeMaster.getOfficeContactNumber();
				}

				String contactEmail = "Personal : ";
				if (gemsEmployeeMaster.getPersonalEmailId() != null) {
					contactEmail += gemsEmployeeMaster.getPersonalEmailId();
				}
				contactEmail += "<br>Off : ";
				if (gemsEmployeeMaster.getOfficialEmailid() != null) {
					contactEmail += gemsEmployeeMaster.getOfficialEmailid();
				}
				boolean activeStatus = false;
				String activeStatusString = "In-Active";
				if (gemsEmployeeMaster.getActiveStatus() == 1) {
					activeStatus = true;
					activeStatusString = "Active";
				}

				return new JSONObject().element("gemsEmployeeMasterId", gemsEmployeeMaster.getGemsEmployeeMasterId())
						.element("employeeCode", gemsEmployeeMaster.getEmployeeCode())
						.element("employeeCodeName", "" + gemsEmployeeMaster.getEmployeeCode() + " - " + empName + "")
						.element("employeeLocation", gemsEmployeeMaster.getEmployeeLocation())
						.element("ssnNumber", decryptedSSNValue).element("panCardNumber", decryptedPANValue)
						.element("employeeDob", employeeDOB).element("empName", empName)
						.element("employeeGender", gemsEmployeeMaster.getEmployeeGender())
						.element("employeeFirstName", gemsEmployeeMaster.getEmployeeFirstName())
						.element("employeeLastName", gemsEmployeeMaster.getEmployeeLastName())
						.element("pfAccountNumber", pfAccountNumber)
						.element("maritalStatus", gemsEmployeeMaster.getMaritalStatus())
						.element("activeStatus", activeStatus)
						.element("contactAddressCity",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressCity())
						.element("contactAddressState",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressState())
						.element("contactAddressStreet1",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressStreet1())
						.element("contactAddressStreet2",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressStreet2())
						.element("contactAddressZipCode",
								gemsEmployeeMaster.getGemsEmployeeContactDetail().getCorrespondenseAddressZipCode())
						.element("personalEmailId", gemsEmployeeMaster.getPersonalEmailId())
						.element("officialEmailid", gemsEmployeeMaster.getOfficialEmailid())
						.element("contactNumber", contactNumber).element("contactEmail", contactEmail)
						.element("selectedGemsEmploymentStatusId", selectedGemsEmploymentStatusId)
						.element("selected_employeestatus", selected_employeestatus)
						.element("selected_employeestatusdesc", selected_employeestatusdesc)
						.element("selectedRoleId", selectedRoleId).element("selectedRole", selectedRole)
						.element("activeStatusString", activeStatusString);
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
	// Generate Password Dynamically Method

	private static final Random RANDOM = new SecureRandom();
	/** Length of password. @see #generateRandomPassword() */
	public static final int PASSWORD_LENGTH = 8;

	public static String generateRandomPassword() {

		// Pick from some letters that won't be easily mistaken for each
		// other. So, for example, omit o O and 0, 1 l and L.
		String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

		String pw = "";
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			int index = (int) (RANDOM.nextDouble() * letters.length());
			pw += letters.substring(index, index + 1);
		}
		return pw;
	}
	/*
	 * public static String base64EncryptPassword(String token) { byte[]
	 * encodedBytes = Base64.encode(token.getBytes()); return new
	 * String(encodedBytes, Charset.forName("UTF-8")); }
	 * 
	 * public static String base64DecryptPassword(String token) { byte[]
	 * decodedBytes = Base64.decode(token.getBytes()); return new
	 * String(decodedBytes, Charset.forName("UTF-8")); }
	 */
}
