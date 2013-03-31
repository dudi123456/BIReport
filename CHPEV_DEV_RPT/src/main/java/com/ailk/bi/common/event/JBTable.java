package com.ailk.bi.common.event;

import com.ailk.bi.common.app.AppException;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 一个表的JavaBean底层定义
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
public interface JBTable extends java.io.Serializable, java.lang.Cloneable {
	/**
	 * 系统内部使用，设置该Table所对应的EJB处理Action
	 * 
	 * @param ejbActionClassName
	 */
	public void setEJBActionClassName(String ejbActionClassName);

	/**
	 * 系统内部使用，获取该Table所对应的EJB处理Action
	 * 
	 * @return
	 */
	public String getEJBActionClassName();

	/**
	 * 获取本Table的名称
	 * 
	 * @return
	 */
	public String getTableName();

	/**
	 * 获得该JB在session存放的session属性名称
	 * 
	 * @return
	 */
	public String getSessoinName();

	/**
	 * 将本JavaBean转化成XML格式，用于传输和显示
	 * 
	 * @return
	 */
	public String toXml() throws AppException;

	/**
	 * 将本JavaBean转化成XML格式，用于传输和显示
	 * 
	 * @param simp
	 *            是否使用简化/复杂显示
	 * @return
	 */
	public String toXml(boolean simp) throws AppException;

	/**
	 * 获取记录的修改标记
	 * 
	 * @return
	 */
	public int getRecFlag();

	/**
	 * 设置记录的修改标记
	 * 
	 * @param extern_oper_flag
	 *            取值应从TableKeys类中取值
	 */
	public void setRecFlag(int extern_oper_flag);

}
