package eProject.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;

public class TimeSheetExcelView extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<GemsEmployeeTimeSheet> timeSheetList = (List) model.get("timesheets");

		/*
		 * HSSFSheet sheet = workbook.createSheet("Employee Report");
		 * 
		 * HSSFRow header = sheet.createRow(0);
		 * header.createCell(0).setCellValue("Employee Id");
		 * header.createCell(1).setCellValue("First Name");
		 * header.createCell(2).setCellValue("Last Name");
		 * header.createCell(3).setCellValue("Salary");
		 */

		GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = null;

		for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : timeSheetList) {
			gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader();

		}

		String employeeName = "";
		String managerName = "";
		String employeePhone = "";
		String officialMailId = "";
		double totalHours = 0.0;
		String selectMonthYear = "";
		if (gemsEmployeeTimeSheetHeader != null) {
			employeeName = "" + gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getEmployeeLastName() + " "
					+ gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getEmployeeFirstName() + "";
			managerName = ""
					+ gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getCurrentReportingTo().getEmployeeLastName()
					+ " "
					+ gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getCurrentReportingTo().getEmployeeLastName()
					+ "";
			if (gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getPersonalContactNumber() != null) {
				employeePhone = "" + gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getPersonalContactNumber()
						+ "";
			}
			if (gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getOfficialEmailid() != null) {
				officialMailId = "" + gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getOfficialEmailid() + "";
			}
			totalHours = gemsEmployeeTimeSheetHeader.getTimsheetTotalHours();
			selectMonthYear = gemsEmployeeTimeSheetHeader.getTimeSheetMonthYear();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
		int maxDays = 0;
		try {
			Date date = sdf.parse(selectMonthYear);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			maxDays = cal.getActualMaximum(cal.DAY_OF_MONTH);
			System.out.println("Max Days:" + maxDays);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HSSFSheet sheet = workbook.createSheet("TimeSheet");

		sheet.setColumnWidth(0, 300);
		sheet.setColumnWidth(1, 7000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 6000);
		sheet.setColumnWidth(7, 6000);

		sheet.setDisplayGridlines(false);

		Font fontTitle = sheet.getWorkbook().createFont();
		fontTitle.setFontName("Verdana");
		fontTitle.setColor(HSSFColor.GREY_50_PERCENT.index);
		fontTitle.setFontHeight((short) 400);

		// Create cell style for the report title
		HSSFCellStyle cellStyleTitle = sheet.getWorkbook().createCellStyle();
		cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleTitle.setWrapText(true);
		cellStyleTitle.setFont(fontTitle);

		Font fontGreen = sheet.getWorkbook().createFont();
		fontGreen.setFontName("Verdana");
		fontGreen.setColor(HSSFColor.GREEN.index);
		fontGreen.setFontHeight((short) 200);

		HSSFCellStyle cellGreen = sheet.getWorkbook().createCellStyle();
		cellGreen.setFont(fontGreen);

		// Red
		Font fontRed = sheet.getWorkbook().createFont();
		fontRed.setFontName("Verdana");
		fontRed.setColor(HSSFColor.RED.index);
		fontRed.setFontHeight((short) 200);

		HSSFCellStyle cellRed = sheet.getWorkbook().createCellStyle();
		cellRed.setFont(fontRed);

		// Black
		Font fontBlack = sheet.getWorkbook().createFont();
		fontBlack.setFontName("Verdana");
		// fontBlack.setColor(HSSFColor.BLACK.index);
		fontBlack.setFontHeight((short) 200);

		// table formatting
		Font font = sheet.getWorkbook().createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.WHITE.getIndex());
		font.setFontName("Verdana");
		font.setFontHeight((short) 210);

		Font tablefont = sheet.getWorkbook().createFont();
		tablefont.setFontName("Verdana");
		tablefont.setFontHeight((short) 200);

		// Create cell style for the headers
		HSSFCellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
		headerCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headerCellStyle.setWrapText(true);
		headerCellStyle.setFont(font);

		headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerCellStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerCellStyle.setBorderTop(CellStyle.BORDER_THIN);

		HSSFCellStyle tableDataCellStyle = sheet.getWorkbook().createCellStyle();
		tableDataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
		tableDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		tableDataCellStyle.setWrapText(true);
		tableDataCellStyle.setFont(tablefont);

		tableDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		tableDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		tableDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
		tableDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);

		HSSFCellStyle cellBlack = sheet.getWorkbook().createCellStyle();
		cellBlack.setFont(fontBlack);

		HSSFRow rowTitle = sheet.createRow(0);
		rowTitle.setHeight((short) 500);
		HSSFCell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellValue("BPA Technologies - Time Sheet");
		cellTitle.setCellStyle(cellStyleTitle);

		// Create merged region for the report title
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

		HSSFRow rowTitle1 = sheet.createRow(1);
		rowTitle1.setHeight((short) 250);
		HSSFCell cellTitle1 = rowTitle1.createCell(0);
		cellTitle1.setCellValue("");
		cellTitle1.setCellStyle(cellStyleTitle);
		// Create merged region for empty
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

		// detail print

		// Row2
		HSSFRow excelRow2 = sheet.createRow(2);
		excelRow2.setHeight((short) 300);

		HSSFCell excelRow2Cell1 = excelRow2.createCell(1);
		excelRow2Cell1.setCellValue(" Employee        ");
		excelRow2Cell1.setCellStyle(cellGreen);

		HSSFCell excelRow2Cell23 = excelRow2.createCell(2);
		excelRow2Cell23.setCellValue(employeeName);
		excelRow2Cell23.setCellStyle(cellRed);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));

		HSSFCell excelRow2Cell4 = excelRow2.createCell(5);
		excelRow2Cell4.setCellValue(" Manager        ");
		excelRow2Cell4.setCellStyle(cellGreen);

		HSSFCell excelRow2Cell56 = excelRow2.createCell(6);
		excelRow2Cell56.setCellValue(managerName);
		excelRow2Cell56.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));

		// Row3
		HSSFRow excelRow3 = sheet.createRow(3);
		excelRow3.setHeight((short) 300);

		HSSFCell excelRow3Cell1 = excelRow3.createCell(1);
		excelRow3Cell1.setCellValue(" [Street Address]      ");
		excelRow3Cell1.setCellStyle(cellGreen);

		HSSFCell excelRow3Cell23 = excelRow3.createCell(2);
		excelRow3Cell23.setCellValue("28, 30, Metro I,");
		excelRow3Cell23.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

		HSSFCell excelRow3Cell4 = excelRow3.createCell(5);
		excelRow3Cell4.setCellValue(" Employee phone        ");
		excelRow3Cell4.setCellStyle(cellGreen);

		HSSFCell excelRow3Cell56 = excelRow3.createCell(6);
		excelRow3Cell56.setCellValue(employeePhone);
		excelRow3Cell56.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));

		// Row4
		HSSFRow excelRow4 = sheet.createRow(4);
		excelRow4.setHeight((short) 300);

		HSSFCell excelRow4Cell1 = excelRow4.createCell(1);
		excelRow4Cell1.setCellValue(" [Address 2]      ");
		excelRow4Cell1.setCellStyle(cellGreen);

		HSSFCell excelRow4Cell23 = excelRow4.createCell(2);
		excelRow4Cell23.setCellValue("Kodambakkam High Road");
		excelRow4Cell23.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));

		HSSFCell excelRow4Cell4 = excelRow4.createCell(5);
		excelRow4Cell4.setCellValue(" Employee e-mail       ");
		excelRow4Cell4.setCellStyle(cellGreen);

		HSSFCell excelRow4Cell56 = excelRow4.createCell(6);
		excelRow4Cell56.setCellValue(officialMailId);
		excelRow4Cell56.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));

		// Row5
		HSSFRow excelRow5 = sheet.createRow(5);
		excelRow5.setHeight((short) 300);

		HSSFCell excelRow5Cell1 = excelRow5.createCell(1);
		excelRow5Cell1.setCellValue(" [City, ST  ZIP Code]      ");
		excelRow5Cell1.setCellStyle(cellGreen);

		HSSFCell excelRow5Cell23 = excelRow5.createCell(2);
		excelRow5Cell23.setCellValue("Kodambakkam High Road");
		excelRow5Cell23.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));

		HSSFCell excelRow5Cell4 = excelRow5.createCell(5);
		excelRow5Cell4.setCellValue(" Total Hours       ");
		excelRow5Cell4.setCellStyle(cellGreen);

		HSSFCell excelRow5Cell56 = excelRow5.createCell(6);
		excelRow5Cell56.setCellValue(totalHours);
		excelRow5Cell56.setCellStyle(cellBlack);
		sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));

		HSSFRow excelRow6 = sheet.createRow(6);
		rowTitle1.setHeight((short) 250);
		HSSFCell excelRow61 = rowTitle1.createCell(0);
		cellTitle1.setCellValue("");
		cellTitle1.setCellStyle(cellStyleTitle);
		// Create merged region for empty
		sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 7));

		WeekEndDateUtil weekEndDateDetail = new WeekEndDateUtil();

		List<String> weekEndDates = weekEndDateDetail.getWeekEndDates(selectMonthYear);

		int rowCount = 7;

		HSSFRow rowHeader = sheet.createRow(rowCount);
		rowHeader.setHeight((short) 500);
		int startColIndex = 1;
		HSSFCell cell1 = rowHeader.createCell(1);
		cell1.setCellValue("Day");
		cell1.setCellStyle(headerCellStyle);

		HSSFCell cell2 = rowHeader.createCell(2);
		cell2.setCellValue("Date");
		cell2.setCellStyle(headerCellStyle);

		HSSFCell cell3 = rowHeader.createCell(3);
		cell3.setCellValue("RegualarHours");
		cell3.setCellStyle(headerCellStyle);

		HSSFCell cell4 = rowHeader.createCell(4);
		cell4.setCellValue("Activities");
		cell4.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));
		Calendar cal = Calendar.getInstance();

		rowCount = rowCount + 1;
		for (int j = 1; j <= maxDays; j++) {
			String[] selectedMonthYearArray = selectMonthYear.split("/");
			Integer month = new Integer(selectedMonthYearArray[0].toString());
			Integer year = new Integer(selectedMonthYearArray[1].toString());
			String date = "" + month + "/" + j + "/" + year + "";

			cal.set(year, month, j);
			int day = cal.get(Calendar.DAY_OF_WEEK);

			Date monthDate = sdf1.parse(month + "/" + j + "/" + year);
			// System.out.println(""+sdf1.format(monthDate)+" --"+new
			// SimpleDateFormat("EEEE").format(monthDate));
			String dayString = new SimpleDateFormat("EEEE").format(monthDate);

			double timeSheetHours = 0.0;
			String finalcomments = "";

			for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : timeSheetList) {
				double hours = 0.0;
				String comments = "";
				switch (j) {
				case 1:
					hours = gemsEmployeeTimeSheet.getTimeDay1();
					comments = gemsEmployeeTimeSheet.getCommentDay1();
					break;

				case 2:
					hours = gemsEmployeeTimeSheet.getTimeDay2();
					comments = gemsEmployeeTimeSheet.getCommentDay2();
					break;

				case 3:
					hours = gemsEmployeeTimeSheet.getTimeDay3();
					comments = gemsEmployeeTimeSheet.getCommentDay3();
					break;

				case 4:
					hours = gemsEmployeeTimeSheet.getTimeDay4();
					comments = gemsEmployeeTimeSheet.getCommentDay4();
					break;

				case 5:
					hours = gemsEmployeeTimeSheet.getTimeDay5();
					comments = gemsEmployeeTimeSheet.getCommentDay5();
					break;

				case 6:
					hours = gemsEmployeeTimeSheet.getTimeDay6();
					comments = gemsEmployeeTimeSheet.getCommentDay6();
					break;

				case 7:
					hours = gemsEmployeeTimeSheet.getTimeDay7();
					comments = gemsEmployeeTimeSheet.getCommentDay7();
					break;

				case 8:
					hours = gemsEmployeeTimeSheet.getTimeDay8();
					comments = gemsEmployeeTimeSheet.getCommentDay8();
					break;

				case 9:
					hours = gemsEmployeeTimeSheet.getTimeDay9();
					comments = gemsEmployeeTimeSheet.getCommentDay9();
					break;

				case 10:
					hours = gemsEmployeeTimeSheet.getTimeDay10();
					comments = gemsEmployeeTimeSheet.getCommentDay10();
					break;

				case 11:
					hours = gemsEmployeeTimeSheet.getTimeDay11();
					comments = gemsEmployeeTimeSheet.getCommentDay11();
					break;

				case 12:
					hours = gemsEmployeeTimeSheet.getTimeDay12();
					comments = gemsEmployeeTimeSheet.getCommentDay12();
					break;

				case 13:
					hours = gemsEmployeeTimeSheet.getTimeDay13();
					comments = gemsEmployeeTimeSheet.getCommentDay13();
					break;

				case 14:
					hours = gemsEmployeeTimeSheet.getTimeDay14();
					comments = gemsEmployeeTimeSheet.getCommentDay14();
					break;

				case 15:
					hours = gemsEmployeeTimeSheet.getTimeDay15();
					comments = gemsEmployeeTimeSheet.getCommentDay15();
					break;

				case 16:
					hours = gemsEmployeeTimeSheet.getTimeDay16();
					comments = gemsEmployeeTimeSheet.getCommentDay16();
					break;

				case 17:
					hours = gemsEmployeeTimeSheet.getTimeDay17();
					comments = gemsEmployeeTimeSheet.getCommentDay17();
					break;

				case 18:
					hours = gemsEmployeeTimeSheet.getTimeDay18();
					comments = gemsEmployeeTimeSheet.getCommentDay18();
					break;

				case 19:
					hours = gemsEmployeeTimeSheet.getTimeDay19();
					comments = gemsEmployeeTimeSheet.getCommentDay19();
					break;

				case 20:
					hours = gemsEmployeeTimeSheet.getTimeDay20();
					comments = gemsEmployeeTimeSheet.getCommentDay20();
					break;

				case 21:
					hours = gemsEmployeeTimeSheet.getTimeDay21();
					comments = gemsEmployeeTimeSheet.getCommentDay21();
					break;

				case 22:
					hours = gemsEmployeeTimeSheet.getTimeDay22();
					comments = gemsEmployeeTimeSheet.getCommentDay22();
					break;

				case 23:
					hours = gemsEmployeeTimeSheet.getTimeDay23();
					comments = gemsEmployeeTimeSheet.getCommentDay23();
					break;

				case 24:
					hours = gemsEmployeeTimeSheet.getTimeDay24();
					comments = gemsEmployeeTimeSheet.getCommentDay24();
					break;

				case 25:
					hours = gemsEmployeeTimeSheet.getTimeDay25();
					comments = gemsEmployeeTimeSheet.getCommentDay25();
					break;

				case 26:
					hours = gemsEmployeeTimeSheet.getTimeDay26();
					comments = gemsEmployeeTimeSheet.getCommentDay26();
					break;

				case 27:
					hours = gemsEmployeeTimeSheet.getTimeDay27();
					comments = gemsEmployeeTimeSheet.getCommentDay27();
					break;

				case 28:
					hours = gemsEmployeeTimeSheet.getTimeDay28();
					comments = gemsEmployeeTimeSheet.getCommentDay28();
					break;

				case 29:
					hours = gemsEmployeeTimeSheet.getTimeDay29();
					comments = gemsEmployeeTimeSheet.getCommentDay29();
					break;

				case 30:
					hours = gemsEmployeeTimeSheet.getTimeDay30();
					comments = gemsEmployeeTimeSheet.getCommentDay30();
					break;

				case 31:
					hours = gemsEmployeeTimeSheet.getTimeDay31();
					comments = gemsEmployeeTimeSheet.getCommentDay31();
					break;

				default:
					System.out.println("leave it");
				} // switch
				timeSheetHours = timeSheetHours + hours;
				if ((StringUtils.isNotBlank(finalcomments)) || (StringUtils.isNotEmpty(finalcomments))) {
					finalcomments += ". " + comments + "";
				} else {
					finalcomments += comments;
				}

			} // gems employee time sheet

			HSSFRow rowHeaderDetail = sheet.createRow(rowCount);
			rowHeaderDetail.setHeight((short) 500);

			HSSFCell cell11 = rowHeaderDetail.createCell(1);
			cell11.setCellValue(dayString);
			cell11.setCellStyle(tableDataCellStyle);

			HSSFCell cell21 = rowHeaderDetail.createCell(2);
			cell21.setCellValue(sdf1.format(monthDate));
			cell21.setCellStyle(tableDataCellStyle);

			HSSFCell cell31 = rowHeaderDetail.createCell(3);
			cell31.setCellValue(timeSheetHours);
			cell31.setCellStyle(tableDataCellStyle);

			HSSFCell cell41 = rowHeaderDetail.createCell(4);
			cell41.setCellValue(finalcomments);
			cell41.setCellStyle(tableDataCellStyle);

			HSSFCell cell51 = rowHeaderDetail.createCell(5);
			cell51.setCellStyle(tableDataCellStyle);
			HSSFCell cell61 = rowHeaderDetail.createCell(6);
			cell61.setCellStyle(tableDataCellStyle);
			HSSFCell cell71 = rowHeaderDetail.createCell(7);
			cell71.setCellStyle(tableDataCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

			rowCount++;

		}

		HSSFRow rowBottom = sheet.createRow(rowCount);
		rowBottom.setHeight((short) 500);

		HSSFCell bottomCell1 = rowBottom.createCell(1);
		bottomCell1.setCellValue("Total");
		bottomCell1.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));

		HSSFCell bottomCell2 = rowBottom.createCell(3);
		bottomCell2.setCellValue(totalHours);
		bottomCell2.setCellStyle(headerCellStyle);

		HSSFCell bottomCell4 = rowBottom.createCell(4);
		bottomCell4.setCellValue("");
		bottomCell4.setCellStyle(headerCellStyle);
		HSSFCell cell111 = rowBottom.createCell(5);
		cell111.setCellStyle(tableDataCellStyle);
		HSSFCell cell161 = rowBottom.createCell(6);
		cell161.setCellStyle(tableDataCellStyle);
		HSSFCell cell171 = rowBottom.createCell(7);
		cell171.setCellStyle(tableDataCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

	} // method end
}// class end