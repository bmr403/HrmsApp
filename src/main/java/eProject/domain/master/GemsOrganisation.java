package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_organisation database table.
 * 
 */
@Entity
@Table(name = "gems_organisation")
@Lazy(false)
public class GemsOrganisation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_ORG_ID")
	private int gemsOrgId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "AD_ORG_ZIP_CODE")
	private String adOrgZipCode;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "GEMS_ORG_ADDRESS")
	private String gemsOrgAddress;

	@Column(name = "ORGANISATION_CODE")
	private String gemsOrganisationCode;

	@Column(name = "GEMS_ORG_CITY")
	private String gemsOrgCity;

	@Column(name = "GEMS_ORG_EMAIL")
	private String gemsOrgEmail;

	@Column(name = "GEMS_ORG_FAX")
	private String gemsOrgFax;

	@Column(name = "GEMS_ORG_NAME")
	private String gemsOrgName;

	@Column(name = "GEMS_ORG_NOTE")
	private String gemsOrgNote;

	@Column(name = "GEMS_ORG_PHONE")
	private String gemsOrgPhone;

	@Column(name = "GEMS_ORG_PHONE1")
	private String gemsOrgPhone1;

	@Column(name = "GEMS_ORG_REGISTRATION_NO")
	private String gemsOrgRegistrationNo;

	@Column(name = "GEMS_ORG_STATE")
	private String gemsOrgState;

	@Column(name = "GEMS_ORG_TAX_ID")
	private String gemsOrgTaxId;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsCountryMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_COUNTRY")
	private GemsCountryMaster gemsCountryMaster;

	// bi-directional many-to-one association to GemsCountryMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CURRENCY_ID")
	private GemsCurrencyMaster gemsCurrencyMaster;

	public GemsOrganisation() {

	}

	public int getGemsOrgId() {
		return this.gemsOrgId;
	}

	public void setGemsOrgId(int gemsOrgId) {
		this.gemsOrgId = gemsOrgId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getAdOrgZipCode() {
		return this.adOrgZipCode;
	}

	public void setAdOrgZipCode(String adOrgZipCode) {
		this.adOrgZipCode = adOrgZipCode;
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

	public String getGemsOrgAddress() {
		return this.gemsOrgAddress;
	}

	public void setGemsOrgAddress(String gemsOrgAddress) {
		this.gemsOrgAddress = gemsOrgAddress;
	}

	public String getGemsOrgCity() {
		return this.gemsOrgCity;
	}

	public void setGemsOrgCity(String gemsOrgCity) {
		this.gemsOrgCity = gemsOrgCity;
	}

	public String getGemsOrgEmail() {
		return this.gemsOrgEmail;
	}

	public void setGemsOrgEmail(String gemsOrgEmail) {
		this.gemsOrgEmail = gemsOrgEmail;
	}

	public String getGemsOrgFax() {
		return this.gemsOrgFax;
	}

	public void setGemsOrgFax(String gemsOrgFax) {
		this.gemsOrgFax = gemsOrgFax;
	}

	public String getGemsOrgName() {
		return this.gemsOrgName;
	}

	public void setGemsOrgName(String gemsOrgName) {
		this.gemsOrgName = gemsOrgName;
	}

	public String getGemsOrgNote() {
		return this.gemsOrgNote;
	}

	public void setGemsOrgNote(String gemsOrgNote) {
		this.gemsOrgNote = gemsOrgNote;
	}

	public String getGemsOrgPhone() {
		return this.gemsOrgPhone;
	}

	public void setGemsOrgPhone(String gemsOrgPhone) {
		this.gemsOrgPhone = gemsOrgPhone;
	}

	public String getGemsOrgPhone1() {
		return this.gemsOrgPhone1;
	}

	public void setGemsOrgPhone1(String gemsOrgPhone1) {
		this.gemsOrgPhone1 = gemsOrgPhone1;
	}

	public String getGemsOrgRegistrationNo() {
		return this.gemsOrgRegistrationNo;
	}

	public void setGemsOrgRegistrationNo(String gemsOrgRegistrationNo) {
		this.gemsOrgRegistrationNo = gemsOrgRegistrationNo;
	}

	public String getGemsOrgState() {
		return this.gemsOrgState;
	}

	public void setGemsOrgState(String gemsOrgState) {
		this.gemsOrgState = gemsOrgState;
	}

	public String getGemsOrgTaxId() {
		return this.gemsOrgTaxId;
	}

	public void setGemsOrgTaxId(String gemsOrgTaxId) {
		this.gemsOrgTaxId = gemsOrgTaxId;
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

	/**
	 * @return the gemsCountryMaster
	 */
	public GemsCountryMaster getGemsCountryMaster() {
		return gemsCountryMaster;
	}

	/**
	 * @param gemsCountryMaster
	 *            the gemsCountryMaster to set
	 */
	public void setGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		this.gemsCountryMaster = gemsCountryMaster;
	}

	/**
	 * @return the gemsOrganisationCode
	 */
	public String getGemsOrganisationCode() {
		return gemsOrganisationCode;
	}

	/**
	 * @param gemsOrganisationCode
	 *            the gemsOrganisationCode to set
	 */
	public void setGemsOrganisationCode(String gemsOrganisationCode) {
		this.gemsOrganisationCode = gemsOrganisationCode;
	}

	public GemsCurrencyMaster getGemsCurrencyMaster() {
		return gemsCurrencyMaster;
	}

	public void setGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		this.gemsCurrencyMaster = gemsCurrencyMaster;
	}

}