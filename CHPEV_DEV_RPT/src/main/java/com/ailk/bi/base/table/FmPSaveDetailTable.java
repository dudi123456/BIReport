package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class FmPSaveDetailTable extends JBTableBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6035143941779566660L;

	/**
	 * 登陆用户
	 */
	public String sys_user_id = "";

	/**
	 * 收藏夹标识
	 */
	public String pack_id = "";

	/**
	 * 品牌标识
	 */
	public String brand_code = "";

	/**
	 * 品牌描述
	 */
	public String brandcode_desc = "";

	/**
	 * 子品牌类型
	 */
	public String sub_brand_knd = "";

	/**
	 * 子品牌描述
	 */
	public String subbrandknd_desc = "";

	/**
	 * 产品类型
	 */
	public String product_type = "";

	/**
	 * 产品类型描述
	 */
	public String producttype_desc = "";

	/**
	 * 产品
	 */
	public String product = "";

	/**
	 * 产品描述
	 */
	public String product_name = "";

	/**
	 * 创建日期
	 */
	public String create_date = "";

	/**
	 * 修改日期
	 */
	public String update_date = "";

	/**
	 * 是否有效
	 */
	public String valid_falg = "";

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("sys_user_id = ").append(this.sys_user_id).append("\n");

		sb.append("pack_id = ").append(this.pack_id).append("\n");

		sb.append("brand_code = ").append(this.brand_code).append("\n");

		sb.append("brandcode_desc = ").append(this.brandcode_desc).append("\n");

		sb.append("sub_brand_knd = ").append(this.sub_brand_knd).append("\n");

		sb.append("subbrandknd_desc = ").append(this.subbrandknd_desc)
				.append("\n");

		sb.append("product = ").append(this.product).append("\n");

		sb.append("product_desc = ").append(this.product_name).append("\n");

		sb.append("create_date = ").append(this.create_date).append("\n");

		sb.append("update_desc = ").append(this.update_date).append("\n");

		sb.append("valid_falg = ").append(this.valid_falg).append("\n");
		return sb.toString();
	}

}
