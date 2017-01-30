package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsNationalitiesMaster;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsUserMaster;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * The persistent class for the gems_employee_master database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_master")
public class GemsEmployeeMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_master_id")
	private int gemsEmployeeMasterId;

	@Column(name = "employee_code")
	private String employeeCode;

	@Temporal(TemporalType.DATE)
	@Column(name = "employee_dob")
	private Date employeeDob;

	@Column(name = "employee_first_name")
	private String employeeFirstName;

	@Column(name = "employee_gender")
	private String employeeGender;

	@Column(name = "employee_last_name")
	private String employeeLastName;

	@Column(name = "employee_middle_name")
	private String employeeMiddleName;

	@Column(name = "pf_account_number")
	private String pfAccountNumber;

	@Column(name = "employee_location")
	private String employeeLocation;

	@Column(name = "ssn_number")
	private String ssnNumber;

	@Column(name = "pan_card_number")
	private String panCardNumber;

	@Column(name = "marital_status")
	private String maritalStatus;

	@Column(name = "personal_email_id")
	private String personalEmailId;

	@Column(name = "official_email_id")
	private String officialEmailid;

	@Column(name = "personal_contact_number")
	private String personalContactNumber;

	@Column(name = "office_contact_number")
	private String officeContactNumber;

	@Column(name = "emergency_contact_number")
	private String emergencyContactNumber;

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

	/*
	 * @Column(name="user_id") private int gemsUserMasterId;
	 */

	@Column(name = "required_login")
	private int requiredLogin;

	@Column(name = "leave_allocation_status")
	private int leaveAllocationStatus;

	@OneToOne(mappedBy = "gemsEmployeeMaster", cascade = CascadeType.ALL)
	private GemsEmployeeContactDetail gemsEmployeeContactDetail;

	@OneToOne(mappedBy = "gemsEmployeeMaster", cascade = CascadeType.ALL)
	private GemsEmployeeJobDetail employeeJobDetails;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private GemsUserMaster gemsUserMaster;

	// bi-directional many-to-one association to GemsEmploymentStatus. This
	// denotes the current status of employee
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "current_employee_status")
	private GemsEmploymentStatus currentEmployeeStatus;

	// bi-directional many-to-one association to GemsNationalitiesMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_nationality")
	private GemsNationalitiesMaster gemsNationalitiesMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_organisation_id")
	private GemsOrganisation gemsOrganisation;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "reporting_to")
	private GemsEmployeeMaster currentReportingTo;

	@OneToMany(mappedBy = "gemsEmployeeMaster")
	private Set<GemsEmployeeImmigrationDetail> gemsEmployeeImmigrationDetailList;

	public GemsEmployeeMaster() {
	}

	public int getGemsEmployeeMasterId() {
		return this.gemsEmployeeMasterId;
	}

	public void setGemsEmployeeMasterId(int gemsEmployeeMasterId) {
		this.gemsEmployeeMasterId = gemsEmployeeMasterId;
	}

	public Date getEmployeeDob() {
		return this.employeeDob;
	}

	public void setEmployeeDob(Date employeeDob) {
		this.employeeDob = employeeDob;
	}

	public String getEmployeeFirstName() {
		return this.employeeFirstName;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public String getEmployeeGender() {
		return this.employeeGender;
	}

	public void setEmployeeGender(String employeeGender) {
		this.employeeGender = employeeGender;
	}

	public String getEmployeeLastName() {
		return this.employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeMiddleName() {
		return this.employeeMiddleName;
	}

	public void setEmployeeMiddleName(String employeeMiddleName) {
		this.employeeMiddleName = employeeMiddleName;
	}

	public String getPfAccountNumber() {
		return pfAccountNumber;
	}

	public void setPfAccountNumber(String pfAccountNumber) {
		this.pfAccountNumber = pfAccountNumber;
	}

	public String getPanCardNumber() {
		return panCardNumber;
	}

	public void setPanCardNumber(String panCardNumber) {
		this.panCardNumber = panCardNumber;
	}

	public String getMaritalStatus() {
		return this.maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public GemsNationalitiesMaster getGemsNationalitiesMaster() {
		return this.gemsNationalitiesMaster;
	}

	public void setGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster) {
		this.gemsNationalitiesMaster = gemsNationalitiesMaster;
	}

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public GemsUserMaster getGemsUserMaster() {
		return gemsUserMaster;
	}

	public void setGemsUserMaster(GemsUserMaster gemsUserMaster) {
		this.gemsUserMaster = gemsUserMaster;
	}

	public int getRequiredLogin() {
		return requiredLogin;
	}

	public void setRequiredLogin(int requiredLogin) {
		this.requiredLogin = requiredLogin;
	}

	public GemsEmployeeContactDetail getGemsEmployeeContactDetail() {
		return gemsEmployeeContactDetail;
	}

	public void setGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		this.gemsEmployeeContactDetail = gemsEmployeeContactDetail;
	}

	public GemsEmployeeJobDetail getEmployeeJobDetails() {
		return employeeJobDetails;
	}

	public void setEmployeeJobDetails(GemsEmployeeJobDetail employeeJobDetails) {
		this.employeeJobDetails = employeeJobDetails;
	}

	public GemsEmploymentStatus getCurrentEmployeeStatus() {
		return currentEmployeeStatus;
	}

	public void setCurrentEmployeeStatus(GemsEmploymentStatus currentEmployeeStatus) {
		this.currentEmployeeStatus = currentEmployeeStatus;
	}

	public GemsEmployeeMaster getCurrentReportingTo() {
		return currentReportingTo;
	}

	public void setCurrentReportingTo(GemsEmployeeMaster currentReportingTo) {
		this.currentReportingTo = currentReportingTo;
	}

	public String getPersonalEmailId() {
		return personalEmailId;
	}

	public void setPersonalEmailId(String personalEmailId) {
		this.personalEmailId = personalEmailId;
	}

	public String getOfficialEmailid() {
		return officialEmailid;
	}

	public void setOfficialEmailid(String officialEmailid) {
		this.officialEmailid = officialEmailid;
	}

	public String getPersonalContactNumber() {
		return personalContactNumber;
	}

	public void setPersonalContactNumber(String personalContactNumber) {
		this.personalContactNumber = personalContactNumber;
	}

	public String getOfficeContactNumber() {
		return officeContactNumber;
	}

	public void setOfficeContactNumber(String officeContactNumber) {
		this.officeContactNumber = officeContactNumber;
	}

	public String getEmergencyContactNumber() {
		return emergencyContactNumber;
	}

	public void setEmergencyContactNumber(String emergencyContactNumber) {
		this.emergencyContactNumber = emergencyContactNumber;
	}

	public int getLeaveAllocationStatus() {
		return leaveAllocationStatus;
	}

	public void setLeaveAllocationStatus(int leaveAllocationStatus) {
		this.leaveAllocationStatus = leaveAllocationStatus;
	}

	public String getEmployeeLocation() {
		return employeeLocation;
	}

	public void setEmployeeLocation(String employeeLocation) {
		this.employeeLocation = employeeLocation;
	}

	public String getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(String ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public Set<GemsEmployeeImmigrationDetail> getGemsEmployeeImmigrationDetailList() {
		return gemsEmployeeImmigrationDetailList;
	}

	public void setGemsEmployeeImmigrationDetailList(
			Set<GemsEmployeeImmigrationDetail> gemsEmployeeImmigrationDetailList) {
		this.gemsEmployeeImmigrationDetailList = gemsEmployeeImmigrationDetailList;
	}

}