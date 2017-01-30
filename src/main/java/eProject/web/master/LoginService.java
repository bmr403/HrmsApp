package eProject.web.master;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.master.GemsUserToken;
import eProject.service.employee.EmployeeService;
import eProject.service.mail.MailService;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;
import eProject.utility.HrKeyStoreUtility;
import eProject.web.employee.GemsEmployeeMasterService;

@Controller
public class LoginService {
	@Autowired
	private MasterService masterService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private MailService mailService;

	protected final Log logger = LogFactory.getLog(LoginService.class);

	/*
	 * Gems User Master
	 */

	@RequestMapping(value = "/master/changePassword", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> changePassword(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
			HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");
			String firstName = "";
			if (userEmployee != null) {
				firstName = "" + userEmployee.getEmployeeFirstName() + " " + userEmployee.getEmployeeLastName() + "";
			}
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String confirmPassword = request.getParameter("confirmPassword");

			String encryptedPasswordValue = hrKeyStoreUtility.getEncryptedStringValue(oldPassword, "E");
			if (loggedInUser.getUserPassword().equalsIgnoreCase(encryptedPasswordValue)) {
				if (newPassword.equalsIgnoreCase(confirmPassword)) {

					loggedInUser.setUserPassword(hrKeyStoreUtility.getEncryptedStringValue(newPassword, "E"));
					loggedInUser.setUserDecryptPassword(newPassword);
					loggedInUser.setUpdatedBy(loggedInUser.getGemsUserMasterId());
					loggedInUser.setUpdatedOn(todayDate);
					masterService.saveGemsUserMaster(loggedInUser);

					// Reading From EMail ID from Properties File
					Properties properties_mail = new Properties();
					String propFileName_mail = "/properties/mail.properties";
					InputStream stream_mail = getClass().getClassLoader().getResourceAsStream(propFileName_mail);
					properties_mail.load(stream_mail);
					String scheme = request.getScheme();
					String serverName = request.getServerName();
					int port = request.getServerPort();
					String contextpath = request.getContextPath();
					String appURL = scheme + "://" + serverName + ":" + port + contextpath;
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("from", properties_mail.getProperty("javaMailSender.username"));
					map.put("to", loggedInUser.getUserName());
					map.put("subject", "Your HRMS application password was reset");
					map.put("firstName", firstName);
					map.put("appURL", appURL);
					map.put("userEmail", loggedInUser.getUserName());
					// map.put("userPassword", user.getUserPassword()); //
					// sending password to mail with out encryption
					mailService.changePasswordEmail(map);

					modelMap.put("message", "Password Changed Successfully");
					modelMap.put("success", true);

				} else {
					modelMap.put("message", "New password and Confirm password does not match");
					modelMap.put("success", false);
				}
			} else {
				modelMap.put("message", "Current Password is wrong.");
				modelMap.put("success", false);
			}

			// masterService.saveGemsBusinessUnit(gemsBusinessUnit);
			logger.info("Insert Method Executed.,");

			return modelMap;
		} catch (Exception ex) {
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> loginValidate(HttpServletRequest request) {

		try {

			GemsUserMaster userMaster = new GemsUserMaster();
			HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
			String userName = "";
			String userPassword = "";

			if ((StringUtils.isNotBlank(request.getParameter("userEmail")))
					|| (StringUtils.isNotEmpty(request.getParameter("userEmail")))) {
				userName = request.getParameter("userEmail");
				userMaster.setUserName(request.getParameter("userEmail"));

			}
			if ((StringUtils.isNotBlank(request.getParameter("password")))
					|| (StringUtils.isNotEmpty(request.getParameter("password")))) {
				userPassword = request.getParameter("password");
				String encryptedPasswordValue = hrKeyStoreUtility.getEncryptedStringValue(userPassword, "E");
				userMaster.setUserPassword(encryptedPasswordValue);
			}

			String locale = request.getParameter("locale");

			if ((userMaster.getUserName() != null) && (userMaster.getUserPassword() != null)) {
				GemsUserMaster loggedInUser = masterService.getLoginVerificationByEmail(userMaster);
				if (loggedInUser == null) {
					return getModelMapError("Error trying to retrieve user.");
				} else {
					GemsActivityMaster gemsActivityMaster = new GemsActivityMaster();
					gemsActivityMaster.setGemsRoleMaster(loggedInUser.getGemsRoleMaster());

					GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
					gemsEmployeeMaster.setGemsUserMaster(loggedInUser);
					gemsEmployeeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

					Integer gemsEmployeeMasterId = new Integer(0);

					if ((loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE))
							|| (loggedInUser.getGemsRoleMaster().getRoleCode()
									.equalsIgnoreCase(ConstantVariables.HR))) {
						GemsEmployeeMaster userEmployee = employeeService
								.getGemsEmployeeMasterByUser(gemsEmployeeMaster);
						WebUtils.setSessionAttribute(request, "userEmployee", userEmployee);
						WebUtils.setSessionAttribute(request, "userName", "" + userEmployee.getEmployeeLastName() + "  "
								+ userEmployee.getEmployeeFirstName() + "");
						userName = "" + userEmployee.getEmployeeLastName() + "  " + userEmployee.getEmployeeFirstName()
								+ "";
						gemsEmployeeMasterId = userEmployee.getGemsEmployeeMasterId();
					} else {
						WebUtils.setSessionAttribute(request, "userName", loggedInUser.getUserName());
					}

					List<GemsActivityMaster> activities = masterService.getOdActivityListByRole(gemsActivityMaster);
					List<GemsComponentMaster> userComponentList = new ArrayList();
					List<GemsComponentMaster> userComponentListSecondLevel = new ArrayList();
					Iterator activitiesIterator = activities.iterator();
					while (activitiesIterator.hasNext()) {
						GemsActivityMaster activity = (GemsActivityMaster) activitiesIterator.next();
						if ((activity.getAddPermission() == 1)
								&& (activity.getGemsComponentMaster().getParentComponentId() == 0)) {
							if (userComponentList.contains(activity.getGemsComponentMaster())) {

							} else {
								userComponentList.add(activity.getGemsComponentMaster());
							}
						} else if ((activity.getAddPermission() == 1)
								&& (activity.getGemsComponentMaster().getParentComponentId() != 0)) {
							if (userComponentListSecondLevel.contains(activity.getGemsComponentMaster())) {

							} else {
								userComponentListSecondLevel.add(activity.getGemsComponentMaster());
							}
						}

					}
					List<String> componentNameStringList = new ArrayList();
					Collections.sort(userComponentList, GemsComponentMaster.ComponentMasterIdCompareObject);
					for (GemsComponentMaster gemsComponentMaster : userComponentList) {
						componentNameStringList.add(gemsComponentMaster.getComponentName());

					}

					// WebUtils.setSessionAttribute(request, "userComponents",
					// userComponentList);

					// WebUtils.setSessionAttribute(request, "userActivities",
					// userComponentListSecondLevel);

					// WebUtils.setSessionAttribute(request, "locale", locale);
					HttpSession session = request.getSession();
					
					String userToken = masterService.createEncodedToken(loggedInUser.getGemsUserMasterId());
					session.setAttribute("userToken", userToken);
					WebUtils.setSessionAttribute(request, "sessionuser", loggedInUser);
					WebUtils.setSessionAttribute(request, "userId", loggedInUser.getGemsUserMasterId());
					WebUtils.setSessionAttribute(request, "loggedInUser", loggedInUser);

					Map<String, Object> modelMap = new HashMap<String, Object>(2);
					modelMap.put("userRole", loggedInUser.getGemsRoleMaster().getRoleCode());
					// modelMap.put("userComponentListSecondLevel",
					// userComponentListSecondLevel);
					modelMap.put("permissions", componentNameStringList.toArray());
					modelMap.put("userName", userName);
					modelMap.put("userId", loggedInUser.getGemsUserMasterId());
					modelMap.put("userToken", userToken);
					modelMap.put("gemsEmployeeMasterId", gemsEmployeeMasterId);
					modelMap.put("userToken", userToken);
					String profileImgStr = "";
					if (loggedInUser.getProfileImgData() != null) {

						profileImgStr = new sun.misc.BASE64Encoder().encode(loggedInUser.getProfileImgData());

					}
					modelMap.put("profileImgData", profileImgStr);
					modelMap.put("success", true);

					// modelMap.put("loggedInUser", loggedInUser);
					// modelMap.put("message", "Product Saved");

					return modelMap;
					/*
					 * Map<String, Object> modelMap = new HashMap<String,
					 * Object>(3); modelMap.put("success", true);
					 * modelMap.put("message", "Saved Successfully"); return
					 * getModelMapUser(loggedInUser);
					 */

				}
			} else {
				return getModelMapError("Error trying to retrieve user.");
			}

		} catch (Exception e) {

			return getModelMapError("Error trying to retrieve user.");
		}
	}

	@RequestMapping(value = "/viewAllServices.action", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewUserList(HttpServletRequest request) {

		try {

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			GemsActivityMaster gemsActivityMaster = new GemsActivityMaster();
			gemsActivityMaster.setGemsRoleMaster(loggedInUser.getGemsRoleMaster());

			List<GemsActivityMaster> activities = masterService.getOdActivityListByRole(gemsActivityMaster);
			List<GemsComponentMaster> userComponentList = new ArrayList();
			List<GemsComponentMaster> userComponentListSecondLevel = new ArrayList();
			Iterator activitiesIterator = activities.iterator();
			while (activitiesIterator.hasNext()) {
				GemsActivityMaster activity = (GemsActivityMaster) activitiesIterator.next();
				userComponentList.add(activity.getGemsComponentMaster());
			}
			Collections.sort(userComponentList, GemsComponentMaster.ComponentMasterIdCompareObject);

			logger.info("Returned list size" + userComponentList.size());

			return getModelMapComponentList(userComponentList, userComponentList.size());

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapComponentList(List<GemsComponentMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsComponentMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsComponentMaster)) {
					return new JSONObject(true);
				}

				GemsComponentMaster gemsComponentMaster = (GemsComponentMaster) bean;

				return new JSONObject().element("gemsComponentMasterId", gemsComponentMaster.getGemsComponentMasterId())
						.element("componentDescription", gemsComponentMaster.getComponentDescription())
						.element("componentUrl", gemsComponentMaster.getComponentUrl())
						.element("parentComponentId", gemsComponentMaster.getParentComponentId());
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

	private ModelAndView getModelMap(GemsUserMaster loginUser) {

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

	@RequestMapping(value = "/user/logoutUser", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> logoutUser(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String userId = request.getParameter("userId");
		if (userId != null) {
			logger.info("User Id" + Integer.parseInt(userId));
		}

		try {
			GemsUserToken userToken = new GemsUserToken();

			String loggedInUserToken = request.getParameter("userToken");

			if (loggedInUserToken != null) {

				String decodedToken = userToken.getDecodedToken(loggedInUserToken);
				String[] tokenParams = decodedToken.split(":");
				userToken.setUserTokenId(Integer.parseInt(tokenParams[0]));
				masterService.removeGemsUserToken(userToken);

				logger.info("Delete Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Deleted Successfully");
				return modelMap;
			} else {

				modelMap.put("success", false);
				modelMap.put("message", "Error in deletion");
				return modelMap;

			}

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}

	}

	@RequestMapping(value = "/user/forgotPassword", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> forgotPassword(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {

			GemsUserMaster forgotPwdUser = new GemsUserMaster();
			String userEmail = request.getParameter("userEmail");

			if (userEmail != null && userEmail.isEmpty() == false) {

				forgotPwdUser.setUserName(userEmail);

			}

			GemsUserMaster user = masterService.getGemsUserMasterByCode(forgotPwdUser);

			String firstName = "";

			if (user != null) {
				GemsEmployeeMaster gemsEmployeeMaster = new GemsEmployeeMaster();
				gemsEmployeeMaster.setGemsUserMaster(user);
				gemsEmployeeMaster.setGemsOrganisation(user.getGemsOrganisation());
				GemsEmployeeMaster userEmployee = employeeService.getGemsEmployeeMasterByUser(gemsEmployeeMaster);

				if (userEmployee != null) {
					firstName = "" + userEmployee.getEmployeeFirstName() + " " + userEmployee.getEmployeeLastName()
							+ "";
				}

			}

			if (user.getGemsUserMasterId() != 0) {
				// logger.info("### gng to Forgot Pwd Encrypt Pwd is
				// :"+GemsEmployeeMasterService.base64EncryptPassword(user.getUserPassword()));
				// logger.info("### gng to Forgot Pwd Decrypt Pwd is
				// :"+GemsEmployeeMasterService.base64DecryptPassword(user.getUserPassword()));

				// Reading From EMail ID from Properties File
				HrKeyStoreUtility hrKeyStoreUtility = new HrKeyStoreUtility();
				Properties properties_mail = new Properties();
				String propFileName_mail = "/properties/mail.properties";
				InputStream stream_mail = getClass().getClassLoader().getResourceAsStream(propFileName_mail);
				properties_mail.load(stream_mail);

				String scheme = request.getScheme();
				String serverName = request.getServerName();
				int port = request.getServerPort();
				String contextpath = request.getContextPath();
				String appURL = scheme + "://" + serverName + ":" + port + contextpath;

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("from", properties_mail.getProperty("javaMailSender.username"));
				map.put("to", user.getUserName());
				map.put("subject", "Your HRMS application password was reset");
				map.put("firstName", firstName);
				map.put("userEmail", user.getUserName());
				map.put("appURL", appURL);
				String decryptedPasswordValue = hrKeyStoreUtility.getEncryptedStringValue(user.getUserPassword(), "D");
				map.put("userPassword", decryptedPasswordValue);
				// map.put("userPassword", user.getUserPassword()); // sending
				// password to mail with out encryption
				mailService.sendForgotPasswordEmail(map);

				modelMap.put("success", true);
				modelMap.put("message", "Forgot Password link sent to your registred email");
				return modelMap;

			} else {
				return getModelMapError("User not found");
			}
		} catch (Exception ex) {
			return getModelMapError("Login Failed");

		}

	}

	/*
	 * private Map<String, Object> getModelMapUser(GemsUserMaster user) {
	 * 
	 * Map<String, Object> modelMap = new HashMap<String, Object>(3); JsonConfig
	 * jsonConfig = new JsonConfig();
	 * jsonConfig.registerJsonBeanProcessor(GemsUserMaster.class, new
	 * JsonBeanProcessor() { public JSONObject processBean(Object bean,
	 * JsonConfig jsonConfig) { if (!(bean instanceof GemsUserMaster)) { return
	 * new JSONObject(true); }
	 * 
	 * GemsUserMaster gemsUserMaster = (GemsUserMaster) bean;
	 * 
	 * return new JSONObject()
	 * .element("userId",gemsUserMaster.getGemsUserMasterId())
	 * .element("userName", gemsUserMaster.getUserName()) ; } });
	 * 
	 * JSON json = JSONSerializer.toJSON(user, jsonConfig);
	 * 
	 * --- modelMap.put("data", json); modelMap.put("success", true);
	 * 
	 * return modelMap;
	 * 
	 * }
	 */

}
