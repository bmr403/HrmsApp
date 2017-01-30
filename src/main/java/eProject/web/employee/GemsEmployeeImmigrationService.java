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

import eProject.domain.employee.GemsEmployeeImmigrationDetail;

import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsEmployeeImmigrationService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeImmigrationService.class);

	@RequestMapping(value = "/employee/viewEmployeeImmigrationList", method = RequestMethod.GET)
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

			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = new GemsEmployeeImmigrationDetail();

			String searchEmpCode = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmployeeImmigrationDetail
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService
					.getGemsEmployeeImmigrationDetailFilterCount(gemsEmployeeImmigrationDetail);
			List<GemsEmployeeImmigrationDetail> list = employeeService.getGemsEmployeeImmigrationDetailList(start,
					limit, gemsEmployeeImmigrationDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeImmigrationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmployeeImmigration", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmployeeImmigration(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = new GemsEmployeeImmigrationDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEmployeeImmigrationDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeImmigrationDetail.setUpdatedOn(todayDate);

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeImmigrationDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeImmigrationDetailId")))) {
				id_value = request.getParameter("gemsEmployeeImmigrationDetailId");
				gemsEmployeeImmigrationDetail = employeeService
						.getGemsEmployeeImmigrationDetail(Integer.parseInt(id_value));
			} else {
				gemsEmployeeImmigrationDetail.setCreatedOn(todayDate);
				gemsEmployeeImmigrationDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String documentNumber = request.getParameter("documentNumber");
			gemsEmployeeImmigrationDetail.setDocumentNumber(documentNumber);

			String documentType = request.getParameter("documentType");
			gemsEmployeeImmigrationDetail.setDocumentType(documentType);

			String eligibiltyReviewDateString = request.getParameter("eligibiltyReviewDate");
			if ((StringUtils.isNotBlank(eligibiltyReviewDateString))
					|| (StringUtils.isNotEmpty(eligibiltyReviewDateString))) {
				Date eligibiltyReviewDate = formatter.parse(eligibiltyReviewDateString);
				gemsEmployeeImmigrationDetail.setEligibiltyReviewDate(eligibiltyReviewDate);
			}

			String expiryDateString = request.getParameter("expiryDate");
			if ((StringUtils.isNotBlank(expiryDateString)) || (StringUtils.isNotEmpty(expiryDateString))) {
				Date expiryDate = formatter.parse(expiryDateString);
				gemsEmployeeImmigrationDetail.setExpiryDate(expiryDate);

			}

			if ((StringUtils.isNotBlank(request.getParameter("issuedBy")))
					|| (StringUtils.isNotEmpty(request.getParameter("issuedBy")))) {
				gemsEmployeeImmigrationDetail.setIssuedBy(request.getParameter("issuedBy"));
			}

			String issueDateString = request.getParameter("issuedDate");
			if ((StringUtils.isNotBlank(issueDateString)) || (StringUtils.isNotEmpty(issueDateString))) {
				Date issuedDate = formatter.parse(issueDateString);
				gemsEmployeeImmigrationDetail.setIssuedDate(issuedDate);
			}

			String isActive = request.getParameter("docActiveStatus");
			if ((StringUtils.isNotBlank(issueDateString)) || (StringUtils.isNotEmpty(issueDateString))) {
				if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
					gemsEmployeeImmigrationDetail.setActiveStatus(1);
				} else {
					gemsEmployeeImmigrationDetail.setActiveStatus(0);
				}
			} else {
				gemsEmployeeImmigrationDetail.setActiveStatus(0);
			}

			String gemsEmployeeMasterId = request.getParameter("empDocGemsEmployeeMasterId");
			gemsEmployeeImmigrationDetail.setGemsEmployeeMaster(employeeService
					.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("empDocGemsEmployeeMasterId"))));

			if (file.getSize() > 0) {
				// loggedInUser.setProfileImgData(file.getBytes());
				/*
				 * String uploadDirectoryBase =
				 * System.getProperty("catalina.base"); String documentFileRepo
				 * = uploadDirectoryBase + File.separator + "webapps" +
				 * File.separator + "upload" + File.separator +
				 * "document_repository"; in = file.getInputStream();
				 * gemsEmployeeImmigrationDetail.setUploadedFileName(file.
				 * getOriginalFilename()); out = new FileOutputStream(new
				 * File(documentFileRepo + File.separator +
				 * file.getOriginalFilename())); int read = 0; final byte[]
				 * bytes = new byte[1024]; while ((read = in.read(bytes)) != -1)
				 * { out.write(bytes, 0, read); }
				 */
				gemsEmployeeImmigrationDetail.setDocumentContent(file.getBytes());
				gemsEmployeeImmigrationDetail.setUploadedFileName(file.getOriginalFilename());
				gemsEmployeeImmigrationDetail.setDocumentContentType(file.getContentType());
			}

			employeeService.saveGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetail);
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
		/*
		 * finally { try { out.flush(); in.close(); out.close(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 */

	}

	@RequestMapping(value = "/employee/getGemsEmployeeImmigration", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getGemsEmployeeImmigrationInfo(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeImmigrationDetailIdString = request.getParameter("gemsEmployeeImmigrationDetailId");

		GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = new GemsEmployeeImmigrationDetail();
		try {
			if (gemsEmployeeImmigrationDetailIdString != null) {
				int gemsEmployeeImmigrationDetailId = Integer
						.parseInt(request.getParameter("gemsEmployeeImmigrationDetailId"));
				gemsEmployeeImmigrationDetail = employeeService
						.getGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetailId);
			} else {
				return getModelMapError("Failed to Load Data");
			}

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

		return getModelMapEmployeeImmigrationDetail(gemsEmployeeImmigrationDetail);

	}

	@RequestMapping(value = "/employee/deleteGemsEmployeeImmigration", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmployeeImmigration(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeImmigrationDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = employeeService
					.getGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetailId);
			employeeService.removeGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetail);
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

	@RequestMapping(value = "/employee/downloadDocument", method = RequestMethod.GET)
	public void downloadDocument(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			int gemsEmployeeImmigrationDetailId = Integer.parseInt(request.getParameter("objectId"));
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = employeeService
					.getGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetailId);

			byte[] documentContent = gemsEmployeeImmigrationDetail.getDocumentContent();

			response.setHeader("Content-Disposition",
					"inline;filename=\"" + gemsEmployeeImmigrationDetail.getUploadedFileName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(gemsEmployeeImmigrationDetail.getDocumentContentType());

			out.write(documentContent);

			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> getModelMapEmployeeImmigrationDetail(
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeImmigrationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeImmigrationDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = (GemsEmployeeImmigrationDetail) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeImmigrationDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				String eligibiltyReviewDate = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsEmployeeImmigrationDetail.getEligibiltyReviewDate() != null) {
					eligibiltyReviewDate = sdf.format(gemsEmployeeImmigrationDetail.getEligibiltyReviewDate());
				}
				String expiryDate = "";
				if (gemsEmployeeImmigrationDetail.getExpiryDate() != null) {
					expiryDate = sdf.format(gemsEmployeeImmigrationDetail.getExpiryDate());
				}
				String issuedDate = "";
				if (gemsEmployeeImmigrationDetail.getIssuedDate() != null) {
					issuedDate = sdf.format(gemsEmployeeImmigrationDetail.getIssuedDate());
				}
				String issuedBy = "";
				if (gemsEmployeeImmigrationDetail.getIssuedBy() != null) {
					issuedBy = gemsEmployeeImmigrationDetail.getIssuedBy();
				}
				String documentNo = "";
				if (gemsEmployeeImmigrationDetail.getDocumentNumber() != null) {
					documentNo = gemsEmployeeImmigrationDetail.getDocumentNumber();
				}
				boolean activeStatus = false;

				if (gemsEmployeeImmigrationDetail.getActiveStatus() == 1) {
					activeStatus = true;
				}
				return new JSONObject()
						.element("gemsEmployeeImmigrationDetailId",
								gemsEmployeeImmigrationDetail.getGemsEmployeeImmigrationDetailId())
						.element("documentNumber", documentNo)
						.element("documentType", gemsEmployeeImmigrationDetail.getDocumentType())
						.element("eligibiltyReviewDate", eligibiltyReviewDate).element("expiryDate", expiryDate)
						.element("issuedDate", issuedDate).element("issuedBy", issuedBy)
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("docActiveStatus", activeStatus);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeImmigrationDetail, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeImmigrationList(List<GemsEmployeeImmigrationDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeImmigrationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeImmigrationDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail = (GemsEmployeeImmigrationDetail) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeImmigrationDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeImmigrationDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				String eligibiltyReviewDate = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsEmployeeImmigrationDetail.getEligibiltyReviewDate() != null) {
					eligibiltyReviewDate = sdf.format(gemsEmployeeImmigrationDetail.getEligibiltyReviewDate());
				}
				String expiryDate = "";
				if (gemsEmployeeImmigrationDetail.getExpiryDate() != null) {
					expiryDate = sdf.format(gemsEmployeeImmigrationDetail.getExpiryDate());
				}
				String issuedDate = "";
				if (gemsEmployeeImmigrationDetail.getIssuedDate() != null) {
					issuedDate = sdf.format(gemsEmployeeImmigrationDetail.getIssuedDate());
				}
				String issuedBy = "";
				if (gemsEmployeeImmigrationDetail.getIssuedBy() != null) {
					issuedBy = gemsEmployeeImmigrationDetail.getIssuedBy();
				}
				String documentNo = "";
				if (gemsEmployeeImmigrationDetail.getDocumentNumber() != null) {
					documentNo = gemsEmployeeImmigrationDetail.getDocumentNumber();
				}
				boolean activeStatus = false;

				if (gemsEmployeeImmigrationDetail.getActiveStatus() == 1) {
					activeStatus = true;
				}
				return new JSONObject()
						.element("gemsEmployeeImmigrationDetailId",
								gemsEmployeeImmigrationDetail.getGemsEmployeeImmigrationDetailId())
						.element("documentNumber", documentNo)
						.element("documentType", gemsEmployeeImmigrationDetail.getDocumentType())
						.element("eligibiltyReviewDate", eligibiltyReviewDate).element("expiryDate", expiryDate)
						.element("issuedDate", issuedDate).element("issuedBy", issuedBy)
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)
						.element("docActiveStatus", activeStatus);
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
