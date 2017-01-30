package eProject.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eProject.dao.project.ProjectDao;
import eProject.domain.project.GemsProjectMaster;
import eProject.domain.project.GemsProjectResourceMaster;
import eProject.domain.project.GemsProjectTypeMaster;

@Service("projectService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDao projectDao;

	public ProjectServiceImpl() {
	}

	/*
	 * Gems Project Type Methods
	 */

	public int getGemsProjectTypeMasterFilterCount(GemsProjectTypeMaster gemsProjectTypeMaster) {
		return projectDao.getGemsProjectTypeMasterFilterCount(gemsProjectTypeMaster);
	}

	public List getGemsProjectTypeMasterList(int start, int limit, GemsProjectTypeMaster gemsProjectTypeMaster) {
		return projectDao.getGemsProjectTypeMasterList(start, limit, gemsProjectTypeMaster);
	}

	public void saveGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster) {
		projectDao.saveGemsProjectTypeMaster(gemsProjectTypeMaster);
	}

	public void removeGemsProjectTypeMaster(GemsProjectTypeMaster gemsProjectTypeMaster) {
		projectDao.removeGemsProjectTypeMaster(gemsProjectTypeMaster);
	}

	public GemsProjectTypeMaster getGemsProjectTypeMaster(Integer Id) {
		return projectDao.getGemsProjectTypeMaster(Id);
	}

	public GemsProjectTypeMaster getGemsProjectTypeMasterByCode(GemsProjectTypeMaster gemsProjectTypeMaster) {
		return projectDao.getGemsProjectTypeMasterByCode(gemsProjectTypeMaster);
	}

	/*
	 * End of Gems Project Type Master Methods
	 */

	/*
	 * Gems Project Master Methods
	 */

	public int getGemsProjectMasterFilterCount(GemsProjectMaster gemsProjectMaster) {
		return projectDao.getGemsProjectMasterFilterCount(gemsProjectMaster);
	}

	public List getGemsProjectMasterList(int start, int limit, GemsProjectMaster gemsProjectMaster) {
		return projectDao.getGemsProjectMasterList(start, limit, gemsProjectMaster);
	}

	public void saveGemsProjectMaster(GemsProjectMaster gemsProjectMaster) {
		projectDao.saveGemsProjectMaster(gemsProjectMaster);
	}
	
	public void saveBatchProjects(List projectList) {
		projectDao.saveBatchProjects(projectList);
	}

	public void removeGemsProjectMaster(GemsProjectMaster gemsProjectMaster) {
		projectDao.removeGemsProjectMaster(gemsProjectMaster);
	}

	public GemsProjectMaster getGemsProjectMaster(Integer Id) {
		return projectDao.getGemsProjectMaster(Id);
	}

	public GemsProjectMaster getGemsProjectMasterByCode(GemsProjectMaster gemsProjectMaster) {
		return projectDao.getGemsProjectMasterByCode(gemsProjectMaster);
	}

	public List getAllGemsProjectMasterList(GemsProjectMaster gemsProjectMaster) {
		return projectDao.getAllGemsProjectMasterList(gemsProjectMaster);
	}

	/*
	 * End of Gems Project Master Methods
	 */

	/*
	 * Gems Project Resource Methods
	 */

	public int getGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getGemsProjectResourceMasterFilterCount(gemsProjectResourceMaster);
	}

	public List getGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getGemsProjectResourceMasterList(start, limit, gemsProjectResourceMaster);
	}

	public void saveGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster) {
		projectDao.saveGemsProjectResourceMaster(gemsProjectResourceMaster);
	}
	
	public void saveBatchProjectResources(List gemsProjectResourceMasterList)
	{
		projectDao.saveBatchProjectResources(gemsProjectResourceMasterList);
	}
	
	public void removeGemsProjectResourceMaster(GemsProjectResourceMaster gemsProjectResourceMaster) {
		projectDao.removeGemsProjectResourceMaster(gemsProjectResourceMaster);
	}

	public GemsProjectResourceMaster getGemsProjectResourceMaster(Integer Id) {
		return projectDao.getGemsProjectResourceMaster(Id);
	}

	public int getMyGemsProjectResourceMasterFilterCount(GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getMyGemsProjectResourceMasterFilterCount(gemsProjectResourceMaster);
	}

	public List getMyGemsProjectResourceMasterList(int start, int limit,
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getMyGemsProjectResourceMasterList(start, limit, gemsProjectResourceMaster);
	}

	public GemsProjectResourceMaster getGemsProjectResourceMasterByResourceAndProject(
			GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getGemsProjectResourceMasterByResourceAndProject(gemsProjectResourceMaster);
	}

	public List getAllGemsProjectResourceMasterList(GemsProjectResourceMaster gemsProjectResourceMaster) {
		return projectDao.getAllGemsProjectResourceMasterList(gemsProjectResourceMaster);
	}

	/*
	 * End of Project Resource Methods
	 */
}
