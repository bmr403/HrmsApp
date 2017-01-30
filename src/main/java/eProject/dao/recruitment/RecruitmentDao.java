/**
 * 
 */
package eProject.dao.recruitment;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;

import eProject.domain.recruitment.GemsCandidateContactDetail;
import eProject.domain.recruitment.GemsCandidateEducationDetail;
import eProject.domain.recruitment.GemsCandidateImmigrationDetail;
import eProject.domain.recruitment.GemsCandidateMaster;
import eProject.domain.recruitment.GemsCandidateSkillDetail;
import eProject.domain.recruitment.GemsCandidateStatusMaster;
import eProject.domain.recruitment.GemsCandidateWorkExp;
import eProject.domain.recruitment.GemsManpowerRequest;
import eProject.domain.recruitment.GemsManpowerRequestInterview;
import eProject.domain.recruitment.GemsRecruitmentRequest;
import eProject.domain.recruitment.GemsRecruitmentRequestLine;
import eProject.domain.recruitment.GemsRecruitmentRequirementCandidate;

@Repository("RecruitmentDao")
public class RecruitmentDao extends ErpAbstractDao {

	public RecruitmentDao() {
		super();
	}

	/*
	 * Gems Recruitment Methods
	 */

	private DetachedCriteria createGemsRecruitmentRequestCriteria(GemsRecruitmentRequest gemsRecruitmentRequest,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsRecruitmentRequest.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsRecruitmentRequest.getRequestCode() != null) {
				criteria.add(Restrictions.eq("requestCode",
						"" + gemsRecruitmentRequest.getRequestCode().toUpperCase() + ""));
			}

		} else {
			if (gemsRecruitmentRequest.getRequestCode() != null) {
				criteria.add(Restrictions.like("requestCode", "%" + gemsRecruitmentRequest.getRequestCode() + "%")
						.ignoreCase());
			}
			if (gemsRecruitmentRequest.getRequestDescription() != null) {
				criteria.add(Restrictions
						.like("requestDescription", "%" + gemsRecruitmentRequest.getRequestDescription() + "%")
						.ignoreCase());
			}

		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsRecruitmentRequest.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsRecruitmentRequestFilterCount(GemsRecruitmentRequest gemsRecruitmentRequest) {
		DetachedCriteria criteria = createGemsRecruitmentRequestCriteria(gemsRecruitmentRequest, "");
		return super.getObjectListCount(GemsRecruitmentRequest.class, criteria);
	}

	public List getGemsRecruitmentRequestList(int start, int limit, GemsRecruitmentRequest gemsRecruitmentRequest) {
		DetachedCriteria criteria = createGemsRecruitmentRequestCriteria(gemsRecruitmentRequest, "");
		criteria.addOrder(Order.desc("gemsRecruitmentRequestId"));
		return super.getObjectListByRange(GemsRecruitmentRequest.class, criteria, start, limit);
	}

	public void saveGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest) {
		super.saveOrUpdate(gemsRecruitmentRequest);
	}

	public void removeGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest) {
		super.delete(gemsRecruitmentRequest);
	}

	public GemsRecruitmentRequest getGemsRecruitmentRequest(Integer Id) {
		return (GemsRecruitmentRequest) super.find(GemsRecruitmentRequest.class, Id);
	}

	public GemsRecruitmentRequest getGemsRecruitmentRequestByCode(GemsRecruitmentRequest gemsRecruitmentRequest) {
		DetachedCriteria criteria = createGemsRecruitmentRequestCriteria(gemsRecruitmentRequest, "exact");
		return (GemsRecruitmentRequest) super.checkUniqueCode(GemsRecruitmentRequest.class, criteria);
	}

	public String getMaxGemsRecruitmentRequestCode() {
		String candidateMaxCode = (String) super.getMaxCode(GemsCandidateMaster.class, "gemsCandidateCode");
		return candidateMaxCode;
	}

	/*
	 * End of Gems Recruitment Master Methods
	 */

	/*
	 * Gems Recruitment Request Line Methods
	 */

	private DetachedCriteria createGemsRecruitmentRequestLineCriteria(
			GemsRecruitmentRequestLine gemsRecruitmentRequestLine, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsRecruitmentRequestLine.class);
		criteria.add(Restrictions.eq("gemsRecruitmentRequest", gemsRecruitmentRequestLine.getGemsRecruitmentRequest()));

		return criteria;

	}

	public int getGemsGemsRecruitmentRequestLineFilterCount(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		DetachedCriteria criteria = createGemsRecruitmentRequestLineCriteria(gemsRecruitmentRequestLine, "");
		return super.getObjectListCount(GemsRecruitmentRequestLine.class, criteria);
	}

	public List getGemsRecruitmentRequestLineList(int start, int limit,
			GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		DetachedCriteria criteria = createGemsRecruitmentRequestLineCriteria(gemsRecruitmentRequestLine, "");
		return super.getObjectListByRange(GemsRecruitmentRequestLine.class, criteria, start, limit);
	}

	public void saveGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		super.saveOrUpdate(gemsRecruitmentRequestLine);
	}

	public void removeGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		super.delete(gemsRecruitmentRequestLine);
	}

	public GemsRecruitmentRequestLine getGemsRecruitmentRequestLine(Integer Id) {
		return (GemsRecruitmentRequestLine) super.find(GemsRecruitmentRequestLine.class, Id);
	}

	/*
	 * End of Recruitment line methods
	 */

	/*
	 * GemsRecruitmentRequirementCandidate Methods
	 */

	private DetachedCriteria createGemsRecruitmentRequirementCandidateCriteria(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsRecruitmentRequirementCandidate.class);
		if (gemsRecruitmentRequirementCandidate.getGemsManpowerRequest() != null) {
			criteria.add(Restrictions.eq("gemsManpowerRequest",
					gemsRecruitmentRequirementCandidate.getGemsManpowerRequest()));
		}
		if (gemsRecruitmentRequirementCandidate.getGemsCandidateMaster() != null) {
			criteria.add(Restrictions.eq("gemsCandidateMaster",
					gemsRecruitmentRequirementCandidate.getGemsCandidateMaster()));
		}

		return criteria;

	}

	public int getGemsGemsRecruitmentRequirementCandidateFilterCount(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		DetachedCriteria criteria = createGemsRecruitmentRequirementCandidateCriteria(
				gemsRecruitmentRequirementCandidate, "");
		return super.getObjectListCount(GemsRecruitmentRequirementCandidate.class, criteria);
	}

	public List getGemsRecruitmentRequirementCandidateList(int start, int limit,
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		DetachedCriteria criteria = createGemsRecruitmentRequirementCandidateCriteria(
				gemsRecruitmentRequirementCandidate, "");
		criteria.addOrder(Order.desc("gemsRecruitmentRequirementCandidateId"));
		return super.getObjectListByRange(GemsRecruitmentRequirementCandidate.class, criteria, start, limit);
	}

	public void saveGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		super.saveOrUpdate(gemsRecruitmentRequirementCandidate);
	}

	public void removeGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		super.delete(gemsRecruitmentRequirementCandidate);
	}

	public GemsRecruitmentRequirementCandidate getGemsRecruitmentRequirementCandidate(Integer Id) {
		return (GemsRecruitmentRequirementCandidate) super.find(GemsRecruitmentRequirementCandidate.class, Id);
	}

	/*
	 * End of GemsRecruitmentRequirementCandidate Methods
	 */

	/*
	 * Gems Candiate Master Methods
	 */

	private DetachedCriteria createGemsCandidateMasterCriteria(GemsCandidateMaster gemsCandidateMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsCandidateMaster.getGemsCandidateCode() != null) {
				criteria.add(Restrictions.eq("gemsCandidateCode",
						"" + gemsCandidateMaster.getGemsCandidateCode().toUpperCase() + ""));
			}

		} else {
			if (gemsCandidateMaster.getGemsCandidateCode() != null) {
				criteria.add(Restrictions
						.like("gemsCandidateCode", "" + gemsCandidateMaster.getGemsCandidateCode() + "%").ignoreCase());
			}
			if (gemsCandidateMaster.getGemsCandidateExperience() != null) {
				criteria.add(
						Restrictions.le("gemsCandidateExperience", gemsCandidateMaster.getGemsCandidateExperience()));
			}
			if (gemsCandidateMaster.getGemsCandidateKeySkill() != null) {
				criteria.add(Restrictions
						.like("gemsCandidateKeySkill", "%" + gemsCandidateMaster.getGemsCandidateKeySkill() + "%")
						.ignoreCase());
			}

		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsCandidateMaster.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsCandidateMasterFilterCount(GemsCandidateMaster gemsCandidateMaster) {
		DetachedCriteria criteria = createGemsCandidateMasterCriteria(gemsCandidateMaster, "");
		return super.getObjectListCount(GemsCandidateMaster.class, criteria);
	}

	public List getGemsCandidateMasterList(int start, int limit, GemsCandidateMaster gemsCandidateMaster) {
		DetachedCriteria criteria = createGemsCandidateMasterCriteria(gemsCandidateMaster, "");
		criteria.addOrder(Order.desc("gemsCandidateMasterId"));
		return super.getObjectListByRange(GemsCandidateMaster.class, criteria, start, limit);
	}

	public void saveGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		super.saveOrUpdate(gemsCandidateMaster);
	}

	public void removeGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		super.delete(gemsCandidateMaster);
	}

	public GemsCandidateMaster getGemsCandidateMaster(Integer Id) {
		return (GemsCandidateMaster) super.find(GemsCandidateMaster.class, Id);
	}

	public GemsCandidateMaster getGemsCandidateMasterByCode(GemsCandidateMaster gemsCandidateMaster) {
		DetachedCriteria criteria = createGemsCandidateMasterCriteria(gemsCandidateMaster, "exact");
		return (GemsCandidateMaster) super.checkUniqueCode(GemsCandidateMaster.class, criteria);
	}

	/*
	 * End of Gems Candidate Master Methods
	 */

	/*
	 * Gems Candidate Contact Methods
	 */

	private DetachedCriteria createGemsCandidateContactDetailCriteria(
			GemsCandidateContactDetail gemsCandidateContactDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateContactDetail.class);
		criteria.add(Restrictions.eq("gemsCandidateMaster", gemsCandidateContactDetail.getGemsCandidateMaster()));
		return criteria;

	}

	public int getGemsCandidateContactDetailFilterCount(GemsCandidateContactDetail gemsCandidateContactDetail) {
		DetachedCriteria criteria = createGemsCandidateContactDetailCriteria(gemsCandidateContactDetail, "");
		return super.getObjectListCount(GemsCandidateContactDetail.class, criteria);
	}

	public List getGemsCandidateContactDetailList(int start, int limit,
			GemsCandidateContactDetail gemsCandidateContactDetail) {
		DetachedCriteria criteria = createGemsCandidateContactDetailCriteria(gemsCandidateContactDetail, "");
		return super.getObjectListByRange(GemsCandidateContactDetail.class, criteria, start, limit);
	}

	public void saveGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail) {
		super.saveOrUpdate(gemsCandidateContactDetail);
	}

	public void removeGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail) {
		super.delete(gemsCandidateContactDetail);
	}

	public GemsCandidateContactDetail getGemsCandidateContactDetail(Integer Id) {
		return (GemsCandidateContactDetail) super.find(GemsCandidateContactDetail.class, Id);
	}

	/*
	 * End of Gems Candidate contact Detail Methods
	 */

	/*
	 * Gems Candidate Education Detail Methods
	 */

	private DetachedCriteria createGemsCandidateEducationDetailCriteria(
			GemsCandidateEducationDetail gemsCandidateEducationDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateEducationDetail.class);
		criteria.add(Restrictions.eq("gemsCandidateMaster", gemsCandidateEducationDetail.getGemsCandidateMaster()));
		return criteria;

	}

	public int getGemsCandidateEducationDetailFilterCount(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		DetachedCriteria criteria = createGemsCandidateEducationDetailCriteria(gemsCandidateEducationDetail, "");
		return super.getObjectListCount(GemsCandidateEducationDetail.class, criteria);
	}

	public List getGemsCandidateEducationDetailList(int start, int limit,
			GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		DetachedCriteria criteria = createGemsCandidateEducationDetailCriteria(gemsCandidateEducationDetail, "");
		return super.getObjectListByRange(GemsCandidateEducationDetail.class, criteria, start, limit);
	}

	public void saveGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		super.saveOrUpdate(gemsCandidateEducationDetail);
	}

	public void removeGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		super.delete(gemsCandidateEducationDetail);
	}

	public GemsCandidateEducationDetail getGemsCandidateEducationDetail(Integer Id) {
		return (GemsCandidateEducationDetail) super.find(GemsCandidateEducationDetail.class, Id);
	}

	/*
	 * End of Gems Candidate Education Detail Methods
	 */

	/*
	 * Gems Candidate Immigration Methods
	 */

	private DetachedCriteria createGemsCandidateImmigrationDetailCriteria(
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateImmigrationDetail.class);
		criteria.add(Restrictions.eq("gemsCandidateMaster", gemsCandidateImmigrationDetail.getGemsCandidateMaster()));
		return criteria;

	}

	public int getGemsCandidateImmigrationDetailFilterCount(
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		DetachedCriteria criteria = createGemsCandidateImmigrationDetailCriteria(gemsCandidateImmigrationDetail, "");
		return super.getObjectListCount(GemsCandidateImmigrationDetail.class, criteria);
	}

	public List getGemsCandidateImmigrationDetailList(int start, int limit,
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		DetachedCriteria criteria = createGemsCandidateImmigrationDetailCriteria(gemsCandidateImmigrationDetail, "");
		return super.getObjectListByRange(GemsCandidateImmigrationDetail.class, criteria, start, limit);
	}

	public void saveGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		super.saveOrUpdate(gemsCandidateImmigrationDetail);
	}

	public void removeGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		super.delete(gemsCandidateImmigrationDetail);
	}

	public GemsCandidateImmigrationDetail getGemsCandidateImmigrationDetail(Integer Id) {
		return (GemsCandidateImmigrationDetail) super.find(GemsCandidateImmigrationDetail.class, Id);
	}

	/*
	 * End of Gems Candidate Immigration Methods
	 */

	/*
	 * Gems Candidate WorkExp Methods
	 */

	private DetachedCriteria createGemsCandidateWorkExpCriteria(GemsCandidateWorkExp gemsCandidateWorkExp,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateWorkExp.class);
		criteria.add(Restrictions.eq("gemsCandidateMaster", gemsCandidateWorkExp.getGemsCandidateMaster()));
		return criteria;

	}

	public int getGemsCandidateWorkExpFilterCount(GemsCandidateWorkExp gemsCandidateWorkExp) {
		DetachedCriteria criteria = createGemsCandidateWorkExpCriteria(gemsCandidateWorkExp, "");
		return super.getObjectListCount(GemsCandidateWorkExp.class, criteria);
	}

	public List getGemsCandidateWorkExpList(int start, int limit, GemsCandidateWorkExp gemsCandidateWorkExp) {
		DetachedCriteria criteria = createGemsCandidateWorkExpCriteria(gemsCandidateWorkExp, "");
		return super.getObjectListByRange(GemsCandidateWorkExp.class, criteria, start, limit);
	}

	public void saveGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp) {
		super.saveOrUpdate(gemsCandidateWorkExp);
	}

	public void removeGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp) {
		super.delete(gemsCandidateWorkExp);
	}

	public GemsCandidateWorkExp getGemsCandidateWorkExp(Integer Id) {
		return (GemsCandidateWorkExp) super.find(GemsCandidateWorkExp.class, Id);
	}

	/*
	 * End of Gems Candidate Job Detail Methods
	 */

	/*
	 * Gems Candidate Skill Methods
	 */

	private DetachedCriteria createGemsCandidateSkillDetailCriteria(GemsCandidateSkillDetail gemsCandidateSkillDetail,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateSkillDetail.class);
		criteria.add(Restrictions.eq("gemsCandidateMaster", gemsCandidateSkillDetail.getGemsCandidateMaster()));
		return criteria;

	}

	public int getGemsCandidateSkillDetailFilterCount(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		DetachedCriteria criteria = createGemsCandidateSkillDetailCriteria(gemsCandidateSkillDetail, "");
		return super.getObjectListCount(GemsCandidateSkillDetail.class, criteria);
	}

	public List getGemsCandidateSkillDetailList(int start, int limit,
			GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		DetachedCriteria criteria = createGemsCandidateSkillDetailCriteria(gemsCandidateSkillDetail, "");
		return super.getObjectListByRange(GemsCandidateSkillDetail.class, criteria, start, limit);
	}

	public void saveGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		super.saveOrUpdate(gemsCandidateSkillDetail);
	}

	public void removeGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		super.delete(gemsCandidateSkillDetail);
	}

	public GemsCandidateSkillDetail getGemsCandidateSkillDetail(Integer Id) {
		return (GemsCandidateSkillDetail) super.find(GemsCandidateSkillDetail.class, Id);
	}

	/*
	 * End of Gems Candidate Skill Detail Methods
	 */

	/*
	 * Manpower Request
	 */

	private DetachedCriteria createGemsManpowerRequestCriteria(GemsManpowerRequest gemsManpowerRequest,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsManpowerRequest.class);
		if (gemsManpowerRequest.getJobCode() != null) {
			criteria.add(Restrictions.like("jobCode", "" + gemsManpowerRequest.getJobCode() + "%").ignoreCase());
		}
		if (gemsManpowerRequest.getRequestType() != null) {
			criteria.add(
					Restrictions.like("requestType", "" + gemsManpowerRequest.getRequestType() + "%").ignoreCase());
		}
		if (gemsManpowerRequest.getGemsCustomerMaster() != null) {
			criteria.createCriteria("gemsCustomerMaster", "gemsCustomerMaster")
					.add(Restrictions
							.like("gemsCustomerMaster.gemsCustomerName",
									"%" + gemsManpowerRequest.getGemsCustomerMaster().getGemsCustomerName() + "%")
							.ignoreCase());
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsManpowerRequest.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsManpowerRequestFilterCount(GemsManpowerRequest gemsManpowerRequest) {
		DetachedCriteria criteria = createGemsManpowerRequestCriteria(gemsManpowerRequest, "");
		return super.getObjectListCount(GemsManpowerRequest.class, criteria);
	}

	public List getGemsManpowerRequestList(int start, int limit, GemsManpowerRequest gemsManpowerRequest) {
		DetachedCriteria criteria = createGemsManpowerRequestCriteria(gemsManpowerRequest, "");
		criteria.addOrder(Order.desc("gemsMapowerRequestId"));
		return super.getObjectListByRange(GemsManpowerRequest.class, criteria, start, limit);
	}

	public void saveGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		super.saveOrUpdate(gemsManpowerRequest);
	}

	public void removeGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		super.delete(gemsManpowerRequest);
	}

	public GemsManpowerRequest getGemsManpowerRequest(Integer Id) {
		return (GemsManpowerRequest) super.find(GemsManpowerRequest.class, Id);
	}

	public GemsManpowerRequest getGemsManpowerRequestByCode(GemsManpowerRequest gemsManpowerRequest) {
		DetachedCriteria criteria = createGemsManpowerRequestCriteria(gemsManpowerRequest, "exact");
		return (GemsManpowerRequest) super.checkUniqueCode(GemsManpowerRequest.class, criteria);
	}

	/*
	 * Gems Candidate status master
	 */
	private DetachedCriteria createGemsCandidateStatusMasterCriteria(
			GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCandidateStatusMaster.class);
		return criteria;

	}

	public int getGemsCandidateStatusMasterFilterCount(GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		DetachedCriteria criteria = createGemsCandidateStatusMasterCriteria(gemsCandidateStatusMaster);
		return super.getObjectListCount(GemsCandidateStatusMaster.class, criteria);
	}

	public List getGemsCandidateStatusMasterList(int start, int limit,
			GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		DetachedCriteria criteria = createGemsCandidateStatusMasterCriteria(gemsCandidateStatusMaster);
		criteria.addOrder(Order.asc("gemsCandidateStatusMasterId"));
		return super.getObjectListByRange(GemsCandidateStatusMaster.class, criteria, start, limit);
	}

	public GemsCandidateStatusMaster getGemsCandidateStatusMaster(Integer Id) {
		return (GemsCandidateStatusMaster) super.find(GemsCandidateStatusMaster.class, Id);
	}

	/*
	 * Manpower Request Interview
	 */

	private DetachedCriteria createGemsManpowerRequestInterviewCriteria(
			GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsManpowerRequestInterview.class);
		if (gemsManpowerRequestInterview.getGemsManpowerRequest() != null) {
			criteria.createCriteria("gemsManpowerRequest", "gemsManpowerRequest")
					.add(Restrictions.eq("gemsManpowerRequest.gemsMapowerRequestId",
							gemsManpowerRequestInterview.getGemsManpowerRequest().getGemsMapowerRequestId()));
		}
		// criteria.add(Restrictions.eq("gemsManpowerRequest",gemsManpowerRequestInterview.getGemsManpowerRequest()));
		return criteria;
	}

	public int getGemsManpowerRequestInterviewFilterCount(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		DetachedCriteria criteria = createGemsManpowerRequestInterviewCriteria(gemsManpowerRequestInterview);
		return super.getObjectListCount(GemsManpowerRequestInterview.class, criteria);
	}

	public List getGemsManpowerRequestInterviewList(int start, int limit,
			GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		DetachedCriteria criteria = createGemsManpowerRequestInterviewCriteria(gemsManpowerRequestInterview);
		criteria.addOrder(Order.desc("gemsMapowerRequestInterviewId"));
		return super.getObjectListByRange(GemsManpowerRequestInterview.class, criteria, start, limit);
	}

	public void saveGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		super.saveOrUpdate(gemsManpowerRequestInterview);
	}

	public void removeGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		super.delete(gemsManpowerRequestInterview);
	}

	public GemsManpowerRequestInterview getGemsManpowerRequestInterview(Integer Id) {
		return (GemsManpowerRequestInterview) super.find(GemsManpowerRequestInterview.class, Id);
	}

}
