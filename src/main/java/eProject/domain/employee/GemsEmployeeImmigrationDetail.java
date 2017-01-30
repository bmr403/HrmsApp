package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsCountryMaster;

import java.util.Date;

/**
 * The persistent class for the gems_employee_immigration_detail database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_immigration_detail")
public class GemsEmployeeImmigrationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_immigration_detail_id")
	private int gemsEmployeeImmigrationDetailId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "comments_section")
	private String comments;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "document_number")
	private String documentNumber;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "document_content")
	@Lob
	private byte[] documentContent;

	@Column(name = "uploaded_file_name")
	private String uploadedFileName;

	@Column(name = "document_content_type")
	private String documentContentType;

	@Temporal(TemporalType.DATE)
	@Column(name = "eligibilty_review_date")
	private Date eligibiltyReviewDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "issued_date")
	private Date issuedDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	@Column(name = "issued_by")
	private String issuedBy;

	public GemsEmployeeImmigrationDetail() {
	}

	public int getGemsEmployeeImmigrationDetailId() {
		return this.gemsEmployeeImmigrationDetailId;
	}

	public void setGemsEmployeeImmigrationDetailId(int gemsEmployeeImmigrationDetailId) {
		this.gemsEmployeeImmigrationDetailId = gemsEmployeeImmigrationDetailId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public Date getEligibiltyReviewDate() {
		return this.eligibiltyReviewDate;
	}

	public void setEligibiltyReviewDate(Date eligibiltyReviewDate) {
		this.eligibiltyReviewDate = eligibiltyReviewDate;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Date getIssuedDate() {
		return this.issuedDate;
	}

	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public byte[] getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(byte[] documentContent) {
		this.documentContent = documentContent;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getDocumentContentType() {
		return documentContentType;
	}

	public void setDocumentContentType(String documentContentType) {
		this.documentContentType = documentContentType;
	}

	/*
	 * public String getUploadedFileName() { return uploadedFileName; }
	 * 
	 * public void setUploadedFileName(String uploadedFileName) {
	 * this.uploadedFileName = uploadedFileName; }
	 */

}