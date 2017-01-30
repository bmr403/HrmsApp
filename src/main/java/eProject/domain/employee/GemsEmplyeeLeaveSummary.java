/**
 * 
 */
package eProject.domain.employee;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Lazy;

import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
import eProject.domain.master.GemsOrganisation;

/**
 * @author aparna
 *
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_leave_summary")
public class GemsEmplyeeLeaveSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_leave_summary_id")
	private int gemsEmployeeLeaveSummaryId;

	@Column(name = "leave_entitled")
	private double leaveEntitled;

	@Column(name = "leave_scheduled")
	private double leaveScheduled;

	@Column(name = "leave_taken")
	private double leaveTaken;

	@Column(name = "leave_balance")
	private double leaveBalance;

	@Column(name = "lop_days")
	private double lopDays;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

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
	@Column(name = "balance_updated_on")
	private Date balanceUpdatedOn;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leave_period_master_id")
	private GemsLeavePeriodMaster gemsLeavePeriodMaster;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "leave_type_master_id")
	private GemsLeaveTypeMaster gemsLeaveTypeMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsEmplyeeLeaveSummary() {

	}

	public int getGemsEmployeeLeaveSummaryId() {
		return gemsEmployeeLeaveSummaryId;
	}

	public void setGemsEmployeeLeaveSummaryId(int gemsEmployeeLeaveSummaryId) {
		this.gemsEmployeeLeaveSummaryId = gemsEmployeeLeaveSummaryId;
	}

	public double getLeaveEntitled() {
		return leaveEntitled;
	}

	public void setLeaveEntitled(double leaveEntitled) {
		this.leaveEntitled = leaveEntitled;
	}

	public double getLeaveScheduled() {
		return leaveScheduled;
	}

	public void setLeaveScheduled(double leaveScheduled) {
		this.leaveScheduled = leaveScheduled;
	}

	public double getLeaveTaken() {
		return leaveTaken;
	}

	public void setLeaveTaken(double leaveTaken) {
		this.leaveTaken = leaveTaken;
	}

	public double getLeaveBalance() {
		return leaveBalance;
	}

	public void setLeaveBalance(double leaveBalance) {
		this.leaveBalance = leaveBalance;
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

	public GemsLeavePeriodMaster getGemsLeavePeriodMaster() {
		return gemsLeavePeriodMaster;
	}

	public void setGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		this.gemsLeavePeriodMaster = gemsLeavePeriodMaster;
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

	public double getLopDays() {
		return lopDays;
	}

	public void setLopDays(double lopDays) {
		this.lopDays = lopDays;
	}

	public Date getBalanceUpdatedOn() {
		return balanceUpdatedOn;
	}

	public void setBalanceUpdatedOn(Date balanceUpdatedOn) {
		this.balanceUpdatedOn = balanceUpdatedOn;
	}
	
	

}
