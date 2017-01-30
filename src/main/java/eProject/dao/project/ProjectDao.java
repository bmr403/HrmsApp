/**
 * 
 */
package eProject.dao.project;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import eProject.dao.ErpAbstractDao;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;

@Repository("ProjectDao")
public class ProjectDao extends ErpAbstractDao {

	public ProjectDao() {
		super();
	}

	/*
	 * Gems Project Type Methods
	 */

	private DetachedCriteria createGemsProjectTypeMasterCriteria(GemsProjectTypeMaster gemsProjectTypeMaster,
			String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsProjectTypeMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsProjectTypeMaster.getProjectTypeCode() != null) {
				criteria.add(Restrictions.eq("projectTypeCode",
						"" + gemsProjectTypeMaster.getProjectTypeCode().toUpperCase() + ""));
			}

		} else {
			if (gemsProjectTypeMaster.getProjectTypeCode() != null) {
				criteria.add(Restrictions.like("projectTypeCode", "" + gemsProjectTypeMaster.getProjectTypeCode() + "%")
						.ignoreCase());
			}
			if (gemsProjectTypeMaster.getProjectTypeDescription() != null) {
				criteria.add(Restrictions
						.like("projectTypeDescription", "%" + gemsProjectTypeMaster.getProjectTypeDescription() + "%")
						.ignoreCase());
			}
			if ((gemsProjectTypeMaster.getActiveStatus() == 1) || (gemsProjectTypeMaster.getActiveStatus() == 0)) {
				criteria.add(Restrictions.eq("activeStatus", gemsProjectTypeMaster.getActiveStatus()));
			}

		}
		criteria.add(Restrictions.eq("gemsOrganisation", gemsProjectTypeMaster.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsProjectTypeMasterFilterCount(GemsProjectTypeMaster gemsProjectTypeMaster) {
		DetachedCriteria criteria = createGemsProjectTypeMasterCriteria(gemsProjectTypeMaster, "");
		return super.getObjectListCount(GemsProjectTypeMaster.class, criteria);
	}

	public List getGemsProjectTypeMasterList(int start, int limit, GemsProjectTypeMaster gemsProjectTypeMaster) {
		DetachedCriteria criteria = createGemsProjectTypeMasterCriteria(gemsProjectTypeMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsProjectTypeMaster.class, criteria, start, limit);
	}

	public void saveGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster) {
		super.saveOrUpdate(gemsProjectTypeMaster);
	}

	public void removeGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster) {
		super.delete(gemsProjectTypeMaster);
	}

	public GemsProjectTypeMaster getGemsProjectTypeMaster(Integer Id) {
		return (GemsProjectTypeMaster) super.find(GemsProjectTypeMaster.class, Id);
	}

	public GemsProjectTypeMaster getGemsProjectTypeMasterByCode(GemsProjectTypeMaster gemsProjectTypeMaster) {
		DetachedCriteria criteria = createGemsProjectTypeMasterCriteria(gemsProjectTypeMaster, "exact");
		return (GemsProjectTypeMaster) super.checkUniqueCode(GemsProjectTypeMaster.class, criteria);
	}

	/*
	 * End of Gems Project Type Master Methods
	 */

	/*
	 * Gems Project Master Methods
	 */

	private DetachedCriteria createGemsProjectMasterCriteria(GemsProjectMaster gemsProjectMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsProjectMaster.class);
		if (searchType.equalsIgnoreCase("exact")) {
			if (gemsProjectMaster.getProjectCode() != null) {
				criteria.add(Restrictions.eq("projectCode", gemsProjectMaster.getProjectCode()));
			}
			if (gemsProjectMaster.getProjectName() != null) {
				criteria.add(Restrictions.eq("projectName", gemsProjectMaster.getProjectName()));
			}

		} else {
			if (gemsProjectMaster.getProjectCode() != null) {
				criteria.add(
						Restrictions.like("projectCode", "" + gemsProjectMaster.getProjectCode() + "%").ignoreCase());
			}
			if (gemsProjectMaster.getProjectName() != null) {
				criteria.add(
						Restrictions.like("projectName", "" + gemsProjectMaster.getProjectName() + "%").ignoreCase());
			}
			if (gemsProjectMaster.getActiveStatus() != null) {
				criteria.add(Restrictions.eq("activeStatus", gemsProjectMaster.getActiveStatus()));
			}
			if (gemsProjectMaster.getProjectTypeMaster() != null) {
				criteria.add(Restrictions.eq("projectTypeMaster", gemsProjectMaster.getProjectTypeMaster()));
			}
			if (gemsProjectMaster.getGemsCustomerMaster() != null) {
				criteria.add(Restrictions.eq("gemsCustomerMaster", gemsProjectMaster.getGemsCustomerMaster()));
			}
			if (gemsProjectMaster.getBillingType() != null) {
				criteria.add(Restrictions.like("billingType", "" + gemsProjectMaster.getBillingType() + "%"));
			}
			if (gemsProjectMaster.getProjectStatus() != null) {
				criteria.add(Restrictions.like("projectStatus", "" + gemsProjectMaster.getProjectStatus() + "%"));
			}

		}
		// criteria.add(Restrictions.eq("gemsOrganisation",gemsProjectMaster.getGemsOrganisation()));

		return criteria;

	}

	public int getGemsProjectMasterFilterCount(GemsProjectMaster gemsProjectMaster) {
		DetachedCriteria criteria = createGemsProjectMasterCriteria(gemsProjectMaster, "");
		return super.getObjectListCount(GemsProjectMaster.class, criteria);
	}

	public List getGemsProjectMasterList(int start, int limit, GemsProjectMaster gemsProjectMaster) {
		DetachedCriteria criteria = createGemsProjectMasterCriteria(gemsProjectMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsProjectMaster.class, criteria, start, limit);
	}

	public List getAllGemsProjectMasterList(GemsProjectMaster gemsProjectMaster) {
		DetachedCriteria criteria = createGemsProjectMasterCriteria(gemsProjectMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(GemsProjectMaster.class, criteria);
	}

	public void saveGemsProjectMaster(GemsProjectMaster gemsProjectMaster) {
		super.saveOrUpdate(gemsProjectMaster);
	}
	
	public void saveBatchProjects(List projectList) {
		super.saveAll(projectList);
	}

	public void removeGemsProjectMaster(GemsProjectMaster gemsProjectMaster) {
		super.delete(gemsProjectMaster);
	}

	public GemsProjectMaster getGemsProjectMaster(Integer Id) {
		return (GemsProjectMaster) super.find(GemsProjectMaster.class, Id);
	}

	public GemsProjectMaster getGemsProjectMasterByCode(GemsProjectMaster gemsProjectMaster) {
		DetachedCriteria criteria = createGemsProjectMasterCriteria(gemsProjectMaster, "exact");
		return (GemsProjectMaster) super.checkUniqueCode(GemsProjectMaster.class, criteria);
	}

	/*
	 * End of Gems Project Master Methods
	 */

	/*
	 * Gems Project Resource Methods
	 */

	private DetachedCriteria createGemsProjectResourceMasterCriteria(
			GemsProjectResourceMaster gemsProjectResourceMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsProjectResourceMaster.class);
		if (gemsProjectResourceMaster.getGemsProjectMaster() != null) {
			if (gemsProjectResourceMaster.getGemsProjectMaster().getGemsProjectMasterId() != 0) {
				criteria.add(Restrictions.eq("gemsProjectMaster", gemsProjectResourceMaster.getGemsProjectMaster()));
			} else {
				criteria.createAlias("gemsProjectMaster", "gemsProjectMaster");
				criteria.add(Restrictions
						.like("gemsProjectMaster.projectName",
								"%" + gemsProjectResourceMaster.getGemsProjectMaster().getProjectName() + "%")
						.ignoreCase());
			}

		}
		if (gemsProjectResourceMaster.getGemsEmployeeMaster() != null) {
			// criteria.add(Restrictions.eq("gemsEmployeeMaster",gemsProjectResourceMaster.getGemsEmployeeMaster()));

			if (gemsProjectResourceMaster.getGemsEmployeeMaster().getGemsEmployeeMasterId() != 0) {
				
				criteria.add(Restrictions.eq("gemsEmployeeMaster",gemsProjectResourceMaster.getGemsEmployeeMaster()));
				/*
				Criterion cr1 = Restrictions.eq("gemsEmployeeMaster",
						gemsProjectResourceMaster.getGemsEmployeeMaster());
				criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
				Criterion cr2 = Restrictions.eq("gemsEmployeeMaster.currentReportingTo",
						gemsProjectResourceMaster.getGemsEmployeeMaster());
				criteria.add(Restrictions.or(cr1, cr2));*/
			} else {
				if (gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
					criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
					criteria.add(Restrictions
							.like("gemsEmployeeMaster.employeeFirstName", "%"
									+ gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() + "%")
							.ignoreCase());
				}
				if (gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName() != null) {
					criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
					criteria.add(Restrictions
							.like("gemsEmployeeMaster.employeeLastName",
									"%" + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName() + "%")
							.ignoreCase());
				}
			}

		}
		if (gemsProjectResourceMaster.getActiveStatus() != null) {
			criteria.add(Restrictions.eq("activeStatus", gemsProjectResourceMaster.getActiveStatus()));
		}

		return criteria;

	}

	public int getGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "");
	//	criteria.setProjection(Projections.distinct(Projections.property("gemsProjectMaster")));
		return super.getObjectListCount(GemsProjectResourceMaster.class, criteria);
	}

	public List getGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "");
		//criteria.setProjection(Projections.distinct(Projections.property("gemsProjectMaster")));
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsProjectResourceMaster.class, criteria, start, limit);
	}

	public List getAllGemsProjectResourceMasterList(GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getAllObjectList(GemsProjectResourceMaster.class, criteria);
	}

	public void saveGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster) {
		logger.info("resource project:" + gemsProjectResourceMaster.getGemsProjectMaster().getGemsProjectMasterId());
		super.saveOrUpdate(gemsProjectResourceMaster);
	}
	
	public void saveBatchProjectResources(List gemsProjectResourceMasterList) {
		super.saveAll(gemsProjectResourceMasterList);
	}

	public void removeGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster) {
		super.delete(gemsProjectResourceMaster);
	}

	public GemsProjectResourceMaster getGemsProjectResourceMaster(Integer Id) {
		return (GemsProjectResourceMaster) super.find(GemsProjectResourceMaster.class, Id);
	}

	public GemsProjectResourceMaster getGemsProjectResourceMasterByResourceAndProject(
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createMyGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "exact");
		return (GemsProjectResourceMaster) super.checkUniqueCode(GemsProjectResourceMaster.class, criteria);
	}

	private DetachedCriteria createMyGemsProjectResourceMasterCriteria(
			GemsProjectResourceMaster gemsProjectResourceMaster, String searchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsProjectResourceMaster.class);
		if (gemsProjectResourceMaster.getGemsProjectMaster() != null) {
			if (gemsProjectResourceMaster.getGemsProjectMaster().getGemsProjectMasterId() != 0) {
				criteria.add(Restrictions.eq("gemsProjectMaster", gemsProjectResourceMaster.getGemsProjectMaster()));
			} else {
				criteria.createAlias("gemsProjectMaster", "gemsProjectMaster");
				criteria.add(Restrictions
						.like("gemsProjectMaster.projectName",
								"%" + gemsProjectResourceMaster.getGemsProjectMaster().getProjectName() + "%")
						.ignoreCase());
			}

		}
		if (gemsProjectResourceMaster.getGemsEmployeeMaster() != null) {
			// criteria.add(Restrictions.eq("gemsEmployeeMaster",gemsProjectResourceMaster.getGemsEmployeeMaster()));

			if (gemsProjectResourceMaster.getGemsEmployeeMaster().getGemsEmployeeMasterId() != 0) {
				
				/*Criterion cr1 = Restrictions.eq("gemsEmployeeMaster",
						gemsProjectResourceMaster.getGemsEmployeeMaster());
				criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
				Criterion cr2 = Restrictions.eq("gemsEmployeeMaster.currentReportingTo",
						gemsProjectResourceMaster.getGemsEmployeeMaster());
				criteria.add(Restrictions.or(cr1, cr2));*/
				criteria.add(Restrictions.eq("gemsEmployeeMaster",gemsProjectResourceMaster.getGemsEmployeeMaster()));
			} else {
				if (gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() != null) {
					criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
					criteria.add(Restrictions
							.like("gemsEmployeeMaster.employeeFirstName", "%"
									+ gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeFirstName() + "%")
							.ignoreCase());
				}
				if (gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName() != null) {
					criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
					criteria.add(Restrictions
							.like("gemsEmployeeMaster.employeeLastName",
									"%" + gemsProjectResourceMaster.getGemsEmployeeMaster().getEmployeeLastName() + "%")
							.ignoreCase());
				}
			}

		}
		return criteria;

	}

	public int getMyGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createMyGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "");
		return super.getObjectListCount(GemsProjectResourceMaster.class, criteria);
	}

	public List getMyGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		DetachedCriteria criteria = createMyGemsProjectResourceMasterCriteria(gemsProjectResourceMaster, "");
		criteria.addOrder(Order.desc("updatedOn"));
		return super.getObjectListByRange(GemsProjectResourceMaster.class, criteria, start, limit);
	}

}
