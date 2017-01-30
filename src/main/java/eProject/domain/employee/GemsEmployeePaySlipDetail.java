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
@Table(name = "gems_employee_payslip_detail")
public class GemsEmployeePaySlipDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_emp_payslip_detail_id")
	private int gemsEmployeePayslipDetailId;

	@Column(name = "uploaded_file_name")
	private String uploadedFileName;

	@Column(name = "document_content")
	@Lob
	private byte[] documentContent;

	@Column(name = "document_content_type")
	private String documentContentType;

	@Column(name = "payslip_date")
	private String paySlipDate;

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

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeePaySlipDetail() {
	}

	public int getGemsEmployeePayslipDetailId() {
		return gemsEmployeePayslipDetailId;
	}

	public void setGemsEmployeePayslipDetailId(int gemsEmployeePayslipDetailId) {
		this.gemsEmployeePayslipDetailId = gemsEmployeePayslipDetailId;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getPaySlipDate() {
		return paySlipDate;
	}

	public void setPaySlipDate(String paySlipDate) {
		this.paySlipDate = paySlipDate;
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

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
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

	public String getDocumentContentType() {
		return documentContentType;
	}

	public void setDocumentContentType(String documentContentType) {
		this.documentContentType = documentContentType;
	}

}