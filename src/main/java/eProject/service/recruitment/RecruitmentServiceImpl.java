package eProject.service.recruitment;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.recruitment.RecruitmentDao;

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

@Service("recruitementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RecruitmentServiceImpl implements RecruitementService {

	@Autowired
	private RecruitmentDao recruitmentDao;

	public RecruitmentServiceImpl() {
	}

	/*
	 * Gems Recruitment Methods
	 */

	public int getGemsRecruitmentRequestFilterCount(GemsRecruitmentRequest gemsRecruitmentRequest) {
		return recruitmentDao.getGemsRecruitmentRequestFilterCount(gemsRecruitmentRequest);
	}

	public List getGemsRecruitmentRequestList(int start, int limit, GemsRecruitmentRequest gemsRecruitmentRequest) {
		return recruitmentDao.getGemsRecruitmentRequestList(start, limit, gemsRecruitmentRequest);
	}

	public void saveGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest) {
		recruitmentDao.saveGemsRecruitmentRequest(gemsRecruitmentRequest);
	}

	public void removeGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest) {
		recruitmentDao.removeGemsRecruitmentRequest(gemsRecruitmentRequest);
	}

	public GemsRecruitmentRequest getGemsRecruitmentRequest(Integer Id) {
		return recruitmentDao.getGemsRecruitmentRequest(Id);
	}

	public GemsRecruitmentRequest getGemsRecruitmentRequestByCode(GemsRecruitmentRequest gemsRecruitmentRequest) {
		return recruitmentDao.getGemsRecruitmentRequestByCode(gemsRecruitmentRequest);
	}

	public String getMaxGemsRecruitmentRequestCode() {
		return recruitmentDao.getMaxGemsRecruitmentRequestCode();
	}

	/*
	 * End of Gems Recruitment Master Methods
	 */

	/*
	 * Gems Recruitment Request Line Methods
	 */

	public int getGemsGemsRecruitmentRequestLineFilterCount(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		return recruitmentDao.getGemsGemsRecruitmentRequestLineFilterCount(gemsRecruitmentRequestLine);
	}

	public List getGemsRecruitmentRequestLineList(int start, int limit,
			GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		return recruitmentDao.getGemsRecruitmentRequestLineList(start, limit, gemsRecruitmentRequestLine);
	}

	public void saveGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		recruitmentDao.saveGemsRecruitmentRequestLine(gemsRecruitmentRequestLine);
	}

	public void removeGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine) {
		recruitmentDao.removeGemsRecruitmentRequestLine(gemsRecruitmentRequestLine);
	}

	public GemsRecruitmentRequestLine getGemsRecruitmentRequestLine(Integer Id) {
		return recruitmentDao.getGemsRecruitmentRequestLine(Id);
	}

	/*
	 * End of Recruitment line methods
	 */

	/*
	 * Gems Candidate Master Methods
	 */

	public int getGemsCandidateMasterFilterCount(GemsCandidateMaster gemsCandidateMaster) {
		return recruitmentDao.getGemsCandidateMasterFilterCount(gemsCandidateMaster);
	}

	public List getGemsCandidateMasterList(int start, int limit, GemsCandidateMaster gemsCandidateMaster) {
		return recruitmentDao.getGemsCandidateMasterList(start, limit, gemsCandidateMaster);
	}

	public void saveGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		recruitmentDao.saveGemsCandidateMaster(gemsCandidateMaster);
	}

	public void removeGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		recruitmentDao.removeGemsCandidateMaster(gemsCandidateMaster);
	}

	public GemsCandidateMaster getGemsCandidateMaster(Integer Id) {
		return recruitmentDao.getGemsCandidateMaster(Id);
	}

	public GemsCandidateMaster getGemsCandidateMasterByCode(GemsCandidateMaster gemsCandidateMaster) {
		return recruitmentDao.getGemsCandidateMasterByCode(gemsCandidateMaster);
	}

	/*
	 * End of Gems Candidate Master Methods
	 */

	/*
	 * GemsRecruitmentRequirementCandidate Methods
	 */

	public int getGemsGemsRecruitmentRequirementCandidateFilterCount(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		return recruitmentDao
				.getGemsGemsRecruitmentRequirementCandidateFilterCount(gemsRecruitmentRequirementCandidate);
	}

	public List getGemsRecruitmentRequirementCandidateList(int start, int limit,
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		return recruitmentDao.getGemsRecruitmentRequirementCandidateList(start, limit,
				gemsRecruitmentRequirementCandidate);
	}

	public void saveGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		recruitmentDao.saveGemsRecruitmentRequirementCandidate(gemsRecruitmentRequirementCandidate);
	}

	public void removeGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate) {
		recruitmentDao.removeGemsRecruitmentRequirementCandidate(gemsRecruitmentRequirementCandidate);
	}

	public GemsRecruitmentRequirementCandidate getGemsRecruitmentRequirementCandidate(Integer Id) {
		return recruitmentDao.getGemsRecruitmentRequirementCandidate(Id);
	}

	/*
	 * End of GemsRecruitmentRequirementCandidate Methods
	 */

	/*
	 * Gems Candidate Contact Methods
	 */

	public int getGemsCandidateContactDetailFilterCount(GemsCandidateContactDetail gemsCandidateContactDetail) {
		return recruitmentDao.getGemsCandidateContactDetailFilterCount(gemsCandidateContactDetail);
	}

	public List getGemsCandidateContactDetailList(int start, int limit,
			GemsCandidateContactDetail gemsCandidateContactDetail) {
		return recruitmentDao.getGemsCandidateContactDetailList(start, limit, gemsCandidateContactDetail);
	}

	public void saveGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail) {
		recruitmentDao.saveGemsCandidateContactDetail(gemsCandidateContactDetail);
	}

	public void removeGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail) {
		recruitmentDao.removeGemsCandidateContactDetail(gemsCandidateContactDetail);
	}

	public GemsCandidateContactDetail getGemsCandidateContactDetail(Integer Id) {
		return recruitmentDao.getGemsCandidateContactDetail(Id);
	}

	/*
	 * End of Gems Candidate contact Detail Methods
	 */

	/*
	 * Gems Candidte Education Detail Methods
	 */
	public int getGemsCandidateEducationDetailFilterCount(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		return recruitmentDao.getGemsCandidateEducationDetailFilterCount(gemsCandidateEducationDetail);
	}

	public List getGemsCandidateEducationDetailList(int start, int limit,
			GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		return recruitmentDao.getGemsCandidateEducationDetailList(start, limit, gemsCandidateEducationDetail);
	}

	public void saveGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		recruitmentDao.saveGemsCandidateEducationDetail(gemsCandidateEducationDetail);
	}

	public void removeGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail) {
		recruitmentDao.removeGemsCandidateEducationDetail(gemsCandidateEducationDetail);
	}

	public GemsCandidateEducationDetail getGemsCandidateEducationDetail(Integer Id) {
		return recruitmentDao.getGemsCandidateEducationDetail(Id);
	}

	/*
	 * End of Gems Candidate Detail Methods
	 */

	/*
	 * Gems Candidate Immigration Methods
	 */

	public int getGemsCandidateImmigrationDetailFilterCount(
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		return recruitmentDao.getGemsCandidateImmigrationDetailFilterCount(gemsCandidateImmigrationDetail);
	}

	public List getGemsCandidateImmigrationDetailList(int start, int limit,
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		return recruitmentDao.getGemsCandidateImmigrationDetailList(start, limit, gemsCandidateImmigrationDetail);
	}

	public void saveGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		recruitmentDao.saveGemsCandidateImmigrationDetail(gemsCandidateImmigrationDetail);
	}

	public void removeGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail) {
		recruitmentDao.removeGemsCandidateImmigrationDetail(gemsCandidateImmigrationDetail);
	}

	public GemsCandidateImmigrationDetail getGemsCandidateImmigrationDetail(Integer Id) {
		return recruitmentDao.getGemsCandidateImmigrationDetail(Id);
	}

	/*
	 * End of Gems Candidate Immigration Methods
	 */

	/*
	 * Gems Candidate WorkExp Methods
	 */

	public int getGemsCandidateWorkExpFilterCount(GemsCandidateWorkExp gemsCandidateWorkExp) {
		return recruitmentDao.getGemsCandidateWorkExpFilterCount(gemsCandidateWorkExp);
	}

	public List getGemsCandidateWorkExpList(int start, int limit, GemsCandidateWorkExp gemsCandidateWorkExp) {
		return recruitmentDao.getGemsCandidateWorkExpList(start, limit, gemsCandidateWorkExp);
	}

	public void saveGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp) {
		recruitmentDao.saveGemsCandidateWorkExp(gemsCandidateWorkExp);
	}

	public void removeGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp) {
		recruitmentDao.removeGemsCandidateWorkExp(gemsCandidateWorkExp);
	}

	public GemsCandidateWorkExp getGemsCandidateWorkExp(Integer Id) {
		return recruitmentDao.getGemsCandidateWorkExp(Id);
	}

	/*
	 * End of Gems Candidate Job Detail Methods
	 */

	/*
	 * Gems Candidate Skill Methods
	 */

	public int getGemsCandidateSkillDetailFilterCount(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		return recruitmentDao.getGemsCandidateSkillDetailFilterCount(gemsCandidateSkillDetail);
	}

	public List getGemsCandidateSkillDetailList(int start, int limit,
			GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		return recruitmentDao.getGemsCandidateSkillDetailList(start, limit, gemsCandidateSkillDetail);
	}

	public void saveGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		recruitmentDao.saveGemsCandidateSkillDetail(gemsCandidateSkillDetail);
	}

	public void removeGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail) {
		recruitmentDao.removeGemsCandidateSkillDetail(gemsCandidateSkillDetail);
	}

	public GemsCandidateSkillDetail getGemsCandidateSkillDetail(Integer Id) {
		return recruitmentDao.getGemsCandidateSkillDetail(Id);
	}

	/*
	 * End of Gems Candidate Skill Detail Methods
	 */
	/*
	 * Manpower Request Methods
	 */
	public int getGemsManpowerRequestFilterCount(GemsManpowerRequest gemsManpowerRequest) {
		return recruitmentDao.getGemsManpowerRequestFilterCount(gemsManpowerRequest);
	}

	public List getGemsManpowerRequestList(int start, int limit, GemsManpowerRequest gemsManpowerRequest) {
		return recruitmentDao.getGemsManpowerRequestList(start, limit, gemsManpowerRequest);
	}

	public void saveGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		recruitmentDao.saveGemsManpowerRequest(gemsManpowerRequest);
	}

	public void removeGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		recruitmentDao.removeGemsManpowerRequest(gemsManpowerRequest);
	}

	public GemsManpowerRequest getGemsManpowerRequest(Integer Id) {
		return recruitmentDao.getGemsManpowerRequest(Id);
	}

	public GemsManpowerRequest getGemsManpowerRequestByCode(GemsManpowerRequest gemsManpowerRequest) {
		return recruitmentDao.getGemsManpowerRequestByCode(gemsManpowerRequest);
	}

	public int getGemsCandidateStatusMasterFilterCount(GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		return recruitmentDao.getGemsCandidateStatusMasterFilterCount(gemsCandidateStatusMaster);
	}

	public List getGemsCandidateStatusMasterList(int start, int limit,
			GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		return recruitmentDao.getGemsCandidateStatusMasterList(start, limit, gemsCandidateStatusMaster);
	}

	public GemsCandidateStatusMaster getGemsCandidateStatusMaster(Integer Id) {
		return recruitmentDao.getGemsCandidateStatusMaster(Id);
	}

	/*
	 * Manpower Request Interview
	 */

	public int getGemsManpowerRequestInterviewFilterCount(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		return recruitmentDao.getGemsManpowerRequestInterviewFilterCount(gemsManpowerRequestInterview);
	}

	public List getGemsManpowerRequestInterviewList(int start, int limit,
			GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		return recruitmentDao.getGemsManpowerRequestInterviewList(start, limit, gemsManpowerRequestInterview);
	}

	public void saveGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		recruitmentDao.saveGemsManpowerRequestInterview(gemsManpowerRequestInterview);
	}

	public void removeGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview) {
		recruitmentDao.removeGemsManpowerRequestInterview(gemsManpowerRequestInterview);
	}

	public GemsManpowerRequestInterview getGemsManpowerRequestInterview(Integer Id) {
		return recruitmentDao.getGemsManpowerRequestInterview(Id);
	}

}
