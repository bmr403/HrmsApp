package eProject.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekEndDateUtil {

	protected List<String> getWeekEndDates(String selectedMonthYear) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		List<String> weekEndArrayString = new ArrayList();
		try {
			Date date = sdf.parse(selectedMonthYear);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int maxDays = cal.getActualMaximum(cal.DAY_OF_MONTH);
			System.out.println("Max Days:" + maxDays);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH);
			System.out.println("Year:" + year + "---month:" + month);

			for (int i = 1; i <= maxDays; i++) {

				cal.set(year, month, i);
				int day = cal.get(Calendar.DAY_OF_WEEK);

				if (day == 1) {

					weekEndArrayString.add(sdf1.format(cal.getTime()));

				}

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weekEndArrayString;
	}

}
