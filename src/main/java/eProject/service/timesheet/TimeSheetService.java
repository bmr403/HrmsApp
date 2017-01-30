package eProject.service.timesheet;

import java.util.Date;
import java.util.List;

import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.domain.timesheet.GemsEmployeeTimeSheetView;

public interface TimeSheetService {

	/*
	 * Gems Time Sheet Methods
	 */

	public int getTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet);

	public int getApprovalTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet);

	public List getApprovalTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet);

	public GemsEmployeeTimeSheetHeader getTimeSheetBySearchParameter(GemsEmployeeTimeSheetHeader timeSheet);

	public List getTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet);

	public List getAllTimeSheetList(GemsEmployeeTimeSheetHeader timeSheet);

	public GemsEmployeeTimeSheetHeader saveTimeSheetWithReturn(GemsEmployeeTimeSheetHeader timeSheet);

	public GemsEmployeeTimeSheetHeader getTimeSheetById(Integer Id);
	
	public void saveBatchTimeSheetHeader(List employeeTimeSheetHeaderList);

	public void removeGemsEmployeeTimeSheetHeader(GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader);

	public GemsEmployeeTimeSheet saveGemsEmployeeTimeSheetWithReturn(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public int getGemsEmployeeTimeSheetFilterCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public List getGemsEmployeeTimeSheetList(int start, int limit, GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetByMonthYear(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetById(Integer Id);

	public void removeGemsEmployeeTimeSheet(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public int getGemsEmployeeTimeSheetFilterProjectionCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public List getGemsEmployeeTimeSheetProjectionList(int start, int limit,
			GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public List getAllGemsEmployeeTimeSheetList(GemsEmployeeTimeSheet gemsEmployeeTimeSheet);

	public void saveBatchTimeSheet(List employeeTimeSheetList);
	
	
	public List<Date> getWeekStartEndDatesList(int year, int month);
	
	/*
	 * End of Gems Time Sheet Methods
	 */
	public List getAllTimeSheets(GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView);
}
