package eProject.domain.recruitment;

import java.io.Serializable;
import javax.persistence.*;

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.master.GemsOrganisation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_recruitment_request database table.
 * 
 */
@Entity
@Table(name = "gems_recruitment_request")
public class GemsRecruitmentRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_recruitment_request_id")
	private int gemsRecruitmentRequestId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "current_status")
	private String currentStatus;

	@Column(name = "job_profile_file_name")
	private String jobProfileFileName;

	@Column(name = "request_code")
	private String requestCode;

	@Column(name = "request_description")
	private String requestDescription;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsCustomerMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_customer_master_id")
	private GemsCustomerMaster gemsCustomerMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	public GemsRecruitmentRequest() {
	}

	public int getGemsRecruitmentRequestId() {
		return this.gemsRecruitmentRequestId;
	}

	public void setGemsRecruitmentRequestId(int gemsRecruitmentRequestId) {
		this.gemsRecruitmentRequestId = gemsRecruitmentRequestId;
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

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getJobProfileFileName() {
		return jobProfileFileName;
	}

	public void setJobProfileFileName(String jobProfileFileName) {
		this.jobProfileFileName = jobProfileFileName;
	}

	public String getRequestCode() {
		return this.requestCode;
	}

	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}

	public String getRequestDescription() {
		return this.requestDescription;
	}

	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public GemsCustomerMaster getGemsCustomerMaster() {
		return this.gemsCustomerMaster;
	}

	public void setGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		this.gemsCustomerMaster = gemsCustomerMaster;
	}

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
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

}