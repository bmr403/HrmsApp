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
import eProject.domain.recruitment.GemsCandidateEducationDetail;

import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;

@Controller
public class GemsCandidateEducationDetailService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCandidateEducationDetailService.class);

	@RequestMapping(value = "/recruitment/viewCandidateEducationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCandidateEducationList(HttpServletRequest request) {

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

			GemsCandidateEducationDetail gemsCandidateEducationDetail = new GemsCandidateEducationDetail();

			String selectedGemsCandidateMasterId = request.getParameter("selectedGemsCandidateMasterId");
			if (selectedGemsCandidateMasterId != null && selectedGemsCandidateMasterId.isEmpty() == false) {
				gemsCandidateEducationDetail.setGemsCandidateMaster(
						recruitementService.getGemsCandidateMaster(Integer.parseInt(selectedGemsCandidateMasterId)));
			}

			int totalResults = recruitementService
					.getGemsCandidateEducationDetailFilterCount(gemsCandidateEducationDetail);
			List<GemsCandidateEducationDetail> list = recruitementService.getGemsCandidateEducationDetailList(start,
					limit, gemsCandidateEducationDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapCandidateEducationDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveCandidateEducationDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCandidateEducationDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCandidateEducationDetail gemsCandidateEducationDetail = new GemsCandidateEducationDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsCandidateMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsCandidateMasterId")))) {
				gemsCandidateEducationDetail.setGemsCandidateMaster(recruitementService.getGemsCandidateMaster(
						Integer.parseInt(request.getParameter("selectedGemsCandidateMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsCandidateEducationDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCandidateEducationDetailId")))) {
				id_value = request.getParameter("gemsCandidateEducationDetailId");
				gemsCandidateEducationDetail = recruitementService
						.getGemsCandidateEducationDetail(Integer.parseInt(id_value));
			} else {
				gemsCandidateEducationDetail.setCreatedOn(todayDate);
				gemsCandidateEducationDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String isCourseRegular = request.getParameter("isCourseRegular");
			if ((isCourseRegular == "on") || (isCourseRegular.equalsIgnoreCase("on"))) {
				gemsCandidateEducationDetail.setIsCourseRegular(1);
			} else {
				gemsCandidateEducationDetail.setIsCourseRegular(0);
			}

			String yearPercentage = request.getParameter("yearPercentage");
			gemsCandidateEducationDetail.setYearPercentage(yearPercentage);

			String universityName = request.getParameter("universityName");
			gemsCandidateEducationDetail.setUniversityName(universityName);

			String yearOfPassString = request.getParameter("yearOfPass");
			if ((StringUtils.isNotBlank(yearOfPassString)) || (StringUtils.isNotEmpty(yearOfPassString))) {
				Date yearOfPass = formatter.parse(yearOfPassString);
				gemsCandidateEducationDetail.setYearOfPass(yearOfPass);
			}

			int gemsEducationMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_education")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_education")))) {
				try {
					gemsEducationMasterId = Integer.parseInt(request.getParameter("selected_education"));

					gemsCandidateEducationDetail
							.setGemsEducationMaster(masterService.getGemsEducationMaster(gemsEducationMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsCandidateEducationDetail
							.setGemsEducationMaster(gemsCandidateEducationDetail.getGemsEducationMaster());
				}
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCandidateEducationDetail.setActiveStatus(1);
			} else {
				gemsCandidateEducationDetail.setActiveStatus(0);
			}

			recruitementService.saveGemsCandidateEducationDetail(gemsCandidateEducationDetail);
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

	@RequestMapping(value = "/recruitment/deleteCandidateEducationDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCandidateEducationDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCandidateEducationDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCandidateEducationDetail gemsCandidateEducationDetail = recruitementService
					.getGemsCandidateEducationDetail(gemsCandidateEducationDetailId);
			recruitementService.removeGemsCandidateEducationDetail(gemsCandidateEducationDetail);
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

	private Map<String, Object> getModelMapCandidateEducationDetailList(List<GemsCandidateEducationDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateEducationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateEducationDetail)) {
					return new JSONObject(true);
				}

				GemsCandidateEducationDetail gemsCandidateEducationDetail = (GemsCandidateEducationDetail) bean;

				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				String yearOfPass = "";
				if (gemsCandidateEducationDetail.getYearOfPass() != null) {
					yearOfPass = sdf.format(gemsCandidateEducationDetail.getYearOfPass());
				}
				int selectedGemsEducationMasterId = 0;
				String selected_education = "";
				if (gemsCandidateEducationDetail.getGemsEducationMaster() != null) {
					selectedGemsEducationMasterId = gemsCandidateEducationDetail.getGemsEducationMaster()
							.getGemsEducationMasterId();
					selected_education = gemsCandidateEducationDetail.getGemsEducationMaster().getEducationCode();

				}

				int selectedGemsCandidateMasterId = 0;
				String selectedGemsCandidateMasterName = "";
				if (gemsCandidateEducationDetail.getGemsCandidateMaster() != null) {
					selectedGemsCandidateMasterId = gemsCandidateEducationDetail.getGemsCandidateMaster()
							.getGemsCandidateMasterId();

					if (gemsCandidateEducationDetail.getGemsCandidateMaster().getGemsCandidateFirstName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateEducationDetail.getGemsCandidateMaster().getGemsCandidateFirstName();
					}
					if (gemsCandidateEducationDetail.getGemsCandidateMaster().getGemsCandidateLastName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateEducationDetail.getGemsCandidateMaster().getGemsCandidateLastName();
					}
				}

				return new JSONObject()
						.element("gemsCandidateEducationDetailId",
								gemsCandidateEducationDetail.getGemsCandidateEducationDetailId())
						.element("isCourseRegular", gemsCandidateEducationDetail.getIsCourseRegular())
						.element("yearPercentage", gemsCandidateEducationDetail.getYearPercentage())
						.element("universityName", gemsCandidateEducationDetail.getUniversityName())
						.element("yearOfPass", yearOfPass)
						.element("selectedGemsEducationMasterId", selectedGemsEducationMasterId)
						.element("selected_education", selected_education)
						.element("activeStatus", gemsCandidateEducationDetail.getActiveStatus())
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
