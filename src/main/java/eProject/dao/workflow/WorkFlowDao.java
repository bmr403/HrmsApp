/**
 * 
 */
package eProject.dao.workflow;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
import eProject.domain.workflow.GemsWorkFlowHeader;
import eProject.domain.workflow.GemsWorkFlowLine;
import eProject.domain.workflow.GemsWorkFlowTransaction;


@Repository("WorkFlowDao")
public class WorkFlowDao extends ErpAbstractDao {

	public WorkFlowDao() {
		super();
	}

	/*
	 * Gems WorkFlow Header Methods
	 */

	private DetachedCriteria createGemsWorkFlowHeaderCriteria(GemsWorkFlowHeader gemsWorkFlowHeader,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsWorkFlowHeader.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsWorkFlowHeader.getTransactionTypeCode() != null) {
				criteria.add(Restrictions.eq("transactionTypeCode",
						"" + gemsWorkFlowHeader.getTransactionTypeCode().toUpperCase() + ""));
			}

		} else {
			if (gemsWorkFlowHeader.getTransactionTypeCode() != null) {
				criteria.add(Restrictions.eq("transactionTypeCode", "" + gemsWorkFlowHeader.getTransactionTypeCode() + "%")
						.ignoreCase());
			}
			if (gemsWorkFlowHeader.getWorkFlowType() != null) {
				criteria.add(Restrictions
						.like("workFlowType", "%" + gemsWorkFlowHeader.getWorkFlowType() + "%")
						.ignoreCase());
			}
			if (gemsWorkFlowHeader.getActiveStatus() != null) {
				criteria.add(Restrictions.eq("activeStatus", gemsWorkFlowHeader.getActiveStatus()));
			}

		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsWorkFlowHeader.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsWorkFlowHeaderFilterCount(GemsWorkFlowHeader gemsWorkFlowHeader) {
		DetachedCriteria criteria = createGemsWorkFlowHeaderCriteria(gemsWorkFlowHeader, "");
		return super.getObjectListCount(GemsWorkFlowHeader.class, criteria);
	}

	public List getGemsWorkFlowHeaderList(int start, int limit, GemsWorkFlowHeader gemsWorkFlowHeader) {
		DetachedCriteria criteria = createGemsWorkFlowHeaderCriteria(gemsWorkFlowHeader, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsWorkFlowHeader.class, criteria, start, limit);
	}

	public void saveGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader) {
		super.saveOrUpdate(gemsWorkFlowHeader);
	}

	public void removeGemsWorkFlowHeader(GemsWorkFlowHeader gemsWorkFlowHeader) {
		super.delete(gemsWorkFlowHeader);
	}

	public GemsWorkFlowHeader getGemsWorkFlowHeader(Integer Id) {
		return (GemsWorkFlowHeader) super.find(GemsWorkFlowHeader.class, Id);
	}

	public GemsWorkFlowHeader getGemsWorkFlowHeaderByTransactionTypeCode(GemsWorkFlowHeader gemsWorkFlowHeader) {
		DetachedCriteria criteria = createGemsWorkFlowHeaderCriteria(gemsWorkFlowHeader, "exact");
		return (GemsWorkFlowHeader) super.checkUniqueCode(GemsWorkFlowHeader.class, criteria);
	}

	/*
	 * End of Gems WorkFlow Header
	 */

	
	/*
	 * Gems Workflow Line
	 */

	private DetachedCriteria createGemsWorkFlowLineCriteria(GemsWorkFlowLine gemsWorkFlowLine, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsWorkFlowLine.class);
		if (searchType.equalsIgnoreCase("exact")) {
			

		} else {
			
			if (gemsWorkFlowLine.getGemsWorkFlowHeader() != null) {
				criteria.add(Restrictions.eq("gemsWorkFlowHeader", gemsWorkFlowLine.getGemsWorkFlowHeader()));
			}
		}
		// criteria.add(Restrictions.eq("gemsOrganisation",gemsProjectMaster.getGemsOrganisation()));

		return criteria;

	}

	public List getAllGemsWorkFlowLineList(GemsWorkFlowLine gemsWorkFlowLine) {
		DetachedCriteria criteria = createGemsWorkFlowLineCriteria(gemsWorkFlowLine, "");
		criteria.addOrder(Order.desc("nodeId"));
		return super.getAllObjectList(GemsWorkFlowLine.class, criteria);
	}

	public void saveGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine) {
		super.saveOrUpdate(gemsWorkFlowLine);
	}
	
	
	public void removeGemsWorkFlowLine(GemsWorkFlowLine gemsWorkFlowLine) {
		super.delete(gemsWorkFlowLine);
	}

	public GemsWorkFlowLine getGemsWorkFlowLine(Integer Id) {
		return (GemsWorkFlowLine) super.find(GemsWorkFlowLine.class, Id);
	}

	/*
	 * End of Gems WorkFlow line Methods
	 */

	/*
	 * Gems WorkFlow Transaction
	 */

	private DetachedCriteria createGemsWorkFlowTransactionCriteria(
			GemsWorkFlowTransaction gemsWorkFlowTransaction, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsWorkFlowTransaction.class);
		
		if (gemsWorkFlowTransaction.getTransactionTypeCode() != null) {
			criteria.add(Restrictions.eq("transactionTypeCode", "" + gemsWorkFlowTransaction.getTransactionTypeCode() + "%")
					.ignoreCase());
		}
		
		if (gemsWorkFlowTransaction.getActiveStatus() != null) {
			criteria.add(Restrictions.eq("activeStatus", gemsWorkFlowTransaction.getActiveStatus()));
		}
		
		if (gemsWorkFlowTransaction.getTransactionId() != null) {
			criteria.add(Restrictions.eq("transactionId", gemsWorkFlowTransaction.getTransactionId()));
		}
		
		if (gemsWorkFlowTransaction.getNodeId() != null) {
			criteria.add(Restrictions.eq("nodeId", gemsWorkFlowTransaction.getNodeId()));
		}

		return criteria;

	}

	

	public List getAllGemsWorkFlowTransactionList(GemsWorkFlowTransaction gemsWorkFlowTransaction) {
		DetachedCriteria criteria = createGemsWorkFlowTransactionCriteria(gemsWorkFlowTransaction, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(GemsWorkFlowTransaction.class, criteria);
	}

	public void saveGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction) {
		super.saveOrUpdate(gemsWorkFlowTransaction);
	}
	
	public void removeGemsWorkFlowTransaction(GemsWorkFlowTransaction gemsWorkFlowTransaction) {
		super.delete(gemsWorkFlowTransaction);
	}

	public GemsWorkFlowTransaction getGemsWorkFlowTransaction(Integer Id) {
		return (GemsWorkFlowTransaction) super.find(GemsWorkFlowTransaction.class, Id);
	}

	public GemsWorkFlowTransaction getGemsWorkFlowTransactionByNodeTrx(
			GemsWorkFlowTransaction gemsWorkFlowTransaction) {
		DetachedCriteria criteria = createGemsWorkFlowTransactionCriteria(gemsWorkFlowTransaction, "exact");
		return (GemsWorkFlowTransaction) super.checkUniqueCode(GemsWorkFlowTransaction.class, criteria);
	}

	
}
