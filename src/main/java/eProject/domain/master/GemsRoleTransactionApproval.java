package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the transaction_approval_master database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "role_transaction_approval")
public class GemsRoleTransactionApproval implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_transaction_approval_id")
	private int transactionApprovalMasterId;

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

	@Column(name = "approval_level")
	private int approvalLevel;

	// bi-directional many-to-one association to GemsRoleMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private GemsRoleMaster gemsRoleMaster;

	// bi-directional many-to-one association to GemsTransactionApprovalMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "transaction_approval_master_id")
	private GemsTransactionApprovalMaster gemsTransactionApprovalMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	public GemsRoleTransactionApproval() {
	}

	public int getTransactionApprovalMasterId() {
		return transactionApprovalMasterId;
	}

	public void setTransactionApprovalMasterId(int transactionApprovalMasterId) {
		this.transactionApprovalMasterId = transactionApprovalMasterId;
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

	public int getApprovalLevel() {
		return approvalLevel;
	}

	public void setApprovalLevel(int approvalLevel) {
		this.approvalLevel = approvalLevel;
	}

	public GemsRoleMaster getGemsRoleMaster() {
		return gemsRoleMaster;
	}

	public void setGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		this.gemsRoleMaster = gemsRoleMaster;
	}

	public GemsTransactionApprovalMaster getGemsTransactionApprovalMaster() {
		return gemsTransactionApprovalMaster;
	}

	public void setGemsTransactionApprovalMaster(GemsTransactionApprovalMaster gemsTransactionApprovalMaster) {
		this.gemsTransactionApprovalMaster = gemsTransactionApprovalMaster;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

}