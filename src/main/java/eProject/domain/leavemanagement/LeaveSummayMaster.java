package eProject.domain.leavemanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsOrganisation;

/**
 * The persistent class for the leave_summay_master database table.
 * 
 */
@Entity
@Table(name = "leave_summay_master")
public class LeaveSummayMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_summay_master_id")
	private int leaveSummayMasterId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "total_days")
	private int totalDays;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsLeaveTypeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_leave_type_master_id")
	private GemsLeaveTypeMaster gemsLeaveTypeMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employment_status_id")
	private GemsEmploymentStatus gemsEmploymentStatus;

	public LeaveSummayMaster() {
	}

	public int getLeaveSummayMasterId() {
		return leaveSummayMasterId;
	}

	public void setLeaveSummayMasterId(int leaveSummayMasterId) {
		this.leaveSummayMasterId = leaveSummayMasterId;
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

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
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

	public GemsLeaveTypeMaster getGemsLeaveTypeMaster() {
		return gemsLeaveTypeMaster;
	}

	public void setGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		this.gemsLeaveTypeMaster = gemsLeaveTypeMaster;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public GemsEmploymentStatus getGemsEmploymentStatus() {
		return gemsEmploymentStatus;
	}

	public void setGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus) {
		this.gemsEmploymentStatus = gemsEmploymentStatus;
	}

}