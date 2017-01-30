package eProject.domain.master;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the gems_pay_grade database table.
 * 
 */
@Entity
@Table(name = "gems_pay_grade")
@Lazy(false)
public class GemsPayGrade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GEMS_PAY_GRADE_ID")
	private int gemsPayGradeId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "PAY_GRADE_CODE")
	private String payGradeCode;

	@Column(name = "PAY_GRADE_DESCRIPTION")
	private String payGradeDescription;

	@Column(name = "MAX_SALARY")
	private BigDecimal maxSalary;

	@Column(name = "MIN_SALARY")
	private BigDecimal minSalary;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsPayGrade() {
	}

	public int getGemsPayGradeId() {
		return this.gemsPayGradeId;
	}

	public void setGemsPayGradeId(int gemsPayGradeId) {
		this.gemsPayGradeId = gemsPayGradeId;
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

	public String getPayGradeCode() {
		return this.payGradeCode;
	}

	public void setPayGradeCode(String payGradeCode) {
		this.payGradeCode = payGradeCode;
	}

	public String getPayGradeDescription() {
		return this.payGradeDescription;
	}

	public void setPayGradeDescription(String payGradeDescription) {
		this.payGradeDescription = payGradeDescription;
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

	public GemsOrganisation getGemsOrganisation() {
		return this.gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public BigDecimal getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(BigDecimal maxSalary) {
		this.maxSalary = maxSalary;
	}

	public BigDecimal getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(BigDecimal minSalary) {
		this.minSalary = minSalary;
	}

}