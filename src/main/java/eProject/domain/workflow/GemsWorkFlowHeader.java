package eProject.domain.workflow;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsOrganisation;

/**
 * The persistent class for the gems_department database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_workflow_header")
public class GemsWorkFlowHeader implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_workflow_header_id")
	private Integer gemsWorkFlowHeaderId;

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

	@Column(name = "node_count")
	private Integer nodeCount;

	@Column(name = "workflow_type")
	private String workFlowType;
	
	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_org_id")
	private GemsOrganisation gemsOrganisation;

	public Integer getGemsWorkFlowHeaderId() {
		return gemsWorkFlowHeaderId;
	}

	public void setGemsWorkFlowHeaderId(Integer gemsWorkFlowHeaderId) {
		this.gemsWorkFlowHeaderId = gemsWorkFlowHeaderId;
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

	public Integer getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getWorkFlowType() {
		return workFlowType;
	}

	public void setWorkFlowType(String workFlowType) {
		this.workFlowType = workFlowType;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	

}