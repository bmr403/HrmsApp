package eProject.domain.customer;

import java.io.Serializable;

import javax.persistence.*;

import eProject.domain.master.GemsOrganisation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_customer_master database table.
 * 
 */
@Entity
@Table(name = "gems_customer_master")
public class GemsCustomerMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_customer_master_id")
	private int gemsCustomerMasterId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "gems_customer_code")
	private String gemsCustomerCode;

	@Column(name = "gems_customer_description")
	private String gemsCustomerDescription;

	@Column(name = "gems_customer_fax")
	private String gemsCustomerFax;

	@Column(name = "gems_customer_name")
	private String gemsCustomerName;

	@Column(name = "gems_customer_phone")
	private String gemsCustomerPhone;

	@Column(name = "gems_customer_phone1")
	private String gemsCustomerPhone1;

	@Column(name = "gems_customer_web")
	private String gemsCustomerWeb;

	@Column(name = "correspondense_address_street1")
	private String correspondenseAddressStreet1;

	@Column(name = "correspondense_address_street2")
	private String correspondenseAddressStreet2;

	@Column(name = "correspondense_address_city")
	private String correspondenseAddressCity;

	@Column(name = "correspondense_address_state")
	private String correspondenseAddressState;

	@Column(name = "correspondense_address_country")
	private String correspondenseAddressCountry;

	@Column(name = "correspondense_address_zip_code")
	private String correspondenseAddressZipCode;

	@Column(name = "permanent_address_street1")
	private String permanentAddressStreet1;

	@Column(name = "permanent_address_street2")
	private String permanentAddressStreet2;

	@Column(name = "permanent_address_city")
	private String permanentAddressCity;

	@Column(name = "permanent_address_state")
	private String permanentAddressState;

	@Column(name = "permanent_address_country")
	private String permanentAddressCountry;

	@Column(name = "permanent_address_zip_code")
	private String permanentAddressZipCode;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsCustomerMaster() {
	}

	public int getGemsCustomerMasterId() {
		return this.gemsCustomerMasterId;
	}

	public void setGemsCustomerMasterId(int gemsCustomerMasterId) {
		this.gemsCustomerMasterId = gemsCustomerMasterId;
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

	public String getCorrespondenseAddressStreet1() {
		return correspondenseAddressStreet1;
	}

	public void setCorrespondenseAddressStreet1(String correspondenseAddressStreet1) {
		this.correspondenseAddressStreet1 = correspondenseAddressStreet1;
	}

	public String getCorrespondenseAddressStreet2() {
		return correspondenseAddressStreet2;
	}

	public void setCorrespondenseAddressStreet2(String correspondenseAddressStreet2) {
		this.correspondenseAddressStreet2 = correspondenseAddressStreet2;
	}

	public String getCorrespondenseAddressCity() {
		return correspondenseAddressCity;
	}

	public void setCorrespondenseAddressCity(String correspondenseAddressCity) {
		this.correspondenseAddressCity = correspondenseAddressCity;
	}

	public String getCorrespondenseAddressState() {
		return correspondenseAddressState;
	}

	public void setCorrespondenseAddressState(String correspondenseAddressState) {
		this.correspondenseAddressState = correspondenseAddressState;
	}

	public String getCorrespondenseAddressCountry() {
		return correspondenseAddressCountry;
	}

	public void setCorrespondenseAddressCountry(String correspondenseAddressCountry) {
		this.correspondenseAddressCountry = correspondenseAddressCountry;
	}

	public String getCorrespondenseAddressZipCode() {
		return correspondenseAddressZipCode;
	}

	public void setCorrespondenseAddressZipCode(String correspondenseAddressZipCode) {
		this.correspondenseAddressZipCode = correspondenseAddressZipCode;
	}

	public String getPermanentAddressStreet1() {
		return permanentAddressStreet1;
	}

	public void setPermanentAddressStreet1(String permanentAddressStreet1) {
		this.permanentAddressStreet1 = permanentAddressStreet1;
	}

	public String getPermanentAddressStreet2() {
		return permanentAddressStreet2;
	}

	public void setPermanentAddressStreet2(String permanentAddressStreet2) {
		this.permanentAddressStreet2 = permanentAddressStreet2;
	}

	public String getPermanentAddressCity() {
		return permanentAddressCity;
	}

	public void setPermanentAddressCity(String permanentAddressCity) {
		this.permanentAddressCity = permanentAddressCity;
	}

	public String getPermanentAddressState() {
		return permanentAddressState;
	}

	public void setPermanentAddressState(String permanentAddressState) {
		this.permanentAddressState = permanentAddressState;
	}

	public String getPermanentAddressCountry() {
		return permanentAddressCountry;
	}

	public void setPermanentAddressCountry(String permanentAddressCountry) {
		this.permanentAddressCountry = permanentAddressCountry;
	}

	public String getPermanentAddressZipCode() {
		return permanentAddressZipCode;
	}

	public void setPermanentAddressZipCode(String permanentAddressZipCode) {
		this.permanentAddressZipCode = permanentAddressZipCode;
	}

	public String getGemsCustomerCode() {
		return this.gemsCustomerCode;
	}

	public void setGemsCustomerCode(String gemsCustomerCode) {
		this.gemsCustomerCode = gemsCustomerCode;
	}

	public String getGemsCustomerDescription() {
		return this.gemsCustomerDescription;
	}

	public void setGemsCustomerDescription(String gemsCustomerDescription) {
		this.gemsCustomerDescription = gemsCustomerDescription;
	}

	public String getGemsCustomerFax() {
		return this.gemsCustomerFax;
	}

	public void setGemsCustomerFax(String gemsCustomerFax) {
		this.gemsCustomerFax = gemsCustomerFax;
	}

	public String getGemsCustomerName() {
		return this.gemsCustomerName;
	}

	public void setGemsCustomerName(String gemsCustomerName) {
		this.gemsCustomerName = gemsCustomerName;
	}

	public String getGemsCustomerPhone() {
		return this.gemsCustomerPhone;
	}

	public void setGemsCustomerPhone(String gemsCustomerPhone) {
		this.gemsCustomerPhone = gemsCustomerPhone;
	}

	public String getGemsCustomerPhone1() {
		return this.gemsCustomerPhone1;
	}

	public void setGemsCustomerPhone1(String gemsCustomerPhone1) {
		this.gemsCustomerPhone1 = gemsCustomerPhone1;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getGemsCustomerWeb() {
		return gemsCustomerWeb;
	}

	public void setGemsCustomerWeb(String gemsCustomerWeb) {
		this.gemsCustomerWeb = gemsCustomerWeb;
	}

}