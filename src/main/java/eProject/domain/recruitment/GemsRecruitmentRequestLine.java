package eProject.domain.recruitment;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.Date;

/**
 * The persistent class for the gems_recruitment_request_line database table.
 * 
 */
@Entity
@Table(name = "gems_recruitment_request_line")
public class GemsRecruitmentRequestLine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_recruitment_request_line_id")
	private int gemsRecruitmentRequestLineId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "approved_by")
	private int approvedBy;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "line_status")
	private String lineStatus;

	@Column(name = "last_updated_by")
	private String lastUpdatedBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsRecruitmentRequest
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_recruitment_request_id")
	private GemsRecruitmentRequest gemsRecruitmentRequest;

	public GemsRecruitmentRequestLine() {
	}

	public int getGemsRecruitmentRequestLineId() {
		return gemsRecruitmentRequestLineId;
	}

	public void setGemsRecruitmentRequestLineId(int gemsRecruitmentRequestLineId) {
		this.gemsRecruitmentRequestLineId = gemsRecruitmentRequestLineId;
	}

	public int getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
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

	public String getLineStatus() {
		return lineStatus;
	}

	public void setLineStatus(String lineStatus) {
		this.lineStatus = lineStatus;
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

	public GemsRecruitmentRequest getGemsRecruitmentRequest() {
		return gemsRecruitmentRequest;
	}

	public void setGemsRecruitmentRequest(GemsRecruitmentRequest gemsRecruitmentRequest) {
		this.gemsRecruitmentRequest = gemsRecruitmentRequest;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

}