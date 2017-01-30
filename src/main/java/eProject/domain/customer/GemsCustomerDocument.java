package eProject.domain.customer;

import java.sql.Blob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "gems_customer_document")
public class GemsCustomerDocument {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_customer_document_id")
	private int gemsCustomerDocumentId;

	@Column(name = "document_file_name")
	private String documentFileName;

	@Column(name = "document_content_type")
	private String documentContentType;

	/*
	 * @Column(name="document_content")
	 * 
	 * @Lob private Blob documentContent;
	 */

	@Column(name = "document_content")
	@Lob
	private byte[] documentContent;

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
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsCustomerMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_customer_master_id")
	private GemsCustomerMaster gemsCustomerMaster;

	public GemsCustomerDocument() {

	}

	public int getGemsCustomerDocumentId() {
		return gemsCustomerDocumentId;
	}

	public void setGemsCustomerDocumentId(int gemsCustomerDocumentId) {
		this.gemsCustomerDocumentId = gemsCustomerDocumentId;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getDocumentContentType() {
		return documentContentType;
	}

	public void setDocumentContentType(String documentContentType) {
		this.documentContentType = documentContentType;
	}

	/*
	 * public Blob getDocumentContent() { return documentContent; }
	 * 
	 * public void setDocumentContent(Blob documentContent) {
	 * this.documentContent = documentContent; }
	 */

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

	public GemsCustomerMaster getGemsCustomerMaster() {
		return gemsCustomerMaster;
	}

	public void setGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		this.gemsCustomerMaster = gemsCustomerMaster;
	}

	public byte[] getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(byte[] documentContent) {
		this.documentContent = documentContent;
	}

}
