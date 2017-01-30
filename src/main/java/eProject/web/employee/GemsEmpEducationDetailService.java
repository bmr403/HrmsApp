package eProject.web.employee;

import java.text.ParseException;
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
import eProject.domain.employee.GemsEmployeeSkillDetail;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmpEducationDetailService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmpBankDetailService.class);

	@RequestMapping(value = "/employee/viewEmployeeEducationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeEducationList(HttpServletRequest request) {

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

			GemsEmpEducationDetail gemsEmpEducationDetail = new GemsEmpEducationDetail();

			String searchEmpCode = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmpEducationDetail
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getGemsEmpEducationDetailFilterCount(gemsEmpEducationDetail);
			List<GemsEmpEducationDetail> list = employeeService.getGemsEmpEducationDetailList(start, limit,
					gemsEmpEducationDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeEducationDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveEmployeeEducationDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmployeeEducationDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmpEducationDetail gemsEmpEducationDetail = new GemsEmpEducationDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				gemsEmpEducationDetail.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeEducationDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeEducationDetailId")))) {
				id_value = request.getParameter("gemsEmployeeEducationDetailId");
				gemsEmpEducationDetail = employeeService.getGemsEmpEducationDetail(Integer.parseInt(id_value));
			} else {
				gemsEmpEducationDetail.setCreatedOn(todayDate);
				gemsEmpEducationDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String isCourseRegular = request.getParameter("isCourseRegular");
			if ((isCourseRegular == "on") || (isCourseRegular.equalsIgnoreCase("on"))) {
				gemsEmpEducationDetail.setIsCourseRegular(1);
			} else {
				gemsEmpEducationDetail.setIsCourseRegular(0);
			}
			String isActive = request.getParameter("educationActiveStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmpEducationDetail.setActiveStatus(1);
			} else {
				gemsEmpEducationDetail.setActiveStatus(0);
			}

			String yearPercentage = request.getParameter("yearPercentage");
			gemsEmpEducationDetail.setYearPercentage(Double.valueOf(yearPercentage));

			String universityName = request.getParameter("universityName");
			gemsEmpEducationDetail.setUniversityName(universityName);

			String yearOfPass = request.getParameter("yearOfPass");
			if ((StringUtils.isNotBlank(yearOfPass)) || (StringUtils.isNotEmpty(yearOfPass))) {
				gemsEmpEducationDetail.setYearOfPass(yearOfPass);
			}

			int gemsEducationMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("course_dropdown")))
					|| (StringUtils.isNotEmpty(request.getParameter("course_dropdown")))) {
				try {
					gemsEducationMasterId = Integer.parseInt(request.getParameter("course_dropdown"));

					gemsEmpEducationDetail
							.setGemsEducationMaster(masterService.getGemsEducationMaster(gemsEducationMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsEmpEducationDetail.setGemsEducationMaster(gemsEmpEducationDetail.getGemsEducationMaster());
				}
			}

			gemsEmpEducationDetail.setActiveStatus(1);

			employeeService.saveGemsEmpEducationDetail(gemsEmpEducationDetail);
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

	@RequestMapping(value = "/employee/deleteEmployeeEducationDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteEmployeeEducationDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeEducationDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmpEducationDetail gemsEmpEducationDetail = employeeService
					.getGemsEmpEducationDetail(gemsEmployeeEducationDetailId);
			employeeService.removeGemsEmpEducationDetail(gemsEmpEducationDetail);
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

	@RequestMapping(value = "/employee/getEmployeeEducationDetail.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeEducationDetailIdString = request.getParameter("gemsEmployeeEducationDetailId");

		GemsEmpEducationDetail returnedGemsEmpEducationDetail = new GemsEmpEducationDetail();

		try {
			if (gemsEmployeeEducationDetailIdString != null) {

				Integer gemsEmployeeEducationDetailId = new Integer(gemsEmployeeEducationDetailIdString);

				returnedGemsEmpEducationDetail = employeeService
						.getGemsEmpEducationDetail(gemsEmployeeEducationDetailId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapEmployeeEducationInfo(returnedGemsEmpEducationDetail);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapEmployeeEducationInfo(GemsEmpEducationDetail gemsEmpEducationDetail) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmpEducationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmpEducationDetail)) {
					return new JSONObject(true);
				}

				GemsEmpEducationDetail gemsEmployeeEducationDetail = (GemsEmpEducationDetail) bean;

				boolean isCourseRegular = false;

				if (gemsEmployeeEducationDetail.getIsCourseRegular() == 1) {
					isCourseRegular = true;
				}

				int selectedGemsEducationMasterId = 0;
				String selected_education = "";
				if (gemsEmployeeEducationDetail.getGemsEducationMaster() != null) {
					selectedGemsEducationMasterId = gemsEmployeeEducationDetail.getGemsEducationMaster()
							.getGemsEducationMasterId();
					selected_education = gemsEmployeeEducationDetail.getGemsEducationMaster().getEducationCode();

				}
				boolean educationActiveStatus = false;

				if (gemsEmployeeEducationDetail.getActiveStatus() == 1) {
					educationActiveStatus = true;
				}
				return new JSONObject()
						.element("gemsEmployeeEducationDetailId",
								gemsEmployeeEducationDetail.getGemsEmployeeEducationDetailId())
						.element("isCourseRegular", isCourseRegular)
						.element("yearPercentage", gemsEmployeeEducationDetail.getYearPercentage())
						.element("universityName", gemsEmployeeEducationDetail.getUniversityName())
						.element("yearOfPass", gemsEmployeeEducationDetail.getYearOfPass())
						.element("selectedGemsEducationMasterId", selectedGemsEducationMasterId)
						.element("selected_education", selected_education)
						.element("educationActiveStatus", educationActiveStatus).element("gemsEmployeeMasterId",
								gemsEmployeeEducationDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId());

			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmpEducationDetail, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeEducationDetailList(List<GemsEmpEducationDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmpEducationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmpEducationDetail)) {
					return new JSONObject(true);
				}

				GemsEmpEducationDetail gemsEmpEducationDetail = (GemsEmpEducationDetail) bean;

				int selectedGemsEducationMasterId = 0;
				String selected_education = "";
				if (gemsEmpEducationDetail.getGemsEducationMaster() != null) {
					selectedGemsEducationMasterId = gemsEmpEducationDetail.getGemsEducationMaster()
							.getGemsEducationMasterId();
					selected_education = gemsEmpEducationDetail.getGemsEducationMaster().getEducationCode();

				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmpEducationDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmpEducationDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmpEducationDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpEducationDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmpEducationDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpEducationDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				String isCourseRegular = "";
				if (gemsEmpEducationDetail.getIsCourseRegular() == 1) {
					isCourseRegular = "Yes";
				} else {
					isCourseRegular = "No";
				}

				boolean educationActiveStatus = false;

				if (gemsEmpEducationDetail.getActiveStatus() == 1) {
					educationActiveStatus = true;
				}

				return new JSONObject()
						.element("gemsEmployeeEducationDetailId",
								gemsEmpEducationDetail.getGemsEmployeeEducationDetailId())
						.element("isCourseRegular", isCourseRegular)
						.element("yearPercentage", gemsEmpEducationDetail.getYearPercentage())
						.element("universityName", gemsEmpEducationDetail.getUniversityName())
						.element("yearOfPass", gemsEmpEducationDetail.getYearOfPass())
						.element("selectedGemsEducationMasterId", selectedGemsEducationMasterId)
						.element("selected_education", selected_education)
						.element("gemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("educationActiveStatus", educationActiveStatus)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)

				;
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
