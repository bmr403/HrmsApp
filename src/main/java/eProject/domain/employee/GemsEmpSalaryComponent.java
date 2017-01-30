package eProject.domain.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.master.GemsCurrencyMaster;
import eProject.domain.master.GemsPayGrade;

/**
 * The persistent class for the gems_emp_salary_component database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_emp_salary_component")
public class GemsEmpSalaryComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_emp_salary_component_id")
	private int gemsEmpSalaryComponentId;

	@Column(name = "salary_component")
	private String salaryComponent;

	@Column(name = "pay_frequency")
	private String payFrequency;

	@Column(name = "salary_component_comment")
	private String salaryComponentComment;

	@Column(name = "salary_amount")
	private BigDecimal salaryAmount;

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

	// bi-directional many-to-one association to GemsEmployeeMaster
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_emp_master_id")
	private GemsEmployeeMaster gemsEmployeeMaster;

	// bi-directional many-to-one association to GemsPayGrade
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pay_grade_id")
	private GemsPayGrade gemsPayGrade;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "currency_id")
	private GemsCurrencyMaster gemsCurrencyMaster;

	public GemsEmpSalaryComponent() {
	}

	public int getGemsEmpSalaryComponentId() {
		return this.gemsEmpSalaryComponentId;
	}

	public void setGemsEmpSalaryComponentId(int gemsEmpSalaryComponentId) {
		this.gemsEmpSalaryComponentId = gemsEmpSalaryComponentId;
	}

	public String getSalaryComponent() {
		return this.salaryComponent;
	}

	public void setSalaryComponent(String salaryComponent) {
		this.salaryComponent = salaryComponent;
	}

	public GemsEmployeeMaster getGemsEmployeeMaster() {
		return this.gemsEmployeeMaster;
	}

	public void setGemsEmployeeMaster(GemsEmployeeMaster gemsEmployeeMaster) {
		this.gemsEmployeeMaster = gemsEmployeeMaster;
	}

	public GemsPayGrade getGemsPayGrade() {
		return this.gemsPayGrade;
	}

	public void setGemsPayGrade(GemsPayGrade gemsPayGrade) {
		this.gemsPayGrade = gemsPayGrade;
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

	public String getPayFrequency() {
		return payFrequency;
	}

	public void setPayFrequency(String payFrequency) {
		this.payFrequency = payFrequency;
	}

	public String getSalaryComponentComment() {
		return salaryComponentComment;
	}

	public void setSalaryComponentComment(String salaryComponentComment) {
		this.salaryComponentComment = salaryComponentComment;
	}

	public BigDecimal getSalaryAmount() {
		return salaryAmount;
	}

	public void setSalaryAmount(BigDecimal salaryAmount) {
		this.salaryAmount = salaryAmount;
	}

	public GemsCurrencyMaster getGemsCurrencyMaster() {
		return gemsCurrencyMaster;
	}

	public void setGemsCurrencyMaster(GemsCurrencyMaster gemsCurrencyMaster) {
		this.gemsCurrencyMaster = gemsCurrencyMaster;
	}

}