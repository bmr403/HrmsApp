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
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsOrganisationService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsOrganisationService.class);

	@RequestMapping(value = "/master/checkOrganisationByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkOrganisationByCode(HttpServletRequest request) {

		String gemsOrganisationCode = request.getParameter("gemsOrganisationCode");

		GemsOrganisation gemsOrganisation = new GemsOrganisation();
		gemsOrganisation.setGemsOrganisationCode(gemsOrganisationCode);

		GemsOrganisation returnedGemsOrganisation = masterService.getGemsOrganisationByCode(gemsOrganisation);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsOrganisation.getGemsOrganisationCode()))
				|| (StringUtils.isNotEmpty(returnedGemsOrganisation.getGemsOrganisationCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewOrganisationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewOrganisationList(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			GemsOrganisation gemsOrganisation = new GemsOrganisation();

			String searchCode = request.getParameter("searchOrgCode");
			if (searchCode != null && searchCode.isEmpty() == false) {
				gemsOrganisation.setGemsOrganisationCode(searchCode);
			}

			String searchDescription = request.getParameter("searchOrgName");
			if (searchDescription != null && searchDescription.isEmpty() == false) {
				gemsOrganisation.setGemsOrgName(searchDescription);
			}

			int totalResults = masterService.getGemsOrganisationFilterCount(gemsOrganisation);
			List<GemsOrganisation> list = masterService.getGemsOrganisationList(start, limit, gemsOrganisation);

			logger.info("Returned list size" + list.size());

			return getModelMapOrganisationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/saveGemsOrganisation", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsOrganisation(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsOrganisation gemsOrganisation = new GemsOrganisation();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsOrganisation.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsOrganisation.setUpdatedOn(todayDate);
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsOrgId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsOrgId")))) {
				id_value = request.getParameter("gemsOrgId");
				gemsOrganisation = masterService.getGemsOrganisation(Integer.parseInt(id_value));
			} else {
				gemsOrganisation.setCreatedOn(todayDate);
				gemsOrganisation.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String gemsOrganisationCode = request.getParameter("gemsOrganisationCode");
			gemsOrganisation.setGemsOrganisationCode(gemsOrganisationCode);
			String gemsOrgName = request.getParameter("gemsOrgName");
			gemsOrganisation.setGemsOrgName(gemsOrgName);
			String gemsOrgAddress = request.getParameter("gemsOrgAddress");
			gemsOrganisation.setGemsOrgAddress(gemsOrgAddress);
			String gemsOrgCity = request.getParameter("gemsOrgCity");
			gemsOrganisation.setGemsOrgCity(gemsOrgCity);
			String gemsOrgEmail = request.getParameter("gemsOrgEmail");
			gemsOrganisation.setGemsOrgEmail(gemsOrgEmail);
			String gemsOrgFax = request.getParameter("gemsOrgFax");
			gemsOrganisation.setGemsOrgFax(gemsOrgFax);
			String gemsOrgNote = request.getParameter("gemsOrgNote");
			gemsOrganisation.setGemsOrgNote(gemsOrgNote);
			String gemsOrgPhone = request.getParameter("gemsOrgPhone");
			gemsOrganisation.setGemsOrgPhone(gemsOrgPhone);
			String gemsOrgPhone1 = request.getParameter("gemsOrgPhone1");
			gemsOrganisation.setGemsOrgPhone1(gemsOrgPhone1);
			String gemsOrgRegistrationNo = request.getParameter("gemsOrgRegistrationNo");
			gemsOrganisation.setGemsOrgRegistrationNo(gemsOrgRegistrationNo);
			String gemsOrgState = request.getParameter("gemsOrgState");
			gemsOrganisation.setGemsOrgState(gemsOrgState);
			String gemsOrgTaxId = request.getParameter("gemsOrgTaxId");
			gemsOrganisation.setGemsOrgTaxId(gemsOrgTaxId);
			String adOrgZipCode = request.getParameter("adOrgZipCode");
			gemsOrganisation.setAdOrgZipCode(adOrgZipCode);

			int countryId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_country")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_country")))) {
				try {
					countryId = Integer.parseInt(request.getParameter("selected_country"));

					gemsOrganisation.setGemsCountryMaster(masterService.getGemsCountryMaster(countryId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsOrganisation.setGemsCountryMaster(gemsOrganisation.getGemsCountryMaster());
				}
			}

			int currencyId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_currency")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_currency")))) {
				try {
					currencyId = Integer.parseInt(request.getParameter("selected_currency"));

					gemsOrganisation.setGemsCurrencyMaster(masterService.getGemsCurrencyMaster(currencyId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsOrganisation.setGemsCurrencyMaster(gemsOrganisation.getGemsCurrencyMaster());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsOrganisation.setActiveStatus(1);
			} else {
				gemsOrganisation.setActiveStatus(0);
			}

			masterService.saveGemsOrganisation(gemsOrganisation);
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

	@RequestMapping(value = "/master/deleteGemsOrganisation", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsOrganisation(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsOrgId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsOrganisation gemsOrganisation = masterService.getGemsOrganisation(gemsOrgId);
			masterService.removeGemsOrganisation(gemsOrganisation);
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

	private Map<String, Object> getModelMapOrganisationList(List<GemsOrganisation> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsOrganisation.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsOrganisation)) {
					return new JSONObject(true);
				}

				GemsOrganisation gemsOrganisation = (GemsOrganisation) bean;
				int selectedCountryId = 0;
				String selected_country = "";
				if (gemsOrganisation.getGemsCountryMaster() != null) {
					selectedCountryId = gemsOrganisation.getGemsCountryMaster().getGemsCountryMasterId();
					selected_country = gemsOrganisation.getGemsCountryMaster().getGemsCountryCode();
				}
				int selectedCurrencyId = 0;
				String selected_currency = "";
				if (gemsOrganisation.getGemsCurrencyMaster() != null) {
					selectedCurrencyId = gemsOrganisation.getGemsCurrencyMaster().getGemsCurrencyMasterId();
					selected_currency = gemsOrganisation.getGemsCurrencyMaster().getCurrencyCode();
				}
				return new JSONObject().element("gemsOrgId", gemsOrganisation.getGemsOrgId())
						.element("activeStatus", gemsOrganisation.getActiveStatus())
						.element("adOrgZipCode", gemsOrganisation.getAdOrgZipCode())
						.element("gemsOrgAddress", gemsOrganisation.getGemsOrgAddress())
						.element("gemsOrganisationCode", gemsOrganisation.getGemsOrganisationCode())
						.element("gemsOrgCity", gemsOrganisation.getGemsOrgCity())
						.element("gemsOrgEmail", gemsOrganisation.getGemsOrgEmail())
						.element("gemsOrgFax", gemsOrganisation.getGemsOrgFax())
						.element("gemsOrgName", gemsOrganisation.getGemsOrgName())
						.element("gemsOrgNote", gemsOrganisation.getGemsOrgNote())
						.element("gemsOrgPhone", gemsOrganisation.getGemsOrgPhone())
						.element("gemsOrgPhone1", gemsOrganisation.getGemsOrgPhone1())
						.element("gemsOrgRegistrationNo", gemsOrganisation.getGemsOrgRegistrationNo())
						.element("gemsOrgState", gemsOrganisation.getGemsOrgState())
						.element("selectedCountryId", selectedCountryId).element("selected_country", selected_country)
						.element("selectedCurrencyId", selectedCurrencyId)
						.element("selected_currency", selected_currency)
						.element("gemsOrgTaxId", gemsOrganisation.getGemsOrgTaxId());
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
