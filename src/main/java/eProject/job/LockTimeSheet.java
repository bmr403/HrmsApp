package eProject.job;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import eProject.service.timesheet.TimeSheetServiceImpl;

public class LockTimeSheet {
	
	@Autowired
	private TimeSheetServiceImpl timeSheetService;
	
	public void lockTimeSheet() {
		
		Calendar cal = Calendar.getInstance();
		
		
		
		
	}


}
