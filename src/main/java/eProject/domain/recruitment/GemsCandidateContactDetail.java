package eProject.domain.recruitment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import eProject.domain.master.GemsCountryMaster;

/**
 * The persistent class for the gems_candidate_contact_detail database table.
 * 
 */
@Entity
@Table(name = "gems_candidate_contact_detail")
public class GemsCandidateContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gems_candidate_master_id", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "gemsCandidateMaster"))
	private int gemsCandidateMasterId;

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

	@Column(name = "contact_address_city")
	private String contactAddressCity;

	@Column(name = "contact_address_state")
	private String contactAddressState;

	@Column(name = "contact_address_street2")
	private String contactAddressStreet2;

	@Column(name = "contact_address_street1")
	private String contactAddressStreet1;

	@Column(name = "contact_address_zip_code")
	private String contactAddressZipCode;

	@Column(name = "contact_home_phone")
	private String contactHomePhone;

	@Column(name = "contact_personal_email")
	private String contactPersonalEmail;

	@Column(name = "contact_work_mobile")
	private String contactWorkMobile;

	@Column(name = "contact_work_phone")
	private String contactWorkPhone;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contact_address_country")
	private GemsCountryMaster gemsCountryMaster;

	@OneToOne
	@PrimaryKeyJoinColumn
	private GemsCandidateMaster gemsCandidateMaster;

	public GemsCandidateContactDetail() {
	}

	public int getGemsCandidateMasterId() {
		return gemsCandidateMasterId;
	}

	public void setGemsCandidateMasterId(int gemsCandidateMasterId) {
		this.gemsCandidateMasterId = gemsCandidateMasterId;
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

	public String getContactAddressCity() {
		return contactAddressCity;
	}

	public void setContactAddressCity(String contactAddressCity) {
		this.contactAddressCity = contactAddressCity;
	}

	public String getContactAddressState() {
		return contactAddressState;
	}

	public void setContactAddressState(String contactAddressState) {
		this.contactAddressState = contactAddressState;
	}

	public String getContactAddressStreet2() {
		return contactAddressStreet2;
	}

	public void setContactAddressStreet2(String contactAddressStreet2) {
		this.contactAddressStreet2 = contactAddressStreet2;
	}

	public String getContactAddressStreet1() {
		return contactAddressStreet1;
	}

	public void setContactAddressStreet1(String contactAddressStreet1) {
		this.contactAddressStreet1 = contactAddressStreet1;
	}

	public String getContactAddressZipCode() {
		return contactAddressZipCode;
	}

	public void setContactAddressZipCode(String contactAddressZipCode) {
		this.contactAddressZipCode = contactAddressZipCode;
	}

	public String getContactHomePhone() {
		return contactHomePhone;
	}

	public void setContactHomePhone(String contactHomePhone) {
		this.contactHomePhone = contactHomePhone;
	}

	public String getContactPersonalEmail() {
		return contactPersonalEmail;
	}

	public void setContactPersonalEmail(String contactPersonalEmail) {
		this.contactPersonalEmail = contactPersonalEmail;
	}

	public String getContactWorkMobile() {
		return contactWorkMobile;
	}

	public void setContactWorkMobile(String contactWorkMobile) {
		this.contactWorkMobile = contactWorkMobile;
	}

	public String getContactWorkPhone() {
		return contactWorkPhone;
	}

	public void setContactWorkPhone(String contactWorkPhone) {
		this.contactWorkPhone = contactWorkPhone;
	}

	public GemsCountryMaster getGemsCountryMaster() {
		return gemsCountryMaster;
	}

	public void setGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		this.gemsCountryMaster = gemsCountryMaster;
	}

	public GemsCandidateMaster getGemsCandidateMaster() {
		return gemsCandidateMaster;
	}

	public void setGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		this.gemsCandidateMaster = gemsCandidateMaster;
	}

}