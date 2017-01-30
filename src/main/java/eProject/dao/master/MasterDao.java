/**
 * 
 */
package eProject.dao.master;

import java.text.SimpleDateFormat;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
import eProject.domain.master.GemsActivityMaster;
import eProject.domain.master.GemsAlertNotificationMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsComponentMaster;
import eProject.domain.master.GemsCountryMaster;
import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsDesignation;
import eProject.domain.master.GemsEducationMaster;
import eProject.domain.master.GemsEmploymentStatus;
import eProject.domain.master.GemsNationalitiesMaster;
import eProject.domain.master.GemsNotification;
import eProject.domain.master.GemsNotificationAssignment;
import eProject.domain.master.GemsOrganisation;
import eProject.domain.master.GemsPayGrade;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsRoleTransactionApproval;
import eProject.domain.master.GemsTransactionApprovalMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.master.GemsUserToken;
import eProject.domain.master.GemsWorkShiftMaster;

@Repository("masterDao")
public class MasterDao extends ErpAbstractDao {

	public MasterDao() {
		super();
	}

	/*
	 * Login Verification Method
	 */
	public GemsUserMaster getLoginVerificationByEmail(GemsUserMaster gemsUserMaster) {
		return (GemsUserMaster) super.getLoginVerificationByEmail(gemsUserMaster);
	}

	/*
	 * GemsComponent Master Methods
	 */
	private DetachedCriteria createGemsComponentCriteria(GemsComponentMaster gemsComponentMaster) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsActivityMaster.class);
		if (gemsComponentMaster.getComponentDescription() != null) {
			criteria.add(
					Restrictions.like("componentDescription", "" + gemsComponentMaster.getComponentDescription() + "%")
							.ignoreCase());
		}
		if (gemsComponentMaster.getParentComponentId() != 0) {
			criteria.add(Restrictions.eq("parentComponentId", gemsComponentMaster.getParentComponentId()));
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsComponentMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsComponentMasterListFilterCount(GemsComponentMaster gemsComponentMaster) {
		DetachedCriteria criteria = createGemsComponentCriteria(gemsComponentMaster);
		return super.getObjectListCount(GemsComponentMaster.class, criteria);
	}

	public List getGemsComponentMasterList(int start, int limit, GemsComponentMaster gemsComponentMaster) {
		DetachedCriteria criteria = createGemsComponentCriteria(gemsComponentMaster);
		return super.getObjectListByRange(GemsComponentMaster.class, criteria, start, limit);
	}

	public void saveGemsComponentMaster(GemsComponentMaster gemsComponentMaster) {
		super.saveOrUpdate(gemsComponentMaster);
	}

	public void removeGemsComponentMaster(GemsComponentMaster gemsComponentMaster) {
		super.delete(gemsComponentMaster);
	}

	public GemsComponentMaster getGemsComponentMaster(Integer componentId) {
		return (GemsComponentMaster) super.find(GemsComponentMaster.class, componentId);
	}

	/*
	 * Gems Component Master Methods
	 */

	public List getOdActivityListByRole(GemsActivityMaster gemsActivityMaster) {

		DetachedCriteria criteria = DetachedCriteria.forClass(GemsActivityMaster.class);
		if (gemsActivityMaster.getGemsRoleMaster() != null) {
			criteria.add(Restrictions.eq("gemsRoleMaster", gemsActivityMaster.getGemsRoleMaster()));

		}
		return super.getObjectList(GemsActivityMaster.class, criteria);
	}

	/*
	 * GemsCountry Methods
	 */

	private DetachedCriteria createGemsCountryCriteria(GemsCountryMaster gemsCountryMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCountryMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsCountryMaster.getGemsCountryCode() != null) {
				criteria.add(Restrictions.eq("gemsCountryCode",
						"" + gemsCountryMaster.getGemsCountryCode().toUpperCase() + ""));
			}
		} else {
			if (gemsCountryMaster.getGemsCountryCode() != null) {
				criteria.add(Restrictions.like("gemsCountryCode", "" + gemsCountryMaster.getGemsCountryCode() + "%")
						.ignoreCase());
			}
			if (gemsCountryMaster.getGemsCountryDescription() != null) {
				criteria.add(Restrictions
						.like("gemsCountryDescription", "%" + gemsCountryMaster.getGemsCountryDescription() + "%")
						.ignoreCase());
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsCountryMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsCountryMasterListFilterCount(GemsCountryMaster gemsCountryMaster) {
		DetachedCriteria criteria = createGemsCountryCriteria(gemsCountryMaster, "");
		return super.getObjectListCount(GemsCountryMaster.class, criteria);
	}

	public List getGemsCountryMasterList(int start, int limit, GemsCountryMaster gemsCountryMaster) {
		DetachedCriteria criteria = createGemsCountryCriteria(gemsCountryMaster, "");
		return super.getObjectListByRange(GemsCountryMaster.class, criteria, start, limit);
	}

	public void saveGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		super.saveOrUpdate(gemsCountryMaster);
	}

	public void removeGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		super.delete(gemsCountryMaster);
	}

	public GemsCountryMaster getGemsCountryMaster(Integer countryId) {
		return (GemsCountryMaster) super.find(GemsCountryMaster.class, countryId);
	}

	public GemsCountryMaster getGemsCountryMasterByCode(GemsCountryMaster gemsCountryMaster) {
		DetachedCriteria criteria = createGemsCountryCriteria(gemsCountryMaster, "exact");
		return (GemsCountryMaster) super.checkUniqueCode(GemsCountryMaster.class, criteria);
	}

	/*
	 * End of Country Methods
	 */

	/*
	 * GemsOrganisation Methods
	 */

	private DetachedCriteria createGemsOrganisationCriteria(GemsOrganisation gemsOrganisation, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsOrganisation.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsOrganisation.getGemsOrganisationCode() != null) {
				criteria.add(Restrictions.eq("gemsBuCode",
						"" + gemsOrganisation.getGemsOrganisationCode().toUpperCase() + ""));
			}
		} else {
			if (gemsOrganisation.getGemsOrganisationCode() != null) {
				criteria.add(Restrictions.like("gemsBuCode", "" + gemsOrganisation.getGemsOrganisationCode() + "%")
						.ignoreCase());
			}
			if (gemsOrganisation.getGemsOrgName() != null) {
				criteria.add(
						Restrictions.like("gemsOrgName", "%" + gemsOrganisation.getGemsOrgName() + "%").ignoreCase());
			}
		}
		// criteria.add(Restrictions.eq("gemsOrganisation",
		// ""+gemsBusinessUnit.getGemsOrganisation()+""));
		return criteria;

	}

	public int getGemsOrganisationFilterCount(GemsOrganisation gemsOrganisation) {
		DetachedCriteria criteria = createGemsOrganisationCriteria(gemsOrganisation, "");
		return super.getObjectListCount(GemsOrganisation.class, criteria);
	}

	public List getGemsOrganisationList(int start, int limit, GemsOrganisation gemsOrganisation) {
		DetachedCriteria criteria = createGemsOrganisationCriteria(gemsOrganisation, "");
		return super.getObjectListByRange(GemsOrganisation.class, criteria, start, limit);
	}

	public void saveGemsOrganisation(GemsOrganisation gemsOrganisation) {
		super.saveOrUpdate(gemsOrganisation);
	}

	public GemsOrganisation getGemsOrganisation(Integer gemsOrgId) {
		return (GemsOrganisation) super.find(GemsOrganisation.class, gemsOrgId);
	}

	public void removeGemsOrganisation(GemsOrganisation gemsOrganisation) {
		super.delete(gemsOrganisation);
	}

	public GemsOrganisation getGemsOrganisationByCode(GemsOrganisation gemsOrganisation) {
		DetachedCriteria criteria = createGemsOrganisationCriteria(gemsOrganisation, "exact");
		return (GemsOrganisation) super.checkUniqueCode(GemsOrganisation.class, criteria);
	}

	/*
	 * End of Organisation Methods
	 */

	/*
	 * Business Unit Methods
	 */

	private DetachedCriteria createGemsBusinessUnitCriteria(GemsBusinessUnit gemsBusinessUnit, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsBusinessUnit.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsBusinessUnit.getGemsBuCode() != null) {
				criteria.add(Restrictions.eq("gemsBuCode", "" + gemsBusinessUnit.getGemsBuCode().toUpperCase() + ""));
			}
		} else {
			if (gemsBusinessUnit.getGemsBuCode() != null) {
				criteria.add(Restrictions.like("gemsBuCode", "" + gemsBusinessUnit.getGemsBuCode() + "%").ignoreCase());
			}
			if (gemsBusinessUnit.getGemsBuDescription() != null) {
				criteria.add(Restrictions.like("gemsBuDescription", "%" + gemsBusinessUnit.getGemsBuDescription() + "%")
						.ignoreCase());
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsBusinessUnit.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsBusinessUnitFilterCount(GemsBusinessUnit gemsBusinessUnit) {
		DetachedCriteria criteria = createGemsBusinessUnitCriteria(gemsBusinessUnit, "");
		return super.getObjectListCount(GemsBusinessUnit.class, criteria);
	}

	public List getGemsBusinessUnitList(int start, int limit, GemsBusinessUnit gemsBusinessUnit) {
		DetachedCriteria criteria = createGemsBusinessUnitCriteria(gemsBusinessUnit, "");
		return super.getObjectListByRange(GemsBusinessUnit.class, criteria, start, limit);
	}

	public void saveGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit) {
		super.saveOrUpdate(gemsBusinessUnit);
	}

	public void removeGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit) {
		super.delete(gemsBusinessUnit);
	}

	public GemsBusinessUnit getGemsBusinessUnit(Integer Id) {
		return (GemsBusinessUnit) super.find(GemsBusinessUnit.class, Id);
	}

	public GemsBusinessUnit getGemsBusinessUnitByCode(GemsBusinessUnit gemsBusinessUnit) {
		DetachedCriteria criteria = createGemsBusinessUnitCriteria(gemsBusinessUnit, "exact");
		return (GemsBusinessUnit) super.checkUniqueCode(GemsBusinessUnit.class, criteria);
	}

	/*
	 * End of Business Unit Methods
	 */

	/*
	 * RoleMaster Methods
	 */

	private DetachedCriteria createGemsRoleMasterCriteria(GemsRoleMaster gemsRoleMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsRoleMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsRoleMaster.getRoleCode() != null) {
				criteria.add(Restrictions.eq("roleCode", "" + gemsRoleMaster.getRoleCode() + "").ignoreCase());
			}
		} else {
			if (gemsRoleMaster.getRoleCode() != null) {
				criteria.add(Restrictions.like("roleCode", "" + gemsRoleMaster.getRoleCode() + "%").ignoreCase());
			}
			if (gemsRoleMaster.getRoleName() != null) {
				criteria.add(Restrictions.like("roleName", "%" + gemsRoleMaster.getRoleName() + "%").ignoreCase());
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsRoleMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsRoleMasterFilterCount(GemsRoleMaster gemsRoleMaster) {
		DetachedCriteria criteria = createGemsRoleMasterCriteria(gemsRoleMaster, "");
		return super.getObjectListCount(GemsRoleMaster.class, criteria);
	}

	public List getGemsRoleMasterList(int start, int limit, GemsRoleMaster gemsRoleMaster) {
		DetachedCriteria criteria = createGemsRoleMasterCriteria(gemsRoleMaster, "");
		return super.getObjectListByRange(GemsRoleMaster.class, criteria, start, limit);
	}

	public List getAllGemsRoleMasterList(GemsRoleMaster gemsRoleMaster) {
		DetachedCriteria criteria = createGemsRoleMasterCriteria(gemsRoleMaster, "");
		return super.getAllObjectList(GemsRoleMaster.class, criteria);
	}

	public void saveGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		super.saveOrUpdate(gemsRoleMaster);
	}

	public void removeGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		super.delete(gemsRoleMaster);
	}

	public GemsRoleMaster getGemsRoleMaster(Integer Id) {
		return (GemsRoleMaster) super.find(GemsRoleMaster.class, Id);
	}

	public GemsRoleMaster getGemsRoleMasterByCode(GemsRoleMaster gemsRoleMaster) {
		DetachedCriteria criteria = createGemsRoleMasterCriteria(gemsRoleMaster, "exact");

		return (GemsRoleMaster) super.checkUniqueCode(GemsRoleMaster.class, criteria);
	}

	/*
	 * End of Role Master Methods
	 */

	/*
	 * UserMaster Methods
	 */

	private DetachedCriteria createGemsUserMasterCriteria(GemsUserMaster gemsUserMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsUserMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsUserMaster.getUserName() != null) {
				criteria.add(Restrictions.eq("userName", "" + gemsUserMaster.getUserName() + "").ignoreCase());
			}
			if (gemsUserMaster.getGemsRoleMaster() != null) {
				criteria.add(Restrictions.eq("gemsRoleMaster", gemsUserMaster.getGemsRoleMaster()));
			}
		} else {
			if (gemsUserMaster.getUserName() != null) {
				criteria.add(Restrictions.like("userName", "" + gemsUserMaster.getUserName() + "%").ignoreCase());
			}
			if (gemsUserMaster.getGemsRoleMaster() != null) {
				criteria.add(Restrictions.eq("gemsRoleMaster", gemsUserMaster.getGemsRoleMaster()));
			}
		}
		if (gemsUserMaster.getGemsOrganisation() != null) {
			criteria.add(Restrictions.eq("gemsOrganisation", gemsUserMaster.getGemsOrganisation()));
		}

		return criteria;

	}

	public int getGemsUserMasterFilterCount(GemsUserMaster gemsUserMaster) {
		DetachedCriteria criteria = createGemsUserMasterCriteria(gemsUserMaster, "");
		return super.getObjectListCount(GemsRoleMaster.class, criteria);
	}

	public List getGemsUserMasterList(int start, int limit, GemsUserMaster gemsUserMaster) {
		DetachedCriteria criteria = createGemsUserMasterCriteria(gemsUserMaster, "");
		return super.getObjectListByRange(GemsRoleMaster.class, criteria, start, limit);
	}

	public void saveGemsUserMaster(GemsUserMaster gemsUserMaster) {
		super.saveOrUpdate(gemsUserMaster);
	}

	public void removeGemsUserMaster(GemsUserMaster gemsUserMaster) {
		super.delete(gemsUserMaster);
	}

	public GemsUserMaster getGemsUserMaster(Integer Id) {
		return (GemsUserMaster) super.find(GemsUserMaster.class, Id);
	}

	public GemsUserMaster getGemsUserMasterByCode(GemsUserMaster gemsUserMaster) {
		DetachedCriteria criteria = createGemsUserMasterCriteria(gemsUserMaster, "exact");
		return (GemsUserMaster) super.checkUniqueCode(GemsUserMaster.class, criteria);
	}

	/*
	 * End of User Master Methods
	 */

	/*
	 * Gems Department Methods
	 */

	private DetachedCriteria createGemsDepartmentCriteria(GemsDepartment gemsDepartment, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsDepartment.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsDepartment.getDepartmentCode() != null) {
				criteria.add(
						Restrictions.eq("departmentCode", "" + gemsDepartment.getDepartmentCode().toUpperCase() + ""));
			}
		} else {
			if (gemsDepartment.getDepartmentCode() != null) {
				criteria.add(Restrictions.like("departmentCode", "" + gemsDepartment.getDepartmentCode() + "%")
						.ignoreCase());
			}
			if (gemsDepartment.getDepartmentDescription() != null) {
				criteria.add(Restrictions.like("departmentDescription",
						"" + gemsDepartment.getDepartmentDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsDepartment.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsDepartmentFilterCount(GemsDepartment gemsDepartment) {
		DetachedCriteria criteria = createGemsDepartmentCriteria(gemsDepartment, "");
		return super.getObjectListCount(GemsDepartment.class, criteria);
	}

	public List getGemsDepartmentList(int start, int limit, GemsDepartment gemsDepartment) {
		DetachedCriteria criteria = createGemsDepartmentCriteria(gemsDepartment, "");
		return super.getObjectListByRange(GemsDepartment.class, criteria, start, limit);
	}

	public void saveGemsDepartment(GemsDepartment gemsDepartment) {
		super.saveOrUpdate(gemsDepartment);
	}

	public void removeGemsDepartment(GemsDepartment gemsDepartment) {
		super.delete(gemsDepartment);
	}

	public GemsDepartment getGemsDepartment(Integer Id) {
		return (GemsDepartment) super.find(GemsDepartment.class, Id);
	}

	public GemsDepartment getGemsDepartmentByCode(GemsDepartment gemsDepartment) {
		DetachedCriteria criteria = createGemsDepartmentCriteria(gemsDepartment, "exact");
		return (GemsDepartment) super.checkUniqueCode(GemsDepartment.class, criteria);
	}

	/*
	 * End of Department Methods
	 */

	/*
	 * Gems Designation Methods
	 */

	private DetachedCriteria createGemsDesignationCriteria(GemsDesignation gemsDesignation, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsDesignation.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsDesignation.getGemsDesignationCode() != null) {
				criteria.add(Restrictions.eq("gemsDesignationCode",
						"" + gemsDesignation.getGemsDesignationCode().toUpperCase() + ""));
			}
		} else {
			if (gemsDesignation.getGemsDesignationCode() != null) {
				criteria.add(Restrictions
						.like("gemsDesignationCode", "" + gemsDesignation.getGemsDesignationCode() + "%").ignoreCase());
			}
			if (gemsDesignation.getGemsDesignationDescription() != null) {
				criteria.add(Restrictions.like("gemsDesignationDescription",
						"" + gemsDesignation.getGemsDesignationDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsDesignation.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsDesignationFilterCount(GemsDesignation gemsDesignation) {
		DetachedCriteria criteria = createGemsDesignationCriteria(gemsDesignation, "");
		return super.getObjectListCount(GemsDesignation.class, criteria);
	}

	public List getGemsDesignationList(int start, int limit, GemsDesignation gemsDesignation) {
		DetachedCriteria criteria = createGemsDesignationCriteria(gemsDesignation, "");
		return super.getObjectListByRange(GemsDesignation.class, criteria, start, limit);
	}

	public void saveGemsDesignation(GemsDesignation gemsDesignation) {
		super.saveOrUpdate(gemsDesignation);
	}

	public void removeGemsDesignation(GemsDesignation gemsDesignation) {
		super.delete(gemsDesignation);
	}

	public GemsDesignation getGemsDesignation(Integer Id) {
		return (GemsDesignation) super.find(GemsDesignation.class, Id);
	}

	public GemsDesignation getGemsDesignationByCode(GemsDesignation gemsDesignation) {
		DetachedCriteria criteria = createGemsDesignationCriteria(gemsDesignation, "exact");
		return (GemsDesignation) super.checkUniqueCode(GemsDesignation.class, criteria);
	}

	/*
	 * End of Designation Methods
	 */

	/*
	 * Gems Currency Methods
	 */

	private DetachedCriteria createGemsCurrencyMasterCriteria(GemsCurrencyMaster gemsCurrencyMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsCurrencyMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsCurrencyMaster.getCurrencyCode() != null) {
				criteria.add(
						Restrictions.eq("currencyCode", "" + gemsCurrencyMaster.getCurrencyCode().toUpperCase() + ""));
			}
		} else {
			if (gemsCurrencyMaster.getCurrencyCode() != null) {
				criteria.add(Restrictions.like("currencyCode", "" + gemsCurrencyMaster.getCurrencyCode() + "%")
						.ignoreCase());
			}
			if (gemsCurrencyMaster.getCurrencyDescription() != null) {
				criteria.add(Restrictions.like("currencyDescription",
						"" + gemsCurrencyMaster.getCurrencyDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsCurrencyMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsCurrencyMasterFilterCount(GemsCurrencyMaster gemsCurrencyMaster) {
		DetachedCriteria criteria = createGemsCurrencyMasterCriteria(gemsCurrencyMaster, "");
		return super.getObjectListCount(GemsCurrencyMaster.class, criteria);
	}

	public List getGemsCurrencyMasterList(int start, int limit, GemsCurrencyMaster gemsCurrencyMaster) {
		DetachedCriteria criteria = createGemsCurrencyMasterCriteria(gemsCurrencyMaster, "");
		return super.getObjectListByRange(GemsCurrencyMaster.class, criteria, start, limit);
	}

	public void saveGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		super.saveOrUpdate(gemsCurrencyMaster);
	}

	public void removeGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		super.delete(gemsCurrencyMaster);
	}

	public GemsCurrencyMaster getGemsCurrencyMaster(Integer Id) {
		return (GemsCurrencyMaster) super.find(GemsCurrencyMaster.class, Id);
	}

	public GemsCurrencyMaster getGemsCurrencyMasterByCode(GemsCurrencyMaster gemsCurrencyMaster) {
		DetachedCriteria criteria = createGemsCurrencyMasterCriteria(gemsCurrencyMaster, "exact");
		return (GemsCurrencyMaster) super.checkUniqueCode(GemsCurrencyMaster.class, criteria);
	}

	/*
	 * End of Currency Methods
	 */

	/*
	 * Gems Education Master Methods
	 */

	private DetachedCriteria createGemsEducationMasterCriteria(GemsEducationMaster gemsEducationMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEducationMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsEducationMaster.getEducationCode() != null) {
				criteria.add(Restrictions.eq("educationCode",
						"" + gemsEducationMaster.getEducationCode().toUpperCase() + ""));
			}
		} else {
			if (gemsEducationMaster.getEducationCode() != null) {
				criteria.add(Restrictions.like("educationCode", "" + gemsEducationMaster.getEducationCode() + "%")
						.ignoreCase());
			}
			if (gemsEducationMaster.getEducationDescription() != null) {
				criteria.add(Restrictions.like("educationDescription",
						"" + gemsEducationMaster.getEducationDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsEducationMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsEducationMasterFilterCount(GemsEducationMaster gemsEducationMaster) {
		DetachedCriteria criteria = createGemsEducationMasterCriteria(gemsEducationMaster, "");
		return super.getObjectListCount(GemsEducationMaster.class, criteria);
	}

	public List getGemsEducationMasterList(int start, int limit, GemsEducationMaster gemsEducationMaster) {
		DetachedCriteria criteria = createGemsEducationMasterCriteria(gemsEducationMaster, "");
		return super.getObjectListByRange(GemsEducationMaster.class, criteria, start, limit);
	}

	public void saveGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		super.saveOrUpdate(gemsEducationMaster);
	}

	public void removeGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		super.delete(gemsEducationMaster);
	}

	public GemsEducationMaster getGemsEducationMaster(Integer Id) {
		return (GemsEducationMaster) super.find(GemsEducationMaster.class, Id);
	}

	public GemsEducationMaster getGemsEducationMasterByCode(GemsEducationMaster gemsEducationMaster) {
		DetachedCriteria criteria = createGemsEducationMasterCriteria(gemsEducationMaster, "exact");
		return (GemsEducationMaster) super.checkUniqueCode(GemsEducationMaster.class, criteria);
	}

	/*
	 * End of Education Master Methods
	 */

	/*
	 * Gems Employment Status Methods
	 */

	private DetachedCriteria createGemsEmploymentStatusCriteria(GemsEmploymentStatus gemsEmploymentStatus,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmploymentStatus.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsEmploymentStatus.getStatusCode() != null) {
				criteria.add(
						Restrictions.eq("statusCode", "" + gemsEmploymentStatus.getStatusCode() + "").ignoreCase());
			}
		} else {
			if (gemsEmploymentStatus.getStatusCode() != null) {
				criteria.add(
						Restrictions.like("statusCode", "" + gemsEmploymentStatus.getStatusCode() + "%").ignoreCase());
			}
			if (gemsEmploymentStatus.getStatusDescription() != null) {
				criteria.add(
						Restrictions.like("statusDescription", "" + gemsEmploymentStatus.getStatusDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsEmploymentStatus.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsEmploymentStatusFilterCount(GemsEmploymentStatus gemsEmploymentStatus) {
		DetachedCriteria criteria = createGemsEmploymentStatusCriteria(gemsEmploymentStatus, "");
		return super.getObjectListCount(GemsEmploymentStatus.class, criteria);
	}

	public List getGemsEmploymentStatusList(int start, int limit, GemsEmploymentStatus gemsEmploymentStatus) {
		DetachedCriteria criteria = createGemsEmploymentStatusCriteria(gemsEmploymentStatus, "");
		return super.getObjectListByRange(GemsEmploymentStatus.class, criteria, start, limit);
	}

	public void saveGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus) {
		super.saveOrUpdate(gemsEmploymentStatus);
	}

	public void removeGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus) {
		super.delete(gemsEmploymentStatus);
	}

	public GemsEmploymentStatus getGemsEmploymentStatus(Integer Id) {
		return (GemsEmploymentStatus) super.find(GemsEmploymentStatus.class, Id);
	}

	public GemsEmploymentStatus getGemsEmploymentStatusByCode(GemsEmploymentStatus gemsEmploymentStatus) {
		DetachedCriteria criteria = createGemsEmploymentStatusCriteria(gemsEmploymentStatus, "exact");
		return (GemsEmploymentStatus) super.checkUniqueCode(GemsEmploymentStatus.class, criteria);
	}

	/*
	 * End of Employment Status Methods
	 */

	/*
	 * Gems Nationality Master Methods
	 */

	private DetachedCriteria createGemsNationalitiesMasterCriteria(GemsNationalitiesMaster gemsNationalitiesMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsNationalitiesMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsNationalitiesMaster.getNationalityCode() != null) {
				criteria.add(Restrictions.eq("nationalityCode",
						"" + gemsNationalitiesMaster.getNationalityCode().toUpperCase() + ""));
			}
		} else {
			if (gemsNationalitiesMaster.getNationalityCode() != null) {
				criteria.add(Restrictions
						.like("nationalityCode", "" + gemsNationalitiesMaster.getNationalityCode() + "%").ignoreCase());
			}
			if (gemsNationalitiesMaster.getNationalityDescription() != null) {
				criteria.add(Restrictions.like("nationalityDescription",
						"" + gemsNationalitiesMaster.getNationalityDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsNationalitiesMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsNationalitiesMasterFilterCount(GemsNationalitiesMaster gemsNationalitiesMaster) {
		DetachedCriteria criteria = createGemsNationalitiesMasterCriteria(gemsNationalitiesMaster, "");
		return super.getObjectListCount(GemsNationalitiesMaster.class, criteria);
	}

	public List getGemsNationalitiesMasterList(int start, int limit, GemsNationalitiesMaster gemsNationalitiesMaster) {
		DetachedCriteria criteria = createGemsNationalitiesMasterCriteria(gemsNationalitiesMaster, "");
		return super.getObjectListByRange(GemsNationalitiesMaster.class, criteria, start, limit);
	}

	public void saveGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster) {
		super.saveOrUpdate(gemsNationalitiesMaster);
	}

	public void removeGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster) {
		super.delete(gemsNationalitiesMaster);
	}

	public GemsNationalitiesMaster getGemsNationalitiesMaster(Integer Id) {
		return (GemsNationalitiesMaster) super.find(GemsNationalitiesMaster.class, Id);
	}

	public GemsNationalitiesMaster getGemsNationalitiesMasterByCode(GemsNationalitiesMaster gemsNationalitiesMaster) {
		DetachedCriteria criteria = createGemsNationalitiesMasterCriteria(gemsNationalitiesMaster, "exact");
		return (GemsNationalitiesMaster) super.checkUniqueCode(GemsNationalitiesMaster.class, criteria);
	}

	/*
	 * End of Gems Nationality Methods
	 */

	/*
	 * Gems Paygrade Methods
	 */

	private DetachedCriteria createGemsPayGradeCriteria(GemsPayGrade gemsPayGrade, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsPayGrade.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsPayGrade.getPayGradeCode() != null) {
				criteria.add(Restrictions.eq("payGradeCode", "" + gemsPayGrade.getPayGradeCode().toUpperCase() + ""));
			}
		} else {
			if (gemsPayGrade.getPayGradeCode() != null) {
				criteria.add(Restrictions.like("payGradeCode", "" + gemsPayGrade.getPayGradeCode() + "%").ignoreCase());
			}
			if (gemsPayGrade.getPayGradeDescription() != null) {
				criteria.add(Restrictions.like("payGradeDescription", "" + gemsPayGrade.getPayGradeDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsPayGrade.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsPayGradeFilterCount(GemsPayGrade gemsPayGrade) {
		DetachedCriteria criteria = createGemsPayGradeCriteria(gemsPayGrade, "");
		return super.getObjectListCount(GemsPayGrade.class, criteria);
	}

	public List getGemsPayGradeList(int start, int limit, GemsPayGrade gemsPayGrade) {
		DetachedCriteria criteria = createGemsPayGradeCriteria(gemsPayGrade, "");
		return super.getObjectListByRange(GemsPayGrade.class, criteria, start, limit);
	}

	public void saveGemsPayGrade(GemsPayGrade gemsPayGrade) {
		super.saveOrUpdate(gemsPayGrade);
	}

	public void removeGemsPayGrade(GemsPayGrade gemsPayGrade) {
		super.delete(gemsPayGrade);
	}

	public GemsPayGrade getGemsPayGrade(Integer Id) {
		return (GemsPayGrade) super.find(GemsPayGrade.class, Id);
	}

	public GemsPayGrade getGemsPayGradeByCode(GemsPayGrade gemsPayGrade) {
		DetachedCriteria criteria = createGemsPayGradeCriteria(gemsPayGrade, "exact");
		return (GemsPayGrade) super.checkUniqueCode(GemsPayGrade.class, criteria);
	}

	/*
	 * End of Gems Paygrade Methods
	 */

	/*
	 * Gems Shift Hours Methods
	 */

	private DetachedCriteria createGemsWorkShiftMasterCriteria(GemsWorkShiftMaster gemsWorkShiftMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsWorkShiftMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsWorkShiftMaster.getWorkShiftCode() != null) {
				criteria.add(Restrictions.eq("workShiftCode",
						"" + gemsWorkShiftMaster.getWorkShiftCode().toUpperCase() + ""));
			}
		} else {
			if (gemsWorkShiftMaster.getWorkShiftCode() != null) {
				criteria.add(Restrictions.like("workShiftCode", "" + gemsWorkShiftMaster.getWorkShiftCode() + "%")
						.ignoreCase());
			}
			if (gemsWorkShiftMaster.getWorkShiftDescription() != null) {
				criteria.add(Restrictions.like("workShiftDescription",
						"" + gemsWorkShiftMaster.getWorkShiftDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsWorkShiftMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsWorkShiftMasterFilterCount(GemsWorkShiftMaster gemsWorkShiftMaster) {
		DetachedCriteria criteria = createGemsWorkShiftMasterCriteria(gemsWorkShiftMaster, "");
		return super.getObjectListCount(GemsWorkShiftMaster.class, criteria);
	}

	public List getGemsWorkShiftMasterList(int start, int limit, GemsWorkShiftMaster gemsWorkShiftMaster) {
		DetachedCriteria criteria = createGemsWorkShiftMasterCriteria(gemsWorkShiftMaster, "");
		return super.getObjectListByRange(GemsWorkShiftMaster.class, criteria, start, limit);
	}

	public void saveGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster) {
		super.saveOrUpdate(gemsWorkShiftMaster);
	}

	public void removeGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster) {
		super.delete(gemsWorkShiftMaster);
	}

	public GemsWorkShiftMaster getGemsWorkShiftMaster(Integer Id) {
		return (GemsWorkShiftMaster) super.find(GemsWorkShiftMaster.class, Id);
	}

	public GemsWorkShiftMaster getGemsWorkShiftMasterByCode(GemsWorkShiftMaster gemsWorkShiftMaster) {
		DetachedCriteria criteria = createGemsWorkShiftMasterCriteria(gemsWorkShiftMaster, "exact");
		return (GemsWorkShiftMaster) super.checkUniqueCode(GemsWorkShiftMaster.class, criteria);
	}

	/*
	 * End of Gems Shift Hours Methods
	 */

	/*
	 * Gems Alert Notification Master Methods
	 */

	private DetachedCriteria createGemsAlertNotificationMasterCriteria(
			GemsAlertNotificationMaster gemsAlertNotificationMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsAlertNotificationMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsAlertNotificationMaster.getTransactionCode() != null) {
				criteria.add(Restrictions.eq("transactionCode",
						"" + gemsAlertNotificationMaster.getTransactionCode().toUpperCase() + ""));
			}
		} else {
			if (gemsAlertNotificationMaster.getTransactionCode() != null) {
				criteria.add(Restrictions
						.like("transactionCode", "" + gemsAlertNotificationMaster.getTransactionCode() + "%")
						.ignoreCase());
			}
			if (gemsAlertNotificationMaster.getTransactionDescription() != null) {
				criteria.add(Restrictions.like("transactionDescription",
						"" + gemsAlertNotificationMaster.getTransactionDescription() + ""));
			}
		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsAlertNotificationMaster.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsAlertNotificationMasterFilterCount(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		DetachedCriteria criteria = createGemsAlertNotificationMasterCriteria(gemsAlertNotificationMaster, "");
		return super.getObjectListCount(GemsAlertNotificationMaster.class, criteria);
	}

	public List getGemsAlertNotificationMasterList(int start, int limit,
			GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		DetachedCriteria criteria = createGemsAlertNotificationMasterCriteria(gemsAlertNotificationMaster, "");
		return super.getObjectListByRange(GemsAlertNotificationMaster.class, criteria, start, limit);
	}

	public void saveGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		super.saveOrUpdate(gemsAlertNotificationMaster);
	}

	public void removeGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		super.delete(gemsAlertNotificationMaster);
	}

	public GemsAlertNotificationMaster getGemsAlertNotificationMaster(Integer Id) {
		return (GemsAlertNotificationMaster) super.find(GemsAlertNotificationMaster.class, Id);
	}

	public GemsAlertNotificationMaster getGemsAlertNotificationMasterByCode(
			GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		DetachedCriteria criteria = createGemsAlertNotificationMasterCriteria(gemsAlertNotificationMaster, "exact");
		return (GemsAlertNotificationMaster) super.checkUniqueCode(GemsAlertNotificationMaster.class, criteria);
	}

	/*
	 * End of Gems Alert Notification Methods
	 */

	/*
	 * Gems Notification Methods
	 */

	private DetachedCriteria createGemsNotificationCriteria(GemsNotification gemsNotification, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsNotification.class);
		criteria.add(Restrictions.eq("gemsOrganisation", gemsNotification.getGemsOrganisation()));
		return criteria;

	}

	public int getGemsNotificationFilterCount(GemsNotification gemsNotification) {
		DetachedCriteria criteria = createGemsNotificationCriteria(gemsNotification, "");
		return super.getObjectListCount(GemsNotification.class, criteria);
	}

	public List getGemsNotificationList(int start, int limit, GemsNotification gemsNotification) {
		DetachedCriteria criteria = createGemsNotificationCriteria(gemsNotification, "");
		return super.getObjectListByRange(GemsNotification.class, criteria, start, limit);
	}

	public void saveGemsNotification(GemsNotification gemsNotification) {
		super.saveOrUpdate(gemsNotification);
	}

	public void removeGemsNotification(GemsNotification gemsNotification) {
		super.delete(gemsNotification);
	}

	public GemsNotification getGemsNotification(Integer Id) {
		return (GemsNotification) super.find(GemsNotification.class, Id);
	}

	/*
	 * End of Gems Notification Methods
	 */

	/*
	 * Gems Notification Assignment Methods
	 */

	private DetachedCriteria createGemsNotificationAssignmentCriteria(
			GemsNotificationAssignment gemsNotificationAssignment, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsNotificationAssignment.class);
		if (gemsNotificationAssignment.getGemsNotification() != null) {
			criteria.add(Restrictions.eq("gemsNotification", gemsNotificationAssignment.getGemsNotification()));
		}
		if (gemsNotificationAssignment.getGemsEmployeeMaster() != null) {
			criteria.add(Restrictions.eq("gemsEmployeeMaster", gemsNotificationAssignment.getGemsEmployeeMaster()));
		}
		if (searchType.equalsIgnoreCase("exact")) {
			Criterion cr1 = Expression.ge("notifiedOn", gemsNotificationAssignment.getNotifiedOn());
			Criterion cr2 = Expression.le("notificationExpiry", gemsNotificationAssignment.getNotificationExpiry());
			criteria.add(Restrictions.or(cr1, cr2));

		}

		return criteria;

	}

	public int getGemsNotificationAssignmentFilterCount(GemsNotificationAssignment gemsNotificationAssignment) {
		DetachedCriteria criteria = createGemsNotificationAssignmentCriteria(gemsNotificationAssignment, "");
		return super.getObjectListCount(GemsNotificationAssignment.class, criteria);
	}

	public List getGemsNotificationAssignmentList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment) {
		DetachedCriteria criteria = createGemsNotificationAssignmentCriteria(gemsNotificationAssignment, "");
		criteria.addOrder(Order.desc("notificationAssignmentId"));
		return super.getObjectListByRange(GemsNotificationAssignment.class, criteria, start, limit);
	}

	public void saveGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment) {
		super.saveOrUpdate(gemsNotificationAssignment);
	}

	public void removeGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment) {
		super.delete(gemsNotificationAssignment);
	}

	public GemsNotificationAssignment getGemsNotificationAssignment(Integer Id) {
		return (GemsNotificationAssignment) super.find(GemsNotificationAssignment.class, Id);
	}

	public int getGemsNotificationAssignmentByDateRangeFilterCount(
			GemsNotificationAssignment gemsNotificationAssignment) {
		DetachedCriteria criteria = createGemsNotificationAssignmentCriteria(gemsNotificationAssignment, "exact");
		return super.getObjectListCount(GemsNotificationAssignment.class, criteria);
	}

	public List getGemsNotificationAssignmentByDateRangeList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment) {
		DetachedCriteria criteria = createGemsNotificationAssignmentCriteria(gemsNotificationAssignment, "exact");
		criteria.addOrder(Order.desc("notificationAssignmentId"));
		return super.getObjectListByRange(GemsNotificationAssignment.class, criteria, start, limit);
	}

	/*
	 * End of Gems Notification Assignment Methods
	 */

	/*
	 * Gems Role Transaction Approval Methods
	 */

	private DetachedCriteria createGemsRoleTransactionApprovalCriteria(
			GemsRoleTransactionApproval gemsRoleTransactionApproval, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsRoleTransactionApproval.class);
		if (gemsRoleTransactionApproval.getGemsRoleMaster() != null) {
			criteria.add(Restrictions.eq("gemsRoleMaster", gemsRoleTransactionApproval.getGemsRoleMaster()));
		}
		if (gemsRoleTransactionApproval.getGemsTransactionApprovalMaster() != null) {
			criteria.add(Restrictions.eq("gemsTransactionApprovalMaster",
					gemsRoleTransactionApproval.getGemsTransactionApprovalMaster()));
		}
		if (gemsRoleTransactionApproval.getApprovalLevel() != 0) {
			criteria.add(Restrictions.eq("approvalLevel", gemsRoleTransactionApproval.getApprovalLevel()));
		}

		criteria.add(Restrictions.eq("gemsOrganisation", gemsRoleTransactionApproval.getGemsOrganisation()));

		return criteria;

	}

	public GemsRoleTransactionApproval getGemsRoleTransactionApprovalByRoleTransaction(
			GemsRoleTransactionApproval gemsRoleTransactionApproval) {
		DetachedCriteria criteria = createGemsRoleTransactionApprovalCriteria(gemsRoleTransactionApproval, "exact");
		return (GemsRoleTransactionApproval) super.checkUniqueCode(GemsRoleTransactionApproval.class, criteria);
	}

	/*
	 * End of Gems Role Transaction Approval Methods
	 */
	/*
	 * Gems Role Transaction Approval Methods
	 */

	private DetachedCriteria createGemsTransactionApprovalMasterCriteria(
			GemsTransactionApprovalMaster gemsTransactionApprovalMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsTransactionApprovalMaster.class);
		if (gemsTransactionApprovalMaster.getTransactionCode() != null) {
			criteria.add(
					Restrictions.eq("transactionCode", "" + gemsTransactionApprovalMaster.getTransactionCode() + ""));
		}

		criteria.add(Restrictions.eq("gemsOrganisation", gemsTransactionApprovalMaster.getGemsOrganisation()));

		return criteria;

	}

	public GemsTransactionApprovalMaster getGemsTransactionApprovalMasterByCode(
			GemsTransactionApprovalMaster gemsTransactionApprovalMaster) {
		DetachedCriteria criteria = createGemsTransactionApprovalMasterCriteria(gemsTransactionApprovalMaster, "exact");
		return (GemsTransactionApprovalMaster) super.checkUniqueCode(GemsTransactionApprovalMaster.class, criteria);
	}

	/*
	 * End of Gems Role Transaction Approval Methods
	 */

	private DetachedCriteria createGemsUserTokenCriteria(GemsUserToken gemsUserToken, String searchType) {

		DetachedCriteria criteria = DetachedCriteria.forClass(GemsUserToken.class);

		if (gemsUserToken.getUserTokenId() != null) {
			criteria.add(Restrictions.eq("userTokenId", gemsUserToken.getUserTokenId()));
		}

		if (gemsUserToken.getUserId() != null) {
			criteria.add(Restrictions.eq("userId", gemsUserToken.getUserId()));
		}
		if (gemsUserToken.getToken() != null) {
			criteria.add(Restrictions.eq("token", gemsUserToken.getToken()));
		}

		return criteria;

	}

	public void saveGemsUserToken(GemsUserToken gemsUserToken) {
		super.saveOrUpdate(gemsUserToken);
	}

	@SuppressWarnings("unchecked")
	public GemsUserToken getGemsUserToken(GemsUserToken userTokenValue) {

		DetachedCriteria criteria = createGemsUserTokenCriteria(userTokenValue, "exact");
		return (GemsUserToken) super.checkUniqueCode(GemsUserToken.class, criteria);
	}

	public void removeGemsUserToken(GemsUserToken userToken) {
		super.delete(userToken);
	}

}
