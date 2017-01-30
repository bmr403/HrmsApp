package eProject.domain.recruitment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eProject.domain.master.GemsEducationMaster;

/**
 * The persistent class for the gems_candidate_education_detail database table.
 * 
 */
@Entity
@Table(name = "gems_candidate_education_detail")
public class GemsCandidateEducationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_candidate_education_detail_id")
	private int gemsCandidateEducationDetailId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "is_course_regular")
	private int isCourseRegular;

	@Column(name = "university_name")
	private String universityName;

	@Temporal(TemporalType.DATE)
	@Column(name = "year_of_pass")
	private Date yearOfPass;

	@Column(name = "year_percentage")
	private String yearPercentage;

	// bi-directional many-to-one association to GemsEducationMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "education_master_id")
	private GemsEducationMaster gemsEducationMaster;

	// bi-directional many-to-one association to GemsCandidateMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_candidate_master_id")
	private GemsCandidateMaster gemsCandidateMaster;

	public GemsCandidateEducationDetail() {
	}

	public int getGemsCandidateEducationDetailId() {
		return gemsCandidateEducationDetailId;
	}

	public void setGemsCandidateEducationDetailId(int gemsCandidateEducationDetailId) {
		this.gemsCandidateEducationDetailId = gemsCandidateEducationDetailId;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public int getIsCourseRegular() {
		return isCourseRegular;
	}

	public void setIsCourseRegular(int isCourseRegular) {
		this.isCourseRegular = isCourseRegular;
	}

	public String getUniversityName() {
		return universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}

	public Date getYearOfPass() {
		return yearOfPass;
	}

	public void setYearOfPass(Date yearOfPass) {
		this.yearOfPass = yearOfPass;
	}

	public String getYearPercentage() {
		return yearPercentage;
	}

	public void setYearPercentage(String yearPercentage) {
		this.yearPercentage = yearPercentage;
	}

	public GemsCandidateMaster getGemsCandidateMaster() {
		return gemsCandidateMaster;
	}

	public void setGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		this.gemsCandidateMaster = gemsCandidateMaster;
	}

	public GemsEducationMaster getGemsEducationMaster() {
		return gemsEducationMaster;
	}

	public void setGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		this.gemsEducationMaster = gemsEducationMaster;
	}

}