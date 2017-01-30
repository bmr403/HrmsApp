package eProject.domain.recruitment;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.employee.GemsEmployeeMaster;
import eProject.domain.master.GemsDepartment;
import eProject.domain.master.GemsOrganisation;

@Entity
@Table(name = "gems_manpower_request_interview")
public class GemsManpowerRequestInterview {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_manpower_request_interview_id")
	private int gemsMapowerRequestInterviewId;

	@Temporal(TemporalType.DATE)
	@Column(name = "interview_date")
	private Date interviewDate;

	@Column(name = "interview_time")
	private String interviewTime;

	@Column(name = "level_no")
	private Integer levelNo;

	@Column(name = "interviewer_remarks")
	private String interviewerRemarks;

	@Column(name = "interview_status")
	private String interviewStatus;

	@Column(name = "skill_rating")
	private String skillRating;

	@Column(name = "communication_rating")
	private String communicationRating;

	@Column(name = "team_player")
	private String teamPlayer;

	@Column(name = "other_rating")
	private String otherRating;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_manpower_request_id")
	private GemsManpowerRequest gemsManpowerRequest;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "interviewer_id")
	private GemsEmployeeMaster interviewer;

	public int getGemsMapowerRequestInterviewId() {
		return gemsMapowerRequestInterviewId;
	}

	public void setGemsMapowerRequestInterviewId(int gemsMapowerRequestInterviewId) {
		this.gemsMapowerRequestInterviewId = gemsMapowerRequestInterviewId;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(String interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getInterviewerRemarks() {
		return interviewerRemarks;
	}

	public void setInterviewerRemarks(String interviewerRemarks) {
		this.interviewerRemarks = interviewerRemarks;
	}

	public String getInterviewStatus() {
		return interviewStatus;
	}

	public void setInterviewStatus(String interviewStatus) {
		this.interviewStatus = interviewStatus;
	}

	public String getSkillRating() {
		return skillRating;
	}

	public void setSkillRating(String skillRating) {
		this.skillRating = skillRating;
	}

	public String getCommunicationRating() {
		return communicationRating;
	}

	public void setCommunicationRating(String communicationRating) {
		this.communicationRating = communicationRating;
	}

	public String getTeamPlayer() {
		return teamPlayer;
	}

	public void setTeamPlayer(String teamPlayer) {
		this.teamPlayer = teamPlayer;
	}

	public String getOtherRating() {
		return otherRating;
	}

	public void setOtherRating(String otherRating) {
		this.otherRating = otherRating;
	}

	public GemsManpowerRequest getGemsManpowerRequest() {
		return gemsManpowerRequest;
	}

	public void setGemsManpowerRequest(GemsManpowerRequest gemsManpowerRequest) {
		this.gemsManpowerRequest = gemsManpowerRequest;
	}

	public GemsEmployeeMaster getInterviewer() {
		return interviewer;
	}

	public void setInterviewer(GemsEmployeeMaster interviewer) {
		this.interviewer = interviewer;
	}

	public Integer getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}

}
