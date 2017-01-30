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

import eProject.domain.master.GemsUserMaster;
import eProject.domain.recruitment.GemsCandidateWorkExp;
import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;

@Controller
public class GemsCandidateWorkExpService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCandidateWorkExpService.class);

	@RequestMapping(value = "/recruitment/viewGemsCandidateWorkExpList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewGemsCandidateWorkExpList(HttpServletRequest request) {

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

			GemsCandidateWorkExp gemsCandidateWorkExp = new GemsCandidateWorkExp();

			String selectedGemsCandidateMasterId = request.getParameter("selectedGemsCandidateMasterId");
			if (selectedGemsCandidateMasterId != null && selectedGemsCandidateMasterId.isEmpty() == false) {
				gemsCandidateWorkExp.setGemsCandidateMaster(
						recruitementService.getGemsCandidateMaster(Integer.parseInt(selectedGemsCandidateMasterId)));
			}

			int totalResults = recruitementService.getGemsCandidateWorkExpFilterCount(gemsCandidateWorkExp);
			List<GemsCandidateWorkExp> list = recruitementService.getGemsCandidateWorkExpList(start, limit,
					gemsCandidateWorkExp);

			logger.info("Returned list size" + list.size());

			return getModelMapCandidateWorkExpList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveGemsCandidateWorkExp", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsCandidateWorkExp(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCandidateWorkExp gemsCandidateWorkExp = new GemsCandidateWorkExp();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsCandidateMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsCandidateMasterId")))) {
				gemsCandidateWorkExp.setGemsCandidateMaster(recruitementService.getGemsCandidateMaster(
						Integer.parseInt(request.getParameter("selectedGemsCandidateMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmpWorkExpId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmpWorkExpId")))) {
				id_value = request.getParameter("gemsEmpWorkExpId");
				gemsCandidateWorkExp = recruitementService.getGemsCandidateWorkExp(Integer.parseInt(id_value));
			} else {
				gemsCandidateWorkExp.setCreatedOn(todayDate);
				gemsCandidateWorkExp.setCreatedBy(loggedInUser.getCreatedBy());
			}

			String fromDateString = request.getParameter("fromDate");
			if ((StringUtils.isNotBlank(fromDateString)) || (StringUtils.isNotEmpty(fromDateString))) {
				Date fromDate = formatter.parse(fromDateString);
				gemsCandidateWorkExp.setFromDate(fromDate);
			}

			String toDateString = request.getParameter("toDate");
			if ((StringUtils.isNotBlank(toDateString)) || (StringUtils.isNotEmpty(toDateString))) {
				Date toDate = formatter.parse(toDateString);
				gemsCandidateWorkExp.setToDate(toDate);
			}

			String companyName = request.getParameter("companyName");
			gemsCandidateWorkExp.setCompanyName(companyName);

			String jobTitle = request.getParameter("jobTitle");
			gemsCandidateWorkExp.setJobTitle(jobTitle);

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCandidateWorkExp.setActiveStatus(1);
			} else {
				gemsCandidateWorkExp.setActiveStatus(0);
			}

			recruitementService.saveGemsCandidateWorkExp(gemsCandidateWorkExp);
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

	@RequestMapping(value = "/recruitment/deleteGemsCandidateWorkExp", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsCandidateWorkExp(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsEmpWorkExpId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCandidateWorkExp gemsCandidateWorkExp = recruitementService.getGemsCandidateWorkExp(gemsEmpWorkExpId);
			recruitementService.removeGemsCandidateWorkExp(gemsCandidateWorkExp);
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

	private Map<String, Object> getModelMapCandidateWorkExpList(List<GemsCandidateWorkExp> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateWorkExp.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateWorkExp)) {
					return new JSONObject(true);
				}

				GemsCandidateWorkExp gemsCandidateWorkExp = (GemsCandidateWorkExp) bean;

				int selectedGemsCandidateMasterId = 0;
				String selectedGemsCandidateMasterName = "";
				if (gemsCandidateWorkExp.getGemsCandidateMaster() != null) {
					selectedGemsCandidateMasterId = gemsCandidateWorkExp.getGemsCandidateMaster()
							.getGemsCandidateMasterId();

					if (gemsCandidateWorkExp.getGemsCandidateMaster().getGemsCandidateFirstName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateWorkExp.getGemsCandidateMaster().getGemsCandidateLastName();
					}
					if (gemsCandidateWorkExp.getGemsCandidateMaster().getGemsCandidateLastName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateWorkExp.getGemsCandidateMaster().getGemsCandidateLastName();
					}
				}

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String fromDate = "";
				if (gemsCandidateWorkExp.getFromDate() != null) {
					fromDate = sdf.format(gemsCandidateWorkExp.getFromDate());
				}

				String toDate = "";
				if (gemsCandidateWorkExp.getToDate() != null) {
					toDate = sdf.format(gemsCandidateWorkExp.getToDate());
				}

				return new JSONObject()
						.element("gemsCandidateWorkExpId", gemsCandidateWorkExp.getGemsCandidateWorkExpId())
						.element("companyName", gemsCandidateWorkExp.getCompanyName()).element("fromDate", fromDate)
						.element("toDate", toDate).element("jobTitle", gemsCandidateWorkExp.getJobTitle())
						.element("activeStatus", gemsCandidateWorkExp.getActiveStatus())
						.element("selectedGemsCandidateMasterId", selectedGemsCandidateMasterId)
						.element("selectedGemsCandidateMasterName", selectedGemsCandidateMasterName)

				;
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
