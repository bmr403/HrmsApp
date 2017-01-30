package eProject.domain.timesheet;

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

import eProject.domain.employee.GemsEmployeeMaster;

@Entity
@Table(name = "gems_employee_timesheet_header")
public class GemsEmployeeTimeSheetHeader {

	@Id
	@Column(name = "gems_employee_timesheet_header_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gemsEmployeeTimeSheetHeaderId;

	@Column(name = "timesheet_month_year")
	private String timeSheetMonthYear;

	// bi-directional many-to-one association to GemsBusinessUnit
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "timesheet_employee_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	@Column(name = "timesheet_total_hour")
	private double timsheetTotalHours;

	@Column(name = "timesheet_approved_status")
	private String timesheetApprovedStatus;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "active_status")
	private Integer activeStatus;

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

	public Integer getGemsEmployeeTimeSheetHeaderId() {
		return gemsEmployeeTimeSheetHeaderId;
	}

	public void setGemsEmployeeTimeSheetHeaderId(Integer gemsEmployeeTimeSheetHeaderId) {
		this.gemsEmployeeTimeSheetHeaderId = gemsEmployeeTimeSheetHeaderId;
	}

	public String getTimeSheetMonthYear() {
		return timeSheetMonthYear;
	}

	public void setTimeSheetMonthYear(String timeSheetMonthYear) {
		this.timeSheetMonthYear = timeSheetMonthYear;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public double getTimsheetTotalHours() {
		return timsheetTotalHours;
	}

	public void setTimsheetTotalHours(double timsheetTotalHours) {
		this.timsheetTotalHours = timsheetTotalHours;
	}

	public String getTimesheetApprovedStatus() {
		return timesheetApprovedStatus;
	}

	public void setTimesheetApprovedStatus(String timesheetApprovedStatus) {
		this.timesheetApprovedStatus = timesheetApprovedStatus;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
