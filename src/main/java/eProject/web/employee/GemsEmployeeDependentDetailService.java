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
import eProject.domain.employee.GemsEmployeeDependentDetail;
import eProject.domain.employee.GemsEmployeeWorkExp;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmployeeDependentDetailService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeDependentDetailService.class);

	@RequestMapping(value = "/employee/viewEmployeeDependentDetailList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeDependentDetailList(HttpServletRequest request) {

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

			GemsEmployeeDependentDetail gemsEmployeeDependentDetail = new GemsEmployeeDependentDetail();

			String searchEmpCode = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmployeeDependentDetail
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getGemsEmployeeDependentDetailFilterCount(gemsEmployeeDependentDetail);
			List<GemsEmployeeDependentDetail> list = employeeService.getGemsEmployeeDependentDetailList(start, limit,
					gemsEmployeeDependentDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeDependentDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmployeeDependentDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmployeeDependentDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeDependentDetail gemsEmployeeDependentDetail = new GemsEmployeeDependentDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsEmployeeDependentDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsEmployeeDependentDetail.setUpdatedOn(todayDate);
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				gemsEmployeeDependentDetail.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeDependentDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeDependentDetailId")))) {
				id_value = request.getParameter("gemsEmployeeDependentDetailId");
				gemsEmployeeDependentDetail = employeeService
						.getGemsEmployeeDependentDetail(Integer.parseInt(id_value));

			} else {
				gemsEmployeeDependentDetail.setCreatedOn(todayDate);
				gemsEmployeeDependentDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String dependentName = request.getParameter("dependentName");
			gemsEmployeeDependentDetail.setDependentName(dependentName);

			String dependentDateOfBirthString = request.getParameter("dependentDateOfBirth");
			if ((StringUtils.isNotBlank(dependentDateOfBirthString))
					|| (StringUtils.isNotEmpty(dependentDateOfBirthString))) {
				Date dependentDateOfBirth = formatter.parse(dependentDateOfBirthString);
				gemsEmployeeDependentDetail.setDependentDateOfBirth(dependentDateOfBirth);
			}

			String dependentRelationship = request.getParameter("dependentRelationship");
			gemsEmployeeDependentDetail.setDependentRelationship(dependentRelationship);

			String isActive = request.getParameter("dependentactiveStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmployeeDependentDetail.setActiveStatus(1);
			} else {
				gemsEmployeeDependentDetail.setActiveStatus(0);
			}

			employeeService.saveGemsEmployeeDependentDetail(gemsEmployeeDependentDetail);
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

	@RequestMapping(value = "/employee/getGemsEmployeeDependentDetail.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeDependentDetailIdString = request.getParameter("gemsEmployeeDependentDetailId");

		GemsEmployeeDependentDetail returnedGemsEmployeeDependentInfo = new GemsEmployeeDependentDetail();

		try {
			if (gemsEmployeeDependentDetailIdString != null) {

				Integer gemsEmployeeDependentDetailId = new Integer(gemsEmployeeDependentDetailIdString);

				returnedGemsEmployeeDependentInfo = employeeService
						.getGemsEmployeeDependentDetail(gemsEmployeeDependentDetailId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapEmployeeDependentInfo(returnedGemsEmployeeDependentInfo);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/employee/deleteGemsEmployeeDependentDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmployeeDependentDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeDependentDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail = employeeService
					.getGemsEmployeeDependentDetail(gemsEmployeeDependentDetailId);
			employeeService.removeGemsEmployeeDependentDetail(gemsEmployeeDependentDetail);
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

	private Map<String, Object> getModelMapEmployeeDependentInfo(
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeDependentDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeDependentDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeeDependentDetail gemsEmployeeDependentDetail = (GemsEmployeeDependentDetail) bean;

				String dependentDateOfBirth = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsEmployeeDependentDetail.getDependentDateOfBirth() != null) {
					dependentDateOfBirth = sdf.format(gemsEmployeeDependentDetail.getDependentDateOfBirth());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeDependentDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeDependentDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				boolean dependentactiveStatus = false;

				if (gemsEmployeeDependentDetail.getActiveStatus() == 1) {
					dependentactiveStatus = true;
				}
				return new JSONObject()
						.element("gemsEmployeeDependentDetailId",
								gemsEmployeeDependentDetail.getGemsEmployeeDependentDetailId())
						.element("dependentDateOfBirth", dependentDateOfBirth)
						.element("dependentName", gemsEmployeeDependentDetail.getDependentName())
						.element("dependentRelationship", gemsEmployeeDependentDetail.getDependentRelationship())
						.element("dependentactiveStatus", dependentactiveStatus)
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeDependentDetail, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeDependentDetailList(List<GemsEmployeeDependentDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeDependentDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeDependentDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeeDependentDetail gemsEmployeeDependentDetail = (GemsEmployeeDependentDetail) bean;

				String dependentDateOfBirth = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsEmployeeDependentDetail.getDependentDateOfBirth() != null) {
					dependentDateOfBirth = sdf.format(gemsEmployeeDependentDetail.getDependentDateOfBirth());
				}

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeDependentDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeDependentDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeDependentDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				boolean dependentactiveStatus = false;

				if (gemsEmployeeDependentDetail.getActiveStatus() == 1) {
					dependentactiveStatus = true;
				}
				return new JSONObject()
						.element("gemsEmployeeDependentDetailId",
								gemsEmployeeDependentDetail.getGemsEmployeeDependentDetailId())
						.element("dependentDateOfBirth", dependentDateOfBirth)
						.element("dependentName", gemsEmployeeDependentDetail.getDependentName())
						.element("dependentRelationship", gemsEmployeeDependentDetail.getDependentRelationship())
						.element("dependentactiveStatus", dependentactiveStatus)
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName);
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
