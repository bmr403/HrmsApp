package eProject.service.workflow;

import java.util.List;

import eProject.domain.workflow.GemsWorkFlowHeader;
import eProject.domain.workflow.GemsWorkFlowLine;
import eProject.domain.workflow.GemsWorkFlowTransaction;

public interface WorkflowService {

	public int getGemsWorkFlowHeaderFilterCount(GemsWorkFlowHeader gemsWorkFlowHeader);
	public List getGemsWorkFlowHeaderList(int start, int limit, GemsWorkFlowHeader gemsWorkFlowHeader);
	public void saveGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader);
	public void removeGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader);
	public GemsWorkFlowHeader getGemsWorkFlowHeader(Integer Id);
	public GemsWorkFlowHeader getGemsWorkFlowHeaderByTransactionTypeCode(GemsWorkFlowHeader gemsWorkFlowHeader);
	public List getAllGemsWorkFlowLineList(GemsWorkFlowLine gemsWorkFlowLine);
	public void saveGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine);
	public void removeGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine);
	public GemsWorkFlowLine getGemsWorkFlowLine(Integer Id);
	public List getAllGemsWorkFlowTransactionList(GemsWorkFlowTransaction gemsWorkFlowTransaction);
	public void saveGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction);
	public void removeGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction) ;
	public GemsWorkFlowTransaction getGemsWorkFlowTransaction(Integer Id);
	public GemsWorkFlowTransaction getGemsWorkFlowTransactionByNodeTrx(GemsWorkFlowTransaction gemsWorkFlowTransaction);
	
}
