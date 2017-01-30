package eProject.dao.timesheet;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.ErpAbstractDao;
import eProject.domain.project.GemsProjectTypeMaster;
import eProject.domain.timesheet.GemsEmployeeTimeSheet;
import eProject.domain.timesheet.GemsEmployeeTimeSheetHeader;
import eProject.domain.timesheet.GemsEmployeeTimeSheetView;

@Repository("TimeSheetDAO")
public class TimeSheetDao extends ErpAbstractDao {

	private DetachedCriteria createTimeSheetCriteria(GemsEmployeeTimeSheetHeader timeSheet, String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeTimeSheetHeader.class);

		if (timeSheet.getGemsEmployeeMaster() != null) {
			if (matchType.equalsIgnoreCase("exact")) {
				if (timeSheet.getGemsEmployeeMaster().getGemsEmployeeMasterId() > 0) {
					criteria.add(Restrictions.eq("gemsEmployeeMaster", timeSheet.getGemsEmployeeMaster()));
				} else {
					criteria.createCriteria("gemsEmployeeMaster", "gemsEmployeeMaster")
							.add(Restrictions
									.like("gemsEmployeeMaster.employeeLastName",
											"%" + timeSheet.getGemsEmployeeMaster().getEmployeeLastName() + "%")
									.ignoreCase());
				}
			} else {
				criteria.add(Restrictions.eq("gemsEmployeeMaster", timeSheet.getGemsEmployeeMaster()));
			}
		}
		if (timeSheet.getTimeSheetMonthYear() != null) {

			criteria.add(
					Restrictions.eq("timeSheetMonthYear", "" + timeSheet.getTimeSheetMonthYear() + "").ignoreCase());

		}
		if (timeSheet.getTimesheetApprovedStatus() != null) {

			criteria.add(Restrictions.eq("timesheetApprovedStatus", "" + timeSheet.getTimesheetApprovedStatus() + "")
					.ignoreCase());

		}
		return criteria;

	}

	private DetachedCriteria createTimeSheetApprovalCriteria(GemsEmployeeTimeSheetHeader timeSheet, String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeTimeSheetHeader.class);

		if (timeSheet.getGemsEmployeeMaster() != null) {
			if (matchType.equalsIgnoreCase("exact")) {
				if (timeSheet.getGemsEmployeeMaster().getGemsEmployeeMasterId() > 0) {
					// criteria.add(Restrictions.eq("gemsEmployeeMaster",
					// timeSheet.getGemsEmployeeMaster()));
					criteria.createAlias("gemsEmployeeMaster", "gemsEmployeeMaster");
					criteria.add(Restrictions.eq("gemsEmployeeMaster.currentReportingTo",
							timeSheet.getGemsEmployeeMaster()));

				} else {
					// criteria.add(Restrictions.eq("projectMaster",
					// ""+timeSheet.getProjectMaster()+"").ignoreCase());
					criteria.createCriteria("gemsEmployeeMaster", "gemsEmployeeMaster")
							.add(Restrictions
									.like("gemsEmployeeMaster.employeeLastName",
											"%" + timeSheet.getGemsEmployeeMaster().getEmployeeLastName() + "%")
									.ignoreCase());
				}
			} else {
				criteria.add(Restrictions.eq("gemsEmployeeMaster", timeSheet.getGemsEmployeeMaster()));
			}
		}
		if (timeSheet.getTimeSheetMonthYear() != null) {

			criteria.add(
					Restrictions.eq("timeSheetMonthYear", "" + timeSheet.getTimeSheetMonthYear() + "").ignoreCase());

		}
		if (timeSheet.getTimesheetApprovedStatus() != null) {

			criteria.add(Restrictions.eq("timesheetApprovedStatus", "" + timeSheet.getTimesheetApprovedStatus() + "")
					.ignoreCase());

		}
		return criteria;

	}

	public int getTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetCriteria(timeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetHeaderId"));
		return super.getObjectListCount(GemsEmployeeTimeSheetHeader.class, criteria);
	}

	public int getApprovalTimeSheetFilterCount(GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetApprovalCriteria(timeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetHeaderId"));
		return super.getObjectListCount(GemsEmployeeTimeSheetHeader.class, criteria);
	}

	public GemsEmployeeTimeSheetHeader getTimeSheetBySearchParameter(GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetCriteria(timeSheet, "exact");
		return (GemsEmployeeTimeSheetHeader) super.checkObjectByParameter(GemsEmployeeTimeSheetHeader.class, criteria);
	}

	public List getTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetCriteria(timeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetHeaderId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getObjectListByRange(GemsEmployeeTimeSheetHeader.class, criteria, start, limit);
	}

	public List getApprovalTimeSheetList(int start, int limit, GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetApprovalCriteria(timeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetHeaderId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getObjectListByRange(GemsEmployeeTimeSheetHeader.class, criteria, start, limit);
	}

	public List getAllTimeSheetList(GemsEmployeeTimeSheetHeader timeSheet) {
		DetachedCriteria criteria = createTimeSheetCriteria(timeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetHeaderId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getAllObjectList(GemsEmployeeTimeSheetHeader.class, criteria);
	}

	public GemsEmployeeTimeSheetHeader saveTimeSheetWithReturn(GemsEmployeeTimeSheetHeader timeSheet) {
		return (GemsEmployeeTimeSheetHeader) super.saveOrUpdatewithReturn(timeSheet);
	}
	
	public void saveBatchTimeSheetHeader(List employeeTimeSheetHeaderList) {
		super.saveAll(employeeTimeSheetHeaderList);
	}

	public GemsEmployeeTimeSheetHeader getTimeSheetById(Integer Id) {
		return (GemsEmployeeTimeSheetHeader) super.find(GemsEmployeeTimeSheetHeader.class, Id);
	}

	public void removeGemsEmployeeTimeSheetHeader(GemsEmployeeTimeSheetHeader gemsEmployeeTimeSheetHeader) {
		super.delete(gemsEmployeeTimeSheetHeader);
	}

	private DetachedCriteria createGemsEmployeeTimeSheetCriteria(GemsEmployeeTimeSheet gemsEmployeeTimeSheet,
			String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeTimeSheet.class);

		if (gemsEmployeeTimeSheet.getEmployeeId() > 0) {
			logger.info("Employee Id @ Timesheet DAO  :" + gemsEmployeeTimeSheet.getEmployeeId());
			criteria.add(Restrictions.eq("employeeId", gemsEmployeeTimeSheet.getEmployeeId()));

		}
		if (gemsEmployeeTimeSheet.getTimeMonthYear() != null) {
			criteria.add(Restrictions.eq("timeMonthYear", gemsEmployeeTimeSheet.getTimeMonthYear()));
		}
		if (gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetId() != null) {
			criteria.add(
					Restrictions.eq("gemsEmployeeTimeSheetId", gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetId()));
		}
		if (gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader() != null) 
		{
			
			if (gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getGemsEmployeeTimeSheetHeaderId() != null)
			{
				criteria.add(Restrictions.eq("gemsEmployeeTimeSheetHeader",
						gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader()));
			}
			else
			{
				criteria.createAlias("gemsEmployeeTimeSheetHeader", "gemsEmployeeTimeSheetHeader");
				criteria.add(Restrictions.eq("gemsEmployeeTimeSheetHeader.timesheetApprovedStatus",
						gemsEmployeeTimeSheet.getGemsEmployeeTimeSheetHeader().getTimesheetApprovedStatus()));
			}		
		}
		if (gemsEmployeeTimeSheet.getActiveStatus() != null)
		{
			criteria.add(Restrictions.eq("activeStatus", gemsEmployeeTimeSheet.getActiveStatus()));
		}
		return criteria;

	}

	public GemsEmployeeTimeSheet saveGemsEmployeeTimeSheetWithReturn(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		logger.info("In DAO saveOrUpdatewithReturn method");
		return (GemsEmployeeTimeSheet) super.saveOrUpdatewithReturn(gemsEmployeeTimeSheet);
	}

	public int getGemsEmployeeTimeSheetFilterCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		DetachedCriteria criteria = createGemsEmployeeTimeSheetCriteria(gemsEmployeeTimeSheet, "");
		return super.getObjectListCount(GemsEmployeeTimeSheet.class, criteria);
	}

	public List getGemsEmployeeTimeSheetList(int start, int limit, GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		DetachedCriteria criteria = createGemsEmployeeTimeSheetCriteria(gemsEmployeeTimeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetId"));
		return super.getObjectListByRange(GemsEmployeeTimeSheet.class, criteria, start, limit);

	}

	public List getAllGemsEmployeeTimeSheetList(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {

		DetachedCriteria criteria = createGemsEmployeeTimeSheetCriteria(gemsEmployeeTimeSheet, "exact");
		criteria.addOrder(Order.desc("gemsEmployeeTimeSheetId"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return super.getAllObjectList(GemsEmployeeTimeSheet.class, criteria);

	}

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetByMonthYear(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		DetachedCriteria criteria = createGemsEmployeeTimeSheetCriteria(gemsEmployeeTimeSheet, "exact");
		return (GemsEmployeeTimeSheet) super.checkUniqueCode(GemsEmployeeTimeSheet.class, criteria);
	}

	public GemsEmployeeTimeSheet getGemsEmployeeTimeSheetById(Integer Id) {
		return (GemsEmployeeTimeSheet) super.find(GemsEmployeeTimeSheet.class, Id);
	}

	public void removeGemsEmployeeTimeSheet(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		super.delete(gemsEmployeeTimeSheet);
	}

	public void saveBatchTimeSheet(List employeeTimeSheetList) {
		super.saveAll(employeeTimeSheetList);
	}

	private DetachedCriteria createGemsEmployeeTimeSheetProjectionCriteria(GemsEmployeeTimeSheet gemsEmployeeTimeSheet,
			String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeTimeSheet.class);

		if (gemsEmployeeTimeSheet.getEmployeeId() > 0) {
			logger.info("Employee Id @ Timesheet DAO  :" + gemsEmployeeTimeSheet.getEmployeeId());
			criteria.add(Restrictions.eq("employeeId", gemsEmployeeTimeSheet.getEmployeeId()));

		}
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.sum("totalHours").as("totalHours"));
		projList.add(Projections.groupProperty("timeMonthYear").as("timeMonthYear"));

		criteria.setProjection(projList).setResultTransformer(Transformers.aliasToBean(GemsEmployeeTimeSheet.class));
		return criteria;

	}

	public int getGemsEmployeeTimeSheetFilterProjectionCount(GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		DetachedCriteria criteria = createGemsEmployeeTimeSheetProjectionCriteria(gemsEmployeeTimeSheet, "");
		return super.getObjectListCount(GemsEmployeeTimeSheet.class, criteria);
	}

	public List getGemsEmployeeTimeSheetProjectionList(int start, int limit,
			GemsEmployeeTimeSheet gemsEmployeeTimeSheet) {
		DetachedCriteria criteria = createGemsEmployeeTimeSheetProjectionCriteria(gemsEmployeeTimeSheet, "exact");
		criteria.addOrder(Order.desc("timeMonthYear"));
		List list = super.getObjectListByRange(GemsEmployeeTimeSheet.class, criteria, start, limit);
		return list;

	}
	
	
	private DetachedCriteria createGemsEmployeeTimeSheetViewCriteria(GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView,
			String matchType) {
		DetachedCriteria criteria = DetachedCriteria.forClass(GemsEmployeeTimeSheetView.class);

		if (gemsEmployeeTimeSheetView.getEmployeeId() != null) {
			logger.info("Employee Id @ Timesheet DAO  :" + gemsEmployeeTimeSheetView.getEmployeeId());
			criteria.add(Restrictions.eq("employeeId", gemsEmployeeTimeSheetView.getEmployeeId()));

		}
		if (gemsEmployeeTimeSheetView.getTimeMonthYear() != null) {
			criteria.add(Restrictions.eq("timeMonthYear", gemsEmployeeTimeSheetView.getTimeMonthYear()));
		}
		if (gemsEmployeeTimeSheetView.getTimeSheetApprovedStatus() != null) {

			criteria.add(Restrictions.eq("timeSheetApprovedStatus", "" + gemsEmployeeTimeSheetView.getTimeSheetApprovedStatus() + "")
					.ignoreCase());

		}
		return criteria;

	}
	
	public List getAllTimeSheets(GemsEmployeeTimeSheetView gemsEmployeeTimeSheetView) {

		DetachedCriteria criteria = createGemsEmployeeTimeSheetViewCriteria(gemsEmployeeTimeSheetView, "exact");
		return super.getAllObjectList(GemsEmployeeTimeSheetView.class, criteria);

	}
	
	
}
