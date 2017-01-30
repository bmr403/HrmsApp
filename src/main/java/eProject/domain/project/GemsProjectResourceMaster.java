package eProject.domain.project;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsOrganisation;

import java.util.Date;

/**
 * The persistent class for the gems_department database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_project_resource")
public class GemsProjectResourceMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_project_resoure_id")
	private int gemsProjectResourceId;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "inactive_from")
	private Date inActiveFrom;

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

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date")
	private Date resourceStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date resourceEndDate;

	@Column(name = "project_billing_rate")
	private BigDecimal projectBillingRate;

	@Column(name = "schedule_effort")
	private int scheduleEffort;

	// bi-directional many-to-one association to GemsBusinessUnit
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_project_master_id")
	private GemsProjectMaster gemsProjectMaster;

	// bi-directional many-to-one association to Project Type Master
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsProjectResourceMaster() {
	}

	public int getGemsProjectResourceId() {
		return gemsProjectResourceId;
	}

	public void setGemsProjectResourceId(int gemsProjectResourceId) {
		this.gemsProjectResourceId = gemsProjectResourceId;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public Date getResourceStartDate() {
		return resourceStartDate;
	}

	public void setResourceStartDate(Date resourceStartDate) {
		this.resourceStartDate = resourceStartDate;
	}

	public Date getResourceEndDate() {
		return resourceEndDate;
	}

	public void setResourceEndDate(Date resourceEndDate) {
		this.resourceEndDate = resourceEndDate;
	}

	public BigDecimal getProjectBillingRate() {
		return projectBillingRate;
	}

	public void setProjectBillingRate(BigDecimal projectBillingRate) {
		this.projectBillingRate = projectBillingRate;
	}

	public int getScheduleEffort() {
		return scheduleEffort;
	}

	public void setScheduleEffort(int scheduleEffort) {
		this.scheduleEffort = scheduleEffort;
	}

	public GemsProjectMaster getGemsProjectMaster() {
		return gemsProjectMaster;
	}

	public void setGemsProjectMaster(GemsProjectMaster gemsProjectMaster) {
		this.gemsProjectMaster = gemsProjectMaster;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public Date getInActiveFrom() {
		return inActiveFrom;
	}

	public void setInActiveFrom(Date inActiveFrom) {
		this.inActiveFrom = inActiveFrom;
	}

}