package eProject.domain.customer;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the gems_customer_contact database table.
 * 
 */
@Entity
@Table(name = "gems_customer_contact")
public class GemsCustomerContact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_customer_contact_id")
	private int gemsCustomerContactId;

	@Column(name = "contact_email")
	private String contactEmail;

	@Column(name = "contact_mobile")
	private String contactMobile;

	@Column(name = "contact_phone")
	private String contactPhone;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "contact_department")
	private String contactDepartment;

	@Column(name = "contact_designation")
	private String contactDesignation;

	@Column(name = "is_primary_contact")
	private int isPrimaryContact;

	@Column(name = "last_name")
	private String lastName;

	// bi-directional many-to-one association to GemsCustomerMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_customer_master_id")
	private GemsCustomerMaster gemsCustomerMaster;

	public GemsCustomerContact() {
	}

	public int getGemsCustomerContactId() {
		return this.gemsCustomerContactId;
	}

	public void setGemsCustomerContactId(int gemsCustomerContactId) {
		this.gemsCustomerContactId = gemsCustomerContactId;
	}

	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactMobile() {
		return this.contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getIsPrimaryContact() {
		return this.isPrimaryContact;
	}

	public void setIsPrimaryContact(int isPrimaryContact) {
		this.isPrimaryContact = isPrimaryContact;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public GemsCustomerMaster getGemsCustomerMaster() {
		return this.gemsCustomerMaster;
	}

	public void setGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		this.gemsCustomerMaster = gemsCustomerMaster;
	}

	public String getContactDepartment() {
		return contactDepartment;
	}

	public void setContactDepartment(String contactDepartment) {
		this.contactDepartment = contactDepartment;
	}

	public String getContactDesignation() {
		return contactDesignation;
	}

	public void setContactDesignation(String contactDesignation) {
		this.contactDesignation = contactDesignation;
	}

}