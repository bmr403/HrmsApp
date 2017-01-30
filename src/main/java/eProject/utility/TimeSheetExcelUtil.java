package eProject.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;

public class TimeSheetExcelUtil {

	protected static final Log logger = LogFactory.getLog(TimeSheetExcelUtil.class);

	public static void createTimeSheet(String tempFilePathName, List<GemsEmployeeTimeSheet> timeSheetList) {

		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

		Workbook wb = new XSSFWorkbook();

		Map<String, CellStyle> styles = createStyles(wb);

		Sheet sheet = wb.createSheet("Time Sheet");

		GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = null;

		for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : timeSheetList) {
			gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader();
		}

		String employeeName = "";
		String managerName = "";
		String employeePhone = "";
		String officialMailId = "";
		double totalHours = 0.0;
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
		}

		// turn off gridlines
		sheet.setDisplayGridlines(true);
		sheet.setPrintGridlines(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short) 1);
		printSetup.setFitWidth((short) 1);

		// title row
		Row titleRow = sheet.createRow(0);
		titleRow.setHeightInPoints(30);

		Cell titleCell = titleRow.createCell(0);
		titleCell.setCellValue("BPA Technologies - Time Sheet");
		titleCell.setCellStyle(styles.get("title"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$J$1"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$G$6:$H$6"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$E$6:$F$6"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$E$4:$F$4"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$G$4:$H$4"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$E$5:$F$5"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$G$5:$H$5"));

		sheet.addMergedRegion(CellRangeAddress.valueOf("$E$3:$F$3"));
		sheet.addMergedRegion(CellRangeAddress.valueOf("$G$3:$H$3"));
		CreationHelper createHelper = wb.getCreationHelper();

		Row employeeDetailRow = sheet.createRow(2);
		Cell employeeDetailCell = employeeDetailRow.createCell(1);
		employeeDetailRow.setHeightInPoints((short) 15);
		RichTextString emp = createHelper.createRichTextString("Employee");
		employeeDetailCell.setCellValue(emp);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(2);
		RichTextString empName = createHelper.createRichTextString(employeeName);
		employeeDetailCell.setCellValue(empName);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_right"));
		employeeDetailCell.setAsActiveCell();

		employeeDetailCell = employeeDetailRow.createCell(4);
		employeeDetailRow.setHeightInPoints((short) 15);
		RichTextString managerLabel = createHelper.createRichTextString("Manager");
		employeeDetailCell.setCellValue(managerLabel);

		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(6);
		RichTextString managerValue = createHelper.createRichTextString(managerName);
		employeeDetailCell.setCellValue(managerValue);

		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_managerVal"));
		employeeDetailCell.setAsActiveCell();

		employeeDetailRow = sheet.createRow(3);
		employeeDetailCell = employeeDetailRow.createCell(1);
		RichTextString streetAddress = createHelper.createRichTextString("[Street Address] ");
		employeeDetailCell.setCellValue(streetAddress);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(2);
		RichTextString streetAddressValue = createHelper.createRichTextString("28, 30, Metro I, ");
		employeeDetailCell.setCellValue(streetAddressValue);
		// employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));

		employeeDetailCell = employeeDetailRow.createCell(4);
		employeeDetailRow.setHeightInPoints((short) 15);
		RichTextString empPhoneLabel = createHelper.createRichTextString("Employee phone: ");
		employeeDetailCell.setCellValue(empPhoneLabel);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(6);
		RichTextString empPhoneValue = createHelper.createRichTextString(employeePhone);
		employeeDetailCell.setCellValue(empPhoneValue);

		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_right"));
		employeeDetailCell.setAsActiveCell();

		employeeDetailRow = sheet.createRow(4);
		employeeDetailCell = employeeDetailRow.createCell(1);
		RichTextString streetAddress2 = createHelper.createRichTextString("[Address 2] ");
		employeeDetailCell.setCellValue(streetAddress2);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(2);
		RichTextString streetAddress2Value = createHelper.createRichTextString("Kodambakkam High Road ");
		employeeDetailCell.setCellValue(streetAddress2Value);
		// employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));

		employeeDetailCell = employeeDetailRow.createCell(4);
		employeeDetailRow.setHeightInPoints((short) 15);
		RichTextString empEmailLabel = createHelper.createRichTextString("Employee e-mail: ");
		employeeDetailCell.setCellValue(empEmailLabel);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(6);
		RichTextString empEmailValue = createHelper.createRichTextString(officialMailId);
		employeeDetailCell.setCellValue(empEmailValue);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_right"));
		employeeDetailCell.setAsActiveCell();

		employeeDetailRow = sheet.createRow(5);
		employeeDetailCell = employeeDetailRow.createCell(1);
		RichTextString cityAddress = createHelper.createRichTextString("[City, ST  ZIP Code] ");
		employeeDetailCell.setCellValue(cityAddress);
		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(2);
		RichTextString cityAddressValue = createHelper.createRichTextString("Chennai-600034 ");
		employeeDetailCell.setCellValue(cityAddressValue);
		// employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));

		employeeDetailCell = employeeDetailRow.createCell(4);
		employeeDetailRow.setHeightInPoints((short) 15);
		RichTextString empTotalHoursLabel = createHelper.createRichTextString("Total Hours: ");
		employeeDetailCell.setCellValue(empTotalHoursLabel);

		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_left"));
		employeeDetailCell = employeeDetailRow.createCell(6);
		RichTextString empTotalHoursValue = createHelper.createRichTextString(String.valueOf(totalHours));
		employeeDetailCell.setCellValue(empTotalHoursValue);

		employeeDetailCell.setCellStyle(styles.get("empDetailsFont_right"));
		employeeDetailCell.setAsActiveCell();

		sheet.setZoom(1, 1); // 100% scale

		// Write the output to a file
		try {
			FileOutputStream out = new FileOutputStream(tempFilePathName);
			wb.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Timesheet has been created succesfully ... @" + tempFilePathName);

	}

	/**
	 * create a library of cell styles
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;

		Font titleFont = wb.createFont();
		titleFont.setFontHeightInPoints((short) 30);
		titleFont.setColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setFont(titleFont);
		styles.put("title", style);

		Font empDetailsFontLeft = wb.createFont();
		empDetailsFontLeft.setFontHeightInPoints((short) 12);
		empDetailsFontLeft.setFontName("Times New Roman");
		empDetailsFontLeft.setColor(IndexedColors.GREEN.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(empDetailsFontLeft);
		styles.put("empDetailsFont_left", style);

		Font empDetailsFontRight = wb.createFont();
		empDetailsFontRight.setFontHeightInPoints((short) 12);
		empDetailsFontRight.setFontName("Times New Roman");
		empDetailsFontRight.setColor(IndexedColors.RED.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(empDetailsFontRight);
		styles.put("empDetailsFont_right", style);

		Font empDetailManagerVal = wb.createFont();
		empDetailManagerVal.setFontHeightInPoints((short) 12);
		empDetailManagerVal.setFontName("Times New Roman");
		empDetailManagerVal.setColor(IndexedColors.LIGHT_ORANGE.getIndex());
		style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(empDetailManagerVal);
		styles.put("empDetailsFont_managerVal", style);

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		// style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styles.put("header", style);

		Font weekTotalFont = wb.createFont();
		weekTotalFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		weekTotalFont.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(weekTotalFont);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styles.put("weekTotalFooter", style);

		Font timeSheetWeekFont = wb.createFont();
		// timeSheetWeekFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		timeSheetWeekFont.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(timeSheetWeekFont);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styles.put("timeSheetWeekFont", style);

		Font timeSheetWeekFontBold = wb.createFont();
		timeSheetWeekFontBold.setBoldweight(Font.BOLDWEIGHT_BOLD);
		timeSheetWeekFontBold.setColor(IndexedColors.BLACK.getIndex());
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(timeSheetWeekFontBold);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styles.put("timeSheetWeekFontBold", style);

		return styles;
	}

	private static CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

}
