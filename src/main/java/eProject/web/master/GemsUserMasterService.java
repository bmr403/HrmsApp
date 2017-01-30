package eProject.web.master;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.utility.HrKeyStoreUtility;
import eProject.web.employee.GemsEmployeeMasterService;

@Controller
public class GemsUserMasterService {
	@Autowired
	private MasterService masterService;

	@Autowired
	private EmployeeService employeeService;

	protected final Log logger = LogFactory.getLog(GemsUserMasterService.class);

	@RequestMapping(value = "/master/checkUserByUserName", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkUserByUserName(HttpServletRequest request) {

		String userName = request.getParameter("userName");

		GemsUserMaster gemsUserMaster = new GemsUserMaster();
		gemsUserMaster.setUserName(userName);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsUserMaster returnedGemsUserMaster = masterService.getGemsUserMasterByCode(gemsUserMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsUserMaster.getUserName()))
				|| (StringUtils.isNotEmpty(returnedGemsUserMaster.getUserName()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewUserList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewUserList(HttpServletRequest request) {

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

			GemsUserMaster gemsUserMaster = new GemsUserMaster();

			String searchCode = request.getParameter("searchUserName");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsUserMaster.setUserName(searchCode);
			}

			String searchRole = request.getParameter("searchRole");
			if (searchRole != null && searchRole.isEmpty() == false) {
				GemsRoleMaster gemsRoleMaster = masterService.getGemsRoleMaster(Integer.parseInt(searchRole));
				gemsUserMaster.setGemsRoleMaster(gemsRoleMaster);
			}
			gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsUserMasterFilterCount(gemsUserMaster);
			List<GemsUserMaster> list = masterService.getGemsUserMasterList(start, limit, gemsUserMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapUserList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveUser(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsUserMaster gemsUserMaster = new GemsUserMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsUserMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsUserMaster.setUpdatedOn(todayDate);
			gemsUserMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsUserMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsUserMasterId")))) {
				id_value = request.getParameter("gemsUserMasterId");
				gemsUserMaster = masterService.getGemsUserMaster(Integer.parseInt(id_value));
			} else {
				gemsUserMaster.setCreatedOn(todayDate);
				gemsUserMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String userName = request.getParameter("userName");
			gemsUserMaster.setUserName(userName);
			String userPassword = request.getParameter("userPassword");
			gemsUserMaster.setUserPassword(userPassword);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsUserMaster.setActiveStatus(1);
			} else {
				gemsUserMaster.setActiveStatus(0);
			}
			int roleId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_role")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_role")))) {
				try {
					roleId = Integer.parseInt(request.getParameter("selected_role"));

					gemsUserMaster.setGemsRoleMaster(masterService.getGemsRoleMaster(roleId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsUserMaster.setGemsRoleMaster(gemsUserMaster.getGemsRoleMaster());
				}
			}
			masterService.saveGemsUserMaster(gemsUserMaster);
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

	@RequestMapping(value = "/master/deleteUser", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteUser(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsUserMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsUserMaster gemsUserMaster = masterService.getGemsUserMaster(gemsUserMasterId);
			masterService.removeGemsUserMaster(gemsUserMaster);
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

	@RequestMapping(value = "/master/getMyProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> userId(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		try {
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			return getModelMapUser(userEmployee);

		} catch (Exception e) {
			e.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;

		}
	}

	private Map<String, Object> getModelMapUser(GemsEmployeeMaster userEmployee) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeMaster)) {
					return new JSONObject(true);
				}
				GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) bean;

				return new JSONObject()
						.element("gemsUserMasterId", gemsEmployeeMaster.getGemsUserMaster().getGemsUserMasterId())
						.element("gemsEmployeeMasterId", gemsEmployeeMaster.getGemsEmployeeMasterId())
						.element("employeeFirstName", gemsEmployeeMaster.getEmployeeFirstName())
						.element("employeeLastName", gemsEmployeeMaster.getEmployeeLastName())
						.element("officialEmailid", gemsEmployeeMaster.getOfficialEmailid())
						.element("personalEmailId", gemsEmployeeMaster.getPersonalEmailId())
						.element("personalContactNumber", gemsEmployeeMaster.getPersonalContactNumber())
						.element("officeContactNumber", gemsEmployeeMaster.getOfficeContactNumber());

			}
		});

		JSON json = JSONSerializer.toJSON(userEmployee, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapUserList(List<GemsUserMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsUserMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsUserMaster)) {
					return new JSONObject(true);
				}

				GemsUserMaster gemsUserMaster = (GemsUserMaster) bean;
				int selectedRoleId = 0;
				String selected_role = "";
				if (gemsUserMaster.getGemsRoleMaster() != null) {
					selectedRoleId = gemsUserMaster.getGemsRoleMaster().getGemsRoleMasterId();
					selected_role = gemsUserMaster.getGemsRoleMaster().getRoleName();
				}
				return new JSONObject().element("gemsUserMasterId", gemsUserMaster.getGemsUserMasterId())
						.element("userName", gemsUserMaster.getUserName())
						.element("userPassword", gemsUserMaster.getUserPassword())
						.element("selectedRoleId", selectedRoleId).element("selected_role", selected_role)
						.element("activeStatus", gemsUserMaster.getActiveStatus());
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

	@RequestMapping(value = "/master/saveUserProfile", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveUser(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
			HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			if (StringUtils.isNotBlank(request.getParameter("employeeFirstName"))) {
				userEmployee.setEmployeeFirstName(request.getParameter("employeeFirstName"));
			}

			if (StringUtils.isNotBlank(request.getParameter("employeeLastName"))) {
				userEmployee.setEmployeeLastName(request.getParameter("employeeLastName"));
			}

			if (StringUtils.isNotBlank(request.getParameter("personalEmailId"))) {
				userEmployee.setPersonalEmailId(request.getParameter("personalEmailId"));
			}
			if (StringUtils.isNotBlank(request.getParameter("personalContactNumber"))) {
				userEmployee.setPersonalContactNumber(request.getParameter("personalContactNumber"));
			}

			if (StringUtils.isNotBlank(request.getParameter("userPassword"))) {
				String userPassword = request.getParameter("userPassword");
				loggedInUser.setUserPassword(hrKeyStoreUtility.getEncryptedStringValue(userPassword, "E"));
				loggedInUser.setUserDecryptPassword(userPassword);
			}

			// save profile image.
			if (file.getSize() > 0) {
				loggedInUser.setProfileImgData(file.getBytes());
			}
			employeeService.saveGemsEmployeeMaster(userEmployee);
			masterService.saveGemsUserMaster(loggedInUser);

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

}
