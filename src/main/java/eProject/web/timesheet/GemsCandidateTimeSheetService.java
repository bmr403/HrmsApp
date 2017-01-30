package eProject.web.timesheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.domain.timesheet.GemsEmployeeTimeSheetView;
import eProject.domain.timesheet.WeekStartEndDate;
import eProject.service.employee.EmployeeService;
import eProject.service.project.ProjectService;
import eProject.service.timesheet.TimeSheetServiceImpl;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonBeanProcessor;

@Controller
public class GemsCandidateTimeSheetService {

	protected final Log logger = LogFactory.getLog(GemsCandidateTimeSheetService.class);

	@Autowired
	private ProjectService projectMasterService;

	@Autowired
	private TimeSheetServiceImpl timeSheetService;

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value = "/timesheet/viewTimeSheetList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewTimeSheetList(HttpServletRequest request) {

		try {

			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			// Constructing User Search Object
			GemsEmployeeTimeSheet gemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();

			GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			gemsEmployeeTimeSheet.setEmployeeId(gemsEmployeeMaster.getGemsEmployeeMasterId());

			if ((StringUtils.isNotBlank(request.getParameter("timeMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("timeMonthYear")))) {
				gemsEmployeeTimeSheet.setTimeMonthYear(request.getParameter("timeMonthYear"));
			}

			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeTimeSheetHeaderId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeTimeSheetHeaderId")))) {
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = timeSheetService
						.getTimeSheetById(new Integer(request.getParameter("gemsEmployeeTimeSheetHeaderId")));
				if (gemsEmployeeTimeSheetHeader != null) {
					gemsEmployeeTimeSheet.setTimeMonthYear(gemsEmployeeTimeSheetHeader.getTimeSheetMonthYear());
				}
				// User for approval process
				gemsEmployeeTimeSheet
						.setEmployeeId(gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getGemsEmployeeMasterId());

			}
			if ((StringUtils.isNotBlank(request.getParameter("employeeMasterId")))
					|| (StringUtils.isNotEmpty(request.getParameter("employeeMasterId")))) {
				gemsEmployeeTimeSheet.setEmployeeId(new Integer(request.getParameter("employeeMasterId")));
			}

			int totalResults = timeSheetService.getGemsEmployeeTimeSheetFilterCount(gemsEmployeeTimeSheet);

			List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService.getGemsEmployeeTimeSheetList(start,
					limit, gemsEmployeeTimeSheet);

			logger.info("Returned list size" + gemsEmployeeTimeSheetList.size());

			return getModelMapGemsEmployeeTimeSheetList(gemsEmployeeTimeSheetList, totalResults);
		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	@RequestMapping(value = "/timesheet/viewTimeSheetApprovalList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewTimeSheetApprovalList(HttpServletRequest request) {

		try {

			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			// Constructing User Search Object
			GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = new GemsEmployeeTimeSheetHeader();

			gemsEmployeeTimeSheetHeader.setTimesheetApprovedStatus("SUBMITTED");

			if ((StringUtils.isNotBlank(request.getParameter("selectedTimeMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedTimeMonthYear")))) {
				gemsEmployeeTimeSheetHeader.setTimeSheetMonthYear(request.getParameter("selectedTimeMonthYear"));
			}

			gemsEmployeeTimeSheetHeader.setGemsEmployeeMaster(gemsEmployeeMaster);

			int totalResults = timeSheetService.getApprovalTimeSheetFilterCount(gemsEmployeeTimeSheetHeader);

			List<GemsEmployeeTimeSheetHeader> gemsEmployeeTimeSheetHeaderList = timeSheetService
					.getApprovalTimeSheetList(start, limit, gemsEmployeeTimeSheetHeader);

			logger.info("Returned list size" + gemsEmployeeTimeSheetHeaderList.size());

			return getModelMapGemsEmployeeTimeSheetHeaderList(gemsEmployeeTimeSheetHeaderList, totalResults);
		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	@RequestMapping(value = "/timesheet/viewTimeSheetHeaderList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewTimeSheetHeaderList(HttpServletRequest request) {

		try {

			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			String startValue = request.getParameter("start");
			if (startValue != null && startValue.isEmpty() == false) {
				start = Integer.parseInt(request.getParameter("start"));
			}
			String limitValue = request.getParameter("limit");
			if (limitValue != null && limitValue.isEmpty() == false) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}

			// Constructing User Search Object
			GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = new GemsEmployeeTimeSheetHeader();

			GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			gemsEmployeeTimeSheetHeader.setGemsEmployeeMaster(gemsEmployeeMaster);

			if ((StringUtils.isNotBlank(request.getParameter("selectedTimeMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedTimeMonthYear")))) {
				gemsEmployeeTimeSheetHeader.setTimeSheetMonthYear(request.getParameter("selectedTimeMonthYear"));
			}

			int totalResults = timeSheetService.getTimeSheetFilterCount(gemsEmployeeTimeSheetHeader);

			List<GemsEmployeeTimeSheetHeader> gemsEmployeeTimeSheetHeaderList = timeSheetService.getTimeSheetList(start,
					limit, gemsEmployeeTimeSheetHeader);

			logger.info("Returned list size" + gemsEmployeeTimeSheetHeaderList.size());

			return getModelMapGemsEmployeeTimeSheetHeaderList(gemsEmployeeTimeSheetHeaderList, totalResults);
		} catch (Exception e) {
			e.printStackTrace();
			return getModelMapError("Error trying to List. " + e.getMessage());
		}
	}

	@RequestMapping(value = "/timesheet/saveTimesheet", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> saveTimeSheet(HttpServletRequest request) {
		logger.info("saveTimesheet Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			Integer rowCount = new Integer(0);
			if ((StringUtils.isNotBlank(request.getParameter("rowCount")))
					|| (StringUtils.isNotEmpty(request.getParameter("rowCount")))) {
				System.out.println("rowCount:" + request.getParameter("rowCount"));
				rowCount = new Integer(request.getParameter("rowCount"));
			}

			String timeSheetStatus = "DRAFT";

			if ((StringUtils.isNotBlank(request.getParameter("timeSheetStatus")))
					|| (StringUtils.isNotEmpty(request.getParameter("timeSheetStatus")))) {
				timeSheetStatus = request.getParameter("timeSheetStatus");

			}

			String selectedMonthYear = "";
			int maxDays = 0;
			if ((StringUtils.isNotBlank(request.getParameter("selectedMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedMonthYear")))) {
				selectedMonthYear = request.getParameter("selectedMonthYear");
				System.out.println("selectedMonthYear:" + request.getParameter("selectedMonthYear"));

				// getting max number of days in selected month and year
				SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
				try {
					Date date = sdf.parse(selectedMonthYear);
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					maxDays = cal.getActualMaximum(cal.DAY_OF_MONTH);
					System.out.println("Max Days:" + maxDays);

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// remove data

			GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();

			GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
					"userEmployee");

			searchGemsEmployeeTimeSheet.setEmployeeId(gemsEmployeeMaster.getGemsEmployeeMasterId());

			String timeMonthYear = "";

			if ((StringUtils.isNotBlank(request.getParameter("selectedMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedMonthYear")))) {
				timeMonthYear = request.getParameter("selectedMonthYear");
				searchGemsEmployeeTimeSheet.setTimeMonthYear(request.getParameter("selectedMonthYear"));

			}
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeTimeSheetHeaderId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeTimeSheetHeaderId")))) {
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = timeSheetService
						.getTimeSheetById(new Integer(request.getParameter("gemsEmployeeTimeSheetHeaderId")));
				if (gemsEmployeeTimeSheetHeader != null) {
					searchGemsEmployeeTimeSheet.setTimeMonthYear(gemsEmployeeTimeSheetHeader.getTimeSheetMonthYear());
				}
				// User for approval process
				searchGemsEmployeeTimeSheet
						.setEmployeeId(gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getGemsEmployeeMasterId());

			}

			List<GemsEmployeeTimeSheet> existingGemsEmployeeTimeSheetList = timeSheetService
					.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);

			if (existingGemsEmployeeTimeSheetList.size() != 0) {
				if ((StringUtils.isNotBlank(request.getParameter("selectedMonthYear")))
						|| (StringUtils.isNotEmpty(request.getParameter("selectedMonthYear")))) {
					GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = null;
					for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : existingGemsEmployeeTimeSheetList) {
						gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader();
						timeSheetService.removeGemsEmployeeTimeSheet(gemsEmployeeTimeSheet);
					}
					if (gemsEmployeeTimeSheetHeader != null) {
						timeSheetService.removeGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);
					}
				}

			}

			String dataString = "";
			HashMap<String, String> timeValue = new HashMap<String, String>();
			if ((StringUtils.isNotBlank(request.getParameter("dataString")))
					|| (StringUtils.isNotEmpty(request.getParameter("dataString")))) {
				dataString = request.getParameter("dataString");

				String[] tabColumnIndexValueArray = dataString.split("\\|");

				for (int k = 0; k < tabColumnIndexValueArray.length; k++) {
					String[] columnIndexValueArray = tabColumnIndexValueArray[k].split("~");

					String tableColumnIndex = columnIndexValueArray[0].toString();
					String tableColumnValue = "";
					if (columnIndexValueArray.length > 1) {
						if ((StringUtils.isNotBlank(columnIndexValueArray[1]))
								|| (StringUtils.isNotEmpty(columnIndexValueArray[1]))) {
							tableColumnValue = columnIndexValueArray[1].toString();
						}
					}

					timeValue.put(tableColumnIndex, tableColumnValue);

				}
			}

			List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = new ArrayList();

			double timesheetMonthTotalHour = 0.0;

			for (int i = 0; i < rowCount; i++) {
				GemsEmployeeTimeSheet gemeEmployeeTimeSheet = new GemsEmployeeTimeSheet();
				i++;

				gemeEmployeeTimeSheet.setTimeMonthYear(selectedMonthYear);

				String projectKey = "project_" + i;

				String projectKeyValueFromHashMap = timeValue.get(projectKey);

				// need to revisit following project code

				String[] projectArray = projectKeyValueFromHashMap.split("--");
				String projectCode = projectArray[0].toString().trim();
				GemsProjectMaster gemsProjectMaster = new GemsProjectMaster();
				gemsProjectMaster.setProjectCode(projectCode);

				gemsProjectMaster.setGemsOrganisation(loggedInUser.getGemsOrganisation());
				GemsProjectMaster returnedGemsProjectMaster = projectMasterService
						.getGemsProjectMasterByCode(gemsProjectMaster);

				if (returnedGemsProjectMaster == null) {
					String errorProject = "Sorry problem in saving data";
					modelMap.put("success", false);
					modelMap.put("message", errorProject);
					return modelMap;
				}

				gemeEmployeeTimeSheet.setProjectMaster(returnedGemsProjectMaster);
				
				gemeEmployeeTimeSheet.setActiveStatus(new Integer(1));

				gemeEmployeeTimeSheet.setEmployeeId(gemsEmployeeMaster.getGemsEmployeeMasterId());

				String taskKey = "task_" + i;

				String taskKeyValueFromHashMap = timeValue.get(taskKey);
				gemeEmployeeTimeSheet.setTaskDescription(taskKeyValueFromHashMap);

				String totalRowKey = "total_" + i;
				String totalKeyValueFromHashMap = "0";
				if ((StringUtils.isNotBlank(timeValue.get(totalRowKey)))
						|| (StringUtils.isNotEmpty(timeValue.get(totalRowKey)))) {
					totalKeyValueFromHashMap = timeValue.get(totalRowKey);
				}
				gemeEmployeeTimeSheet.setTotalHours(new Double(totalKeyValueFromHashMap).doubleValue());
				timesheetMonthTotalHour += new Double(totalKeyValueFromHashMap).doubleValue();

				if (maxDays != 0) {
					for (int j = 1; j <= maxDays; j++) {
						String[] selectedMonthYearArray = selectedMonthYear.split("/");
						Integer month = new Integer(selectedMonthYearArray[0].toString());
						Integer year = new Integer(selectedMonthYearArray[1].toString());

						String date = "" + month + "/" + j + "/" + year + "";
						String timeKey = "time_" + i + "_" + date + "";
						String timeKeyValue = "0";
						if ((StringUtils.isNotBlank(timeValue.get(timeKey)))
								|| (StringUtils.isNotEmpty(timeValue.get(timeKey)))) {
							timeKeyValue = timeValue.get(timeKey);
						}

						String commentKey = "comment_" + i + "_" + date + "";
						String commentKeyValue = timeValue.get(commentKey);

						switch (j) {
						case 1:
							gemeEmployeeTimeSheet.setTimeDay1(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay1(commentKeyValue);
							break;

						case 2:
							gemeEmployeeTimeSheet.setTimeDay2(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay2(commentKeyValue);
							break;

						case 3:
							gemeEmployeeTimeSheet.setTimeDay3(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay3(commentKeyValue);
							break;

						case 4:
							gemeEmployeeTimeSheet.setTimeDay4(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay4(commentKeyValue);
							break;

						case 5:
							gemeEmployeeTimeSheet.setTimeDay5(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay5(commentKeyValue);
							break;

						case 6:
							gemeEmployeeTimeSheet.setTimeDay6(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay6(commentKeyValue);
							break;

						case 7:
							gemeEmployeeTimeSheet.setTimeDay7(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay7(commentKeyValue);
							break;

						case 8:
							gemeEmployeeTimeSheet.setTimeDay8(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay8(commentKeyValue);
							break;

						case 9:
							gemeEmployeeTimeSheet.setTimeDay9(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay9(commentKeyValue);
							break;

						case 10:
							gemeEmployeeTimeSheet.setTimeDay10(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay10(commentKeyValue);
							break;

						case 11:
							gemeEmployeeTimeSheet.setTimeDay11(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay11(commentKeyValue);
							break;

						case 12:
							gemeEmployeeTimeSheet.setTimeDay12(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay12(commentKeyValue);
							break;

						case 13:
							gemeEmployeeTimeSheet.setTimeDay13(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay13(commentKeyValue);
							break;

						case 14:
							gemeEmployeeTimeSheet.setTimeDay14(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay14(commentKeyValue);
							break;

						case 15:
							gemeEmployeeTimeSheet.setTimeDay15(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay15(commentKeyValue);
							break;

						case 16:
							gemeEmployeeTimeSheet.setTimeDay16(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay16(commentKeyValue);
							break;

						case 17:
							gemeEmployeeTimeSheet.setTimeDay17(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay17(commentKeyValue);
							break;

						case 18:
							gemeEmployeeTimeSheet.setTimeDay18(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay18(commentKeyValue);
							break;

						case 19:
							gemeEmployeeTimeSheet.setTimeDay19(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay19(commentKeyValue);
							break;

						case 20:
							gemeEmployeeTimeSheet.setTimeDay20(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay20(commentKeyValue);
							break;

						case 21:
							gemeEmployeeTimeSheet.setTimeDay21(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay21(commentKeyValue);
							break;

						case 22:
							gemeEmployeeTimeSheet.setTimeDay22(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay22(commentKeyValue);
							break;

						case 23:
							gemeEmployeeTimeSheet.setTimeDay23(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay23(commentKeyValue);
							break;

						case 24:
							gemeEmployeeTimeSheet.setTimeDay24(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay24(commentKeyValue);
							break;

						case 25:
							gemeEmployeeTimeSheet.setTimeDay25(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay25(commentKeyValue);
							break;

						case 26:
							gemeEmployeeTimeSheet.setTimeDay26(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay26(commentKeyValue);
							break;

						case 27:
							gemeEmployeeTimeSheet.setTimeDay27(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay27(commentKeyValue);
							break;

						case 28:
							gemeEmployeeTimeSheet.setTimeDay28(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay28(commentKeyValue);
							break;

						case 29:
							gemeEmployeeTimeSheet.setTimeDay29(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay29(commentKeyValue);
							break;

						case 30:
							gemeEmployeeTimeSheet.setTimeDay30(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay30(commentKeyValue);
							break;

						case 31:
							gemeEmployeeTimeSheet.setTimeDay31(new Double(timeKeyValue).doubleValue());
							gemeEmployeeTimeSheet.setCommentDay31(commentKeyValue);
							break;

						default:
							System.out.println("leave it");

						}

					}
				}
				gemsEmployeeTimeSheetList.add(gemeEmployeeTimeSheet);
				i--;
			}

			logger.info("GemsEmployeeTimeSheetList size:" + gemsEmployeeTimeSheetList.size());

			try {
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = new GemsEmployeeTimeSheetHeader();
				gemsEmployeeTimeSheetHeader.setTimeSheetMonthYear(timeMonthYear);
				gemsEmployeeTimeSheetHeader.setTimsheetTotalHours(timesheetMonthTotalHour);
				gemsEmployeeTimeSheetHeader.setActiveStatus(new Integer(1));
				gemsEmployeeTimeSheetHeader.setCreatedBy(loggedInUser.getGemsUserMasterId());
				gemsEmployeeTimeSheetHeader.setCreatedOn(todayDate);
				gemsEmployeeTimeSheetHeader.setGemsEmployeeMaster(gemsEmployeeMaster);
				gemsEmployeeTimeSheetHeader.setTimesheetApprovedStatus(timeSheetStatus);
				GemsEmployeeTimeSheetHeader returnedGemsEmployeeTimeSheetHeader = timeSheetService
						.saveTimeSheetWithReturn(gemsEmployeeTimeSheetHeader);
				for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList) {
					logger.info("project:" + gemsEmployeeTimeSheet.getProjectMaster().getGemsProjectMasterId());
					gemsEmployeeTimeSheet.setGemsEmployeeTimeSheetHeader(returnedGemsEmployeeTimeSheetHeader);
					timeSheetService.saveGemsEmployeeTimeSheetWithReturn(gemsEmployeeTimeSheet);
				}

			} catch (Exception ex) {

			}

			logger.info("saveTimesheet Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");

		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);

		}
		return modelMap;
	}

	@RequestMapping(value = "/timesheet/approveTimesheet", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> approveTimesheet(HttpServletRequest request) {
		logger.info("saveTimesheet Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date todayDate = (Date) formatter.parse(formatter.format(currentDate.getTime()));

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");

			Integer gemsEmployeeTimeSheetHeaderId = new Integer(0);
			if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeTimeSheetHeaderId")))
					|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeTimeSheetHeaderId")))) {
				System.out.println(
						"gemsEmployeeTimeSheetHeaderId:" + request.getParameter("gemsEmployeeTimeSheetHeaderId"));
				gemsEmployeeTimeSheetHeaderId = new Integer(request.getParameter("gemsEmployeeTimeSheetHeaderId"));
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = timeSheetService
						.getTimeSheetById(gemsEmployeeTimeSheetHeaderId);

				String timeSheetIsApproved = "";

				if ((StringUtils.isNotBlank(request.getParameter("timeSheetIsApproved")))
						|| (StringUtils.isNotEmpty(request.getParameter("timeSheetIsApproved")))) {
					timeSheetIsApproved = request.getParameter("timeSheetIsApproved");
					if ((timeSheetIsApproved == "on") || (timeSheetIsApproved.equalsIgnoreCase("on"))) {
						gemsEmployeeTimeSheetHeader.setTimesheetApprovedStatus("APPROVED");
					} else {
						gemsEmployeeTimeSheetHeader.setTimesheetApprovedStatus("REJECTED");
					}

				}
				String remarks = "";
				if ((StringUtils.isNotBlank(request.getParameter("timesheet_remarks")))
						|| (StringUtils.isNotEmpty(request.getParameter("timesheet_remarks")))) {
					remarks = request.getParameter("timesheet_remarks");

				}
				gemsEmployeeTimeSheetHeader.setRemarks(remarks);

				timeSheetService.saveTimeSheetWithReturn(gemsEmployeeTimeSheetHeader);

			}

			logger.info("saveTimesheet Method Executed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Saved Successfully");
			return modelMap;
		} catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Sorry problem in saving data";
			modelMap.put("success", false);
			modelMap.put("message", msg);
			return modelMap;
		}
	}

	// Delete Service
	@RequestMapping(value = "/timesheet/deleteTimeSheet", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> deleteTimeSheet(HttpServletRequest request) {

		logger.info("Delete TimeSheet Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		String gemsEmployeeTimeSheetHeaderIdStr = request.getParameter("gemsEmployeeTimeSheetHeaderId");
		try {
			if (gemsEmployeeTimeSheetHeaderIdStr != null) {

				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = timeSheetService
						.getTimeSheetById(Integer.parseInt(gemsEmployeeTimeSheetHeaderIdStr));

				if (gemsEmployeeTimeSheetHeader != null) {
					GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();
					searchGemsEmployeeTimeSheet.setGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);

					List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService
							.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);
					for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList) {
						timeSheetService.removeGemsEmployeeTimeSheet(gemsEmployeeTimeSheet);
					}

					timeSheetService.removeGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);
				}

			}
			logger.info("Delete Method Completed.,");
			modelMap.put("success", true);
			modelMap.put("message", "Deleted Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in deletion");
			return modelMap;
		}
	}

	/*
	 * @RequestMapping(value = "/timesheet/exportTimeSheetTOExcel", method =
	 * RequestMethod.POST) public @ResponseBody Map<String, Object>
	 * exportTimeSheetTOExcel(HttpServletRequest request) {
	 */

	// JSon Construction
	private Map<String, Object> getModelMapGemsEmployeeTimeSheetList(List<GemsEmployeeTimeSheet> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeTimeSheet.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeTimeSheet)) {
					return new JSONObject(true);
				}
				GemsEmployeeTimeSheet gemsEmployeeTimeSheet = (GemsEmployeeTimeSheet) bean;
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

				GemsProjectResourceMaster searchGemsProjectResourceMaster = new GemsProjectResourceMaster();
				int selectedGemsProjectMasterId = 0;
				String selected_project = "";
				if (gemsEmployeeTimeSheet.getProjectMaster() != null) {
					selectedGemsProjectMasterId = gemsEmployeeTimeSheet.getProjectMaster().getGemsProjectMasterId();
					selected_project = "" + gemsEmployeeTimeSheet.getProjectMaster().getProjectCode() + " -- "
							+ gemsEmployeeTimeSheet.getProjectMaster().getProjectName() + "";
					searchGemsProjectResourceMaster.setGemsProjectMaster(gemsEmployeeTimeSheet.getProjectMaster());
				}
				String employeeName = "";
				Integer employeeMasterId = new Integer(0);
				if (gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeMaster() != null) {
					employeeName = ""
							+ gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeMaster()
									.getEmployeeLastName()
							+ " " + gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeMaster()
									.getEmployeeFirstName()
							+ "";
					employeeMasterId = gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeMaster()
							.getGemsEmployeeMasterId();
					searchGemsProjectResourceMaster.setGemsEmployeeMaster(
							gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeMaster());
				}

				// need to revisit following code
				String projectResourceActiveString = "In-Active";
				boolean projectResourceActiveStatus = false;
				String inActiveDateString = "";
				String resourceStartDate = "";
				String resourceEndDate = "";
				if ((searchGemsProjectResourceMaster.getGemsEmployeeMaster() != null)
						&& (searchGemsProjectResourceMaster.getGemsProjectMaster() != null)) {
					GemsProjectResourceMaster gemsProjectResourceMaster = projectMasterService
							.getGemsProjectResourceMasterByResourceAndProject(searchGemsProjectResourceMaster);
					if (gemsProjectResourceMaster.getActiveStatus() == 1) {
						projectResourceActiveString = "Active";
					}

					if (gemsProjectResourceMaster.getActiveStatus() == 1) {
						projectResourceActiveStatus = true;
					} else {
						inActiveDateString = sdf.format(gemsProjectResourceMaster.getInActiveFrom());
					}
					if (gemsProjectResourceMaster.getResourceStartDate() != null) {
						resourceStartDate = sdf.format(gemsProjectResourceMaster.getResourceStartDate());
					}
					if (gemsProjectResourceMaster.getResourceEndDate() != null) {
						resourceEndDate = sdf.format(gemsProjectResourceMaster.getResourceEndDate());
					}
				}

				return new JSONObject()
						.element("gemsEmployeeTimeSheetId", gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetId())
						.element("resourceStartDate", resourceStartDate).element("resourceEndDate", resourceEndDate)
						.element("gemsEmployeeTimeSheetHeaderId",
								gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader()
										.getGemsEmployeeTimeSheetHeaderId())
						.element("activeStatus",
								gemsEmployeeTimeSheet.getActiveStatus())
						.element("timesheetApprovedStatus",
								gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getTimesheetApprovedStatus())
						.element("timeMonthYear", gemsEmployeeTimeSheet.getTimeMonthYear())
						.element("selectedTimeMonthYear", gemsEmployeeTimeSheet.getTimeMonthYear())
						.element("selectedGemsProjectMasterId", selectedGemsProjectMasterId)
						.element("employeeName", employeeName).element("employeeMasterId", employeeMasterId)
						.element("selected_project", selected_project)
						.element("taskDescription", gemsEmployeeTimeSheet.getTaskDescription())
						.element("totalHours", gemsEmployeeTimeSheet.getTotalHours())
						.element("timeDay1", gemsEmployeeTimeSheet.getTimeDay1())
						.element("timeDay2", gemsEmployeeTimeSheet.getTimeDay2())
						.element("timeDay3", gemsEmployeeTimeSheet.getTimeDay3())
						.element("timeDay4", gemsEmployeeTimeSheet.getTimeDay4())
						.element("timeDay5", gemsEmployeeTimeSheet.getTimeDay5())
						.element("timeDay6", gemsEmployeeTimeSheet.getTimeDay6())
						.element("timeDay7", gemsEmployeeTimeSheet.getTimeDay7())
						.element("timeDay8", gemsEmployeeTimeSheet.getTimeDay8())
						.element("timeDay9", gemsEmployeeTimeSheet.getTimeDay9())
						.element("timeDay10", gemsEmployeeTimeSheet.getTimeDay10())
						.element("timeDay11", gemsEmployeeTimeSheet.getTimeDay11())
						.element("timeDay12", gemsEmployeeTimeSheet.getTimeDay12())
						.element("timeDay13", gemsEmployeeTimeSheet.getTimeDay13())
						.element("timeDay14", gemsEmployeeTimeSheet.getTimeDay14())
						.element("timeDay15", gemsEmployeeTimeSheet.getTimeDay15())
						.element("timeDay16", gemsEmployeeTimeSheet.getTimeDay16())
						.element("timeDay17", gemsEmployeeTimeSheet.getTimeDay17())
						.element("timeDay18", gemsEmployeeTimeSheet.getTimeDay18())
						.element("timeDay19", gemsEmployeeTimeSheet.getTimeDay19())
						.element("timeDay20", gemsEmployeeTimeSheet.getTimeDay20())
						.element("timeDay21", gemsEmployeeTimeSheet.getTimeDay21())
						.element("timeDay22", gemsEmployeeTimeSheet.getTimeDay22())
						.element("timeDay23", gemsEmployeeTimeSheet.getTimeDay23())
						.element("timeDay24", gemsEmployeeTimeSheet.getTimeDay24())
						.element("timeDay25", gemsEmployeeTimeSheet.getTimeDay25())
						.element("timeDay26", gemsEmployeeTimeSheet.getTimeDay26())
						.element("timeDay27", gemsEmployeeTimeSheet.getTimeDay27())
						.element("timeDay28", gemsEmployeeTimeSheet.getTimeDay28())
						.element("timeDay29", gemsEmployeeTimeSheet.getTimeDay29())
						.element("timeDay30", gemsEmployeeTimeSheet.getTimeDay30())
						.element("timeDay31", gemsEmployeeTimeSheet.getTimeDay31())
						.element("commentDay1", gemsEmployeeTimeSheet.getCommentDay1())
						.element("commentDay2", gemsEmployeeTimeSheet.getCommentDay2())
						.element("commentDay3", gemsEmployeeTimeSheet.getCommentDay3())
						.element("commentDay4", gemsEmployeeTimeSheet.getCommentDay4())
						.element("commentDay5", gemsEmployeeTimeSheet.getCommentDay5())
						.element("commentDay6", gemsEmployeeTimeSheet.getCommentDay6())
						.element("commentDay7", gemsEmployeeTimeSheet.getCommentDay7())
						.element("commentDay8", gemsEmployeeTimeSheet.getCommentDay8())
						.element("commentDay9", gemsEmployeeTimeSheet.getCommentDay9())
						.element("commentDay10", gemsEmployeeTimeSheet.getCommentDay10())
						.element("commentDay11", gemsEmployeeTimeSheet.getCommentDay11())
						.element("commentDay12", gemsEmployeeTimeSheet.getCommentDay12())
						.element("commentDay13", gemsEmployeeTimeSheet.getCommentDay13())
						.element("commentDay14", gemsEmployeeTimeSheet.getCommentDay14())
						.element("commentDay15", gemsEmployeeTimeSheet.getCommentDay15())
						.element("commentDay16", gemsEmployeeTimeSheet.getCommentDay16())
						.element("commentDay17", gemsEmployeeTimeSheet.getCommentDay17())
						.element("commentDay18", gemsEmployeeTimeSheet.getCommentDay18())
						.element("commentDay19", gemsEmployeeTimeSheet.getCommentDay19())
						.element("commentDay20", gemsEmployeeTimeSheet.getCommentDay20())
						.element("commentDay21", gemsEmployeeTimeSheet.getCommentDay21())
						.element("commentDay22", gemsEmployeeTimeSheet.getCommentDay22())
						.element("commentDay23", gemsEmployeeTimeSheet.getCommentDay23())
						.element("commentDay24", gemsEmployeeTimeSheet.getCommentDay24())
						.element("commentDay25", gemsEmployeeTimeSheet.getCommentDay25())
						.element("commentDay26", gemsEmployeeTimeSheet.getCommentDay26())
						.element("commentDay27", gemsEmployeeTimeSheet.getCommentDay27())
						.element("commentDay28", gemsEmployeeTimeSheet.getCommentDay28())
						.element("commentDay29", gemsEmployeeTimeSheet.getCommentDay29())
						.element("commentDay30", gemsEmployeeTimeSheet.getCommentDay30())
						.element("commentDay31", gemsEmployeeTimeSheet.getCommentDay31())
						.element("inActiveFrom", inActiveDateString)
						.element("projectResourceActiveStatus", projectResourceActiveStatus)

				;
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapGemsEmployeeTimeSheetHeaderList(List<GemsEmployeeTimeSheetHeader> list,
			int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(GemsEmployeeTimeSheetHeader.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof GemsEmployeeTimeSheetHeader)) {
					return new JSONObject(true);
				}
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = (GemsEmployeeTimeSheetHeader) bean;

				String employeeName = "";
				Integer employeeMasterId = new Integer(0);
				if (gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster() != null) {
					employeeName = "" + gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getEmployeeLastName() + " "
							+ gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getEmployeeFirstName() + "";
					employeeMasterId = gemsEmployeeTimeSheetHeader.getGemsEmployeeMaster().getGemsEmployeeMasterId();
				}
				return new JSONObject()
						.element("gemsEmployeeTimeSheetHeaderId",
								gemsEmployeeTimeSheetHeader.getGemsEmployeeTimeSheetHeaderId())
						.element("employeeName", employeeName).element("employeeMasterId", employeeMasterId)
						.element("timeSheetMonthYear", gemsEmployeeTimeSheetHeader.getTimeSheetMonthYear())
						.element("totalHours", gemsEmployeeTimeSheetHeader.getTimsheetTotalHours())
						.element("activeStatus",gemsEmployeeTimeSheetHeader.getActiveStatus())
						.element("timesheetApprovedStatus", gemsEmployeeTimeSheetHeader.getTimesheetApprovedStatus());
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}

	private Map<String, Object> getModelMapError(String msg) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("message", msg);
		modelMap.put("success", false);
		modelMap.put("data", "");

		return modelMap;
	}

	@RequestMapping(value = "/timesheet/exportTimeSheetTOExcel", method = RequestMethod.GET)

	public ModelAndView exportTimeSheetTOExcel(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("export TimeSheetTOExcel Method Strarted.,");

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");

		// Constructing User Search Object
		GemsEmployeeTimeSheet gemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();

		GemsEmployeeMaster gemsEmployeeMaster = (GemsEmployeeMaster) WebUtils.getRequiredSessionAttribute(request,
				"userEmployee");

		gemsEmployeeTimeSheet.setEmployeeId(gemsEmployeeMaster.getGemsEmployeeMasterId());

		if ((StringUtils.isNotBlank(request.getParameter("timeMonthYear")))
				|| (StringUtils.isNotEmpty(request.getParameter("timeMonthYear")))) {
			gemsEmployeeTimeSheet.setTimeMonthYear(request.getParameter("timeMonthYear"));
		}

		if ((StringUtils.isNotBlank(request.getParameter("gemsEmployeeTimeSheetHeaderId")))
				|| (StringUtils.isNotEmpty(request.getParameter("gemsEmployeeTimeSheetHeaderId")))) {
			GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = timeSheetService
					.getTimeSheetById(new Integer(request.getParameter("gemsEmployeeTimeSheetHeaderId")));
			if (gemsEmployeeTimeSheetHeader != null) {
				gemsEmployeeTimeSheet.setTimeMonthYear(gemsEmployeeTimeSheetHeader.getTimeSheetMonthYear());
			}

		}

		List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService
				.getAllGemsEmployeeTimeSheetList(gemsEmployeeTimeSheet);

		ModelAndView modelAndView = new ModelAndView("excelView", "timesheets", gemsEmployeeTimeSheetList);

		return modelAndView;

	}

	/*@RequestMapping(value = "/timesheet/generateTimeSheetReport", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> generateTimeSheetReport(HttpServletRequest request) {

		logger.info("Generate Timesheet report Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		try {

			GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();

			String timeMonthYear = "";

			if ((StringUtils.isNotBlank(request.getParameter("timeMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("timeMonthYear")))) {
				timeMonthYear = request.getParameter("timeMonthYear");
				searchGemsEmployeeTimeSheet.setTimeMonthYear(request.getParameter("timeMonthYear"));	
				searchGemsEmployeeTimeSheet.setActiveStatus(new Integer(1)); // will only take active time sheets
				GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader = new GemsEmployeeTimeSheetHeader();
				gemsEmployeeTimeSheetHeader.setTimesheetApprovedStatus("APPROVED"); // WILL ONLY export approved time sheets
				searchGemsEmployeeTimeSheet.setGemsEmployeeTimeSheetHeader(gemsEmployeeTimeSheetHeader);
				
			}

			GemsProjectResourceMaster searchGemsProjectResourceMaster = new GemsProjectResourceMaster();
			List<GemsProjectResourceMaster> projectResourceList = projectMasterService
					.getAllGemsProjectResourceMasterList(searchGemsProjectResourceMaster);

			List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService
					.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);

			String replacedtimeMonthYear = timeMonthYear.replaceFirst("/", "-");

			String uploadDirectoryBase = System.getProperty("catalina.base");
			String timesheetFileRepoString = uploadDirectoryBase + File.separator + "webapps" + File.separator
					+ "upload" + File.separator + "timesheet" + File.separator + replacedtimeMonthYear;
			File timesheetFileRepo = new File(timesheetFileRepoString);
			if (!(timesheetFileRepo.exists())) {
				timesheetFileRepo.mkdirs();
			}

			for (GemsProjectResourceMaster gemsProjectResourceMaster : projectResourceList) {

				GemsProjectMaster gemsProjectMaster = gemsProjectResourceMaster.getGemsProjectMaster();
				GemsEmployeeMaster gemsEmployeeMaster = gemsProjectResourceMaster.getGemsEmployeeMaster();
				List<GemsEmployeeTimeSheet> resultedGemsEmployeeTimeSheet = new ArrayList<GemsEmployeeTimeSheet>();

				for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList) {

					if ((gemsEmployeeTimeSheet.getTimeMonthYear().equalsIgnoreCase(timeMonthYear))
							&& (gemsEmployeeTimeSheet.getProjectMaster().getGemsProjectMasterId() == gemsProjectMaster
									.getGemsProjectMasterId())
							&& (gemsEmployeeTimeSheet.getEmployeeId() == gemsEmployeeMaster
									.getGemsEmployeeMasterId())) {
						resultedGemsEmployeeTimeSheet.add(gemsEmployeeTimeSheet);
					}
				}

				if (resultedGemsEmployeeTimeSheet.size() != 0) {

					for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : resultedGemsEmployeeTimeSheet) {

						String employeeName = "";
						String managerName = "";
						String employeePhone = "";
						String officialMailId = "";
						double totalHours = 0.0;
						String selectMonthYear = "";

						GemsEmployeeMaster timeSheetEmployee = employeeService
								.getGemsEmployeeMaster(gemsEmployeeTimeSheet.getEmployeeId());
						employeeName = "" + timeSheetEmployee.getEmployeeLastName() + " "
								+ timeSheetEmployee.getEmployeeFirstName() + "";
						managerName = "" + timeSheetEmployee.getCurrentReportingTo().getEmployeeLastName() + " "
								+ timeSheetEmployee.getCurrentReportingTo().getEmployeeLastName() + "";
						if (timeSheetEmployee.getPersonalContactNumber() != null) {
							employeePhone = "" + timeSheetEmployee.getPersonalContactNumber() + "";
						}
						if (timeSheetEmployee.getOfficialEmailid() != null) {
							officialMailId = "" + timeSheetEmployee.getOfficialEmailid() + "";
						}
						totalHours = gemsEmployeeTimeSheet.getTotalHours();
						selectMonthYear = gemsEmployeeTimeSheet.getTimeMonthYear();

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

						// FileInputStream fis = new
						// FileInputStream(employeeTimeSheet);
						Workbook myWorkBook = new XSSFWorkbook();
						Sheet sheet = null;
						sheet = myWorkBook.createSheet("TimeSheet");

						sheet.setColumnWidth(0, 300);
						sheet.setColumnWidth(1, 7000);
						sheet.setColumnWidth(2, 6000);
						sheet.setColumnWidth(3, 6000);
						sheet.setColumnWidth(5, 6000);
						sheet.setColumnWidth(6, 6000);
						sheet.setColumnWidth(7, 6000);

						sheet.setDisplayGridlines(false);

						// Font fontTitle = sheet.getWorkbook().createFont();

						Font fontTitle = sheet.getWorkbook().createFont();
						fontTitle.setFontName("Verdana");
						fontTitle.setColor(IndexedColors.RED.index);
						fontTitle.setFontHeight((short) 400);

						CellStyle cellStyleTitle = sheet.getWorkbook().createCellStyle();
						cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
						cellStyleTitle.setWrapText(true);
						cellStyleTitle.setFont(fontTitle);

						Font fontGreen = sheet.getWorkbook().createFont();
						fontGreen.setFontName("Verdana");
						fontGreen.setColor(IndexedColors.GREEN.index);
						fontGreen.setFontHeight((short) 200);

						CellStyle cellGreen = sheet.getWorkbook().createCellStyle();
						cellGreen.setFont(fontGreen);

						// Red

						Font fontRed = sheet.getWorkbook().createFont();
						fontRed.setFontName("Verdana");
						fontRed.setColor(IndexedColors.RED.index);
						fontRed.setFontHeight((short) 200);

						CellStyle cellRed = sheet.getWorkbook().createCellStyle();
						cellRed.setFont(fontRed);

						// Black
						Font fontBlack = sheet.getWorkbook().createFont();
						fontBlack.setFontName("Verdana");
						// fontBlack.setColor(XSSFColor.BLACK.index);
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
						CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
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

						CellStyle tableDataCellStyle = sheet.getWorkbook().createCellStyle();
						tableDataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
						tableDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
						tableDataCellStyle.setWrapText(true);
						tableDataCellStyle.setFont(tablefont);

						tableDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);

						CellStyle cellBlack = sheet.getWorkbook().createCellStyle();
						cellBlack.setFont(fontBlack);

						Row rowTitle = sheet.createRow(0);
						rowTitle.setHeight((short) 500);
						Cell cellTitle = rowTitle.createCell(0);
						cellTitle.setCellValue("BPA Technologies - Time Sheet");
						cellTitle.setCellStyle(cellStyleTitle);

						// Create merged region for the report title
						sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

						Row rowTitle1 = sheet.createRow(1);
						rowTitle1.setHeight((short) 250);
						Cell cellTitle1 = rowTitle1.createCell(0);
						cellTitle1.setCellValue("");
						cellTitle1.setCellStyle(cellStyleTitle);
						// Create merged region for empty
						sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

						// detail print

						// Row2
						Row excelRow2 = sheet.createRow(2);
						excelRow2.setHeight((short) 300);

						Cell excelRow2Cell1 = excelRow2.createCell(1);
						excelRow2Cell1.setCellValue(" Employee        ");
						excelRow2Cell1.setCellStyle(cellGreen);

						Cell excelRow2Cell23 = excelRow2.createCell(2);
						excelRow2Cell23.setCellValue(employeeName);
						excelRow2Cell23.setCellStyle(cellRed);
						sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));

						Cell excelRow2Cell4 = excelRow2.createCell(5);
						excelRow2Cell4.setCellValue(" Manager        ");
						excelRow2Cell4.setCellStyle(cellGreen);

						Cell excelRow2Cell56 = excelRow2.createCell(6);
						excelRow2Cell56.setCellValue(managerName);
						excelRow2Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));

						// Row3
						Row excelRow3 = sheet.createRow(3);
						excelRow3.setHeight((short) 300);

						Cell excelRow3Cell1 = excelRow3.createCell(1);
						excelRow3Cell1.setCellValue(" [Street Address]      ");
						excelRow3Cell1.setCellStyle(cellGreen);

						Cell excelRow3Cell23 = excelRow3.createCell(2);
						excelRow3Cell23.setCellValue("28, 30, Metro I,");
						excelRow3Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

						Cell excelRow3Cell4 = excelRow3.createCell(5);
						excelRow3Cell4.setCellValue(" Employee phone        ");
						excelRow3Cell4.setCellStyle(cellGreen);

						Cell excelRow3Cell56 = excelRow3.createCell(6);
						excelRow3Cell56.setCellValue(employeePhone);
						excelRow3Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));

						// Row4
						Row excelRow4 = sheet.createRow(4);
						excelRow4.setHeight((short) 300);

						Cell excelRow4Cell1 = excelRow4.createCell(1);
						excelRow4Cell1.setCellValue(" [Address 2]      ");
						excelRow4Cell1.setCellStyle(cellGreen);

						Cell excelRow4Cell23 = excelRow4.createCell(2);
						excelRow4Cell23.setCellValue("Kodambakkam High Road");
						excelRow4Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));

						Cell excelRow4Cell4 = excelRow4.createCell(5);
						excelRow4Cell4.setCellValue(" Employee e-mail       ");
						excelRow4Cell4.setCellStyle(cellGreen);

						Cell excelRow4Cell56 = excelRow4.createCell(6);
						excelRow4Cell56.setCellValue(officialMailId);
						excelRow4Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));

						// Row5
						Row excelRow5 = sheet.createRow(5);
						excelRow5.setHeight((short) 300);

						Cell excelRow5Cell1 = excelRow5.createCell(1);
						excelRow5Cell1.setCellValue(" [City, ST  ZIP Code]      ");
						excelRow5Cell1.setCellStyle(cellGreen);

						Cell excelRow5Cell23 = excelRow5.createCell(2);
						excelRow5Cell23.setCellValue("Kodambakkam High Road");
						excelRow5Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));

						Cell excelRow5Cell4 = excelRow5.createCell(5);
						excelRow5Cell4.setCellValue(" Total Hours       ");
						excelRow5Cell4.setCellStyle(cellGreen);

						Cell excelRow5Cell56 = excelRow5.createCell(6);
						excelRow5Cell56.setCellValue(totalHours);
						excelRow5Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));

						Row excelRow6 = sheet.createRow(6);
						excelRow6.setHeight((short) 300);

						Cell excelRow6Cell1 = excelRow6.createCell(1);
						excelRow6Cell1.setCellValue(" Project Name      ");
						excelRow6Cell1.setCellStyle(cellGreen);

						Cell excelRow6Cell23 = excelRow6.createCell(2);
						excelRow6Cell23.setCellValue(gemsEmployeeTimeSheet.getProjectMaster().getProjectName());
						excelRow6Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(6, 6, 2, 3));

						Cell excelRow6Cell4 = excelRow6.createCell(5);
						excelRow6Cell4.setCellValue(" Client       ");
						excelRow6Cell4.setCellStyle(cellGreen);

						Cell excelRow6Cell56 = excelRow6.createCell(6);
						excelRow6Cell56.setCellValue(
								gemsEmployeeTimeSheet.getProjectMaster().getGemsCustomerMaster().getGemsCustomerName());
						excelRow6Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 7));

						Row excelRow7 = sheet.createRow(7);
						rowTitle1.setHeight((short) 250);
						Cell excelRow71 = rowTitle1.createCell(0);
						cellTitle1.setCellValue("");
						cellTitle1.setCellStyle(cellStyleTitle);
						// Create merged region for empty
						sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));

						List<String> weekEndDates = getWeekEndDates(selectMonthYear);

						int rowCount = 8;

						Row rowHeader = sheet.createRow(rowCount);
						rowHeader.setHeight((short) 500);
						int startColIndex = 1;
						Cell cell1 = rowHeader.createCell(1);
						cell1.setCellValue("Day");
						cell1.setCellStyle(headerCellStyle);

						Cell cell2 = rowHeader.createCell(2);
						cell2.setCellValue("Date");
						cell2.setCellStyle(headerCellStyle);

						Cell cell3 = rowHeader.createCell(3);
						cell3.setCellValue("RegualarHours");
						cell3.setCellStyle(headerCellStyle);

						Cell cell4 = rowHeader.createCell(4);
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
							// System.out.println(""+sdf1.format(monthDate)+"
							// --"+new
							// SimpleDateFormat("EEEE").format(monthDate));
							String dayString = new SimpleDateFormat("EEEE").format(monthDate);

							double timeSheetHours = 0.0;
							String finalcomments = "";

							// for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet
							// : resultedGemsEmployeeTimeSheet)
							// {
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

							///////////////////////////////////////////////////// }
							///////////////////////////////////////////////////// //
							///////////////////////////////////////////////////// gems
							///////////////////////////////////////////////////// employee
							///////////////////////////////////////////////////// time
							///////////////////////////////////////////////////// sheet

							Row rowHeaderDetail = sheet.createRow(rowCount);
							rowHeaderDetail.setHeight((short) 500);

							Cell cell11 = rowHeaderDetail.createCell(1);
							cell11.setCellValue(dayString);
							cell11.setCellStyle(tableDataCellStyle);

							Cell cell21 = rowHeaderDetail.createCell(2);
							cell21.setCellValue(sdf1.format(monthDate));
							cell21.setCellStyle(tableDataCellStyle);

							Cell cell31 = rowHeaderDetail.createCell(3);
							cell31.setCellValue(timeSheetHours);
							cell31.setCellStyle(tableDataCellStyle);

							Cell cell41 = rowHeaderDetail.createCell(4);
							cell41.setCellValue(finalcomments);
							cell41.setCellStyle(tableDataCellStyle);

							Cell cell51 = rowHeaderDetail.createCell(5);
							cell51.setCellStyle(tableDataCellStyle);
							Cell cell61 = rowHeaderDetail.createCell(6);
							cell61.setCellStyle(tableDataCellStyle);

							Cell cell71 = rowHeaderDetail.createCell(7);
							cell71.setCellStyle(tableDataCellStyle);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

							rowCount++;

						}
						Row rowBottom = sheet.createRow(rowCount);
						rowBottom.setHeight((short) 500);

						Cell bottomCell1 = rowBottom.createCell(1);
						bottomCell1.setCellValue("Total");
						bottomCell1.setCellStyle(headerCellStyle);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));

						Cell bottomCell2 = rowBottom.createCell(3);
						bottomCell2.setCellValue(totalHours);
						bottomCell2.setCellStyle(headerCellStyle);

						Cell bottomCell4 = rowBottom.createCell(4);
						bottomCell4.setCellValue("");
						bottomCell4.setCellStyle(headerCellStyle);
						Cell cell111 = rowBottom.createCell(5);
						cell111.setCellStyle(tableDataCellStyle);
						Cell cell161 = rowBottom.createCell(6);
						cell161.setCellStyle(tableDataCellStyle);
						Cell cell171 = rowBottom.createCell(7);
						cell171.setCellStyle(tableDataCellStyle);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

						File employeeTimeSheet = new File(timesheetFileRepoString + File.separator
								+ replacedtimeMonthYear + "_" + gemsProjectMaster.getProjectName() + "_"
								+ gemsEmployeeMaster.getEmployeeCode() + ".xlsx");
						if (!(employeeTimeSheet.exists())) {
							employeeTimeSheet.createNewFile();
						}
						FileOutputStream os = new FileOutputStream(employeeTimeSheet);
						myWorkBook.write(os);
						os.close();
						os = null;

					}

				}

			}

			if (timesheetFileRepo.exists()) {
				String timesheetFileZipRepoString = uploadDirectoryBase + File.separator + "webapps" + File.separator
						+ "upload" + File.separator + "timesheet" + File.separator + replacedtimeMonthYear + ".zip";
				try {

					// create byte buffer
					byte[] buffer = new byte[1024];

					FileOutputStream fos = new FileOutputStream(timesheetFileZipRepoString);

					ZipOutputStream zos = new ZipOutputStream(fos);

					File dir = new File(timesheetFileRepoString);

					File[] files = dir.listFiles();

					for (int i = 0; i < files.length; i++) {

						System.out.println("Adding file: " + files[i].getName());

						FileInputStream fis = new FileInputStream(files[i]);

						// begin writing a new ZIP entry, positions the stream
						// to the start of the entry data
						zos.putNextEntry(new ZipEntry(files[i].getName()));

						int length;

						while ((length = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, length);
						}

						zos.closeEntry();

						// close the InputStream
						fis.close();
					}

					// close the ZipOutputStream
					zos.close();

				} catch (IOException ioe) {
					System.out.println("Error creating zip file" + ioe);
				}

			}

			modelMap.put("success", true);
			modelMap.put("message", "Report generated Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in report generation");
			return modelMap;
		}
	}*/
	
	@RequestMapping(value = "/timesheet/generateTimeSheetReport", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> generateTimeSheetReport(HttpServletRequest request) {

		logger.info("Generate Timesheet report Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);

		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		try {

			GemsEmployeeTimeSheetView searchGemsEmployeeTimeSheetView = new GemsEmployeeTimeSheetView();

			String timeMonthYear = "";

			if ((StringUtils.isNotBlank(request.getParameter("timeMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("timeMonthYear")))) {
				timeMonthYear = request.getParameter("timeMonthYear");
				searchGemsEmployeeTimeSheetView.setTimeMonthYear(request.getParameter("timeMonthYear"));	
				searchGemsEmployeeTimeSheetView.setTimeSheetApprovedStatus("APPROVED"); // WILL ONLY export approved time sheets
				
				
			}

			GemsProjectResourceMaster searchGemsProjectResourceMaster = new GemsProjectResourceMaster();
			List<GemsProjectResourceMaster> projectResourceList = projectMasterService
					.getAllGemsProjectResourceMasterList(searchGemsProjectResourceMaster);

			List<GemsEmployeeTimeSheetView> gemsEmployeeTimeSheetList = timeSheetService
					.getAllTimeSheets(searchGemsEmployeeTimeSheetView);

			String replacedtimeMonthYear = timeMonthYear.replaceFirst("/", "-");

			String uploadDirectoryBase = System.getProperty("catalina.base");
			String timesheetFileRepoString = uploadDirectoryBase + File.separator + "webapps" + File.separator
					+ "upload" + File.separator + "timesheet" + File.separator + replacedtimeMonthYear;
			File timesheetFileRepo = new File(timesheetFileRepoString);
			if (!(timesheetFileRepo.exists())) {
				timesheetFileRepo.mkdirs();
			}

			for (GemsProjectResourceMaster gemsProjectResourceMaster : projectResourceList) {

				GemsProjectMaster gemsProjectMaster = gemsProjectResourceMaster.getGemsProjectMaster();
				GemsEmployeeMaster gemsEmployeeMaster = gemsProjectResourceMaster.getGemsEmployeeMaster();
				List<GemsEmployeeTimeSheetView> resultedGemsEmployeeTimeSheet = new ArrayList<GemsEmployeeTimeSheetView>();

				for (GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView : gemsEmployeeTimeSheetList) {

					if ((gemsEmployeeTimeSheetView.getTimeMonthYear().equalsIgnoreCase(timeMonthYear))
							&& (gemsEmployeeTimeSheetView.getProjectId() == gemsProjectMaster
									.getGemsProjectMasterId())
							&& (gemsEmployeeTimeSheetView.getEmployeeId() == gemsEmployeeMaster
									.getGemsEmployeeMasterId())) {
						resultedGemsEmployeeTimeSheet.add(gemsEmployeeTimeSheetView);
					}
				}

				if (resultedGemsEmployeeTimeSheet.size() != 0) {
					
					GemsEmployeeTimeSheet updatedEmployeeTimeSheet = new GemsEmployeeTimeSheet();
					
					for (GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView : resultedGemsEmployeeTimeSheet) {
						
						String employeeName = "";
						String managerName = "";
						String employeePhone = "";
						String officialMailId = "";
						double totalHours = 0.0;
						String selectMonthYear = "";

						GemsEmployeeMaster timeSheetEmployee = employeeService
								.getGemsEmployeeMaster(gemsEmployeeTimeSheetView.getEmployeeId());
						employeeName = "" + timeSheetEmployee.getEmployeeLastName() + " "
								+ timeSheetEmployee.getEmployeeFirstName() + "";
						managerName = "" + timeSheetEmployee.getCurrentReportingTo().getEmployeeLastName() + " "
								+ timeSheetEmployee.getCurrentReportingTo().getEmployeeLastName() + "";
						if (timeSheetEmployee.getPersonalContactNumber() != null) {
							employeePhone = "" + timeSheetEmployee.getPersonalContactNumber() + "";
						}
						if (timeSheetEmployee.getOfficialEmailid() != null) {
							officialMailId = "" + timeSheetEmployee.getOfficialEmailid() + "";
						}
						
						selectMonthYear = gemsEmployeeTimeSheetView.getTimeMonthYear();

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

						// FileInputStream fis = new
						// FileInputStream(employeeTimeSheet);
						Workbook myWorkBook = new XSSFWorkbook();
						Sheet sheet = null;
						sheet = myWorkBook.createSheet("TimeSheet");

						sheet.setColumnWidth(0, 300);
						sheet.setColumnWidth(1, 7000);
						sheet.setColumnWidth(2, 6000);
						sheet.setColumnWidth(3, 6000);
						sheet.setColumnWidth(5, 6000);
						sheet.setColumnWidth(6, 6000);
						sheet.setColumnWidth(7, 6000);

						sheet.setDisplayGridlines(false);

						// Font fontTitle = sheet.getWorkbook().createFont();

						Font fontTitle = sheet.getWorkbook().createFont();
						fontTitle.setFontName("Verdana");
						fontTitle.setColor(IndexedColors.RED.index);
						fontTitle.setFontHeight((short) 400);

						CellStyle cellStyleTitle = sheet.getWorkbook().createCellStyle();
						cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER);
						cellStyleTitle.setWrapText(true);
						cellStyleTitle.setFont(fontTitle);

						Font fontGreen = sheet.getWorkbook().createFont();
						fontGreen.setFontName("Verdana");
						fontGreen.setColor(IndexedColors.GREEN.index);
						fontGreen.setFontHeight((short) 200);

						CellStyle cellGreen = sheet.getWorkbook().createCellStyle();
						cellGreen.setFont(fontGreen);

						// Red

						Font fontRed = sheet.getWorkbook().createFont();
						fontRed.setFontName("Verdana");
						fontRed.setColor(IndexedColors.RED.index);
						fontRed.setFontHeight((short) 200);

						CellStyle cellRed = sheet.getWorkbook().createCellStyle();
						cellRed.setFont(fontRed);

						// Black
						Font fontBlack = sheet.getWorkbook().createFont();
						fontBlack.setFontName("Verdana");
						// fontBlack.setColor(XSSFColor.BLACK.index);
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
						CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
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

						CellStyle tableDataCellStyle = sheet.getWorkbook().createCellStyle();
						tableDataCellStyle.setAlignment(CellStyle.ALIGN_LEFT);
						tableDataCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
						tableDataCellStyle.setWrapText(true);
						tableDataCellStyle.setFont(tablefont);

						tableDataCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderLeft(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderRight(CellStyle.BORDER_THIN);
						tableDataCellStyle.setBorderTop(CellStyle.BORDER_THIN);

						CellStyle cellBlack = sheet.getWorkbook().createCellStyle();
						cellBlack.setFont(fontBlack);

						Row rowTitle = sheet.createRow(0);
						rowTitle.setHeight((short) 500);
						Cell cellTitle = rowTitle.createCell(0);
						cellTitle.setCellValue("BPA Technologies - Time Sheet");
						cellTitle.setCellStyle(cellStyleTitle);

						// Create merged region for the report title
						sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

						Row rowTitle1 = sheet.createRow(1);
						rowTitle1.setHeight((short) 250);
						Cell cellTitle1 = rowTitle1.createCell(0);
						cellTitle1.setCellValue("");
						cellTitle1.setCellStyle(cellStyleTitle);
						// Create merged region for empty
						sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

						// detail print

						// Row2
						Row excelRow2 = sheet.createRow(2);
						excelRow2.setHeight((short) 300);

						Cell excelRow2Cell1 = excelRow2.createCell(1);
						excelRow2Cell1.setCellValue(" Employee        ");
						excelRow2Cell1.setCellStyle(cellGreen);

						Cell excelRow2Cell23 = excelRow2.createCell(2);
						excelRow2Cell23.setCellValue(employeeName);
						excelRow2Cell23.setCellStyle(cellRed);
						sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));

						Cell excelRow2Cell4 = excelRow2.createCell(5);
						excelRow2Cell4.setCellValue(" Manager        ");
						excelRow2Cell4.setCellStyle(cellGreen);

						Cell excelRow2Cell56 = excelRow2.createCell(6);
						excelRow2Cell56.setCellValue(managerName);
						excelRow2Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(2, 2, 6, 7));

						// Row3
						Row excelRow3 = sheet.createRow(3);
						excelRow3.setHeight((short) 300);

						Cell excelRow3Cell1 = excelRow3.createCell(1);
						excelRow3Cell1.setCellValue(" [Street Address]      ");
						excelRow3Cell1.setCellStyle(cellGreen);

						Cell excelRow3Cell23 = excelRow3.createCell(2);
						excelRow3Cell23.setCellValue("28, 30, Metro I,");
						excelRow3Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

						Cell excelRow3Cell4 = excelRow3.createCell(5);
						excelRow3Cell4.setCellValue(" Employee phone        ");
						excelRow3Cell4.setCellStyle(cellGreen);

						Cell excelRow3Cell56 = excelRow3.createCell(6);
						excelRow3Cell56.setCellValue(employeePhone);
						excelRow3Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(3, 3, 6, 7));

						// Row4
						Row excelRow4 = sheet.createRow(4);
						excelRow4.setHeight((short) 300);

						Cell excelRow4Cell1 = excelRow4.createCell(1);
						excelRow4Cell1.setCellValue(" [Address 2]      ");
						excelRow4Cell1.setCellStyle(cellGreen);

						Cell excelRow4Cell23 = excelRow4.createCell(2);
						excelRow4Cell23.setCellValue("Kodambakkam High Road");
						excelRow4Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(4, 4, 2, 3));

						Cell excelRow4Cell4 = excelRow4.createCell(5);
						excelRow4Cell4.setCellValue(" Employee e-mail       ");
						excelRow4Cell4.setCellStyle(cellGreen);

						Cell excelRow4Cell56 = excelRow4.createCell(6);
						excelRow4Cell56.setCellValue(officialMailId);
						excelRow4Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));

						// Row5
						Row excelRow5 = sheet.createRow(5);
						excelRow5.setHeight((short) 300);

						Cell excelRow5Cell1 = excelRow5.createCell(1);
						excelRow5Cell1.setCellValue(" [City, ST  ZIP Code]      ");
						excelRow5Cell1.setCellStyle(cellGreen);

						Cell excelRow5Cell23 = excelRow5.createCell(2);
						excelRow5Cell23.setCellValue("Kodambakkam High Road");
						excelRow5Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(5, 5, 2, 3));

						Cell excelRow5Cell4 = excelRow5.createCell(5);
						excelRow5Cell4.setCellValue(" Total Hours       ");
						excelRow5Cell4.setCellStyle(cellGreen);

						

						Row excelRow6 = sheet.createRow(6);
						excelRow6.setHeight((short) 300);

						Cell excelRow6Cell1 = excelRow6.createCell(1);
						excelRow6Cell1.setCellValue(" Project Name      ");
						excelRow6Cell1.setCellStyle(cellGreen);

						Cell excelRow6Cell23 = excelRow6.createCell(2);
						excelRow6Cell23.setCellValue(gemsEmployeeTimeSheetView.getProject());
						excelRow6Cell23.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(6, 6, 2, 3));

						Cell excelRow6Cell4 = excelRow6.createCell(5);
						excelRow6Cell4.setCellValue(" Client       ");
						excelRow6Cell4.setCellStyle(cellGreen);

						Cell excelRow6Cell56 = excelRow6.createCell(6);
						excelRow6Cell56.setCellValue(
								gemsEmployeeTimeSheetView.getCustomer());
						excelRow6Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(6, 6, 6, 7));

						Row excelRow7 = sheet.createRow(7);
						rowTitle1.setHeight((short) 250);
						Cell excelRow71 = rowTitle1.createCell(0);
						cellTitle1.setCellValue("");
						cellTitle1.setCellStyle(cellStyleTitle);
						// Create merged region for empty
						sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 7));

						List<String> weekEndDates = getWeekEndDates(selectMonthYear);

						int rowCount = 8;

						Row rowHeader = sheet.createRow(rowCount);
						rowHeader.setHeight((short) 500);
						int startColIndex = 1;
						Cell cell1 = rowHeader.createCell(1);
						cell1.setCellValue("Day");
						cell1.setCellStyle(headerCellStyle);

						Cell cell2 = rowHeader.createCell(2);
						cell2.setCellValue("Date");
						cell2.setCellStyle(headerCellStyle);

						Cell cell3 = rowHeader.createCell(3);
						cell3.setCellValue("Regualar Hours");
						cell3.setCellStyle(headerCellStyle);

						Cell cell4 = rowHeader.createCell(4);
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
							// System.out.println(""+sdf1.format(monthDate)+"
							// --"+new
							// SimpleDateFormat("EEEE").format(monthDate));
							String dayString = new SimpleDateFormat("EEEE").format(monthDate);

							// for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet
							// : resultedGemsEmployeeTimeSheet)
							// {
							double hours = 0.0;
							String comments = "";
							switch (j) {
							case 1:
								hours = gemsEmployeeTimeSheetView.getTimeDay1();
								comments = gemsEmployeeTimeSheetView.getCommentDay1();
								break;

							case 2:
								hours = gemsEmployeeTimeSheetView.getTimeDay2();
								comments = gemsEmployeeTimeSheetView.getCommentDay2();
								break;

							case 3:
								hours = gemsEmployeeTimeSheetView.getTimeDay3();
								comments = gemsEmployeeTimeSheetView.getCommentDay3();
								break;

							case 4:
								hours = gemsEmployeeTimeSheetView.getTimeDay4();
								comments = gemsEmployeeTimeSheetView.getCommentDay4();
								break;

							case 5:
								hours = gemsEmployeeTimeSheetView.getTimeDay5();
								comments = gemsEmployeeTimeSheetView.getCommentDay5();
								break;

							case 6:
								hours = gemsEmployeeTimeSheetView.getTimeDay6();
								comments = gemsEmployeeTimeSheetView.getCommentDay6();
								break;

							case 7:
								hours = gemsEmployeeTimeSheetView.getTimeDay7();
								comments = gemsEmployeeTimeSheetView.getCommentDay7();
								break;

							case 8:
								hours = gemsEmployeeTimeSheetView.getTimeDay8();
								comments = gemsEmployeeTimeSheetView.getCommentDay8();
								break;

							case 9:
								hours = gemsEmployeeTimeSheetView.getTimeDay9();
								comments = gemsEmployeeTimeSheetView.getCommentDay9();
								break;

							case 10:
								hours = gemsEmployeeTimeSheetView.getTimeDay10();
								comments = gemsEmployeeTimeSheetView.getCommentDay10();
								break;

							case 11:
								hours = gemsEmployeeTimeSheetView.getTimeDay11();
								comments = gemsEmployeeTimeSheetView.getCommentDay11();
								break;

							case 12:
								hours = gemsEmployeeTimeSheetView.getTimeDay12();
								comments = gemsEmployeeTimeSheetView.getCommentDay12();
								break;

							case 13:
								hours = gemsEmployeeTimeSheetView.getTimeDay13();
								comments = gemsEmployeeTimeSheetView.getCommentDay13();
								break;

							case 14:
								hours = gemsEmployeeTimeSheetView.getTimeDay14();
								comments = gemsEmployeeTimeSheetView.getCommentDay14();
								break;

							case 15:
								hours = gemsEmployeeTimeSheetView.getTimeDay15();
								comments = gemsEmployeeTimeSheetView.getCommentDay15();
								break;

							case 16:
								hours = gemsEmployeeTimeSheetView.getTimeDay16();
								comments = gemsEmployeeTimeSheetView.getCommentDay16();
								break;

							case 17:
								hours = gemsEmployeeTimeSheetView.getTimeDay17();
								comments = gemsEmployeeTimeSheetView.getCommentDay17();
								break;

							case 18:
								hours = gemsEmployeeTimeSheetView.getTimeDay18();
								comments = gemsEmployeeTimeSheetView.getCommentDay18();
								break;

							case 19:
								hours = gemsEmployeeTimeSheetView.getTimeDay19();
								comments = gemsEmployeeTimeSheetView.getCommentDay19();
								break;

							case 20:
								hours = gemsEmployeeTimeSheetView.getTimeDay20();
								comments = gemsEmployeeTimeSheetView.getCommentDay20();
								break;

							case 21:
								hours = gemsEmployeeTimeSheetView.getTimeDay21();
								comments = gemsEmployeeTimeSheetView.getCommentDay21();
								break;

							case 22:
								hours = gemsEmployeeTimeSheetView.getTimeDay22();
								comments = gemsEmployeeTimeSheetView.getCommentDay22();
								break;

							case 23:
								hours = gemsEmployeeTimeSheetView.getTimeDay23();
								comments = gemsEmployeeTimeSheetView.getCommentDay23();
								break;

							case 24:
								hours = gemsEmployeeTimeSheetView.getTimeDay24();
								comments = gemsEmployeeTimeSheetView.getCommentDay24();
								break;

							case 25:
								hours = gemsEmployeeTimeSheetView.getTimeDay25();
								comments = gemsEmployeeTimeSheetView.getCommentDay25();
								break;

							case 26:
								hours = gemsEmployeeTimeSheetView.getTimeDay26();
								comments = gemsEmployeeTimeSheetView.getCommentDay26();
								break;

							case 27:
								hours = gemsEmployeeTimeSheetView.getTimeDay27();
								comments = gemsEmployeeTimeSheetView.getCommentDay27();
								break;

							case 28:
								hours = gemsEmployeeTimeSheetView.getTimeDay28();
								comments = gemsEmployeeTimeSheetView.getCommentDay28();
								break;

							case 29:
								hours = gemsEmployeeTimeSheetView.getTimeDay29();
								comments = gemsEmployeeTimeSheetView.getCommentDay29();
								break;

							case 30:
								hours = gemsEmployeeTimeSheetView.getTimeDay30();
								comments = gemsEmployeeTimeSheetView.getCommentDay30();
								break;

							case 31:
								hours = gemsEmployeeTimeSheetView.getTimeDay31();
								comments = gemsEmployeeTimeSheetView.getCommentDay31();
								break;

							default:
								System.out.println("leave it");
							} // switch
							
							totalHours = totalHours + hours;
							String finalCommentString = "";
							if ((StringUtils.isNotBlank(comments)) || (StringUtils.isNotEmpty(comments))) {
								if (comments.startsWith("."))
								{
									finalCommentString = comments.replace(".", "");
								}
								else
								{
									
									String[] commentsArray = comments.split("\\.");
									if (commentsArray.length ==2)
									{
										StringBuffer sb = new StringBuffer();
										sb.append(commentsArray[0].toString());
										sb.append(System.getProperty("line.separator"));
										sb.append(commentsArray[1].toString());
										finalCommentString = sb.toString();
									}
									else
									{
										finalCommentString = commentsArray[0].toString();
									}
								}
							} 
							
							///////////////////////////////////////////////////// }
							///////////////////////////////////////////////////// //
							///////////////////////////////////////////////////// gems
							///////////////////////////////////////////////////// employee
							///////////////////////////////////////////////////// time
							///////////////////////////////////////////////////// sheet

							Row rowHeaderDetail = sheet.createRow(rowCount);
							rowHeaderDetail.setHeight((short) 500);

							Cell cell11 = rowHeaderDetail.createCell(1);
							cell11.setCellValue(dayString);
							cell11.setCellStyle(tableDataCellStyle);

							Cell cell21 = rowHeaderDetail.createCell(2);
							cell21.setCellValue(sdf1.format(monthDate));
							cell21.setCellStyle(tableDataCellStyle);

							Cell cell31 = rowHeaderDetail.createCell(3);
							cell31.setCellValue(hours);
							cell31.setCellStyle(tableDataCellStyle);

							Cell cell41 = rowHeaderDetail.createCell(4);
							cell41.setCellValue(finalCommentString);
							cell41.setCellStyle(tableDataCellStyle);

							Cell cell51 = rowHeaderDetail.createCell(5);
							cell51.setCellStyle(tableDataCellStyle);
							Cell cell61 = rowHeaderDetail.createCell(6);
							cell61.setCellStyle(tableDataCellStyle);

							Cell cell71 = rowHeaderDetail.createCell(7);
							cell71.setCellStyle(tableDataCellStyle);
							sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));

							rowCount++;

						}
						Row rowBottom = sheet.createRow(rowCount);
						rowBottom.setHeight((short) 500);

						Cell bottomCell1 = rowBottom.createCell(1);
						bottomCell1.setCellValue("Total");
						bottomCell1.setCellStyle(headerCellStyle);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1, 2));

						Cell bottomCell2 = rowBottom.createCell(3);
						bottomCell2.setCellValue(totalHours);
						bottomCell2.setCellStyle(headerCellStyle);

						Cell bottomCell4 = rowBottom.createCell(4);
						bottomCell4.setCellValue("");
						bottomCell4.setCellStyle(headerCellStyle);
						Cell cell111 = rowBottom.createCell(5);
						cell111.setCellStyle(tableDataCellStyle);
						Cell cell161 = rowBottom.createCell(6);
						cell161.setCellStyle(tableDataCellStyle);
						Cell cell171 = rowBottom.createCell(7);
						cell171.setCellStyle(tableDataCellStyle);
						sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 4, 7));
						
						Cell excelRow5Cell56 = excelRow5.createCell(6);
						excelRow5Cell56.setCellValue(totalHours);
						excelRow5Cell56.setCellStyle(cellBlack);
						sheet.addMergedRegion(new CellRangeAddress(5, 5, 6, 7));

						File employeeTimeSheet = new File(timesheetFileRepoString + File.separator
								+ replacedtimeMonthYear + "_" + gemsProjectMaster.getProjectName() + "_"
								+ gemsEmployeeMaster.getEmployeeCode() + ".xlsx");
						if (!(employeeTimeSheet.exists())) {
							employeeTimeSheet.createNewFile();
						}
						FileOutputStream os = new FileOutputStream(employeeTimeSheet);
						myWorkBook.write(os);
						os.close();
						os = null;

						
					}
					
					

				}

			}

			if (timesheetFileRepo.exists()) {
				String timesheetFileZipRepoString = uploadDirectoryBase + File.separator + "webapps" + File.separator
						+ "upload" + File.separator + "timesheet" + File.separator + replacedtimeMonthYear + ".zip";
				try {

					// create byte buffer
					byte[] buffer = new byte[1024];

					FileOutputStream fos = new FileOutputStream(timesheetFileZipRepoString);

					ZipOutputStream zos = new ZipOutputStream(fos);

					File dir = new File(timesheetFileRepoString);

					File[] files = dir.listFiles();

					for (int i = 0; i < files.length; i++) {

						System.out.println("Adding file: " + files[i].getName());

						FileInputStream fis = new FileInputStream(files[i]);

						// begin writing a new ZIP entry, positions the stream
						// to the start of the entry data
						zos.putNextEntry(new ZipEntry(files[i].getName()));

						int length;

						while ((length = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, length);
						}

						zos.closeEntry();

						// close the InputStream
						fis.close();
					}

					// close the ZipOutputStream
					zos.close();

				} catch (IOException ioe) {
					System.out.println("Error creating zip file" + ioe);
				}

			}

			modelMap.put("success", true);
			modelMap.put("message", "Report generated Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in report generation");
			return modelMap;
		}
	}

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

	@RequestMapping(value = "/timesheet/lockUnlockTimeSheet", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> lockUnlockTimeSheet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		logger.info("export TimeSheetTOExcel Method Strarted.,");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request, "loggedInUser");
		try {
			String weekDuration = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			
			GemsEmployeeTimeSheet searchGemsEmployeeTimeSheet = new GemsEmployeeTimeSheet();
			
			if ((StringUtils.isNotBlank(request.getParameter("selectedMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedMonthYear")))) {
				
				String selectedMonthYear = request.getParameter("selectedMonthYear");
				searchGemsEmployeeTimeSheet.setTimeMonthYear(selectedMonthYear);
				
			}
			if ((StringUtils.isNotBlank(request.getParameter("employeeCode")))
					|| (StringUtils.isNotEmpty(request.getParameter("employeeCode")))) {
				
				String employeeCode = request.getParameter("employeeCode");
				GemsEmployeeMaster searchGemsEmployeeMaster = new GemsEmployeeMaster();
				searchGemsEmployeeMaster.setEmployeeCode(employeeCode);
				GemsEmployeeMaster gemsEmployeeMaster = employeeService.getGemsEmployeeMasterByCode(searchGemsEmployeeMaster);
				if (gemsEmployeeMaster != null)
				{
					searchGemsEmployeeTimeSheet.setEmployeeId(gemsEmployeeMaster.getGemsEmployeeMasterId());
				}			
			}
			
			List<GemsEmployeeTimeSheet> gemsEmployeeTimeSheetList = timeSheetService
					.getAllGemsEmployeeTimeSheetList(searchGemsEmployeeTimeSheet);
			
			List<String> dates = new ArrayList<String>();
			
			if ((StringUtils.isNotBlank(request.getParameter("weekDuration")))
					|| (StringUtils.isNotEmpty(request.getParameter("weekDuration")))) {
				
				weekDuration = request.getParameter("weekDuration");
				if (!((weekDuration == "0") || (weekDuration.equalsIgnoreCase("0")))) {
					
					String[] weekDurationArray = weekDuration.split("-");
					String startDateString = weekDurationArray[0].toString();
					String endDateString = weekDurationArray[1].toString();
					
					Date startDate = dateFormat.parse(startDateString);
					Date endDate = dateFormat.parse(endDateString);

					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date endOfMonthDate = c.getTime();

					if (endDate.after(endOfMonthDate)) {
						endDate = endOfMonthDate;
					}

					
					Calendar calendar = new GregorianCalendar();
					calendar.setTime(startDate);

					while ((calendar.getTime().before(endDate)) || (calendar.getTime().equals(endDate))) {
						Date result = calendar.getTime();
						dates.add(dateFormat.format(result));
						calendar.add(Calendar.DATE, 1);
					}
					
				}
				
				
			}
			
			List<GemsEmployeeTimeSheet> updatedGemsEmployeeTimeSheet = new ArrayList<GemsEmployeeTimeSheet>();

			if ((gemsEmployeeTimeSheetList.size() != 0) && (dates.size() != 0)) {
				for (GemsEmployeeTimeSheet gemsEmployeeTimeSheet : gemsEmployeeTimeSheetList) {
					for (String day : dates) {
						String[] dayOfWeekArray = day.split("/");
						int dayOfWeek = Integer.parseInt(dayOfWeekArray[1].toString());

						switch (dayOfWeek) {
						case 1:
							gemsEmployeeTimeSheet.setIsDay1Locked(0);

							break;

						case 2:
							gemsEmployeeTimeSheet.setIsDay2Locked(0);

							break;

						case 3:
							gemsEmployeeTimeSheet.setIsDay3Locked(0);

							break;

						case 4:
							gemsEmployeeTimeSheet.setIsDay4Locked(0);

							break;

						case 5:
							gemsEmployeeTimeSheet.setIsDay5Locked(0);

							break;

						case 6:
							gemsEmployeeTimeSheet.setIsDay6Locked(0);

							break;

						case 7:
							gemsEmployeeTimeSheet.setIsDay7Locked(0);

							break;

						case 8:
							gemsEmployeeTimeSheet.setIsDay8Locked(0);

							break;

						case 9:
							gemsEmployeeTimeSheet.setIsDay9Locked(0);

							break;

						case 10:
							gemsEmployeeTimeSheet.setIsDay10Locked(0);

							break;

						case 11:
							gemsEmployeeTimeSheet.setIsDay11Locked(0);

							break;

						case 12:
							gemsEmployeeTimeSheet.setIsDay12Locked(0);

							break;

						case 13:
							gemsEmployeeTimeSheet.setIsDay13Locked(0);

							break;

						case 14:
							gemsEmployeeTimeSheet.setIsDay14Locked(0);

							break;

						case 15:
							gemsEmployeeTimeSheet.setIsDay15Locked(0);

							break;

						case 16:
							gemsEmployeeTimeSheet.setIsDay16Locked(0);

							break;

						case 17:
							gemsEmployeeTimeSheet.setIsDay17Locked(0);

							break;

						case 18:
							gemsEmployeeTimeSheet.setIsDay18Locked(0);

							break;

						case 19:
							gemsEmployeeTimeSheet.setIsDay19Locked(0);

							break;

						case 20:
							gemsEmployeeTimeSheet.setIsDay20Locked(0);

							break;

						case 21:
							gemsEmployeeTimeSheet.setIsDay21Locked(0);

							break;

						case 22:
							gemsEmployeeTimeSheet.setIsDay22Locked(0);

							break;

						case 23:
							gemsEmployeeTimeSheet.setIsDay23Locked(0);

							break;

						case 24:
							gemsEmployeeTimeSheet.setIsDay24Locked(0);

							break;

						case 25:
							gemsEmployeeTimeSheet.setIsDay25Locked(0);

							break;

						case 26:
							gemsEmployeeTimeSheet.setIsDay26Locked(0);

							break;

						case 27:
							gemsEmployeeTimeSheet.setIsDay27Locked(0);

							break;

						case 28:
							gemsEmployeeTimeSheet.setIsDay28Locked(0);

							break;

						case 29:
							gemsEmployeeTimeSheet.setIsDay29Locked(0);

							break;

						case 30:
							gemsEmployeeTimeSheet.setIsDay30Locked(0);

							break;

						case 31:
							gemsEmployeeTimeSheet.setIsDay31Locked(0);

							break;

						default:
							System.out.println("leave it");
						} // switch

					}
					updatedGemsEmployeeTimeSheet.add(gemsEmployeeTimeSheet);
				}
				timeSheetService.saveBatchTimeSheet(updatedGemsEmployeeTimeSheet);
			}
			
			

			

			modelMap.put("success", true);
			modelMap.put("message", "Report generated Successfully");
			return modelMap;

		} catch (Exception ex) {
			modelMap.put("success", false);
			modelMap.put("message", "Error in report generation");
			return modelMap;
		}

	}
	


	@RequestMapping(value = "/timesheet/getWeeksList", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> viewWeeksOfMonth(HttpServletRequest request) {

		try {
			int start = 0;
			int limit = 20;

			GemsUserMaster loggedInUser = (GemsUserMaster) WebUtils.getRequiredSessionAttribute(request,
					"loggedInUser");
			
			List<WeekStartEndDate> weekStartEndDateList = new ArrayList<WeekStartEndDate>();
			int totalResults = 0;
			if ((StringUtils.isNotBlank(request.getParameter("selectedMonthYear")))
					|| (StringUtils.isNotEmpty(request.getParameter("selectedMonthYear")))) 
			{
				
				String selectedMonthYear = request.getParameter("selectedMonthYear");
				String[] selectedMonthYearArray = selectedMonthYear.split("/");
				String selectedMonth = selectedMonthYearArray[0].toString();
				String selectedYear = selectedMonthYearArray[1].toString();
				
				int month = 0;
				int year = 0;
				
				if ((StringUtils.isNotBlank(selectedMonth))
						|| (StringUtils.isNotEmpty(selectedMonth))) 
				{
					month = Integer.parseInt(selectedMonth);
				}
				if ((StringUtils.isNotBlank(selectedYear))
						|| (StringUtils.isNotEmpty(selectedYear))) 
				{
					year = Integer.parseInt(selectedYear);
				}
				
				String DATE_FORMAT = "MM/dd/yyyy";
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				
				if ((month <= 12 && month >= 1) && (year >= 1900 && year <= 2099)) 
				{
					List<Date> weeksList = timeSheetService.getWeekStartEndDatesList(year, month);
					Iterator<Date> dateItr = weeksList.iterator();
					int counter = 1;
					
					while (dateItr.hasNext()) {
						
						WeekStartEndDate weekStartEndDate = new WeekStartEndDate();
						weekStartEndDate.setRowId(counter);
						String startDateString = sdf.format(dateItr.next());
						String endDateString = sdf.format(dateItr.next());
						weekStartEndDate.setWeekStartDate(startDateString);
						weekStartEndDate.setWeekEndDate(endDateString);
						weekStartEndDateList.add(weekStartEndDate);
					}
					
					
					totalResults = weekStartEndDateList.size();
					

				}					
			}
			return getModelMapWeeksList(weekStartEndDateList, totalResults);
			

		} catch (Exception e) {

			return getModelMapError("Error trying to List." + e.getMessage());
		}
	}
	private Map<String, Object> getModelMapWeeksList(List<WeekStartEndDate> list, int totalResults) {

		Map<String, Object> modelMap = new HashMap<String, Object>(3);
		modelMap.put("total", totalResults);
		modelMap.put("recordsTotal", totalResults);
		modelMap.put("recordsFiltered", totalResults);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonBeanProcessor(WeekStartEndDate.class, new JsonBeanProcessor() {
			public JSONObject processBean(Object bean, JsonConfig jsonConfig) {
				if (!(bean instanceof WeekStartEndDate)) {
					return new JSONObject(true);
				}

				WeekStartEndDate weekStartEndDate = (WeekStartEndDate) bean;
				
				String weekStartEndString = weekStartEndDate.getWeekStartDate() +"-"+weekStartEndDate.getWeekEndDate();
				
				return new JSONObject().element("rowId", weekStartEndDate.getRowId())
						.element("weekStartDate", weekStartEndDate.getWeekStartDate())
						.element("weekEndDate", weekStartEndDate.getWeekEndDate())
						.element("weekStartEndString", weekStartEndString);
			}
		});

		JSON json = JSONSerializer.toJSON(list, jsonConfig);

		/*---*/
		modelMap.put("data", json);
		modelMap.put("success", true);

		return modelMap;
	}


}
