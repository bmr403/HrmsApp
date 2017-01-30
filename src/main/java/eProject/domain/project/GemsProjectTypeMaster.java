package eProject.domain.project;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsOrganisation;

import java.util.Date;

/**
 * The persistent class for the gems_department database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "project_type_master")
public class GemsProjectTypeMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_type_master_id")
	private int projectTypeMasterId;

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

	@Column(name = "project_type_code")
	private String projectTypeCode;

	@Column(name = "project_type_description")
	private String projectTypeDescription;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsProjectTypeMaster() {
	}

	public int getProjectTypeMasterId() {
		return projectTypeMasterId;
	}

	public void setProjectTypeMasterId(int projectTypeMasterId) {
		this.projectTypeMasterId = projectTypeMasterId;
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

	public String getProjectTypeCode() {
		return projectTypeCode;
	}

	public void setProjectTypeCode(String projectTypeCode) {
		this.projectTypeCode = projectTypeCode;
	}

	public String getProjectTypeDescription() {
		return projectTypeDescription;
	}

	public void setProjectTypeDescription(String projectTypeDescription) {
		this.projectTypeDescription = projectTypeDescription;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

}