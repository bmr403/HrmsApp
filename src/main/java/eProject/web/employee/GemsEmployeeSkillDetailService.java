package eProject.web.employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
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

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmployeeSkillDetail;
import eProject.domain.master.GemsUserMaster;

import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmployeeSkillDetailService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeSkillDetailService.class);

	@RequestMapping(value = "/employee/viewEmployeeSkillList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeSkillList(HttpServletRequest request) {

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

			GemsEmployeeSkillDetail gemsEmployeeSkillDetail = new GemsEmployeeSkillDetail();

			String selectedGemsEmployeeMasterId = request.getParameter("gemsEmployeeMasterId");
			if (selectedGemsEmployeeMasterId != null && selectedGemsEmployeeMasterId.isEmpty() == false) {
				gemsEmployeeSkillDetail.setGemsEmployeeMaster(
						employeeService.getGemsEmployeeMaster(Integer.parseInt(selectedGemsEmployeeMasterId)));
			}

			int totalResults = employeeService.getGemsEmployeeSkillDetailFilterCount(gemsEmployeeSkillDetail);
			List<GemsEmployeeSkillDetail> list = employeeService.getGemsEmployeeSkillDetailList(start, limit,
					gemsEmployeeSkillDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeSkillDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveEmployeeSkillDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmployeeEducationDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeSkillDetail gemsEmployeeSkillDetail = new GemsEmployeeSkillDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				gemsEmployeeSkillDetail.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeSkillId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeSkillId")))) {
				id_value = request.getParameter("gemsEmployeeSkillId");
				gemsEmployeeSkillDetail = employeeService.getGemsEmployeeSkillDetail(Integer.parseInt(id_value));

			} else {
				gemsEmployeeSkillDetail.setCreatedOn(todayDate);
				gemsEmployeeSkillDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String isPrimarySkill = request.getParameter("isPrimarySkill");
			if ((isPrimarySkill == "on") || (isPrimarySkill.equalsIgnoreCase("on"))) {
				gemsEmployeeSkillDetail.setIsPrimarySkill(1);
			} else {
				gemsEmployeeSkillDetail.setIsPrimarySkill(0);
			}

			gemsEmployeeSkillDetail.setUpdatedOn(todayDate);
			gemsEmployeeSkillDetail.setUpdatedBy(loggedInUser.getCreatedBy());

			String skillName = request.getParameter("skillName");
			gemsEmployeeSkillDetail.setSkillName(skillName);

			String versionNo = request.getParameter("versionNo");
			gemsEmployeeSkillDetail.setVersionNo(versionNo);

			String lastUsed = request.getParameter("lastUsed");
			gemsEmployeeSkillDetail.setLastUsed(Integer.parseInt(lastUsed));

			String experienseInMonths = request.getParameter("experienseInMonths");
			gemsEmployeeSkillDetail.setExperienseInMonths(Integer.parseInt(experienseInMonths));

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmployeeSkillDetail.setActiveStatus(1);
			} else {
				gemsEmployeeSkillDetail.setActiveStatus(0);
			}

			employeeService.saveGemsEmployeeSkillDetail(gemsEmployeeSkillDetail);
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

	@RequestMapping(value = "/employee/deleteEmployeeSkillDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteEmployeeSkillDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeSkillId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeSkillDetail gemsEmployeeSkillDetail = employeeService
					.getGemsEmployeeSkillDetail(gemsEmployeeSkillId);
			employeeService.removeGemsEmployeeSkillDetail(gemsEmployeeSkillDetail);
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

	@RequestMapping(value = "/employee/getEmployeeSkillDetail.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeSkillIdString = request.getParameter("gemsEmployeeSkillId");

		GemsEmployeeSkillDetail returnedGemsEmployeeSkillDetail = new GemsEmployeeSkillDetail();

		try {
			if (gemsEmployeeSkillIdString != null) {

				Integer gemsEmployeeSkillId = new Integer(gemsEmployeeSkillIdString);

				returnedGemsEmployeeSkillDetail = employeeService.getGemsEmployeeSkillDetail(gemsEmployeeSkillId);

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapEmployeeSkillInfo(returnedGemsEmployeeSkillDetail);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapEmployeeSkillInfo(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeSkillDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeSkillDetail)) {
					return new JSONObject(true);
				}

				final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

				GemsEmployeeSkillDetail gemsEmployeeSkillDetail = (GemsEmployeeSkillDetail) bean;

				boolean isPrimarySkill = false;

				if (gemsEmployeeSkillDetail.getIsPrimarySkill() == 1) {
					isPrimarySkill = true;
				}
				boolean activeStatus = false;

				if (gemsEmployeeSkillDetail.getActiveStatus() == 1) {
					activeStatus = true;
				}
				return new JSONObject().element("gemsEmployeeSkillId", gemsEmployeeSkillDetail.getGemsEmployeeSkillId())
						.element("skillName", gemsEmployeeSkillDetail.getSkillName())
						.element("versionNo", gemsEmployeeSkillDetail.getVersionNo())
						.element("experienseInMonths", gemsEmployeeSkillDetail.getExperienseInMonths())
						.element("lastUsed", gemsEmployeeSkillDetail.getLastUsed())
						.element("isPrimarySkill", isPrimarySkill).element("activeStatus", activeStatus)
						.element("gemsEmployeeMasterId",
								gemsEmployeeSkillDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId());

			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmployeeSkillDetail, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeSkillDetailList(List<GemsEmployeeSkillDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeSkillDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeSkillDetail)) {
					return new JSONObject(true);
				}

				GemsEmployeeSkillDetail gemsEmployeeSkillDetail = (GemsEmployeeSkillDetail) bean;
				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";

				if (gemsEmployeeSkillDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeSkillDetail.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeSkillDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeSkillDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeSkillDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeSkillDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				String isPrimarySkill = "";
				if (gemsEmployeeSkillDetail.getIsPrimarySkill() == 1) {
					isPrimarySkill = "Yes";
				} else {
					isPrimarySkill = "No";
				}
				boolean activeStatus = false;

				if (gemsEmployeeSkillDetail.getActiveStatus() == 1) {
					activeStatus = true;
				}
				return new JSONObject().element("gemsEmployeeSkillId", gemsEmployeeSkillDetail.getGemsEmployeeSkillId())
						.element("activeStatus", activeStatus).element("isPrimarySkill", isPrimarySkill)
						.element("skillName", gemsEmployeeSkillDetail.getSkillName())
						.element("activeStatus", gemsEmployeeSkillDetail.getActiveStatus())
						.element("versionNo", gemsEmployeeSkillDetail.getVersionNo())
						.element("lastUsed", gemsEmployeeSkillDetail.getLastUsed())
						.element("experienseInMonths", gemsEmployeeSkillDetail.getExperienseInMonths())
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
