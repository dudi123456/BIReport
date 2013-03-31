package com.ailk.bi.common.ejb;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: CMPEB的底层定义类适配器，你的CMPEB可以继承它
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public abstract class CMPEntityBeanAdapter implements EntityBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5057407602447487432L;
	EntityContext entityContext;

	public void ejbLoad() {
	}

	public void ejbStore() {
	}

	public void ejbActivate() {
	}

	public void ejbPassivate() {
	}

	public void ejbRemove() throws RemoveException {
	}

	public void unsetEntityContext() {
		this.entityContext = null;
	}

	public void setEntityContext(EntityContext entityContext) {
		this.entityContext = entityContext;
	}

}