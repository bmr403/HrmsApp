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
import eProject.domain.recruitment.GemsCandidateSkillDetail;
import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;

@Controller
public class GemsCandidateSkillDetailService {
	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCandidateSkillDetailService.class);

	@RequestMapping(value = "/recruitment/viewCandidateSkillList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCandidateSkillList(HttpServletRequest request) {

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

			GemsCandidateSkillDetail gemsCandidateSkillDetail = new GemsCandidateSkillDetail();

			String selectedGemsCandidateMasterId = request.getParameter("selectedGemsCandidateMasterId");
			if (selectedGemsCandidateMasterId != null && selectedGemsCandidateMasterId.isEmpty() == false) {
				gemsCandidateSkillDetail.setGemsCandidateMaster(
						recruitementService.getGemsCandidateMaster(Integer.parseInt(selectedGemsCandidateMasterId)));
			}

			int totalResults = recruitementService.getGemsCandidateSkillDetailFilterCount(gemsCandidateSkillDetail);
			List<GemsCandidateSkillDetail> list = recruitementService.getGemsCandidateSkillDetailList(start, limit,
					gemsCandidateSkillDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapCandidateSkillDetailList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveCandidateSkillDetail", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveCandidateEducationDetail(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCandidateSkillDetail gemsCandidateSkillDetail = new GemsCandidateSkillDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsCandidateMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsCandidateMasterId")))) {
				gemsCandidateSkillDetail.setGemsCandidateMaster(recruitementService.getGemsCandidateMaster(
						Integer.parseInt(request.getParameter("selectedGemsCandidateMasterId"))));
			}

			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsCandidateSkillId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCandidateSkillId")))) {
				id_value = request.getParameter("gemsCandidateSkillId");
				gemsCandidateSkillDetail = recruitementService.getGemsCandidateSkillDetail(Integer.parseInt(id_value));
			} else {
				gemsCandidateSkillDetail.setCreatedOn(todayDate);
				gemsCandidateSkillDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String isPrimarySkill = request.getParameter("isPrimarySkill");
			if ((isPrimarySkill == "on") || (isPrimarySkill.equalsIgnoreCase("on"))) {
				gemsCandidateSkillDetail.setIsPrimarySkill(1);
			} else {
				gemsCandidateSkillDetail.setIsPrimarySkill(0);
			}

			String skillName = request.getParameter("skillName");
			gemsCandidateSkillDetail.setSkillName(skillName);

			String versionNo = request.getParameter("versionNo");
			gemsCandidateSkillDetail.setVersionNo(versionNo);

			String lastUsed = request.getParameter("lastUsed");
			gemsCandidateSkillDetail.setLastUsed(Integer.parseInt(lastUsed));

			String experienseInMonths = request.getParameter("experienseInMonths");
			gemsCandidateSkillDetail.setExperienseInMonths(Integer.parseInt(experienseInMonths));

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCandidateSkillDetail.setActiveStatus(1);
			} else {
				gemsCandidateSkillDetail.setActiveStatus(0);
			}

			recruitementService.saveGemsCandidateSkillDetail(gemsCandidateSkillDetail);
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

	@RequestMapping(value = "/recruitment/deleteCandidateSkillDetail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteCandidateSkillDetail(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCandidateSkillId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCandidateSkillDetail gemsCandidateSkillDetail = recruitementService
					.getGemsCandidateSkillDetail(gemsCandidateSkillId);
			recruitementService.removeGemsCandidateSkillDetail(gemsCandidateSkillDetail);
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

	private Map<String, Object> getModelMapCandidateSkillDetailList(List<GemsCandidateSkillDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateSkillDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateSkillDetail)) {
					return new JSONObject(true);
				}

				GemsCandidateSkillDetail gemsCandidateSkillDetail = (GemsCandidateSkillDetail) bean;

				int selectedGemsCandidateMasterId = 0;
				String selectedGemsCandidateMasterName = "";
				if (gemsCandidateSkillDetail.getGemsCandidateMaster() != null) {
					selectedGemsCandidateMasterId = gemsCandidateSkillDetail.getGemsCandidateMaster()
							.getGemsCandidateMasterId();

					if (gemsCandidateSkillDetail.getGemsCandidateMaster().getGemsCandidateFirstName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateSkillDetail.getGemsCandidateMaster().getGemsCandidateFirstName();
					}
					if (gemsCandidateSkillDetail.getGemsCandidateMaster().getGemsCandidateLastName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateSkillDetail.getGemsCandidateMaster().getGemsCandidateLastName();
					}
				}

				return new JSONObject()
						.element("gemsCandidateSkillId", gemsCandidateSkillDetail.getGemsCandidateSkillId())
						.element("isPrimarySkill", gemsCandidateSkillDetail.getIsPrimarySkill())
						.element("skillName", gemsCandidateSkillDetail.getSkillName())
						.element("versionNo", gemsCandidateSkillDetail.getSkillName())
						.element("lastUsed", gemsCandidateSkillDetail.getSkillName())
						.element("experienseInMonths", gemsCandidateSkillDetail.getSkillName())
						.element("activeStatus", gemsCandidateSkillDetail.getActiveStatus())
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
