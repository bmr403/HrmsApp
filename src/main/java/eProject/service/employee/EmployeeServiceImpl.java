package eProject.service.employee;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.employee.EmployeeDao;
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

@Service("employeeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	public EmployeeServiceImpl() {
	}

	/*
	 * Gems Employee Master Methods
	 */

	public int getGemsEmployeeMasterFilterCount(GemsEmployeeMaster gemsEmployeeMaster) {
		return employeeDao.getGemsEmployeeMasterFilterCount(gemsEmployeeMaster);
	}

	public List getGemsEmployeeMasterList(int start, int limit, GemsEmployeeMaster gemsEmployeeMaster) {
		return employeeDao.getGemsEmployeeMasterList(start, limit, gemsEmployeeMaster);
	}

	public void saveGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		employeeDao.saveGemsEmployeeMaster(gemsEmployeeMaster);
	}

	public void removeGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		employeeDao.removeGemsEmployeeMaster(gemsEmployeeMaster);
	}

	public GemsEmployeeMaster getGemsEmployeeMaster(Integer Id) {
		return employeeDao.getGemsEmployeeMaster(Id);
	}

	public GemsEmployeeMaster getGemsEmployeeMasterByCode(GemsEmployeeMaster gemsEmployeeMaster) {
		return employeeDao.getGemsEmployeeMasterByCode(gemsEmployeeMaster);
	}

	public GemsEmployeeMaster getGemsEmployeeMasterByUser(GemsEmployeeMaster gemsEmployeeMaster) {
		return employeeDao.getGemsEmployeeMasterByUser(gemsEmployeeMaster);
	}

	public List getAllGemsEmployeeMasterList(GemsEmployeeMaster gemsEmployeeMaster) {
		return employeeDao.getAllGemsEmployeeMasterList(gemsEmployeeMaster);
	}
	public void saveBatchEmployeeList(List employeeList) {
		employeeDao.saveBatchEmployeeList(employeeList);
	}

	/*
	 * End of Gems Employee Master Methods
	 */

	/*
	 * Gems Employee Bank Methods
	 */

	public int getGemsEmpBankDetailFilterCount(GemsEmpBankDetail gemsEmpBankDetail) {
		return employeeDao.getGemsEmpBankDetailFilterCount(gemsEmpBankDetail);
	}

	public List getGemsEmpBankDetailList(int start, int limit, GemsEmpBankDetail gemsEmpBankDetail) {
		return employeeDao.getGemsEmpBankDetailList(start, limit, gemsEmpBankDetail);
	}

	public void saveGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail) {
		employeeDao.saveGemsEmpBankDetail(gemsEmpBankDetail);
	}

	public void removeGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail) {
		employeeDao.removeGemsEmpBankDetail(gemsEmpBankDetail);
	}

	public GemsEmpBankDetail getGemsEmpBankDetail(Integer Id) {
		return employeeDao.getGemsEmpBankDetail(Id);
	}

	/*
	 * End of Gems Employee Bank Methods
	 */

	/*
	 * Gems Employee Education Detail Methods
	 */
	public int getGemsEmpEducationDetailFilterCount(GemsEmpEducationDetail gemsEmpEducationDetail) {
		return employeeDao.getGemsEmpEducationDetailFilterCount(gemsEmpEducationDetail);
	}

	public List getGemsEmpEducationDetailList(int start, int limit, GemsEmpEducationDetail gemsEmpEducationDetail) {
		return employeeDao.getGemsEmpEducationDetailList(start, limit, gemsEmpEducationDetail);
	}

	public void saveGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail) {
		employeeDao.saveGemsEmpEducationDetail(gemsEmpEducationDetail);
	}

	public void removeGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail) {
		employeeDao.removeGemsEmpEducationDetail(gemsEmpEducationDetail);
	}

	public GemsEmpEducationDetail getGemsEmpEducationDetail(Integer Id) {
		return employeeDao.getGemsEmpEducationDetail(Id);
	}

	/*
	 * End of Gems Education Detail Methods
	 */

	/*
	 * Gems Employee Contact Methods
	 */

	public int getGemsEmployeeContactDetailFilterCount(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		return employeeDao.getGemsEmployeeContactDetailFilterCount(gemsEmployeeContactDetail);
	}

	public List getGemsEmployeeContactDetailList(int start, int limit,
			GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		return employeeDao.getGemsEmployeeContactDetailList(start, limit, gemsEmployeeContactDetail);
	}

	public void saveGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		employeeDao.saveGemsEmployeeContactDetail(gemsEmployeeContactDetail);
	}

	public void removeGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		employeeDao.removeGemsEmployeeContactDetail(gemsEmployeeContactDetail);
	}

	public GemsEmployeeContactDetail getGemsEmployeeContactDetail(Integer Id) {
		return employeeDao.getGemsEmployeeContactDetail(Id);
	}

	/*
	 * End of Gems Employee contact Detail Methods
	 */

	/*
	 * Gems Employee Dependent Methods
	 */

	public int getGemsEmployeeDependentDetailFilterCount(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		return employeeDao.getGemsEmployeeDependentDetailFilterCount(gemsEmployeeDependentDetail);
	}

	public List getGemsEmployeeDependentDetailList(int start, int limit,
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		return employeeDao.getGemsEmployeeDependentDetailList(start, limit, gemsEmployeeDependentDetail);
	}

	public void saveGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		employeeDao.saveGemsEmployeeDependentDetail(gemsEmployeeDependentDetail);
	}

	public void removeGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		employeeDao.removeGemsEmployeeDependentDetail(gemsEmployeeDependentDetail);
	}

	public GemsEmployeeDependentDetail getGemsEmployeeDependentDetail(Integer Id) {
		return employeeDao.getGemsEmployeeDependentDetail(Id);
	}

	/*
	 * End of Gems Employee Dependent Detail Methods
	 */

	/*
	 * Gems Employee Emergency contact Methods
	 */

	public int getGemsEmployeeEmergencyContactFilterCount(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		return employeeDao.getGemsEmployeeEmergencyContactFilterCount(gemsEmployeeEmergencyContact);
	}

	public List getGemsEmployeeEmergencyContactList(int start, int limit,
			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		return employeeDao.getGemsEmployeeEmergencyContactList(start, limit, gemsEmployeeEmergencyContact);
	}

	public void saveGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		employeeDao.saveGemsEmployeeEmergencyContact(gemsEmployeeEmergencyContact);
	}

	public void removeGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		employeeDao.removeGemsEmployeeEmergencyContact(gemsEmployeeEmergencyContact);
	}

	public GemsEmployeeEmergencyContact getGemsEmployeeEmergencyContact(Integer Id) {
		return employeeDao.getGemsEmployeeEmergencyContact(Id);
	}

	/*
	 * End of Gems Employee Emergency contact Methods
	 */

	/*
	 * Gems Employee Immigration Methods
	 */

	public int getGemsEmployeeImmigrationDetailFilterCount(
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		return employeeDao.getGemsEmployeeImmigrationDetailFilterCount(gemsEmployeeImmigrationDetail);
	}

	public List getGemsEmployeeImmigrationDetailList(int start, int limit,
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		return employeeDao.getGemsEmployeeImmigrationDetailList(start, limit, gemsEmployeeImmigrationDetail);
	}

	public void saveGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		employeeDao.saveGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetail);
	}

	public void removeGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		employeeDao.removeGemsEmployeeImmigrationDetail(gemsEmployeeImmigrationDetail);
	}

	public GemsEmployeeImmigrationDetail getGemsEmployeeImmigrationDetail(Integer Id) {
		return employeeDao.getGemsEmployeeImmigrationDetail(Id);
	}

	/*
	 * End of Gems Employee Immigration Methods
	 */

	/*
	 * Gems Employee Job Detail Methods
	 */

	public int getGemsEmployeeJobDetailFilterCount(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		return employeeDao.getGemsEmployeeJobDetailFilterCount(gemsEmployeeJobDetail);
	}

	public List getGemsEmployeeJobDetailList(int start, int limit, GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		return employeeDao.getGemsEmployeeJobDetailList(start, limit, gemsEmployeeJobDetail);
	}

	public void saveGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		employeeDao.saveGemsEmployeeJobDetail(gemsEmployeeJobDetail);
	}

	public void removeGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		employeeDao.removeGemsEmployeeJobDetail(gemsEmployeeJobDetail);
	}

	public GemsEmployeeJobDetail getGemsEmployeeJobDetail(Integer Id) {
		return employeeDao.getGemsEmployeeJobDetail(Id);
	}

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmployeeWorkExp Methods
	 */

	public int getGemsEmployeeWorkExpFilterCount(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		return employeeDao.getGemsEmployeeWorkExpFilterCount(gemsEmployeeWorkExp);
	}

	public List getGemsEmployeeWorkExpList(int start, int limit, GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		return employeeDao.getGemsEmployeeWorkExpList(start, limit, gemsEmployeeWorkExp);
	}

	public void saveGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		employeeDao.saveGemsEmployeeWorkExp(gemsEmployeeWorkExp);
	}

	public void removeGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		employeeDao.removeGemsEmployeeWorkExp(gemsEmployeeWorkExp);
	}

	public GemsEmployeeWorkExp getGemsEmployeeWorkExp(Integer Id) {
		return employeeDao.getGemsEmployeeWorkExp(Id);
	}

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmpSalaryComponent Methods
	 */

	public int getgemsEmpSalaryComponentFilterCount(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		return employeeDao.getgemsEmpSalaryComponentFilterCount(gemsEmpSalaryComponent);
	}

	public List getgemsEmpSalaryComponentList(int start, int limit, GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		return employeeDao.getgemsEmpSalaryComponentList(start, limit, gemsEmpSalaryComponent);
	}

	public void saveGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		employeeDao.saveGemsEmpSalaryComponent(gemsEmpSalaryComponent);
	}

	public void removeGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		employeeDao.saveGemsEmpSalaryComponent(gemsEmpSalaryComponent);
	}

	public GemsEmpSalaryComponent getGemsEmpSalaryComponent(Integer Id) {
		return employeeDao.getGemsEmpSalaryComponent(Id);
	}

	/*
	 * End of Gems Employee Salary Component Methods
	 */

	/*
	 * GemsEmpLeaveSummary Methods
	 */

	public int getGemsEmplyeeLeaveSummaryFilterCount(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		return employeeDao.getGemsEmplyeeLeaveSummaryFilterCount(gemsEmplyeeLeaveSummary);
	}

	public List getGemsEmplyeeLeaveSummaryList(int start, int limit, GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		return employeeDao.getGemsEmplyeeLeaveSummaryList(start, limit, gemsEmplyeeLeaveSummary);
	}

	public void saveGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		employeeDao.saveGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	}

	public void removeGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		employeeDao.removeGemsEmplyeeLeaveSummary(gemsEmplyeeLeaveSummary);
	}

	public GemsEmplyeeLeaveSummary getGemsEmplyeeLeaveSummary(Integer Id) {
		return employeeDao.getGemsEmplyeeLeaveSummary(Id);
	}

	public List getAllLeaveSummaryByEmployee(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		return employeeDao.getAllLeaveSummaryByEmployee(gemsEmplyeeLeaveSummary);
	}

	/*
	 * End of Gems Employee Summary Component Methods
	 */

	/*
	 * Employee Leave Methods
	 */

	public int getGemsEmployeeLeaveMasterFilterCount(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		return employeeDao.getGemsEmployeeLeaveMasterFilterCount(gemsEmployeeLeaveMaster);
	}

	public List getGemsEmployeeLeaveMasterList(int start, int limit, GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		return employeeDao.getGemsEmployeeLeaveMasterList(start, limit, gemsEmployeeLeaveMaster);
	}

	public void saveGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		employeeDao.saveGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
	}

	public void removeGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		employeeDao.removeGemsEmployeeLeaveMaster(gemsEmployeeLeaveMaster);
	}

	public GemsEmployeeLeaveMaster getGemsEmployeeLeaveMaster(Integer Id) {
		return employeeDao.getGemsEmployeeLeaveMaster(Id);
	}

	/*
	 * End of employee leave methods
	 */

	/*
	 * Employee Leave Lines
	 */

	public int getGemsEmployeeLeaveLineFilterCount(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		return employeeDao.getGemsEmployeeLeaveLineFilterCount(gemsEmployeeLeaveLine);
	}

	public List getGemsEmployeeLeaveLineList(int start, int limit, GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		return employeeDao.getGemsEmployeeLeaveLineList(start, limit, gemsEmployeeLeaveLine);
	}

	public void saveGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		employeeDao.saveGemsEmployeeLeaveLine(gemsEmployeeLeaveLine);
	}

	public void removeGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		employeeDao.removeGemsEmployeeLeaveLine(gemsEmployeeLeaveLine);
	}

	public GemsEmployeeLeaveLine getGemsEmployeeLeaveLine(Integer Id) {
		return employeeDao.getGemsEmployeeLeaveLine(Id);
	}

	public GemsEmployeeLeaveLine getMaxEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		return employeeDao.getMaxEmployeeLeaveLine(gemsEmployeeLeaveLine);
	}

	public List getAllGemsEmployeeLeaveLineList(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		return employeeDao.getAllGemsEmployeeLeaveLineList(gemsEmployeeLeaveLine);
	}

	/*
	 * Gems Employee Skill Methods
	 */

	public int getGemsEmployeeSkillDetailFilterCount(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		return employeeDao.getGemsEmployeeSkillDetailFilterCount(gemsEmployeeSkillDetail);
	}

	public List getGemsEmployeeSkillDetailList(int start, int limit, GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		return employeeDao.getGemsEmployeeSkillDetailList(start, limit, gemsEmployeeSkillDetail);
	}

	public void saveGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		employeeDao.saveGemsEmployeeSkillDetail(gemsEmployeeSkillDetail);
	}

	public void removeGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		employeeDao.removeGemsEmployeeSkillDetail(gemsEmployeeSkillDetail);
	}

	public GemsEmployeeSkillDetail getGemsEmployeeSkillDetail(Integer Id) {
		return employeeDao.getGemsEmployeeSkillDetail(Id);
	}

	/*
	 * End of Gems Employee Skill Detail Methods
	 */

	/*
	 * Gems Employee Payslip Detail
	 */

	public int getGemsEmployeePaySlipDetailFilterCount(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		return employeeDao.getGemsEmployeePaySlipDetailFilterCount(gemsEmployeePaySlipDetail);
	}

	public List getGemsEmployeePaySlipDetailList(int start, int limit,
			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		return employeeDao.getGemsEmployeePaySlipDetailList(start, limit, gemsEmployeePaySlipDetail);
	}

	public void saveGemsEmployeePaySlipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		employeeDao.saveGemsEmployeePaySlipDetail(gemsEmployeePaySlipDetail);
	}

	public void removeGemsEmployeePayslipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		employeeDao.removeGemsEmployeePayslipDetail(gemsEmployeePaySlipDetail);
	}

	public GemsEmployeePaySlipDetail getGemsEmployeePayslipDetail(Integer Id) {
		return employeeDao.getGemsEmployeePayslipDetail(Id);
	}

	public List getAllGemsEmployeePaySlipDetailList(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		return employeeDao.getAllGemsEmployeePaySlipDetailList(gemsEmployeePaySlipDetail);
	}

	/*
	 * End of Gems Employee Payslip Detail
	 */
}
