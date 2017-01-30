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


import eProject.domain.master.GemsUserMaster;



/**
 * The persistent class for the gems_department database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_workflow_line")
public class GemsWorkFlowLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_workflow_line_id")
	private Integer gemsWorkFlowLineId;
	
	// bi-directional many-to-one association to Gems Workflow Header
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_workflow_header_id")
	private GemsWorkFlowHeader gemsWorkFlowHeader;

	@Column(name = "node_id")
	private Integer nodeId;
	
	// bi-directional many-to-one association to Gems User Master
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "node_approver")
	private GemsUserMaster gemsUserMaster;

	

	public Integer getGemsWorkFlowLineId() {
		return gemsWorkFlowLineId;
	}

	public void setGemsWorkFlowLineId(Integer gemsWorkFlowLineId) {
		this.gemsWorkFlowLineId = gemsWorkFlowLineId;
	}

	public GemsWorkFlowHeader getGemsWorkFlowHeader() {
		return gemsWorkFlowHeader;
	}

	public void setGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader) {
		this.gemsWorkFlowHeader = gemsWorkFlowHeader;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public GemsUserMaster getGemsUserMaster() {
		return gemsUserMaster;
	}

	public void setGemsUserMaster(GemsUserMaster gemsUserMaster) {
		this.gemsUserMaster = gemsUserMaster;
	}

	

}
