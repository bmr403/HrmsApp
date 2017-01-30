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

import eProject.domain.employee.GemsEmpEducationDetail;
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeDependentDetail;
import eProject.domain.employee.GemsEmployeeEmergencyContact;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;

@Controller
public class GemsEmployeeEmergencyContactService {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsEmployeeEmergencyContactService.class);

	@RequestMapping(value = "/employee/viewEmployeeEmergencyContactList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewEmployeeEmergencyContactList(HttpServletRequest request) {

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

			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact = new GemsEmployeeEmergencyContact();

			String searchEmpCode = request.getParameter("selectedGemsEmployeeMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsEmployeeEmergencyContact
						.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = employeeService.getGemsEmployeeEmergencyContactFilterCount(gemsEmployeeEmergencyContact);
			List<GemsEmployeeEmergencyContact> list = employeeService.getGemsEmployeeEmergencyContactList(start, limit,
					gemsEmployeeEmergencyContact);

			logger.info("Returned list size" + list.size());

			return getModelMapEmployeeEmergencyContactList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/employee/saveGemsEmployeeEmergencyContact", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsEmployeeEmergencyContact(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact = new GemsEmployeeEmergencyContact();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsEmployeeMasterId")))) {
				gemsEmployeeEmergencyContact.setGemsEmployeeMaster(employeeService
						.getGemsEmployeeMaster(Integer.parseInt(request.getParameter("selectedGemsEmployeeMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeEmergencyContactId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeEmergencyContactId")))) {
				id_value = request.getParameter("gemsEmployeeEmergencyContactId");
				gemsEmployeeEmergencyContact = employeeService
						.getGemsEmployeeEmergencyContact(Integer.parseInt(id_value));
			} else {
				gemsEmployeeEmergencyContact.setCreatedOn(todayDate);
				gemsEmployeeEmergencyContact.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String contactHomePhone = request.getParameter("contactHomePhone");
			gemsEmployeeEmergencyContact.setContactHomePhone(contactHomePhone);

			String contactMobileNo = request.getParameter("contactMobileNo");
			gemsEmployeeEmergencyContact.setContactMobileNo(contactMobileNo);

			String contactName = request.getParameter("contactName");
			gemsEmployeeEmergencyContact.setContactName(contactName);

			String contactRelationShip = request.getParameter("contactRelationShip");
			gemsEmployeeEmergencyContact.setContactRelationShip(contactRelationShip);

			String contactWorkPhone = request.getParameter("contactWorkPhone");
			gemsEmployeeEmergencyContact.setContactWorkPhone(contactWorkPhone);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsEmployeeEmergencyContact.setActiveStatus(1);
			} else {
				gemsEmployeeEmergencyContact.setActiveStatus(0);
			}

			employeeService.saveGemsEmployeeEmergencyContact(gemsEmployeeEmergencyContact);
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

	@RequestMapping(value = "/employee/deleteGemsEmployeeEmergencyContact", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsEmployeeEmergencyContact(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmployeeEmergencyContactId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact = employeeService
					.getGemsEmployeeEmergencyContact(gemsEmployeeEmergencyContactId);
			employeeService.removeGemsEmployeeEmergencyContact(gemsEmployeeEmergencyContact);
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

	private Map<String, Object> getModelMapEmployeeEmergencyContactList(List<GemsEmployeeEmergencyContact> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeEmergencyContact.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeEmergencyContact)) {
					return new JSONObject(true);
				}

				GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact = (GemsEmployeeEmergencyContact) bean;

				int selectedGemsEmployeeMasterId = 0;
				String selectedGemsEmployeeMasterName = "";
				if (gemsEmployeeEmergencyContact.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsEmployeeEmergencyContact.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();

					if (gemsEmployeeEmergencyContact.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeEmergencyContact.getGemsEmployeeMaster().getEmployeeFirstName();
					}
					if (gemsEmployeeEmergencyContact.getGemsEmployeeMaster().getEmployeeLastName() != null) {
						selectedGemsEmployeeMasterName = selectedGemsEmployeeMasterName
								+ gemsEmployeeEmergencyContact.getGemsEmployeeMaster().getEmployeeLastName();
					}
				}

				return new JSONObject()
						.element("gemsEmployeeEmergencyContactId",
								gemsEmployeeEmergencyContact.getGemsEmployeeEmergencyContactId())
						.element("contactHomePhone", gemsEmployeeEmergencyContact.getContactHomePhone())
						.element("contactMobileNo", gemsEmployeeEmergencyContact.getContactMobileNo())
						.element("contactName", gemsEmployeeEmergencyContact.getContactName())
						.element("contactRelationShip", gemsEmployeeEmergencyContact.getContactRelationShip())
						.element("contactWorkPhone", gemsEmployeeEmergencyContact.getContactWorkPhone())
						.element("activeStatus", gemsEmployeeEmergencyContact.getActiveStatus())
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
