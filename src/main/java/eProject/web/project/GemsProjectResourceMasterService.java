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

import eProject.dao.timesheet.TimeSheetDao;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.service.employee.EmployeeService;
import eProject.service.master.MasterService;
import eProject.service.project.ProjectService;
import eProject.service.timesheet.TimeSheetServiceImpl;

@Controller
public class GemsProjectResourceMasterService {
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TimeSheetServiceImpl timeSheetService;

	@Autowired
	private MasterService masterService;

	@Autowired
	private EmployeeService employeeService;

	protected final Log logger = LogFactory.getLog(GemsProjectResourceMasterService.class);

	@RequestMapping(value = "/project/viewProjectResourceList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewProjectResourceList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 10;

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

			GemsProjectResourceMaster gemsProjectResourceMaster = new GemsProjectResourceMaster();

			String selectedGemsProjectMasterId = request.getParameter("selectedGemsProjectMasterId");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsProjectMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsProjectMasterId")))) {
				logger.info("selected Gems ProjectMasterId is : " + selectedGemsProjectMasterId);
				gemsProjectResourceMaster.setGemsProjectMaster(
						projectService.getGemsProjectMaster(Integer.parseInt(selectedGemsProjectMasterId)));
			}

			String selectedGemsEmployeeMasterId_Str = request.getParameter("selectedGemsEmployeeMasterId");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsEmployeeMasterId")))) {
				logger.info(" selected GemsEmployeeMasterId_Str is : " + selectedGemsEmployeeMasterId_Str);
				gemsProjectResourceMaster.setGemsEmployeeMaster(
						employeeService.getGemsEmployeeMaster(Integer.parseInt(selectedGemsEmployeeMasterId_Str)));
			}

			String searchProjectName = request.getParameter("searchProjectName");
			if ((StringUtils.isNotBlank(request.getParameter("searchProjectName")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchProjectName")))) {
				GemsProjectMaster searchGemsProjectMaster = new GemsProjectMaster();
				searchGemsProjectMaster.setProjectName(searchProjectName);
				gemsProjectResourceMaster.setGemsProjectMaster(searchGemsProjectMaster);
			}

			GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
			if ((StringUtils.isNotBlank(request.getParameter("searchResourceFirstName")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchResourceFirstName")))) {
				String searchResourceFirstName = request.getParameter("searchResourceFirstName");
				searchGemsEmployeeMaster.setEmployeeFirstName(searchResourceFirstName);

			}
			if ((StringUtils.isNotBlank(request.getParameter("searchResourceLastName")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchResourceLastName")))) {
				String searchResourceLastName = request.getParameter("searchResourceLastName");
				searchGemsEmployeeMaster.setEmployeeLastName(searchResourceLastName);
			}

			if ((searchGemsEmployeeMaster.getEmployeeLastName() != null)
					|| (searchGemsEmployeeMaster.getEmployeeFirstName() != null)) {
				gemsProjectResourceMaster.setGemsEmployeeMaster(searchGemsEmployeeMaster);
			}

			if ((StringUtils.isNotBlank(request.getParameter("showMyProject")))
					|| (StringUtils.isNotEmpty(request.getParameter("showMyProject")))) {
				GemsEmployeeMaster userEmployee = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
						"userEmployee");
				gemsProjectResourceMaster.setGemsEmployeeMaster(userEmployee);
			}

			if ((StringUtils.isNotBlank(request.getParameter("searchProjectResourceActive")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchProjectResourceActive")))) {
				String isActive = request.getParameter("searchProjectResourceActive");
				if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
					gemsProjectResourceMaster.setActiveStatus(1);
				} else {
					gemsProjectResourceMaster.setActiveStatus(0);

				}

			} else {
				gemsProjectResourceMaster.setActiveStatus(1); // By Default it
																// will show
																// only Active
																// employees
			}

			int totalResults = projectService.getGemsProjectResourceMasterFilterCount(gemsProjectResourceMaster);
			List<GemsProjectResourceMaster> list = projectService.getGemsProjectResourceMasterList(start, limit,gemsProjectResourceMaster);
			
			
			
			

			logger.info("Returned list size" + list.size());

			return getModelMapProjectResourceList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/project/viewMyProjects", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewMyProjects(HttpServletRequest request) {

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

			GemsProjectResourceMaster gemsProjectResourceMaster = new GemsProjectResourceMaster();

			GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			gemsProjectResourceMaster.setGemsEmployeeMaster(gemsEmployeeMaster);

			int totalResults = projectService.getMyGemsProjectResourceMasterFilterCount(gemsProjectResourceMaster);
			List<GemsProjectResourceMaster> list = projectService.getMyGemsProjectResourceMasterList(start, limit,
					gemsProjectResourceMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapProjectResourceList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/project/saveGemsProjectResource", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsProjectResource(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsProjectResourceMaster gemsProjectResourceMaster = new GemsProjectResourceMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsProjectResourceMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsProjectResourceMaster.setUpdatedOn(todayDate);

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsProjectResourceId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsProjectResourceId")))) {
				id_value = request.getParameter("gemsProjectResourceId");
				gemsProjectResourceMaster = projectService.getGemsProjectResourceMaster(Integer.parseInt(id_value));
			} else {
				gemsProjectResourceMaster.setCreatedOn(todayDate);
				gemsProjectResourceMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}

			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsProjectMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsProjectMasterId")))) {
				gemsProjectResourceMaster.setGemsProjectMaster(projectService
						.getGemsProjectMaster(Integer.parseInt(request.getParameter("selectedGemsProjectMasterId"))));
			}

			String resourceStartDateString = request.getParameter("resourceStartDate");
			if ((StringUtils.isNotBlank(resourceStartDateString))
					|| (StringUtils.isNotEmpty(resourceStartDateString))) {
				Date resourceStartDate = formatter.parse(resourceStartDateString);
				gemsProjectResourceMaster.setResourceStartDate(resourceStartDate);
			}

			String resourceEndDateString = request.getParameter("resourceEndDate");
			if ((StringUtils.isNotBlank(resourceEndDateString)) || (StringUtils.isNotEmpty(resourceEndDateString))) {
				Date resourceEndDate = formatter.parse(resourceEndDateString);
				gemsProjectResourceMaster.setResourceEndDate(resourceEndDate);
			}

			if ((StringUtils.isNotBlank(request.getParameter("projectBillingRate")))
					|| (StringUtils.isNotEmpty(request.getParameter("projectBillingRate")))) {
				gemsProjectResourceMaster
						.setProjectBillingRate(new BigDecimal(request.getParameter("projectBillingRate")));
			}

			int effort_duration_days = (int) ((gemsProjectResourceMaster.getResourceEndDate().getTime()
					- gemsProjectResourceMaster.getResourceStartDate().getTime()) / (1000 * 60 * 60 * 24));
			gemsProjectResourceMaster.setScheduleEffort(effort_duration_days);

			int gemsEmployeeMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selectedEmployeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedEmployeeMasterId")))) {
				try {
					gemsEmployeeMasterId = Integer.parseInt(request.getParameter("selectedEmployeeMasterId"));

					gemsProjectResourceMaster
							.setGemsEmployeeMaster(employeeService.getGemsEmployeeMaster(gemsEmployeeMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsProjectResourceMaster.setGemsEmployeeMaster(gemsProjectResourceMaster.getGemsEmployeeMaster());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsProjectResourceMaster.setActiveStatus(1);
				gemsProjectResourceMaster.setInActiveFrom(null);
			} else {
				gemsProjectResourceMaster.setActiveStatus(0);
				String inActiveFromString = request.getParameter("inActiveFrom");
				if ((StringUtils.isNotBlank(inActiveFromString)) || (StringUtils.isNotEmpty(inActiveFromString))) {
					Date inActiveFrom = formatter.parse(inActiveFromString);
					gemsProjectResourceMaster.setInActiveFrom(inActiveFrom);
				} else {
					gemsProjectResourceMaster.setInActiveFrom(todayDate);
				}
			}

			projectService.saveGemsProjectResourceMaster(gemsProjectResourceMaster);
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

	/*
	 * @RequestMapping(value="/project/saveGemsProjectResource",method=
	 * RequestMethod.POST) public @ResponseBody Map<String, Object>
	 * saveGemsProjectResource(HttpServletRequest request) { logger.info(
	 * "Insert Method Strarted.,"); Map<String, Object> modelMap = new
	 * HashMap<String, Object>(2); try { Calendar currentDate =
	 * Calendar.getInstance(); SimpleDateFormat formatter = new
	 * SimpleDateFormat("MM/dd/yyyy"); Date todayDate = (Date)
	 * formatter.parse(formatter.format(currentDate .getTime()));
	 * 
	 * GemsProjectResourceMaster gemsProjectResourceMaster = new
	 * GemsProjectResourceMaster(); GemsUserMaster loggedInUser =
	 * (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
	 * "loggedInUser");
	 * gemsProjectResourceMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId()
	 * ); gemsProjectResourceMaster.setUpdatedOn(todayDate);
	 * 
	 * String id_value = ""; if
	 * ((StringUtils.isNotBlank(request.getParameter("gemsProjectResourceId")))
	 * ||
	 * (StringUtils.isNotEmpty(request.getParameter("gemsProjectResourceId"))))
	 * { id_value = request.getParameter("gemsProjectResourceId");
	 * gemsProjectResourceMaster =
	 * projectService.getGemsProjectResourceMaster(Integer.parseInt(id_value));
	 * } else { gemsProjectResourceMaster.setCreatedOn(todayDate);
	 * gemsProjectResourceMaster.setCreatedBy(loggedInUser.getCreatedBy()); }
	 * 
	 * String resourceStartDateString =
	 * request.getParameter("resourceStartDate"); if
	 * ((StringUtils.isNotBlank(resourceStartDateString)) ||
	 * (StringUtils.isNotEmpty(resourceStartDateString))) { Date
	 * resourceStartDate = formatter.parse(resourceStartDateString);
	 * gemsProjectResourceMaster.setResourceStartDate(resourceStartDate); }
	 * 
	 * String resourceEndDateString = request.getParameter("resourceEndDate");
	 * if ((StringUtils.isNotBlank(resourceEndDateString)) ||
	 * (StringUtils.isNotEmpty(resourceEndDateString))) { Date resourceEndDate =
	 * formatter.parse(resourceEndDateString);
	 * gemsProjectResourceMaster.setResourceEndDate(resourceEndDate); }
	 * 
	 * 
	 * if ((StringUtils.isNotBlank(request.getParameter("projectBillingRate")))
	 * || (StringUtils.isNotEmpty(request.getParameter("projectBillingRate"))))
	 * { gemsProjectResourceMaster.setProjectBillingRate(new
	 * BigDecimal(request.getParameter("projectBillingRate"))); }
	 * 
	 * 
	 * int effort_duration_days = (int)(
	 * (gemsProjectResourceMaster.getResourceEndDate().getTime() -
	 * gemsProjectResourceMaster.getResourceStartDate().getTime()) / (1000 * 60
	 * * 60 * 24));
	 * gemsProjectResourceMaster.setScheduleEffort(effort_duration_days);
	 * 
	 * String isActive = request.getParameter("activeStatus"); if ((isActive ==
	 * "on") || (isActive.equalsIgnoreCase("on"))) {
	 * gemsProjectResourceMaster.setActiveStatus(1); } else {
	 * gemsProjectResourceMaster.setActiveStatus(0); }
	 * 
	 * int gemsEmployeeMasterId = 0; String selectedEmployeesStr = null;
	 * String[] employeesArray = null ; selectedEmployeesStr =
	 * request.getParameter("gemsSelectedEmployeesArray");
	 * if(selectedEmployeesStr != null){ employeesArray =
	 * selectedEmployeesStr.split(","); GemsEmployeeMaster employeeMaster =
	 * null; for(int employeeMasterId = 0; employeeMasterId <
	 * employeesArray.length; employeeMasterId++){ logger.info(
	 * "each Employee id is : "+employeesArray[employeeMasterId]); try { if
	 * ((StringUtils.isNotBlank(request.getParameter(
	 * "selectedGemsProjectMasterId"))) ||
	 * (StringUtils.isNotEmpty(request.getParameter(
	 * "selectedGemsProjectMasterId")))) {
	 * gemsProjectResourceMaster.setGemsProjectMaster(projectService.
	 * getGemsProjectMaster(Integer.parseInt(request.getParameter(
	 * "selectedGemsProjectMasterId")))); }
	 * 
	 * 
	 * 
	 * gemsEmployeeMasterId =
	 * Integer.parseInt(employeesArray[employeeMasterId].trim()); employeeMaster
	 * = new GemsEmployeeMaster(); employeeMaster =
	 * employeeService.getGemsEmployeeMaster(gemsEmployeeMasterId);
	 * gemsProjectResourceMaster.setGemsEmployeeMaster(employeeService.
	 * getGemsEmployeeMaster(gemsEmployeeMasterId));
	 * projectService.saveGemsProjectResourceMaster(gemsProjectResourceMaster);
	 * } catch(NumberFormatException ex) { // this will be called when the drop
	 * down value does not changed
	 * gemsProjectResourceMaster.setGemsEmployeeMaster(gemsProjectResourceMaster
	 * .getGemsEmployeeMaster()); } } }
	 * 
	 * //
	 * projectService.saveGemsProjectResourceMaster(gemsProjectResourceMaster);
	 * logger.info("Insert Method Executed.,"); modelMap.put("success", true);
	 * modelMap.put("message", "Saved Successfully"); return modelMap; }
	 * catch(Exception ex) { String msg = "Sorry problem in saving data";
	 * modelMap.put("success", false); modelMap.put("message", msg); return
	 * modelMap; }
	 * 
	 * 
	 * 
	 * }
	 */
	@RequestMapping(value = "/project/deleteGemsProjectResourceMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> deleteGemsProjectResourceMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		
		int gemsProjectResourceId = 0;
		
		try 
		{
				if ((StringUtils.isNotBlank(request.getParameter("objectId")))
					|| (StringUtils.isNotEmpty(request.getParameter("objectId")))) {
				
				gemsProjectResourceId = Integer.parseInt(request.getParameter("objectId"));
				Calendar currentDate = Calendar.getInstance();
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
				Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));
				GemsProjectResourceMaster gemsProjectResourceMaster = projectService
						.getGemsProjectResourceMaster(gemsProjectResourceId);
				
				
				
				GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();
				searchGemsEmployeeTimeSheet.setEmployeeId(gemsProjectResourceMaster.getGemsEmployeeMaster().getGemsEmployeeMasterId());
				searchGemsEmployeeTimeSheet.setActiveStatus(1);
				List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);
				//List<GemsEmployeeTimeSheet> updatedGemsEmployeeTimeSheetList = new ArrayList();
				//List<GemsEmployeeTimeSheetHeader> updatedGemsEmployeeTimeSheetHeaderList = new ArrayList();
				
				if (gemsEmployeeTimeSheetList.size() != 0)
				{
					for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList)
					{
						gemsEmployeeTimeSheet.setActiveStatus(0);
						GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader();
						timeSheetService.removeGemsEmployeeTimeSheet(gemsEmployeeTimeSheet);	
						timeSheetService.removeGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);
						
						
						
					}
					
					/*if (updatedGemsEmployeeTimeSheetList.size() != 0)
					{
						timeSheetService.saveBatchTimeSheet(updatedGemsEmployeeTimeSheetList);
					}
					
					if (updatedGemsEmployeeTimeSheetHeaderList.size() != 0)
					{
						timeSheetService.saveBatchTimeSheetHeader(updatedGemsEmployeeTimeSheetHeaderList);
					}*/
					
					
				}
				//gemsProjectResourceMaster.setActiveStatus(0);
				//gemsProjectResourceMaster.setInActiveFrom(todayDate);
				
				//projectService.saveGemsProjectResourceMaster(gemsProjectResourceMaster);
				projectService.removeGemsProjectResourceMaster(gemsProjectResourceMaster);
				logger.info("Delete Method Completed.,");
				modelMap.put("success", true);
				modelMap.put("message", "Deleted Successfully");
			}
			else
			{
				modelMap.put("success", false);
				modelMap.put("message", "Error in deletion");
			}
			return modelMap;
			
		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}	
		
		

	}

	@RequestMapping(value = "/project/getProjectResourceObject.action", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryInfoById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsProjectResourceId_Str = request.getParameter("gemsProjectResourceId");
		GemsProjectResourceMaster gemsProjectResourceMaster = new GemsProjectResourceMaster();
		try {
			if (gemsProjectResourceId_Str != null) {
				gemsProjectResourceMaster = projectService
						.getGemsProjectResourceMaster(Integer.parseInt(gemsProjectResourceId_Str));

			} else {
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapProjectResourceObject(gemsProjectResourceMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapProjectResourceList(List<GemsProjectResourceMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectResourceMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectResourceMaster)) {
					return new JSONObject(true);
				}

				GemsProjectResourceMaster gemsProjectResourceMaster = (GemsProjectResourceMaster) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String resourceStartDate = "";
				String resourceEndDate = "";
				BigDecimal projectBillingRate = new BigDecimal(0.0);

				if (gemsProjectResourceMaster.getResourceStartDate() != null) {
					resourceStartDate = sdf.format(gemsProjectResourceMaster.getResourceStartDate());
				}
				if (gemsProjectResourceMaster.getResourceEndDate() != null) {
					resourceEndDate = sdf.format(gemsProjectResourceMaster.getResourceEndDate());
				}
				if (gemsProjectResourceMaster.getProjectBillingRate() != new BigDecimal(0.0)) {
					projectBillingRate = gemsProjectResourceMaster.getProjectBillingRate();
				}
				int selectedGemsEmployeeMasterId = 0;
				String selected_employee = "";
				if (gemsProjectResourceMaster.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsProjectResourceMaster.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();
					selected_employee = "" + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName()
							+ "  " + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() + "";

				}
				int selectedGemsProjectMasterId = 0;
				String selected_project = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster() != null) {
					selectedGemsProjectMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getGemsProjectMasterId();
					selected_project = "" + gemsProjectResourceMaster.getGemsProjectMaster().getProjectCode() + " -- "
							+ gemsProjectResourceMaster.getGemsProjectMaster().getProjectName() + "";

				}

				String projectStartDate = "";
				String projectEndDate = "";
				BigDecimal projectCost = new BigDecimal(0.0);

				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectStartDate() != null) {
					projectStartDate = sdf
							.format(gemsProjectResourceMaster.getGemsProjectMaster().getProjectStartDate());
				}
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectEndDate() != null) {
					projectEndDate = sdf.format(gemsProjectResourceMaster.getGemsProjectMaster().getProjectEndDate());
				}
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectCost() != new BigDecimal(0.0)) {
					projectCost = gemsProjectResourceMaster.getGemsProjectMaster().getProjectCost();
				}
				/*
				 * int selectedGemsBusinessUnitId = 0; String selected_bu = "";
				 * if (gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit() != null) { selectedGemsBusinessUnitId =
				 * gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit().getGemsBusinessUnitId(); selected_bu =
				 * gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit().getGemsBuCode();
				 * 
				 * }
				 */
				int selectedProjectTypeMasterId = 0;
				String selected_projecttype = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster() != null) {
					selectedProjectTypeMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getProjectTypeMaster().getProjectTypeMasterId();
					selected_projecttype = ""
							+ gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster()
									.getProjectTypeCode()
							+ " - " + gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster()
									.getProjectTypeDescription()
							+ "";

				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getGemsCustomerMaster().getGemsCustomerMasterId();
					selected_customer = ""
							+ gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster()
									.getGemsCustomerCode()
							+ " - " + gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster()
									.getGemsCustomerName()
							+ "";

				}
				String projectResourceActiveString = "In-Active";

				if (gemsProjectResourceMaster.getActiveStatus() == 1) {
					projectResourceActiveString = "Active";
				}
				boolean activeStatus = false;
				String inActiveDateString = "";
				if (gemsProjectResourceMaster.getActiveStatus() == 1) {
					activeStatus = true;
				} else {
					inActiveDateString = sdf.format(gemsProjectResourceMaster.getInActiveFrom());
				}
				
				GemsEmployeeTimeSheet searchGemsEmployee = new GemsEmployeeTimeSheet();
				searchGemsEmployee.setEmployeeId(gemsProjectResourceMaster.getGemsEmployeeMaster().getGemsEmployeeMasterId());				
				int timeSheetListCount = timeSheetService.getGemsEmployeeTimeSheetFilterCount(searchGemsEmployee);

				return new JSONObject()
						.element("gemsProjectResourceId", gemsProjectResourceMaster.getGemsProjectResourceId())
						.element("resourceStartDate", resourceStartDate).element("resourceEndDate", resourceEndDate)
						.element("projectBillingRate", projectBillingRate)
						.element("scheduleEffort", gemsProjectResourceMaster.getScheduleEffort())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selected_employee", selected_employee)
						.element("selectedGemsProjectMasterId", selectedGemsProjectMasterId)
						.element("selected_project", selected_project).element("activeStatus", activeStatus)
						.element("projectCode", gemsProjectResourceMaster.getGemsProjectMaster().getProjectCode())
						.element("projectName", gemsProjectResourceMaster.getGemsProjectMaster().getProjectName())
						.element("projectDescription",
								gemsProjectResourceMaster.getGemsProjectMaster().getProjectDescription())
						.element("projectStatus", gemsProjectResourceMaster.getGemsProjectMaster().getProjectStatus())
						.element("billingType", gemsProjectResourceMaster.getGemsProjectMaster().getBillingType())
						.element("projectStartDate", projectStartDate).element("projectEndDate", projectEndDate)
						.element("projectCost", projectCost)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("selectedProjectTypeMasterId", selectedProjectTypeMasterId)
						.element("selected_projecttype", selected_projecttype)
						// .element("selectedGemsBusinessUnitId",selectedGemsBusinessUnitId)
						// .element("selected_bu",selected_bu)
						.element("projectResourceActiveString", projectResourceActiveString)
						.element("timeSheetListCount",timeSheetListCount)
						.element("inActiveFrom", inActiveDateString);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapProjectResourceObject(GemsProjectResourceMaster gemsProjectResourceMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectResourceMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectResourceMaster)) {
					return new JSONObject(true);
				}

				GemsProjectResourceMaster gemsProjectResourceMaster = (GemsProjectResourceMaster) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String resourceStartDate = "";
				String resourceEndDate = "";
				BigDecimal projectBillingRate = new BigDecimal(0.0);

				if (gemsProjectResourceMaster.getResourceStartDate() != null) {
					resourceStartDate = sdf.format(gemsProjectResourceMaster.getResourceStartDate());
				}
				if (gemsProjectResourceMaster.getResourceEndDate() != null) {
					resourceEndDate = sdf.format(gemsProjectResourceMaster.getResourceEndDate());
				}
				if (gemsProjectResourceMaster.getProjectBillingRate() != new BigDecimal(0.0)) {
					projectBillingRate = gemsProjectResourceMaster.getProjectBillingRate();
				}
				int selectedGemsEmployeeMasterId = 0;
				String selected_employee = "";
				if (gemsProjectResourceMaster.getGemsEmployeeMaster() != null) {
					selectedGemsEmployeeMasterId = gemsProjectResourceMaster.getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();
					selected_employee = "" + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName()
							+ "  " + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() + "";

				}
				int selectedGemsProjectMasterId = 0;
				String selected_project = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster() != null) {
					selectedGemsProjectMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getGemsProjectMasterId();
					selected_project = "" + gemsProjectResourceMaster.getGemsProjectMaster().getProjectCode() + " - "
							+ gemsProjectResourceMaster.getGemsProjectMaster().getProjectName() + "";

				}

				String projectStartDate = "";
				String projectEndDate = "";
				BigDecimal projectCost = new BigDecimal(0.0);

				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectStartDate() != null) {
					projectStartDate = sdf
							.format(gemsProjectResourceMaster.getGemsProjectMaster().getProjectStartDate());
				}
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectEndDate() != null) {
					projectEndDate = sdf.format(gemsProjectResourceMaster.getGemsProjectMaster().getProjectEndDate());
				}
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectCost() != new BigDecimal(0.0)) {
					projectCost = gemsProjectResourceMaster.getGemsProjectMaster().getProjectCost();
				}
				/*
				 * int selectedGemsBusinessUnitId = 0; String selected_bu = "";
				 * if (gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit() != null) { selectedGemsBusinessUnitId =
				 * gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit().getGemsBusinessUnitId(); selected_bu =
				 * gemsProjectResourceMaster.getGemsProjectMaster().
				 * getGemsBusinessUnit().getGemsBuCode();
				 * 
				 * }
				 */
				int selectedProjectTypeMasterId = 0;
				String selected_projecttype = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster() != null) {
					selectedProjectTypeMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getProjectTypeMaster().getProjectTypeMasterId();
					selected_projecttype = ""
							+ gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster()
									.getProjectTypeCode()
							+ " - " + gemsProjectResourceMaster.getGemsProjectMaster().getProjectTypeMaster()
									.getProjectTypeDescription()
							+ "";

				}
				int selectedGemsCustomerMasterId = 0;
				String selected_customer = "";
				if (gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster() != null) {
					selectedGemsCustomerMasterId = gemsProjectResourceMaster.getGemsProjectMaster()
							.getGemsCustomerMaster().getGemsCustomerMasterId();
					selected_customer = ""
							+ gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster()
									.getGemsCustomerCode()
							+ " - " + gemsProjectResourceMaster.getGemsProjectMaster().getGemsCustomerMaster()
									.getGemsCustomerName()
							+ "";

				}
				boolean activeStatus = false;
				String inActiveDateString = "";
				if (gemsProjectResourceMaster.getActiveStatus() == 1) {
					activeStatus = true;
				} else {
					inActiveDateString = sdf.format(gemsProjectResourceMaster.getInActiveFrom());
				}
				return new JSONObject()
						.element("gemsProjectResourceId", gemsProjectResourceMaster.getGemsProjectResourceId())
						.element("resourceStartDate", resourceStartDate).element("resourceEndDate", resourceEndDate)
						.element("projectBillingRate", projectBillingRate)
						.element("scheduleEffort", gemsProjectResourceMaster.getScheduleEffort())
						.element("selectedGemsEmployeeMasterId", selectedGemsEmployeeMasterId)
						.element("selected_employee", selected_employee)
						.element("selectedGemsProjectMasterId", selectedGemsProjectMasterId)
						.element("selected_project", selected_project).element("activeStatus", activeStatus)
						.element("projectCode", gemsProjectResourceMaster.getGemsProjectMaster().getProjectCode())
						.element("projectName", gemsProjectResourceMaster.getGemsProjectMaster().getProjectName())
						.element("projectDescription",
								gemsProjectResourceMaster.getGemsProjectMaster().getProjectDescription())
						.element("projectStatus", gemsProjectResourceMaster.getGemsProjectMaster().getProjectStatus())
						.element("billingType", gemsProjectResourceMaster.getGemsProjectMaster().getBillingType())
						.element("projectStartDate", projectStartDate).element("projectEndDate", projectEndDate)
						.element("projectCost", projectCost)
						.element("selectedGemsCustomerMasterId", selectedGemsCustomerMasterId)
						.element("selected_customer", selected_customer)
						.element("selectedProjectTypeMasterId", selectedProjectTypeMasterId)
						.element("selected_projecttype", selected_projecttype)
						// .element("selectedGemsBusinessUnitId",selectedGemsBusinessUnitId)
						// .element("selected_bu",selected_bu)
						.element("inActiveFrom", inActiveDateString);
			}
		});

		JSON json = JSONSerializer.toJSON(gemsProjectResourceMaster, jsonConfig);

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
