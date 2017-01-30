package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsEducationMaster;

import java.util.Date;

/**
 * The persistent class for the gems_emp_education_detail database table.
 * 
 */
@Entity
@Table(name = "gems_emp_education_detail")
@Lazy(false)
public class GemsEmpEducationDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_education_detail_id")
	private int gemsEmployeeEducationDetailId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "is_course_regular")
	private int isCourseRegular;

	@Column(name = "year_percentage")
	private double yearPercentage;

	@Column(name = "university_name")
	private String universityName;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "year_of_pass")
	private String yearOfPass;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	// bi-directional many-to-one association to GemsEducationMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "education_master_id")
	private GemsEducationMaster gemsEducationMaster;

	public GemsEmpEducationDetail() {
	}

	public int getGemsEmployeeEducationDetailId() {
		return this.gemsEmployeeEducationDetailId;
	}

	public void setGemsEmployeeEducationDetailId(int gemsEmployeeEducationDetailId) {
		this.gemsEmployeeEducationDetailId = gemsEmployeeEducationDetailId;
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

	public int getIsCourseRegular() {
		return this.isCourseRegular;
	}

	public void setIsCourseRegular(int isCourseRegular) {
		this.isCourseRegular = isCourseRegular;
	}

	public double getYearPercentage() {
		return yearPercentage;
	}

	public void setYearPercentage(double yearPercentage) {
		this.yearPercentage = yearPercentage;
	}

	public String getUniversityName() {
		return this.universityName;
	}

	public void setUniversityName(String universityName) {
		this.universityName = universityName;
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

	public String getYearOfPass() {
		return this.yearOfPass;
	}

	public void setYearOfPass(String yearOfPass) {
		this.yearOfPass = yearOfPass;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public GemsEducationMaster getGemsEducationMaster() {
		return this.gemsEducationMaster;
	}

	public void setGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		this.gemsEducationMaster = gemsEducationMaster;
	}

}