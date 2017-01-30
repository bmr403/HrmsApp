package eProject.domain.leavemanagement;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsOrganisation;

import java.util.Date;

/**
 * The persistent class for the gems_leave_type_master database table.
 * 
 */
@Entity
@Table(name = "gems_leave_type_master")
@Lazy(false)
public class GemsLeaveTypeMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_LEAVE_TYPE_MASTER_ID")
	private int gemsLeaveTypeMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "LEAVE_TYPE_CODE")
	private String leaveTypeCode;

	@Column(name = "LEAVE_TYPE_DESCRIPTION")
	private String leaveTypeDescription;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsLeaveTypeMaster() {
	}

	public int getGemsLeaveTypeMasterId() {
		return this.gemsLeaveTypeMasterId;
	}

	public void setGemsLeaveTypeMasterId(int gemsLeaveTypeMasterId) {
		this.gemsLeaveTypeMasterId = gemsLeaveTypeMasterId;
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

	public String getLeaveTypeCode() {
		return this.leaveTypeCode;
	}

	public void setLeaveTypeCode(String leaveTypeCode) {
		this.leaveTypeCode = leaveTypeCode;
	}

	public String getLeaveTypeDescription() {
		return this.leaveTypeDescription;
	}

	public void setLeaveTypeDescription(String leaveTypeDescription) {
		this.leaveTypeDescription = leaveTypeDescription;
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

}