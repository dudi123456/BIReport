package com.ailk.bi.base.table;

import java.lang.reflect.Field;

import com.ailk.bi.common.app.Arith;
import com.ailk.bi.common.event.JBTableBase;

@SuppressWarnings({ "rawtypes" })
public class FmUniUserViewMTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 新增
	public String customer_name = "";//
	public String user_no = "";//
	public String corp_name = "";//
	public String device_number = "";//
	public String customer_id = "";// 客户证件编号
	public String viplevel_desc = "";// 大客户级别描述
	public String brandknd_desc = "";// 品牌类型
	public String develop_dept_desc = "";// 发展部门
	public String eval_area_desc = "";// 通话归属地描述
	public String customerstatus_desc = "";// 用户状态描述
	public String in_net_date = "";// 入网时间
	public String off_net_date = "";// 入网时间

	public String sexflag_desc = "";// 性 别
	public String customer_phone = "";// 住宅电话
	public String idtype_desc = "";// 证件类型
	public String customer_address = "";// 居住地址
	public String customer_post = "";// 通信邮编
	public String contact_addr = "";// 联系地址

	public String product_desc = "";// 产 品
	public String dinnerpkg_desc = "";// 服务套餐包
	public String producttype_desc = "";// 产品类型
	public String corpproduct_desc = "";// 主集团产品

	public String corpdinnerpkg_desc = "";// 主集团服务套餐包
	// public String beg_day ="";//协议起始时间
	// public String end_day ="";//联系地址
	// public String real_end_day ="";//联系地址

	// public String agree_fee ="";//联系地址
	// public String agree_mons ="";//联系地址
	// public String rent_times ="";//联系地址
	// public String min_consumed_fee ="";//联系地址
	public String saletype_desc = "";// 销售模式

	// public String avg_consumed_fee ="";//协议期内平均消费
	public String link_name = "";// 联系人
	public String link_phone = "";// 联 系 人 电 话
	public String link_email = "";// 联系人e-mail
	public String v_fst_lac_desc = "";// 联 系 人 电 话
	public String v_sec_lac_desc = "";// 联系人e-mail

	public String a_arr_acct_mon_num = "";// 欠费帐期个数
	public String r_late_fee = "";// 实收滞纳金
	public String present_cost = "";// 赠款成本
	public String adjust_cost = "";// 帐后调整成本
	public String detain_cost = "";// 维系成本
	public String score_cost = "";// 积分成本
	public String sale_cost = "";// 营销成本

	public String lastproduct_desc = "";// 上月月末产品
	public String lastproducttype_desc = "";// 产品类型
	public String last3_product_chg_num = "";// 近三个月内产品变更次数

	public String status_desc = "";// 本月月末状态
	public String laststatus_desc = "";// 上月月末状态
	public String sta_chg_num = "";// 本月状态变更次数
	public String last3_sta_chg_num = "";// 近三个月内状态变更次数
	public String init_stop_num = "";// 本月主动停机次数
	public String last3_init_stop_num = "";// 近三个月内主动停机次数
	public String last_stop_date = "";// 最近停机时间
	public String sum_num23 = "";// 实收本金
	public String sum_num24 = "";// 服务成本

	public String f_loc_gn_toll_fee = ""; // 国内长途通话费
	public String f_roam_gn_gj_toll_fee = ""; // 国际长途通话费
	public String f_roam_gn_gat_toll_fee = ""; // 港澳台长途通话费
	public String f_voice_total_fee = ""; // 语音增值
	public String f_tatal_198_fee = ""; // 198增值
	public String f_trip_fee = ""; // 商旅热线
	public String f_search_fee = ""; // 话费查询

	public String a_m1_m2_arr_fee = ""; // 1－2个月内的欠费金额
	public String a_m2_m3_arr_fee = ""; // 2－3个月内的欠费金额
	public String a_m3_m6_arr_fee = ""; // 3－6个月内的欠费金额
	public String a_m6_m12_arr_fee = ""; // 6-12个月内的欠费金额
	public String a_y1_y2_arr_fee = ""; // 1年-2年内的欠费

	public String a_y2_y3_arr_fee = ""; // 2年-3年内的欠费金额
	public String a_y3_arr_fee = ""; // 3年以上的欠费

	public String r_oth_real_fee = ""; // 3年以上的欠费

	// HERE IS FROM DATABASE
	public String gather_mon = "";
	public String user_id = "";
	public String svc_id = "";
	public String cust_id = "";
	public String cust_name = "";
	public String grpcust_name = "";
	public String vipknd_desc = "";
	public String userdnnr_desc = "";
	public String innetchannel_desc = "";
	public String devechannel_desc = "";
	public String viptag_desc = "";
	public String detainchannel_desc = "";
	public String evalchannel_desc = "";
	public String innettag_desc = "";
	public String innet_day = "";
	public String offnet_day = "";
	public String gender_desc = "";
	public String age = "";
	public String olduserdnnr_desc = "";
	public String idknd_desc = "";
	public String cert_id = "";
	public String post_zip = "";
	public String post_addr = "";
	public String home_telno = "";
	public String home_addr = "";
	public String varietyknd_desc = "";
	public String saleplan_desc = "";
	public String vsvcplanpack_desc = "";
	public String dsvcplanpack_desc = "";
	public String ssvcplanpack_desc = "";
	public String vsvcplan_desc = "";
	public String dsvcplan_desc = "";
	public String ssvcplan_desc = "";
	public String beg_day = "";
	public String end_day = "";
	public String real_end_day = "";
	public String agree_fee = "";
	public String agree_mons = "";
	public String rent_times = "";
	public String min_consumed_fee = "";
	public String avg_consumed_fee = "";
	public String contact_name = "";
	public String contact_telno = "";
	public String contact_email = "";
	public String commend_name = "";
	public String commend_unit = "";
	public String v_call_times = "";
	public String v_d_avg_call_times = "";
	public String v_last3_d_avg_call_times = "";
	public String v_call_dura = "";
	public String v_d_avg_call_dura = "";
	public String v_last3_d_avg_call_dura = "";
	public String v_lac_num = "";
	public String v_fst_lac_id = "";
	public String v_fst_lac_times = "";
	public String v_fst_lac_dura = "";
	public String v_sec_lac_id = "";
	public String v_sec_lac_times = "";
	public String v_sec_lac_dura = "";
	public String v_fst_busy_seg = "";
	public String v_fst_busy_times = "";
	public String v_fst_busy_dura = "";
	public String v_sec_busy_seg = "";
	public String v_sec_busy_times = "";
	public String v_sec_busy_dura = "";
	public String v_fstcarrier_desc = "";
	public String v_fst_carrier_times = "";
	public String v_fst_carrier_dura = "";
	public String v_cf_times = "";
	public String v_cf_dura = "";
	public String v_max_cf_svc_id = "";
	public String v_max_cf_times = "";
	public String v_max_cf_dura = "";
	public String v_i_cf_times = "";
	public String v_i_cf_dura = "";
	public String v_o_cf_times = "";
	public String v_o_cf_dura = "";
	public String s_sms_num = "";
	public String s_ix_bytes = "";
	public String s_i_ptp_mo_num = "";
	public String s_o_cmc_ptp_mo_num = "";
	public String s_o_phs_ptp_mo_num = "";
	public String s_ptp_num = "";
	public String s_voice_10158_num = "";
	public String s_voice_nice_num = "";
	public String s_voice_oth_num = "";
	public String s_hand_ix_bytes = "";
	public String s_hdsj_ix_bytes = "";
	public String s_e_ix_bytes = "";
	public String s_star_ix_bytes = "";
	public String s_sxxgx_ix_bytes = "";
	public String s_oth_ix_bytes = "";
	public String v_org_dura = "";
	public String v_trm_dura = "";
	public String v_toll_dura = "";
	public String v_i_toll_dura = "";
	public String v_n_toll_dura = "";
	public String f_fav_fee = "";
	public String f_rent_fee = "";
	public String f_call_fee = "";
	public String f_addval_fee = "";
	public String f_oth_fav_fee = "";
	public String f_loc_fee = "";
	public String f_roam_fee = "";
	public String f_sms_fee = "";
	public String f_ix_fee = "";
	public String f_ring_fee = "";
	public String f_voicebox_fee = "";
	public String f_nicevoice_fee = "";
	public String f_airmeeting_fee = "";
	public String f_voicenet_fee = "";
	public String f_oth_addval_fee = "";
	public String f_last3_avg_arpu = "";
	public String v_last3_avg_call_dura = "";
	public String last3_avg_arpm = "";
	public String arpm = "";
	public String f_pref_fav_fee = "";
	public String f_toll_fee = "";
	public String f_hand_ix_fee = "";
	public String f_hdsj_ix_fee = "";
	public String f_e_ix_fee = "";
	public String f_star_ix_fee = "";
	public String f_sxxgx_ix_fee = "";
	public String f_oth_ix_fee = "";
	public String f_val_desc = "";
	public String f_last3_valchg_num = "";
	public String f_last6_valchg_num = "";
	public String f_val_value = "";
	public String valqltydetail_desc = "";
	public String a_agg_arrfee = "";
	public String a_arr_fee = "";
	public String a_yarr_fee = "";
	public String a_first_arr_mon = "";
	public String a_last_arr_mon = "";
	public String a_arr_mon_num = "";
	public String a_m02_arrfee = "";
	public String a_m03_arrfee = "";
	public String a_m06_arrfee = "";
	public String a_m12_arrfee = "";
	public String a_g12_arrfee = "";
	public String r_real_fee = "";
	public String r_last_mon_fee = "";
	public String r_this_year_fee = "";
	public String r_rent_fee = "";
	public String r_call_fee = "";
	public String r_addval_fee = "";
	public String r_oth_fav_fee = "";
	public String total_cost_fee = "";
	public String settle_out_fee = "";
	public String cs_perc_fee = "";
	public String cs_cut_fee = "";
	public String cs_agent_fee = "";
	public String cs_fix_fee = "";
	public String cs_fee = "";
	public String mp_cost = "";
	public String consumed_fee = "";
	public String consumed_mons = "";
	public String v_r_call_dura = "";
	public String v_loc_dura = "";
	public String v_busy_call_dura = "";
	public String v_notbusy_call_dura = "";
	public String s_o_ptp_num = "";
	public String s_o_cmc_ptp_mt_num = "";
	public String s_o_phs_ptp_mt_num = "";
	public String s_ltzx_mo_num = "";
	public String s_ltzx_mt_num = "";
	public String f_unroam_inla_toll_fee = "";
	public String f_roam_inla_toll_fee = "";
	public String f_unroam_intr_toll_fee = "";
	public String f_roam_intr_toll_fee = "";
	public String f_unroam_gat_toll_fee = "";
	public String f_roam_gat_toll_fee = "";
	public String f_java_ix_fee = "";
	public String f_brew_ix_fee = "";
	public String this_mp_cost = "";

	public String cs_datafix_fee = "";
	public String cs_addval_fee = "";
	public String cs_initpack_fee = "";
	public String cs_mailbox_fee = "";
	public String cs_voiceplat_fee = "";
	public String s_brew_ix_bytes = "";
	public String s_java_ix_bytes = "";
	public String v_total_fee = "";
	public String onl_dura = "";
	public String v_base_fee_dura = "";

	// 计算字段
	public String sum_num1 = ""; // CONSUMED_FEE/AGREE_FEE //已完成消费约定
	public String sum_num2 = ""; // AGREE_MONS-CONSUMED_MONS //协议剩余月数
	public String sum_num3 = ""; // V_LOC_DURA/V_R_CALL_DURA //本地/漫游
	public String sum_num4 = ""; // V_NOTBUSY_CALL_DURA/V_BUSY_CALL_DURA // 发生时段
	public String sum_num5 = ""; // V_CF_TIMES/V_CALL_TIMES //占总通话次数比例
	public String sum_num6 = ""; // S_PTP_NUM/S_SMS_NUM //点对点短信占
	public String sum_num7 = ""; // V_CALL_TIMES/V_CALL_DURA //单次通话时长
	public String sum_num8 = ""; // S_O_PTP_NUM-S_O_CMC_PTP_MO_NUM-S_O_PHS_PTP_MO_NUM
									// //网间其他 -- 其他
	public String sum_num9 = ""; // S_O_CMC_PTP_MO_NUM+S_O_CMC_PTP_MT_NUM //与移动
	public String sum_num10 = ""; // S_O_PHS_PTP_MO_NUM+S_O_PHS_PTP_MT_NUM
									// //与小灵通
	public String sum_num11 = ""; // S_LTZX_MO_NUM+S_LTZX_MT_NUM //联通在信条数
	public String sum_num12 = ""; // F_UNROAM_INLA_TOLL_FEE+F_ROAM_INLA_TOLL_FEE
									// //国内长途
	public String sum_num13 = ""; // F_UNROAM_INTR_TOLL_FEE+F_ROAM_INTR_TOLL_FEE
									// //国际长途
	public String sum_num14 = ""; // F_UNROAM_GAT_TOLL_FEE+F_ROAM_GAT_TOLL_FEE
									// //港澳台长途
	public String sum_num15 = ""; // F_ADDVAL_FEE-F_SMS_FEE-F_IX_FEE //其他增值
	public String sum_num16 = ""; // F_JAVA_IX_FEE+F_BREW_IX_FEE //神奇宝典
	public String sum_num17 = ""; // CS_FEE+MOB_GIFT_FEE+MP_COST_FEE //经营成本
	public String sum_num18 = ""; // CS_MAILBOX_FEE+CS_VOICEPLAT_FEE+CS_INITPACK_FEE+CS_ADDVAL_FEE+CS_DATAFIX_FEE
									// //其它
	public String sum_num19 = ""; // V_CALL_DURA-V_TOLL_DURA //本地时长
	public String sum_num20 = ""; // s_brew_ix_bytes+s_java_ix_bytes //神奇宝典
	public String sum_num21 = ""; // v_total_fee/V_CALL_DURA //ARPM 分钟
	public String sum_num22 = ""; // F_PREF_FAV_FEE/(F_FAV_FEE +
									// F_PREF_FAV_FEE)*100优惠比例

	// HERE IS USER DEFINE 这一行不要删除！

	public static void main(String[] args) {
		String a = Arith.divs(null, null, 2);
		if (a != null && Double.parseDouble(a) > 1) {
			System.out.println("dddddddd");
		}

		FmUniUserViewMTable obj = new FmUniUserViewMTable();
		Class tempClass = obj.getClass();
		Field tempField[] = null;
		String temp = "";
		try {
			tempField = tempClass.getDeclaredFields();
			for (int i = 0; i < tempField.length; i++) {
				// 获取对象赋值列表
				// if(i+1<tempField.length){
				// temp=tempField[i+1].getName();
				// temp="fm[i]."+temp+" = result[i]["+i+"];";
				// System.out.println(temp);
				// }
				// 获取sql语句字串
				if (i < tempField.length) {
					temp += tempField[i].getName() + ",";
					if (i % 5 == 0 || i == tempField.length - 1) {
						System.out.println(temp);
						temp = "";
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
