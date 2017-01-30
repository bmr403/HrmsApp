package eProject.domain.employee;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.IndexColumn;

/**
 * The persistent class for the gems_employee_leave_master database table.
 * 
 */
@Entity
@Table(name = "gems_employee_leave_master")
public class GemsEmployeeLeaveMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_leave_id")
	private int gemsEmployeeLeaveId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "address_during_leave")
	private String addressDuringLeave;

	@Column(name = "leave_code")
	private String leaveCode;

	@Column(name = "contact_number")
	private String contactNumber;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "current_status")
	private String currentStatus;

	@Temporal(TemporalType.DATE)
	@Column(name = "gems_employee_compoff_from")
	private Date gemsEmployeeCompOffFrom;

	@Temporal(TemporalType.DATE)
	@Column(name = "gems_employee_compoff_to")
	private Date gemsEmployeeCompOffTo;

	@Temporal(TemporalType.DATE)
	@Column(name = "from_date")
	private Date fromDate;

	@Column(name = "reason_for_leave")
	private String reasonForLeave;

	@Temporal(TemporalType.DATE)
	@Column(name = "to_date")
	private Date toDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "employee_duty_resume_date")
	private Date employeeDutyResumeDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	// bi-directional many-to-one association to GemsEmployeeLeaveSummaryService
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_leave_summary_id")
	private GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary;

	/*
	 * @OneToMany(fetch = FetchType.EAGER, cascade={CascadeType.ALL})
	 * 
	 * @JoinColumn(name="gems_employee_leave_id") private
	 * List<GemsEmployeeLeaveLine> gemsEmployeeLeaveLines;
	 */

	public GemsEmployeeLeaveMaster() {
	}

	public int getGemsEmployeeLeaveId() {
		return gemsEmployeeLeaveId;
	}

	public void setGemsEmployeeLeaveId(int gemsEmployeeLeaveId) {
		this.gemsEmployeeLeaveId = gemsEmployeeLeaveId;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getAddressDuringLeave() {
		return addressDuringLeave;
	}

	public void setAddressDuringLeave(String addressDuringLeave) {
		this.addressDuringLeave = addressDuringLeave;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getReasonForLeave() {
		return reasonForLeave;
	}

	public void setReasonForLeave(String reasonForLeave) {
		this.reasonForLeave = reasonForLeave;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

	public Date getEmployeeDutyResumeDate() {
		return employeeDutyResumeDate;
	}

	public void setEmployeeDutyResumeDate(Date employeeDutyResumeDate) {
		this.employeeDutyResumeDate = employeeDutyResumeDate;
	}

	public String getLeaveCode() {
		return leaveCode;
	}

	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}

	public GemsEmplyeeLeaveSummary getGemsEmplyeeLeaveSummary() {
		return gemsEmplyeeLeaveSummary;
	}

	public void setGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		this.gemsEmplyeeLeaveSummary = gemsEmplyeeLeaveSummary;
	}

	public Date getGemsEmployeeCompOffFrom() {
		return gemsEmployeeCompOffFrom;
	}

	public void setGemsEmployeeCompOffFrom(Date gemsEmployeeCompOffFrom) {
		this.gemsEmployeeCompOffFrom = gemsEmployeeCompOffFrom;
	}

	public Date getGemsEmployeeCompOffTo() {
		return gemsEmployeeCompOffTo;
	}

	public void setGemsEmployeeCompOffTo(Date gemsEmployeeCompOffTo) {
		this.gemsEmployeeCompOffTo = gemsEmployeeCompOffTo;
	}

	/*
	 * public List<GemsEmployeeLeaveLine> getGemsEmployeeLeaveLines() { return
	 * gemsEmployeeLeaveLines; }
	 * 
	 * public void setGemsEmployeeLeaveLines(List<GemsEmployeeLeaveLine>
	 * gemsEmployeeLeaveLines) { this.gemsEmployeeLeaveLines =
	 * gemsEmployeeLeaveLines; }
	 */

}