package eProject.service.timesheet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.project.ProjectDao;
import eProject.dao.timesheet.TimeSheetDao;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.domain.timesheet.GemsEmployeeTimeSheetView;

@Service("TimeSheetService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TimeSheetServiceImpl implements TimeSheetService {

	public TimeSheetServiceImpl() {
	}

	@Autowired
	public TimeSheetDao timeSheetDAO;

	public int getTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getTimeSheetFilterCount(timeSheet);
	}

	public int getApprovalTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getApprovalTimeSheetFilterCount(timeSheet);
	}

	public GemsEmployeeTimeSheetHeader getTimeSheetBySearchParameter(GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getTimeSheetBySearchParameter(timeSheet);
	}

	public List getTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getTimeSheetList(start, limit, timeSheet);
	}

	public List getApprovalTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getApprovalTimeSheetList(start, limit, timeSheet);
	}

	public List getAllTimeSheetList(GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.getAllTimeSheetList(timeSheet);
	}

	public GemsEmployeeTimeSheetHeader saveTimeSheetWithReturn(GemsEmployeeTimeSheetHeader timeSheet) {
		return timeSheetDAO.saveTimeSheetWithReturn(timeSheet);
	}
	
	public void saveBatchTimeSheetHeader(List employeeTimeSheetHeaderList) {
		timeSheetDAO.saveBatchTimeSheetHeader(employeeTimeSheetHeaderList);
	}

	public GemsEmployeeTimeSheetHeader getTimeSheetById(Integer Id) {
		return timeSheetDAO.getTimeSheetById(Id);
	}

	public void removeGemsEmployeeTimeSheetHeader(GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader) {
		timeSheetDAO.removeGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);
	}

	public GemsEmployeeTimeSheet saveGemsEmployeeTimeSheetWithReturn(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.saveGemsEmployeeTimeSheetWithReturn(gemsEmployeeTimeSheet);
	}

	public int getGemsEmployeeTimeSheetFilterCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getGemsEmployeeTimeSheetFilterCount(gemsEmployeeTimeSheet);
	}

	public List getGemsEmployeeTimeSheetList(int start, int limit, GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getGemsEmployeeTimeSheetList(start, limit, gemsEmployeeTimeSheet);
	}

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetByMonthYear(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getGemsEmployeeTimeSheetByMonthYear(gemsEmployeeTimeSheet);
	}

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetById(Integer Id) {
		return timeSheetDAO.getGemsEmployeeTimeSheetById(Id);
	}

	public void removeGemsEmployeeTimeSheet(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		timeSheetDAO.removeGemsEmployeeTimeSheet(gemsEmployeeTimeSheet);
	}

	public int getGemsEmployeeTimeSheetFilterProjectionCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getGemsEmployeeTimeSheetFilterProjectionCount(gemsEmployeeTimeSheet);
	}

	public List getGemsEmployeeTimeSheetProjectionList(int start, int limit,
			GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getGemsEmployeeTimeSheetProjectionList(start, limit, gemsEmployeeTimeSheet);
	}

	public List getAllGemsEmployeeTimeSheetList(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		return timeSheetDAO.getAllGemsEmployeeTimeSheetList(gemsEmployeeTimeSheet);
	}

	public void saveBatchTimeSheet(List employeeTimeSheetList) {
		timeSheetDAO.saveAll(employeeTimeSheetList);
	}
	public List<Date> getWeekStartEndDatesList(int year, int month)
	{
		List<Date> dateList = null;
		List<Date> weeksDateList = null;
		month = month - 1;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, c.getFirstDayOfWeek());
		dateList = new ArrayList<Date>();
		weeksDateList = new ArrayList<Date>();
		while (c.get(Calendar.MONTH) == month) {

			while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				dateList.add(c.getTime());
				c.add(Calendar.DAY_OF_MONTH, 1);
			}
			weeksDateList.add(c.getTime());
			c.add(Calendar.DAY_OF_MONTH, 6);

			if (c.get(Calendar.MONTH) == month) {
				weeksDateList.add(c.getTime());

				c.add(Calendar.DAY_OF_MONTH, 1);
			} else {

				if (c.get(Calendar.YEAR) != year && c.get(Calendar.MONTH) - 1 != month) {
					while (c.get(Calendar.YEAR) - 1 == year && c.get(Calendar.MONTH) != month) {
						c.add(Calendar.DATE, -1);
					}
				} else {

					while (c.get(Calendar.MONTH) - 1 == month) {
						c.add(Calendar.DATE, -1);
					}
				}
				weeksDateList.add(c.getTime());
				break;
			}
		}
		if (weeksDateList.size() > 0 && dateList.size() > 0) {
			weeksDateList.add(0, dateList.get(0));
			weeksDateList.add(1, dateList.get(dateList.size() - 1));
		}
		return weeksDateList;
	}
	public List getAllTimeSheets(GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView) {
		return timeSheetDAO.getAllTimeSheets(gemsEmployeeTimeSheetView);
	}
}
