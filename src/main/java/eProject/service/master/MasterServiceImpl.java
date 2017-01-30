package eProject.service.master;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import eProject.dao.master.MasterDao;
import eProject.domain.leavemanagement.GemsLeavePeriodMaster;
import eProject.domain.leavemanagement.GemsLeaveTypeMaster;
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
import eProject.domain.master.GemsRoleTransactionApproval;
import eProject.domain.master.GemsTransactionApprovalMaster;
import eProject.domain.master.GemsRoleMaster;
import eProject.domain.master.GemsUserMaster;
import eProject.domain.master.GemsUserToken;
import eProject.domain.master.GemsWorkShiftMaster;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service("masterService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MasterServiceImpl implements MasterService {

	@Autowired
	private MasterDao masterDao;

	public MasterServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eProject.service.master.MasterService#getLoginVerificationByEmail(
	 * eProject.domain.master.GemsUserMaster)
	 */

	public GemsUserMaster getLoginVerificationByEmail(GemsUserMaster gemsUserMaster) {
		return masterDao.getLoginVerificationByEmail(gemsUserMaster);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eProject.service.master.MasterService#getOdComponentFilterCount(eProject.
	 * domain.master.GemsComponentMaster)
	 */

	public int getGemsComponentMasterListFilterCount(GemsComponentMaster gemsComponentMaster) {
		return masterDao.getGemsComponentMasterListFilterCount(gemsComponentMaster);
	}

	public List getGemsComponentMasterList(int start, int limit, GemsComponentMaster gemsComponentMaster) {
		return masterDao.getGemsComponentMasterList(start, limit, gemsComponentMaster);
	}

	public void saveGemsComponentMaster(GemsComponentMaster gemsComponentMaster) {
		masterDao.saveGemsComponentMaster(gemsComponentMaster);
	}

	public void removeGemsComponentMaster(GemsComponentMaster gemsComponentMaster) {
		masterDao.removeGemsComponentMaster(gemsComponentMaster);
	}

	public GemsComponentMaster getGemsComponentMaster(Integer componentId) {
		return masterDao.getGemsComponentMaster(componentId);
	}

	public List getOdActivityListByRole(GemsActivityMaster gemsActivityMaster) {
		return masterDao.getOdActivityListByRole(gemsActivityMaster);
	}

	/*
	 * Gems Country Methods
	 */
	public int getGemsCountryMasterListFilterCount(GemsCountryMaster gemsCountryMaster) {
		return masterDao.getGemsCountryMasterListFilterCount(gemsCountryMaster);
	}

	public List getGemsCountryMasterList(int start, int limit, GemsCountryMaster gemsCountryMaster) {
		return masterDao.getGemsCountryMasterList(start, limit, gemsCountryMaster);
	}

	public void saveGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		masterDao.saveGemsCountryMaster(gemsCountryMaster);
	}

	public void removeGemsCountryMaster(GemsCountryMaster gemsCountryMaster) {
		masterDao.removeGemsCountryMaster(gemsCountryMaster);
	}

	public GemsCountryMaster getGemsCountryMaster(Integer countryId) {
		return masterDao.getGemsCountryMaster(countryId);
	}

	public GemsCountryMaster getGemsCountryMasterByCode(GemsCountryMaster gemsCountryMaster) {
		return masterDao.getGemsCountryMasterByCode(gemsCountryMaster);
	}

	/*
	 * End of Gems Country Methods
	 */

	/*
	 * Organisation Methods
	 */
	public void saveGemsOrganisation(GemsOrganisation gemsOrganisation) {
		masterDao.saveGemsOrganisation(gemsOrganisation);
	}

	public GemsOrganisation getGemsOrganisation(Integer gemsOrgId) {
		return masterDao.getGemsOrganisation(gemsOrgId);
	}

	public int getGemsOrganisationFilterCount(GemsOrganisation gemsOrganisation) {
		return masterDao.getGemsOrganisationFilterCount(gemsOrganisation);
	}

	public List getGemsOrganisationList(int start, int limit, GemsOrganisation gemsOrganisation) {
		return masterDao.getGemsOrganisationList(start, limit, gemsOrganisation);
	}

	public void removeGemsOrganisation(GemsOrganisation gemsOrganisation) {
		masterDao.removeGemsOrganisation(gemsOrganisation);
	}

	public GemsOrganisation getGemsOrganisationByCode(GemsOrganisation gemsOrganisation) {
		return masterDao.getGemsOrganisationByCode(gemsOrganisation);
	}
	/*
	 * End Organisation
	 */

	/*
	 * Business Unit Methods
	 */

	public int getGemsBusinessUnitFilterCount(GemsBusinessUnit gemsBusinessUnit) {
		return masterDao.getGemsBusinessUnitFilterCount(gemsBusinessUnit);
	}

	public List getGemsBusinessUnitList(int start, int limit, GemsBusinessUnit gemsBusinessUnit) {
		return masterDao.getGemsBusinessUnitList(start, limit, gemsBusinessUnit);
	}

	public void saveGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit) {
		masterDao.saveGemsBusinessUnit(gemsBusinessUnit);
	}

	public void removeGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit) {
		masterDao.removeGemsBusinessUnit(gemsBusinessUnit);
	}

	public GemsBusinessUnit getGemsBusinessUnit(Integer Id) {
		return masterDao.getGemsBusinessUnit(Id);
	}

	public GemsBusinessUnit getGemsBusinessUnitByCode(GemsBusinessUnit gemsBusinessUnit) {
		return masterDao.getGemsBusinessUnitByCode(gemsBusinessUnit);
	}

	/*
	 * End of Business Unit Methods
	 */

	/*
	 * Role Master Methods
	 */
	public int getGemsRoleMasterFilterCount(GemsRoleMaster gemsRoleMaster) {
		return masterDao.getGemsRoleMasterFilterCount(gemsRoleMaster);
	}

	public List getGemsRoleMasterList(int start, int limit, GemsRoleMaster gemsRoleMaster) {
		return masterDao.getGemsRoleMasterList(start, limit, gemsRoleMaster);
	}

	public List getAllGemsRoleMasterList(GemsRoleMaster gemsRoleMaster) {
		return masterDao.getAllGemsRoleMasterList(gemsRoleMaster);
	}

	public void saveGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		masterDao.saveGemsRoleMaster(gemsRoleMaster);
	}

	public void removeGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		masterDao.removeGemsRoleMaster(gemsRoleMaster);
	}

	public GemsRoleMaster getGemsRoleMaster(Integer Id) {
		return masterDao.getGemsRoleMaster(Id);
	}

	public GemsRoleMaster getGemsRoleMasterByCode(GemsRoleMaster gemsRoleMaster) {
		return masterDao.getGemsRoleMasterByCode(gemsRoleMaster);
	}

	/*
	 * End of Role Master Methods
	 */

	/*
	 * Gems User Methods
	 */

	public int getGemsUserMasterFilterCount(GemsUserMaster gemsUserMaster) {
		return masterDao.getGemsUserMasterFilterCount(gemsUserMaster);
	}

	public List getGemsUserMasterList(int start, int limit, GemsUserMaster gemsUserMaster) {
		return masterDao.getGemsUserMasterList(start, limit, gemsUserMaster);
	}

	public void saveGemsUserMaster(GemsUserMaster gemsUserMaster) {
		masterDao.saveGemsUserMaster(gemsUserMaster);
	}

	public void removeGemsUserMaster(GemsUserMaster gemsUserMaster) {
		masterDao.removeGemsUserMaster(gemsUserMaster);
	}

	public GemsUserMaster getGemsUserMaster(Integer Id) {
		return masterDao.getGemsUserMaster(Id);
	}

	public GemsUserMaster getGemsUserMasterByCode(GemsUserMaster gemsUserMaster) {
		return masterDao.getGemsUserMasterByCode(gemsUserMaster);
	}

	/*
	 * End of User Master Methods
	 */

	/*
	 * Gems Department Methods
	 */
	public int getGemsDepartmentFilterCount(GemsDepartment gemsDepartment) {
		return masterDao.getGemsDepartmentFilterCount(gemsDepartment);
	}

	public List getGemsDepartmentList(int start, int limit, GemsDepartment gemsDepartment) {
		return masterDao.getGemsDepartmentList(start, limit, gemsDepartment);
	}

	public void saveGemsDepartment(GemsDepartment gemsDepartment) {
		masterDao.saveGemsDepartment(gemsDepartment);
	}

	public void removeGemsDepartment(GemsDepartment gemsDepartment) {
		masterDao.removeGemsDepartment(gemsDepartment);
	}

	public GemsDepartment getGemsDepartment(Integer Id) {
		return masterDao.getGemsDepartment(Id);
	}

	public GemsDepartment getGemsDepartmentByCode(GemsDepartment gemsDepartment) {

		return masterDao.getGemsDepartmentByCode(gemsDepartment);
	}

	/*
	 * End of Designation Methods
	 */

	public int getGemsDesignationFilterCount(GemsDesignation gemsDesignation) {
		return masterDao.getGemsDesignationFilterCount(gemsDesignation);
	}

	public List getGemsDesignationList(int start, int limit, GemsDesignation gemsDesignation) {
		return masterDao.getGemsDesignationList(start, limit, gemsDesignation);
	}

	public void saveGemsDesignation(GemsDesignation gemsDesignation) {
		masterDao.saveGemsDesignation(gemsDesignation);
	}

	public void removeGemsDesignation(GemsDesignation gemsDesignation) {
		masterDao.removeGemsDesignation(gemsDesignation);
	}

	public GemsDesignation getGemsDesignation(Integer Id) {
		return masterDao.getGemsDesignation(Id);
	}

	public GemsDesignation getGemsDesignationByCode(GemsDesignation gemsDesignation) {
		return masterDao.getGemsDesignationByCode(gemsDesignation);
	}

	/*
	 * End of Designation Methods
	 */

	/*
	 * Gems Currency
	 */

	public int getGemsCurrencyMasterFilterCount(GemsCurrencyMaster gemsCurrencyMaster) {

		return masterDao.getGemsCurrencyMasterFilterCount(gemsCurrencyMaster);
	}

	public List getGemsCurrencyMasterList(int start, int limit, GemsCurrencyMaster gemsCurrencyMaster) {

		return masterDao.getGemsCurrencyMasterList(start, limit, gemsCurrencyMaster);
	}

	public void saveGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		masterDao.saveGemsCurrencyMaster(gemsCurrencyMaster);
	}

	public void removeGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		masterDao.removeGemsCurrencyMaster(gemsCurrencyMaster);
	}

	public GemsCurrencyMaster getGemsCurrencyMaster(Integer Id) {
		return masterDao.getGemsCurrencyMaster(Id);
	}

	public GemsCurrencyMaster getGemsCurrencyMasterByCode(GemsCurrencyMaster gemsCurrencyMaster) {
		return masterDao.getGemsCurrencyMasterByCode(gemsCurrencyMaster);
	}

	/*
	 * End of Gems Currency
	 */

	/*
	 * Gems Education Master Methods
	 */

	public int getGemsEducationMasterFilterCount(GemsEducationMaster gemsEducationMaster) {
		return masterDao.getGemsEducationMasterFilterCount(gemsEducationMaster);
	}

	public List getGemsEducationMasterList(int start, int limit, GemsEducationMaster gemsEducationMaster) {
		return masterDao.getGemsEducationMasterList(start, limit, gemsEducationMaster);
	}

	public void saveGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		masterDao.saveGemsEducationMaster(gemsEducationMaster);
	}

	public void removeGemsEducationMaster(GemsEducationMaster gemsEducationMaster) {
		masterDao.removeGemsEducationMaster(gemsEducationMaster);
	}

	public GemsEducationMaster getGemsEducationMaster(Integer Id) {
		return masterDao.getGemsEducationMaster(Id);
	}

	public GemsEducationMaster getGemsEducationMasterByCode(GemsEducationMaster gemsEducationMaster) {
		return masterDao.getGemsEducationMasterByCode(gemsEducationMaster);
	}

	/*
	 * End of Education Master Methods
	 */

	/*
	 * Gems Employment Status Methods
	 */

	public int getGemsEmploymentStatusFilterCount(GemsEmploymentStatus gemsEmploymentStatus) {
		return masterDao.getGemsEmploymentStatusFilterCount(gemsEmploymentStatus);
	}

	public List getGemsEmploymentStatusList(int start, int limit, GemsEmploymentStatus gemsEmploymentStatus) {
		return masterDao.getGemsEmploymentStatusList(start, limit, gemsEmploymentStatus);
	}

	public void saveGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus) {
		masterDao.saveGemsEmploymentStatus(gemsEmploymentStatus);
	}

	public void removeGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus) {
		masterDao.removeGemsEmploymentStatus(gemsEmploymentStatus);
	}

	public GemsEmploymentStatus getGemsEmploymentStatus(Integer Id) {
		return masterDao.getGemsEmploymentStatus(Id);
	}

	public GemsEmploymentStatus getGemsEmploymentStatusByCode(GemsEmploymentStatus gemsEmploymentStatus) {
		return masterDao.getGemsEmploymentStatusByCode(gemsEmploymentStatus);
	}

	/*
	 * End of Employment Status Methods
	 */

	/*
	 * Gems Natinality Methods
	 */

	public int getGemsNationalitiesMasterFilterCount(GemsNationalitiesMaster gemsNationalitiesMaster) {
		return masterDao.getGemsNationalitiesMasterFilterCount(gemsNationalitiesMaster);
	}

	public List getGemsNationalitiesMasterList(int start, int limit, GemsNationalitiesMaster gemsNationalitiesMaster) {
		return masterDao.getGemsNationalitiesMasterList(start, limit, gemsNationalitiesMaster);
	}

	public void saveGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster) {
		masterDao.saveGemsNationalitiesMaster(gemsNationalitiesMaster);
	}

	public void removeGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster) {
		masterDao.removeGemsNationalitiesMaster(gemsNationalitiesMaster);
	}

	public GemsNationalitiesMaster getGemsNationalitiesMaster(Integer Id) {
		return masterDao.getGemsNationalitiesMaster(Id);
	}

	public GemsNationalitiesMaster getGemsNationalitiesMasterByCode(GemsNationalitiesMaster gemsNationalitiesMaster) {
		return masterDao.getGemsNationalitiesMasterByCode(gemsNationalitiesMaster);
	}

	/*
	 * End of Nationality Methods
	 */

	/*
	 * Gems Paygrade Methods
	 */

	public int getGemsPayGradeFilterCount(GemsPayGrade gemsPayGrade) {
		return masterDao.getGemsPayGradeFilterCount(gemsPayGrade);
	}

	public List getGemsPayGradeList(int start, int limit, GemsPayGrade gemsPayGrade) {
		return masterDao.getGemsPayGradeList(start, limit, gemsPayGrade);
	}

	public void saveGemsPayGrade(GemsPayGrade gemsPayGrade) {
		masterDao.saveGemsPayGrade(gemsPayGrade);
	}

	public void removegemsPayGrade(GemsPayGrade gemsPayGrade) {
		masterDao.removeGemsPayGrade(gemsPayGrade);
	}

	public GemsPayGrade getGemsPayGrade(Integer Id) {
		return masterDao.getGemsPayGrade(Id);
	}

	public GemsPayGrade getGemsPayGradeByCode(GemsPayGrade gemsPayGrade) {
		return masterDao.getGemsPayGradeByCode(gemsPayGrade);
	}

	/*
	 * End of Gems Paygrade Methods
	 */

	/*
	 * Gems Shift Hours Methods
	 */

	public int getGemsWorkShiftMasterFilterCount(GemsWorkShiftMaster gemsWorkShiftMaster) {
		return masterDao.getGemsWorkShiftMasterFilterCount(gemsWorkShiftMaster);
	}

	public List getGemsWorkShiftMasterList(int start, int limit, GemsWorkShiftMaster gemsWorkShiftMaster) {
		return masterDao.getGemsWorkShiftMasterList(start, limit, gemsWorkShiftMaster);
	}

	public void saveGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster) {
		masterDao.saveGemsWorkShiftMaster(gemsWorkShiftMaster);
	}

	public void removeGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster) {
		masterDao.removeGemsWorkShiftMaster(gemsWorkShiftMaster);
	}

	public GemsWorkShiftMaster getGemsWorkShiftMaster(Integer Id) {
		return masterDao.getGemsWorkShiftMaster(Id);
	}

	public GemsWorkShiftMaster getGemsWorkShiftMasterByCode(GemsWorkShiftMaster gemsWorkShiftMaster) {
		return masterDao.getGemsWorkShiftMasterByCode(gemsWorkShiftMaster);
	}

	/*
	 * End of Gems Shift Hours Methods
	 */

	/*
	 * Gems Alert Notification Master Methods
	 */

	public int getGemsAlertNotificationMasterFilterCount(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		return masterDao.getGemsAlertNotificationMasterFilterCount(gemsAlertNotificationMaster);
	}

	public List getGemsAlertNotificationMasterList(int start, int limit,
			GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		return masterDao.getGemsAlertNotificationMasterList(start, limit, gemsAlertNotificationMaster);
	}

	public void saveGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		masterDao.saveGemsAlertNotificationMaster(gemsAlertNotificationMaster);
	}

	public void removeGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		masterDao.removeGemsAlertNotificationMaster(gemsAlertNotificationMaster);
	}

	public GemsAlertNotificationMaster getGemsAlertNotificationMaster(Integer Id) {
		return masterDao.getGemsAlertNotificationMaster(Id);
	}

	public GemsAlertNotificationMaster getGemsAlertNotificationMasterByCode(
			GemsAlertNotificationMaster gemsAlertNotificationMaster) {
		return masterDao.getGemsAlertNotificationMasterByCode(gemsAlertNotificationMaster);
	}

	/*
	 * End of Gems Alert Notification Methods
	 */

	/*
	 * Gems Notification Methods
	 */

	public int getGemsNotificationFilterCount(GemsNotification gemsNotification) {
		return masterDao.getGemsNotificationFilterCount(gemsNotification);
	}

	public List getGemsNotificationList(int start, int limit, GemsNotification gemsNotification) {
		return masterDao.getGemsNotificationList(start, limit, gemsNotification);
	}

	public void saveGemsNotification(GemsNotification gemsNotification) {
		masterDao.saveGemsNotification(gemsNotification);
	}

	public void removeGemsNotification(GemsNotification gemsNotification) {
		masterDao.removeGemsNotification(gemsNotification);
	}

	public GemsNotification getGemsNotification(Integer Id) {
		return masterDao.getGemsNotification(Id);
	}

	/*
	 * End of Gems Notification Methods
	 */

	/*
	 * Gems Notification Assignment Methods
	 */

	public int getGemsNotificationAssignmentFilterCount(GemsNotificationAssignment gemsNotificationAssignment) {
		return masterDao.getGemsNotificationAssignmentFilterCount(gemsNotificationAssignment);
	}

	public List getGemsNotificationAssignmentList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment) {
		return masterDao.getGemsNotificationAssignmentList(start, limit, gemsNotificationAssignment);
	}

	public void saveGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment) {
		masterDao.saveGemsNotificationAssignment(gemsNotificationAssignment);
	}

	public void removeGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment) {
		masterDao.removeGemsNotificationAssignment(gemsNotificationAssignment);
	}

	public GemsNotificationAssignment getGemsNotificationAssignment(Integer Id) {
		return masterDao.getGemsNotificationAssignment(Id);
	}

	public int getGemsNotificationAssignmentByDateRangeFilterCount(
			GemsNotificationAssignment gemsNotificationAssignment) {
		return masterDao.getGemsNotificationAssignmentByDateRangeFilterCount(gemsNotificationAssignment);
	}

	public List getGemsNotificationAssignmentByDateRangeList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment) {
		return masterDao.getGemsNotificationAssignmentByDateRangeList(start, limit, gemsNotificationAssignment);
	}

	/*
	 * End of Gems Notification Assignment Methods
	 */

	/*
	 * Gems Role Transaction Approval Methods
	 */

	public GemsRoleTransactionApproval getGemsRoleTransactionApprovalByRoleTransaction(
			GemsRoleTransactionApproval gemsRoleTransactionApproval) {
		return masterDao.getGemsRoleTransactionApprovalByRoleTransaction(gemsRoleTransactionApproval);
	}

	/*
	 * End of Gems Role Transaction Approval Methods
	 */
	/*
	 * Gems Role Transaction Approval Methods
	 */

	public GemsTransactionApprovalMaster getGemsTransactionApprovalMasterByCode(
			GemsTransactionApprovalMaster gemsTransactionApprovalMaster) {
		return masterDao.getGemsTransactionApprovalMasterByCode(gemsTransactionApprovalMaster);
	}

	/*
	 * End of Gems Role Transaction Approval Methods
	 */

	public String createEncodedToken(Integer userId) {

		// Creating Expire Time that is after three hour from current time
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, 1);
		Timestamp expireTime = new Timestamp(cal.getTimeInMillis());

		// Creating Random token
		SecureRandom random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);

		try {

			// Save User Token
			GemsUserToken gemsUserToken = new GemsUserToken();
			gemsUserToken.setUserId(userId);
			gemsUserToken.setToken(token);
			gemsUserToken.setExpireTime(expireTime);
			masterDao.saveGemsUserToken(gemsUserToken);

			// Return the Encoded the token
			return gemsUserToken.getEncodedToken();

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public boolean validateToken(String tokenFromRequest, String loggedInUserId) {

		GemsUserToken userToken = new GemsUserToken();

		try {

			String decodedToken = userToken.getDecodedToken(tokenFromRequest);

			String[] tokenParams = decodedToken.split(":");

			userToken.setUserTokenId(Integer.parseInt(tokenParams[0]));
			userToken.setUserId(Integer.parseInt(loggedInUserId));

			userToken = masterDao.getGemsUserToken(userToken);
			
			

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			Timestamp currentTimeStamp = new Timestamp(cal.getTimeInMillis());

			if (userToken.getExpireTime().after(currentTimeStamp)) {
				return true;
			} else {
				if (userToken != null)
				{
					masterDao.removeGemsUserToken(userToken);
				}
				
				return false;
			}

		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public GemsUserToken getGemsUserByToken(GemsUserToken gemsUserToken) {
		return masterDao.getGemsUserToken(gemsUserToken);
	}

	public void removeGemsUserToken(GemsUserToken gemsUserToken) {
		masterDao.removeGemsUserToken(gemsUserToken);
		// TODO Auto-generated method stub

	}

}
