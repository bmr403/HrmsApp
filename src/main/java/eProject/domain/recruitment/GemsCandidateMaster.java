package eProject.domain.recruitment;

import java.io.Serializable;
import javax.persistence.*;

import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.master.GemsOrganisation;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_candidate_master database table.
 * 
 */
@Entity
@Table(name = "gems_candidate_master")
public class GemsCandidateMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_candidate_master_id")
	private int gemsCandidateMasterId;

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

	@Column(name = "gems_candidate_code")
	private String gemsCandidateCode;

	@Column(name = "gems_candidate_email")
	private String gemsCandidateEmail;

	@Column(name = "gems_candidate_first_name")
	private String gemsCandidateFirstName;

	@Column(name = "gems_candidate_last_name")
	private String gemsCandidateLastName;

	@Column(name = "gems_candidate_mobile")
	private String gemsCandidateMobile;

	@Column(name = "gems_candidate_phone")
	private String gemsCandidatePhone;

	@Column(name = "gems_candidate_status")
	private String gemsCandidateStatus;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	/*
	 * @OneToOne(mappedBy="gemsCandidateMaster", cascade=CascadeType.ALL)
	 * private GemsCandidateContactDetail gemsCandidateContactDetail;
	 */

	@Column(name = "gems_candidate_profile_reference")
	private String gemsCandidateProfileReference;

	@Column(name = "gems_candidate_referred_by")
	private String gemsCandidateReferredBy;

	@Column(name = "gems_candidate_current_location")
	private String gemsCandidateCurrentLocation;

	@Column(name = "gems_candidate_current_designation")
	private String gemsCandidateCurrentDesignation;

	@Column(name = "gems_candidate_key_skill")
	private String gemsCandidateKeySkill;

	@Column(name = "gems_candidate_experience")
	private Double gemsCandidateExperience;

	@Column(name = "gems_candidate_current_ctc")
	private Double gemsCandidateCurrentCtc;

	@Column(name = "gems_candidate_notice_period")
	private Integer gemsCandidateNoticePeriod;

	@Column(name = "gems_candidate_expected_ctc")
	private Double gemsCandidateExpectedCtc;

	@Column(name = "gems_candidate_resume_file_name")
	private String gemsCandidateResumeFileName;
	
	@Column(name = "document_content_type")
	private String documentContentType;
	
	@Column(name = "document_content")
	@Lob
	private byte[] documentContent;

	@Column(name = "gems_candidate_education")
	private String gemsCandidateEducation;

	public GemsCandidateMaster() {
	}

	public int getGemsCandidateMasterId() {
		return gemsCandidateMasterId;
	}

	public void setGemsCandidateMasterId(int gemsCandidateMasterId) {
		this.gemsCandidateMasterId = gemsCandidateMasterId;
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

	public String getGemsCandidateCode() {
		return gemsCandidateCode;
	}

	public void setGemsCandidateCode(String gemsCandidateCode) {
		this.gemsCandidateCode = gemsCandidateCode;
	}

	public String getGemsCandidateEmail() {
		return gemsCandidateEmail;
	}

	public void setGemsCandidateEmail(String gemsCandidateEmail) {
		this.gemsCandidateEmail = gemsCandidateEmail;
	}

	public String getGemsCandidateFirstName() {
		return gemsCandidateFirstName;
	}

	public void setGemsCandidateFirstName(String gemsCandidateFirstName) {
		this.gemsCandidateFirstName = gemsCandidateFirstName;
	}

	public String getGemsCandidateLastName() {
		return gemsCandidateLastName;
	}

	public void setGemsCandidateLastName(String gemsCandidateLastName) {
		this.gemsCandidateLastName = gemsCandidateLastName;
	}

	public String getGemsCandidateMobile() {
		return gemsCandidateMobile;
	}

	public void setGemsCandidateMobile(String gemsCandidateMobile) {
		this.gemsCandidateMobile = gemsCandidateMobile;
	}

	public String getGemsCandidatePhone() {
		return gemsCandidatePhone;
	}

	public void setGemsCandidatePhone(String gemsCandidatePhone) {
		this.gemsCandidatePhone = gemsCandidatePhone;
	}

	public String getGemsCandidateStatus() {
		return gemsCandidateStatus;
	}

	public void setGemsCandidateStatus(String gemsCandidateStatus) {
		this.gemsCandidateStatus = gemsCandidateStatus;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	/*
	 * public GemsCandidateContactDetail getGemsCandidateContactDetail() {
	 * return gemsCandidateContactDetail; }
	 * 
	 * public void setGemsCandidateContactDetail( GemsCandidateContactDetail
	 * gemsCandidateContactDetail) { this.gemsCandidateContactDetail =
	 * gemsCandidateContactDetail; }
	 */

	public String getGemsCandidateCurrentLocation() {
		return gemsCandidateCurrentLocation;
	}

	public void setGemsCandidateCurrentLocation(String gemsCandidateCurrentLocation) {
		this.gemsCandidateCurrentLocation = gemsCandidateCurrentLocation;
	}

	public String getGemsCandidateKeySkill() {
		return gemsCandidateKeySkill;
	}

	public void setGemsCandidateKeySkill(String gemsCandidateKeySkill) {
		this.gemsCandidateKeySkill = gemsCandidateKeySkill;
	}

	public Double getGemsCandidateExperience() {
		return gemsCandidateExperience;
	}

	public void setGemsCandidateExperience(Double gemsCandidateExperience) {
		this.gemsCandidateExperience = gemsCandidateExperience;
	}

	public Double getGemsCandidateCurrentCtc() {
		return gemsCandidateCurrentCtc;
	}

	public void setGemsCandidateCurrentCtc(Double gemsCandidateCurrentCtc) {
		this.gemsCandidateCurrentCtc = gemsCandidateCurrentCtc;
	}

	public Integer getGemsCandidateNoticePeriod() {
		return gemsCandidateNoticePeriod;
	}

	public void setGemsCandidateNoticePeriod(Integer gemsCandidateNoticePeriod) {
		this.gemsCandidateNoticePeriod = gemsCandidateNoticePeriod;
	}

	public Double getGemsCandidateExpectedCtc() {
		return gemsCandidateExpectedCtc;
	}

	public void setGemsCandidateExpectedCtc(Double gemsCandidateExpectedCtc) {
		this.gemsCandidateExpectedCtc = gemsCandidateExpectedCtc;
	}

	public String getGemsCandidateResumeFileName() {
		return gemsCandidateResumeFileName;
	}

	public void setGemsCandidateResumeFileName(String gemsCandidateResumeFileName) {
		this.gemsCandidateResumeFileName = gemsCandidateResumeFileName;
	}

	public String getGemsCandidateEducation() {
		return gemsCandidateEducation;
	}

	public void setGemsCandidateEducation(String gemsCandidateEducation) {
		this.gemsCandidateEducation = gemsCandidateEducation;
	}

	public String getGemsCandidateCurrentDesignation() {
		return gemsCandidateCurrentDesignation;
	}

	public void setGemsCandidateCurrentDesignation(String gemsCandidateCurrentDesignation) {
		this.gemsCandidateCurrentDesignation = gemsCandidateCurrentDesignation;
	}

	public String getGemsCandidateProfileReference() {
		return gemsCandidateProfileReference;
	}

	public void setGemsCandidateProfileReference(String gemsCandidateProfileReference) {
		this.gemsCandidateProfileReference = gemsCandidateProfileReference;
	}

	public String getGemsCandidateReferredBy() {
		return gemsCandidateReferredBy;
	}

	public void setGemsCandidateReferredBy(String gemsCandidateReferredBy) {
		this.gemsCandidateReferredBy = gemsCandidateReferredBy;
	}

	public String getDocumentContentType() {
		return documentContentType;
	}

	public void setDocumentContentType(String documentContentType) {
		this.documentContentType = documentContentType;
	}

	public byte[] getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(byte[] documentContent) {
		this.documentContent = documentContent;
	}
	
	

}