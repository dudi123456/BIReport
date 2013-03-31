/**
 * 【业务层接口】套餐管理
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
package com.ailk.bi.marketing.service;

import java.util.List;

import com.ailk.bi.marketing.entity.PackageInfo;
import com.ailk.bi.marketing.entity.PackageParentInfo;

public interface IPackageService {
	public boolean save(PackageInfo entity);
	public boolean delete(String ids);
	public PackageInfo getById(int id);
	public PackageParentInfo getParentById(int id);
	/**
	 * 通过条件查询，返回count条记录
	 * @param  entity 当entity为null时，没有条件
	 * @param  count 返回指定的记录数，当count=0时，返回所有的记录
	 */
	public List<PackageInfo> getAll(PackageInfo entity,int count);
	public List<PackageParentInfo> getParentAll(PackageInfo entity,int count);
	public List<PackageInfo> getAllSonPackage(String ids);
}
