package eProject.web.master;

import java.math.BigDecimal;
import java.text.ParseException;
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

import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;

import eProject.service.master.MasterService;

@Controller
public class GemsRoleMasterService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsRoleMasterService.class);

	@RequestMapping(value = "/master/checkRoleByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkRoleByCode(HttpServletRequest request) {

		String roleCode = request.getParameter("roleCode");

		GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
		gemsRoleMaster.setRoleCode(roleCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsRoleMaster returnedGemsRoleMaster = masterService.getGemsRoleMasterByCode(gemsRoleMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsRoleMaster.getRoleCode()))
				|| (StringUtils.isNotEmpty(returnedGemsRoleMaster.getRoleCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewRoleList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewRoleList(HttpServletRequest request) {

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

			GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();

			String searchCode = request.getParameter("searchRoleCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsRoleMaster.setRoleCode(searchCode);
			}

			String searchDescription = request.getParameter("searchRoleName");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsRoleMaster.setRoleName(searchDescription);
			}
			gemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsRoleMasterFilterCount(gemsRoleMaster);
			List<GemsRoleMaster> list = masterService.getGemsRoleMasterList(start, limit, gemsRoleMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapRoleList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/viewAllRoles", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewAllRoles(HttpServletRequest request) {

		try {

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();

			String searchCode = request.getParameter("searchRoleCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsRoleMaster.setRoleCode(searchCode);
			}

			String searchDescription = request.getParameter("searchRoleName");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsRoleMaster.setRoleName(searchDescription);
			}
			gemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsRoleMasterFilterCount(gemsRoleMaster);
			List<GemsRoleMaster> list = masterService.getAllGemsRoleMasterList(gemsRoleMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapRoleList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveRole", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveRole(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsRoleMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsRoleMaster.setUpdatedOn(todayDate);
			gemsRoleMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsRoleMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsRoleMasterId")))) {
				id_value = request.getParameter("gemsRoleMasterId");
				gemsRoleMaster = masterService.getGemsRoleMaster(Integer.parseInt(id_value));
			} else {
				gemsRoleMaster.setCreatedOn(todayDate);
				gemsRoleMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String roleCode = request.getParameter("roleCode");
			gemsRoleMaster.setRoleCode(roleCode);
			String roleName = request.getParameter("roleName");
			gemsRoleMaster.setRoleName(roleName);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsRoleMaster.setActiveStatus(1);
			} else {
				gemsRoleMaster.setActiveStatus(0);
			}

			masterService.saveGemsRoleMaster(gemsRoleMaster);
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

	@RequestMapping(value = "/master/getRoleById", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> gemsProjectMasterId(HttpServletRequest request) throws ParseException {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsRoleMasterId_Str = request.getParameter("gemsRoleMasterId");

		GemsRoleMaster gemsRoleMaster = new GemsRoleMaster();
		try {
			if (gemsRoleMasterId_Str != null) {

				gemsRoleMaster = masterService.getGemsRoleMaster(Integer.parseInt(gemsRoleMasterId_Str));

			} else {
				String msg = "Sorry problem in loading data";
				modelMap.put("success", false);
				modelMap.put("message", msg);
				return getModelMapError("Failed to Load Data");
			}

			return getModelMapRoleObj(gemsRoleMaster);

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in loading data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/deleteRole", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteRole(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsRoleMasterId = Integer.parseInt(request.getParameter("gemsRoleMasterId"));
		try {
			GemsRoleMaster gemsRoleMaster = masterService.getGemsRoleMaster(gemsRoleMasterId);
			masterService.removeGemsRoleMaster(gemsRoleMaster);
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

	private Map<String, Object> getModelMapRoleList(List<GemsRoleMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRoleMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRoleMaster)) {
					return new JSONObject(true);
				}

				GemsRoleMaster gemsRoleMaster = (GemsRoleMaster) bean;

				String roleName = "";
				if (gemsRoleMaster.getRoleName() != null) {
					roleName = gemsRoleMaster.getRoleName();
				}

				return new JSONObject().element("gemsRoleMasterId", gemsRoleMaster.getGemsRoleMasterId())
						.element("roleName", roleName).element("roleCode", gemsRoleMaster.getRoleCode())
						.element("activeStatus", gemsRoleMaster.getActiveStatus());
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
	private Map<String, Object> getModelMapRoleObj(GemsRoleMaster gemsRoleMaster) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsRoleMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsRoleMaster)) {
					return new JSONObject(true);
				}
				GemsRoleMaster gemsRoleMaster = (GemsRoleMaster) bean;

				String roleName = "";
				if (gemsRoleMaster.getRoleName() != null) {
					roleName = gemsRoleMaster.getRoleName();
				}

				return new JSONObject().element("gemsRoleMasterId", gemsRoleMaster.getGemsRoleMasterId())
						.element("roleCode", gemsRoleMaster.getRoleCode()).element("roleName", roleName)
						.element("activeStatus", gemsRoleMaster.getActiveStatus());

			}
		});

		JSON json = JSONSerializer.toJSON(gemsRoleMaster, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

}
