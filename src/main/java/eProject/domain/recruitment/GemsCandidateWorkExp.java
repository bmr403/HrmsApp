package eProject.domain.recruitment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The persistent class for the gems_candidate_work_exp database table.
 * 
 */
@Entity
@Table(name = "gems_candidate_work_exp")
public class GemsCandidateWorkExp implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_candidate_work_exp_id")
	private int gemsCandidateWorkExpId;

	@Column(name = "active_status")
	private int activeStatus;

	@Column(name = "created_by")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_by")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "updated_on")
	private Date updatedOn;

	@Column(name = "company_name")
	private String companyName;

	@Temporal(TemporalType.DATE)
	@Column(name = "from_date")
	private Date fromDate;

	@Column(name = "job_title")
	private String jobTitle;

	@Temporal(TemporalType.DATE)
	@Column(name = "to_date")
	private Date toDate;

	// bi-directional many-to-one association to GemsCandidateMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_candidate_master_id")
	private GemsCandidateMaster gemsCandidateMaster;

	public GemsCandidateWorkExp() {
	}

	public int getGemsCandidateWorkExpId() {
		return gemsCandidateWorkExpId;
	}

	public void setGemsCandidateWorkExpId(int gemsCandidateWorkExpId) {
		this.gemsCandidateWorkExpId = gemsCandidateWorkExpId;
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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public GemsCandidateMaster getGemsCandidateMaster() {
		return gemsCandidateMaster;
	}

	public void setGemsCandidateMaster(GemsCandidateMaster gemsCandidateMaster) {
		this.gemsCandidateMaster = gemsCandidateMaster;
	}

}