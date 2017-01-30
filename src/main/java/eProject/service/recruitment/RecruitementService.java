package eProject.service.recruitment;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

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

public interface RecruitementService {

	/*
	 * Gems Recruitment Methods
	 */

	public int getGemsRecruitmentRequestFilterCount(GemsRecruitmentRequest gemsRecruitmentRequest);

	public List getGemsRecruitmentRequestList(int start, int limit, GemsRecruitmentRequest gemsRecruitmentRequest);

	public void saveGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest);

	public void removeGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest);

	public GemsRecruitmentRequest getGemsRecruitmentRequest(Integer Id);

	public GemsRecruitmentRequest getGemsRecruitmentRequestByCode(GemsRecruitmentRequest gemsRecruitmentRequest);

	/*
	 * End of Gems Recruitment Master Methods
	 */

	/*
	 * Gems Recruitment Request Line Methods
	 */

	public int getGemsGemsRecruitmentRequestLineFilterCount(GemsRecruitmentRequestLine gemsRecruitmentRequestLine);

	public List getGemsRecruitmentRequestLineList(int start, int limit,
			GemsRecruitmentRequestLine gemsRecruitmentRequestLine);

	public void saveGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine);

	public void removeGemsRecruitmentRequestLine(GemsRecruitmentRequestLine gemsRecruitmentRequestLine);

	public GemsRecruitmentRequestLine getGemsRecruitmentRequestLine(Integer Id);

	/*
	 * End of Recruitment line methods
	 */

	/*
	 * Gems Candidate Master Methods
	 */

	public int getGemsCandidateMasterFilterCount(GemsCandidateMaster gemsCandidateMaster);

	public List getGemsCandidateMasterList(int start, int limit, GemsCandidateMaster gemsCandidateMaster);

	public void saveGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster);

	public void removeGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster);

	public GemsCandidateMaster getGemsCandidateMaster(Integer Id);

	public GemsCandidateMaster getGemsCandidateMasterByCode(GemsCandidateMaster gemsCandidateMaster);

	public String getMaxGemsRecruitmentRequestCode();

	/*
	 * End of Gems Candidate Master Methods
	 */

	/*
	 * GemsRecruitmentRequirementCandidate Methods
	 */

	public int getGemsGemsRecruitmentRequirementCandidateFilterCount(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate);

	public List getGemsRecruitmentRequirementCandidateList(int start, int limit,
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate);

	public void saveGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate);

	public void removeGemsRecruitmentRequirementCandidate(
			GemsRecruitmentRequirementCandidate gemsRecruitmentRequirementCandidate);

	public GemsRecruitmentRequirementCandidate getGemsRecruitmentRequirementCandidate(Integer Id);

	/*
	 * End of GemsRecruitmentRequirementCandidate Methods
	 */

	/*
	 * Gems Candidate Contact Methods
	 */

	public int getGemsCandidateContactDetailFilterCount(GemsCandidateContactDetail gemsCandidateContactDetail);

	public List getGemsCandidateContactDetailList(int start, int limit,
			GemsCandidateContactDetail gemsCandidateContactDetail);

	public void saveGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail);

	public void removeGemsCandidateContactDetail(GemsCandidateContactDetail gemsCandidateContactDetail);

	public GemsCandidateContactDetail getGemsCandidateContactDetail(Integer Id);

	/*
	 * End of Gems Candidate contact Detail Methods
	 */

	/*
	 * Gems Candidate Education Detail Methods
	 */
	public int getGemsCandidateEducationDetailFilterCount(GemsCandidateEducationDetail gemsCandidateEducationDetail);

	public List getGemsCandidateEducationDetailList(int start, int limit,
			GemsCandidateEducationDetail gemsCandidateEducationDetail);

	public void saveGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail);

	public void removeGemsCandidateEducationDetail(GemsCandidateEducationDetail gemsCandidateEducationDetail);

	public GemsCandidateEducationDetail getGemsCandidateEducationDetail(Integer Id);

	/*
	 * End of Gems Candidate Detail Methods
	 */

	/*
	 * Gems Candidate Immigration Methods
	 */

	public int getGemsCandidateImmigrationDetailFilterCount(
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail);

	public List getGemsCandidateImmigrationDetailList(int start, int limit,
			GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail);

	public void saveGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail);

	public void removeGemsCandidateImmigrationDetail(GemsCandidateImmigrationDetail gemsCandidateImmigrationDetail);

	public GemsCandidateImmigrationDetail getGemsCandidateImmigrationDetail(Integer Id);

	/*
	 * End of Gems Candidate Immigration Methods
	 */

	/*
	 * Gems Candiate WorkExp Methods
	 */

	public int getGemsCandidateWorkExpFilterCount(GemsCandidateWorkExp gemsCandidateWorkExp);

	public List getGemsCandidateWorkExpList(int start, int limit, GemsCandidateWorkExp gemsCandidateWorkExp);

	public void saveGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp);

	public void removeGemsCandidateWorkExp(GemsCandidateWorkExp gemsCandidateWorkExp);

	public GemsCandidateWorkExp getGemsCandidateWorkExp(Integer Id);

	/*
	 * End of Gems Candidate Job Detail Methods
	 */

	/*
	 * Gems Candidate Skill Methods
	 */

	public int getGemsCandidateSkillDetailFilterCount(GemsCandidateSkillDetail gemsCandidateSkillDetail);

	public List getGemsCandidateSkillDetailList(int start, int limit,
			GemsCandidateSkillDetail gemsCandidateSkillDetail);

	public void saveGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail);

	public void removeGemsCandidateSkillDetail(GemsCandidateSkillDetail gemsCandidateSkillDetail);

	public GemsCandidateSkillDetail getGemsCandidateSkillDetail(Integer Id);

	/*
	 * End of Gems Candidate Skill Detail Methods
	 */

	/*
	 * Manpower Request Methods
	 */
	public int getGemsManpowerRequestFilterCount(GemsManpowerRequest gemsManpowerRequest);

	public List getGemsManpowerRequestList(int start, int limit, GemsManpowerRequest gemsManpowerRequest);

	public void saveGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest);

	public void removeGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest);

	public GemsManpowerRequest getGemsManpowerRequest(Integer Id);

	public GemsManpowerRequest getGemsManpowerRequestByCode(GemsManpowerRequest gemsManpowerRequest);

	public int getGemsCandidateStatusMasterFilterCount(GemsCandidateStatusMaster gemsCandidateStatusMaster);

	public List getGemsCandidateStatusMasterList(int start, int limit,
			GemsCandidateStatusMaster gemsCandidateStatusMaster);

	public GemsCandidateStatusMaster getGemsCandidateStatusMaster(Integer Id);

	/*
	 * Manpower Request Interview
	 */

	public int getGemsManpowerRequestInterviewFilterCount(GemsManpowerRequestInterview gemsManpowerRequestInterview);

	public List getGemsManpowerRequestInterviewList(int start, int limit,
			GemsManpowerRequestInterview gemsManpowerRequestInterview);

	public void saveGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview);

	public void removeGemsManpowerRequestInterview(GemsManpowerRequestInterview gemsManpowerRequestInterview);

	public GemsManpowerRequestInterview getGemsManpowerRequestInterview(Integer Id);

}
