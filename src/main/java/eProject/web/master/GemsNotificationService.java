package eProject.web.master;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsNotification;
import eProject.domain.master.GemsNotificationAssignment;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsNotificationService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsNotificationService.class);

	@RequestMapping(value = "/master/viewNotificationListByDate", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewNotificationListByDate(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsNotificationAssignment gemsNotificationAssignment = new GemsNotificationAssignment();

			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsNotificationAssignment.setGemsEmployeeMaster(userEmployee);
			}

			gemsNotificationAssignment.setNotifiedOn(todayDate);
			gemsNotificationAssignment.setNotificationExpiry(todayDate);

			int totalResults = masterService
					.getGemsNotificationAssignmentByDateRangeFilterCount(gemsNotificationAssignment);
			List<GemsNotificationAssignment> list = masterService.getGemsNotificationAssignmentByDateRangeList(start,
					limit, gemsNotificationAssignment);

			logger.info("Returned list size" + list.size());

			return getModelMapNotificationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/viewNotificationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewNotificationList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsNotificationAssignment gemsNotificationAssignment = new GemsNotificationAssignment();

			if (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE)) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsNotificationAssignment.setGemsEmployeeMaster(userEmployee);
			}

			int totalResults = masterService.getGemsNotificationAssignmentFilterCount(gemsNotificationAssignment);
			List<GemsNotificationAssignment> list = masterService.getGemsNotificationAssignmentList(start, limit,
					gemsNotificationAssignment);

			logger.info("Returned list size" + list.size());

			return getModelMapNotificationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	private Map<String, Object> getModelMapNotificationList(List<GemsNotificationAssignment> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsNotificationAssignment.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsNotificationAssignment)) {
					return new JSONObject(true);
				}

				GemsNotificationAssignment gemsNotificationAssignment = (GemsNotificationAssignment) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String notificationExpiry = "";
				if (gemsNotificationAssignment.getNotificationExpiry() != null) {
					notificationExpiry = sdf.format(gemsNotificationAssignment.getNotificationExpiry());
				}

				String notifiedOn = "";
				if (gemsNotificationAssignment.getNotifiedOn() != null) {
					notifiedOn = sdf.format(gemsNotificationAssignment.getNotifiedOn());
				}

				String notificationDescription = "";
				if (gemsNotificationAssignment.getGemsNotification() != null) {
					notificationDescription = gemsNotificationAssignment.getGemsNotification()
							.getNotificationDescription();
				}

				String transactionDescription = "";
				if (gemsNotificationAssignment.getGemsNotification().getGemsAlertNotificationMaster() != null) {
					transactionDescription = gemsNotificationAssignment.getGemsNotification()
							.getGemsAlertNotificationMaster().getTransactionDescription();
				}

				return new JSONObject()
						.element("notificationAssignmentId", gemsNotificationAssignment.getNotificationAssignmentId())
						.element("notificationExpiry", notificationExpiry).element("notifiedOn", notifiedOn)
						.element("notificationDescription", notificationDescription)
						.element("transactionDescription", transactionDescription);
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
