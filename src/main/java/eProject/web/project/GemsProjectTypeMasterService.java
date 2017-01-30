package eProject.web.project;

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

import eProject.dao.project.ProjectDao;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.service.project.ProjectService;
import eProject.service.timesheet.TimeSheetServiceImpl;

@Controller
public class GemsProjectTypeMasterService {
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private TimeSheetServiceImpl timeSheetService;

	protected final Log logger = LogFactory.getLog(GemsProjectTypeMasterService.class);

	@RequestMapping(value = "/project/checkProjectTypeByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkProjectTypeByCode(HttpServletRequest request) {

		String projectTypeCode = request.getParameter("projectTypeCode");

		GemsProjectTypeMaster gemsProjectTypeMaster = new GemsProjectTypeMaster();
		gemsProjectTypeMaster.setProjectTypeCode(projectTypeCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsProjectTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsProjectTypeMaster returnedGemsProjectTypeMaster = projectService
				.getGemsProjectTypeMasterByCode(gemsProjectTypeMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsProjectTypeMaster.getProjectTypeCode()))
				|| (StringUtils.isNotEmpty(returnedGemsProjectTypeMaster.getProjectTypeCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/project/viewProjectTypeList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewProjectTypeList(HttpServletRequest request) {

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

			GemsProjectTypeMaster gemsProjectTypeMaster = new GemsProjectTypeMaster();

			String searchProjectTypeCode = request.getParameter("searchProjectTypeCode");
			if (searchProjectTypeCode != null && searchProjectTypeCode.isEmpty() == false) {
				gemsProjectTypeMaster.setProjectTypeCode(searchProjectTypeCode);
			}

			String searchProjectTypeDescription = request.getParameter("searchProjectTypeDescription");
			if (searchProjectTypeDescription != null && searchProjectTypeDescription.isEmpty() == false) {
				gemsProjectTypeMaster.setProjectTypeDescription(searchProjectTypeDescription);
			}

			if (gemsProjectTypeMaster.getGemsOrganisation() != null) {
				gemsProjectTypeMaster.setGemsOrganisation(gemsProjectTypeMaster.getGemsOrganisation());
			} else {
				gemsProjectTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			}

			if ((StringUtils.isNotBlank(request.getParameter("searchProjectTypeActive")))
					|| (StringUtils.isNotEmpty(request.getParameter("searchProjectTypeActive")))) {
				String isActive = request.getParameter("searchProjectTypeActive");
				if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
					gemsProjectTypeMaster.setActiveStatus(1);
				} else {
					gemsProjectTypeMaster.setActiveStatus(0);

				}

			} else {
				gemsProjectTypeMaster.setActiveStatus(1); // By Default it will
															// show only Active
															// employees
			}

			int totalResults = projectService.getGemsProjectTypeMasterFilterCount(gemsProjectTypeMaster);
			List<GemsProjectTypeMaster> list = projectService.getGemsProjectTypeMasterList(start, limit,
					gemsProjectTypeMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapProjectTypeMasterList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/project/saveGemsProjectTypeMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsProjectTypeMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsProjectTypeMaster gemsProjectTypeMaster = new GemsProjectTypeMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsProjectTypeMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsProjectTypeMaster.setUpdatedOn(todayDate);
			gemsProjectTypeMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("projectTypeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("projectTypeMasterId")))) {
				id_value = request.getParameter("projectTypeMasterId");
				gemsProjectTypeMaster = projectService.getGemsProjectTypeMaster(Integer.parseInt(id_value));
			} else {
				gemsProjectTypeMaster.setCreatedOn(todayDate);
				gemsProjectTypeMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String projectTypeCode = request.getParameter("projectTypeCode");
			gemsProjectTypeMaster.setProjectTypeCode(projectTypeCode);
			String projectTypeDescription = request.getParameter("projectTypeDescription");
			gemsProjectTypeMaster.setProjectTypeDescription(projectTypeDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsProjectTypeMaster.setActiveStatus(1);
			} else {
				gemsProjectTypeMaster.setActiveStatus(0);
			}
			projectService.saveGemsProjectTypeMaster(gemsProjectTypeMaster);
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

	@RequestMapping(value = "/project/deleteGemsProjectTypeMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsProjectTypeMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int projectTypeMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsProjectTypeMaster gemsProjectTypeMaster = projectService.getGemsProjectTypeMaster(projectTypeMasterId);
			GemsProjectMaster searchGemsProjectMaster = new GemsProjectMaster();
			searchGemsProjectMaster.setProjectTypeMaster(gemsProjectTypeMaster);	
			searchGemsProjectMaster.setActiveStatus(new Integer(1));
			List<GemsProjectMaster> gemsProjectMasterList = projectService.getAllGemsProjectMasterList(searchGemsProjectMaster);
			//List<GemsProjectMaster> updatedGemsProjectMasterList = new ArrayList();
			//List<GemsEmployeeTimeSheet> updatedGemsEmployeeTimeSheetList = new ArrayList();
			//List<GemsEmployeeTimeSheetHeader> updatedGemsEmployeeTimeSheetHeaderList = new ArrayList();
			//List<GemsProjectResourceMaster> updatedGemsProjectResourceMasterList = new ArrayList();
			
			if (gemsProjectMasterList.size() != 0)
			{
				for (GemsProjectMaster gemsProjectMaster: gemsProjectMasterList)
				{
					//gemsProjectMaster.setActiveStatus(new Integer(0));
					//updatedGemsProjectMasterList.add(gemsProjectMaster);
					
					
					GemsProjectResourceMaster searchGemsProjectResourceMaster = new GemsProjectResourceMaster();
					searchGemsProjectResourceMaster.setGemsProjectMaster(gemsProjectMaster);
					searchGemsProjectResourceMaster.setActiveStatus(new Integer(1));
					List<GemsProjectResourceMaster> gemsProjectResourceMasterList = projectService.getAllGemsProjectResourceMasterList(searchGemsProjectResourceMaster);
					
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
					projectService.removeGemsProjectMaster(gemsProjectMaster);
				}
			}
			
			
			// Batch Update starts
			
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
			
			/*if (updatedGemsProjectMasterList.size() != 0)
			{
				projectService.saveBatchProjects(updatedGemsProjectMasterList);
			}*/
			//gemsProjectTypeMaster.setActiveStatus(0);
			//projectService.saveGemsProjectTypeMaster(gemsProjectTypeMaster);			
			
			projectService.removeGemsProjectTypeMaster(gemsProjectTypeMaster);
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

	@RequestMapping(value = "/projectType/getProjectTypeObjById", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProjectTypeObjById(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsProjectTypeMasterId_Str = request.getParameter("projectTypeMasterId");

		GemsProjectTypeMaster gemsProjectTypeMaster = new GemsProjectTypeMaster();
		try {
			if (gemsProjectTypeMasterId_Str != null) {

				gemsProjectTypeMaster = projectService
						.getGemsProjectTypeMaster(Integer.parseInt(gemsProjectTypeMasterId_Str));

			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapProjectTypeObj(gemsProjectTypeMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	private Map<String, Object> getModelMapProjectTypeMasterList(List<GemsProjectTypeMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectTypeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectTypeMaster)) {
					return new JSONObject(true);
				}

				GemsProjectTypeMaster gemsProjectTypeMaster = (GemsProjectTypeMaster) bean;

				String projectTypeActiveStatusString = "In-Active";

				if (gemsProjectTypeMaster.getActiveStatus() == 1) {
					projectTypeActiveStatusString = "Active";
				}

				return new JSONObject().element("projectTypeMasterId", gemsProjectTypeMaster.getProjectTypeMasterId())
						.element("projectTypeCode", gemsProjectTypeMaster.getProjectTypeCode())
						.element("projectTypeDescription", gemsProjectTypeMaster.getProjectTypeDescription())
						.element("projectTypeCodeName",
								"" + gemsProjectTypeMaster.getProjectTypeCode() + "  "
										+ gemsProjectTypeMaster.getProjectTypeDescription() + "")
						.element("activeStatus", gemsProjectTypeMaster.getActiveStatus())
						.element("projectTypeActiveStatusString", projectTypeActiveStatusString);
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
	private Map<String, Object> getModelMapProjectTypeObj(GemsProjectTypeMaster gemsProjectTypeMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsProjectTypeMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsProjectTypeMaster)) {
					return new JSONObject(true);
				}
				GemsProjectTypeMaster gemsProjectTypeMaster = (GemsProjectTypeMaster) bean;

				boolean activeStatus = false;

				if (gemsProjectTypeMaster.getActiveStatus() == 1) {
					activeStatus = true;
				}

				return new JSONObject().element("projectTypeMasterId", gemsProjectTypeMaster.getProjectTypeMasterId())
						.element("projectTypeCode", gemsProjectTypeMaster.getProjectTypeCode())
						.element("projectTypeDescription", gemsProjectTypeMaster.getProjectTypeDescription())
						.element("projectTypeCodeName",
								"" + gemsProjectTypeMaster.getProjectTypeCode() + "  "
										+ gemsProjectTypeMaster.getProjectTypeDescription() + "")
						.element("activeStatus", gemsProjectTypeMaster.getActiveStatus());

			}
		});

		JSON json = JSONSerializer.toJSON(gemsProjectTypeMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}
}
