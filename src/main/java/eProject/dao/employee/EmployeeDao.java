/**
 * 
 */
package eProject.dao.employee;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
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

@Repository("EmployeeDao")
public class EmployeeDao extends ErpAbstractDao {

	public EmployeeDao() {
		super();
	}

	/*
	 * Gems Employee Master Methods
	 */

	private DetachedCriteria createGemsEmployeeMasterCriteria(GemsEmployeeMaster gemsEmployeeMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsEmployeeMaster.getEmployeeCode() != null) {
				criteria.add(
						Restrictions.eq("employeeCode", "" + gemsEmployeeMaster.getEmployeeCode().toUpperCase() + ""));
			}

			if (gemsEmployeeMaster.getGemsUserMaster() != null) {
				criteria.add(Restrictions.eq("gemsUserMaster", gemsEmployeeMaster.getGemsUserMaster()));
			}
		} else {
			if (gemsEmployeeMaster.getEmployeeCode() != null) {
				criteria.add(Restrictions.like("employeeCode", "" + gemsEmployeeMaster.getEmployeeCode() + "%")
						.ignoreCase());
			}
			if (gemsEmployeeMaster.getEmployeeFirstName() != null) {
				criteria.add(Restrictions
						.like("employeeFirstName", "" + gemsEmployeeMaster.getEmployeeFirstName() + "%").ignoreCase());
			}
			if (gemsEmployeeMaster.getEmployeeLocation() != null) {
				criteria.add(Restrictions.eq("employeeLocation", gemsEmployeeMaster.getEmployeeLocation()));
			}
			if (gemsEmployeeMaster.getEmployeeLastName() != null) {
				criteria.add(Restrictions.like("employeeLastName", "" + gemsEmployeeMaster.getEmployeeLastName() + "%")
						.ignoreCase());
			}
			if (gemsEmployeeMaster.getOfficeContactNumber() != null) {
				Criterion offContactNo = Restrictions
						.like("officeContactNumber", "" + gemsEmployeeMaster.getOfficeContactNumber() + "%")
						.ignoreCase();
				Criterion perContactNo = Restrictions
						.like("personalContactNumber", "" + gemsEmployeeMaster.getPersonalContactNumber() + "%")
						.ignoreCase();
				LogicalExpression orExpContactNo = Restrictions.or(offContactNo, perContactNo);
				criteria.add(orExpContactNo);
			}
			if (gemsEmployeeMaster.getEmployeeJobDetails() != null) {
				criteria.createAlias("employeeJobDetails", "employeeJobDetails");
				criteria.add(Restrictions.eq("employeeJobDetails.gemsDepartment",
						gemsEmployeeMaster.getEmployeeJobDetails().getGemsDepartment()));
			}
			if (gemsEmployeeMaster.getCurrentEmployeeStatus() != null) {
				criteria.add(Restrictions.eq("currentEmployeeStatus", gemsEmployeeMaster.getCurrentEmployeeStatus()));
			}
			if (gemsEmployeeMaster.getPersonalEmailId() != null) {
				criteria.add(Restrictions.like("personalEmailId", "" + gemsEmployeeMaster.getPersonalEmailId() + "%")
						.ignoreCase());
			}
			if (gemsEmployeeMaster.getOfficialEmailid() != null) {
				criteria.add(Restrictions.like("officialEmailid", "" + gemsEmployeeMaster.getOfficialEmailid() + "%")
						.ignoreCase());
			}
			if ((gemsEmployeeMaster.getActiveStatus() == 1) || (gemsEmployeeMaster.getActiveStatus() == 0)) {
				criteria.add(Restrictions.eq("activeStatus", gemsEmployeeMaster.getActiveStatus()));
			}
			if (gemsEmployeeMaster.getGemsEmployeeMasterId() != 0) {
				Criterion cr1 = Restrictions.eq("gemsEmployeeMasterId", gemsEmployeeMaster.getGemsEmployeeMasterId());
				Criterion cr2 = Restrictions.eq("currentReportingTo", gemsEmployeeMaster.getCurrentReportingTo());
				criteria.add(Restrictions.or(cr1, cr2));
			}
			/*
			 * if (gemsEmployeeMaster.getCurrentReportingTo() != 0){ cr2 =
			 * Restrictions.eq("currentReportingTo",gemsEmployeeMaster.
			 * getCurrentReportingTo()); }
			 */

			if (gemsEmployeeMaster.getLeaveAllocationStatus() == 2) {
				criteria.add(Restrictions.eq("leaveAllocationStatus", gemsEmployeeMaster.getLeaveAllocationStatus()));
			}
			if (gemsEmployeeMaster.getGemsUserMaster() != null) {
				if (gemsEmployeeMaster.getGemsUserMaster().getGemsRoleMaster() != null) {
					criteria.createAlias("gemsUserMaster", "gemsUserMaster"); // inner
																				// join
																				// by
																				// default
					criteria.createAlias("gemsUserMaster.gemsRoleMaster", "gemsRoleMaster");
					criteria.add(Restrictions.eq("gemsRoleMaster.roleCode", "EMPLOYEE"));
				} else {
					// need to think.........................
				}

			} else {
				String[] roleCode = { "Administrator" };
				criteria.createAlias("gemsUserMaster", "gemsUserMaster"); // inner
																			// join
																			// by
																			// default
				criteria.createAlias("gemsUserMaster.gemsRoleMaster", "gemsRoleMaster");
				criteria.add(Restrictions.not((Restrictions.in("gemsRoleMaster.roleCode", roleCode))));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsEmployeeMaster.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsEmployeeMasterFilterCount(GemsEmployeeMaster gemsEmployeeMaster) {
		DetachedCriteria criteria = createGemsEmployeeMasterCriteria(gemsEmployeeMaster, "");
		return super.getObjectListCount(GemsEmployeeMaster.class, criteria);
	}

	public List getGemsEmployeeMasterList(int start, int limit, GemsEmployeeMaster gemsEmployeeMaster) {
		DetachedCriteria criteria = createGemsEmployeeMasterCriteria(gemsEmployeeMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsEmployeeMaster.class, criteria, start, limit);
	}

	public List getAllGemsEmployeeMasterList(GemsEmployeeMaster gemsEmployeeMaster) {
		DetachedCriteria criteria = createGemsEmployeeMasterCriteria(gemsEmployeeMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(GemsEmployeeMaster.class, criteria);
	}

	public void saveGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		super.saveOrUpdate(gemsEmployeeMaster);
	}

	public void removeGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		super.delete(gemsEmployeeMaster);
	}

	public GemsEmployeeMaster getGemsEmployeeMaster(Integer Id) {
		return (GemsEmployeeMaster) super.find(GemsEmployeeMaster.class, Id);
	}

	public GemsEmployeeMaster getGemsEmployeeMasterByCode(GemsEmployeeMaster gemsEmployeeMaster) {
		DetachedCriteria criteria = createGemsEmployeeMasterCriteria(gemsEmployeeMaster, "exact");
		return (GemsEmployeeMaster) super.checkUniqueCode(GemsEmployeeMaster.class, criteria);
	}

	public GemsEmployeeMaster getGemsEmployeeMasterByUser(GemsEmployeeMaster gemsEmployeeMaster) {
		DetachedCriteria criteria = createGemsEmployeeMasterCriteria(gemsEmployeeMaster, "exact");
		return (GemsEmployeeMaster) super.checkObjectByParameter(GemsEmployeeMaster.class, criteria);
	}
	
	public void saveBatchEmployeeList(List employeeList) {
		super.saveAll(employeeList);
	}

	/*
	 * End of Gems Employee Master Methods
	 */

	/*
	 * Gems Employee Bank Methods
	 */

	private DetachedCriteria createGemsEmpBankDetailCriteria(GemsEmpBankDetail gemsEmpBankDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmpBankDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmpBankDetail.getGemsEmployeeMaster()));

		return criteria;

	}

	public int getGemsEmpBankDetailFilterCount(GemsEmpBankDetail gemsEmpBankDetail) {
		DetachedCriteria criteria = createGemsEmpBankDetailCriteria(gemsEmpBankDetail, "");
		return super.getObjectListCount(GemsEmpBankDetail.class, criteria);
	}

	public List getGemsEmpBankDetailList(int start, int limit, GemsEmpBankDetail gemsEmpBankDetail) {
		DetachedCriteria criteria = createGemsEmpBankDetailCriteria(gemsEmpBankDetail, "");
		return super.getObjectListByRange(GemsEmpBankDetail.class, criteria, start, limit);
	}

	public void saveGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail) {
		super.saveOrUpdate(gemsEmpBankDetail);
	}

	public void removeGemsEmpBankDetail(GemsEmpBankDetail gemsEmpBankDetail) {
		super.delete(gemsEmpBankDetail);
	}

	public GemsEmpBankDetail getGemsEmpBankDetail(Integer Id) {
		return (GemsEmpBankDetail) super.find(GemsEmpBankDetail.class, Id);
	}

	/*
	 * End of Gems Employee Bank Methods
	 */

	/*
	 * Gems Employee Education Detail Methods
	 */

	private DetachedCriteria createGemsEmpEducationDetailCriteria(GemsEmpEducationDetail gemsEmpEducationDetail,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmpEducationDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmpEducationDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmpEducationDetailFilterCount(GemsEmpEducationDetail gemsEmpEducationDetail) {
		DetachedCriteria criteria = createGemsEmpEducationDetailCriteria(gemsEmpEducationDetail, "");
		return super.getObjectListCount(GemsEmpEducationDetail.class, criteria);
	}

	public List getGemsEmpEducationDetailList(int start, int limit, GemsEmpEducationDetail gemsEmpEducationDetail) {
		DetachedCriteria criteria = createGemsEmpEducationDetailCriteria(gemsEmpEducationDetail, "");
		return super.getObjectListByRange(GemsEmpEducationDetail.class, criteria, start, limit);
	}

	public void saveGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail) {
		super.saveOrUpdate(gemsEmpEducationDetail);
	}

	public void removeGemsEmpEducationDetail(GemsEmpEducationDetail gemsEmpEducationDetail) {
		super.delete(gemsEmpEducationDetail);
	}

	public GemsEmpEducationDetail getGemsEmpEducationDetail(Integer Id) {
		return (GemsEmpEducationDetail) super.find(GemsEmpEducationDetail.class, Id);
	}

	/*
	 * End of Gems Education Detail Methods
	 */

	/*
	 * Gems Employee Contact Methods
	 */

	private DetachedCriteria createGemsEmployeeContactDetailCriteria(
			GemsEmployeeContactDetail gemsEmployeeContactDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeContactDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeContactDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeContactDetailFilterCount(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		DetachedCriteria criteria = createGemsEmployeeContactDetailCriteria(gemsEmployeeContactDetail, "");
		return super.getObjectListCount(GemsEmployeeContactDetail.class, criteria);
	}

	public List getGemsEmployeeContactDetailList(int start, int limit,
			GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		DetachedCriteria criteria = createGemsEmployeeContactDetailCriteria(gemsEmployeeContactDetail, "");
		return super.getObjectListByRange(GemsEmployeeContactDetail.class, criteria, start, limit);
	}

	public void saveGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		super.saveOrUpdate(gemsEmployeeContactDetail);
	}

	public void removeGemsEmployeeContactDetail(GemsEmployeeContactDetail gemsEmployeeContactDetail) {
		super.delete(gemsEmployeeContactDetail);
	}

	public GemsEmployeeContactDetail getGemsEmployeeContactDetail(Integer Id) {
		return (GemsEmployeeContactDetail) super.find(GemsEmployeeContactDetail.class, Id);
	}

	/*
	 * End of Gems Employee contact Detail Methods
	 */

	/*
	 * Gems Employee Dependent Methods
	 */

	private DetachedCriteria createGemsEmployeeDependentDetailCriteria(
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeDependentDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeDependentDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeDependentDetailFilterCount(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		DetachedCriteria criteria = createGemsEmployeeDependentDetailCriteria(gemsEmployeeDependentDetail, "");
		return super.getObjectListCount(GemsEmployeeDependentDetail.class, criteria);
	}

	public List getGemsEmployeeDependentDetailList(int start, int limit,
			GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		DetachedCriteria criteria = createGemsEmployeeDependentDetailCriteria(gemsEmployeeDependentDetail, "");
		return super.getObjectListByRange(GemsEmployeeDependentDetail.class, criteria, start, limit);
	}

	public void saveGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		super.saveOrUpdate(gemsEmployeeDependentDetail);
	}

	public void removeGemsEmployeeDependentDetail(GemsEmployeeDependentDetail gemsEmployeeDependentDetail) {
		super.delete(gemsEmployeeDependentDetail);
	}

	public GemsEmployeeDependentDetail getGemsEmployeeDependentDetail(Integer Id) {
		return (GemsEmployeeDependentDetail) super.find(GemsEmployeeDependentDetail.class, Id);
	}

	/*
	 * End of Gems Employee Dependent Detail Methods
	 */

	/*
	 * Gems Employee Emergency contact Methods
	 */

	private DetachedCriteria createGemsEmployeeEmergencyContactCriteria(
			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeEmergencyContact.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeEmergencyContact.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeEmergencyContactFilterCount(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		DetachedCriteria criteria = createGemsEmployeeEmergencyContactCriteria(gemsEmployeeEmergencyContact, "");
		return super.getObjectListCount(GemsEmployeeEmergencyContact.class, criteria);
	}

	public List getGemsEmployeeEmergencyContactList(int start, int limit,
			GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		DetachedCriteria criteria = createGemsEmployeeEmergencyContactCriteria(gemsEmployeeEmergencyContact, "");
		return super.getObjectListByRange(GemsEmployeeEmergencyContact.class, criteria, start, limit);
	}

	public void saveGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		super.saveOrUpdate(gemsEmployeeEmergencyContact);
	}

	public void removeGemsEmployeeEmergencyContact(GemsEmployeeEmergencyContact gemsEmployeeEmergencyContact) {
		super.delete(gemsEmployeeEmergencyContact);
	}

	public GemsEmployeeEmergencyContact getGemsEmployeeEmergencyContact(Integer Id) {
		return (GemsEmployeeEmergencyContact) super.find(GemsEmployeeEmergencyContact.class, Id);
	}

	/*
	 * End of Gems Employee Emergency contact Methods
	 */

	/*
	 * Gems Employee Immigration Methods
	 */

	private DetachedCriteria createGemsEmployeeImmigrationDetailCriteria(
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeImmigrationDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeImmigrationDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeImmigrationDetailFilterCount(
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		DetachedCriteria criteria = createGemsEmployeeImmigrationDetailCriteria(gemsEmployeeImmigrationDetail, "");
		return super.getObjectListCount(GemsEmployeeImmigrationDetail.class, criteria);
	}

	public List getGemsEmployeeImmigrationDetailList(int start, int limit,
			GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		DetachedCriteria criteria = createGemsEmployeeImmigrationDetailCriteria(gemsEmployeeImmigrationDetail, "");
		return super.getObjectListByRange(GemsEmployeeImmigrationDetail.class, criteria, start, limit);
	}

	public void saveGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		super.saveOrUpdate(gemsEmployeeImmigrationDetail);
	}

	public void removeGemsEmployeeImmigrationDetail(GemsEmployeeImmigrationDetail gemsEmployeeImmigrationDetail) {
		super.delete(gemsEmployeeImmigrationDetail);
	}

	public GemsEmployeeImmigrationDetail getGemsEmployeeImmigrationDetail(Integer Id) {
		return (GemsEmployeeImmigrationDetail) super.find(GemsEmployeeImmigrationDetail.class, Id);
	}

	/*
	 * End of Gems Employee Immigration Methods
	 */

	/*
	 * Gems Employee Job Detail Methods
	 */

	private DetachedCriteria createGemsEmployeeJobDetailCriteria(GemsEmployeeJobDetail gemsEmployeeJobDetail,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeJobDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeJobDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeJobDetailFilterCount(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		DetachedCriteria criteria = createGemsEmployeeJobDetailCriteria(gemsEmployeeJobDetail, "");
		return super.getObjectListCount(GemsEmployeeJobDetail.class, criteria);
	}

	public List getGemsEmployeeJobDetailList(int start, int limit, GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		DetachedCriteria criteria = createGemsEmployeeJobDetailCriteria(gemsEmployeeJobDetail, "");
		return super.getObjectListByRange(GemsEmployeeJobDetail.class, criteria, start, limit);
	}

	public void saveGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		super.saveOrUpdate(gemsEmployeeJobDetail);
	}

	public void removeGemsEmployeeJobDetail(GemsEmployeeJobDetail gemsEmployeeJobDetail) {
		super.delete(gemsEmployeeJobDetail);
	}

	public GemsEmployeeJobDetail getGemsEmployeeJobDetail(Integer Id) {
		return (GemsEmployeeJobDetail) super.find(GemsEmployeeJobDetail.class, Id);
	}

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmployeeWorkExp Methods
	 */

	private DetachedCriteria createGemsEmployeeWorkExpCriteria(GemsEmployeeWorkExp gemsEmployeeWorkExp,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeWorkExp.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeWorkExp.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeWorkExpFilterCount(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		DetachedCriteria criteria = createGemsEmployeeWorkExpCriteria(gemsEmployeeWorkExp, "");
		return super.getObjectListCount(GemsEmployeeWorkExp.class, criteria);
	}

	public List getGemsEmployeeWorkExpList(int start, int limit, GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		DetachedCriteria criteria = createGemsEmployeeWorkExpCriteria(gemsEmployeeWorkExp, "");
		return super.getObjectListByRange(GemsEmployeeWorkExp.class, criteria, start, limit);
	}

	public void saveGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		super.saveOrUpdate(gemsEmployeeWorkExp);
	}

	public void removeGemsEmployeeWorkExp(GemsEmployeeWorkExp gemsEmployeeWorkExp) {
		super.delete(gemsEmployeeWorkExp);
	}

	public GemsEmployeeWorkExp getGemsEmployeeWorkExp(Integer Id) {
		return (GemsEmployeeWorkExp) super.find(GemsEmployeeWorkExp.class, Id);
	}

	/*
	 * End of Gems Employee Job Detail Methods
	 */

	/*
	 * GemsEmpSalaryComponent Methods
	 */

	private DetachedCriteria createGemsEmpSalaryComponentCriteria(GemsEmpSalaryComponent gemsEmpSalaryComponent,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmpSalaryComponent.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmpSalaryComponent.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getgemsEmpSalaryComponentFilterCount(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		DetachedCriteria criteria = createGemsEmpSalaryComponentCriteria(gemsEmpSalaryComponent, "");
		return super.getObjectListCount(GemsEmpSalaryComponent.class, criteria);
	}

	public List getgemsEmpSalaryComponentList(int start, int limit, GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		DetachedCriteria criteria = createGemsEmpSalaryComponentCriteria(gemsEmpSalaryComponent, "");
		return super.getObjectListByRange(GemsEmpSalaryComponent.class, criteria, start, limit);
	}

	public void saveGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		super.saveOrUpdate(gemsEmpSalaryComponent);
	}

	public void removeGemsEmpSalaryComponent(GemsEmpSalaryComponent gemsEmpSalaryComponent) {
		super.delete(gemsEmpSalaryComponent);
	}

	public GemsEmpSalaryComponent getGemsEmpSalaryComponent(Integer Id) {
		return (GemsEmpSalaryComponent) super.find(GemsEmpSalaryComponent.class, Id);
	}

	/*
	 * End of Gems Employee Salary Component Methods
	 */

	/*
	 * GemsEmpLeaveSummary Methods
	 */

	private DetachedCriteria createGemsEmplyeeLeaveSummaryCriteria(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmplyeeLeaveSummary.class);

		if (gemsEmplyeeLeaveSummary.getGemsEmployeeMaster() != null) {
			criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmplyeeLeaveSummary.getGemsEmployeeMaster()));
		}

		criteria.add(Restrictions.eq("gemsOrganisation", gemsEmplyeeLeaveSummary.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsEmplyeeLeaveSummaryFilterCount(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		DetachedCriteria criteria = createGemsEmplyeeLeaveSummaryCriteria(gemsEmplyeeLeaveSummary, "");
		return super.getObjectListCount(GemsEmplyeeLeaveSummary.class, criteria);
	}

	public List getGemsEmplyeeLeaveSummaryList(int start, int limit, GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		DetachedCriteria criteria = createGemsEmplyeeLeaveSummaryCriteria(gemsEmplyeeLeaveSummary, "");
		return super.getObjectListByRange(GemsEmplyeeLeaveSummary.class, criteria, start, limit);
	}

	public void saveGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		super.saveOrUpdate(gemsEmplyeeLeaveSummary);
	}

	public void removeGemsEmplyeeLeaveSummary(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		super.delete(gemsEmplyeeLeaveSummary);
	}

	public List getAllLeaveSummaryByEmployee(GemsEmplyeeLeaveSummary gemsEmplyeeLeaveSummary) {
		DetachedCriteria criteria = createGemsEmplyeeLeaveSummaryCriteria(gemsEmplyeeLeaveSummary, "");
		return super.getAllObjectList(GemsEmplyeeLeaveSummary.class, criteria);
	}

	public GemsEmplyeeLeaveSummary getGemsEmplyeeLeaveSummary(Integer Id) {
		return (GemsEmplyeeLeaveSummary) super.find(GemsEmplyeeLeaveSummary.class, Id);
	}
	/*
	 * public void saveGemsEmployeeLeaveSummarList(List
	 * gemsEmplyeeLeaveSummaryList) {
	 * super.saveAll(gemsEmplyeeLeaveSummaryList); }
	 */

	/*
	 * End of Gems Employee Leave Summary Methods
	 */

	/*
	 * GemsEmp Leave Methods
	 */

	private DetachedCriteria createGemsEmployeeLeaveMasterCriteria(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeLeaveMaster.class);

		if (gemsEmployeeLeaveMaster.getGemsEmployeeMaster() != null) {
			criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeLeaveMaster.getGemsEmployeeMaster()));
		}

		// criteria.add(Restrictions.eq("gemsOrganisation",gemsEmplyeeLeaveSummary.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsEmployeeLeaveMasterFilterCount(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		DetachedCriteria criteria = createGemsEmployeeLeaveMasterCriteria(gemsEmployeeLeaveMaster, "");
		return super.getObjectListCount(GemsEmployeeLeaveMaster.class, criteria);
	}

	public List getGemsEmployeeLeaveMasterList(int start, int limit, GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		DetachedCriteria criteria = createGemsEmployeeLeaveMasterCriteria(gemsEmployeeLeaveMaster, "");
		criteria.addOrder(Order.desc("gemsEmployeeLeaveId"));
		return super.getObjectListByRange(GemsEmployeeLeaveMaster.class, criteria, start, limit);
	}

	public void saveGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		super.saveOrUpdate(gemsEmployeeLeaveMaster);
	}

	public void removeGemsEmployeeLeaveMaster(GemsEmployeeLeaveMaster gemsEmployeeLeaveMaster) {
		super.delete(gemsEmployeeLeaveMaster);
	}

	public GemsEmployeeLeaveMaster getGemsEmployeeLeaveMaster(Integer Id) {
		return (GemsEmployeeLeaveMaster) super.find(GemsEmployeeLeaveMaster.class, Id);
	}

	/*
	 * End of Gems Employee leave Methods
	 */

	/*
	 * GemsEmp Leave line Methods
	 */

	private DetachedCriteria createGemsEmployeeLeaveLineCriteria(GemsEmployeeLeaveLine gemsEmployeeLeaveLine,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeLeaveLine.class);

		if (gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster() != null) {
			criteria.add(
					Restrictions.eq("gemsEmployeeLeaveMaster", gemsEmployeeLeaveLine.getGemsEmployeeLeaveMaster()));
		}
		if (gemsEmployeeLeaveLine.getApprover() != null) {
			criteria.add(Restrictions.eq("approver", gemsEmployeeLeaveLine.getApprover()));
		}
		if (gemsEmployeeLeaveLine.getApprovedStatus() != null) {
			criteria.add(Restrictions.eq("approvedStatus", "" + gemsEmployeeLeaveLine.getApprovedStatus() + ""));
		}

		return criteria;

	}

	public int getGemsEmployeeLeaveLineFilterCount(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		DetachedCriteria criteria = createGemsEmployeeLeaveLineCriteria(gemsEmployeeLeaveLine, "");
		return super.getObjectListCount(GemsEmployeeLeaveLine.class, criteria);
	}

	public List getGemsEmployeeLeaveLineList(int start, int limit, GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		DetachedCriteria criteria = createGemsEmployeeLeaveLineCriteria(gemsEmployeeLeaveLine, "");
		criteria.addOrder(Order.desc("gemsEmployeeLeaveLineId"));
		return super.getObjectListByRange(GemsEmployeeLeaveLine.class, criteria, start, limit);
	}

	public List getAllGemsEmployeeLeaveLineList(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		DetachedCriteria criteria = createGemsEmployeeLeaveLineCriteria(gemsEmployeeLeaveLine, "");
		criteria.addOrder(Order.desc("gemsEmployeeLeaveLineId"));
		return super.getAllObjectList(GemsEmployeeLeaveLine.class, criteria);
	}

	public void saveGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		super.saveOrUpdate(gemsEmployeeLeaveLine);
	}

	public void removeGemsEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		super.delete(gemsEmployeeLeaveLine);
	}

	public GemsEmployeeLeaveLine getGemsEmployeeLeaveLine(Integer Id) {
		return (GemsEmployeeLeaveLine) super.find(GemsEmployeeLeaveLine.class, Id);
	}

	public GemsEmployeeLeaveLine getMaxEmployeeLeaveLine(GemsEmployeeLeaveLine gemsEmployeeLeaveLine) {
		DetachedCriteria criteria = createGemsEmployeeLeaveLineCriteria(gemsEmployeeLeaveLine, "");
		DetachedCriteria maxId = DetachedCriteria.forClass(GemsEmployeeLeaveLine.class)
				.setProjection(Projections.max("gemsEmployeeLeaveLineId"));
		criteria.add(Property.forName("gemsEmployeeLeaveLineId").eq(maxId));
		return (GemsEmployeeLeaveLine) super.checkUniqueCode(GemsEmployeeLeaveLine.class, criteria);
	}

	/*
	 * End of Gems Employee leave line Methods
	 */

	/*
	 * Gems Employee Skill Methods
	 */

	private DetachedCriteria createGemsEmployeeSkillDetailCriteria(GemsEmployeeSkillDetail gemsEmployeeSkillDetail,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeSkillDetail.class);
		criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeeSkillDetail.getGemsEmployeeMaster()));
		return criteria;

	}

	public int getGemsEmployeeSkillDetailFilterCount(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		DetachedCriteria criteria = createGemsEmployeeSkillDetailCriteria(gemsEmployeeSkillDetail, "");
		return super.getObjectListCount(GemsEmployeeSkillDetail.class, criteria);
	}

	public List getGemsEmployeeSkillDetailList(int start, int limit, GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		DetachedCriteria criteria = createGemsEmployeeSkillDetailCriteria(gemsEmployeeSkillDetail, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsEmployeeSkillDetail.class, criteria, start, limit);
	}

	public void saveGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		super.saveOrUpdate(gemsEmployeeSkillDetail);
	}

	public void removeGemsEmployeeSkillDetail(GemsEmployeeSkillDetail gemsEmployeeSkillDetail) {
		super.delete(gemsEmployeeSkillDetail);
	}

	public GemsEmployeeSkillDetail getGemsEmployeeSkillDetail(Integer Id) {
		return (GemsEmployeeSkillDetail) super.find(GemsEmployeeSkillDetail.class, Id);
	}

	/*
	 * End of Gems Candidate Skill Detail Methods
	 */

	/*
	 * Gems Employee Payslip Methods
	 */

	private DetachedCriteria createGemsEmployeePaySlipDetailCriteria(
			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeePaySlipDetail.class);
		if (gemsEmployeePaySlipDetail.getPaySlipDate() != null) {
			criteria.add(Restrictions.eq("paySlipDate", gemsEmployeePaySlipDetail.getPaySlipDate()));
		}
		if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster() != null) {
			if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeCode() != null) {
				criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster"); // inner
																					// join
																					// by
																					// default
				criteria.add(Restrictions
						.like("gemsEmployeeMaster.employeeCode",
								"" + gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getEmployeeCode() + "%")
						.ignoreCase());
			}
			if (gemsEmployeePaySlipDetail.getGemsEmployeeMaster().getGemsEmployeeMasterId() != 0) {
				criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsEmployeePaySlipDetail.getGemsEmployeeMaster()));
			}

		}

		return criteria;

	}

	public int getGemsEmployeePaySlipDetailFilterCount(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		DetachedCriteria criteria = createGemsEmployeePaySlipDetailCriteria(gemsEmployeePaySlipDetail, "");
		return super.getObjectListCount(GemsEmployeePaySlipDetail.class, criteria);
	}

	public List getGemsEmployeePaySlipDetailList(int start, int limit,
			GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		DetachedCriteria criteria = createGemsEmployeePaySlipDetailCriteria(gemsEmployeePaySlipDetail, "");
		return super.getObjectListByRange(GemsEmployeePaySlipDetail.class, criteria, start, limit);
	}

	public List getAllGemsEmployeePaySlipDetailList(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		DetachedCriteria criteria = createGemsEmployeePaySlipDetailCriteria(gemsEmployeePaySlipDetail, "");
		return super.getAllObjectList(GemsEmployeePaySlipDetail.class, criteria);
	}

	public void saveGemsEmployeePaySlipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		super.saveOrUpdate(gemsEmployeePaySlipDetail);
	}

	public void removeGemsEmployeePayslipDetail(GemsEmployeePaySlipDetail gemsEmployeePaySlipDetail) {
		super.delete(gemsEmployeePaySlipDetail);
	}

	public GemsEmployeePaySlipDetail getGemsEmployeePayslipDetail(Integer Id) {
		return (GemsEmployeePaySlipDetail) super.find(GemsEmployeePaySlipDetail.class, Id);
	}

	/*
	 * End of Gems Employee PaySlip Methods
	 */

}
