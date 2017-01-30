package eProject.web.project;

import java.math.BigDecimal;
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

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.service.customer.CustomerService;
import eProject.service.master.MasterService;
import eProject.service.project.ProjectService;
import eProject.service.timesheet.TimeSheetServiceImpl;

@Controller
public class GemsProjectMasterService {
	@Autowired
	private ProjectService projectService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private TimeSheetServiceImpl timeSheetService;

	protected final Log logger = LogFactory.getLog(GemsProjectMasterService.class);

	@RequestMapping(value = "/project/checkProjectMasterByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkProjectMasterByCode(HttpServletRequest request) {

		String projectCode = request.getParameter("projectCode");

		GemsProjectMaster gemsProjectMaster = new GemsProjectMaster();
		gemsProjectMaster.setProjectCode(projectCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsProjectMaster returnedGemsProjectMaster = projectService.getGemsProjectMasterByCode(gemsProjectMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsProjectMaster.getProjectCode()))
				|| (StringUtils.isNotEmpty(returnedGemsProjectMaster.getProjectCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/project/viewProjectList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewProjectList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 10;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			boolean showAllProjectsFlag = false;
			String showAllProjects = request.getParameter("showAllProjects");
			if (showAllProjects != null && showAllProjects.isEmpty() == false) {
				showAllProjectsFlag = Boolean.valueOf(showAllProjects);
			}
			
			String startValue = request.getParameter("iDisplayStart");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("iDisplayStart"));
			}
			String limitValue = request.getParameter("iDisplayLength");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("iDisplayLength"));
			}

			GemsProjectMaster gemsProjectMaster = new GemsProjectMaster();

			String searchProjectCode = request.getParameter("searchProjectCode");
			if (searchProjectCode != null && searchProjectCode.isEmpty() == false) {
				gemsProjectMaster.setProjectCode(searchProjectCode);
			}

			String searchProjectName = request.getParameter("searchProjectName");
			if (searchProjectName != null && searchProjectName.isEmpty() == false) {
				gemsProjectMaster.setProjectName(searchProjectName);
			}

			if ((StringUtils.isNotBlank(request.getParameter("projectTypeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("projectTypeMasterId")))) {

				GemsProjectTypeMaster gemsProjectTypeMaster = projectService
						.getGemsProjectTypeMaster(new Integer(request.getParameter("projectTypeMasterId")));
				gemsProjectMaster.setProjectTypeMaster(gemsProjectTypeMaster);
			}
			if ((StringUtils.isNotBlank(request.getParameter("billingType")))
					|| (StringUtils.isNotEmpty(request.getParameter("billingType")))) {
				gemsProjectMaster.setBillingType(request.getParameter("billingType"));
			}
			if ((StringUtils.isNotBlank(request.getParameter("projectStatus")))
					|| (StringUtils.isNotEmpty(request.getParameter("projectStatus")))) {
				gemsProjectMaster.setProjectStatus(request.getParameter("projectStatus"));
			}
			if ((StringUtils.isNotBlank(request.getParameter("customerId")))
					|| (StringUtils.isNotEmpty(request.getParameter("customerId")))) {
				GemsCustomerMaster searchGemsCustomerMaster = customerService
						.getGemsCustomerMaster(new Integer(request.getParameter("customerId")));
				gemsProjectMaster.setGemsCustomerMaster(searchGemsCustomerMaster);
			}
			if ((StringUtils.isNotBlank(request.getParameter("searchProjectActive")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchProjectActive")))) {
				String isActive = request.getParameter("searchProjectActive");
				if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
					gemsProjectMaster.setActiveStatus(new Integer(1));
				} else {
					gemsProjectMaster.setActiveStatus(new Integer(0));

				}

			} else {
				gemsProjectMaster.setActiveStatus(new Integer(1)); // By Default it will show
														// only Active employees
			}

			gemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			
			int totalResults = 0;
			List<GemsProjectMaster> list = new ArrayList<GemsProjectMaster>();
			
			if (showAllProjectsFlag == true)
			{
				list = projectService.getAllGemsProjectMasterList(gemsProjectMaster);
				totalResults = list.size();
			}
			else
			{
				totalResults = projectService.getGemsProjectMasterFilterCount(gemsProjectMaster);
				list = projectService.getGemsProjectMasterList(start, limit, gemsProjectMaster);
			}
			
			

			logger.info("Returned list size" + list.size());

			return getModelMapProjectMasterList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/project/saveGemsProjectMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsProjectMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsProjectMaster gemsProjectMaster = new GemsProjectMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsProjectMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsProjectMaster.setUpdatedOn(todayDate);
			gemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsProjectMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsProjectMasterId")))) {
				id_value = request.getParameter("gemsProjectMasterId");
				gemsProjectMaster = projectService.getGemsProjectMaster(Integer.parseInt(id_value));
			} else {
				gemsProjectMaster.setCreatedOn(todayDate);
				gemsProjectMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String projectCode = request.getParameter("projectCode");
			gemsProjectMaster.setProjectCode(projectCode);
			String projectName = request.getParameter("projectName");
			gemsProjectMaster.setProjectName(projectName);

			String projectDescription = request.getParameter("projectDescription");
			gemsProjectMaster.setProjectDescription(projectDescription);
			String projectStatus = request.getParameter("projectStatus");
			gemsProjectMaster.setProjectStatus(projectStatus);

			String billingType = request.getParameter("billingType");
			gemsProjectMaster.setBillingType(billingType);

			String projectStartDateString = request.getParameter("projectStartDate");
			if ((StringUtils.isNotBlank(projectStartDateString)) || (StringUtils.isNotEmpty(projectStartDateString))) {
				Date projectStartDate = formatter.parse(projectStartDateString);
				gemsProjectMaster.setProjectStartDate(projectStartDate);
			}

			String projectEndDateString = request.getParameter("projectEndDate");
			if ((StringUtils.isNotBlank(projectEndDateString)) || (StringUtils.isNotEmpty(projectEndDateString))) {
				Date projectEndDate = formatter.parse(projectEndDateString);
				gemsProjectMaster.setProjectEndDate(projectEndDate);
			}

			if ((StringUtils.isNotBlank(request.getParameter("projectCost")))
					|| (StringUtils.isNotEmpty(request.getParameter("projectCost")))) {
				gemsProjectMaster.setProjectCost(new BigDecimal(request.getParameter("projectCost")));
			}

			/*
			 * int gemsBusinessUnitId = 0;
			 * 
			 * if ((StringUtils.isNotBlank(request.getParameter("selected_bu")))
			 * || (StringUtils.isNotEmpty(request.getParameter("selected_bu"))))
			 * { try { gemsBusinessUnitId =
			 * Integer.parseInt(request.getParameter("selected_bu"));
			 * 
			 * gemsProjectMaster.setGemsBusinessUnit(masterService.
			 * getGemsBusinessUnit(gemsBusinessUnitId)); }
			 * catch(NumberFormatException ex) { // this will be called when the
			 * drop down value does not changed
			 * gemsProjectMaster.setGemsBusinessUnit(gemsProjectMaster.
			 * getGemsBusinessUnit()); } }
			 */

			int projectTypeMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_projecttype")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_projecttype")))) {
				try {
					projectTypeMasterId = Integer.parseInt(request.getParameter("selected_projecttype"));

					gemsProjectMaster
							.setProjectTypeMaster(projectService.getGemsProjectTypeMaster(projectTypeMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsProjectMaster.setProjectTypeMaster(gemsProjectMaster.getProjectTypeMaster());
				}
			}

			int gemsCustomerMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_customer")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_customer")))) {
				try {
					gemsCustomerMasterId = Integer.parseInt(request.getParameter("selected_customer"));

					gemsProjectMaster
							.setGemsCustomerMaster(customerService.getGemsCustomerMaster(gemsCustomerMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsProjectMaster.setGemsCustomerMaster(gemsProjectMaster.getGemsCustomerMaster());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsProjectMaster.setActiveStatus(new Integer(1));
			} else {
				gemsProjectMaster.setActiveStatus(new Integer(0));
			}

			projectService.saveGemsProjectMaster(gemsProjectMaster);
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

	@RequestMapping(value = "/project/deleteGemsProjectMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsProjectMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsProjectMasterId = 0;
		try {
			
			if ((StringUtils.isNotBlank(request.getParameter("objectId")))
					|| (StringUtils.isNotEmpty(request.getParameter("objectId")))) {
				gemsProjectMasterId = Integer.parseInt(request.getParameter("objectId"));
				GemsProjectMaster gemsProjectMaster = projectService.getGemsProjectMaster(gemsProjectMasterId);
				gemsProjectMaster.setActiveStatus(new Integer(0));
				
				GemsProjectResourceMaster searchGemsProjectResourceMaster = new GemsProjectResourceMaster();
				searchGemsProjectResourceMaster.setGemsProjectMaster(gemsProjectMaster);
				List<GemsProjectResourceMaster> gemsProjectResourceMasterList = projectService.getAllGemsProjectResourceMasterList(searchGemsProjectResourceMaster);
				
				//List<GemsProjectResourceMaster> updatedGemsProjectResourceMasterList = new ArrayList();
				
				
				if (gemsProjectResourceMasterList.size() != 0)
				{
					for (GemsProjectResourceMaster gemsProjectResourceMaster: gemsProjectResourceMasterList)
					{
						//gemsProjectResourceMaster.setActiveStatus(new Integer(0));
						//updatedGemsProjectResourceMasterList.add(gemsProjectResourceMaster);
						
						
						GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();
						searchGemsEmployeeTimeSheet.setEmployeeId(gemsProjectResourceMaster.getGemsEmployeeMaster().getGemsEmployeeMasterId());
						searchGemsEmployeeTimeSheet.setActiveStatus(1);
						List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);
						
						
						if (gemsEmployeeTimeSheetList.size() != 0)
						{
							for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList)
							{
								//gemsEmployeeTimeSheet.setActiveStatus(0);
								GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader();
								timeSheetService.removeGemsEmployeeTimeSheet(gemsEmployeeTimeSheet);
								timeSheetService.removeGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);							
								
							}
						}			
						projectService.removeGemsProjectResourceMaster(gemsProjectResourceMaster);
						
						
						
						
					}
					
				}
				
				
				/*if (updatedGemsEmployeeTimeSheetList.size() != 0)
				{
					timeSheetService.saveBatchTimeSheet(updatedGemsEmployeeTimeSheetList);
				}
				
				if (updatedGemsEmployeeTimeSheetHeaderList.size() != 0)
				{
					timeSheetService.saveBatchTimeSheetHeader(updatedGemsEmployeeTimeSheetHeaderList);
				}*/
				
				/*if (updatedGemsProjectResourceMasterList.size() != 0)
				{
					projectService.saveBatchProjectResources(updatedGemsProjectResourceMasterList);
				}*/
				
				//gemsProjectMaster.setActiveStatus(new Integer(0));				
				//projectService.saveGemsProjectMaster(gemsProjectMaster);
				projectService.removeGemsProjectMaster(gemsProjectMaster);
				
				
				//projectService.removeGemsProjectMaster(gemsProjectMaster);
			}
			
			
			
			
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

	@RequestMapping(value = "/project/getProjectById", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> gemsProjectMasterId(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsProjectMasterId_Str = request.getParameter("gemsProjectMasterId");

		GemsProjectMaster gemsProjectMaster = new GemsProjectMaster();
		try {
			if (gemsProjectMasterId_Str != null) {

				gemsProjectMaster = projectService.getGemsProjectMaster(Integer.parseInt(gemsProjectMasterId_Str));

			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapProjectObj(gemsProjectMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapProjectMasterList(List<GemsProjectMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectMaster)) {
					return new JSONObject(true);
				}

				GemsProjectMaster gemsProjectMaster = (GemsProjectMaster) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String projectStartDate = "";
				String projectEndDate = "";
				BigDecimal projectCost = new BigDecimal(0.0);

				if (gemsProjectMaster.getProjectStartDate() != null) {
					projectStartDate = sdf.format(gemsProjectMaster.getProjectStartDate());
				}
				if (gemsProjectMaster.getProjectEndDate() != null) {
					projectEndDate = sdf.format(gemsProjectMaster.getProjectEndDate());
				}
				if (gemsProjectMaster.getProjectCost() != new BigDecimal(0.0)) {
					projectCost = gemsProjectMaster.getProjectCost();
				}
				/*
				 * int selectedGemsBusinessUnitId = 0; String selected_bu = "";
				 * if (gemsProjectMaster.getGemsBusinessUnit() != null) {
				 * selectedGemsBusinessUnitId =
				 * gemsProjectMaster.getGemsBusinessUnit().getGemsBusinessUnitId
				 * (); selected_bu =
				 * gemsProjectMaster.getGemsBusinessUnit().getGemsBuCode();
				 * 
				 * }
				 */
				int selectedProjectTypeMasterId = 0;
				String selected_projecttype = "";
				if (gemsProjectMaster.getProjectTypeMaster() != null) {
					selectedProjectTypeMasterId = gemsProjectMaster.getProjectTypeMaster().getProjectTypeMasterId();
					selected_projecttype = "" + gemsProjectMaster.getProjectTypeMaster().getProjectTypeCode() + " - "
							+ gemsProjectMaster.getProjectTypeMaster().getProjectTypeDescription() + "";

				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsProjectMaster.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerMasterId();
					selected_customer = "" + gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerCode() + " - "
							+ gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerName() + "";

				}
				String projectActiveStatusString = "In-Active";

				if (gemsProjectMaster.getActiveStatus().intValue() == 1) {
					projectActiveStatusString = "Active";
				}
				return new JSONObject().element("gemsProjectMasterId", gemsProjectMaster.getGemsProjectMasterId())
						.element("projectCode", gemsProjectMaster.getProjectCode())
						.element("projectName", gemsProjectMaster.getProjectName())
						.element("projectDescription", gemsProjectMaster.getProjectDescription())
						.element("projectStatus", gemsProjectMaster.getProjectStatus())
						.element("billingType", gemsProjectMaster.getBillingType())
						.element("projectStartDate", projectStartDate).element("projectEndDate", projectEndDate)
						.element("projectCost", projectCost)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("selectedProjectTypeMasterId", selectedProjectTypeMasterId)
						.element("selected_projecttype", selected_projecttype)
						// .element("selectedGemsBusinessUnitId",selectedGemsBusinessUnitId)
						// .element("selected_bu",selected_bu)
						.element("activeStatus", gemsProjectMaster.getActiveStatus())
						.element("projectActiveStatusString", projectActiveStatusString);
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

	// JSon for Obj Construction
	private Map<String, Object> getModelMapProjectObj(GemsProjectMaster gemsProjectMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectMaster)) {
					return new JSONObject(true);
				}
				GemsProjectMaster gemsProjectMaster = (GemsProjectMaster) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String projectStartDate = "";
				String projectEndDate = "";
				BigDecimal projectCost = new BigDecimal(0.0);

				if (gemsProjectMaster.getProjectStartDate() != null) {
					projectStartDate = sdf.format(gemsProjectMaster.getProjectStartDate());
				}
				if (gemsProjectMaster.getProjectEndDate() != null) {
					projectEndDate = sdf.format(gemsProjectMaster.getProjectEndDate());
				}
				if (gemsProjectMaster.getProjectCost() != new BigDecimal(0.0)) {
					projectCost = gemsProjectMaster.getProjectCost();
				}
				/*
				 * int selectedGemsBusinessUnitId = 0; String selected_bu = "";
				 * if (gemsProjectMaster.getGemsBusinessUnit() != null) {
				 * selectedGemsBusinessUnitId =
				 * gemsProjectMaster.getGemsBusinessUnit().getGemsBusinessUnitId
				 * (); selected_bu =
				 * gemsProjectMaster.getGemsBusinessUnit().getGemsBuCode();
				 * 
				 * }
				 */
				int selectedProjectTypeMasterId = 0;
				String selected_projecttype = "";
				if (gemsProjectMaster.getProjectTypeMaster() != null) {
					selectedProjectTypeMasterId = gemsProjectMaster.getProjectTypeMaster().getProjectTypeMasterId();
					selected_projecttype = "" + gemsProjectMaster.getProjectTypeMaster().getProjectTypeCode() + " - "
							+ gemsProjectMaster.getProjectTypeMaster().getProjectTypeDescription() + "";

				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsProjectMaster.getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerMasterId();
					selected_customer = "" + gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerCode() + " - "
							+ gemsProjectMaster.getGemsCustomerMaster().getGemsCustomerName() + "";

				}

				boolean activeStatus = false;

				if (gemsProjectMaster.getActiveStatus().intValue() == 1) {
					activeStatus = true;
				}
				SimpleDateFormat importDateFormat = new SimpleDateFormat("MM/dd/yyyy");

				return new JSONObject().element("gemsProjectMasterId", gemsProjectMaster.getGemsProjectMasterId())
						.element("projectCode", gemsProjectMaster.getProjectCode())
						.element("projectName", gemsProjectMaster.getProjectName())
						.element("projectDescription", gemsProjectMaster.getProjectDescription())
						.element("projectStatus", gemsProjectMaster.getProjectStatus())
						.element("billingType", gemsProjectMaster.getBillingType())
						.element("projectStartDate", projectStartDate).element("projectEndDate", projectEndDate)
						.element("projectCost", projectCost)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("selectedProjectTypeMasterId", selectedProjectTypeMasterId)
						.element("selected_projecttype", selected_projecttype)
						// .element("selectedGemsBusinessUnitId",selectedGemsBusinessUnitId)
						// .element("selected_bu",selected_bu)
						.element("activeStatus", activeStatus);

			}
		});

		JSON json = JSONSerializer.toJSON(gemsProjectMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

}
