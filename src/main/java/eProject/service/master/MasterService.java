package eProject.service.master;

import java.util.List;

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

public interface MasterService {

	/*
	 * Gems User Master Methods
	 */
	public GemsUserMaster getLoginVerificationByEmail(GemsUserMaster gemsUserMaster);

	/*
	 * End of User Master Methods
	 */

	public int getGemsComponentMasterListFilterCount(GemsComponentMaster gemsComponentMaster);

	public List getGemsComponentMasterList(int start, int limit, GemsComponentMaster gemsComponentMaster);

	public void saveGemsComponentMaster(GemsComponentMaster gemsComponentMaster);

	public void removeGemsComponentMaster(GemsComponentMaster gemsComponentMaster);

	public GemsComponentMaster getGemsComponentMaster(Integer componentId);

	public List getOdActivityListByRole(GemsActivityMaster gemsActivityMaster);

	/*
	 * Country
	 */
	public int getGemsCountryMasterListFilterCount(GemsCountryMaster gemsCountryMaster);

	public List getGemsCountryMasterList(int start, int limit, GemsCountryMaster gemsCountryMaster);

	public void saveGemsCountryMaster(GemsCountryMaster gemsCountryMaster);

	public void removeGemsCountryMaster(GemsCountryMaster gemsCountryMaster);

	public GemsCountryMaster getGemsCountryMaster(Integer countryId);

	public GemsCountryMaster getGemsCountryMasterByCode(GemsCountryMaster gemsCountryMaster);

	/*
	 * End Country
	 */

	/*
	 * Organisation
	 */
	public void saveGemsOrganisation(GemsOrganisation gemsOrganisation);

	public GemsOrganisation getGemsOrganisation(Integer gemsOrgId);

	public int getGemsOrganisationFilterCount(GemsOrganisation gemsOrganisation);

	public List getGemsOrganisationList(int start, int limit, GemsOrganisation gemsOrganisation);

	public void removeGemsOrganisation(GemsOrganisation gemsOrganisation);

	public GemsOrganisation getGemsOrganisationByCode(GemsOrganisation gemsOrganisation);

	/*
	 * End Organisation
	 */

	/*
	 * Business Unit Methods
	 */

	public int getGemsBusinessUnitFilterCount(GemsBusinessUnit gemsBusinessUnit);

	public List getGemsBusinessUnitList(int start, int limit, GemsBusinessUnit gemsBusinessUnit);

	public void saveGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit);

	public void removeGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit);

	public GemsBusinessUnit getGemsBusinessUnit(Integer Id);

	public GemsBusinessUnit getGemsBusinessUnitByCode(GemsBusinessUnit gemsBusinessUnit);

	/*
	 * End of Business Unit Methods
	 */

	/*
	 * Gems User Methods
	 */

	public int getGemsUserMasterFilterCount(GemsUserMaster gemsUserMaster);

	public List getGemsUserMasterList(int start, int limit, GemsUserMaster gemsUserMaster);

	public void saveGemsUserMaster(GemsUserMaster gemsUserMaster);

	public void removeGemsUserMaster(GemsUserMaster gemsUserMaster);

	public GemsUserMaster getGemsUserMaster(Integer Id);

	public GemsUserMaster getGemsUserMasterByCode(GemsUserMaster gemsUserMaster);

	/*
	 * End of User Master Methods
	 */

	/*
	 * Role Master
	 */

	public int getGemsRoleMasterFilterCount(GemsRoleMaster gemsRoleMaster);

	public List getGemsRoleMasterList(int start, int limit, GemsRoleMaster gemsRoleMaster);

	public List getAllGemsRoleMasterList(GemsRoleMaster gemsRoleMaster);

	public void saveGemsRoleMaster(GemsRoleMaster gemsRoleMaster);

	public void removeGemsRoleMaster(GemsRoleMaster gemsRoleMaster);

	public GemsRoleMaster getGemsRoleMaster(Integer Id);

	public GemsRoleMaster getGemsRoleMasterByCode(GemsRoleMaster gemsRoleMaster);

	/*
	 * End of Role Master
	 */

	/*
	 * Gems Department Methods
	 */
	public int getGemsDepartmentFilterCount(GemsDepartment gemsDepartment);

	public List getGemsDepartmentList(int start, int limit, GemsDepartment gemsDepartment);

	public void saveGemsDepartment(GemsDepartment gemsDepartment);

	public void removeGemsDepartment(GemsDepartment gemsDepartment);

	public GemsDepartment getGemsDepartment(Integer Id);

	public GemsDepartment getGemsDepartmentByCode(GemsDepartment gemsDepartment);

	/*
	 * End of Department Methods
	 */

	/*
	 * End of Designation Methods
	 */

	public int getGemsDesignationFilterCount(GemsDesignation gemsDesignation);

	public List getGemsDesignationList(int start, int limit, GemsDesignation gemsDesignation);

	public void saveGemsDesignation(GemsDesignation gemsDesignation);

	public void removeGemsDesignation(GemsDesignation gemsDesignation);

	public GemsDesignation getGemsDesignation(Integer Id);

	public GemsDesignation getGemsDesignationByCode(GemsDesignation gemsDesignation);

	/*
	 * End of Designation Methods
	 */

	/*
	 * Gems Currency
	 */

	public int getGemsCurrencyMasterFilterCount(GemsCurrencyMaster gemsCurrencyMaster);

	public List getGemsCurrencyMasterList(int start, int limit, GemsCurrencyMaster gemsCurrencyMaster);

	public void saveGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster);

	public void removeGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster);

	public GemsCurrencyMaster getGemsCurrencyMaster(Integer Id);

	public GemsCurrencyMaster getGemsCurrencyMasterByCode(GemsCurrencyMaster gemsCurrencyMaster);

	/*
	 * End of Gems Currency
	 */

	/*
	 * Gems Education Master Methods
	 */

	public int getGemsEducationMasterFilterCount(GemsEducationMaster gemsEducationMaster);

	public List getGemsEducationMasterList(int start, int limit, GemsEducationMaster gemsEducationMaster);

	public void saveGemsEducationMaster(GemsEducationMaster gemsEducationMaster);

	public void removeGemsEducationMaster(GemsEducationMaster gemsEducationMaster);

	public GemsEducationMaster getGemsEducationMaster(Integer Id);

	public GemsEducationMaster getGemsEducationMasterByCode(GemsEducationMaster gemsEducationMaster);

	/*
	 * End of Education Master Methods
	 */

	/*
	 * Gems Employment Status Methods
	 */

	public int getGemsEmploymentStatusFilterCount(GemsEmploymentStatus gemsEmploymentStatus);

	public List getGemsEmploymentStatusList(int start, int limit, GemsEmploymentStatus gemsEmploymentStatus);

	public void saveGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus);

	public void removeGemsEmploymentStatus(GemsEmploymentStatus gemsEmploymentStatus);

	public GemsEmploymentStatus getGemsEmploymentStatus(Integer Id);

	public GemsEmploymentStatus getGemsEmploymentStatusByCode(GemsEmploymentStatus gemsEmploymentStatus);

	/*
	 * End of Employment Status Methods
	 */

	/*
	 * Gems Natinality Methods
	 */

	public int getGemsNationalitiesMasterFilterCount(GemsNationalitiesMaster gemsNationalitiesMaster);

	public List getGemsNationalitiesMasterList(int start, int limit, GemsNationalitiesMaster gemsNationalitiesMaster);

	public void saveGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster);

	public void removeGemsNationalitiesMaster(GemsNationalitiesMaster gemsNationalitiesMaster);

	public GemsNationalitiesMaster getGemsNationalitiesMaster(Integer Id);

	public GemsNationalitiesMaster getGemsNationalitiesMasterByCode(GemsNationalitiesMaster gemsNationalitiesMaster);

	/*
	 * End of Nationality Methods
	 */

	/*
	 * Gems Paygrade Methods
	 */

	public int getGemsPayGradeFilterCount(GemsPayGrade gemsPayGrade);

	public List getGemsPayGradeList(int start, int limit, GemsPayGrade gemsPayGrade);

	public void saveGemsPayGrade(GemsPayGrade gemsPayGrade);

	public void removegemsPayGrade(GemsPayGrade gemsPayGrade);

	public GemsPayGrade getGemsPayGrade(Integer Id);

	public GemsPayGrade getGemsPayGradeByCode(GemsPayGrade gemsPayGrade);

	/*
	 * End of Gems Paygrade Methods
	 */

	/*
	 * Gems Shift Hours Methods
	 */

	public int getGemsWorkShiftMasterFilterCount(GemsWorkShiftMaster gemsWorkShiftMaster);

	public List getGemsWorkShiftMasterList(int start, int limit, GemsWorkShiftMaster gemsWorkShiftMaster);

	public void saveGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster);

	public void removeGemsWorkShiftMaster(GemsWorkShiftMaster gemsWorkShiftMaster);

	public GemsWorkShiftMaster getGemsWorkShiftMaster(Integer Id);

	public GemsWorkShiftMaster getGemsWorkShiftMasterByCode(GemsWorkShiftMaster gemsWorkShiftMaster);

	/*
	 * End of Gems Shift Hours Methods
	 */

	/*
	 * Gems Alert Notification Master Methods
	 */

	public int getGemsAlertNotificationMasterFilterCount(GemsAlertNotificationMaster gemsAlertNotificationMaster);

	public List getGemsAlertNotificationMasterList(int start, int limit,
			GemsAlertNotificationMaster gemsAlertNotificationMaster);

	public void saveGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster);

	public void removeGemsAlertNotificationMaster(GemsAlertNotificationMaster gemsAlertNotificationMaster);

	public GemsAlertNotificationMaster getGemsAlertNotificationMaster(Integer Id);

	public GemsAlertNotificationMaster getGemsAlertNotificationMasterByCode(
			GemsAlertNotificationMaster gemsAlertNotificationMaster);

	/*
	 * End of Gems Alert Notification Methods
	 */

	/*
	 * Gems Notification Methods
	 */

	public int getGemsNotificationFilterCount(GemsNotification gemsNotification);

	public List getGemsNotificationList(int start, int limit, GemsNotification gemsNotification);

	public void saveGemsNotification(GemsNotification gemsNotification);

	public void removeGemsNotification(GemsNotification gemsNotification);

	public GemsNotification getGemsNotification(Integer Id);

	/*
	 * End of Gems Notification Methods
	 */

	/*
	 * Gems Notification Assignment Methods
	 */

	public int getGemsNotificationAssignmentFilterCount(GemsNotificationAssignment gemsNotificationAssignment);

	public List getGemsNotificationAssignmentList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment);

	public void saveGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment);

	public void removeGemsNotificationAssignment(GemsNotificationAssignment gemsNotificationAssignment);

	public GemsNotificationAssignment getGemsNotificationAssignment(Integer Id);

	public int getGemsNotificationAssignmentByDateRangeFilterCount(
			GemsNotificationAssignment gemsNotificationAssignment);

	public List getGemsNotificationAssignmentByDateRangeList(int start, int limit,
			GemsNotificationAssignment gemsNotificationAssignment);

	/*
	 * End of Gems Notification Assignment Methods
	 */

	/*
	 * Gems Role Transaction Approval Methods
	 */

	public GemsRoleTransactionApproval getGemsRoleTransactionApprovalByRoleTransaction(
			GemsRoleTransactionApproval gemsRoleTransactionApproval);

	/*
	 * End of Gems Role Transaction Approval Methods
	 */
	/*
	 * Gems Role Transaction Approval Methods
	 */

	public GemsTransactionApprovalMaster getGemsTransactionApprovalMasterByCode(
			GemsTransactionApprovalMaster gemsTransactionApprovalMaster);

	/*
	 * End of Gems Role Transaction Approval Methods
	 */

	public GemsUserToken getGemsUserByToken(GemsUserToken userToken);

	public void removeGemsUserToken(GemsUserToken gemsUserToken);

	public String createEncodedToken(Integer userId);

	public boolean validateToken(String tokenFromRequest, String loggedInUserId);
}
