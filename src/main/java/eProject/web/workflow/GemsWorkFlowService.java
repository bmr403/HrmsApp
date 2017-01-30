package eProject.web.workflow;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import eProject.domain.master.GemsUserMaster;
import eProject.domain.workflow.GemsWorkFlowHeader;
import eProject.service.workflow.WorkflowService;

@Controller
public class GemsWorkFlowService {
	@Autowired
	private WorkflowService workflowService;
	
	protected final Log logger = LogFactory.getLog(WorkflowService.class);

	@RequestMapping(value = "/workflow/checkWorkFlowHeaderByTransactionTypeCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkWorkFlowHeaderByTransactionTypeCode(HttpServletRequest request) {

		String trxTypeCode = request.getParameter("trxTypeCode");

		GemsWorkFlowHeader gemsWorkFlowHeader = new GemsWorkFlowHeader();
		gemsWorkFlowHeader.setTransactionTypeCode(trxTypeCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsWorkFlowHeader.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsWorkFlowHeader returnedGemsWorkFlowHeader = workflowService.getGemsWorkFlowHeaderByTransactionTypeCode(gemsWorkFlowHeader);
		
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsWorkFlowHeader.getTransactionTypeCode()))
				|| (StringUtils.isNotEmpty(returnedGemsWorkFlowHeader.getTransactionTypeCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/workflow/viewGemsWorkFlowHeaderList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsWorkFlowHeaderList(HttpServletRequest request) {

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

			GemsWorkFlowHeader gemsWorkFlowHeader = new GemsWorkFlowHeader();
			gemsWorkFlowHeader.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			gemsWorkFlowHeader.setActiveStatus(1);

			int totalResults = workflowService.getGemsWorkFlowHeaderFilterCount(gemsWorkFlowHeader);
			List<GemsWorkFlowHeader> list = workflowService.getGemsWorkFlowHeaderList(start, limit, gemsWorkFlowHeader);

			logger.info("Returned list size" + list.size());

			return getModelMapGemsWorkFlowHeaderList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/workflow/saveGemsWorkFlowHeader", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsWorkFlowHeader(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsWorkFlowHeader gemsWorkFlowHeader = new GemsWorkFlowHeader();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsWorkFlowHeader.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsWorkFlowHeader.setUpdatedOn(todayDate);
			gemsWorkFlowHeader.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsWorkFlowHeaderId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsWorkFlowHeaderId")))) {
				id_value = request.getParameter("gemsWorkFlowHeaderId");
				gemsWorkFlowHeader = workflowService.getGemsWorkFlowHeader(Integer.parseInt(id_value));
			} else {
				gemsWorkFlowHeader.setCreatedOn(todayDate);
				gemsWorkFlowHeader.setCreatedBy(loggedInUser.getCreatedBy());
			}
			
			String transactionTypeCode = request.getParameter("transactionTypeCode");
			gemsWorkFlowHeader.setTransactionTypeCode(transactionTypeCode);
			
			if ((StringUtils.isNotBlank(request.getParameter("nodeCount")))
					|| (StringUtils.isNotEmpty(request.getParameter("nodeCount")))) {
				gemsWorkFlowHeader.setNodeCount(new Integer(request.getParameter("nodeCount")));
			}
			
			String workFlowType = request.getParameter("workFlowType");
			gemsWorkFlowHeader.setWorkFlowType(workFlowType);
			
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsWorkFlowHeader.setActiveStatus(1);
			} else {
				gemsWorkFlowHeader.setActiveStatus(0);
			}
			workflowService.saveGemsWorkFlowHeader(gemsWorkFlowHeader);
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

	@RequestMapping(value = "/workflow/deleteGemsWorkFlowHeader", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsWorkFlowHeader(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		
		try {
			Integer gemsWorkFlowHeaderId = new Integer(request.getParameter("objectId"));
			GemsWorkFlowHeader gemsWorkFlowHeader = workflowService.getGemsWorkFlowHeader(gemsWorkFlowHeaderId);
			workflowService.removeGemsWorkFlowHeader(gemsWorkFlowHeader);
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

	@RequestMapping(value = "/workflow/getWorkFlowHeaderById", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWorkFlowHeaderById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsWorkFlowHeaderId_Str = request.getParameter("gemsWorkFlowHeaderId");

		GemsWorkFlowHeader gemsWorkFlowHeader = new GemsWorkFlowHeader();
		try {
			if (gemsWorkFlowHeaderId_Str != null) {

				gemsWorkFlowHeader = workflowService.getGemsWorkFlowHeader(new Integer(gemsWorkFlowHeaderId_Str));

			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapWorkFlowHeaderObj(gemsWorkFlowHeader);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapGemsWorkFlowHeaderList(List<GemsWorkFlowHeader> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsWorkFlowHeader.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsWorkFlowHeader)) {
					return new JSONObject(true);
				}

				GemsWorkFlowHeader gemsWorkFlowHeader = (GemsWorkFlowHeader) bean;

				String gemsWorkFlowHeaderActiveStatusString = "In-Active";

				if (gemsWorkFlowHeader.getActiveStatus() == 1) {
					gemsWorkFlowHeaderActiveStatusString = "Active";
				}

				return new JSONObject().element("gemsWorkFlowHeaderId", gemsWorkFlowHeader.getGemsWorkFlowHeaderId())
						.element("transactionTypeCode", gemsWorkFlowHeader.getTransactionTypeCode())
						.element("nodeCount", gemsWorkFlowHeader.getNodeCount())
						.element("workFlowType",gemsWorkFlowHeader.getWorkFlowType())
						.element("activeStatus", gemsWorkFlowHeader.getActiveStatus())
						.element("gemsWorkFlowHeaderActiveStatusString", gemsWorkFlowHeaderActiveStatusString);
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

	private Map<String, Object> getModelMapWorkFlowHeaderObj(GemsWorkFlowHeader gemsWorkFlowHeader) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsWorkFlowHeader.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsWorkFlowHeader)) {
					return new JSONObject(true);
				}
				GemsWorkFlowHeader gemsWorkFlowHeader = (GemsWorkFlowHeader) bean;

				boolean activeStatus = false;

				String gemsWorkFlowHeaderActiveStatusString = "In-Active";

				if (gemsWorkFlowHeader.getActiveStatus() == 1) {
					gemsWorkFlowHeaderActiveStatusString = "Active";
				}

				return new JSONObject().element("gemsWorkFlowHeaderId", gemsWorkFlowHeader.getGemsWorkFlowHeaderId())
						.element("transactionTypeCode", gemsWorkFlowHeader.getTransactionTypeCode())
						.element("nodeCount", gemsWorkFlowHeader.getNodeCount())
						.element("workFlowType",gemsWorkFlowHeader.getWorkFlowType())
						.element("activeStatus", gemsWorkFlowHeader.getActiveStatus())
						.element("gemsWorkFlowHeaderActiveStatusString", gemsWorkFlowHeaderActiveStatusString);

			}
		});

		JSON json = JSONSerializer.toJSON(gemsWorkFlowHeader, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}
}
