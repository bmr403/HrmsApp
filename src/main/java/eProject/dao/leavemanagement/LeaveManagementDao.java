/**
 * 
 */
package eProject.dao.leavemanagement;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;

@Repository("leaveManagementDao")
public class LeaveManagementDao extends ErpAbstractDao {

	public LeaveManagementDao() {
		super();
	}

	/*
	 * Gems Leave Period Methods
	 */

	private DetachedCriteria createGemsLeavePeriodMasterCriteria(GemsLeavePeriodMaster gemsLeavePeriodMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsLeavePeriodMaster.class);
		if (gemsLeavePeriodMaster.getIsCurrent() == 1) {
			criteria.add(Restrictions.eq("isCurrent", gemsLeavePeriodMaster.getIsCurrent()));
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsLeavePeriodMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsLeavePeriodMasterFilterCount(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		DetachedCriteria criteria = createGemsLeavePeriodMasterCriteria(gemsLeavePeriodMaster, "");
		return super.getObjectListCount(GemsLeavePeriodMaster.class, criteria);
	}

	public List getGemsLeavePeriodMasterList(int start, int limit, GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		DetachedCriteria criteria = createGemsLeavePeriodMasterCriteria(gemsLeavePeriodMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsLeavePeriodMaster.class, criteria, start, limit);
	}

	public void saveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		super.saveOrUpdate(gemsLeavePeriodMaster);
	}

	public void removeGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		super.delete(gemsLeavePeriodMaster);
	}

	public GemsLeavePeriodMaster getGemsLeavePeriodMaster(Integer Id) {
		return (GemsLeavePeriodMaster) super.find(GemsLeavePeriodMaster.class, Id);
	}

	public GemsLeavePeriodMaster getActiveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {

		DetachedCriteria criteria = createGemsLeavePeriodMasterCriteria(gemsLeavePeriodMaster, "");
		return (GemsLeavePeriodMaster) super.checkUniqueCode(GemsLeaveTypeMaster.class, criteria);
	}

	/*
	 * End of Leave Period Methods
	 */

	/*
	 * Gems Leave Type Methods
	 */

	private DetachedCriteria createGemsLeaveTypeMasterCriteria(GemsLeaveTypeMaster gemsLeaveTypeMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsLeaveTypeMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsLeaveTypeMaster.getLeaveTypeCode() != null) {
				criteria.add(Restrictions.eq("leaveTypeCode",
						"" + gemsLeaveTypeMaster.getLeaveTypeCode().toUpperCase() + ""));
			}
		} else {
			if (gemsLeaveTypeMaster.getLeaveTypeCode() != null) {
				criteria.add(Restrictions.like("leaveTypeCode", "" + gemsLeaveTypeMaster.getLeaveTypeCode() + "%")
						.ignoreCase());
			}
			if (gemsLeaveTypeMaster.getLeaveTypeDescription() != null) {
				criteria.add(Restrictions.like("leaveTypeDescription",
						"" + gemsLeaveTypeMaster.getLeaveTypeDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsLeaveTypeMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsLeaveTypeMasterFilterCount(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		DetachedCriteria criteria = createGemsLeaveTypeMasterCriteria(gemsLeaveTypeMaster, "");
		return super.getObjectListCount(GemsLeaveTypeMaster.class, criteria);
	}

	public List getGemsLeaveTypeMasterList(int start, int limit, GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		DetachedCriteria criteria = createGemsLeaveTypeMasterCriteria(gemsLeaveTypeMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsLeaveTypeMaster.class, criteria, start, limit);
	}

	public void saveGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		super.saveOrUpdate(gemsLeaveTypeMaster);
	}

	public void removeGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		super.delete(gemsLeaveTypeMaster);
	}

	public GemsLeaveTypeMaster getGemsLeaveTypeMaster(Integer Id) {
		return (GemsLeaveTypeMaster) super.find(GemsLeaveTypeMaster.class, Id);
	}

	public GemsLeaveTypeMaster getGemsLeaveTypeMasterByCode(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		DetachedCriteria criteria = createGemsLeaveTypeMasterCriteria(gemsLeaveTypeMaster, "exact");
		return (GemsLeaveTypeMaster) super.checkUniqueCode(GemsLeaveTypeMaster.class, criteria);
	}

	public List getAllLeaveTypeList(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		DetachedCriteria criteria = createGemsLeaveTypeMasterCriteria(gemsLeaveTypeMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(GemsLeaveTypeMaster.class, criteria);
	}

	/*
	 * End of Leave Type Methods
	 */

	/*
	 * Gems Leave Summary Methods
	 */

	private DetachedCriteria createLeaveSummayMasterCriteria(LeaveSummayMaster leaveSummayMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LeaveSummayMaster.class);

		if (leaveSummayMaster.getGemsEmploymentStatus() != null) {
			criteria.add(Restrictions.eq("gemsEmploymentStatus", leaveSummayMaster.getGemsEmploymentStatus()));
		}
		criteria.add(Restrictions.eq("gemsOrganisation", leaveSummayMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getLeaveSummayMasterFilterCount(LeaveSummayMaster leaveSummayMaster) {
		DetachedCriteria criteria = createLeaveSummayMasterCriteria(leaveSummayMaster, "");
		return super.getObjectListCount(LeaveSummayMaster.class, criteria);
	}

	public List getLeaveSummayMasteList(int start, int limit, LeaveSummayMaster leaveSummayMaster) {
		DetachedCriteria criteria = createLeaveSummayMasterCriteria(leaveSummayMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(LeaveSummayMaster.class, criteria, start, limit);
	}

	public List getAllLeaveSummayMasteList(LeaveSummayMaster leaveSummayMaster) {
		DetachedCriteria criteria = createLeaveSummayMasterCriteria(leaveSummayMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(LeaveSummayMaster.class, criteria);
	}

	public void saveLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster) {
		super.saveOrUpdate(leaveSummayMaster);
	}
	
	public void saveBatchLeaveSummary(List leaveSummayMasterList) {
		super.saveAll(leaveSummayMasterList);
	}

	public void removeLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster) {
		super.delete(leaveSummayMaster);
	}

	public LeaveSummayMaster getLeaveSummayMaster(Integer Id) {
		return (LeaveSummayMaster) super.find(LeaveSummayMaster.class, Id);
	}

	/*
	 * End of Leave Summary Methods
	 */

}
