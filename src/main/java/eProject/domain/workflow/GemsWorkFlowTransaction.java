package eProject.domain.workflow;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Lazy;

@Entity
@Lazy(false)
@Table(name = "gems_workflow_transaction")
public class GemsWorkFlowTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_workflow_transaction_id")
	private Integer gemsWorkFlowTransactionId;

	@Column(name = "active_status")
	private Integer activeStatus;

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

	@Column(name = "transaction_type_code")
	private String transactionTypeCode;
	
	@Column(name = "transaction_id")
	private Integer transactionId;	

	@Column(name = "node_id")
	private Integer nodeId;

	@Column(name = "node_approver")
	private Integer nodeApprover;
	
	@Column(name = "transaction_status")
	private Integer transactionStatus;

	public Integer getGemsWorkFlowTransactionId() {
		return gemsWorkFlowTransactionId;
	}

	public void setGemsWorkFlowTransactionId(Integer gemsWorkFlowTransactionId) {
		this.gemsWorkFlowTransactionId = gemsWorkFlowTransactionId;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public String getTransactionTypeCode() {
		return transactionTypeCode;
	}

	public void setTransactionTypeCode(String transactionTypeCode) {
		this.transactionTypeCode = transactionTypeCode;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getNodeApprover() {
		return nodeApprover;
	}

	public void setNodeApprover(Integer nodeApprover) {
		this.nodeApprover = nodeApprover;
	}

	public Integer getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(Integer transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	

}
