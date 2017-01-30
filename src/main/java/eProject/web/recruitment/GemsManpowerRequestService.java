package eProject.web.recruitment;

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

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.recruitment.GemsCandidateMaster;
import eProject.domain.recruitment.GemsCandidateStatusMaster;
import eProject.domain.recruitment.GemsManpowerRequest;
import eProject.domain.recruitment.GemsManpowerRequestInterview;
import eProject.domain.recruitment.GemsRecruitmentRequirementCandidate;
import eProject.service.customer.CustomerService;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsManpowerRequestService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private EmployeeService employeeService;

	protected final Log logger = LogFactory.getLog(GemsManpowerRequestService.class);

	@RequestMapping(value = "/recruitment/viewManpowerRequestList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewManpowerRequestList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsManpowerRequest gemsManpowerRequest = new GemsManpowerRequest();
			gemsManpowerRequest.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			if ((StringUtils.isNotBlank(request.getParameter("searchJobCode")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchJobCode")))) {
				gemsManpowerRequest.setJobCode(request.getParameter("searchJobCode"));
			}
			if ((StringUtils.isNotBlank(request.getParameter("searchRequestType")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchRequestType")))) {
				gemsManpowerRequest.setRequestType(request.getParameter("searchRequestType"));
			}
			if ((StringUtils.isNotBlank(request.getParameter("searchCustomerName")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchCustomerName")))) {
				GemsCustomerMaster searchGemsCustomerMaster = new GemsCustomerMaster();
				searchGemsCustomerMaster.setGemsCustomerName(request.getParameter("searchCustomerName"));
				gemsManpowerRequest.setGemsCustomerMaster(searchGemsCustomerMaster);
			}

			int totalResults = recruitementService.getGemsManpowerRequestFilterCount(gemsManpowerRequest);
			List<GemsManpowerRequest> list = recruitementService.getGemsManpowerRequestList(start, limit,
					gemsManpowerRequest);

			logger.info("Returned list size" + list.size());

			return getModelMapManpowerRequestList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapManpowerRequestList(List<GemsManpowerRequest> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsManpowerRequest.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsManpowerRequest)) {
					return new JSONObject(true);
				}

				GemsManpowerRequest gemsManpowerRequest = (GemsManpowerRequest) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				String requestDate = "";
				if (gemsManpowerRequest.getRequestDate() != null) {
					requestDate = sdf.format(gemsManpowerRequest.getRequestDate());
				}
				String requestValidFrom = "";
				if (gemsManpowerRequest.getRequestValidFrom() != null) {
					requestValidFrom = sdf.format(gemsManpowerRequest.getRequestValidFrom());
				}
				String requestValidTo = "";
				if (gemsManpowerRequest.getRequestValidTo() != null) {
					requestValidTo = sdf.format(gemsManpowerRequest.getRequestValidTo());
				}
				String currentDepartmentName = "";
				Integer currentDepartmentId = new Integer(0);
				if ((gemsManpowerRequest.getGemsDepartment() != null)
						&& (gemsManpowerRequest.getGemsDepartment().getGemsDepartmentId() != 0)) {

					currentDepartmentName = "" + gemsManpowerRequest.getGemsDepartment().getDepartmentCode() + "";
					currentDepartmentId = gemsManpowerRequest.getGemsDepartment().getGemsDepartmentId();
				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsManpowerRequest.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsManpowerRequest.getGemsCustomerMaster()
							.getGemsCustomerMasterId();
					selected_customer = "" + gemsManpowerRequest.getGemsCustomerMaster().getGemsCustomerCode() + " - "
							+ gemsManpowerRequest.getGemsCustomerMaster().getGemsCustomerName() + "";

				}
				String requestStatus = "";
				if (gemsManpowerRequest.getRequestStatus() != null) {
					requestStatus = gemsManpowerRequest.getRequestStatus();
				}

				String requisitionNumber = "";
				if (gemsManpowerRequest.getJobCode() != null) {
					requisitionNumber += gemsManpowerRequest.getJobCode();
				}
				if (gemsManpowerRequest.getRequestDate() != null) {
					requisitionNumber += " (" + gemsManpowerRequest.getRequestDate() + ") ";
				}

				return new JSONObject().element("gemsMapowerRequestId", gemsManpowerRequest.getGemsMapowerRequestId())
						.element("jobCode", gemsManpowerRequest.getJobCode())
						.element("requisitionNumber", requisitionNumber)
						.element("requestType", gemsManpowerRequest.getRequestType())
						.element("requestDate", requestDate).element("jobProfile", gemsManpowerRequest.getJobProfile())
						.element("numberOfResources", gemsManpowerRequest.getNumberOfResources())
						.element("jobPosition", gemsManpowerRequest.getJobPosition())
						.element("jobLocation", gemsManpowerRequest.getJobLocation())
						.element("minimumSalary", gemsManpowerRequest.getMinimumSalary())
						.element("maxSalary", gemsManpowerRequest.getMaxSalary())
						.element("profileExperienceMin", gemsManpowerRequest.getProfileExperienceMin())
						.element("profileExperienceMax", gemsManpowerRequest.getProfileExperienceMax())
						.element("employmentType", gemsManpowerRequest.getEmploymentType())
						.element("requestValidFrom", requestValidFrom).element("requestValidTo", requestValidTo)
						.element("requestStatus", requestStatus)
						.element("requestApprovalStatus", gemsManpowerRequest.getRequestApprovalStatus())
						.element("currentDepartmentId", currentDepartmentId)
						.element("currentDepartmentName", currentDepartmentName)
						.element("remarks", gemsManpowerRequest.getRemarks())
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("education", gemsManpowerRequest.getEducation())

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/recruitment/saveManpowerRequest", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveManpowerRequest(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsManpowerRequest gemsManpowerRequest = new GemsManpowerRequest();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			gemsManpowerRequest.setUpdatedBy(loggedInUser.getCreatedBy());
			gemsManpowerRequest.setUpdatedOn(todayDate);

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsMapowerRequestId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsMapowerRequestId")))) {
				id_value = request.getParameter("gemsMapowerRequestId");
				gemsManpowerRequest = recruitementService.getGemsManpowerRequest(Integer.parseInt(id_value));

			} else {
				gemsManpowerRequest.setCreatedOn(todayDate);
				gemsManpowerRequest.setCreatedBy(loggedInUser.getCreatedBy());
			}
			
			String jobCode = request.getParameter("jobCode");
			gemsManpowerRequest.setJobCode(jobCode);
			
			String requestType = request.getParameter("requestType");
			gemsManpowerRequest.setRequestType(requestType);

			gemsManpowerRequest.setRequestDate(todayDate);
			
			if ((StringUtils.isNotBlank(request.getParameter("gemsCustomer")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCustomer")))) {
				GemsCustomerMaster gemsCustomerMaster = customerService.getGemsCustomerMaster(Integer.parseInt(request.getParameter("gemsCustomer")));
				gemsManpowerRequest.setGemsCustomerMaster(gemsCustomerMaster);
			}
			
			if ((StringUtils.isNotBlank(request.getParameter("dropdown_department")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_department")))) {
				GemsDepartment gemsDepartment = masterService
						.getGemsDepartment(Integer.parseInt(request.getParameter("dropdown_department")));
				gemsManpowerRequest.setGemsDepartment(gemsDepartment);
			}
			
			String requestValidFromString = request.getParameter("requestValidFrom");
			if ((StringUtils.isNotBlank(requestValidFromString)) || (StringUtils.isNotEmpty(requestValidFromString))) {
				Date requestValidFrom = formatter.parse(requestValidFromString);
				gemsManpowerRequest.setRequestValidFrom(requestValidFrom);
			}

			String requestValidToString = request.getParameter("requestValidTo");
			if ((StringUtils.isNotBlank(requestValidToString)) || (StringUtils.isNotEmpty(requestValidToString))) {
				Date requestValidTo = formatter.parse(requestValidToString);
				gemsManpowerRequest.setRequestValidTo(requestValidTo);
			}
			
			String jobPosition = request.getParameter("jobPosition");
			gemsManpowerRequest.setJobPosition(jobPosition);
			
			String jobLocation = request.getParameter("jobLocation");
			gemsManpowerRequest.setJobLocation(jobLocation);
			
			String employmentType = request.getParameter("employmentType");
			gemsManpowerRequest.setEmploymentType(employmentType);
			
			String education = request.getParameter("education");
			gemsManpowerRequest.setEducation(education);
			
			String minimumSalaryString = request.getParameter("minimumSalary");
			if ((StringUtils.isNotBlank(minimumSalaryString)) || (StringUtils.isNotEmpty(minimumSalaryString))) {
				Double minimumSalary = Double.valueOf(minimumSalaryString);
				gemsManpowerRequest.setMinimumSalary(minimumSalary);
			}

			String maximumSalaryString = request.getParameter("maxSalary");
			if ((StringUtils.isNotBlank(maximumSalaryString)) || (StringUtils.isNotEmpty(maximumSalaryString))) {
				Double maximumSalary = Double.valueOf(maximumSalaryString);
				gemsManpowerRequest.setMaxSalary(maximumSalary);
			}
			
			String profileExperienceMinString = request.getParameter("profileExperienceMin");
			if ((StringUtils.isNotBlank(profileExperienceMinString))
					|| (StringUtils.isNotEmpty(profileExperienceMinString))) {
				Double profileExperienceMin = Double.valueOf(profileExperienceMinString);
				gemsManpowerRequest.setProfileExperienceMin(profileExperienceMin);
			}

			String profileExperienceMaxString = request.getParameter("profileExperienceMax");
			if ((StringUtils.isNotBlank(profileExperienceMaxString))
					|| (StringUtils.isNotEmpty(profileExperienceMaxString))) {
				Double profileExperienceMax = Double.valueOf(profileExperienceMaxString);
				gemsManpowerRequest.setProfileExperienceMax(profileExperienceMax);
			}
			
			String numberOfResourcesString = request.getParameter("numberOfResources");
			if ((StringUtils.isNotBlank(numberOfResourcesString))
					|| (StringUtils.isNotEmpty(numberOfResourcesString))) {
				gemsManpowerRequest.setNumberOfResources(Integer.parseInt(numberOfResourcesString));
			}
			
			String jobProfile = request.getParameter("jobProfile");
			gemsManpowerRequest.setJobProfile(jobProfile);
			
			

			String mr_submit_type = request.getParameter("mr_submit_type");
			if ((StringUtils.isNotBlank(mr_submit_type)) || (StringUtils.isNotEmpty(mr_submit_type))) {
				gemsManpowerRequest.setRequestStatus(mr_submit_type);
			}

			


			String remarks = request.getParameter("remarks");
			gemsManpowerRequest.setRemarks(remarks);

			gemsManpowerRequest.setGemsOrganisation(loggedInUser.getGemsOrganisation());

			String totalLevelsString = request.getParameter("totalLevels");
			if ((StringUtils.isNotBlank(totalLevelsString)) || (StringUtils.isNotEmpty(totalLevelsString))) {
				Integer totalLevels = new Integer(totalLevelsString);
				gemsManpowerRequest.setTotalLevels(totalLevels);
			}

			recruitementService.saveGemsManpowerRequest(gemsManpowerRequest);

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

	@RequestMapping(value = "/recruitment/getManpowerRequestInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getEmployeeInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsMapowerRequestIdString = request.getParameter("gemsMapowerRequestId");

		GemsManpowerRequest returnedGemsManpowerRequest = new GemsManpowerRequest();

		try {
			if ((StringUtils.isNotBlank(gemsMapowerRequestIdString))
					|| (StringUtils.isNotEmpty(gemsMapowerRequestIdString))) {

				Integer gemsMapowerRequestId = new Integer(gemsMapowerRequestIdString);

				returnedGemsManpowerRequest = recruitementService.getGemsManpowerRequest(gemsMapowerRequestId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapManpowerRequestInfo(returnedGemsManpowerRequest);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/recruitment/deleteManpowerRequest.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteManpowerRequest(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsMapowerRequestIdString = request.getParameter("gemsMapowerRequestId");

		GemsManpowerRequest returnedGemsManpowerRequest = new GemsManpowerRequest();

		try {
			if ((StringUtils.isNotBlank(gemsMapowerRequestIdString))
					|| (StringUtils.isNotEmpty(gemsMapowerRequestIdString))) {

				Integer gemsMapowerRequestId = new Integer(gemsMapowerRequestIdString);
				returnedGemsManpowerRequest = recruitementService.getGemsManpowerRequest(gemsMapowerRequestId);
				recruitementService.removeGemsManpowerRequest(returnedGemsManpowerRequest);

			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return modelMap;
			}

			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapManpowerRequestInfo(GemsManpowerRequest gemsManpowerRequest) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsManpowerRequest.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsManpowerRequest)) {
					return new JSONObject(true);
				}

				GemsManpowerRequest gemsManpowerRequest = (GemsManpowerRequest) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				String requestDate = "";
				if (gemsManpowerRequest.getRequestDate() != null) {
					requestDate = sdf.format(gemsManpowerRequest.getRequestDate());
				}
				String requestValidFrom = "";
				if (gemsManpowerRequest.getRequestValidFrom() != null) {
					requestValidFrom = sdf.format(gemsManpowerRequest.getRequestValidFrom());
				}
				String requestValidTo = "";
				if (gemsManpowerRequest.getRequestValidTo() != null) {
					requestValidTo = sdf.format(gemsManpowerRequest.getRequestValidTo());
				}
				String currentDepartmentName = "";
				Integer currentDepartmentId = new Integer(0);
				if ((gemsManpowerRequest.getGemsDepartment() != null)
						&& (gemsManpowerRequest.getGemsDepartment().getGemsDepartmentId() != 0)) {

					currentDepartmentName = "" + gemsManpowerRequest.getGemsDepartment().getDepartmentCode() + "";
					currentDepartmentId = gemsManpowerRequest.getGemsDepartment().getGemsDepartmentId();
				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsManpowerRequest.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsManpowerRequest.getGemsCustomerMaster()
							.getGemsCustomerMasterId();
					selected_customer = "" + gemsManpowerRequest.getGemsCustomerMaster().getGemsCustomerCode() + " - "
							+ gemsManpowerRequest.getGemsCustomerMaster().getGemsCustomerName() + "";

				}
				String requestStatus = "";
				if (gemsManpowerRequest.getRequestStatus() != null) {
					requestStatus = gemsManpowerRequest.getRequestStatus();
				}
				Integer totalLevels = new Integer(0);
				if (gemsManpowerRequest.getTotalLevels() != 0) {
					totalLevels = gemsManpowerRequest.getTotalLevels();
				}

				return new JSONObject().element("gemsMapowerRequestId", gemsManpowerRequest.getGemsMapowerRequestId())
						.element("jobCode", gemsManpowerRequest.getJobCode())
						.element("requestType", gemsManpowerRequest.getRequestType())
						.element("requestDate", requestDate).element("jobProfile", gemsManpowerRequest.getJobProfile())
						.element("numberOfResources", gemsManpowerRequest.getNumberOfResources())
						.element("jobPosition", gemsManpowerRequest.getJobPosition())
						.element("jobLocation", gemsManpowerRequest.getJobLocation())
						.element("minimumSalary", gemsManpowerRequest.getMinimumSalary())
						.element("maxSalary", gemsManpowerRequest.getMaxSalary())
						.element("profileExperienceMin", gemsManpowerRequest.getProfileExperienceMin())
						.element("profileExperienceMax", gemsManpowerRequest.getProfileExperienceMax())
						.element("employmentType", gemsManpowerRequest.getEmploymentType())
						.element("requestValidFrom", requestValidFrom).element("requestValidTo", requestValidTo)
						.element("requestStatus", requestStatus)
						.element("requestApprovalStatus", gemsManpowerRequest.getRequestApprovalStatus())
						.element("currentDepartmentId", currentDepartmentId)
						.element("currentDepartmentName", currentDepartmentName)
						.element("remarks", gemsManpowerRequest.getRemarks())
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("education", gemsManpowerRequest.getEducation()).element("totalLevels", totalLevels)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(gemsManpowerRequest, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/recruitment/mapRequestCandidate", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> mapRequestCandidate(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			if ((StringUtils.isNotBlank(request.getParameter("requirementId")))
					|| (StringUtils.isNotEmpty(request.getParameter("requirementId")))) {
				GemsManpowerRequest gemsManpowerRequest = recruitementService
						.getGemsManpowerRequest(new Integer(request.getParameter("requirementId")));
				String checkBoxValues = request.getParameter("checkboxArray");
				String[] candidateMasterIdArray = checkBoxValues.split(",");
				for (int i = 0; i < candidateMasterIdArray.length; i++) {
					Integer candidateId = new Integer(candidateMasterIdArray[0]);

					GemsCandidateMaster gemsCandidateMaster = recruitementService.getGemsCandidateMaster(candidateId);
					GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate = new GemsRecruitmentRequirementCandidate();
					gemsRecruitmentRequirementCandidate.setGemsCandidateMaster(gemsCandidateMaster);
					gemsRecruitmentRequirementCandidate.setGemsManpowerRequest(gemsManpowerRequest);
					gemsRecruitmentRequirementCandidate.setCreatedBy(loggedInUser.getGemsUserMasterId());
					gemsRecruitmentRequirementCandidate.setCreatedOn(todayDate);
					gemsRecruitmentRequirementCandidate.setUpdatedBy(loggedInUser.getGemsUserMasterId());
					gemsRecruitmentRequirementCandidate.setUpdatedOn(todayDate);

					// Will change this code later
					GemsCandidateStatusMaster gemsCandidateStatusMaster = recruitementService
							.getGemsCandidateStatusMaster(new Integer(1));
					gemsRecruitmentRequirementCandidate.setGemsCandidateStatusMaster(gemsCandidateStatusMaster);
					recruitementService.saveGemsRecruitmentRequirementCandidate(gemsRecruitmentRequirementCandidate);
					gemsCandidateMaster.setGemsCandidateStatus("Resume Under Processing");
					recruitementService.saveGemsCandidateMaster(gemsCandidateMaster);

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

	@RequestMapping(value = "/recruitment/viewResumeByRequest", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewResumeByRequest(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate = new GemsRecruitmentRequirementCandidate();

			int totalResults = 0;
			List<GemsRecruitmentRequirementCandidate> list = new ArrayList();
			if ((StringUtils.isNotBlank(request.getParameter("gemsMapowerRequestId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsMapowerRequestId")))) {
				GemsManpowerRequest gemsManpowerRequest = recruitementService
						.getGemsManpowerRequest(new Integer(request.getParameter("gemsMapowerRequestId")));
				gemsRecruitmentRequirementCandidate.setGemsManpowerRequest(gemsManpowerRequest);
				totalResults = recruitementService
						.getGemsGemsRecruitmentRequirementCandidateFilterCount(gemsRecruitmentRequirementCandidate);
				list = recruitementService.getGemsRecruitmentRequirementCandidateList(start, limit,
						gemsRecruitmentRequirementCandidate);

			}

			logger.info("Returned list size" + list.size());

			return getModelMapRecruitmentRequirementList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapRecruitmentRequirementList(List<GemsRecruitmentRequirementCandidate> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRecruitmentRequirementCandidate.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRecruitmentRequirementCandidate)) {
					return new JSONObject(true);
				}

				GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate = (GemsRecruitmentRequirementCandidate) bean;

				String candidateName = "";
				String candidateContactInfo = "";
				Integer gemsCandidateMasterId = new Integer(0);
				Integer selectedGemsCandidateStatusMasterId = new Integer(0);
				String selectedGemsCandidateStatusMaster = "";

				if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster() != null) {

					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateFirstName() != null) {
						candidateName = candidateName + gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateFirstName();
					}
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateLastName() != null) {
						candidateName = candidateName + " ";
						candidateName = candidateName + gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateLastName();
					}
					candidateContactInfo += "Phone:";
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidatePhone() != null) {
						candidateContactInfo += gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidatePhone();
					}
					candidateContactInfo += "<br>Mobile:";
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidateMobile() != null) {
						candidateContactInfo += gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateMobile();
					}
					candidateContactInfo += "<br>Email:";
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidateEmail() != null) {
						candidateContactInfo += gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateEmail();
					}
					gemsCandidateMasterId = gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateMasterId();
					selectedGemsCandidateStatusMasterId = gemsRecruitmentRequirementCandidate.getGemsCandidateStatusMaster().getGemsCandidateStatusMasterId();
					selectedGemsCandidateStatusMaster = gemsRecruitmentRequirementCandidate
							.getGemsCandidateStatusMaster().getStatusDescription();
				}

				return new JSONObject()
						.element("gemsRecruitmentRequirementCandidateId",
								gemsRecruitmentRequirementCandidate.getGemsRecruitmentRequirementCandidateId())
						.element("candidateName", candidateName)
						.element("selectedGemsCandidateStatusMasterId", selectedGemsCandidateStatusMasterId)
						.element("selectedGemsCandidateStatusMaster", selectedGemsCandidateStatusMaster)
						.element("candidateContactInfo", candidateContactInfo)
						.element("gemsCandidateMasterId", gemsCandidateMasterId);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/recruitment/getCandidateResumeInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCandidateResumeInfo(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsRecruitmentRequirementCandidateIdString = request
				.getParameter("gemsRecruitmentRequirementCandidateId");

		GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate = new GemsRecruitmentRequirementCandidate();

		try {
			if ((StringUtils.isNotBlank(gemsRecruitmentRequirementCandidateIdString))
					|| (StringUtils.isNotEmpty(gemsRecruitmentRequirementCandidateIdString))) {

				Integer gemsRecruitmentRequirementCandidateId = new Integer(
						gemsRecruitmentRequirementCandidateIdString);

				gemsRecruitmentRequirementCandidate = recruitementService
						.getGemsRecruitmentRequirementCandidate(gemsRecruitmentRequirementCandidateId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapCandidateResumeInfo(gemsRecruitmentRequirementCandidate);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapCandidateResumeInfo(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRecruitmentRequirementCandidate.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRecruitmentRequirementCandidate)) {
					return new JSONObject(true);
				}

				GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate = (GemsRecruitmentRequirementCandidate) bean;

				String candidateName = "";
				String candidateContactInfo = "";
				Integer gemsCandidateMasterId = new Integer(0);
				Integer selectedGemsCandidateStatusMasterId = new Integer(0);
				String selectedGemsCandidateStatusMaster = "";
				Integer totalLevels = new Integer(0);
				if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster() != null) {

					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateFirstName() != null) {
						candidateName = candidateName + gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateFirstName();
					}
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateLastName() != null) {
						candidateName = candidateName + " ";
						candidateName = candidateName + gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateLastName();
					}
					// candidateContactInfo +="Phone:";
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidatePhone() != null) {
						candidateContactInfo += gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidatePhone();
						candidateContactInfo += " / ";
					}
					// candidateContactInfo +="<br>Mobile:";
					if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidateMobile() != null) {
						candidateContactInfo += gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
								.getGemsCandidateMobile();
					}

					gemsCandidateMasterId = gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()
							.getGemsCandidateMasterId();
					selectedGemsCandidateStatusMasterId = gemsRecruitmentRequirementCandidate
							.getGemsCandidateStatusMaster().getGemsCandidateStatusMasterId();
					selectedGemsCandidateStatusMaster = gemsRecruitmentRequirementCandidate
							.getGemsCandidateStatusMaster().getStatusDescription();

					if (gemsRecruitmentRequirementCandidate.getGemsManpowerRequest() != null) {
						totalLevels = gemsRecruitmentRequirementCandidate.getGemsManpowerRequest().getTotalLevels();
					}
				}

				return new JSONObject()
						.element("gemsRecruitmentRequirementCandidateId",
								gemsRecruitmentRequirementCandidate.getGemsRecruitmentRequirementCandidateId())
						.element("candidateName", candidateName).element("candidateContactInfo", candidateContactInfo)
						.element("candidateEmail",
								gemsRecruitmentRequirementCandidate.getGemsCandidateMaster().getGemsCandidateEmail())
						.element("selectedGemsCandidateStatusMasterId", selectedGemsCandidateStatusMasterId)
						.element("selectedGemsCandidateStatusMaster", selectedGemsCandidateStatusMaster)
						.element("totalLevels", totalLevels).element("gemsCandidateMasterId", gemsCandidateMasterId);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsRecruitmentRequirementCandidate, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Status master
	 */

	@RequestMapping(value = "/master/viewGemsCandidateStatusMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsCandidateStatusMaster(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsCandidateStatusMaster gemsCandidateStatusMaster = new GemsCandidateStatusMaster();

			int totalResults = recruitementService.getGemsCandidateStatusMasterFilterCount(gemsCandidateStatusMaster);
			List<GemsCandidateStatusMaster> list = recruitementService.getGemsCandidateStatusMasterList(start, limit,
					gemsCandidateStatusMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapGemsCandidateStatusMasterList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapGemsCandidateStatusMasterList(List<GemsCandidateStatusMaster> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateStatusMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateStatusMaster)) {
					return new JSONObject(true);
				}

				GemsCandidateStatusMaster gemsCandidateStatusMaster = (GemsCandidateStatusMaster) bean;

				return new JSONObject()
						.element("gemsCandidateStatusMasterId",
								gemsCandidateStatusMaster.getGemsCandidateStatusMasterId())
						.element("statusDescription", gemsCandidateStatusMaster.getStatusDescription());
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	/*
	 * Gems Manpower Request Interviewer
	 */
	@RequestMapping(value = "/recruitment/viewManpowerRequestInterviewerList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewManpowerRequestInterviewerList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsManpowerRequestInterview gemsManpowerRequestInterview = new GemsManpowerRequestInterview();
			int totalResults = 0;
			List<GemsManpowerRequestInterview> list = new ArrayList();

			if ((StringUtils.isNotBlank(request.getParameter("gemsMapowerRequestId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsMapowerRequestId")))) {
				Integer gemsMapowerRequestId = new Integer(request.getParameter("gemsMapowerRequestId"));
				GemsManpowerRequest gemsManpowerRequest = new GemsManpowerRequest();
				gemsManpowerRequest.setGemsMapowerRequestId(gemsMapowerRequestId);
				gemsManpowerRequestInterview.setGemsManpowerRequest(gemsManpowerRequest);
				totalResults = recruitementService
						.getGemsManpowerRequestInterviewFilterCount(gemsManpowerRequestInterview);
				list = recruitementService.getGemsManpowerRequestInterviewList(start, limit,
						gemsManpowerRequestInterview);
			}

			logger.info("Returned list size" + list.size());

			return getModelMapManpowerRequestInterviewList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapManpowerRequestInterviewList(List<GemsManpowerRequestInterview> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsManpowerRequestInterview.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsManpowerRequestInterview)) {
					return new JSONObject(true);
				}
				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsManpowerRequestInterview gemsManpowerRequestInterview = (GemsManpowerRequestInterview) bean;

				String interviewerName = "";
				if (gemsManpowerRequestInterview.getInterviewer() != null) {
					interviewerName = "" + gemsManpowerRequestInterview.getInterviewer().getEmployeeLastName() + " "
							+ gemsManpowerRequestInterview.getInterviewer().getEmployeeLastName() + "";
				}
				String interviewDateString = "";
				if (gemsManpowerRequestInterview.getInterviewDate() != null) {
					interviewDateString = dateFormatter.format(gemsManpowerRequestInterview.getInterviewDate());
					if (gemsManpowerRequestInterview.getInterviewTime() != null) {
						interviewDateString += " <br> " + gemsManpowerRequestInterview.getInterviewTime();
					}
				}

				Integer selectedGemsManpowerRequestId = new Integer(0);
				String requisitionNumber = "";
				if (gemsManpowerRequestInterview.getGemsManpowerRequest() != null) {
					if (gemsManpowerRequestInterview.getGemsManpowerRequest().getJobCode() != null) {
						requisitionNumber += gemsManpowerRequestInterview.getGemsManpowerRequest().getJobCode();
					}
					if (gemsManpowerRequestInterview.getGemsManpowerRequest().getRequestDate() != null) {
						requisitionNumber += " ("
								+ gemsManpowerRequestInterview.getGemsManpowerRequest().getRequestDate() + ") ";
					}
					selectedGemsManpowerRequestId = gemsManpowerRequestInterview.getGemsManpowerRequest()
							.getGemsMapowerRequestId();
				}

				return new JSONObject()
						.element("gemsMapowerRequestInterviewId",
								gemsManpowerRequestInterview.getGemsMapowerRequestInterviewId())
						.element("interviewDate", interviewDateString)
						.element("interviewTime", gemsManpowerRequestInterview.getInterviewTime())
						.element("levelNo", gemsManpowerRequestInterview.getLevelNo())
						.element("interviewerRemarks", gemsManpowerRequestInterview.getInterviewerRemarks())
						.element("interviewStatus", gemsManpowerRequestInterview.getInterviewStatus())
						.element("skillRating", gemsManpowerRequestInterview.getSkillRating())
						.element("communicationRating", gemsManpowerRequestInterview.getCommunicationRating())
						.element("teamPlayer", gemsManpowerRequestInterview.getTeamPlayer())
						.element("otherRating", gemsManpowerRequestInterview.getOtherRating())
						.element("interviewerName", interviewerName)
						.element("selectedGemsManpowerRequestId", selectedGemsManpowerRequestId)
						.element("requisitionNumber", requisitionNumber);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/recruitment/saveManpowerRequestInterview", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveManpowerRequestInterview(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsManpowerRequestInterview gemsManpowerRequestInterview = new GemsManpowerRequestInterview();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsMapowerRequestInterviewId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsMapowerRequestInterviewId")))) {
				id_value = request.getParameter("gemsMapowerRequestInterviewId");
				gemsManpowerRequestInterview = recruitementService
						.getGemsManpowerRequestInterview(Integer.parseInt(id_value));

			}

			String interviewDateString = request.getParameter("interviewDate");
			if ((StringUtils.isNotBlank(interviewDateString)) || (StringUtils.isNotEmpty(interviewDateString))) {
				Date interviewDate = formatter.parse(interviewDateString);
				gemsManpowerRequestInterview.setInterviewDate(interviewDate);
			}

			String interviewTime = request.getParameter("interviewTime");
			gemsManpowerRequestInterview.setInterviewTime(interviewTime);

			String levelNo = request.getParameter("levelNo");
			if ((StringUtils.isNotBlank(levelNo)) || (StringUtils.isNotEmpty(levelNo))) {
				gemsManpowerRequestInterview.setLevelNo(new Integer(levelNo));
			}

			String interviewerRemarks = request.getParameter("interviewerRemarks");
			gemsManpowerRequestInterview.setInterviewerRemarks(interviewerRemarks);

			String interviewStatus = request.getParameter("interviewStatus");
			gemsManpowerRequestInterview.setInterviewStatus(interviewStatus);

			String skillRating = request.getParameter("skillRating");
			gemsManpowerRequestInterview.setSkillRating(skillRating);

			String communicationRating = request.getParameter("communicationRating");
			gemsManpowerRequestInterview.setCommunicationRating(communicationRating);

			String teamPlayer = request.getParameter("teamPlayer");
			gemsManpowerRequestInterview.setTeamPlayer(teamPlayer);

			String otherRating = request.getParameter("otherRating");
			gemsManpowerRequestInterview.setOtherRating(otherRating);

			int interviewerId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("dropdown_interviewer")))
					|| (StringUtils.isNotEmpty(request.getParameter("dropdown_interviewer")))) {
				try {
					interviewerId = Integer.parseInt(request.getParameter("dropdown_interviewer"));
					GemsEmployeeMaster gemsEmployeeMaster = employeeService.getGemsEmployeeMaster(interviewerId);
					gemsManpowerRequestInterview.setInterviewer(gemsEmployeeMaster);

				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsManpowerRequestInterview.setInterviewer(gemsManpowerRequestInterview.getInterviewer());
				}
			}

			String gemsMapowerRequestId = request.getParameter("gemsMapowerRequestId");
			gemsManpowerRequestInterview.setGemsManpowerRequest(
					recruitementService.getGemsManpowerRequest(Integer.parseInt(gemsMapowerRequestId)));
			recruitementService.saveGemsManpowerRequestInterview(gemsManpowerRequestInterview);

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

	@RequestMapping(value = "/recruitment/getManpowerRequestInterviewInfo.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getManpowerRequestInterviewInfo(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsMapowerRequestInterviewIdString = request.getParameter("gemsMapowerRequestInterviewId");

		GemsManpowerRequestInterview returnedGemsManpowerRequestInterview = new GemsManpowerRequestInterview();

		try {
			if ((StringUtils.isNotBlank(gemsMapowerRequestInterviewIdString))
					|| (StringUtils.isNotEmpty(gemsMapowerRequestInterviewIdString))) {

				Integer gemsMapowerRequestInterviewId = new Integer(gemsMapowerRequestInterviewIdString);

				returnedGemsManpowerRequestInterview = recruitementService
						.getGemsManpowerRequestInterview(gemsMapowerRequestInterviewId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapManpowerRequestInterviewInfo(returnedGemsManpowerRequestInterview);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapManpowerRequestInterviewInfo(
			GemsManpowerRequestInterview gemsManpowerRequestInterview) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsManpowerRequestInterview.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsManpowerRequestInterview)) {
					return new JSONObject(true);
				}
				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsManpowerRequestInterview gemsManpowerRequestInterview = (GemsManpowerRequestInterview) bean;

				String interviewerName = "";
				Integer interviewerId = new Integer(0);
				if (gemsManpowerRequestInterview.getInterviewer() != null) {
					interviewerName = "" + gemsManpowerRequestInterview.getInterviewer().getEmployeeLastName() + " "
							+ gemsManpowerRequestInterview.getInterviewer().getEmployeeLastName() + "";
					interviewerId = gemsManpowerRequestInterview.getInterviewer().getGemsEmployeeMasterId();
				}
				String interviewDateString = "";
				if (gemsManpowerRequestInterview.getInterviewDate() != null) {
					interviewDateString = dateFormatter.format(gemsManpowerRequestInterview.getInterviewDate());
				}

				Integer selectedGemsManpowerRequestId = new Integer(0);
				String requisitionNumber = "";
				if (gemsManpowerRequestInterview.getGemsManpowerRequest() != null) {
					if (gemsManpowerRequestInterview.getGemsManpowerRequest().getJobCode() != null) {
						requisitionNumber += gemsManpowerRequestInterview.getGemsManpowerRequest().getJobCode();
					}
					if (gemsManpowerRequestInterview.getGemsManpowerRequest().getRequestDate() != null) {
						requisitionNumber += " ("
								+ gemsManpowerRequestInterview.getGemsManpowerRequest().getRequestDate() + ") ";
					}
					selectedGemsManpowerRequestId = gemsManpowerRequestInterview.getGemsManpowerRequest()
							.getGemsMapowerRequestId();
				}

				return new JSONObject()
						.element("gemsMapowerRequestInterviewId",
								gemsManpowerRequestInterview.getGemsMapowerRequestInterviewId())
						.element("interviewDate", interviewDateString)
						.element("interviewTime", gemsManpowerRequestInterview.getInterviewTime())
						.element("levelNo", gemsManpowerRequestInterview.getLevelNo())
						.element("interviewerRemarks", gemsManpowerRequestInterview.getInterviewerRemarks())
						.element("interviewStatus", gemsManpowerRequestInterview.getInterviewStatus())
						.element("skillRating", gemsManpowerRequestInterview.getSkillRating())
						.element("communicationRating", gemsManpowerRequestInterview.getCommunicationRating())
						.element("teamPlayer", gemsManpowerRequestInterview.getTeamPlayer())
						.element("otherRating", gemsManpowerRequestInterview.getOtherRating())
						.element("selectedInterviewerId", interviewerId).element("interviewerName", interviewerName)
						.element("gemsMapowerRequestId", selectedGemsManpowerRequestId)
						.element("requisitionNumber", requisitionNumber);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsManpowerRequestInterview, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	@RequestMapping(value = "/recruitment/deleteManpowerRequestInterview.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteManpowerRequestInterview(HttpServletRequest request)
			throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsMapowerRequestInterviewIdString = request.getParameter("gemsMapowerRequestInterviewId");

		GemsManpowerRequestInterview returnedGemsManpowerRequestInterview = new GemsManpowerRequestInterview();

		try {
			if ((StringUtils.isNotBlank(gemsMapowerRequestInterviewIdString))
					|| (StringUtils.isNotEmpty(gemsMapowerRequestInterviewIdString))) {

				Integer gemsMapowerRequestInterviewId = new Integer(gemsMapowerRequestInterviewIdString);
				returnedGemsManpowerRequestInterview = recruitementService
						.getGemsManpowerRequestInterview(gemsMapowerRequestInterviewId);
				recruitementService.removeGemsManpowerRequestInterview(returnedGemsManpowerRequestInterview);
				logger.info("Delete Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Deleted Successfully");
				return modelMap;
			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return modelMap;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
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

}
