package eProject.domain.employee;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_emp_bank_detail database table.
 * 
 */
@Entity
@Table(name = "gems_emp_bank_detail")
@Lazy(false)
public class GemsEmpBankDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_emp_bank_detail_id")
	private int gemsEmpBankDetailId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "bank_account_number")
	private String bankAccountNumber;

	@Column(name = "bank_account_routing_no")
	private String bankAccountRoutingNo;

	@Column(name = "bank_account_type")
	private String bankAccountType;

	@Column(name = "bank_name")
	private String bankName;

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

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_emp_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsEmpBankDetail() {
	}

	public int getGemsEmpBankDetailId() {
		return this.gemsEmpBankDetailId;
	}

	public void setGemsEmpBankDetailId(int gemsEmpBankDetailId) {
		this.gemsEmpBankDetailId = gemsEmpBankDetailId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getBankAccountNumber() {
		return this.bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getBankAccountRoutingNo() {
		return this.bankAccountRoutingNo;
	}

	public void setBankAccountRoutingNo(String bankAccountRoutingNo) {
		this.bankAccountRoutingNo = bankAccountRoutingNo;
	}

	public String getBankAccountType() {
		return this.bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

}