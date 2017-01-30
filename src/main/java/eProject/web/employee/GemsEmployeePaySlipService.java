package eProject.web.employee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmployeePaySlipDetail;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;
import eProject.utility.PayslipUtility;

@Controller
public class GemsEmployeePaySlipService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeePaySlipService.class);

	@RequestMapping(value = "/employee/viewPaySlipList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeImmigrationList(HttpServletRequest request) {

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

			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail = new GemsEmployeePaySlipDetail();

			String searchEmpMasterId = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpMasterId != null && searchEmpMasterId.isEmpty() == false) {
				gemsEmployeePaySlipDetail.setGemsEmployeeMaster(
						employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpMasterId)));
			}

			String searchPaySlipDate = request.getParameter("searchPaySlipDate");
			if (searchPaySlipDate != null && searchPaySlipDate.isEmpty() == false) {
				String[] selectedMonthYearArray = searchPaySlipDate.split("/");
				searchPaySlipDate = "" + selectedMonthYearArray[0].toString() + "_"
						+ selectedMonthYearArray[1].toString() + "";
				gemsEmployeePaySlipDetail.setPaySlipDate(searchPaySlipDate);
			}
			String searchEmployeeCode = request.getParameter("searchEmployeeCode");
			if (searchEmployeeCode != null && searchEmployeeCode.isEmpty() == false) {
				GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
				searchGemsEmployeeMaster.setEmployeeCode(searchEmployeeCode);
				gemsEmployeePaySlipDetail.setGemsEmployeeMaster(searchGemsEmployeeMaster);

			}

			int totalResults = employeeService.getGemsEmployeePaySlipDetailFilterCount(gemsEmployeePaySlipDetail);
			List<GemsEmployeePaySlipDetail> list = employeeService.getGemsEmployeePaySlipDetailList(start, limit,
					gemsEmployeePaySlipDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeePaySlipList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/uploadPaySlipDetail", method = RequestMethod.POST)
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

			String selectedMonthYear = "";
			if ((StringUtils.isNotBlank(request.getParameter("payslipDate")))
					|| (StringUtils.isNotEmpty(request.getParameter("payslipDate")))) {
				selectedMonthYear = request.getParameter("payslipDate");
				String[] selectedMonthYearArray = selectedMonthYear.split("/");
				selectedMonthYear = "" + selectedMonthYearArray[0].toString() + "_"
						+ selectedMonthYearArray[1].toString() + "";
			}

			// check if any payslip records exist for selected month/year if yes
			// delete them
			GemsEmployeePaySlipDetail searchGemsEmployeePaySlipDetail = new GemsEmployeePaySlipDetail();
			searchGemsEmployeePaySlipDetail.setPaySlipDate(selectedMonthYear);
			int totalCount = employeeService.getGemsEmployeePaySlipDetailFilterCount(searchGemsEmployeePaySlipDetail);
			if (totalCount != 0) {
				List<GemsEmployeePaySlipDetail> list = employeeService
						.getAllGemsEmployeePaySlipDetailList(searchGemsEmployeePaySlipDetail);
				for (GemsEmployeePaySlipDetail gemsEmployeePaySlipDetailObject : list) {
					employeeService.removeGemsEmployeePayslipDetail(gemsEmployeePaySlipDetailObject);
				}
			}

			// selectedMonthYear = request.getParameter("payslipDate");

			if (file.getSize() > 0) {
				// loggedInUser.setProfileImgData(file.getBytes());
				String uploadDirectoryBase = System.getProperty("catalina.base");
				String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator + "upload"
						+ File.separator + "payslip_repository" + File.separator + "UnZip" + File.separator;

				File docFileRepo = new File(documentFileRepo);
				if (!(docFileRepo.exists())) {
					docFileRepo.createNewFile();
				}
				in = file.getInputStream();
				out = new FileOutputStream(new File(documentFileRepo + File.separator + file.getOriginalFilename()));
				int read = 0;
				final byte[] bytes = new byte[1024];
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				String targetDocumentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator
						+ "upload" + File.separator + "payslip_repository";

				targetDocumentFileRepo = targetDocumentFileRepo + File.separator + selectedMonthYear;

				documentFileRepo = documentFileRepo + file.getOriginalFilename();

				PayslipUtility unzipper = new PayslipUtility();
				unzipper.unzip(documentFileRepo, targetDocumentFileRepo);

				File directory = new File(targetDocumentFileRepo);

				// get all the files from a directory

				File[] fList = directory.listFiles();

				for (File paySlipFile : fList) {

					if (paySlipFile.isFile()) {

						System.out.println(paySlipFile.getName());
						String[] fileNameArray = paySlipFile.getName().split("_");
						String employeeCode = fileNameArray[0].toString();

						GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail = new GemsEmployeePaySlipDetail();

						GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
								"loggedInUser");
						if ((StringUtils.isNotBlank(employeeCode)) || (StringUtils.isNotEmpty(employeeCode))) {
							GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
							gemsEmployeeMaster.setEmployeeCode(employeeCode);
							gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
							gemsEmployeePaySlipDetail.setGemsEmployeeMaster(
									employeeService.getGemsEmployeeMasterByCode(gemsEmployeeMaster));
							gemsEmployeePaySlipDetail.setUpdatedBy(loggedInUser.getCreatedBy());
							gemsEmployeePaySlipDetail.setUpdatedOn(todayDate);
							gemsEmployeePaySlipDetail.setCreatedOn(todayDate);
							gemsEmployeePaySlipDetail.setCreatedBy(loggedInUser.getCreatedBy());
							gemsEmployeePaySlipDetail.setPaySlipDate(selectedMonthYear);
							gemsEmployeePaySlipDetail.setUploadedFileName(paySlipFile.getName());
							FileInputStream fin = new FileInputStream(paySlipFile);
							byte fileContent[] = new byte[(int) paySlipFile.length()];
							fin.read(fileContent);
							gemsEmployeePaySlipDetail.setDocumentContent(fileContent);
							gemsEmployeePaySlipDetail.setDocumentContentType(file.getContentType());
							employeeService.saveGemsEmployeePaySlipDetail(gemsEmployeePaySlipDetail);
							fin.close();
							fin = null;
						}

					}

				}
				out.flush();
				in.close();
				out.close();
				in = null;
				out = null;
				File deleteDocumentFileRepo = new File(documentFileRepo);
				File deleteTargetFileRepo = new File(targetDocumentFileRepo);
				try {
					deleteDocumentFileRepo.delete();
					deleteTargetFileRepo.delete();
				} catch (Exception ex) {
					logger.info("Exception while deleting old jar:" + ex.getMessage());
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

	private Map<String, Object> getModelMapEmployeePaySlipList(List<GemsEmployeePaySlipDetail> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeePaySlipDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeePaySlipDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail = (GemsEmployeePaySlipDetail) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				String selectedEmployeeCode = "";
				if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster() != null) {
					selectedEmployeeCode = gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeCode();

					selectedGemsEmployeeMasterId = gemsEmployeePaySlipDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName + " "
								+ gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}

				}
				String selectedMonthYear = "";
				if (gemsEmployeePaySlipDetail.getPaySlipDate() != null) {
					String[] selectedMonthYearArray = gemsEmployeePaySlipDetail.getPaySlipDate().split("_");
					selectedMonthYear = "" + selectedMonthYearArray[0].toString() + "/"
							+ selectedMonthYearArray[1].toString() + "";
				}

				return new JSONObject()
						.element("gemsEmployeePayslipDetailId",
								gemsEmployeePaySlipDetail.getGemsEmployeePayslipDetailId())
						.element("paySlipDate", selectedMonthYear)
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedEmployeeCode", selectedEmployeeCode)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("activeStatus", gemsEmployeePaySlipDetail.getActiveStatus());
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

	@RequestMapping(value = "/employee/downloadPaySlip", method = RequestMethod.GET)
	public void downloadDocument(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			int gemsEmployeePaySlipId = Integer.parseInt(request.getParameter("objectId"));
			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail = employeeService
					.getGemsEmployeePayslipDetail(gemsEmployeePaySlipId);

			// get absolute path of the application
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String uploadDirectory = "\\webapps\\upload\\payslip_repository\\"
					+ gemsEmployeePaySlipDetail.getPaySlipDate() + "\\";

			String appPath = uploadDirectoryBase + uploadDirectory;

			// construct the complete absolute path of the file
			String fullPath = appPath + gemsEmployeePaySlipDetail.getUploadedFileName();
			File downloadFile = new File(fullPath);
			FileInputStream inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = null;
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			response.addHeader("content-disposition",
					"attachment; filename=" + gemsEmployeePaySlipDetail.getUploadedFileName() + "");

			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
