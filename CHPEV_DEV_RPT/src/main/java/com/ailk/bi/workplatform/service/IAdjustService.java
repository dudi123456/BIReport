package com.ailk.bi.workplatform.service;
import java.util.List;
import com.ailk.bi.workplatform.entity.OrderAdjustInfo;
/**
 *【业务层接口】
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IAdjustService {
	public List <OrderAdjustInfo> getAll(OrderAdjustInfo entity,int count);
	public boolean save(OrderAdjustInfo entity);
}
