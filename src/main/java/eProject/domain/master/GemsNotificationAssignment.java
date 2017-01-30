/**
 * 
 */
package eProject.domain.master;

/**
 * @author SathyadevA
 *
 */

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.employee.GemsEmployeeMaster;

import java.util.Date;

/**
 * The persistent class for the gems_notification_assignment database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_notification_assignment")
public class GemsNotificationAssignment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_assignment_id")
	private int notificationAssignmentId;

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

	@Temporal(TemporalType.DATE)
	@Column(name = "notified_on")
	private Date notifiedOn;

	@Temporal(TemporalType.DATE)
	@Column(name = "notification_expiry")
	private Date notificationExpiry;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_notification_id")
	private GemsNotification gemsNotification;

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_employee_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	public GemsNotificationAssignment() {
	}

	public int getNotificationAssignmentId() {
		return notificationAssignmentId;
	}

	public void setNotificationAssignmentId(int notificationAssignmentId) {
		this.notificationAssignmentId = notificationAssignmentId;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public GemsNotification getGemsNotification() {
		return gemsNotification;
	}

	public void setGemsNotification(GemsNotification gemsNotification) {
		this.gemsNotification = gemsNotification;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public Date getNotifiedOn() {
		return notifiedOn;
	}

	public void setNotifiedOn(Date notifiedOn) {
		this.notifiedOn = notifiedOn;
	}

	public Date getNotificationExpiry() {
		return notificationExpiry;
	}

	public void setNotificationExpiry(Date notificationExpiry) {
		this.notificationExpiry = notificationExpiry;
	}

}