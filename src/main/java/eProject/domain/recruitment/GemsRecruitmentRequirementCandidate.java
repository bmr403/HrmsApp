package eProject.domain.recruitment;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the gems_recruitment_requirement_candidate database
 * table.
 * 
 */
@Entity
@Table(name = "gems_recruitment_requirement_candidate")
public class GemsRecruitmentRequirementCandidate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_recruitment_requirement_candidate_id")
	private int gemsRecruitmentRequirementCandidateId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsRecruitmentCandidateMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_candidate_master_id")
	private GemsCandidateMaster gemsCandidateMaster;

	// bi-directional many-to-one association to GemsRecruitmentRequest
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_manpower_request_id")
	private GemsManpowerRequest gemsManpowerRequest;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "interview_status")
	private GemsCandidateStatusMaster gemsCandidateStatusMaster;

	public GemsRecruitmentRequirementCandidate() {
	}

	public int getGemsRecruitmentRequirementCandidateId() {
		return this.gemsRecruitmentRequirementCandidateId;
	}

	public void setGemsRecruitmentRequirementCandidateId(int gemsRecruitmentRequirementCandidateId) {
		this.gemsRecruitmentRequirementCandidateId = gemsRecruitmentRequirementCandidateId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public GemsCandidateStatusMaster getGemsCandidateStatusMaster() {
		return gemsCandidateStatusMaster;
	}

	public void setGemsCandidateStatusMaster(GemsCandidateStatusMaster gemsCandidateStatusMaster) {
		this.gemsCandidateStatusMaster = gemsCandidateStatusMaster;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GemsCandidateMaster getGemsCandidateMaster() {
		return gemsCandidateMaster;
	}

	public void setGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		this.gemsCandidateMaster = gemsCandidateMaster;
	}

	public GemsManpowerRequest getGemsManpowerRequest() {
		return gemsManpowerRequest;
	}

	public void setGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		this.gemsManpowerRequest = gemsManpowerRequest;
	}

}