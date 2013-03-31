package com.ailk.bi.marketing.service.impl;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.ailk.bi.marketing.dao.IPackageDao;
import com.ailk.bi.marketing.dao.IPackageParentDao;
import com.ailk.bi.marketing.entity.PackageInfo;
import com.ailk.bi.marketing.entity.PackageParentInfo;
import com.ailk.bi.marketing.service.IPackageService;

@Service("packageService")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PackageServiceImpl implements IPackageService {
	@Resource
	private IPackageDao packageDao ;

	@Resource
	private IPackageParentDao parentPackageDao ;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean save(PackageInfo entity) {
		// TODO Auto-generated method stub
		return packageDao.save(entity);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean delete(String ids) {
		// TODO Auto-generated method stub
		return packageDao.delect(ids);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PackageInfo getById(int id) {
		// TODO Auto-generated method stub
		return packageDao.find(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public PackageParentInfo getParentById(int id) {
		// TODO Auto-generated method stub
		return parentPackageDao.find(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PackageInfo> getAll(PackageInfo entity, int count) {
		List<PackageInfo> list =  packageDao.getAll(entity, count);
		return list ;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PackageParentInfo> getParentAll(PackageInfo entity, int count) {
		List<PackageParentInfo> list =  parentPackageDao.getParentAll(entity, count);
		return list ;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public List<PackageInfo> getAllSonPackage(String ids) {
		List<PackageInfo> list =  packageDao.getAllSonPackage(ids);
		return list ;
	}

}
