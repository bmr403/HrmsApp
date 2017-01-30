package eProject.service.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.workflow.WorkFlowDao;
import eProject.domain.workflow.GemsWorkFlowHeader;
import eProject.domain.workflow.GemsWorkFlowLine;
import eProject.domain.workflow.GemsWorkFlowTransaction;

@Service("workflowService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private WorkFlowDao workFlowDao;

	public WorkflowServiceImpl() {
	}

	public int getGemsWorkFlowHeaderFilterCount(GemsWorkFlowHeader gemsWorkFlowHeader)
	{
		return workFlowDao.getGemsWorkFlowHeaderFilterCount(gemsWorkFlowHeader);
	}
	public List getGemsWorkFlowHeaderList(int start, int limit, GemsWorkFlowHeader gemsWorkFlowHeader)
	{
		return workFlowDao.getGemsWorkFlowHeaderList(start, limit, gemsWorkFlowHeader);
	}
	public void saveGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader)
	{
		workFlowDao.saveGemsWorkFlowHeader(gemsWorkFlowHeader);
	}
	public void removeGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader)
	{
		workFlowDao.removeGemsWorkFlowHeader(gemsWorkFlowHeader);
	}
	public GemsWorkFlowHeader getGemsWorkFlowHeader(Integer Id)
	{
		return workFlowDao.getGemsWorkFlowHeader(Id);
	}
	public GemsWorkFlowHeader getGemsWorkFlowHeaderByTransactionTypeCode(GemsWorkFlowHeader gemsWorkFlowHeader)
	{
		return workFlowDao.getGemsWorkFlowHeaderByTransactionTypeCode(gemsWorkFlowHeader);
	}
	public List getAllGemsWorkFlowLineList(GemsWorkFlowLine gemsWorkFlowLine)
	{
		return workFlowDao.getAllGemsWorkFlowLineList(gemsWorkFlowLine);
	}
	public void saveGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine)
	{
		workFlowDao.saveGemsWorkFlowLine(gemsWorkFlowLine);
	}
	public void removeGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine)
	{
		workFlowDao.removeGemsWorkFlowLine(gemsWorkFlowLine);
	}
	public GemsWorkFlowLine getGemsWorkFlowLine(Integer Id)
	{
		return workFlowDao.getGemsWorkFlowLine(Id);
	}
	public List getAllGemsWorkFlowTransactionList(GemsWorkFlowTransaction gemsWorkFlowTransaction)
	{
		return workFlowDao.getAllGemsWorkFlowTransactionList(gemsWorkFlowTransaction);
	}
	public void saveGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction)
	{
		workFlowDao.saveGemsWorkFlowTransaction(gemsWorkFlowTransaction);
	}
	public void removeGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction)
	{
		workFlowDao.removeGemsWorkFlowTransaction(gemsWorkFlowTransaction);
	}
	public GemsWorkFlowTransaction getGemsWorkFlowTransaction(Integer Id)
	{
		return workFlowDao.getGemsWorkFlowTransaction(Id);
	}
	public GemsWorkFlowTransaction getGemsWorkFlowTransactionByNodeTrx(GemsWorkFlowTransaction gemsWorkFlowTransaction)
	{
		return workFlowDao.getGemsWorkFlowTransactionByNodeTrx(gemsWorkFlowTransaction);
	}
	
	
}
