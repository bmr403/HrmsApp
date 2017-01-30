package eProject.web.master;

import java.math.BigDecimal;
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

import eProject.domain.master.GemsPayGrade;
import eProject.domain.master.GemsUserMaster;
import eProject.service.master.MasterService;

@Controller
public class GemsPayGradeService {
	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsPayGrade.class);

	@RequestMapping(value = "/master/checkPayGradeByCode", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkPayGradeByCode(HttpServletRequest request) {

		String payGradeCode = request.getParameter("payGradeCode");

		GemsPayGrade gemsPayGrade = new GemsPayGrade();
		gemsPayGrade.setPayGradeCode(payGradeCode);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		gemsPayGrade.setGemsOrganisation(loggedInUser.getGemsOrganisation());
		GemsPayGrade returnedGemsPayGrade = masterService.getGemsPayGradeByCode(gemsPayGrade);
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		if ((StringUtils.isNotBlank(returnedGemsPayGrade.getPayGradeCode()))
				|| (StringUtils.isNotEmpty(returnedGemsPayGrade.getPayGradeCode()))) {
			String msg = "Code already exists";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		} else {
			modelMap.put("success", true);
			return modelMap;
		}

	}

	@RequestMapping(value = "/master/viewPayGradeList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewPayGradeList(HttpServletRequest request) {

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

			GemsPayGrade gemsPayGrade = new GemsPayGrade();

			String searchPayGradeCode = request.getParameter("searchPayGradeCode");
			if (searchPayGradeCode != null && searchPayGradeCode.isEmpty() == false) {
				gemsPayGrade.setPayGradeCode(searchPayGradeCode);
			}

			String searchPayGradeDescription = request.getParameter("searchPayGradeDescription");
			if (searchPayGradeDescription != null && searchPayGradeDescription.isEmpty() == false) {
				gemsPayGrade.setPayGradeDescription(searchPayGradeDescription);
			}
			gemsPayGrade.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			int totalResults = masterService.getGemsPayGradeFilterCount(gemsPayGrade);
			List<GemsPayGrade> list = masterService.getGemsPayGradeList(start, limit, gemsPayGrade);

			logger.info("Returned list size" + list.size());

			return getModelMapPayGradeList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/master/savePayGrade", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> savePayGrade(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd ");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsPayGrade gemsPayGrade = new GemsPayGrade();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsPayGrade.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsPayGrade.setUpdatedOn(todayDate);
			gemsPayGrade.setGemsOrganisation(loggedInUser.getGemsOrganisation());
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsPayGradeId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsPayGradeId")))) {
				id_value = request.getParameter("gemsPayGradeId");
				gemsPayGrade = masterService.getGemsPayGrade(Integer.parseInt(id_value));
			} else {
				gemsPayGrade.setCreatedOn(todayDate);
				gemsPayGrade.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String payGradeCode = request.getParameter("payGradeCode");
			gemsPayGrade.setPayGradeCode(payGradeCode);
			String payGradeDescription = request.getParameter("payGradeDescription");
			gemsPayGrade.setPayGradeDescription(payGradeDescription);
			String maxSalary = request.getParameter("maxSalary");
			gemsPayGrade.setMaxSalary(new BigDecimal(maxSalary));
			String minSalary = request.getParameter("minSalary");
			gemsPayGrade.setMinSalary(new BigDecimal(minSalary));
			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsPayGrade.setActiveStatus(1);
			} else {
				gemsPayGrade.setActiveStatus(0);
			}

			masterService.saveGemsPayGrade(gemsPayGrade);
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

	@RequestMapping(value = "/master/deletePayGrade", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deletePayGrade(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsPayGradeId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsPayGrade gemsPayGrade = masterService.getGemsPayGrade(gemsPayGradeId);
			masterService.removegemsPayGrade(gemsPayGrade);
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

	private Map<String, Object> getModelMapPayGradeList(List<GemsPayGrade> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsPayGrade.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsPayGrade)) {
					return new JSONObject(true);
				}

				GemsPayGrade gemsPayGrade = (GemsPayGrade) bean;

				return new JSONObject().element("gemsPayGradeId", gemsPayGrade.getGemsPayGradeId())
						.element("payGradeCode", gemsPayGrade.getPayGradeCode())
						.element("payGradeCodeDescription",
								"" + gemsPayGrade.getPayGradeCode() + " - " + gemsPayGrade.getPayGradeDescription()
										+ "")
						.element("payGradeDescription", gemsPayGrade.getPayGradeDescription())
						.element("minSalary", gemsPayGrade.getMinSalary())
						.element("maxSalary", gemsPayGrade.getMaxSalary())
						.element("activeStatus", gemsPayGrade.getActiveStatus());
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
