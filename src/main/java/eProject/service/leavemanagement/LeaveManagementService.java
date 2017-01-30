package eProject.service.leavemanagement;

import java.util.List;

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

public interface LeaveManagementService {

	/*
	 * Gems Leave Period Methods
	 */

	public int getGemsLeavePeriodMasterFilterCount(GemsLeavePeriodMaster gemsLeavePeriodMaster);

	public List getGemsLeavePeriodMasterList(int start, int limit, GemsLeavePeriodMaster gemsLeavePeriodMaster);

	public void saveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster);

	public void removeGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster);

	public GemsLeavePeriodMaster getGemsLeavePeriodMaster(Integer Id);

	public GemsLeavePeriodMaster getActiveGemsLeavePeriodMaster(GemsLeavePeriodMaster gemsLeavePeriodMaster);
	/*
	 * end Gems Leave Period Methods
	 */

	/*
	 * Gems Leave Type Methods
	 */
	public int getGemsLeaveTypeMasterFilterCount(GemsLeaveTypeMaster gemsLeaveTypeMaster);

	public List getGemsLeaveTypeMasterList(int start, int limit, GemsLeaveTypeMaster gemsLeaveTypeMaster);

	public void saveGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster);

	public void removeGemsLeaveTypeMaster(GemsLeaveTypeMaster gemsLeaveTypeMaster);

	public GemsLeaveTypeMaster getGemsLeaveTypeMaster(Integer Id);

	public GemsLeaveTypeMaster getGemsLeaveTypeMasterByCode(GemsLeaveTypeMaster gemsLeaveTypeMaster);

	public List getAllLeaveTypeList(GemsLeaveTypeMaster gemsLeaveTypeMaster);
	/*
	 * End of Leave Type Methods
	 */

	/*
	 * Gems Leave Summary Methods
	 */
	public int getLeaveSummayMasterFilterCount(LeaveSummayMaster leaveSummayMaster);

	public List getLeaveSummayMasteList(int start, int limit, LeaveSummayMaster leaveSummayMaster);

	public void saveLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster);

	public void removeLeaveSummayMaster(LeaveSummayMaster leaveSummayMaster);

	public LeaveSummayMaster getLeaveSummayMaster(Integer Id);

	public List getAllLeaveSummayMasteList(LeaveSummayMaster leaveSummayMaster);
	
	public void saveBatchLeaveSummary(List leaveSummayMasterList);

	/*
	 * End of Leave Summary Methods
	 */
}
