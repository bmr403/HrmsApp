package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_employee_emergency_contact database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_employee_emergency_contact")
public class GemsEmployeeEmergencyContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_employee_emergency_contact_id")
	private int gemsEmployeeEmergencyContactId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "contact_home_phone")
	private String contactHomePhone;

	@Column(name = "contact_mobile_no")
	private String contactMobileNo;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "contact_relation_ship")
	private String contactRelationShip;

	@Column(name = "contact_work_phone")
	private String contactWorkPhone;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updates_on")
	private Date updatesOn;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmployeeEmergencyContact() {
	}

	public int getGemsEmployeeEmergencyContactId() {
		return this.gemsEmployeeEmergencyContactId;
	}

	public void setGemsEmployeeEmergencyContactId(int gemsEmployeeEmergencyContactId) {
		this.gemsEmployeeEmergencyContactId = gemsEmployeeEmergencyContactId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getContactHomePhone() {
		return this.contactHomePhone;
	}

	public void setContactHomePhone(String contactHomePhone) {
		this.contactHomePhone = contactHomePhone;
	}

	public String getContactMobileNo() {
		return this.contactMobileNo;
	}

	public void setContactMobileNo(String contactMobileNo) {
		this.contactMobileNo = contactMobileNo;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactRelationShip() {
		return this.contactRelationShip;
	}

	public void setContactRelationShip(String contactRelationShip) {
		this.contactRelationShip = contactRelationShip;
	}

	public String getContactWorkPhone() {
		return this.contactWorkPhone;
	}

	public void setContactWorkPhone(String contactWorkPhone) {
		this.contactWorkPhone = contactWorkPhone;
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

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatesOn() {
		return this.updatesOn;
	}

	public void setUpdatesOn(Date updatesOn) {
		this.updatesOn = updatesOn;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

}