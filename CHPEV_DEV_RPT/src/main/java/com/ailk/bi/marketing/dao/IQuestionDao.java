package com.ailk.bi.marketing.dao;

import java.util.List;
import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.ailk.bi.marketing.entity.QuestionInfo;
/**
 *【DAO层接口】问题接口
 * @author  方慧
 * @version  [版本号, 2012-04-10]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IQuestionDao extends
GenericDAO<QuestionInfo, Integer> {
	public List<QuestionInfo> getAll(QuestionInfo entity,int count);
	public boolean delect(String ids);
}
