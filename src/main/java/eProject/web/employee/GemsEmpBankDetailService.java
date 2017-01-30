package eProject.web.employee;

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

import eProject.domain.employee.GemsEmpBankDetail;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;

@Controller
public class GemsEmpBankDetailService {
	@Autowired
	private EmployeeService employeeService;

	protected final Log logger = LogFactory.getLog(GemsEmpBankDetailService.class);

	@RequestMapping(value = "/employee/viewEmployeeBankDetailList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeBankDetailList(HttpServletRequest request) {

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

			GemsEmpBankDetail gemsEmpBankDetail = new GemsEmpBankDetail();

			String searchEmpCode = request.getParameter("gemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmpBankDetail
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getGemsEmpBankDetailFilterCount(gemsEmpBankDetail);
			List<GemsEmpBankDetail> list = employeeService.getGemsEmpBankDetailList(start, limit, gemsEmpBankDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeBankDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveEmployeeBankDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveEmployeeBankDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmpBankDetail gemsEmpBankDetail = new GemsEmpBankDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeMasterId")))) {
				gemsEmpBankDetail.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("gemsEmployeeMasterId"))));
			}
			gemsEmpBankDetail.setUpdatedBy(loggedInUser.getCreatedBy());
			gemsEmpBankDetail.setUpdatedOn(todayDate);
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmpBankDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmpBankDetailId")))) {
				id_value = request.getParameter("gemsEmpBankDetailId");
				gemsEmpBankDetail = employeeService.getGemsEmpBankDetail(Integer.parseInt(id_value));
			} else {
				gemsEmpBankDetail.setCreatedOn(todayDate);
				gemsEmpBankDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String bankAccountNumber = request.getParameter("bankAccountNumber");
			gemsEmpBankDetail.setBankAccountNumber(bankAccountNumber);

			String bankAccountRoutingNo = request.getParameter("bankAccountRoutingNo");
			gemsEmpBankDetail.setBankAccountRoutingNo(bankAccountRoutingNo);

			// String bankAccountType = request.getParameter("bankAccountType");
			gemsEmpBankDetail.setBankAccountType("Savings");

			String bankName = request.getParameter("bankName");
			gemsEmpBankDetail.setBankName(bankName);

			String isActive = request.getParameter("bankactiveStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmpBankDetail.setActiveStatus(1);
			} else {
				gemsEmpBankDetail.setActiveStatus(0);
			}

			employeeService.saveGemsEmpBankDetail(gemsEmpBankDetail);
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

	@RequestMapping(value = "/employee/getEmployeeBankDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeBankDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmpBankDetailIdString = request.getParameter("gemsEmpBankDetailId");

		GemsEmpBankDetail gemsEmpBankDetail = new GemsEmpBankDetail();
		try {
			if (gemsEmpBankDetailIdString != null) {
				Integer gemsEmpBankDetailId = new Integer(gemsEmpBankDetailIdString);
				gemsEmpBankDetail = employeeService.getGemsEmpBankDetail(gemsEmpBankDetailId);
			} else {
				return getModelMapError("Failed to Load Data");
			}

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}
		return getModelMapEmployeeBankDetail(gemsEmpBankDetail);

	}

	@RequestMapping(value = "/employee/deleteEmployeeBankDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteEmployeeBankDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmpBankDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmpBankDetail gemsEmpBankDetail = employeeService.getGemsEmpBankDetail(gemsEmpBankDetailId);
			employeeService.removeGemsEmpBankDetail(gemsEmpBankDetail);
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

	private Map<String, Object> getModelMapEmployeeBankDetail(GemsEmpBankDetail gemsEmpBankDetail) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmpBankDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmpBankDetail)) {
					return new JSONObject(true);
				}

				GemsEmpBankDetail gemsEmpBankDetail = (GemsEmpBankDetail) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmpBankDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmpBankDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId();

					if (gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}
				boolean bankactiveStatus = false;

				if (gemsEmpBankDetail.getActiveStatus() == 1) {
					bankactiveStatus = true;
				}

				return new JSONObject().element("gemsEmpBankDetailId", gemsEmpBankDetail.getGemsEmpBankDetailId())
						.element("bankName", gemsEmpBankDetail.getBankName())
						.element("bankAccountNumber", gemsEmpBankDetail.getBankAccountNumber())
						.element("bankAccountRoutingNo", gemsEmpBankDetail.getBankAccountRoutingNo())
						.element("bankAccountType", gemsEmpBankDetail.getBankAccountType())
						.element("bankactiveStatus", bankactiveStatus)
						.element("selectedGemsEmployeeMasterId",
								gemsEmpBankDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId())
						.element("selectedGemsEmployeeMasterName", selectedGemsEmployeeMasterName)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(gemsEmpBankDetail, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapEmployeeBankDetailList(List<GemsEmpBankDetail> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmpBankDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmpBankDetail)) {
					return new JSONObject(true);
				}

				GemsEmpBankDetail gemsEmpBankDetail = (GemsEmpBankDetail) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmpBankDetail.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmpBankDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId();

					if (gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmpBankDetail.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				boolean bankactiveStatus = false;

				if (gemsEmpBankDetail.getActiveStatus() == 1) {
					bankactiveStatus = true;
				}

				return new JSONObject().element("gemsEmpBankDetailId", gemsEmpBankDetail.getGemsEmpBankDetailId())
						.element("bankName", gemsEmpBankDetail.getBankName())
						.element("bankAccountNumber", gemsEmpBankDetail.getBankAccountNumber())
						.element("bankAccountRoutingNo", gemsEmpBankDetail.getBankAccountRoutingNo())
						.element("bankAccountType", gemsEmpBankDetail.getBankAccountType())
						.element("bankactiveStatus", bankactiveStatus)
						.element("selectedGemsEmployeeMasterId",
								gemsEmpBankDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId())
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
