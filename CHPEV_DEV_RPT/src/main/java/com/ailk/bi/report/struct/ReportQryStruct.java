package com.ailk.bi.report.struct;

import com.ailk.bi.common.event.JBTableBase;

public class ReportQryStruct extends JBTableBase {
    public String showType;

    public String transState;

    // 流水
    public String adjustUser;

    public String adjustOrderId;

    public String adjustCreator;

    public String adjustState;

    public String rpt_orderType;

    public String rpt_orderType2;

    // 客户接触
    public String qry_custName;

    public String qry_servNumber;

    public String qry_orderType;

    public String qry_interviewNature;

    public String qry_interviewType;

    public String qry_contactMode;

    public String qry_activityId;

    public String qry_interviewState;

    public String qry_pleasedState;

    public String qry_activityName;

    // 工单查询页面
    public String setType;// 1表示查询页面；2表示分派；3：表示改派

    public String custName;

    public String servNumber;

    public String orderState;

    public String orderNo;

    public String orderCusSvcMgr;

    public String orderlevel;

    public String activityId;

    public String date1;

    public String date2;

    public String contact2;

    public String date3;

    public String date4;

    public String activityName;

    public String outChannle;

    public String groupName;

    public String groupType;

    public String createType;

    public String status;

    public String brandType;

    public String step1_survey_name;

    public String step1_survey_type;

    // 营销平台新建
    public String survey_name;

    public String survey_type;

    public String survey_state;

    public String question_content;

    public String question_type;

    public String question_state;

    public String msg_content;

    public String msg_type;

    public String msg_state;

    public String policy_name;

    public String policy_type;

    public String policy_state;

    public String target_name;// 目标名称

    public String target_type;// 目标类型

    public String target_state;// 目标状态

    public String tactic_name;// 策略名称

    public String tactic_type;// 策略类型

    public String tactic_state;// 策略状态

    public String tactic_creator;// 策略创建人

    public String project_name;// 方案名称

    public String project_type;// 方案类型

    public String project_state;// 方案状态

    public String project_level;// 方案优先级

    public String project_Priority;// 方案级别

    public String channleType_name;// 渠道类型名称

    public String channleType_state;// 渠道类型状态

    public String channle_name;// 渠道名称

    public String channle_state;// 渠道状态

    public String channle_code;// 渠道编码

    public String channle_createDate;// 渠道创建时间

    public String package_type;// 套餐类型

    public String package_name;// 套餐名称

    public String package_state;// 套餐状态

    public String activityType_name;// 活动类型名称

    public String activity_type;// 活动类型

    public String activity_name;// 活动名称

    public String activity_state;// 活动状态

    public String activity_client;// 活动中的客户名单类型

    public String activity_project;// 活动中对应的方案

    public String client_type;

    public String nameList_Name;

    /**
     * 报表中心用到的查询结构
     */
    private static final long serialVersionUID = 1L;

    public String first_view = "N";// 是否第一次进入

    public String visible_data = "N";// 报表数据是否可见

    public String date_s = "";// 数据开始日期

    public String date_e = "";// 数据结束日期

    public String region_id_r = "";// 报表选择数据区域

    public String divcity_flag = "";// 是否分区域查看

    public String row_flag = "";// 指标横向还是竖向展示

    public String right_attach_region = "";// 归属区域，数据权限可能用到

    public String right_metro_id = "";// 省份，数据权限可能用到

    public String right_city_id = "";// 行区，数据权限可能用到

    public String right_county_id = "";// 分公司，数据权限可能用到

    public String right_area_id = "";// 分部，数据权限可能用到

    public String right_sec_area_id = "";// 片区，数据权限可能用到

    public String svc_knd = ""; // 业务类型，业务权限可能用到

    public String attach_region = "";// 归属区域

    public String metro_id = "";// 省份

    public String city_id = "";// 行区

    public String county_id = "";// 分公司

    public String area_id = "";// 分部

    public String area_name = "";

    public String sec_area_id = "";// 片区

    public String channel_id = "";// 渠道

    public String oper_no = "";// 操作员

    public String dim1 = "";// 条件1

    public String dim2 = "";// 条件2

    public String dim3 = "";// 条件3

    public String dim4 = "";// 条件4

    public String dim5 = "";// 条件5

    public String dim6 = "";// 条件6

    public String dim7 = "";// 条件7

    public String dim8 = "";// 条件8

    public String dim9 = "";// 条件9

    public String dim10 = "";// 条件10

    public String dim11 = "";// 条件11

    public String dim12 = "";// 条件12

    public String dim13 = "";// 条件13

    public String dim14 = "";// 条件14

    public String dim15 = "";// 条件15

    public String dim16 = "";// 条件16

    public String dim17 = "";// 条件17

    public String dim18 = "";// 条件18

    public String dim19 = "";// 条件19

    public String dim20 = "";// 条件20

    public String expandcol = "";// 展开列

    public String order_code = "";// 排序字段

    public String order_icolumn = "";// 排序列号

    public String order = "";// 排序方向

    public String nowpage = "";// 分页当前页码

    // 用户报表查询
    public String rpt_date = "";// 统计日期

    public String rpt_type = "";// 报表类型

    public String rpt_kind = "";// 报表分类

    public String rpt_cycle = "";// 报表周期

    public String rpt_id = "";// 报表编码

    public String rpt_local_res_code = "";// 报表本地应用编码

    public String rpt_name = "";// 报表名称

    public String rpt_status = "";// 报表类型

    public String print_code = "";// 报表打印码

    public String gather_date = "";

    public String gather_day = "";

    public String gather_month = "";

    public String oper_type = "";

    public String gather_day_s = "";

    public String gather_day_e = "";

    /**
     * 地市描述
     */
    public String city_desc = "";

    /**
     * 区县描述
     */
    public String county_desc = "";

    /**
     * 客户群
     */
    public String cust_group_id = "";

    /**
     * 客户群描述
     */
    public String cust_group_desc = "";

    /**
     * 号段
     */
    public String phone_seg_type_id = "";

    /**
     * 号段描述
     */
    public String phone_seg_type_desc = "";

    public String brand_knd = ""; // 品牌

    public String sub_brand_knd = ""; // 子品牌

    public String channel_type = ""; // 渠道类型

    public String channel_code = ""; // 渠道编号

    public String all_flag_svc = ""; // 全部标志

    public String all_flag_product_lv1 = ""; // 全部标志

    public String svc_id = ""; // 服务类型编码

    public String svc_name = ""; // 服务类型名称

    public String product_lv1_id = ""; // 产品大类ID

    public String product_lv1_name = ""; // 产品大类描述

    public String product_lv2_id = ""; // 产品类型lv2ID

    public String product_lv2_name = ""; // 产品类型lv2描述

    public String product_lv3_id = ""; // 产品类型lv3ID

    public String product_lv3_name = ""; // 产品类型lv3描述

    public String city_name = "";

    public String optype = "";

    public String plan_id = "";

    public String plan_name = "";

    public String wireless_id = "";

    public String wireless_desc = "";

    public String menu_url = "";

    public String muti_plan = "";

    public String user_tag = "";

    public String sub_id = "";

    public String sub_brand_id = "";

    public String brand_id = "";

    public String brand_name = "";

    public String anydog = "";

    public String product_id = "";

    public String cust_group_name = "";

    public String msu_id = "";

    public String msu_name = "";

    public String type = "";

    // 竞争对手稳定性权重
    public String call_times = "";

    public String calling_times = "";

    public String called_times = "";

    public String call_dura = "";

    public String calling_dura = "";

    public String called_dura = "";

    public String sphere_num = "";

    public String avg_call_dura = "";

    public String opp_id = "";

    public String opp_stable_value = "";

    public String opp_rownum = "";

    public String carrier_id = "";

    public String carrier_name = "";

    public String opp_valued_value = "";

    public String avg_3_ct_vip_unum = "";

    public String avg_3_ct_hign_value_unum = "";

    public String avg_3_call_dura = "";

    public String avg_3_call_times = "";

    public String avg_3_avg_call_dura = "";

    public String avg_3_zbjxs = "";

    public String avg_3_sphere_num = "";

    public String stable_value = "";

    public String opp_kfyj = "";

    public String opp_ywyj = "";

    public String opp_gjzkh = "";

    public String opp_jwq = "";

    public String opp_gjzfx = "";

    public String opp_gwdfx = "";

    // 竞争对手策反评估
    public String high_value_cust_amt_lvl_id = "";

    public String rela_net_cust_amt_lvl_id = "";

    public String high_cust_amt_lvl_id = "";

    public String stability_lvl_id = "";

    public String opp_user_value_eval_val = "";

    public String busi_use_tag = "";

    public String cservice_tag = "";

    public String call_times_lvl_id = "";

    public String call_evarage_dura_lvl_id = "";

    /**
     * x轴分界1
     */
    public String splitx_one = "";

    /**
     * x轴分界2
     */
    public String splitx_two = "";

    /**
     * x轴分界3
     */
    public String splitx_three = "";

    /**
     * x轴分界4
     */
    public String splitx_four = "";

    /**
     * y轴分界1
     */
    public String splity_one = "";

    /**
     * y轴分界2
     */
    public String splity_two = "";

    /**
     * y轴分界3
     */
    public String splity_three = "";

    /**
     * y轴分界4
     */
    public String splity_four = "";

    public String table_type = ""; // 11,12 日表 ；13,14月表

    public String check_type = ""; // 核查类型

    public String table_index = ""; // 接口

    public String user_id = "";

    public String price_plan_id = "";

    public String price_plan_desc = "";

    public String add_dim_id = "";

    public String add_dim_desc = "";

    public String online_lvl_id = "";

    public String online_lvl_desc = "";

    public String c_pstn_id = ""; // C固标识

    public String c_pstn_desc = "";

    public String discount_type_id = ""; // 优惠类型

    public String discount_type_desc = "";

    public String busi_type_id = ""; // 优惠类型

    public String busi_type_desc = "";

    // 考评分类
    public String classifySelect = "";

    // 省分id
    public String province_code = "";

    // 地市id
    public String city_code = "";

    // 用户级别
    public String ctl_lvl = "";

    // 连锁级别
    public String chain_level = "";

    // 渠道类型
    public String chanal_type = "";

    // 排名波动
    public String change_rank = "";

    // 排名上升、下降、不变
    public String change_ss = "";

    public String change_xq = "";

    public String change_bb = "";

    // 归属本部
    public String owe_code = "";

    public String chnl_id = "";

    public String chnl_name = "";

    public String isMrg = "";
}
