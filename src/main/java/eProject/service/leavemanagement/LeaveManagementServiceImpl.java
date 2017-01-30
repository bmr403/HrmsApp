package eProject.service.leavemanagement;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import eProject.dao.leavemanagement.LeaveManagementDao;
import eProject.dao.master.MasterDao;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
import eProject.domain.leavemanagement.LeaveSummayMaster;
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsEducationMaster;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsNationalitiesMaster;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsPayGrade;

import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.master.GemsWorkShiftMaster;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service("leaveManagementService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class LeaveManagementServiceImpl implements LeaveManagementService {

	@Autowired
	private LeaveManagementDao leaveManagementDao;

	public LeaveManagementServiceImpl() {
	}

	/*
	 * Gems Leave Period Methods
	 */

	public int getGemsLeavePeriodMasterFilterCount(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		return leaveManagementDao.getGemsLeavePeriodMasterFilterCount(gemsLeavePeriodMaster);
	}

	public List getGemsLeavePeriodMasterList(int start, int limit, GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		return leaveManagementDao.getGemsLeavePeriodMasterList(start, limit, gemsLeavePeriodMaster);
	}

	public void saveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		leaveManagementDao.saveGemsLeavePeriodMaster(gemsLeavePeriodMaster);
	}

	public void removeGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		leaveManagementDao.removeGemsLeavePeriodMaster(gemsLeavePeriodMaster);
	}

	public GemsLeavePeriodMaster getGemsLeavePeriodMaster(Integer Id) {
		return leaveManagementDao.getGemsLeavePeriodMaster(Id);
	}

	public GemsLeavePeriodMaster getActiveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster) {
		return leaveManagementDao.getActiveGemsLeavePeriodMaster(gemsLeavePeriodMaster);
	}

	/*
	 * End of Leave Period Methods
	 */

	/*
	 * Gems Leave Type Methods
	 */
	public int getGemsLeaveTypeMasterFilterCount(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		return leaveManagementDao.getGemsLeaveTypeMasterFilterCount(gemsLeaveTypeMaster);
	}

	public List getGemsLeaveTypeMasterList(int start, int limit, GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		return leaveManagementDao.getGemsLeaveTypeMasterList(start, limit, gemsLeaveTypeMaster);
	}

	public void saveGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		leaveManagementDao.saveGemsLeaveTypeMaster(gemsLeaveTypeMaster);
	}

	public void removeGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		leaveManagementDao.removeGemsLeaveTypeMaster(gemsLeaveTypeMaster);
	}

	public GemsLeaveTypeMaster getGemsLeaveTypeMaster(Integer Id) {
		return leaveManagementDao.getGemsLeaveTypeMaster(Id);
	}

	public GemsLeaveTypeMaster getGemsLeaveTypeMasterByCode(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		return leaveManagementDao.getGemsLeaveTypeMasterByCode(gemsLeaveTypeMaster);
	}

	public List getAllLeaveTypeList(GemsLeaveTypeMaster gemsLeaveTypeMaster) {
		return leaveManagementDao.getAllLeaveTypeList(gemsLeaveTypeMaster);
	}

	/*
	 * End of Leave Type Methods
	 */

	/*
	 * Gems Leave Summary Methods
	 */

	public int getLeaveSummayMasterFilterCount(LeaveSummayMaster leaveSummayMaster) {
		return leaveManagementDao.getLeaveSummayMasterFilterCount(leaveSummayMaster);
	}

	public List getLeaveSummayMasteList(int start, int limit, LeaveSummayMaster leaveSummayMaster) {
		return leaveManagementDao.getLeaveSummayMasteList(start, limit, leaveSummayMaster);
	}

	public void saveLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster) {
		leaveManagementDao.saveLeaveSummayMaster(leaveSummayMaster);
	}

	public void removeLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster) {
		leaveManagementDao.removeLeaveSummayMaster(leaveSummayMaster);
	}

	public LeaveSummayMaster getLeaveSummayMaster(Integer Id) {
		return leaveManagementDao.getLeaveSummayMaster(Id);
	}

	public List getAllLeaveSummayMasteList(LeaveSummayMaster leaveSummayMaster) {
		return leaveManagementDao.getAllLeaveSummayMasteList(leaveSummayMaster);
	}
	public void saveBatchLeaveSummary(List leaveSummayMasterList) {
		leaveManagementDao.saveBatchLeaveSummary(leaveSummayMasterList);
	}
	/*
	 * End of Leave Summary Methods
	 */

}
