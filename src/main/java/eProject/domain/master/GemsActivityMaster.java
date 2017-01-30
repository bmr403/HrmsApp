package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import org.springframework.context.annotation.Lazy;

/**
 * The persistent class for the gems_activity_master database table.
 * 
 */
@Entity
@Table(name = "gems_activity_master")
@Lazy(false)
public class GemsActivityMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_ACTIVITY_MASTER_ID")
	private int gemsActivityMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "ADD_PERMISSION")
	private int addPermission;

	@Column(name = "APPROVE_PERMISSION")
	private int approvePermission;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "DELETE_PERMISSION")
	private int deletePermission;

	@Column(name = "EDIT_PERMISSION")
	private int editPermission;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsRoleMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ROLE_MASTER_ID")
	private GemsRoleMaster gemsRoleMaster;

	// bi-directional many-to-one association to GemsComponentMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_COMPONENT_MASTER_ID")
	private GemsComponentMaster gemsComponentMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsActivityMaster() {

	}

	public int getGemsActivityMasterId() {
		return this.gemsActivityMasterId;
	}

	public void setGemsActivityMasterId(int gemsActivityMasterId) {
		this.gemsActivityMasterId = gemsActivityMasterId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getAddPermission() {
		return this.addPermission;
	}

	public void setAddPermission(int addPermission) {
		this.addPermission = addPermission;
	}

	public int getApprovePermission() {
		return this.approvePermission;
	}

	public void setApprovePermission(int approvePermission) {
		this.approvePermission = approvePermission;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return this.createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getDeletePermission() {
		return this.deletePermission;
	}

	public void setDeletePermission(int deletePermission) {
		this.deletePermission = deletePermission;
	}

	public int getEditPermission() {
		return this.editPermission;
	}

	public void setEditPermission(int editPermission) {
		this.editPermission = editPermission;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return this.updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GemsRoleMaster getGemsRoleMaster() {
		return this.gemsRoleMaster;
	}

	public void setGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		this.gemsRoleMaster = gemsRoleMaster;
	}

	public GemsComponentMaster getGemsComponentMaster() {
		return this.gemsComponentMaster;
	}

	public void setGemsComponentMaster(GemsComponentMaster gemsComponentMaster) {
		this.gemsComponentMaster = gemsComponentMaster;
	}

	/**
	 * @return the gemsOrganisation
	 */
	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	/**
	 * @param gemsOrganisation
	 *            the gemsOrganisation to set
	 */
	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

}