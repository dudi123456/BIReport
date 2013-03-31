package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IFileDao;
import com.ailk.bi.marketing.entity.FileInfo;
import com.ailk.bi.marketing.service.IFileService;

@Service("fileService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class FileServiceImpl implements IFileService {
	@Resource
	private IFileDao fileDao ;
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(FileInfo entity) {
		return fileDao.save(entity);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		return fileDao.delect(ids);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public FileInfo getById(int id) {
		return fileDao.find(id);
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<FileInfo> getAll(FileInfo entity, int count) {
		List<FileInfo> list =  fileDao.getAll(entity, count);
		return list ;
	}
	public List<FileInfo> getAllByRelastionshipID(int id) {
		return fileDao.getAllByRelastionshipID(id);
	}
}
