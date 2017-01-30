package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_user_master database table.
 * 
 */
@Entity
@Table(name = "gems_user_master")
@Lazy(false)
public class GemsUserMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_USER_MASTER_ID")
	private int gemsUserMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "USER_PASSWORD")
	private String userPassword;

	@Column(name = "USER_DECRYPT_PASSWORD")
	private String userDecryptPassword;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	// bi-directional many-to-one association to GemsRoleMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ROLE_MASTER_ID")
	private GemsRoleMaster gemsRoleMaster;

	@Column(name = "COLOR_SKIN")
	protected String colorSkin;

	@Column(name = "PROFILE_IMAGE_DATA")
	@Lob
	protected byte[] profileImgData;

	public GemsUserMaster() {

	}

	public int getGemsUserMasterId() {
		return this.gemsUserMasterId;
	}

	public void setGemsUserMasterId(int gemsUserMasterId) {
		this.gemsUserMasterId = gemsUserMasterId;
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

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public GemsRoleMaster getGemsRoleMaster() {
		return this.gemsRoleMaster;
	}

	public void setGemsRoleMaster(GemsRoleMaster gemsRoleMaster) {
		this.gemsRoleMaster = gemsRoleMaster;
	}

	public byte[] getProfileImgData() {
		return profileImgData;
	}

	public void setProfileImgData(byte[] profileImgData) {
		this.profileImgData = profileImgData;
	}

	public String getColorSkin() {
		return colorSkin;
	}

	public void setColorSkin(String colorSkin) {
		this.colorSkin = colorSkin;
	}

	public String getUserDecryptPassword() {
		return userDecryptPassword;
	}

	public void setUserDecryptPassword(String userDecryptPassword) {
		this.userDecryptPassword = userDecryptPassword;
	}

}