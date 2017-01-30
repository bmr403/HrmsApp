package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the gems_work_shift_master database table.
 * 
 */
@Entity
@Table(name = "gems_work_shift_master")
@Lazy(false)
public class GemsWorkShiftMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_work_shift_master_id")
	private int gemsWorkShiftMasterId;

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

	@Column(name = "work_shift_code")
	private String workShiftCode;

	@Column(name = "work_shift_description")
	private String workShiftDescription;

	@Column(name = "SHIFT_HOURS")
	private double shiftHours;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsWorkShiftMaster() {
	}

	public int getGemsWorkShiftMasterId() {
		return this.gemsWorkShiftMasterId;
	}

	public void setGemsWorkShiftMasterId(int gemsWorkShiftMasterId) {
		this.gemsWorkShiftMasterId = gemsWorkShiftMasterId;
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

	public String getWorkShiftCode() {
		return this.workShiftCode;
	}

	public void setWorkShiftCode(String workShiftCode) {
		this.workShiftCode = workShiftCode;
	}

	public String getWorkShiftDescription() {
		return this.workShiftDescription;
	}

	public void setWorkShiftDescription(String workShiftDescription) {
		this.workShiftDescription = workShiftDescription;
	}

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public double getShiftHours() {
		return shiftHours;
	}

	public void setShiftHours(double shiftHours) {
		this.shiftHours = shiftHours;
	}

}