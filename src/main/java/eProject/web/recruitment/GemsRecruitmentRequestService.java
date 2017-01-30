package eProject.web.recruitment;

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

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.recruitment.GemsRecruitmentRequest;
import eProject.domain.recruitment.GemsRecruitmentRequestLine;
import eProject.service.customer.CustomerService;
import eProject.service.recruitment.RecruitementService;
import eProject.utility.ConstantVariables;

@Controller
public class GemsRecruitmentRequestService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private CustomerService customerService;

	protected final Log logger = LogFactory.getLog(GemsRecruitmentRequestService.class);

	@RequestMapping(value = "/recruitment/viewRecruitmentRequestList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewRecruitmentRequestList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsRecruitmentRequest gemsRecruitmentRequest = new GemsRecruitmentRequest();

			String searchRequestCode = request.getParameter("searchRequestCode");
			if (searchRequestCode != null && searchRequestCode.isEmpty() == false) {
				gemsRecruitmentRequest.setRequestCode(searchRequestCode);
			}

			String searchRequestFromDate = request.getParameter("requestFromDate");
			String searchRequestToDate = request.getParameter("requestToDate");

			int totalResults = recruitementService.getGemsRecruitmentRequestFilterCount(gemsRecruitmentRequest);
			List<GemsRecruitmentRequest> list = recruitementService.getGemsRecruitmentRequestList(start, limit,
					gemsRecruitmentRequest);

			logger.info("Returned list size" + list.size());

			return getModelMapRecruitmentRequestList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/viewRecruitmentRequestLineList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewRecruitmentRequestLineList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsRecruitmentRequestLine gemsRecruitmentRequestLine = new GemsRecruitmentRequestLine();

			String selectedGemsRecruitmentRequestId = request.getParameter("selectedGemsRecruitmentRequestId");
			gemsRecruitmentRequestLine.setGemsRecruitmentRequest(
					recruitementService.getGemsRecruitmentRequest(Integer.parseInt(selectedGemsRecruitmentRequestId)));

			int totalResults = recruitementService
					.getGemsGemsRecruitmentRequestLineFilterCount(gemsRecruitmentRequestLine);
			List<GemsRecruitmentRequestLine> list = recruitementService.getGemsRecruitmentRequestLineList(start, limit,
					gemsRecruitmentRequestLine);

			logger.info("Returned list size" + list.size());

			return getModelMapRecruitmentRequestLineList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveRecruitmentRequest", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveRecruitmentRequest(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsRecruitmentRequest gemsRecruitmentRequest = new GemsRecruitmentRequest();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			gemsRecruitmentRequest.setUpdatedBy(loggedInUser.getCreatedBy());
			gemsRecruitmentRequest.setUpdatedOn(todayDate);
			String requestOldStatus = "";
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsRecruitmentRequestId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsRecruitmentRequestId")))) {
				id_value = request.getParameter("gemsRecruitmentRequestId");
				gemsRecruitmentRequest = recruitementService.getGemsRecruitmentRequest(Integer.parseInt(id_value));
				requestOldStatus = gemsRecruitmentRequest.getCurrentStatus();
			} else {
				gemsRecruitmentRequest.setCreatedOn(todayDate);
				gemsRecruitmentRequest.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String currentStatus = request.getParameter("currentStatus");
			gemsRecruitmentRequest.setCurrentStatus(currentStatus);

			String jobProfileFileName = request.getParameter("jobProfileFileName");
			gemsRecruitmentRequest.setJobProfileFileName(jobProfileFileName);

			String requestCode = request.getParameter("requestCode");
			gemsRecruitmentRequest.setRequestCode(requestCode);

			String requestDescription = request.getParameter("requestDescription");
			gemsRecruitmentRequest.setRequestDescription(requestDescription);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsRecruitmentRequest.setActiveStatus(1);
			} else {
				gemsRecruitmentRequest.setActiveStatus(0);
			}
			int gemsCustomerMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_customer")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_customer")))) {
				try {
					gemsCustomerMasterId = Integer.parseInt(request.getParameter("selected_customer"));

					gemsRecruitmentRequest
							.setGemsCustomerMaster(customerService.getGemsCustomerMaster(gemsCustomerMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsRecruitmentRequest.setGemsCustomerMaster(gemsRecruitmentRequest.getGemsCustomerMaster());
				}
			}
			gemsRecruitmentRequest.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			recruitementService.saveGemsRecruitmentRequest(gemsRecruitmentRequest);
			if (requestOldStatus.equalsIgnoreCase(gemsRecruitmentRequest.getCurrentStatus())) {
				// Code to reviewed
			} else {
				/*
				 * In case of status change
				 */
				GemsRecruitmentRequestLine gemsRecruitmentRequestLine = new GemsRecruitmentRequestLine();
				gemsRecruitmentRequestLine.setActiveStatus(1);
				gemsRecruitmentRequestLine.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsRecruitmentRequestLine.setCreatedOn(todayDate);
				gemsRecruitmentRequestLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
				gemsRecruitmentRequestLine.setUpdatedOn(todayDate);
				gemsRecruitmentRequestLine.setGemsRecruitmentRequest(gemsRecruitmentRequest);
				gemsRecruitmentRequestLine.setLineStatus(ConstantVariables.PUBLISHED);

				if ((loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE))
						|| (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.HR))) {
					GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
							"userEmployee");
					gemsRecruitmentRequestLine.setLastUpdatedBy(
							"" + userEmployee.getEmployeeLastName() + "  " + userEmployee.getEmployeeFirstName() + "");
				} else {
					gemsRecruitmentRequestLine.setLastUpdatedBy(loggedInUser.getUserName());
				}

				recruitementService.saveGemsRecruitmentRequestLine(gemsRecruitmentRequestLine);
			}

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

	@RequestMapping(value = "/recruitment/deleteRecruitmentRequest", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteRecruitmentRequest(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsRecruitmentRequestId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsRecruitmentRequest gemsRecruitmentRequest = recruitementService
					.getGemsRecruitmentRequest(gemsRecruitmentRequestId);
			recruitementService.removeGemsRecruitmentRequest(gemsRecruitmentRequest);
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

	@RequestMapping(value = "/recruitment/publishRecruitmentRequest", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> publishRecruitmentRequest(HttpServletRequest request) {

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsRecruitmentRequestId = Integer.parseInt(request.getParameter("objectId"));
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsRecruitmentRequest gemsRecruitmentRequest = recruitementService
					.getGemsRecruitmentRequest(gemsRecruitmentRequestId);

			/*
			 * save Request line
			 */
			GemsRecruitmentRequestLine gemsRecruitmentRequestLine = new GemsRecruitmentRequestLine();
			gemsRecruitmentRequestLine.setActiveStatus(1);
			gemsRecruitmentRequestLine.setCreatedBy(loggedInUser.getGemsUserMasterId());
			gemsRecruitmentRequestLine.setCreatedOn(todayDate);
			gemsRecruitmentRequestLine.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsRecruitmentRequestLine.setUpdatedOn(todayDate);
			gemsRecruitmentRequestLine.setGemsRecruitmentRequest(gemsRecruitmentRequest);
			gemsRecruitmentRequestLine.setLineStatus(ConstantVariables.PUBLISHED);

			if ((loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.EMPLOYEE))
					|| (loggedInUser.getGemsRoleMaster().getRoleCode().equalsIgnoreCase(ConstantVariables.HR))) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsRecruitmentRequestLine.setLastUpdatedBy(
						"" + userEmployee.getEmployeeLastName() + "  " + userEmployee.getEmployeeFirstName() + "");
			} else {
				gemsRecruitmentRequestLine.setLastUpdatedBy(loggedInUser.getUserName());
			}

			recruitementService.saveGemsRecruitmentRequestLine(gemsRecruitmentRequestLine);
			gemsRecruitmentRequest.setCurrentStatus(ConstantVariables.PUBLISHED);
			recruitementService.saveGemsRecruitmentRequest(gemsRecruitmentRequest);

			logger.info("Publish Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Published Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in Publish");
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapRecruitmentRequestList(List<GemsRecruitmentRequest> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRecruitmentRequest.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRecruitmentRequest)) {
					return new JSONObject(true);
				}

				GemsRecruitmentRequest gemsRecruitmentRequest = (GemsRecruitmentRequest) bean;

				int selectedGemsCustomerMasterId = 0;
				String selectedCustomerName = "";
				if (gemsRecruitmentRequest.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsRecruitmentRequest.getGemsCustomerMaster()
							.getGemsCustomerMasterId();

					if (gemsRecruitmentRequest.getGemsCustomerMaster().getGemsCustomerName() != null) {
						selectedCustomerName = "" + gemsRecruitmentRequest.getGemsCustomerMaster().getGemsCustomerCode()
								+ " - " + gemsRecruitmentRequest.getGemsCustomerMaster().getGemsCustomerName() + "";

					} else {
						selectedCustomerName = gemsRecruitmentRequest.getGemsCustomerMaster().getGemsCustomerCode();
					}

				}
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				String requestInitiatedDate = "";
				if (gemsRecruitmentRequest.getCreatedOn() != null) {
					requestInitiatedDate = sdf.format(gemsRecruitmentRequest.getCreatedOn());
				}

				return new JSONObject()
						.element("gemsRecruitmentRequestId", gemsRecruitmentRequest.getGemsRecruitmentRequestId())
						.element("requestCode", gemsRecruitmentRequest.getRequestCode())
						.element("requestDescription", gemsRecruitmentRequest.getRequestDescription())
						.element("requestInitiatedDate", requestInitiatedDate)
						.element("activeStatus", gemsRecruitmentRequest.getActiveStatus())
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selectedCustomerName", selectedCustomerName)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapRecruitmentRequestLineList(List<GemsRecruitmentRequestLine> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRecruitmentRequestLine.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRecruitmentRequestLine)) {
					return new JSONObject(true);
				}

				GemsRecruitmentRequestLine gemsRecruitmentRequestLine = (GemsRecruitmentRequestLine) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				String requestDate = "";
				if (gemsRecruitmentRequestLine.getUpdatedOn() != null) {
					requestDate = sdf.format(gemsRecruitmentRequestLine.getUpdatedOn());
				}

				return new JSONObject()
						.element("gemsRecruitmentRequestLineId",
								gemsRecruitmentRequestLine.getGemsRecruitmentRequestLineId())
						.element("lineStatus", gemsRecruitmentRequestLine.getLineStatus())
						.element("lastUpdatedBy", gemsRecruitmentRequestLine.getLastUpdatedBy())
						.element("requestDate", requestDate);
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
