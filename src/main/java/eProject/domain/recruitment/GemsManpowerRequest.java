package eProject.domain.recruitment;

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

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsOrganisation;

@Entity
@Table(name = "gems_manpower_request")
public class GemsManpowerRequest {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_manpower_request_id")
	private int gemsMapowerRequestId;

	@Column(name = "request_type")
	private String requestType;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "request_date")
	private Date requestDate;

	@Column(name = "job_profile", columnDefinition = "LONGTEXT")
	private String jobProfile;

	@Column(name = "number_of_resources")
	private int numberOfResources;

	@Column(name = "job_position")
	private String jobPosition;

	@Column(name = "job_location")
	private String jobLocation;

	@Column(name = "education")
	private String education;

	@Column(name = "min_salary")
	private double minimumSalary;

	@Column(name = "max_salary")
	private double maxSalary;

	@Column(name = "profile_experience_min")
	private double profileExperienceMin;

	@Column(name = "profile_experience_max")
	private double profileExperienceMax;

	@Column(name = "employment_type")
	private String employmentType;

	// bi-directional many-to-one association to GemsDepartment
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "requested_by_department")
	private GemsDepartment gemsDepartment;

	@Temporal(TemporalType.DATE)
	@Column(name = "request_valid_from")
	private Date requestValidFrom;

	@Temporal(TemporalType.DATE)
	@Column(name = "request_valid_to")
	private Date requestValidTo;

	@Column(name = "request_status")
	private String requestStatus;

	@Column(name = "request_approval_status")
	private String requestApprovalStatus;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "job_code")
	private String jobCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	// bi-directional many-to-one association to Project Type Master
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_customer_master_id")
	private GemsCustomerMaster gemsCustomerMaster;

	@Column(name = "total_levels")
	private int totalLevels;

	public int getTotalLevels() {
		return totalLevels;
	}

	public void setTotalLevels(int totalLevels) {
		this.totalLevels = totalLevels;
	}

	public int getGemsMapowerRequestId() {
		return gemsMapowerRequestId;
	}

	public void setGemsMapowerRequestId(int gemsMapowerRequestId) {
		this.gemsMapowerRequestId = gemsMapowerRequestId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
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

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getJobProfile() {
		return jobProfile;
	}

	public void setJobProfile(String jobProfile) {
		this.jobProfile = jobProfile;
	}

	public int getNumberOfResources() {
		return numberOfResources;
	}

	public void setNumberOfResources(int numberOfResources) {
		this.numberOfResources = numberOfResources;
	}

	public String getJobPosition() {
		return jobPosition;
	}

	public void setJobPosition(String jobPosition) {
		this.jobPosition = jobPosition;
	}

	public String getJobLocation() {
		return jobLocation;
	}

	public void setJobLocation(String jobLocation) {
		this.jobLocation = jobLocation;
	}

	public double getMinimumSalary() {
		return minimumSalary;
	}

	public void setMinimumSalary(double minimumSalary) {
		this.minimumSalary = minimumSalary;
	}

	public double getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(double maxSalary) {
		this.maxSalary = maxSalary;
	}

	public double getProfileExperienceMin() {
		return profileExperienceMin;
	}

	public void setProfileExperienceMin(double profileExperienceMin) {
		this.profileExperienceMin = profileExperienceMin;
	}

	public double getProfileExperienceMax() {
		return profileExperienceMax;
	}

	public void setProfileExperienceMax(double profileExperienceMax) {
		this.profileExperienceMax = profileExperienceMax;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public GemsDepartment getGemsDepartment() {
		return gemsDepartment;
	}

	public void setGemsDepartment(GemsDepartment gemsDepartment) {
		this.gemsDepartment = gemsDepartment;
	}

	public Date getRequestValidFrom() {
		return requestValidFrom;
	}

	public void setRequestValidFrom(Date requestValidFrom) {
		this.requestValidFrom = requestValidFrom;
	}

	public Date getRequestValidTo() {
		return requestValidTo;
	}

	public void setRequestValidTo(Date requestValidTo) {
		this.requestValidTo = requestValidTo;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getRequestApprovalStatus() {
		return requestApprovalStatus;
	}

	public void setRequestApprovalStatus(String requestApprovalStatus) {
		this.requestApprovalStatus = requestApprovalStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public GemsCustomerMaster getGemsCustomerMaster() {
		return gemsCustomerMaster;
	}

	public void setGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		this.gemsCustomerMaster = gemsCustomerMaster;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

}
