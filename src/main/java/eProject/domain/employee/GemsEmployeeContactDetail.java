package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsCountryMaster;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * The persistent class for the gems_employee_contact_detail database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_contact_detail")
public class GemsEmployeeContactDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gems_employee_master_id", unique = true, nullable = false)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "gemsEmployeeMaster"))
	private int gemsEmployeeMasterId;

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

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	@OneToOne
	@PrimaryKeyJoinColumn
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeeContactDetail() {
	}

	public int getGemsEmployeeMasterId() {
		return gemsEmployeeMasterId;
	}

	public void setGemsEmployeeMasterId(int gemsEmployeeMasterId) {
		this.gemsEmployeeMasterId = gemsEmployeeMasterId;
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

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

}