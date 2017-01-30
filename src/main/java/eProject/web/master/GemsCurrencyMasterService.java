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
import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsCurrencyMasterService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCurrencyMasterService.class);

	@RequestMapping(value = "/master/checkCurrencyByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkCurrencyByCode(HttpServletRequest request) {

		String currencyCode = request.getParameter("currencyCode");

		GemsCurrencyMaster gemsCurrencyMaster = new GemsCurrencyMaster();
		gemsCurrencyMaster.setCurrencyCode(currencyCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsCurrencyMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());

		GemsCurrencyMaster returnedGemsCurrencyMaster = masterService.getGemsCurrencyMasterByCode(gemsCurrencyMaster);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsCurrencyMaster.getCurrencyCode()))
				|| (StringUtils.isNotEmpty(returnedGemsCurrencyMaster.getCurrencyCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewCurrencyList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCurrencyList(HttpServletRequest request) {

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

			GemsCurrencyMaster gemsCurrencyMaster = new GemsCurrencyMaster();

			String searchCode = request.getParameter("searchCurrencyCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsCurrencyMaster.setCurrencyCode(searchCode);
			}

			String searchDescription = request.getParameter("searchCurrencyDescription");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsCurrencyMaster.setCurrencyDescription(searchDescription);
			}
			gemsCurrencyMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsCurrencyMasterFilterCount(gemsCurrencyMaster);
			List<GemsCurrencyMaster> list = masterService.getGemsCurrencyMasterList(start, limit, gemsCurrencyMaster);

			logger.info("Returned list size" + list.size());

			return getModelMapCurrencyList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveCurrencyMaster", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCurrencyMaster(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCurrencyMaster gemsCurrencyMaster = new GemsCurrencyMaster();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsCurrencyMaster.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsCurrencyMaster.setUpdatedOn(todayDate);
			gemsCurrencyMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsCurrencyMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCurrencyMasterId")))) {
				id_value = request.getParameter("gemsCurrencyMasterId");
				gemsCurrencyMaster = masterService.getGemsCurrencyMaster(Integer.parseInt(id_value));
			} else {
				gemsCurrencyMaster.setCreatedOn(todayDate);
				gemsCurrencyMaster.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String currencyCode = request.getParameter("currencyCode");
			gemsCurrencyMaster.setCurrencyCode(currencyCode);
			String currencyDescription = request.getParameter("currencyDescription");
			gemsCurrencyMaster.setCurrencyDescription(currencyDescription);
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCurrencyMaster.setActiveStatus(1);
			} else {
				gemsCurrencyMaster.setActiveStatus(0);
			}

			masterService.saveGemsCurrencyMaster(gemsCurrencyMaster);
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

	@RequestMapping(value = "/master/deleteCurrencyMaster", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCurrencyMaster(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCurrencyMasterId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCurrencyMaster gemsCurrencyMaster = masterService.getGemsCurrencyMaster(gemsCurrencyMasterId);
			masterService.removeGemsCurrencyMaster(gemsCurrencyMaster);
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

	private Map<String, Object> getModelMapCurrencyList(List<GemsCurrencyMaster> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCurrencyMaster.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCurrencyMaster)) {
					return new JSONObject(true);
				}

				GemsCurrencyMaster gemsCurrencyMaster = (GemsCurrencyMaster) bean;

				return new JSONObject().element("gemsCurrencyMasterId", gemsCurrencyMaster.getGemsCurrencyMasterId())
						.element("currencyCode", gemsCurrencyMaster.getCurrencyCode())
						.element("currencyDescription", gemsCurrencyMaster.getCurrencyDescription())
						.element("activeStatus", gemsCurrencyMaster.getActiveStatus());
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
