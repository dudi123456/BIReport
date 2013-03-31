package com.ailk.bi.marketing.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IProjectDao;
import com.ailk.bi.marketing.entity.ProjectInfo;
import com.ailk.bi.marketing.service.IProjectService;

@Service("projectService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ProjectServiceImpl implements IProjectService {
	@Resource
	private IProjectDao projectDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(ProjectInfo entity) {
		return projectDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return projectDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ProjectInfo getById(int id) {
		return projectDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<ProjectInfo> getAll(ProjectInfo entity, int count) {
		List<ProjectInfo> list =  projectDao.getAll(entity, count);
		return list ;
	}
	public int getByTacticId(int tacticId) {
		return projectDao.getByTacticId(tacticId);
	}
	public int getCountByChannleId(int channleId) {
		return projectDao.getCountByChannleId(channleId);
	}
	public int getCountForPlat(String longUser) {
		return projectDao.getCountForPlat(longUser);
	}
}
