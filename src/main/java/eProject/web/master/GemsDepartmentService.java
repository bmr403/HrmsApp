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

import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsDepartmentService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsDepartmentService.class);

	@RequestMapping(value = "/master/checkDepartmentByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDepartmentByCode(HttpServletRequest request) {

		String code = request.getParameter("departmentCode");

		GemsDepartment gemsDepartment = new GemsDepartment();
		gemsDepartment.setDepartmentCode(code);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsDepartment.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsDepartment returnedGemsDepartment = masterService.getGemsDepartmentByCode(gemsDepartment);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsDepartment.getDepartmentCode()))
				|| (StringUtils.isNotEmpty(returnedGemsDepartment.getDepartmentCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewDepartmentList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewDepartmentList(HttpServletRequest request) {

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

			GemsDepartment gemsDepartment = new GemsDepartment();

			String searchCode = request.getParameter("searchDepartmentCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsDepartment.setDepartmentCode(searchCode);
			}

			String searchName = request.getParameter("searchDepartmentName");
			if (searchName != null && searchName.isEmpty() == false) {
				gemsDepartment.setDepartmentName(searchName);
			}
			gemsDepartment.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsDepartmentFilterCount(gemsDepartment);
			List<GemsDepartment> list = masterService.getGemsDepartmentList(start, limit, gemsDepartment);

			logger.info("Returned list size" + list.size());

			return getModelMapDepartmentList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveDepartment", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveDepartment(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsDepartment gemsDepartment = new GemsDepartment();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsDepartment.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsDepartment.setUpdatedOn(todayDate);
			gemsDepartment.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsDepartmentId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsDepartmentId")))) {
				id_value = request.getParameter("gemsDepartmentId");
				gemsDepartment = masterService.getGemsDepartment(Integer.parseInt(id_value));
			} else {
				gemsDepartment.setCreatedOn(todayDate);
				gemsDepartment.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String departmentCode = request.getParameter("departmentCode");
			gemsDepartment.setDepartmentCode(departmentCode);

			String departmentName = request.getParameter("departmentName");
			gemsDepartment.setDepartmentName(departmentName);

			String departmentDescription = request.getParameter("departmentDescription");
			gemsDepartment.setDepartmentDescription(departmentDescription);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsDepartment.setActiveStatus(1);
			} else {
				gemsDepartment.setActiveStatus(0);
			}

			masterService.saveGemsDepartment(gemsDepartment);
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

	@RequestMapping(value = "/master/deleteDepartment", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteDepartment(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsDepartmentId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsDepartment gemsDepartment = masterService.getGemsDepartment(gemsDepartmentId);
			masterService.removeGemsDepartment(gemsDepartment);
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

	private Map<String, Object> getModelMapDepartmentList(List<GemsDepartment> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsDepartment.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsDepartment)) {
					return new JSONObject(true);
				}

				GemsDepartment gemsDepartment = (GemsDepartment) bean;

				return new JSONObject().element("gemsDepartmentId", gemsDepartment.getGemsDepartmentId())
						.element("departmentCode", gemsDepartment.getDepartmentCode())
						.element("departmentName", gemsDepartment.getDepartmentName())
						.element("departmentCodeName",
								"" + gemsDepartment.getDepartmentCode() + " - " + gemsDepartment.getDepartmentName()
										+ "")
						.element("departmentDescription", gemsDepartment.getDepartmentDescription())
						.element("activeStatus", gemsDepartment.getActiveStatus());
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
