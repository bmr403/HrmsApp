package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_employee_dependent_detail database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_dependent_detail")
public class GemsEmployeeDependentDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_dependent_detail_id")
	private int gemsEmployeeDependentDetailId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "dependent_date_of_birth")
	private Date dependentDateOfBirth;

	@Column(name = "dependent_name")
	private String dependentName;

	@Column(name = "dependent_relationship")
	private String dependentRelationship;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeeDependentDetail() {
	}

	public int getGemsEmployeeDependentDetailId() {
		return this.gemsEmployeeDependentDetailId;
	}

	public void setGemsEmployeeDependentDetailId(int gemsEmployeeDependentDetailId) {
		this.gemsEmployeeDependentDetailId = gemsEmployeeDependentDetailId;
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

	public Date getDependentDateOfBirth() {
		return this.dependentDateOfBirth;
	}

	public void setDependentDateOfBirth(Date dependentDateOfBirth) {
		this.dependentDateOfBirth = dependentDateOfBirth;
	}

	public String getDependentName() {
		return this.dependentName;
	}

	public void setDependentName(String dependentName) {
		this.dependentName = dependentName;
	}

	public String getDependentRelationship() {
		return this.dependentRelationship;
	}

	public void setDependentRelationship(String dependentRelationship) {
		this.dependentRelationship = dependentRelationship;
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

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

}