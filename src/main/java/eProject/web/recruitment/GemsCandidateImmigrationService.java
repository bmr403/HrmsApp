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
import eProject.domain.recruitment.GemsCandidateImmigrationDetail;
import eProject.service.master.MasterService;
import eProject.service.recruitment.RecruitementService;

@Controller
public class GemsCandidateImmigrationService {

	@Autowired
	private RecruitementService recruitementService;

	@Autowired
	private MasterService masterService;

	protected final Log logger = LogFactory.getLog(GemsCandidateImmigrationService.class);

	@RequestMapping(value = "/recruitment/viewCandidateImmigrationList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewCandidateImmigrationList(HttpServletRequest request) {

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

			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail = new GemsCandidateImmigrationDetail();

			String searchEmpCode = request.getParameter("selectedGemsCandidateMasterId");
			if (searchEmpCode != null && searchEmpCode.isEmpty() == false) {
				gemsCandidateImmigrationDetail.setGemsCandidateMaster(
						recruitementService.getGemsCandidateMaster(Integer.parseInt(searchEmpCode)));
			}

			int totalResults = recruitementService
					.getGemsCandidateImmigrationDetailFilterCount(gemsCandidateImmigrationDetail);
			List<GemsCandidateImmigrationDetail> list = recruitementService.getGemsCandidateImmigrationDetailList(start,
					limit, gemsCandidateImmigrationDetail);

			logger.info("Returned list size" + list.size());

			return getModelMapCandidateImmigrationList(list, totalResults);

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}

	@RequestMapping(value = "/recruitment/saveGemsCandidateImmigration", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveGemsCandidateImmigration(HttpServletRequest request) {
		logger.info("Insert Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail = new GemsCandidateImmigrationDetail();
			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			gemsCandidateImmigrationDetail.setUpdatedBy(loggedInUser.getGemsUserMasterId());
			gemsCandidateImmigrationDetail.setUpdatedOn(todayDate);
			if ((StringUtils.isNotBlank(request.getParameter("selectedGemsCandidateMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedGemsCandidateMasterId")))) {
				gemsCandidateImmigrationDetail.setGemsCandidateMaster(recruitementService.getGemsCandidateMaster(
						Integer.parseInt(request.getParameter("selectedGemsCandidateMasterId"))));
			}
			String id_value = "";
			if ((StringUtils.isNotBlank(request.getParameter("gemsCandidateImmigrationDetailId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsCandidateImmigrationDetailId")))) {
				id_value = request.getParameter("gemsCandidateImmigrationDetailId");
				gemsCandidateImmigrationDetail = recruitementService
						.getGemsCandidateImmigrationDetail(Integer.parseInt(id_value));
			} else {
				gemsCandidateImmigrationDetail.setCreatedOn(todayDate);
				gemsCandidateImmigrationDetail.setCreatedBy(loggedInUser.getCreatedBy());
			}
			String documentNumber = request.getParameter("documentNumber");
			gemsCandidateImmigrationDetail.setDocumentNumber(documentNumber);

			String documentType = request.getParameter("documentType");
			gemsCandidateImmigrationDetail.setDocumentType(documentType);

			String eligibiltyReviewDateString = request.getParameter("eligibiltyReviewDate");
			if ((StringUtils.isNotBlank(eligibiltyReviewDateString))
					|| (StringUtils.isNotEmpty(eligibiltyReviewDateString))) {
				Date eligibiltyReviewDate = formatter.parse(eligibiltyReviewDateString);
				gemsCandidateImmigrationDetail.setEligibiltyReviewDate(eligibiltyReviewDate);
			}

			String expiryDateString = request.getParameter("expiryDate");
			if ((StringUtils.isNotBlank(expiryDateString)) || (StringUtils.isNotEmpty(expiryDateString))) {
				Date expiryDate = formatter.parse(expiryDateString);
				gemsCandidateImmigrationDetail.setExpiryDate(expiryDate);
			}

			int countryMasterId = 0;

			if ((StringUtils.isNotBlank(request.getParameter("selected_country")))
					|| (StringUtils.isNotEmpty(request.getParameter("selected_country")))) {
				try {
					countryMasterId = Integer.parseInt(request.getParameter("selected_country"));

					gemsCandidateImmigrationDetail
							.setGemsCountryMaster(masterService.getGemsCountryMaster(countryMasterId));
				} catch (NumberFormatException ex) {
					// this will be called when the drop down value does not
					// changed
					gemsCandidateImmigrationDetail
							.setGemsCountryMaster(gemsCandidateImmigrationDetail.getGemsCountryMaster());
				}
			}

			String issueDateString = request.getParameter("issuedDate");
			if ((StringUtils.isNotBlank(issueDateString)) || (StringUtils.isNotEmpty(issueDateString))) {
				Date issuedDate = formatter.parse(issueDateString);
				gemsCandidateImmigrationDetail.setIssuedDate(issuedDate);
			}

			String isActive = request.getParameter("activeStatus");
			if ((isActive == "on") || (isActive.equalsIgnoreCase("on"))) {
				gemsCandidateImmigrationDetail.setActiveStatus(1);
			} else {
				gemsCandidateImmigrationDetail.setActiveStatus(0);
			}

			recruitementService.saveGemsCandidateImmigrationDetail(gemsCandidateImmigrationDetail);
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

	@RequestMapping(value = "/recruitment/deleteGemsCandidateImmigration", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteGemsCandidateImmigration(HttpServletRequest request) {

		logger.info("Delete Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		int gemsCandidateImmigrationDetailId = Integer.parseInt(request.getParameter("objectId"));
		try {
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail = recruitementService
					.getGemsCandidateImmigrationDetail(gemsCandidateImmigrationDetailId);
			recruitementService.removeGemsCandidateImmigrationDetail(gemsCandidateImmigrationDetail);
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

	private Map<String, Object> getModelMapCandidateImmigrationList(List<GemsCandidateImmigrationDetail> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsCandidateImmigrationDetail.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsCandidateImmigrationDetail)) {
					return new JSONObject(true);
				}

				GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail = (GemsCandidateImmigrationDetail) bean;
				int selectedGemsCountryMasterId = 0;
				String selected_country = "";
				if (gemsCandidateImmigrationDetail.getGemsCountryMaster() != null) {
					selectedGemsCountryMasterId = gemsCandidateImmigrationDetail.getGemsCountryMaster()
							.getGemsCountryMasterId();
					selected_country = gemsCandidateImmigrationDetail.getGemsCountryMaster().getGemsCountryCode();

				}

				int selectedGemsCandidateMasterId = 0;
				String selectedGemsCandidateMasterName = "";
				if (gemsCandidateImmigrationDetail.getGemsCandidateMaster() != null) {
					selectedGemsCandidateMasterId = gemsCandidateImmigrationDetail.getGemsCandidateMaster()
							.getGemsCandidateMasterId();

					if (gemsCandidateImmigrationDetail.getGemsCandidateMaster().getGemsCandidateFirstName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateImmigrationDetail.getGemsCandidateMaster().getGemsCandidateFirstName();
					}
					if (gemsCandidateImmigrationDetail.getGemsCandidateMaster().getGemsCandidateLastName() != null) {
						selectedGemsCandidateMasterName = selectedGemsCandidateMasterName
								+ gemsCandidateImmigrationDetail.getGemsCandidateMaster().getGemsCandidateLastName();
					}
				}
				String eligibiltyReviewDate = "";
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				if (gemsCandidateImmigrationDetail.getEligibiltyReviewDate() != null) {
					eligibiltyReviewDate = sdf.format(gemsCandidateImmigrationDetail.getEligibiltyReviewDate());
				}
				String expiryDate = "";
				if (gemsCandidateImmigrationDetail.getExpiryDate() != null) {
					expiryDate = sdf.format(gemsCandidateImmigrationDetail.getExpiryDate());
				}
				String issuedDate = "";
				if (gemsCandidateImmigrationDetail.getIssuedDate() != null) {
					issuedDate = sdf.format(gemsCandidateImmigrationDetail.getIssuedDate());
				}

				return new JSONObject()
						.element("gemsCandidateImmigrationDetailId",
								gemsCandidateImmigrationDetail.getGemsCandidateImmigrationDetailId())
						.element("documentNumber", gemsCandidateImmigrationDetail.getDocumentNumber())
						.element("documentType", gemsCandidateImmigrationDetail.getDocumentType())
						.element("eligibiltyReviewDate", eligibiltyReviewDate).element("expiryDate", expiryDate)
						.element("issuedDate", issuedDate)
						.element("selectedGemsCountryMasterId", selectedGemsCandidateMasterId)
						.element("selected_country", selected_country)
						.element("selectedGemsCandidateMasterId", selectedGemsCandidateMasterId)
						.element("selectedGemsCandidateMasterName", selectedGemsCandidateMasterName)
						.element("activeStatus", gemsCandidateImmigrationDetail.getActiveStatus());
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
