package eProject.domain.recruitment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gems_candidate_status_master")
public class GemsCandidateStatusMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_candidate_status_master_id")
	private int gemsCandidateStatusMasterId;

	@Column(name = "status_description")
	private String statusDescription;

	public int getGemsCandidateStatusMasterId() {
		return gemsCandidateStatusMasterId;
	}

	public void setGemsCandidateStatusMasterId(int gemsCandidateStatusMasterId) {
		this.gemsCandidateStatusMasterId = gemsCandidateStatusMasterId;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

}
