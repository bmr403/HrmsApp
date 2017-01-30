package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_country_master database table.
 * 
 */
@Entity
@Table(name = "gems_country_master")
@Lazy(false)
public class GemsCountryMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_COUNTRY_MASTER_ID")
	private int gemsCountryMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "GEMS_COUNTRY_CODE")
	private String gemsCountryCode;

	@Column(name = "GEMS_COUNTRY_DESCRIPTION")
	private String gemsCountryDescription;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsCountryMaster() {

	}

	/**
	 * @return the gemsCountryMasterId
	 */
	public int getGemsCountryMasterId() {
		return gemsCountryMasterId;
	}

	/**
	 * @param gemsCountryMasterId
	 *            the gemsCountryMasterId to set
	 */
	public void setGemsCountryMasterId(int gemsCountryMasterId) {
		this.gemsCountryMasterId = gemsCountryMasterId;
	}

	/**
	 * @return the activeStatus
	 */
	public int getActiveStatus() {
		return activeStatus;
	}

	/**
	 * @param activeStatus
	 *            the activeStatus to set
	 */
	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	/**
	 * @return the createdBy
	 */
	public int getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the gemsCountryCode
	 */
	public String getGemsCountryCode() {
		return gemsCountryCode;
	}

	/**
	 * @param gemsCountryCode
	 *            the gemsCountryCode to set
	 */
	public void setGemsCountryCode(String gemsCountryCode) {
		this.gemsCountryCode = gemsCountryCode;
	}

	/**
	 * @return the gemsCountryDescription
	 */
	public String getGemsCountryDescription() {
		return gemsCountryDescription;
	}

	/**
	 * @param gemsCountryDescription
	 *            the gemsCountryDescription to set
	 */
	public void setGemsCountryDescription(String gemsCountryDescription) {
		this.gemsCountryDescription = gemsCountryDescription;
	}

	/**
	 * @return the updatedBy
	 */
	public int getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the updatedOn
	 */
	public Date getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn
	 *            the updatedOn to set
	 */
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @return the gemsOrganisation
	 */
	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	/**
	 * @param gemsOrganisation
	 *            the gemsOrganisation to set
	 */
	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

}