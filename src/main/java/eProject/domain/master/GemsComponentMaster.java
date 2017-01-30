package eProject.domain.master;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.context.annotation.Lazy;

/**
 * The persistent class for the gems_component_master database table.
 * 
 */
@Entity
@Table(name = "gems_component_master")
@Lazy(false)
public class GemsComponentMaster implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "GEMS_COMPONENT_MASTER_ID")
	private int gemsComponentMasterId;

	@Column(name = "ACTIVE_STATUS")
	private int activeStatus;

	@Column(name = "COMPONENT_DESCRIPTION")
	private String componentDescription;

	@Column(name = "COMPONENT_NAME")
	private String componentName;

	@Column(name = "COMPONENT_URL")
	private String componentUrl;

	@Column(name = "CREATED_BY")
	private int createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_ON")
	private Date createdOn;

	@Column(name = "PARENT_COMPONENT_ID")
	private int parentComponentId;

	@Column(name = "UPDATED_BY")
	private int updatedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "UPDATED_ON")
	private Date updatedOn;

	// bi-directional many-to-one association to GemsOrganisation
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "GEMS_ORG_ID")
	private GemsOrganisation gemsOrganisation;

	public GemsComponentMaster() {

	}

	public int getGemsComponentMasterId() {
		return this.gemsComponentMasterId;
	}

	public void setGemsComponentMasterId(int gemsComponentMasterId) {
		this.gemsComponentMasterId = gemsComponentMasterId;
	}

	public int getActiveStatus() {
		return this.activeStatus;
	}

	public void setActiveStatus(int activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getComponentDescription() {
		return this.componentDescription;
	}

	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}

	public String getComponentName() {
		return this.componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
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

	public int getParentComponentId() {
		return this.parentComponentId;
	}

	public void setParentComponentId(int parentComponentId) {
		this.parentComponentId = parentComponentId;
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

	/**
	 * @return the gemsOrganisation
	 */
	public GemsOrganisation getGemsOrganisation() {
		return gemsOrganisation;
	}

	/**
	 * @param gemsOrganisation
	 *            the gemsOrganisation to set
	 */
	public void setGemsOrganisation(GemsOrganisation gemsOrganisation) {
		this.gemsOrganisation = gemsOrganisation;
	}

	public static Comparator<GemsComponentMaster> ComponentMasterIdCompareObject = new Comparator<GemsComponentMaster>() {

		public int compare(GemsComponentMaster s1, GemsComponentMaster s2) {

			int Id1 = s1.getGemsComponentMasterId();
			int Id2 = s2.getGemsComponentMasterId();

			/* For ascending order */
			return Id1 - Id2;

			/* For descending order */
			// rollno2-rollno1;
		}
	};

	public String getComponentUrl() {
		return componentUrl;
	}

	public void setComponentUrl(String componentUrl) {
		this.componentUrl = componentUrl;
	}

}