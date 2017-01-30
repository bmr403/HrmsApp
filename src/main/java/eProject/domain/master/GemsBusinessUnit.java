package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import org.springframework.context.annotation.Lazy;

/**
 * The persistent class for the gems_business_unit database table.
 * 
 */
@Entity
@Table(name = "gems_business_unit")
@Lazy(false)
public class GemsBusinessUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_BUSINESS_UNIT_ID")
	private int gemsBusinessUnitId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "GEMS_BU_CODE")
	private String gemsBuCode;

	@Column(name = "GEMS_BU_DESCRIPTION")
	private String gemsBuDescription;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsBusinessUnit() {

	}

	public int getGemsBusinessUnitId() {
		return this.gemsBusinessUnitId;
	}

	public void setGemsBusinessUnitId(int gemsBusinessUnitId) {
		this.gemsBusinessUnitId = gemsBusinessUnitId;
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

	public String getGemsBuCode() {
		return this.gemsBuCode;
	}

	public void setGemsBuCode(String gemsBuCode) {
		this.gemsBuCode = gemsBuCode;
	}

	public String getGemsBuDescription() {
		return this.gemsBuDescription;
	}

	public void setGemsBuDescription(String gemsBuDescription) {
		this.gemsBuDescription = gemsBuDescription;
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