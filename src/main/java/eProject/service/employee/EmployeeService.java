package eProject.service.employee;

import java.util.List;

import eProject.domain.employee.GemsEmpBankDetail;
import eProject.domain.employee.GemsEmpEducationDetail;
import eProject.domain.employee.GemsEmpSalaryComponent;
import eProject.domain.employee.GemsEmployeeContactDetail;
import eProject.domain.employee.GemsEmployeeDependentDetail;
import eProject.domain.employee.GemsEmployeeEmergencyContact;
import eProject.domain.employee.GemsEmployeeImmigrationDetail;
import eProject.domain.employee.GemsEmployeeJobDetail;
import eProject.domain.employee.GemsEmployeeLeaveLine;
import eProject.domain.employee.GemsEmployeeLeaveMaster;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.employee.GemsEmployeePaySlipDetail;
import eProject.domain.employee.GemsEmployeeSkillDetail;
import eProject.domain.employee.GemsEmployeeWorkExp;
import eProject.domain.employee.GemsEmplyeeLeaveSummary;

public interface EmployeeService {

	/*
	 * Gems Employee Master Methods
	 */

	public int getGemsEmployeeMasterFilterCount(GemsEmployeeMaster gemsEmployeeMaster);

	public List getGemsEmployeeMasterList(int start, int limit, GemsEmployeeMaster gemsEmployeeMaster);

	public void saveGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster);

	public void removeGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster);

	public GemsEmployeeMaster getGemsEmployeeMaster(Integer Id);

	public GemsEmployeeMaster getGemsEmployeeMasterByCode(GemsEmployeeMaster gemsEmployeeMaster);

	public GemsEmployeeMaster getGemsEmployeeMasterByUser(GemsEmployeeMaster gemsEmployeeMaster);

	public List getAllGemsEmployeeMasterList(GemsEmployeeMaster gemsEmployeeMaster);
	
	public void saveBatchEmployeeList(List employeeList);
	/*
	 * End of Gems Employee Master Methods
	 */

	/*
	 * Gems Employee Bank Methods
	 */

	public int getGemsEmpBankDetailFilterCount(GemsEmpBankDetail gemsEmpBankDetail);

	public List getGemsEmpBankDetailList(int start, int limit, GemsEmpBankDetail gemsEmpBankDetail);

	public void saveGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail);

	public void removeGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail);

	public GemsEmpBankDetail getGemsEmpBankDetail(Integer Id);

	/*
	 * End of Gems Employee Bank Methods
	 */

	/*
	 * Gems Employee Education Detail Methods
	 */
	public int getGemsEmpEducationDetailFilterCount(GemsEmpEducationDetail gemsEmpEducationDetail);

	public List getGemsEmpEducationDetailList(int start, int limit, GemsEmpEducationDetail gemsEmpEducationDetail);

	public void saveGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail);

	public void removeGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail);

	public GemsEmpEducationDetail getGemsEmpEducationDetail(Integer Id);

	/*
	 * End of Gems Education Detail Methods
	 */

	/*
	 * Gems Employee Contact Methods
	 */

	public int getGemsEmployeeContactDetailFilterCount(GemsEmployeeContactDetail gemsEmployeeContactDetail);

	public List getGemsEmployeeContactDetailList(int start, int limit,
			GemsEmployeeContactDetail gemsEmployeeContactDetail);

	public void saveGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail);

	public void removeGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail);

	public GemsEmployeeContactDetail getGemsEmployeeContactDetail(Integer Id);

	/*
	 * End of Gems Employee contact Detail Methods
	 */

	/*
	 * Gems Employee Dependent Methods
	 */

	public int getGemsEmployeeDependentDetailFilterCount(GemsEmployeeDependentDetail gemsEmployeeDependentDetail);

	public List getGemsEmployeeDependentDetailList(int start, int limit,
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail);

	public void saveGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail);

	public void removeGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail);

	public GemsEmployeeDependentDetail getGemsEmployeeDependentDetail(Integer Id);

	/*
	 * End of Gems Employee Dependent Detail Methods
	 */

	/*
	 * Gems Employee Emergency contact Methods
	 */

	public int getGemsEmployeeEmergencyContactFilterCount(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact);

	public List getGemsEmployeeEmergencyContactList(int start, int limit,
			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact);

	public void saveGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact);

	public void removeGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact);

	public GemsEmployeeEmergencyContact getGemsEmployeeEmergencyContact(Integer Id);

	/*
	 * End of Gems Employee Emergency contact Methods
	 */

	/*
	 * Gems Employee Immigration Methods
	 */

	public int getGemsEmployeeImmigrationDetailFilterCount(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail);

	public List getGemsEmployeeImmigrationDetailList(int start, int limit,
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail);

	public void saveGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail);

	public void removeGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail);

	public GemsEmployeeImmigrationDetail getGemsEmployeeImmigrationDetail(Integer Id);

	/*
	 * End of Gems Employee Immigration Methods
	 */

	/*
	 * Gems Employee Job Detail Methods
	 */

	public int getGemsEmployeeJobDetailFilterCount(GemsEmployeeJobDetail gemsEmployeeJobDetail);

	public List getGemsEmployeeJobDetailList(int start, int limit, GemsEmployeeJobDetail gemsEmployeeJobDetail);

	public void saveGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail);

	public void removeGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail);

	public GemsEmployeeJobDetail getGemsEmployeeJobDetail(Integer Id);

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmployeeWorkExp Methods
	 */

	public int getGemsEmployeeWorkExpFilterCount(GemsEmployeeWorkExp gemsEmployeeWorkExp);

	public List getGemsEmployeeWorkExpList(int start, int limit, GemsEmployeeWorkExp gemsEmployeeWorkExp);

	public void saveGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp);

	public void removeGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp);

	public GemsEmployeeWorkExp getGemsEmployeeWorkExp(Integer Id);

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmpSalaryComponent Methods
	 */

	public int getgemsEmpSalaryComponentFilterCount(GemsEmpSalaryComponent gemsEmpSalaryComponent);

	public List getgemsEmpSalaryComponentList(int start, int limit, GemsEmpSalaryComponent gemsEmpSalaryComponent);

	public void saveGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent);

	public void removeGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent);

	public GemsEmpSalaryComponent getGemsEmpSalaryComponent(Integer Id);

	/*
	 * End of Gems Employee Salary Component Methods
	 */

	/*
	 * GemsEmpLeaveSummary Methods
	 */

	public int getGemsEmplyeeLeaveSummaryFilterCount(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary);

	public List getGemsEmplyeeLeaveSummaryList(int start, int limit, GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary);

	public void saveGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary);

	public void removeGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary);

	public GemsEmplyeeLeaveSummary getGemsEmplyeeLeaveSummary(Integer Id);

	public List getAllLeaveSummaryByEmployee(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary);

	/*
	 * End of Gems Employee leave summary Component Methods
	 */

	/*
	 * Employee Leave Methods
	 */

	public int getGemsEmployeeLeaveMasterFilterCount(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster);

	public List getGemsEmployeeLeaveMasterList(int start, int limit, GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster);

	public void saveGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster);

	public void removeGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster);

	public GemsEmployeeLeaveMaster getGemsEmployeeLeaveMaster(Integer Id);

	/*
	 * End of employee leave methods
	 */

	/*
	 * Employee Leave Line methods
	 */

	public int getGemsEmployeeLeaveLineFilterCount(GemsEmployeeLeaveLine gemsEmployeeLeaveLine);

	public List getGemsEmployeeLeaveLineList(int start, int limit, GemsEmployeeLeaveLine gemsEmployeeLeaveLine);

	public void saveGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine);

	public void removeGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine);

	public GemsEmployeeLeaveLine getGemsEmployeeLeaveLine(Integer Id);

	public GemsEmployeeLeaveLine getMaxEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine);

	public List getAllGemsEmployeeLeaveLineList(GemsEmployeeLeaveLine gemsEmployeeLeaveLine);
	/*
	 * End of employee leave line methods
	 */

	/*
	 * Gems Employee Skill Methods
	 */

	public int getGemsEmployeeSkillDetailFilterCount(GemsEmployeeSkillDetail gemsEmployeeSkillDetail);

	public List getGemsEmployeeSkillDetailList(int start, int limit, GemsEmployeeSkillDetail gemsEmployeeSkillDetail);

	public void saveGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail);

	public void removeGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail);

	public GemsEmployeeSkillDetail getGemsEmployeeSkillDetail(Integer Id);

	/*
	 * End of Gems Employee Skill Detail Methods
	 */

	/*
	 * Gems Employee Payslip Detail
	 */

	public int getGemsEmployeePaySlipDetailFilterCount(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail);

	public List getGemsEmployeePaySlipDetailList(int start, int limit,
			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail);

	public void saveGemsEmployeePaySlipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail);

	public void removeGemsEmployeePayslipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail);

	public GemsEmployeePaySlipDetail getGemsEmployeePayslipDetail(Integer Id);

	public List getAllGemsEmployeePaySlipDetailList(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail);

	/*
	 * End of Gems Employee Payslip Detail
	 */
}
