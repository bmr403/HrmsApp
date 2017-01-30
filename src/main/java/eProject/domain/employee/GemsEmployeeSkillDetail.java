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

/**
 * The persistent class for the gems_candidate_education_detail database table.
 * 
 */
@Entity
@Table(name = "gems_employee_skill")
public class GemsEmployeeSkillDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_skill_id")
	private int gemsEmployeeSkillId;

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

	@Column(name = "is_primary_skill")
	private int isPrimarySkill;

	@Column(name = "skill_name")
	private String skillName;

	@Column(name = "version_number")
	private String versionNo;

	@Column(name = "last_used")
	private Integer lastUsed;

	@Column(name = "experiense_in_months")
	private Integer experienseInMonths;

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(Integer lastUsed) {
		this.lastUsed = lastUsed;
	}

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_emp_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeeSkillDetail() {
	}

	public int getGemsEmployeeSkillId() {
		return gemsEmployeeSkillId;
	}

	public void setGemsEmployeeSkillId(int gemsEmployeeSkillId) {
		this.gemsEmployeeSkillId = gemsEmployeeSkillId;
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

	public int getIsPrimarySkill() {
		return isPrimarySkill;
	}

	public void setIsPrimarySkill(int isPrimarySkill) {
		this.isPrimarySkill = isPrimarySkill;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public Integer getExperienseInMonths() {
		return experienseInMonths;
	}

	public void setExperienseInMonths(Integer experienseInMonths) {
		this.experienseInMonths = experienseInMonths;
	}

}