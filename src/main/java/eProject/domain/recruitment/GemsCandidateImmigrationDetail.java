package eProject.domain.recruitment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import eProject.domain.master.GemsCountryMaster;

/**
 * The persistent class for the gems_candidate_immigration_detail database
 * table.
 * 
 */
@Entity
@Table(name = "gems_candidate_immigration_detail")
public class GemsCandidateImmigrationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_candidate_immigration_detail_id")
	private int gemsCandidateImmigrationDetailId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "comments_section")
	private String comments;

	@Column(name = "document_number")
	private String documentNumber;

	@Column(name = "document_type")
	private String documentType;

	@Temporal(TemporalType.DATE)
	@Column(name = "eligibilty_review_date")
	private Date eligibiltyReviewDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "issued_by")
	private GemsCountryMaster gemsCountryMaster;

	@Temporal(TemporalType.DATE)
	@Column(name = "issued_date")
	private Date issuedDate;

	// bi-directional many-to-one association to GemsCandidateMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_candidate_master_id")
	private GemsCandidateMaster gemsCandidateMaster;

	public GemsCandidateImmigrationDetail() {
	}

	public int getGemsCandidateImmigrationDetailId() {
		return gemsCandidateImmigrationDetailId;
	}

	public void setGemsCandidateImmigrationDetailId(int gemsCandidateImmigrationDetailId) {
		this.gemsCandidateImmigrationDetailId = gemsCandidateImmigrationDetailId;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getEligibiltyReviewDate() {
		return eligibiltyReviewDate;
	}

	public void setEligibiltyReviewDate(Date eligibiltyReviewDate) {
		this.eligibiltyReviewDate = eligibiltyReviewDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public GemsCountryMaster getGemsCountryMaster() {
		return gemsCountryMaster;
	}

	public void setGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		this.gemsCountryMaster = gemsCountryMaster;
	}

	public Date getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public GemsCandidateMaster getGemsCandidateMaster() {
		return gemsCandidateMaster;
	}

	public void setGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		this.gemsCandidateMaster = gemsCandidateMaster;
	}

}