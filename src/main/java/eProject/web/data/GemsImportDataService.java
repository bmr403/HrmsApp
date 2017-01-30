package eProject.web.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeImmigrationDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmployeePaySlipDetail;
import eProject.domain.employee.GemsEmplyeeLeaveSummary;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;
import eProject.service.customer.CustomerService;
import eProject.service.employee.EmployeeService;
import eProject.service.leavemanagement.LeaveManagementService;
import eProject.service.mail.MailService;
import eProject.service.master.MasterService;
import eProject.service.project.ProjectService;
import eProject.utility.ConstantVariables;
import eProject.utility.HrKeyStoreUtility;
import eProject.utility.PayslipUtility;
import eProject.web.project.GemsProjectMasterService;

@Controller
public class GemsImportDataService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private MailService mailService;

	@Autowired
	private LeaveManagementService leaveManagementService;

	@Autowired
	private CustomerService customerService;

	protected final Log logger = LogFactory.getLog(GemsImportDataService.class);

	@RequestMapping(value = "/data/importData", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmployeePaySlipDetail(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String importDoc = "";
			if ((StringUtils.isNotBlank(request.getParameter("import_doc")))
					|| (StringUtils.isNotEmpty(request.getParameter("import_doc")))) {
				importDoc = request.getParameter("import_doc");

			}

			if (file.getSize() > 0) {
				// loggedInUser.setProfileImgData(file.getBytes());

				if (importDoc.equalsIgnoreCase("Employee")) {

					GemsRoleMaster searchGemsRoleMaster = new GemsRoleMaster();
					searchGemsRoleMaster.setRoleCode("Employee");
					searchGemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					GemsRoleMaster employeeRole = masterService.getGemsRoleMasterByCode(searchGemsRoleMaster);

					String uploadDirectoryBase = System.getProperty("catalina.base");
					String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator
							+ "upload" + File.separator;

					File docFileRepo = new File(documentFileRepo);
					if (!(docFileRepo.exists())) {
						docFileRepo.createNewFile();
					}
					in = file.getInputStream();
					out = new FileOutputStream(
							new File(documentFileRepo + File.separator + file.getOriginalFilename()));
					int read = 0;
					final byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					FileInputStream input = new FileInputStream(
							documentFileRepo + File.separator + file.getOriginalFilename());

					// Create Workbook instance holding reference to .xlsx file
					XSSFWorkbook workbook = new XSSFWorkbook(input);

					// Get first/desired sheet from the workbook

					// Employee General Information

					XSSFSheet sheet = workbook.getSheetAt(0);
					boolean employeeExist = false;
					Iterator<Row> rowIterator = sheet.iterator();
					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						if (row.getRowNum() > 0) {
							GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
							GemsEmployeeContactDetail gemsEmployeeContactDetail = new GemsEmployeeContactDetail();
							GemsEmployeeJobDetail gemsEmployeeJobDetail = new GemsEmployeeJobDetail();
							HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
							GemsUserMaster gemsUserMaster = new GemsUserMaster();

							// int id = (int)
							// row.getCell(0).getNumericCellValue();
							if (!(row.getCell(0) == null)) {
								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
								String employeeCode = row.getCell(0).getStringCellValue();
								GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
								searchGemsEmployeeMaster.setEmployeeCode(employeeCode);
								searchGemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsEmployeeMaster existEmployee = employeeService
										.getGemsEmployeeMasterByCode(searchGemsEmployeeMaster);
								if (existEmployee != null) {
									gemsEmployeeMaster.setEmployeeCode(employeeCode.toUpperCase());
									gemsEmployeeMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
									gemsEmployeeMaster.setUpdatedOn(todayDate);
									gemsEmployeeMaster.setGemsEmployeeMasterId(existEmployee.getGemsEmployeeMasterId());
								} else {
									// employeeExist = true;
									gemsEmployeeMaster.setEmployeeCode(employeeCode);
									gemsEmployeeMaster.setCreatedBy(loggedInUser.getGemsUserMasterId());
									gemsEmployeeMaster.setCreatedOn(todayDate);
								}

							}

							if (!(row.getCell(1) == null)) {
								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
								String firstName = row.getCell(1).getStringCellValue();
								if ((StringUtils.isNotBlank(firstName)) || (StringUtils.isNotEmpty(firstName))) {
									gemsEmployeeMaster.setEmployeeFirstName(firstName);
								}

							}
							if (!(row.getCell(2) == null)) {
								row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
								String lastName = row.getCell(2).getStringCellValue();
								if ((StringUtils.isNotBlank(lastName)) || (StringUtils.isNotEmpty(lastName))) {
									gemsEmployeeMaster.setEmployeeLastName(lastName);
								}

							}
							if (!(row.getCell(3) == null)) {
								row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
								String dateOfBirthString = row.getCell(3).getStringCellValue();
								if ((StringUtils.isNotBlank(dateOfBirthString))
										|| (StringUtils.isNotEmpty(dateOfBirthString))) {
									Date employeeDob = formatter.parse(dateOfBirthString);
									gemsEmployeeMaster.setEmployeeDob(employeeDob);
								}

							}
							if (!(row.getCell(4) == null)) {
								row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
								String personalContactNumber = String.valueOf(row.getCell(4).getStringCellValue());
								if ((StringUtils.isNotBlank(personalContactNumber))
										|| (StringUtils.isNotEmpty(personalContactNumber))) {
									gemsEmployeeMaster.setPersonalContactNumber(
											String.valueOf(row.getCell(4).getStringCellValue()));
								}

							}
							if (!(row.getCell(5) == null)) {
								row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
								String officeContactNumber = row.getCell(5).getStringCellValue();
								if ((StringUtils.isNotBlank(officeContactNumber))
										|| (StringUtils.isNotEmpty(officeContactNumber))) {
									gemsEmployeeMaster.setOfficeContactNumber(officeContactNumber);
								}

							}
							if (!(row.getCell(6) == null)) {
								row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
								String personalEmailId = row.getCell(6).getStringCellValue();
								if ((StringUtils.isNotBlank(personalEmailId))
										|| (StringUtils.isNotEmpty(personalEmailId))) {
									gemsEmployeeMaster.setPersonalEmailId(personalEmailId);
								}

							}
							if (!(row.getCell(7) == null)) {
								row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
								String officialEmailid = row.getCell(7).getStringCellValue();
								if ((StringUtils.isNotBlank(officialEmailid))
										|| (StringUtils.isNotEmpty(officialEmailid))) {
									gemsEmployeeMaster.setOfficialEmailid(officialEmailid);
								}

								gemsUserMaster.setUserName(officialEmailid);

								// String userPassWord =
								// generateRandomPassword();
								String userPassWord = "BpaMyCompany";

								String encryptedPasswordString = hrKeyStoreUtility.getEncryptedStringValue(userPassWord,
										"E");
								gemsUserMaster.setUserPassword(encryptedPasswordString);
								gemsUserMaster.setUserDecryptPassword(userPassWord);

								gemsUserMaster.setActiveStatus(1);
								gemsUserMaster.setCreatedBy(loggedInUser.getCreatedBy());
								gemsUserMaster.setCreatedOn(todayDate);
								gemsUserMaster.setUpdatedBy(loggedInUser.getCreatedBy());
								gemsUserMaster.setUpdatedOn(todayDate);
								gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								gemsUserMaster.setGemsRoleMaster(employeeRole);

							}
							if (!(row.getCell(8) == null)) {
								row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
								String employmentStatus = row.getCell(8).getStringCellValue();
								if ((StringUtils.isNotBlank(employmentStatus))
										|| (StringUtils.isNotEmpty(employmentStatus))) {
									String[] employementStatusArray = employmentStatus.split("-");
									String employementStatusCode = employementStatusArray[0].toString();
									GemsEmploymentStatus searchGemsEmploymentStatus = new GemsEmploymentStatus();
									searchGemsEmploymentStatus.setStatusCode(employementStatusCode);
									searchGemsEmploymentStatus.setGemsOrganisation(loggedInUser.getGemsOrganisation());
									GemsEmploymentStatus gemsEmploymentStatus = masterService
											.getGemsEmploymentStatusByCode(searchGemsEmploymentStatus);
									gemsEmployeeMaster.setCurrentEmployeeStatus(gemsEmploymentStatus);
								}

							}
							if (!(row.getCell(9) == null)) {
								row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
								String workLocation = row.getCell(9).getStringCellValue();
								if ((StringUtils.isNotBlank(workLocation)) || (StringUtils.isNotEmpty(workLocation))) {
									gemsEmployeeMaster.setEmployeeLocation(workLocation);
								}

							}
							if (!(row.getCell(10) == null)) {
								row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
								String reportingManagerString = row.getCell(10).getStringCellValue();
								if ((StringUtils.isNotBlank(reportingManagerString))
										|| (StringUtils.isNotEmpty(reportingManagerString))) {
									GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
									searchGemsEmployeeMaster.setEmployeeCode(reportingManagerString);
									searchGemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
									GemsEmployeeMaster reportingManager = employeeService
											.getGemsEmployeeMasterByCode(searchGemsEmployeeMaster);
									gemsEmployeeMaster.setCurrentReportingTo(reportingManager);
								}

							}
							if (!(row.getCell(11) == null)) {
								row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
								String permanentAddressStreet1 = row.getCell(11).getStringCellValue();
								if ((StringUtils.isNotBlank(permanentAddressStreet1))
										|| (StringUtils.isNotEmpty(permanentAddressStreet1))) {
									gemsEmployeeContactDetail.setPermanentAddressStreet1(permanentAddressStreet1);
								}

							}

							if (!(row.getCell(12) == null)) {
								row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
								String permanentAddressCity = row.getCell(12).getStringCellValue();
								if ((StringUtils.isNotBlank(permanentAddressCity))
										|| (StringUtils.isNotEmpty(permanentAddressCity))) {
									gemsEmployeeContactDetail.setPermanentAddressCity(permanentAddressCity);
								}

							}
							if (!(row.getCell(13) == null)) {
								row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
								String permanentAddressState = row.getCell(13).getStringCellValue();
								if ((StringUtils.isNotBlank(permanentAddressState))
										|| (StringUtils.isNotEmpty(permanentAddressState))) {
									gemsEmployeeContactDetail.setPermanentAddressState(permanentAddressState);
								}

							}
							if (!(row.getCell(14) == null)) {
								row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
								String permanentAddressCountry = row.getCell(14).getStringCellValue();
								if ((StringUtils.isNotBlank(permanentAddressCountry))
										|| (StringUtils.isNotEmpty(permanentAddressCountry))) {
									gemsEmployeeContactDetail.setPermanentAddressCountry(permanentAddressCountry);
								}

							}
							if (!(row.getCell(15) == null)) {
								row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
								String permanentAddressZipCode = row.getCell(15).getStringCellValue();
								if ((StringUtils.isNotBlank(permanentAddressZipCode))
										|| (StringUtils.isNotEmpty(permanentAddressZipCode))) {
									gemsEmployeeContactDetail.setPermanentAddressZipCode(permanentAddressZipCode);
								}

							}
							if (!(row.getCell(16) == null)) {
								row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
								String correspondenseAddressStreet1 = row.getCell(16).getStringCellValue();
								if ((StringUtils.isNotBlank(correspondenseAddressStreet1))
										|| (StringUtils.isNotEmpty(correspondenseAddressStreet1))) {
									gemsEmployeeContactDetail
											.setCorrespondenseAddressStreet1(correspondenseAddressStreet1);
								}

							}
							if (!(row.getCell(17) == null)) {
								row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
								String correspondenseAddressCity = row.getCell(17).getStringCellValue();
								if ((StringUtils.isNotBlank(correspondenseAddressCity))
										|| (StringUtils.isNotEmpty(correspondenseAddressCity))) {
									gemsEmployeeContactDetail.setCorrespondenseAddressCity(correspondenseAddressCity);
								}

							}
							if (!(row.getCell(18) == null)) {
								row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
								String correspondenseAddressState = row.getCell(18).getStringCellValue();
								if ((StringUtils.isNotBlank(correspondenseAddressState))
										|| (StringUtils.isNotEmpty(correspondenseAddressState))) {
									gemsEmployeeContactDetail.setCorrespondenseAddressState(correspondenseAddressState);
								}

							}
							if (!(row.getCell(19) == null)) {
								row.getCell(19).setCellType(Cell.CELL_TYPE_STRING);
								String correspondenseAddressCountry = row.getCell(19).getStringCellValue();
								if ((StringUtils.isNotBlank(correspondenseAddressCountry))
										|| (StringUtils.isNotEmpty(correspondenseAddressCountry))) {
									gemsEmployeeContactDetail
											.setCorrespondenseAddressCountry(correspondenseAddressCountry);
								}

							}
							if (!(row.getCell(20) == null)) {
								row.getCell(20).setCellType(Cell.CELL_TYPE_STRING);
								String correspondenseAddressZipCode = row.getCell(20).getStringCellValue();
								if ((StringUtils.isNotBlank(correspondenseAddressZipCode))
										|| (StringUtils.isNotEmpty(correspondenseAddressZipCode))) {
									gemsEmployeeContactDetail
											.setCorrespondenseAddressZipCode(correspondenseAddressZipCode);
								}

							}
							if (!(row.getCell(21) == null)) {
								row.getCell(21).setCellType(Cell.CELL_TYPE_STRING);
								String joinedDateString = row.getCell(21).getStringCellValue();
								if ((StringUtils.isNotBlank(joinedDateString))
										|| (StringUtils.isNotEmpty(joinedDateString))) {
									Date joinedDate = formatter.parse(joinedDateString);
									gemsEmployeeJobDetail.setJoinedDate(joinedDate);
								}
							}
							if (!(row.getCell(22) == null)) {
								row.getCell(22).setCellType(Cell.CELL_TYPE_STRING);
								String contactStartDateString = row.getCell(22).getStringCellValue();
								if ((StringUtils.isNotBlank(contactStartDateString))
										|| (StringUtils.isNotEmpty(contactStartDateString))) {
									Date contractStartDate = formatter.parse(contactStartDateString);
									gemsEmployeeJobDetail.setContactStartDate(contractStartDate);

									Calendar calendar = Calendar.getInstance();
									calendar.setTime(contractStartDate);
									calendar.add(Calendar.MONTH, 6);
									Date contractEndDate = calendar.getTime();
									gemsEmployeeJobDetail.setContractEndDate(contractEndDate);

									Calendar calendar1 = Calendar.getInstance();
									calendar1.setTime(contractEndDate);
									calendar1.add(Calendar.DAY_OF_YEAR, 1);
									Date confirmationDate = calendar1.getTime();
									gemsEmployeeJobDetail.setConfirmationDate(confirmationDate);

								}

							}
							if (!(row.getCell(23) == null)) {
								row.getCell(23).setCellType(Cell.CELL_TYPE_STRING);
								String contractEndDateString = row.getCell(23).getStringCellValue();
								if ((StringUtils.isNotBlank(contractEndDateString))
										|| (StringUtils.isNotEmpty(contractEndDateString))) {
									Date contractEndDate = formatter.parse(contractEndDateString);
									gemsEmployeeJobDetail.setContractEndDate(contractEndDate);
								}

							}
							if (!(row.getCell(24) == null)) {
								row.getCell(24).setCellType(Cell.CELL_TYPE_STRING);
								String confirmationDateString = row.getCell(24).getStringCellValue();
								if ((StringUtils.isNotBlank(confirmationDateString))
										|| (StringUtils.isNotEmpty(confirmationDateString))) {
									Date confirmationDate = formatter.parse(confirmationDateString);
									gemsEmployeeJobDetail.setConfirmationDate(confirmationDate);
								}

							}
							if (!(row.getCell(25) == null)) {
								String departmentCode = row.getCell(25).getStringCellValue();
								if ((StringUtils.isNotBlank(departmentCode))
										|| (StringUtils.isNotEmpty(departmentCode))) {
									GemsDepartment searchDepartment = new GemsDepartment();
									searchDepartment.setDepartmentCode(departmentCode);
									searchDepartment.setGemsOrganisation(loggedInUser.getGemsOrganisation());
									GemsDepartment department = masterService.getGemsDepartmentByCode(searchDepartment);
									gemsEmployeeJobDetail.setGemsDepartment(department);
								}

							}
							if (!(row.getCell(26) == null)) {
								String designationCode = row.getCell(26).getStringCellValue();
								if ((StringUtils.isNotBlank(designationCode))
										|| (StringUtils.isNotEmpty(designationCode))) {
									GemsDesignation searchDesignation = new GemsDesignation();
									searchDesignation.setGemsDesignationCode(designationCode);
									searchDesignation.setGemsOrganisation(loggedInUser.getGemsOrganisation());
									GemsDesignation designation = masterService
											.getGemsDesignationByCode(searchDesignation);
									gemsEmployeeJobDetail.setGemsDesignation(designation);
								}

							}
							if (!(row.getCell(27) == null)) {
								row.getCell(27).setCellType(Cell.CELL_TYPE_STRING);
								String panCardNumber = row.getCell(27).getStringCellValue();
								if ((StringUtils.isNotBlank(panCardNumber))
										|| (StringUtils.isNotEmpty(panCardNumber))) {
									String encryptedPanCardValue = hrKeyStoreUtility
											.getEncryptedStringValue(panCardNumber, "E");
									gemsEmployeeMaster.setPanCardNumber(encryptedPanCardValue);
								}

							}
							if (!(row.getCell(28) == null)) {
								row.getCell(28).setCellType(Cell.CELL_TYPE_STRING);
								String ssnNumber = row.getCell(28).getStringCellValue();
								if ((StringUtils.isNotBlank(ssnNumber)) || (StringUtils.isNotEmpty(ssnNumber))) {
									String encryptedSSNValue = hrKeyStoreUtility.getEncryptedStringValue(ssnNumber,
											"E");
									gemsEmployeeMaster.setSsnNumber(encryptedSSNValue);
								}

							}
							if (!(row.getCell(29) == null)) {
								row.getCell(29).setCellType(Cell.CELL_TYPE_STRING);
								String epfAccountNumber = row.getCell(29).getStringCellValue();
								if ((StringUtils.isNotBlank(epfAccountNumber))
										|| (StringUtils.isNotEmpty(epfAccountNumber))) {
									gemsEmployeeMaster.setPfAccountNumber(epfAccountNumber);
								}

							}

							masterService.saveGemsUserMaster(gemsUserMaster);
							gemsEmployeeMaster.setGemsUserMaster(gemsUserMaster);
							gemsEmployeeMaster.setActiveStatus(1);
							gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							employeeService.saveGemsEmployeeMaster(gemsEmployeeMaster);
							if (gemsEmployeeMaster.getGemsEmployeeMasterId() != 0) {
								gemsEmployeeContactDetail.setGemsEmployeeMaster(gemsEmployeeMaster);
								gemsEmployeeContactDetail.setActiveStatus(1);
								gemsEmployeeContactDetail.setCreatedBy(loggedInUser.getGemsUserMasterId());
								gemsEmployeeContactDetail.setCreatedOn(todayDate);
								gemsEmployeeContactDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
								gemsEmployeeContactDetail.setUpdatedOn(todayDate);
								gemsEmployeeJobDetail.setGemsEmployeeMaster(gemsEmployeeMaster);
								gemsEmployeeJobDetail.setActiveStatus(1);
								gemsEmployeeJobDetail.setCreatedBy(loggedInUser.getGemsUserMasterId());
								gemsEmployeeJobDetail.setCreatedOn(todayDate);
								gemsEmployeeJobDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
								gemsEmployeeJobDetail.setUpdatedOn(todayDate);
							}
							employeeService.saveGemsEmployeeJobDetail(gemsEmployeeJobDetail);
							employeeService.saveGemsEmployeeContactDetail(gemsEmployeeContactDetail);

							System.out.println("Import rows " + row.getRowNum());

						}

					}

					out.flush();
					in.close();
					out.close();
					in = null;
					out = null;
				} else if (importDoc.equalsIgnoreCase("Projects")) {

					String uploadDirectoryBase = System.getProperty("catalina.base");
					String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator
							+ "upload" + File.separator;

					File docFileRepo = new File(documentFileRepo);
					if (!(docFileRepo.exists())) {
						docFileRepo.createNewFile();
					}
					in = file.getInputStream();
					out = new FileOutputStream(
							new File(documentFileRepo + File.separator + file.getOriginalFilename()));
					int read = 0;
					final byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					FileInputStream input = new FileInputStream(
							documentFileRepo + File.separator + file.getOriginalFilename());

					// Create Workbook instance holding reference to .xlsx file
					XSSFWorkbook workbook = new XSSFWorkbook(input);

					// Get first/desired sheet from the workbook

					// Project General Information

					XSSFSheet sheet = workbook.getSheetAt(0);

					Iterator<Row> rowIterator = sheet.iterator();

					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						if (row.getRowNum() > 0) {
							GemsProjectMaster gemsProjectmaster = new GemsProjectMaster();
							gemsProjectmaster.setActiveStatus(1);
							gemsProjectmaster.setCreatedBy(loggedInUser.getGemsUserMasterId());
							gemsProjectmaster.setCreatedOn(todayDate);
							gemsProjectmaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
							gemsProjectmaster.setUpdatedOn(todayDate);
							gemsProjectmaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							// int id = (int)
							// row.getCell(0).getNumericCellValue();
							if (!(row.getCell(0) == null)) {
								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
								String projectCode = row.getCell(0).getStringCellValue();
								GemsProjectMaster searchGemsProjectMaster = new GemsProjectMaster();
								searchGemsProjectMaster.setProjectCode(projectCode);
								searchGemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsProjectMaster existProject = projectService
										.getGemsProjectMasterByCode(searchGemsProjectMaster);
								if (existProject != null) {
									gemsProjectmaster.setGemsProjectMasterId(existProject.getGemsProjectMasterId());
									existProject.setProjectCode(projectCode.toUpperCase());
								} else {
									gemsProjectmaster.setProjectCode(projectCode.toUpperCase());
								}

							}
							if (!(row.getCell(1) == null)) {
								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
								String projectName = row.getCell(1).getStringCellValue();
								gemsProjectmaster.setProjectName(projectName);
							}
							if (!(row.getCell(2) == null)) {
								row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
								String projectDescription = row.getCell(2).getStringCellValue();
								gemsProjectmaster.setProjectDescription(projectDescription);
							}
							if (!(row.getCell(3) == null)) {
								row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
								String startDateString = row.getCell(3).getStringCellValue();
								if ((StringUtils.isNotBlank(startDateString))
										|| (StringUtils.isNotEmpty(startDateString))) {
									Date startDate = formatter.parse(startDateString);
									gemsProjectmaster.setProjectStartDate(startDate);
								}
							}
							if (!(row.getCell(4) == null)) {
								row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
								String endDateString = row.getCell(4).getStringCellValue();
								if ((StringUtils.isNotBlank(endDateString))
										|| (StringUtils.isNotEmpty(endDateString))) {
									Date endDate = formatter.parse(endDateString);
									gemsProjectmaster.setProjectEndDate(endDate);
								}

							}
							if (!(row.getCell(5) == null)) {
								row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
								String customerCodeString = row.getCell(5).getStringCellValue();
								GemsCustomerMaster searchGemsCustomerMaster = new GemsCustomerMaster();
								searchGemsCustomerMaster.setGemsCustomerCode(customerCodeString);
								searchGemsCustomerMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsCustomerMaster gemsCustomerMaster = customerService
										.getGemsCustomerMasterByCode(searchGemsCustomerMaster);
								if (gemsCustomerMaster != null) {
									gemsProjectmaster.setGemsCustomerMaster(gemsCustomerMaster);
								}

							}
							if (!(row.getCell(6) == null)) {
								row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
								String projectTypeString = row.getCell(6).getStringCellValue();
								GemsProjectTypeMaster searchGemsProjectTypeMaster = new GemsProjectTypeMaster();
								searchGemsProjectTypeMaster.setProjectTypeCode(projectTypeString);
								searchGemsProjectTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsProjectTypeMaster gemsProjectTypeMaster = projectService
										.getGemsProjectTypeMasterByCode(searchGemsProjectTypeMaster);
								if (gemsProjectTypeMaster != null) {
									gemsProjectmaster.setProjectTypeMaster(gemsProjectTypeMaster);
								}

							}
							if (!(row.getCell(7) == null)) {
								row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
								String billingType = row.getCell(7).getStringCellValue();
								gemsProjectmaster.setBillingType(billingType);

							}
							if (!(row.getCell(8) == null)) {
								row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
								String cost = row.getCell(8).getStringCellValue();
								gemsProjectmaster.setProjectCost(new BigDecimal(cost));

							}
							if (!(row.getCell(9) == null)) {
								row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
								String projectStatus = row.getCell(9).getStringCellValue();
								gemsProjectmaster.setProjectStatus(projectStatus);

							}
							projectService.saveGemsProjectMaster(gemsProjectmaster);

						}
					}

				} 
				else if (importDoc.equalsIgnoreCase("Project - Resources")) 
				{
					String uploadDirectoryBase = System.getProperty("catalina.base");
					String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator
							+ "upload" + File.separator;

					File docFileRepo = new File(documentFileRepo);
					if (!(docFileRepo.exists())) {
						docFileRepo.createNewFile();
					}
					in = file.getInputStream();
					out = new FileOutputStream(
							new File(documentFileRepo + File.separator + file.getOriginalFilename()));
					int read = 0;
					final byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					FileInputStream input = new FileInputStream(
							documentFileRepo + File.separator + file.getOriginalFilename());

					// Create Workbook instance holding reference to .xlsx file
					XSSFWorkbook workbook = new XSSFWorkbook(input);

					// Get first/desired sheet from the workbook

					// Project General Information

					XSSFSheet sheet = workbook.getSheetAt(0);

					Iterator<Row> rowIterator = sheet.iterator();

					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						if (row.getRowNum() > 0) {
							GemsProjectResourceMaster gemsProjectResourceMaster = new GemsProjectResourceMaster();
							gemsProjectResourceMaster.setActiveStatus(1);
							gemsProjectResourceMaster.setCreatedBy(loggedInUser.getGemsUserMasterId());
							gemsProjectResourceMaster.setCreatedOn(todayDate);
							gemsProjectResourceMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
							gemsProjectResourceMaster.setUpdatedOn(todayDate);

							// int id = (int)
							// row.getCell(0).getNumericCellValue();
							if (!(row.getCell(0) == null)) {
								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
								String projectCode = row.getCell(0).getStringCellValue();
								GemsProjectMaster searchGemsProjectMaster = new GemsProjectMaster();
								searchGemsProjectMaster.setProjectCode(projectCode);
								searchGemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsProjectMaster existProject = projectService
										.getGemsProjectMasterByCode(searchGemsProjectMaster);
								gemsProjectResourceMaster.setGemsProjectMaster(existProject);

							}
							if (!(row.getCell(1) == null)) {
								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
								String employeeCode = row.getCell(1).getStringCellValue();
								GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
								searchGemsEmployeeMaster.setEmployeeCode(employeeCode);
								searchGemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
								GemsEmployeeMaster existEmployee = employeeService
										.getGemsEmployeeMasterByCode(searchGemsEmployeeMaster);
								gemsProjectResourceMaster.setGemsEmployeeMaster(existEmployee);
							}
							if (!(row.getCell(2) == null)) {
								row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
								String startDateString = row.getCell(2).getStringCellValue();
								if ((StringUtils.isNotBlank(startDateString))
										|| (StringUtils.isNotEmpty(startDateString))) {
									Date startDate = formatter.parse(startDateString);
									gemsProjectResourceMaster.setResourceStartDate(startDate);
								}
							}
							if (!(row.getCell(3) == null)) {
								row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
								String endDateString = row.getCell(3).getStringCellValue();
								if ((StringUtils.isNotBlank(endDateString))
										|| (StringUtils.isNotEmpty(endDateString))) {
									Date endDate = formatter.parse(endDateString);
									gemsProjectResourceMaster.setResourceEndDate(endDate);
								}

							}
							if (!(row.getCell(4) == null)) {
								row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
								String billingCostString = row.getCell(4).getStringCellValue();
								if ((StringUtils.isNotBlank(billingCostString))
										|| (StringUtils.isNotEmpty(billingCostString))) {

									gemsProjectResourceMaster.setProjectBillingRate(new BigDecimal(billingCostString));
								}

							}
							projectService.saveGemsProjectResourceMaster(gemsProjectResourceMaster);

						}
					}
				}
				else if (importDoc.equalsIgnoreCase("Leave Balance")) 
				{
					String uploadDirectoryBase = System.getProperty("catalina.base");
					String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator
							+ "upload" + File.separator;

					File docFileRepo = new File(documentFileRepo);
					if (!(docFileRepo.exists())) {
						docFileRepo.createNewFile();
					}
					in = file.getInputStream();
					out = new FileOutputStream(
							new File(documentFileRepo + File.separator + file.getOriginalFilename()));
					int read = 0;
					final byte[] bytes = new byte[1024];
					while ((read = in.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					FileInputStream input = new FileInputStream(
							documentFileRepo + File.separator + file.getOriginalFilename());

					// Create Workbook instance holding reference to .xlsx file
					XSSFWorkbook workbook = new XSSFWorkbook(input);

					// Get first/desired sheet from the workbook

					// Project General Information

					XSSFSheet sheet = workbook.getSheetAt(0);

					Iterator<Row> rowIterator = sheet.iterator();
					
					GemsLeavePeriodMaster gemsLeavePeriodMaster = new GemsLeavePeriodMaster();
					gemsLeavePeriodMaster.setActiveStatus(1);
					gemsLeavePeriodMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					gemsLeavePeriodMaster = leaveManagementService
							.getActiveGemsLeavePeriodMaster(gemsLeavePeriodMaster);
					
					GemsLeaveTypeMaster searchLeaveTypeMaster = new GemsLeaveTypeMaster();
					searchLeaveTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
					List <GemsLeaveTypeMaster> leaveTypeMaterList = leaveManagementService.getAllLeaveTypeList(searchLeaveTypeMaster);
					
					GemsLeaveTypeMaster plLeaveTypeMaster = null;
					GemsLeaveTypeMaster slLeaveTypeMaster = null;
					for (GemsLeaveTypeMaster gemsLeaveTypeMaster : leaveTypeMaterList)
					{
						if ((gemsLeaveTypeMaster.getLeaveTypeCode() == "PL") || (gemsLeaveTypeMaster.getLeaveTypeCode().equalsIgnoreCase("PL")))
						{
							plLeaveTypeMaster = gemsLeaveTypeMaster;
						}
						if ((gemsLeaveTypeMaster.getLeaveTypeCode() == "SL") || (gemsLeaveTypeMaster.getLeaveTypeCode().equalsIgnoreCase("SL")))
						{
							slLeaveTypeMaster = gemsLeaveTypeMaster;
						} 
					}
					
					List<GemsEmplyeeLeaveSummary> gemsEmployeeLeaveSummaryList = new ArrayList();

					while (rowIterator.hasNext()) {
						Row row = rowIterator.next();
						if (row.getRowNum() > 0) {
							
							
							
							GemsEmplyeeLeaveSummary plGemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
							plGemsEmplyeeLeaveSummary.setActiveStatus(1);
							plGemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
							plGemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
							plGemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
							plGemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
							plGemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(gemsLeavePeriodMaster);
							plGemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							
							GemsEmplyeeLeaveSummary slGemsEmplyeeLeaveSummary = new GemsEmplyeeLeaveSummary();
							slGemsEmplyeeLeaveSummary.setActiveStatus(1);
							slGemsEmplyeeLeaveSummary.setCreatedBy(loggedInUser.getGemsUserMasterId());
							slGemsEmplyeeLeaveSummary.setCreatedOn(todayDate);
							slGemsEmplyeeLeaveSummary.setUpdatedBy(loggedInUser.getGemsUserMasterId());
							slGemsEmplyeeLeaveSummary.setUpdatedOn(todayDate);
							slGemsEmplyeeLeaveSummary.setGemsLeavePeriodMaster(gemsLeavePeriodMaster);
							slGemsEmplyeeLeaveSummary.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							
							
							
							if (!(row.getCell(0) == null)) {
								row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
								String employeeCode = row.getCell(0).getStringCellValue();
								if (employeeCode.equalsIgnoreCase("BPA166"))
								{
									System.out.println("I am inside");
								}
								if ((StringUtils.isNotBlank(employeeCode))
										|| (StringUtils.isNotEmpty(employeeCode))) {
									GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
									searchGemsEmployeeMaster.setEmployeeCode(employeeCode.trim());
									searchGemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
									GemsEmployeeMaster existEmployee = employeeService
											.getGemsEmployeeMasterByCode(searchGemsEmployeeMaster);
									if (existEmployee != null)
									{
										plGemsEmplyeeLeaveSummary.setGemsEmployeeMaster(existEmployee);
										slGemsEmplyeeLeaveSummary.setGemsEmployeeMaster(existEmployee);
									}
									else
									{
										System.out.println("I am here");
									}
								}
															
								
							}
							
							if (!(row.getCell(1) == null)) {
								row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
								String plLeave = row.getCell(1).getStringCellValue();
								if ((StringUtils.isNotBlank(plLeave))
										|| (StringUtils.isNotEmpty(plLeave))) {
									
									if (new Double(plLeave) > 24)
									{
										plGemsEmplyeeLeaveSummary.setLeaveEntitled(new Double(24));
										plGemsEmplyeeLeaveSummary.setLeaveBalance(new Double(24));
									}
									else
									{
										plGemsEmplyeeLeaveSummary.setLeaveEntitled(new Double(plLeave));
										plGemsEmplyeeLeaveSummary.setLeaveBalance(new Double(plLeave));
									}
									
								}
								else
								{
									plGemsEmplyeeLeaveSummary.setLeaveBalance(0);
									plGemsEmplyeeLeaveSummary.setLeaveEntitled(0);
								}
								plGemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(plLeaveTypeMaster);
								
								plGemsEmplyeeLeaveSummary.setLeaveScheduled(0);
								plGemsEmplyeeLeaveSummary.setLeaveTaken(0);
								
								
							}
							
							if (!(row.getCell(2) == null)) {
								row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
								String slLeave = row.getCell(2).getStringCellValue();
								if ((StringUtils.isNotBlank(slLeave))
										|| (StringUtils.isNotEmpty(slLeave))) {
									slGemsEmplyeeLeaveSummary.setLeaveBalance(new Double(slLeave));
									if (slGemsEmplyeeLeaveSummary.getGemsEmployeeMaster().getCurrentEmployeeStatus().getStatusCode().equalsIgnoreCase("CO"))
									{
										slGemsEmplyeeLeaveSummary.setLeaveEntitled(5);
									}
									else
									{
										slGemsEmplyeeLeaveSummary.setLeaveEntitled(0);
									}
									
								}
								else
								{
									slGemsEmplyeeLeaveSummary.setLeaveBalance(0);
									slGemsEmplyeeLeaveSummary.setLeaveEntitled(5);
								}
								slGemsEmplyeeLeaveSummary.setGemsLeaveTypeMaster(slLeaveTypeMaster);
								
								slGemsEmplyeeLeaveSummary.setLeaveScheduled(0);
								slGemsEmplyeeLeaveSummary.setLeaveTaken(0);
							}
							plGemsEmplyeeLeaveSummary.setBalanceUpdatedOn(todayDate);
							slGemsEmplyeeLeaveSummary.setBalanceUpdatedOn(todayDate);
							gemsEmployeeLeaveSummaryList.add(plGemsEmplyeeLeaveSummary);
							gemsEmployeeLeaveSummaryList.add(slGemsEmplyeeLeaveSummary);
							

						}
					}
					List<GemsEmployeeMaster> gemsEmployeeList = new ArrayList();
					if (gemsEmployeeLeaveSummaryList.size() != 0)
					{
						leaveManagementService.saveBatchLeaveSummary(gemsEmployeeLeaveSummaryList);
						for (GemsEmplyeeLeaveSummary gemsEmployeeLeaveSummary : gemsEmployeeLeaveSummaryList)
						{
							gemsEmployeeList.add(gemsEmployeeLeaveSummary.getGemsEmployeeMaster());
							
						}
						
						if (gemsEmployeeList.size() != 0)
						{
							employeeService.saveBatchEmployeeList(gemsEmployeeList);
						}
						
						
						
					}
					
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
