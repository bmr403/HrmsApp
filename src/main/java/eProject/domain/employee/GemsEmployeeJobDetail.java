package eProject.domain.employee;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsEmploymentStatus;

import java.util.Date;

/**
 * The persistent class for the gems_employee_job_detail database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_job_detail")
public class GemsEmployeeJobDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gems_employee_master_id", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "gemsEmployeeMaster"))
	private int gemsEmployeeMasterId;

	@Column(name = "active_status")
	private int activeStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "joined_date")
	private Date joinedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "contact_start_date")
	private Date contactStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "contract_end_date")
	private Date contractEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "confirmation_date")
	private Date confirmationDate;

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

	// bi-directional many-to-one association to GemsDesignation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "designation_master_id")
	private GemsDesignation gemsDesignation;

	// bi-directional many-to-one association to GemsDepartment
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_emp_department_id")
	private GemsDepartment gemsDepartment;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reporting_to")
	private GemsEmployeeMaster reportingTo;

	@OneToOne
	@PrimaryKeyJoinColumn
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeeJobDetail() {
	}

	public int getGemsEmployeeMasterId() {
		return gemsEmployeeMasterId;
	}

	public void setGemsEmployeeMasterId(int gemsEmployeeMasterId) {
		this.gemsEmployeeMasterId = gemsEmployeeMasterId;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	public Date getContactStartDate() {
		return contactStartDate;
	}

	public void setContactStartDate(Date contactStartDate) {
		this.contactStartDate = contactStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Date getConfirmationDate() {
		return confirmationDate;
	}

	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
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

	public GemsDesignation getGemsDesignation() {
		return gemsDesignation;
	}

	public void setGemsDesignation(GemsDesignation gemsDesignation) {
		this.gemsDesignation = gemsDesignation;
	}

	public GemsDepartment getGemsDepartment() {
		return gemsDepartment;
	}

	public void setGemsDepartment(GemsDepartment gemsDepartment) {
		this.gemsDepartment = gemsDepartment;
	}

	public GemsEmployeeMaster getReportingTo() {
		return reportingTo;
	}

	public void setReportingTo(GemsEmployeeMaster reportingTo) {
		this.reportingTo = reportingTo;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

}