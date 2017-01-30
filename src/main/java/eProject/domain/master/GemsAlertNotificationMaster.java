package eProject.domain.master;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * The persistent class for the alert_notification_master database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "alert_notification_master")
public class GemsAlertNotificationMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alert_configuration_id")
	private int alertConfigurationId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "transaction_code")
	private String transactionCode;

	@Column(name = "transaction_description")
	private String transactionDescription;

	@Column(name = "email_alert")
	private int emailAlert;

	@Column(name = "notification_alert")
	private int notificationAlert;

	@Column(name = "alert_start")
	private int alertStart;

	@Column(name = "alert_end")
	private int alertEnd;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsAlertNotificationMaster() {
	}

	public int getAlertConfigurationId() {
		return alertConfigurationId;
	}

	public void setAlertConfigurationId(int alertConfigurationId) {
		this.alertConfigurationId = alertConfigurationId;
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

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public int getEmailAlert() {
		return emailAlert;
	}

	public void setEmailAlert(int emailAlert) {
		this.emailAlert = emailAlert;
	}

	public int getNotificationAlert() {
		return notificationAlert;
	}

	public void setNotificationAlert(int notificationAlert) {
		this.notificationAlert = notificationAlert;
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

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public int getAlertStart() {
		return alertStart;
	}

	public void setAlertStart(int alertStart) {
		this.alertStart = alertStart;
	}

	public int getAlertEnd() {
		return alertEnd;
	}

	public void setAlertEnd(int alertEnd) {
		this.alertEnd = alertEnd;
	}

}