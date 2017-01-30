package eProject.web.employee;

import java.math.BigDecimal;
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
import eProject.domain.employee.GemsEmpSalaryComponent;
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeWorkExp;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmpSalaryComponentService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmpSalaryComponentService.class);

	@RequestMapping(value = "/employee/viewGemsEmpSalaryComponentList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsEmpSalaryComponentList(HttpServletRequest request) {

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

			GemsEmpSalaryComponent gemsEmpSalaryComponent = new GemsEmpSalaryComponent();

			String searchEmpCode = request.getParameter("selectedGemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmpSalaryComponent
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getgemsEmpSalaryComponentFilterCount(gemsEmpSalaryComponent);
			List<GemsEmpSalaryComponent> list = employeeService.getgemsEmpSalaryComponentList(start, limit,
					gemsEmpSalaryComponent);

			logger.info("Returned list size" + list.size());

			return getModelMapEmpSalaryComponentList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmpSalaryComponent", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmpSalaryComponent(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmpSalaryComponent gemsEmpSalaryComponent = new GemsEmpSalaryComponent();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsEmployeeMasterId")))) {
				gemsEmpSalaryComponent.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("selectedGemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmpSalaryComponentId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmpSalaryComponentId")))) {
				id_value = request.getParameter("gemsEmpSalaryComponentId");
				gemsEmpSalaryComponent = employeeService.getGemsEmpSalaryComponent(Integer.parseInt(id_value));
			} else {
				gemsEmpSalaryComponent.setCreatedOn(todayDate);
				gemsEmpSalaryComponent.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String salaryComponent = request.getParameter("salaryComponent");
			gemsEmpSalaryComponent.setSalaryComponent(salaryComponent);

			String payFrequency = request.getParameter("payFrequency");
			gemsEmpSalaryComponent.setPayFrequency(payFrequency);

			String salaryComponentComment = request.getParameter("salaryComponentComment");
			gemsEmpSalaryComponent.setSalaryComponentComment(salaryComponentComment);

			String salaryAmount = request.getParameter("salaryAmount");
			gemsEmpSalaryComponent.setSalaryAmount(new BigDecimal(salaryAmount));

			int gemsPayGradeId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_paygrade")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_paygrade")))) {
				try {
					gemsPayGradeId = Integer.parseInt(request.getParameter("selected_paygrade"));

					gemsEmpSalaryComponent.setGemsPayGrade(masterService.getGemsPayGrade(gemsPayGradeId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsEmpSalaryComponent.setGemsPayGrade(gemsEmpSalaryComponent.getGemsPayGrade());
				}
			}

			gemsEmpSalaryComponent.setGemsCurrencyMaster(loggedInUser.getGemsOrganisation().getGemsCurrencyMaster());

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmpSalaryComponent.setActiveStatus(1);
			} else {
				gemsEmpSalaryComponent.setActiveStatus(0);
			}

			employeeService.saveGemsEmpSalaryComponent(gemsEmpSalaryComponent);
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

	@RequestMapping(value = "/employee/deleteGemsEmpSalaryComponent", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmpSalaryComponent(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsPayGradeId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmpSalaryComponent gemsEmpSalaryComponent = employeeService.getGemsEmpSalaryComponent(gemsPayGradeId);
			employeeService.removeGemsEmpSalaryComponent(gemsEmpSalaryComponent);
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

	private Map<String, Object> getModelMapEmpSalaryComponentList(List<GemsEmpSalaryComponent> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmpSalaryComponent.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmpSalaryComponent)) {
					return new JSONObject(true);
				}

				GemsEmpSalaryComponent gemsEmpSalaryComponent = (GemsEmpSalaryComponent) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmpSalaryComponent.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmpSalaryComponent.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmpSalaryComponent.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpSalaryComponent.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmpSalaryComponent.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpSalaryComponent.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				int selectedGemsPayGradeId = 0;
				String selected_paygrade = "";
				if (gemsEmpSalaryComponent.getGemsPayGrade() != null) {
					selectedGemsPayGradeId = gemsEmpSalaryComponent.getGemsPayGrade().getGemsPayGradeId();
					selected_paygrade = "" + gemsEmpSalaryComponent.getGemsPayGrade().getPayGradeCode() + " - "
							+ gemsEmpSalaryComponent.getGemsPayGrade().getPayGradeDescription() + "";

				}
				return new JSONObject()
						.element("gemsEmpSalaryComponentId", gemsEmpSalaryComponent.getGemsEmpSalaryComponentId())
						.element("salaryComponent", gemsEmpSalaryComponent.getSalaryComponent())
						.element("payFrequency", gemsEmpSalaryComponent.getPayFrequency())
						.element("salaryComponentComment", gemsEmpSalaryComponent.getSalaryComponentComment())
						.element("salaryAmount", gemsEmpSalaryComponent.getSalaryAmount())
						.element("activeStatus", gemsEmpSalaryComponent.getActiveStatus())
						.element("selectedGemsPayGradeId", selectedGemsPayGradeId)
						.element("selected_paygrade", selected_paygrade)
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
