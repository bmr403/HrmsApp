package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the gems_employee_leave_line database table.
 * 
 */
@Entity
@Table(name = "gems_employee_leave_line")
public class GemsEmployeeLeaveLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_leave_line_id")
	private int gemsEmployeeLeaveLineId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "approved_status")
	private String approvedStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "current_leavel")
	private int currentLeavel;

	@Column(name = "line_comments")
	private String lineComments;

	@Column(name = "line_description")
	private String lineDescription;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsEmployeeLeaveMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_leave_id")
	private GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "approver_employee_id")
	private GemsEmployeeMaster approver;

	public GemsEmployeeLeaveLine() {
	}

	public int getGemsEmployeeLeaveLineId() {
		return this.gemsEmployeeLeaveLineId;
	}

	public void setGemsEmployeeLeaveLineId(int gemsEmployeeLeaveLineId) {
		this.gemsEmployeeLeaveLineId = gemsEmployeeLeaveLineId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getApprovedStatus() {
		return this.approvedStatus;
	}

	public void setApprovedStatus(String approvedStatus) {
		this.approvedStatus = approvedStatus;
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

	public int getCurrentLeavel() {
		return this.currentLeavel;
	}

	public void setCurrentLeavel(int currentLeavel) {
		this.currentLeavel = currentLeavel;
	}

	public String getLineComments() {
		return this.lineComments;
	}

	public void setLineComments(String lineComments) {
		this.lineComments = lineComments;
	}

	public String getLineDescription() {
		return this.lineDescription;
	}

	public void setLineDescription(String lineDescription) {
		this.lineDescription = lineDescription;
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

	public GemsEmployeeLeaveMaster getGemsEmployeeLeaveMaster() {
		return this.gemsEmployeeLeaveMaster;
	}

	public void setGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		this.gemsEmployeeLeaveMaster = gemsEmployeeLeaveMaster;
	}

	public GemsEmployeeMaster getApprover() {
		return approver;
	}

	public void setApprover(GemsEmployeeMaster approver) {
		this.approver = approver;
	}

}