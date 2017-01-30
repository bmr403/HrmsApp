package eProject.domain.leavemanagement;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsOrganisation;

import java.util.Date;

/**
 * The persistent class for the gems_leave_period_master database table.
 * 
 */
@Entity
@Table(name = "gems_leave_period_master")
@Lazy(false)
public class GemsLeavePeriodMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_leave_period_master_id")
	private int gemsLeavePeriodMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "IS_CURRENT")
	private int isCurrent;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "from_date")
	private Date fromDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "to_date")
	private Date toDate;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsLeavePeriodMaster() {
	}

	public int getGemsLeavePeriodMasterId() {
		return this.gemsLeavePeriodMasterId;
	}

	public void setGemsLeavePeriodMasterId(int gemsLeavePeriodMasterId) {
		this.gemsLeavePeriodMasterId = gemsLeavePeriodMasterId;
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

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public int getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(int isCurrent) {
		this.isCurrent = isCurrent;
	}

}