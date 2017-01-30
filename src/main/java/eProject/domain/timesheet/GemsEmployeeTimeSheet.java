package eProject.domain.timesheet;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

import eProject.domain.project.GemsProjectMaster;

@Entity
@Table(name = "gems_employee_timesheet")
public class GemsEmployeeTimeSheet {

	@Id
	@Column(name = "GEMS_EMPLOYEE_TIMESHEET_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gemsEmployeeTimeSheetId;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID")
	protected GemsProjectMaster projectMaster;

	@ManyToOne
	@JoinColumn(name = "gems_employee_timesheet_header_id")
	protected GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader;

	@Column(name = "EMPLOYEE_ID")
	protected int employeeId;
	
	
	@Column(name = "active_status")
	private Integer activeStatus;

	@Column(name = "TASK_DESC")
	private String taskDescription;

	@Column(name = "TIME_MONTH_YEAR")
	private String timeMonthYear;

	@Column(name = "TOTAL_HOURS")
	private double totalHours;

	@Column(name = "TIME_DAY_1")
	private double timeDay1;

	@Column(name = "TIME_DAY_2")
	private double timeDay2;

	@Column(name = "TIME_DAY_3")
	private double timeDay3;

	@Column(name = "TIME_DAY_4")
	private double timeDay4;

	@Column(name = "TIME_DAY_5")
	private double timeDay5;

	@Column(name = "TIME_DAY_6")
	private double timeDay6;

	@Column(name = "TIME_DAY_7")
	private double timeDay7;

	@Column(name = "TIME_DAY_8")
	private double timeDay8;

	@Column(name = "TIME_DAY_9")
	private double timeDay9;

	@Column(name = "TIME_DAY_10")
	private double timeDay10;

	@Column(name = "TIME_DAY_11")
	private double timeDay11;

	@Column(name = "TIME_DAY_12")
	private double timeDay12;

	@Column(name = "TIME_DAY_13")
	private double timeDay13;

	@Column(name = "TIME_DAY_14")
	private double timeDay14;

	@Column(name = "TIME_DAY_15")
	private double timeDay15;

	@Column(name = "TIME_DAY_16")
	private double timeDay16;

	@Column(name = "TIME_DAY_17")
	private double timeDay17;

	@Column(name = "TIME_DAY_18")
	private double timeDay18;

	@Column(name = "TIME_DAY_19")
	private double timeDay19;

	@Column(name = "TIME_DAY_20")
	private double timeDay20;

	@Column(name = "TIME_DAY_21")
	private double timeDay21;

	@Column(name = "TIME_DAY_22")
	private double timeDay22;

	@Column(name = "TIME_DAY_23")
	private double timeDay23;

	@Column(name = "TIME_DAY_24")
	private double timeDay24;

	@Column(name = "TIME_DAY_25")
	private double timeDay25;

	@Column(name = "TIME_DAY_26")
	private double timeDay26;

	@Column(name = "TIME_DAY_27")
	private double timeDay27;

	@Column(name = "TIME_DAY_28")
	private double timeDay28;

	@Column(name = "TIME_DAY_29")
	private double timeDay29;

	@Column(name = "TIME_DAY_30")
	private double timeDay30;

	@Column(name = "TIME_DAY_31")
	private double timeDay31;

	@Column(name = "COMMENT_DAY_1")
	@Type(type="text")
	private String commentDay1;

	@Column(name = "COMMENT_DAY_2")
	@Type(type="text")
	private String commentDay2;

	@Column(name = "COMMENT_DAY_3")
	@Type(type="text")
	private String commentDay3;

	@Column(name = "COMMENT_DAY_4")
	@Type(type="text")
	private String commentDay4;

	@Column(name = "COMMENT_DAY_5")
	@Type(type="text")
	private String commentDay5;

	@Column(name = "COMMENT_DAY_6")
	@Type(type="text")
	private String commentDay6;

	@Column(name = "COMMENT_DAY_7")
	@Type(type="text")
	private String commentDay7;

	@Column(name = "COMMENT_DAY_8")
	@Type(type="text")
	private String commentDay8;

	@Column(name = "COMMENT_DAY_9")
	@Type(type="text")
	private String commentDay9;

	@Column(name = "COMMENT_DAY_10")
	@Type(type="text")
	private String commentDay10;

	@Column(name = "COMMENT_DAY_11")
	@Type(type="text")
	private String commentDay11;

	@Column(name = "COMMENT_DAY_12")
	@Type(type="text")
	private String commentDay12;

	@Column(name = "COMMENT_DAY_13")
	@Type(type="text")
	private String commentDay13;

	@Column(name = "COMMENT_DAY_14")
	@Type(type="text")
	private String commentDay14;

	@Column(name = "COMMENT_DAY_15")
	@Type(type="text")
	private String commentDay15;

	@Column(name = "COMMENT_DAY_16")
	@Type(type="text")
	private String commentDay16;

	@Column(name = "COMMENT_DAY_17")
	@Type(type="text")
	private String commentDay17;

	@Column(name = "COMMENT_DAY_18")
	@Type(type="text")
	private String commentDay18;

	@Column(name = "COMMENT_DAY_19")
	@Type(type="text")
	private String commentDay19;

	@Column(name = "COMMENT_DAY_20")
	@Type(type="text")
	private String commentDay20;

	@Column(name = "COMMENT_DAY_21")
	@Type(type="text")
	private String commentDay21;

	@Column(name = "COMMENT_DAY_22")
	@Type(type="text")
	private String commentDay22;

	@Column(name = "COMMENT_DAY_23")
	@Type(type="text")
	private String commentDay23;

	@Column(name = "COMMENT_DAY_24")
	@Type(type="text")
	private String commentDay24;

	@Column(name = "COMMENT_DAY_25")
	@Type(type="text")
	private String commentDay25;

	@Column(name = "COMMENT_DAY_26")
	@Type(type="text")
	private String commentDay26;

	@Column(name = "COMMENT_DAY_27")
	@Type(type="text")
	private String commentDay27;

	@Column(name = "COMMENT_DAY_28")
	@Type(type="text")
	private String commentDay28;

	@Column(name = "COMMENT_DAY_29")
	@Type(type="text")
	private String commentDay29;

	@Column(name = "COMMENT_DAY_30")
	@Type(type="text")
	private String commentDay30;

	@Column(name = "COMMENT_DAY_31")
	@Type(type="text")
	private String commentDay31;

	@Column(name = "IS_DAY_1_LOCKED")
	private Integer isDay1Locked;

	@Column(name = "IS_DAY_2_LOCKED")
	private Integer isDay2Locked;

	@Column(name = "IS_DAY_3_LOCKED")
	private Integer isDay3Locked;

	@Column(name = "IS_DAY_4_LOCKED")
	private Integer isDay4Locked;

	@Column(name = "IS_DAY_5_LOCKED")
	private Integer isDay5Locked;

	@Column(name = "IS_DAY_6_LOCKED")
	private Integer isDay6Locked;

	@Column(name = "IS_DAY_7_LOCKED")
	private Integer isDay7Locked;

	@Column(name = "IS_DAY_8_LOCKED")
	private Integer isDay8Locked;

	@Column(name = "IS_DAY_9_LOCKED")
	private Integer isDay9Locked;

	@Column(name = "IS_DAY_10_LOCKED")
	private Integer isDay10Locked;

	@Column(name = "IS_DAY_11_LOCKED")
	private Integer isDay11Locked;

	@Column(name = "IS_DAY_12_LOCKED")
	private Integer isDay12Locked;

	@Column(name = "IS_DAY_13_LOCKED")
	private Integer isDay13Locked;

	@Column(name = "IS_DAY_14_LOCKED")
	private Integer isDay14Locked;

	@Column(name = "IS_DAY_15_LOCKED")
	private Integer isDay15Locked;

	@Column(name = "IS_DAY_16_LOCKED")
	private Integer isDay16Locked;

	@Column(name = "IS_DAY_17_LOCKED")
	private Integer isDay17Locked;

	@Column(name = "IS_DAY_18_LOCKED")
	private Integer isDay18Locked;

	@Column(name = "IS_DAY_19_LOCKED")
	private Integer isDay19Locked;

	@Column(name = "IS_DAY_20_LOCKED")
	private Integer isDay20Locked;

	@Column(name = "IS_DAY_21_LOCKED")
	private Integer isDay21Locked;

	@Column(name = "IS_DAY_22_LOCKED")
	private Integer isDay22Locked;

	@Column(name = "IS_DAY_23_LOCKED")
	private Integer isDay23Locked;

	@Column(name = "IS_DAY_24_LOCKED")
	private Integer isDay24Locked;

	@Column(name = "IS_DAY_25_LOCKED")
	private Integer isDay25Locked;

	@Column(name = "IS_DAY_26_LOCKED")
	private Integer isDay26Locked;

	@Column(name = "IS_DAY_27_LOCKED")
	private Integer isDay27Locked;

	@Column(name = "IS_DAY_28_LOCKED")
	private Integer isDay28Locked;

	@Column(name = "IS_DAY_29_LOCKED")
	private Integer isDay29Locked;

	@Column(name = "IS_DAY_30_LOCKED")
	private Integer isDay30Locked;

	@Column(name = "IS_DAY_31_LOCKED")
	private Integer isDay31Locked;
	
	@Column(name = "LOCK_STATUS")
	private Integer lockStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "LOCK_STATUS_DATE")
	private Date lockStatusDate;
	

	public Integer getGemsEmployeeTimeSheetId() {
		return gemsEmployeeTimeSheetId;
	}

	public void setGemsEmployeeTimeSheetId(Integer gemsEmployeeTimeSheetId) {
		this.gemsEmployeeTimeSheetId = gemsEmployeeTimeSheetId;
	}

	public GemsProjectMaster getProjectMaster() {
		return projectMaster;
	}

	public void setProjectMaster(GemsProjectMaster projectMaster) {
		this.projectMaster = projectMaster;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getTimeMonthYear() {
		return timeMonthYear;
	}

	public void setTimeMonthYear(String timeMonthYear) {
		this.timeMonthYear = timeMonthYear;
	}

	public double getTimeDay1() {
		return timeDay1;
	}

	public void setTimeDay1(double timeDay1) {
		this.timeDay1 = timeDay1;
	}

	public double getTimeDay2() {
		return timeDay2;
	}

	public void setTimeDay2(double timeDay2) {
		this.timeDay2 = timeDay2;
	}

	public double getTimeDay3() {
		return timeDay3;
	}

	public void setTimeDay3(double timeDay3) {
		this.timeDay3 = timeDay3;
	}

	public double getTimeDay4() {
		return timeDay4;
	}

	public void setTimeDay4(double timeDay4) {
		this.timeDay4 = timeDay4;
	}

	public double getTimeDay5() {
		return timeDay5;
	}

	public void setTimeDay5(double timeDay5) {
		this.timeDay5 = timeDay5;
	}

	public double getTimeDay6() {
		return timeDay6;
	}

	public void setTimeDay6(double timeDay6) {
		this.timeDay6 = timeDay6;
	}

	public double getTimeDay7() {
		return timeDay7;
	}

	public void setTimeDay7(double timeDay7) {
		this.timeDay7 = timeDay7;
	}

	public double getTimeDay8() {
		return timeDay8;
	}

	public void setTimeDay8(double timeDay8) {
		this.timeDay8 = timeDay8;
	}

	public double getTimeDay9() {
		return timeDay9;
	}

	public void setTimeDay9(double timeDay9) {
		this.timeDay9 = timeDay9;
	}

	public double getTimeDay10() {
		return timeDay10;
	}

	public void setTimeDay10(double timeDay10) {
		this.timeDay10 = timeDay10;
	}

	public double getTimeDay11() {
		return timeDay11;
	}

	public void setTimeDay11(double timeDay11) {
		this.timeDay11 = timeDay11;
	}

	public double getTimeDay12() {
		return timeDay12;
	}

	public void setTimeDay12(double timeDay12) {
		this.timeDay12 = timeDay12;
	}

	public double getTimeDay13() {
		return timeDay13;
	}

	public void setTimeDay13(double timeDay13) {
		this.timeDay13 = timeDay13;
	}

	public double getTimeDay14() {
		return timeDay14;
	}

	public void setTimeDay14(double timeDay14) {
		this.timeDay14 = timeDay14;
	}

	public double getTimeDay15() {
		return timeDay15;
	}

	public void setTimeDay15(double timeDay15) {
		this.timeDay15 = timeDay15;
	}

	public double getTimeDay16() {
		return timeDay16;
	}

	public void setTimeDay16(double timeDay16) {
		this.timeDay16 = timeDay16;
	}

	public double getTimeDay17() {
		return timeDay17;
	}

	public void setTimeDay17(double timeDay17) {
		this.timeDay17 = timeDay17;
	}

	public double getTimeDay18() {
		return timeDay18;
	}

	public void setTimeDay18(double timeDay18) {
		this.timeDay18 = timeDay18;
	}

	public double getTimeDay19() {
		return timeDay19;
	}

	public void setTimeDay19(double timeDay19) {
		this.timeDay19 = timeDay19;
	}

	public double getTimeDay20() {
		return timeDay20;
	}

	public void setTimeDay20(double timeDay20) {
		this.timeDay20 = timeDay20;
	}

	public double getTimeDay21() {
		return timeDay21;
	}

	public void setTimeDay21(double timeDay21) {
		this.timeDay21 = timeDay21;
	}

	public double getTimeDay22() {
		return timeDay22;
	}

	public void setTimeDay22(double timeDay22) {
		this.timeDay22 = timeDay22;
	}

	public double getTimeDay23() {
		return timeDay23;
	}

	public void setTimeDay23(double timeDay23) {
		this.timeDay23 = timeDay23;
	}

	public double getTimeDay24() {
		return timeDay24;
	}

	public void setTimeDay24(double timeDay24) {
		this.timeDay24 = timeDay24;
	}

	public double getTimeDay25() {
		return timeDay25;
	}

	public void setTimeDay25(double timeDay25) {
		this.timeDay25 = timeDay25;
	}

	public double getTimeDay26() {
		return timeDay26;
	}

	public void setTimeDay26(double timeDay26) {
		this.timeDay26 = timeDay26;
	}

	public double getTimeDay27() {
		return timeDay27;
	}

	public void setTimeDay27(double timeDay27) {
		this.timeDay27 = timeDay27;
	}

	public double getTimeDay28() {
		return timeDay28;
	}

	public void setTimeDay28(double timeDay28) {
		this.timeDay28 = timeDay28;
	}

	public double getTimeDay29() {
		return timeDay29;
	}

	public void setTimeDay29(double timeDay29) {
		this.timeDay29 = timeDay29;
	}

	public double getTimeDay30() {
		return timeDay30;
	}

	public void setTimeDay30(double timeDay30) {
		this.timeDay30 = timeDay30;
	}

	public double getTimeDay31() {
		return timeDay31;
	}

	public void setTimeDay31(double timeDay31) {
		this.timeDay31 = timeDay31;
	}

	public String getCommentDay1() {
		return commentDay1;
	}

	public void setCommentDay1(String commentDay1) {
		this.commentDay1 = commentDay1;
	}

	public String getCommentDay2() {
		return commentDay2;
	}

	public void setCommentDay2(String commentDay2) {
		this.commentDay2 = commentDay2;
	}

	public String getCommentDay3() {
		return commentDay3;
	}

	public void setCommentDay3(String commentDay3) {
		this.commentDay3 = commentDay3;
	}

	public String getCommentDay4() {
		return commentDay4;
	}

	public void setCommentDay4(String commentDay4) {
		this.commentDay4 = commentDay4;
	}

	public String getCommentDay5() {
		return commentDay5;
	}

	public void setCommentDay5(String commentDay5) {
		this.commentDay5 = commentDay5;
	}

	public String getCommentDay6() {
		return commentDay6;
	}

	public void setCommentDay6(String commentDay6) {
		this.commentDay6 = commentDay6;
	}

	public String getCommentDay7() {
		return commentDay7;
	}

	public void setCommentDay7(String commentDay7) {
		this.commentDay7 = commentDay7;
	}

	public String getCommentDay8() {
		return commentDay8;
	}

	public void setCommentDay8(String commentDay8) {
		this.commentDay8 = commentDay8;
	}

	public String getCommentDay9() {
		return commentDay9;
	}

	public void setCommentDay9(String commentDay9) {
		this.commentDay9 = commentDay9;
	}

	public String getCommentDay10() {
		return commentDay10;
	}

	public void setCommentDay10(String commentDay10) {
		this.commentDay10 = commentDay10;
	}

	public String getCommentDay11() {
		return commentDay11;
	}

	public void setCommentDay11(String commentDay11) {
		this.commentDay11 = commentDay11;
	}

	public String getCommentDay12() {
		return commentDay12;
	}

	public void setCommentDay12(String commentDay12) {
		this.commentDay12 = commentDay12;
	}

	public String getCommentDay13() {
		return commentDay13;
	}

	public void setCommentDay13(String commentDay13) {
		this.commentDay13 = commentDay13;
	}

	public String getCommentDay14() {
		return commentDay14;
	}

	public void setCommentDay14(String commentDay14) {
		this.commentDay14 = commentDay14;
	}

	public String getCommentDay15() {
		return commentDay15;
	}

	public void setCommentDay15(String commentDay15) {
		this.commentDay15 = commentDay15;
	}

	public String getCommentDay16() {
		return commentDay16;
	}

	public void setCommentDay16(String commentDay16) {
		this.commentDay16 = commentDay16;
	}

	public String getCommentDay17() {
		return commentDay17;
	}

	public void setCommentDay17(String commentDay17) {
		this.commentDay17 = commentDay17;
	}

	public String getCommentDay18() {
		return commentDay18;
	}

	public void setCommentDay18(String commentDay18) {
		this.commentDay18 = commentDay18;
	}

	public String getCommentDay19() {
		return commentDay19;
	}

	public void setCommentDay19(String commentDay19) {
		this.commentDay19 = commentDay19;
	}

	public String getCommentDay20() {
		return commentDay20;
	}

	public void setCommentDay20(String commentDay20) {
		this.commentDay20 = commentDay20;
	}

	public String getCommentDay21() {
		return commentDay21;
	}

	public void setCommentDay21(String commentDay21) {
		this.commentDay21 = commentDay21;
	}

	public String getCommentDay22() {
		return commentDay22;
	}

	public void setCommentDay22(String commentDay22) {
		this.commentDay22 = commentDay22;
	}

	public String getCommentDay23() {
		return commentDay23;
	}

	public void setCommentDay23(String commentDay23) {
		this.commentDay23 = commentDay23;
	}

	public String getCommentDay24() {
		return commentDay24;
	}

	public void setCommentDay24(String commentDay24) {
		this.commentDay24 = commentDay24;
	}

	public String getCommentDay25() {
		return commentDay25;
	}

	public void setCommentDay25(String commentDay25) {
		this.commentDay25 = commentDay25;
	}

	public String getCommentDay26() {
		return commentDay26;
	}

	public void setCommentDay26(String commentDay26) {
		this.commentDay26 = commentDay26;
	}

	public String getCommentDay27() {
		return commentDay27;
	}

	public void setCommentDay27(String commentDay27) {
		this.commentDay27 = commentDay27;
	}

	public String getCommentDay28() {
		return commentDay28;
	}

	public void setCommentDay28(String commentDay28) {
		this.commentDay28 = commentDay28;
	}

	public String getCommentDay29() {
		return commentDay29;
	}

	public void setCommentDay29(String commentDay29) {
		this.commentDay29 = commentDay29;
	}

	public String getCommentDay30() {
		return commentDay30;
	}

	public void setCommentDay30(String commentDay30) {
		this.commentDay30 = commentDay30;
	}

	public String getCommentDay31() {
		return commentDay31;
	}

	public void setCommentDay31(String commentDay31) {
		this.commentDay31 = commentDay31;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public GemsEmployeeTimeSheetHeader getGemsEmployeeTimeSheetHeader() {
		return gemsEmployeeTimeSheetHeader;
	}

	public void setGemsEmployeeTimeSheetHeader(GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader) {
		this.gemsEmployeeTimeSheetHeader = gemsEmployeeTimeSheetHeader;
	}

	public Integer getIsDay1Locked() {
		return isDay1Locked;
	}

	public void setIsDay1Locked(Integer isDay1Locked) {
		this.isDay1Locked = isDay1Locked;
	}

	public Integer getIsDay2Locked() {
		return isDay2Locked;
	}

	public void setIsDay2Locked(Integer isDay2Locked) {
		this.isDay2Locked = isDay2Locked;
	}

	public Integer getIsDay3Locked() {
		return isDay3Locked;
	}

	public void setIsDay3Locked(Integer isDay3Locked) {
		this.isDay3Locked = isDay3Locked;
	}

	public Integer getIsDay4Locked() {
		return isDay4Locked;
	}

	public void setIsDay4Locked(Integer isDay4Locked) {
		this.isDay4Locked = isDay4Locked;
	}

	public Integer getIsDay5Locked() {
		return isDay5Locked;
	}

	public void setIsDay5Locked(Integer isDay5Locked) {
		this.isDay5Locked = isDay5Locked;
	}

	public Integer getIsDay6Locked() {
		return isDay6Locked;
	}

	public void setIsDay6Locked(Integer isDay6Locked) {
		this.isDay6Locked = isDay6Locked;
	}

	public Integer getIsDay7Locked() {
		return isDay7Locked;
	}

	public void setIsDay7Locked(Integer isDay7Locked) {
		this.isDay7Locked = isDay7Locked;
	}

	public Integer getIsDay8Locked() {
		return isDay8Locked;
	}

	public void setIsDay8Locked(Integer isDay8Locked) {
		this.isDay8Locked = isDay8Locked;
	}

	public Integer getIsDay9Locked() {
		return isDay9Locked;
	}

	public void setIsDay9Locked(Integer isDay9Locked) {
		this.isDay9Locked = isDay9Locked;
	}

	public Integer getIsDay10Locked() {
		return isDay10Locked;
	}

	public void setIsDay10Locked(Integer isDay10Locked) {
		this.isDay10Locked = isDay10Locked;
	}

	public Integer getIsDay11Locked() {
		return isDay11Locked;
	}

	public void setIsDay11Locked(Integer isDay11Locked) {
		this.isDay11Locked = isDay11Locked;
	}

	public Integer getIsDay12Locked() {
		return isDay12Locked;
	}

	public void setIsDay12Locked(Integer isDay12Locked) {
		this.isDay12Locked = isDay12Locked;
	}

	public Integer getIsDay13Locked() {
		return isDay13Locked;
	}

	public void setIsDay13Locked(Integer isDay13Locked) {
		this.isDay13Locked = isDay13Locked;
	}

	public Integer getIsDay14Locked() {
		return isDay14Locked;
	}

	public void setIsDay14Locked(Integer isDay14Locked) {
		this.isDay14Locked = isDay14Locked;
	}

	public Integer getIsDay15Locked() {
		return isDay15Locked;
	}

	public void setIsDay15Locked(Integer isDay15Locked) {
		this.isDay15Locked = isDay15Locked;
	}

	public Integer getIsDay16Locked() {
		return isDay16Locked;
	}

	public void setIsDay16Locked(Integer isDay16Locked) {
		this.isDay16Locked = isDay16Locked;
	}

	public Integer getIsDay17Locked() {
		return isDay17Locked;
	}

	public void setIsDay17Locked(Integer isDay17Locked) {
		this.isDay17Locked = isDay17Locked;
	}

	public Integer getIsDay18Locked() {
		return isDay18Locked;
	}

	public void setIsDay18Locked(Integer isDay18Locked) {
		this.isDay18Locked = isDay18Locked;
	}

	public Integer getIsDay19Locked() {
		return isDay19Locked;
	}

	public void setIsDay19Locked(Integer isDay19Locked) {
		this.isDay19Locked = isDay19Locked;
	}

	public Integer getIsDay20Locked() {
		return isDay20Locked;
	}

	public void setIsDay20Locked(Integer isDay20Locked) {
		this.isDay20Locked = isDay20Locked;
	}

	public Integer getIsDay21Locked() {
		return isDay21Locked;
	}

	public void setIsDay21Locked(Integer isDay21Locked) {
		this.isDay21Locked = isDay21Locked;
	}

	public Integer getIsDay22Locked() {
		return isDay22Locked;
	}

	public void setIsDay22Locked(Integer isDay22Locked) {
		this.isDay22Locked = isDay22Locked;
	}

	public Integer getIsDay23Locked() {
		return isDay23Locked;
	}

	public void setIsDay23Locked(Integer isDay23Locked) {
		this.isDay23Locked = isDay23Locked;
	}

	public Integer getIsDay24Locked() {
		return isDay24Locked;
	}

	public void setIsDay24Locked(Integer isDay24Locked) {
		this.isDay24Locked = isDay24Locked;
	}

	public Integer getIsDay25Locked() {
		return isDay25Locked;
	}

	public void setIsDay25Locked(Integer isDay25Locked) {
		this.isDay25Locked = isDay25Locked;
	}

	public Integer getIsDay26Locked() {
		return isDay26Locked;
	}

	public void setIsDay26Locked(Integer isDay26Locked) {
		this.isDay26Locked = isDay26Locked;
	}

	public Integer getIsDay27Locked() {
		return isDay27Locked;
	}

	public void setIsDay27Locked(Integer isDay27Locked) {
		this.isDay27Locked = isDay27Locked;
	}

	public Integer getIsDay28Locked() {
		return isDay28Locked;
	}

	public void setIsDay28Locked(Integer isDay28Locked) {
		this.isDay28Locked = isDay28Locked;
	}

	public Integer getIsDay29Locked() {
		return isDay29Locked;
	}

	public void setIsDay29Locked(Integer isDay29Locked) {
		this.isDay29Locked = isDay29Locked;
	}

	public Integer getIsDay30Locked() {
		return isDay30Locked;
	}

	public void setIsDay30Locked(Integer isDay30Locked) {
		this.isDay30Locked = isDay30Locked;
	}

	public Integer getIsDay31Locked() {
		return isDay31Locked;
	}

	public void setIsDay31Locked(Integer isDay31Locked) {
		this.isDay31Locked = isDay31Locked;
	}

	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Date getLockStatusDate() {
		return lockStatusDate;
	}

	public void setLockStatusDate(Date lockStatusDate) {
		this.lockStatusDate = lockStatusDate;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	

}
