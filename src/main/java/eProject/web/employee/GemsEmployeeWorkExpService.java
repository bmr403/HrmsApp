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
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeWorkExp;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmployeeWorkExpService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeWorkExpService.class);

	@RequestMapping(value = "/employee/viewGemsEmployeeWorkExpList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsEmployeeWorkExpList(HttpServletRequest request) {

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

			GemsEmployeeWorkExp gemsEmployeeWorkExp = new GemsEmployeeWorkExp();

			String searchEmpCode = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmployeeWorkExp
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getGemsEmployeeWorkExpFilterCount(gemsEmployeeWorkExp);
			List<GemsEmployeeWorkExp> list = employeeService.getGemsEmployeeWorkExpList(start, limit,
					gemsEmployeeWorkExp);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeWorkExpList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmployeeWorkExp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmployeeWorkExp(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeWorkExp gemsEmployeeWorkExp = new GemsEmployeeWorkExp();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				gemsEmployeeWorkExp.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}
			if (StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId"))) {
				gemsEmployeeWorkExp.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmpWorkExpId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmpWorkExpId")))) {
				id_value = request.getParameter("gemsEmpWorkExpId");
				gemsEmployeeWorkExp = employeeService.getGemsEmployeeWorkExp(Integer.parseInt(id_value));
			} else {
				gemsEmployeeWorkExp.setCreatedOn(todayDate);
				gemsEmployeeWorkExp.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String fromDateString = request.getParameter("fromDate");
			if ((StringUtils.isNotBlank(fromDateString)) || (StringUtils.isNotEmpty(fromDateString))) {
				Date fromDate = formatter.parse(fromDateString);
				gemsEmployeeWorkExp.setFromDate(fromDate);
			}

			String toDateString = request.getParameter("toDate");
			if ((StringUtils.isNotBlank(toDateString)) || (StringUtils.isNotEmpty(toDateString))) {
				Date toDate = formatter.parse(toDateString);
				gemsEmployeeWorkExp.setToDate(toDate);
			}
			double workExperience = 0.0;
			if (gemsEmployeeWorkExp.getToDate().compareTo(gemsEmployeeWorkExp.getFromDate()) == 0) {
				workExperience = 0.0;
			} else {
				int monthCount = 0;
				Calendar cal = Calendar.getInstance();
				cal.setTime(gemsEmployeeWorkExp.getFromDate());
				int c1date = cal.get(Calendar.DATE);
				int c1month = cal.get(Calendar.MONTH);
				int c1year = cal.get(Calendar.YEAR);
				cal.setTime(gemsEmployeeWorkExp.getToDate());
				int c2date = cal.get(Calendar.DATE);
				int c2month = cal.get(Calendar.MONTH);
				int c2year = cal.get(Calendar.YEAR);

				monthCount = ((c2year - c1year) * 12) + (c2month - c1month) + ((c2date >= c1date) ? 1 : 0);
				workExperience = new Double(monthCount);

			}
			gemsEmployeeWorkExp.setTotalExpInMonth(workExperience);

			String companyName = request.getParameter("companyName");
			gemsEmployeeWorkExp.setCompanyName(companyName);

			String jobTitle = request.getParameter("jobTitle");
			gemsEmployeeWorkExp.setJobTitle(jobTitle);

			String isActive = request.getParameter("expActiveStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmployeeWorkExp.setActiveStatus(1);
			} else {
				gemsEmployeeWorkExp.setActiveStatus(0);
			}

			employeeService.saveGemsEmployeeWorkExp(gemsEmployeeWorkExp);
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

	@RequestMapping(value = "/employee/deleteGemsEmployeeWorkExp", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmployeeWorkExp(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmpWorkExpId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeWorkExp gemsEmployeeWorkExp = employeeService.getGemsEmployeeWorkExp(gemsEmpWorkExpId);
			employeeService.removeGemsEmployeeWorkExp(gemsEmployeeWorkExp);
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

	@RequestMapping(value = "/employee/getGemsEmployeeWorkExpDetail.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmpWorkExpIdString = request.getParameter("gemsEmpWorkExpId");

		GemsEmployeeWorkExp returnedGemsEmployeeWorkExp = new GemsEmployeeWorkExp();

		try {
			if (gemsEmpWorkExpIdString != null) {

				Integer gemsEmployeeEducationDetailId = new Integer(gemsEmpWorkExpIdString);

				returnedGemsEmployeeWorkExp = employeeService.getGemsEmployeeWorkExp(gemsEmployeeEducationDetailId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapEmployeeExpInfo(returnedGemsEmployeeWorkExp);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapEmployeeExpInfo(GemsEmployeeWorkExp gemsEmployeeWorkExp) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeWorkExp.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeWorkExp)) {
					return new JSONObject(true);
				}

				GemsEmployeeWorkExp gemsEmployeeWorkExp = (GemsEmployeeWorkExp) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				if (gemsEmployeeWorkExp.getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeWorkExp.getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeWorkExp.getToDate() != null) {
					toDate = sdf.format(gemsEmployeeWorkExp.getToDate());
				}

				boolean expActiveStatus = false;

				if (gemsEmployeeWorkExp.getActiveStatus() == 1) {
					expActiveStatus = true;
				}

				return new JSONObject().element("gemsEmpWorkExpId", gemsEmployeeWorkExp.getGemsEmpWorkExpId())
						.element("companyName", gemsEmployeeWorkExp.getCompanyName()).element("fromDate", fromDate)
						.element("toDate", toDate).element("jobTitle", gemsEmployeeWorkExp.getJobTitle())
						.element("totalExpInMonth", gemsEmployeeWorkExp.getTotalExpInMonth())
						.element("expActiveStatus", expActiveStatus).element("gemsEmployeeMasterId",
								gemsEmployeeWorkExp.getGemsEmployeeMaster().getGemsEmployeeMasterId())

				;

			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeWorkExp, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeWorkExpList(List<GemsEmployeeWorkExp> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeWorkExp.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeWorkExp)) {
					return new JSONObject(true);
				}

				GemsEmployeeWorkExp gemsEmployeeWorkExp = (GemsEmployeeWorkExp) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeWorkExp.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeWorkExp.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeWorkExp.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeWorkExp.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeWorkExp.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeWorkExp.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				if (gemsEmployeeWorkExp.getFromDate() != null) {
					fromDate = sdf.format(gemsEmployeeWorkExp.getFromDate());
				}

				String toDate = "";
				if (gemsEmployeeWorkExp.getToDate() != null) {
					toDate = sdf.format(gemsEmployeeWorkExp.getToDate());
				}

				boolean expActiveStatus = false;

				if (gemsEmployeeWorkExp.getActiveStatus() == 1) {
					expActiveStatus = true;
				}

				return new JSONObject().element("gemsEmpWorkExpId", gemsEmployeeWorkExp.getGemsEmpWorkExpId())
						.element("companyName", gemsEmployeeWorkExp.getCompanyName()).element("fromDate", fromDate)
						.element("expActiveStatus", expActiveStatus).element("toDate", toDate)
						.element("jobTitle", gemsEmployeeWorkExp.getJobTitle())
						.element("totalExpInMonth", gemsEmployeeWorkExp.getTotalExpInMonth())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
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
