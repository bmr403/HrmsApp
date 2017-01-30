package eProject.web.recruitment;

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
import org.apache.commons.lang.WordUtils;
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

import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeImmigrationDetail;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.recruitment.GemsCandidateContactDetail;
import eProject.domain.recruitment.GemsCandidateMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsCandidateMasterService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCandidateMasterService.class);

	@RequestMapping(value = "/recruitment/checkeCandidateByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkeCandidateByCode(HttpServletRequest request) {

		String gemsCandidateCode = request.getParameter("gemsCandidateCode");

		GemsCandidateMaster gemsCandidateMaster = new GemsCandidateMaster();
		gemsCandidateMaster.setGemsCandidateCode(gemsCandidateCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsCandidateMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsCandidateMaster returnedGemsCandidateMaster = recruitementService
				.getGemsCandidateMasterByCode(gemsCandidateMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsCandidateMaster.getGemsCandidateCode()))
				|| (StringUtils.isNotEmpty(returnedGemsCandidateMaster.getGemsCandidateCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/recruitment/viewCandidateMasterList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCandidateMasterList(HttpServletRequest request) {

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

			GemsCandidateMaster gemsCandidateMaster = new GemsCandidateMaster();
			gemsCandidateMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String searchExperienceString = request.getParameter("searchExperience");
			if (searchExperienceString != null && searchExperienceString.isEmpty() == false) {
				Double searchExperience = Double.valueOf(searchExperienceString);
				gemsCandidateMaster.setGemsCandidateExperience(searchExperience);
				;
			}

			String searchKeySkill = request.getParameter("searchKeySkill");
			if (searchKeySkill != null && searchKeySkill.isEmpty() == false) {
				gemsCandidateMaster.setGemsCandidateKeySkill(searchKeySkill);
			}

			int totalResults = recruitementService.getGemsCandidateMasterFilterCount(gemsCandidateMaster);
			List<GemsCandidateMaster> list = recruitementService.getGemsCandidateMasterList(start, limit,
					gemsCandidateMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapCandidateList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveCandidate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCandidate(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		InputStream in = null;
		FileOutputStream out = null;
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCandidateMaster gemsCandidateMaster = new GemsCandidateMaster();
			// GemsCandidateContactDetail gemsCandidateContactDetail = new
			// GemsCandidateContactDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsCandidateMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsCandidateMaster.setUpdatedOn(todayDate);
			gemsCandidateMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			// gemsCandidateContactDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			// gemsCandidateContactDetail.setUpdatedOn(todayDate);
			if ((StringUtils.isNotBlank(request.getParameter("gemsCandidateMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCandidateMasterId")))) {
				id_value = request.getParameter("gemsCandidateMasterId");
				gemsCandidateMaster = recruitementService.getGemsCandidateMaster(Integer.parseInt(id_value));
				// gemsCandidateContactDetail =
				// gemsCandidateMaster.getGemsCandidateContactDetail();
			} else {
				gemsCandidateMaster.setCreatedOn(todayDate);
				gemsCandidateMaster.setCreatedBy(loggedInUser.getGemsUserMasterId());
				// gemsCandidateContactDetail.setCreatedOn(todayDate);
				// gemsCandidateContactDetail.setCreatedBy(loggedInUser.getGemsUserMasterId());
				String gemsCandidateCodeString = recruitementService.getMaxGemsRecruitmentRequestCode();
				if ((StringUtils.isNotBlank(gemsCandidateCodeString))
						|| (StringUtils.isNotEmpty(gemsCandidateCodeString))) {
					Integer gemsCandidateCode = Integer.parseInt(gemsCandidateCodeString) + 1;
					gemsCandidateMaster.setGemsCandidateCode(String.valueOf(gemsCandidateCode));

				} else {
					Integer gemsCandidateCode = new Integer(100);
					gemsCandidateMaster.setGemsCandidateCode(String.valueOf(gemsCandidateCode));
				}
			}

			String gemsCandidateEmail = request.getParameter("gemsCandidateEmail");
			gemsCandidateMaster.setGemsCandidateEmail(gemsCandidateEmail);

			String gemsCandidateProfileReference = request.getParameter("gemsCandidateProfileReference");
			gemsCandidateMaster.setGemsCandidateProfileReference(gemsCandidateProfileReference);

			String gemsCandidateReferredBy = request.getParameter("gemsCandidateReferredBy");
			gemsCandidateMaster.setGemsCandidateReferredBy(gemsCandidateReferredBy);

			String gemsCandidateCurrentDesignation = request.getParameter("gemsCandidateCurrentDesignation");
			gemsCandidateMaster.setGemsCandidateCurrentDesignation(gemsCandidateCurrentDesignation);

			String gemsCandidateFirstName = request.getParameter("gemsCandidateFirstName");
			gemsCandidateMaster.setGemsCandidateFirstName(gemsCandidateFirstName);

			String gemsCandidateLastName = request.getParameter("gemsCandidateLastName");
			gemsCandidateMaster.setGemsCandidateLastName(gemsCandidateLastName);

			String gemsCandidateMobile = request.getParameter("gemsCandidateMobile");
			gemsCandidateMaster.setGemsCandidateMobile(gemsCandidateMobile);

			String gemsCandidatePhone = request.getParameter("gemsCandidatePhone");
			gemsCandidateMaster.setGemsCandidatePhone(gemsCandidatePhone);

			// String gemsCandidateStatus =
			// request.getParameter("gemsCandidateStatus");
			String gemsCandidateStatus = "Available";
			gemsCandidateMaster.setGemsCandidateStatus(gemsCandidateStatus);

			String gemsCandidateCurrentLocation = request.getParameter("gemsCandidateCurrentLocation");
			gemsCandidateMaster.setGemsCandidateCurrentLocation(gemsCandidateCurrentLocation);

			String gemsCandidateKeySkill = request.getParameter("gemsCandidateKeySkill");
			gemsCandidateMaster.setGemsCandidateKeySkill(gemsCandidateKeySkill);

			String gemsCandidateExperienceString = request.getParameter("gemsCandidateExperience");
			if ((StringUtils.isNotBlank(gemsCandidateExperienceString))
					|| (StringUtils.isNotEmpty(gemsCandidateExperienceString))) {
				Double gemsCandidateExperience = Double.valueOf(gemsCandidateExperienceString);
				gemsCandidateMaster.setGemsCandidateExperience(gemsCandidateExperience);
			}

			String gemsCandidateCurrentCtcString = request.getParameter("gemsCandidateCurrentCtc");
			if ((StringUtils.isNotBlank(gemsCandidateCurrentCtcString))
					|| (StringUtils.isNotEmpty(gemsCandidateCurrentCtcString))) {
				Double gemsCandidateCurrentCtc = Double.valueOf(gemsCandidateCurrentCtcString);
				gemsCandidateMaster.setGemsCandidateCurrentCtc(gemsCandidateCurrentCtc);
			}

			String gemsCandidateNoticePeriodString = request.getParameter("gemsCandidateNoticePeriod");
			if ((StringUtils.isNotBlank(gemsCandidateNoticePeriodString))
					|| (StringUtils.isNotEmpty(gemsCandidateNoticePeriodString))) {
				Integer gemsCandidateNoticePeriod = new Integer(gemsCandidateNoticePeriodString);
				gemsCandidateMaster.setGemsCandidateNoticePeriod(gemsCandidateNoticePeriod);
			}

			String gemsCandidateExpectedCtcString = request.getParameter("gemsCandidateExpectedCtc");
			if ((StringUtils.isNotBlank(gemsCandidateExpectedCtcString))
					|| (StringUtils.isNotEmpty(gemsCandidateExpectedCtcString))) {
				Double gemsCandidateExpectedCtc = Double.valueOf(gemsCandidateExpectedCtcString);
				gemsCandidateMaster.setGemsCandidateExpectedCtc(gemsCandidateExpectedCtc);
			}

			String gemsCandidateEducation = request.getParameter("gemsCandidateEducation");
			gemsCandidateMaster.setGemsCandidateEducation(gemsCandidateEducation);

			/*
			 * String isActive = request.getParameter("activeStatus"); if
			 * ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
			 * gemsCandidateMaster.setActiveStatus(1); } else {
			 * gemsCandidateMaster.setActiveStatus(0); }
			 */

			if (file.getSize() > 0) {
				// loggedInUser.setProfileImgData(file.getBytes());
				/*String uploadDirectoryBase = System.getProperty("catalina.base");
				String documentFileRepo = uploadDirectoryBase + File.separator + "webapps" + File.separator + "upload"
						+ File.separator + "document_repository" + File.separator + "candidate_resumes";
				in = file.getInputStream();
				gemsCandidateMaster.setGemsCandidateResumeFileName(file.getOriginalFilename());
				out = new FileOutputStream(new File(documentFileRepo + File.separator + file.getOriginalFilename()));
				int read = 0;
				final byte[] bytes = new byte[1024];
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}*/
				
				gemsCandidateMaster.setDocumentContent(file.getBytes());
				gemsCandidateMaster.setGemsCandidateResumeFileName(file.getOriginalFilename());
				gemsCandidateMaster.setDocumentContentType(file.getContentType());
				
			}

			recruitementService.saveGemsCandidateMaster(gemsCandidateMaster);

			/*
			 * Saving contact info
			 */

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

	@RequestMapping(value = "/recruitment/getGemsCandidateInfo", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getGemsCandidateInfo(HttpServletRequest request) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsCandidateMasterIdString = request.getParameter("objectId");

		GemsCandidateMaster returnedGemsCandidateMaster = new GemsCandidateMaster();
		try {
			if (gemsCandidateMasterIdString != null) {

				int gemsCandidateMasterId = Integer.parseInt(request.getParameter("objectId"));
				returnedGemsCandidateMaster = recruitementService.getGemsCandidateMaster(gemsCandidateMasterId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapCustomerInfo(returnedGemsCandidateMaster);

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/recruitment/deleteCandidate", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCandidate(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsCandidateMasterIdString = request.getParameter("gemsCandidateMasterId");

		try {
			if (gemsCandidateMasterIdString != null) {

				int gemsCandidateMasterId = Integer.parseInt(request.getParameter("gemsCandidateMasterId"));
				GemsCandidateMaster gemsCandidateMaster = recruitementService
						.getGemsCandidateMaster(gemsCandidateMasterId);
				recruitementService.removeGemsCandidateMaster(gemsCandidateMaster);
			}

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

	@RequestMapping(value = "/recruitment/downloadResume", method = RequestMethod.GET)
	public void downloadResume(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			int gemsCandidateMasterId = Integer.parseInt(request.getParameter("objectId"));
			GemsCandidateMaster gemsCandidateMaster = recruitementService.getGemsCandidateMaster(gemsCandidateMasterId);
			
			byte[] documentContent = gemsCandidateMaster.getDocumentContent();

			response.setHeader("Content-Disposition",
					"inline;filename=\"" + gemsCandidateMaster.getGemsCandidateResumeFileName() + "\"");
			OutputStream out = response.getOutputStream();
			response.setContentType(gemsCandidateMaster.getDocumentContentType());

			out.write(documentContent);

			out.flush();
			out.close();

			/*// get absolute path of the application
			String uploadDirectoryBase = System.getProperty("catalina.base");
			String appPath = uploadDirectoryBase + File.separator + "webapps" + File.separator + "upload"
					+ File.separator + "document_repository" + File.separator + "candidate_resumes";

			// construct the complete absolute path of the file
			String fullPath = appPath + gemsCandidateMaster.getGemsCandidateResumeFileName();
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
					"attachment; filename=" + gemsCandidateMaster.getGemsCandidateResumeFileName() + "");

			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outStream.close();*/
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> getModelMapCandidateList(List<GemsCandidateMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateMaster)) {
					return new JSONObject(true);
				}

				GemsCandidateMaster gemsCandidateMaster = (GemsCandidateMaster) bean;
				String candidateName = " ";
				if (gemsCandidateMaster.getGemsCandidateFirstName() != null) {
					candidateName = candidateName + gemsCandidateMaster.getGemsCandidateFirstName();
				}
				if (gemsCandidateMaster.getGemsCandidateLastName() != null) {
					candidateName = candidateName + " ";
					candidateName = candidateName + gemsCandidateMaster.getGemsCandidateLastName();
				}
				String employeeDOB = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String contactNumber = "Phone : ";
				if (gemsCandidateMaster.getGemsCandidatePhone() != null) {
					contactNumber += gemsCandidateMaster.getGemsCandidatePhone();
				}
				contactNumber += "<br>Mobile : ";
				if (gemsCandidateMaster.getGemsCandidateMobile() != null) {
					contactNumber += gemsCandidateMaster.getGemsCandidateMobile();
				}
				contactNumber += "<br>Email : ";
				if (gemsCandidateMaster.getGemsCandidateEmail() != null) {
					contactNumber += gemsCandidateMaster.getGemsCandidateEmail();
				}
				String gemsCandidateCode = "";
				if (gemsCandidateMaster.getGemsCandidateCode() != null) {
					gemsCandidateCode += "BPA-" + gemsCandidateMaster.getGemsCandidateCode();
				}
				String gemsCandidateKeySkill = "";
				if (gemsCandidateMaster.getGemsCandidateKeySkill() != null) {
					StringBuilder sb = new StringBuilder(gemsCandidateMaster.getGemsCandidateKeySkill());

					int i = 0;
					while (i + 20 < sb.length() && (i = sb.lastIndexOf(",", i + 20)) != -1) {
						sb.replace(i, i + 1, "\n");
					}
					gemsCandidateKeySkill = sb.toString();
				}

				return new JSONObject().element("gemsCandidateMasterId", gemsCandidateMaster.getGemsCandidateMasterId())
						.element("gemsCandidateCode", gemsCandidateCode).element("contactNumber", contactNumber)
						.element("candidateCodeName",
								"" + gemsCandidateMaster.getGemsCandidateCode() + " - " + candidateName + "")
						.element("candidateName", candidateName)
						.element("gemsCandidateFirstName", gemsCandidateMaster.getGemsCandidateFirstName())
						.element("gemsCandidateLastName", gemsCandidateMaster.getGemsCandidateLastName())
						.element("gemsCandidateMobile", gemsCandidateMaster.getGemsCandidateMobile())
						.element("gemsCandidateEmail", gemsCandidateMaster.getGemsCandidateEmail())
						.element("gemsCandidatePhone", gemsCandidateMaster.getGemsCandidatePhone())
						.element("gemsCandidateCurrentLocation", gemsCandidateMaster.getGemsCandidateCurrentLocation())
						.element("gemsCandidateKeySkill", gemsCandidateKeySkill)
						.element("gemsCandidateExperience", gemsCandidateMaster.getGemsCandidateExperience())
						.element("gemsCandidateCurrentCtc", gemsCandidateMaster.getGemsCandidateCurrentCtc())
						.element("gemsCandidateNoticePeriod", gemsCandidateMaster.getGemsCandidateNoticePeriod())
						.element("gemsCandidateExpectedCtc", gemsCandidateMaster.getGemsCandidateExpectedCtc())
						.element("gemsCandidateEducation", gemsCandidateMaster.getGemsCandidateEducation())
						.element("gemsCandidateStatus", gemsCandidateMaster.getGemsCandidateStatus())
						.element("gemsCandidateCurrentDesignation",
								gemsCandidateMaster.getGemsCandidateCurrentDesignation())
						.element("gemsCandidateProfileReference",
								gemsCandidateMaster.getGemsCandidateProfileReference())
						.element("gemsCandidateReferredBy", gemsCandidateMaster.getGemsCandidateReferredBy());
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapCustomerInfo(GemsCandidateMaster gemsCandidateMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateMaster)) {
					return new JSONObject(true);
				}

				GemsCandidateMaster gemsCandidateMaster = (GemsCandidateMaster) bean;
				String candidateName = " ";
				if (gemsCandidateMaster.getGemsCandidateFirstName() != null) {
					candidateName = candidateName + gemsCandidateMaster.getGemsCandidateFirstName();
				}
				if (gemsCandidateMaster.getGemsCandidateLastName() != null) {
					candidateName = candidateName + " ";
					candidateName = candidateName + gemsCandidateMaster.getGemsCandidateLastName();
				}
				String employeeDOB = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String gemsCandidateCode = "";
				if (gemsCandidateMaster.getGemsCandidateCode() != null) {
					gemsCandidateCode += "BPA-" + gemsCandidateMaster.getGemsCandidateCode();
				}

				return new JSONObject().element("gemsCandidateMasterId", gemsCandidateMaster.getGemsCandidateMasterId())
						.element("gemsCandidateCode", gemsCandidateCode)
						// .element("contactNumber", contactNumber)
						.element("candidateCodeName",
								"" + gemsCandidateMaster.getGemsCandidateCode() + " - " + candidateName + "")
						.element("candidateName", candidateName)
						.element("gemsCandidateFirstName", gemsCandidateMaster.getGemsCandidateFirstName())
						.element("gemsCandidateLastName", gemsCandidateMaster.getGemsCandidateLastName())
						.element("gemsCandidateMobile", gemsCandidateMaster.getGemsCandidateMobile())
						.element("gemsCandidateEmail", gemsCandidateMaster.getGemsCandidateEmail())
						.element("gemsCandidatePhone", gemsCandidateMaster.getGemsCandidatePhone())
						.element("gemsCandidateCurrentLocation", gemsCandidateMaster.getGemsCandidateCurrentLocation())
						.element("gemsCandidateKeySkill", gemsCandidateMaster.getGemsCandidateKeySkill())
						.element("gemsCandidateExperience", gemsCandidateMaster.getGemsCandidateExperience())
						.element("gemsCandidateCurrentCtc", gemsCandidateMaster.getGemsCandidateCurrentCtc())
						.element("gemsCandidateNoticePeriod", gemsCandidateMaster.getGemsCandidateNoticePeriod())
						.element("gemsCandidateExpectedCtc", gemsCandidateMaster.getGemsCandidateExpectedCtc())
						.element("gemsCandidateEducation", gemsCandidateMaster.getGemsCandidateEducation())
						.element("gemsCandidateStatus", gemsCandidateMaster.getActiveStatus())
						.element("gemsCandidateCurrentDesignation",
								gemsCandidateMaster.getGemsCandidateCurrentDesignation())
						.element("gemsCandidateProfileReference",
								gemsCandidateMaster.getGemsCandidateProfileReference())
						.element("gemsCandidateReferredBy", gemsCandidateMaster.getGemsCandidateReferredBy());
			}
		});

		JSON json = JSONSerializer.toJSON(gemsCandidateMaster, jsonConfig);

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
