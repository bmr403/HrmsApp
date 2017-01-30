package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_designation database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_designation")
public class GemsDesignation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_DESIGNATION_ID")
	private int gemsDesignationId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "GEMS_DESIGNATION_CODE")
	private String gemsDesignationCode;

	@Column(name = "GEMS_DESIGNATION_DESCRIPTION")
	private String gemsDesignationDescription;

	@Column(name = "GEMS_DESIGNATION_NAME")
	private String gemsDesignationName;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsDesignation() {
	}

	public int getGemsDesignationId() {
		return this.gemsDesignationId;
	}

	public void setGemsDesignationId(int gemsDesignationId) {
		this.gemsDesignationId = gemsDesignationId;
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

	public String getGemsDesignationCode() {
		return this.gemsDesignationCode;
	}

	public void setGemsDesignationCode(String gemsDesignationCode) {
		this.gemsDesignationCode = gemsDesignationCode;
	}

	public String getGemsDesignationDescription() {
		return this.gemsDesignationDescription;
	}

	public void setGemsDesignationDescription(String gemsDesignationDescription) {
		this.gemsDesignationDescription = gemsDesignationDescription;
	}

	public String getGemsDesignationName() {
		return this.gemsDesignationName;
	}

	public void setGemsDesignationName(String gemsDesignationName) {
		this.gemsDesignationName = gemsDesignationName;
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

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

}