package eProject.service.project;

import java.util.List;

import eProject.domain.customer.GemsCustomerContact;
import eProject.domain.customer.GemsCustomerMaster;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;

public interface ProjectService {

	/*
	 * Gems Project Type Methods
	 */

	public int getGemsProjectTypeMasterFilterCount(GemsProjectTypeMaster gemsProjectTypeMaster);

	public List getGemsProjectTypeMasterList(int start, int limit, GemsProjectTypeMaster gemsProjectTypeMaster);

	public void saveGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster);

	public void removeGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster);

	public GemsProjectTypeMaster getGemsProjectTypeMaster(Integer Id);

	public GemsProjectTypeMaster getGemsProjectTypeMasterByCode(GemsProjectTypeMaster gemsProjectTypeMaster);

	/*
	 * End of Gems Project Type Master Methods
	 */

	/*
	 * Gems Project Master Methods
	 */

	public int getGemsProjectMasterFilterCount(GemsProjectMaster gemsProjectMaster);

	public List getGemsProjectMasterList(int start, int limit, GemsProjectMaster gemsProjectMaster);

	public void saveGemsProjectMaster(GemsProjectMaster gemsProjectMaster);
	
	public void saveBatchProjects(List projectList);

	public void removeGemsProjectMaster(GemsProjectMaster gemsProjectMaster);

	public GemsProjectMaster getGemsProjectMaster(Integer Id);

	public GemsProjectMaster getGemsProjectMasterByCode(GemsProjectMaster gemsProjectMaster);

	public List getAllGemsProjectMasterList(GemsProjectMaster gemsProjectMaster);
	/*
	 * End of Gems Project Master Methods
	 */

	/*
	 * Gems Project Resource Methods
	 */

	public int getGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster);

	public List getGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster);

	public void saveGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster);

	public void saveBatchProjectResources(List gemsProjectResourceMasterList);
	
	public void removeGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster);

	public GemsProjectResourceMaster getGemsProjectResourceMaster(Integer Id);

	public int getMyGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster);

	public List getMyGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster);

	public GemsProjectResourceMaster getGemsProjectResourceMasterByResourceAndProject(
			GemsProjectResourceMaster gemsProjectResourceMaster);

	public List getAllGemsProjectResourceMasterList(GemsProjectResourceMaster gemsProjectResourceMaster);
	/*
	 * End of Project Resource Methods
	 */
}
