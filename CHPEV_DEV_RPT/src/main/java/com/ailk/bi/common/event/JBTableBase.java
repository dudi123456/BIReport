package com.ailk.bi.common.event;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.XMLUtils;
import com.ailk.bi.common.event.utils.Util;
import com.ailk.bi.common.sysconfig.GetSystemConfig;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: 一个表的JavaBean底层类的定义
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
public abstract class JBTableBase implements JBTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -694650700329368587L;

	protected int extern_oper_flag = TableKeys.REC_NEW;

	private String ejbActionClassName = null;

	/**
	 * 实现实体的复制
	 * 
	 * @return
	 */
	public Object clone() {
		JBTableBase o = null;
		try {
			o = (JBTableBase) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 获取该Table所对应的EJBAction的处理类
	 * 
	 * @return When set to null an EJB command is not processed.
	 */
	public String getEJBActionClassName() {

		return ejbActionClassName;
	}

	/**
	 * 设置EJBAction类的类名
	 * 
	 * @param ejbActionClassName
	 */
	public void setEJBActionClassName(String ejbActionClassName) {
		this.ejbActionClassName = ejbActionClassName;
	}

	/**
	 * 返回这个table的名称
	 */
	public String getTableName() {
		return getClass().getName();
	}

	/**
	 * 将本JavaBean转化成XML格式，用于传输和显示
	 * 
	 * @return
	 */
	public String toXml(boolean simp) throws AppException {
		String ret = null;

		Document doc = new DocumentImpl();
		Element root = Util.genAJavaBean(doc, this, this.getTableName(), simp);
		doc.appendChild(root);
		ret = XMLUtils.doc2String(doc, Util.DEFAULT_ENCODING);

		return ret;

	}

	/**
	 * 参见 JBTable#toXml()
	 * 
	 * @param simp
	 * @return
	 * @throws AppException
	 */
	public String toXml() throws AppException {
		return toXml(true);
	}

	/**
	 * 获得该JB在session存放的session属性名称
	 * 
	 * @return
	 */
	public String getSessoinName() {
		return "";
	}

	public int getModuleID() {
		int iRet = GetSystemConfig.getTagFromClass(getClass().getName());
		if (iRet == -1)
			System.err.println("NO FATAL ERROR: 类" + getClass().getName()
					+ "的tag映射值没有定义!");

		return iRet;
	}

	/**
	 * 获取记录的修改标记
	 * 
	 * @return
	 */
	public int getRecFlag() {
		return extern_oper_flag;
	}

	/**
	 * 设置记录的修改标记
	 * 
	 * @param extern_oper_flag
	 *            取值应从TableKeys类中取值
	 */
	public void setRecFlag(int extern_oper_flag) {
		this.extern_oper_flag = extern_oper_flag;
	}
}