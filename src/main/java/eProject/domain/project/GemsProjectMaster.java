package eProject.domain.project;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.springframework.context.annotation.Lazy;

import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.master.GemsBusinessUnit;
import eProject.domain.master.GemsOrganisation;

import java.util.Date;

/**
 * The persistent class for the gems_department database table.
 * 
 */
@Entity
@Lazy(false)
@Table(name = "gems_project_master")
public class GemsProjectMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gems_project_master_id")
	private int gemsProjectMasterId;

	@Column(name = "ACTIVE_STATUS")
	private Integer activeStatus;

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

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "project_description")
	private String projectDescription;

	@Column(name = "project_status")
	private String projectStatus;

	@Column(name = "billing_type")
	private String billingType;

	@Temporal(TemporalType.DATE)
	@Column(name = "project_start_date")
	private Date projectStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "project_end_date")
	private Date projectEndDate;

	@Column(name = "project_cost")
	private BigDecimal projectCost;

	// bi-directional many-to-one association to GemsBusinessUnit
	/*
	 * @ManyToOne(fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn(name="gems_business_unit_id") private GemsBusinessUnit
	 * gemsBusinessUnit;
	 */

	// bi-directional many-to-one association to Project Type Master
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_type_master_id")
	private GemsProjectTypeMaster projectTypeMaster;

	// bi-directional many-to-one association to Project Type Master
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "gems_customer_master_id")
	private GemsCustomerMaster gemsCustomerMaster;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsProjectMaster() {
	}

	public int getGemsProjectMasterId() {
		return gemsProjectMasterId;
	}

	public void setGemsProjectMasterId(int gemsProjectMasterId) {
		this.gemsProjectMasterId = gemsProjectMasterId;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
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

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public Date getProjectStartDate() {
		return projectStartDate;
	}

	public void setProjectStartDate(Date projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	public Date getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public BigDecimal getProjectCost() {
		return projectCost;
	}

	public void setProjectCost(BigDecimal projectCost) {
		this.projectCost = projectCost;
	}

	/*
	 * public GemsBusinessUnit getGemsBusinessUnit() { return gemsBusinessUnit;
	 * }
	 * 
	 * public void setGemsBusinessUnit(GemsBusinessUnit gemsBusinessUnit) {
	 * this.gemsBusinessUnit = gemsBusinessUnit; }
	 */

	public GemsProjectTypeMaster getProjectTypeMaster() {
		return projectTypeMaster;
	}

	public void setProjectTypeMaster(GemsProjectTypeMaster projectTypeMaster) {
		this.projectTypeMaster = projectTypeMaster;
	}

	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public GemsCustomerMaster getGemsCustomerMaster() {
		return gemsCustomerMaster;
	}

	public void setGemsCustomerMaster(GemsCustomerMaster gemsCustomerMaster) {
		this.gemsCustomerMaster = gemsCustomerMaster;
	}

}