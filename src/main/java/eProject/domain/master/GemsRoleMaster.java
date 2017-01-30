package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_role_master database table.
 * 
 */
@Entity
@Table(name = "gems_role_master")
@Lazy(false)
public class GemsRoleMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_ROLE_MASTER_ID")
	private int gemsRoleMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "ROLE_NAME")
	private String roleName;

	@Column(name = "ROLE_CODE")
	private String roleCode;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsRoleMaster() {

	}

	public int getGemsRoleMasterId() {
		return this.gemsRoleMasterId;
	}

	public void setGemsRoleMasterId(int gemsRoleMasterId) {
		this.gemsRoleMasterId = gemsRoleMasterId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
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

	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode
	 *            the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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