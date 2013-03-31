package com.ailk.bi.subject.admin.dao.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ailk.bi.common.app.AppConst;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.dbtools.WebDBUtil;
@SuppressWarnings("unchecked")
public class AiModResultDaoImpl {
    /* 得到规则id */
    public static String getRuleId(String mod_id) {
        String rule_id = "";
        String sql = "select rule_id from ai_mod_item_rule_relation where item_id = (select item_id from ai_mod_item where mod_id = ?)";
        String[] param = { mod_id };
        List<Map<String, Object>> rules = new ArrayList();
        try {
            rules = WebDBUtil.execQryArrayMap(sql, param);
        } catch (AppException e) {
            e.printStackTrace();
        }
        if (rules.size() > 0) {
            Map rule = (Map) rules.get(0);
            rule_id = (String) rule.get("RULE_ID");
        }
        return rule_id;
    }

    // 表头数据的查询
    public static List<Map<String, Object>> getHeadList(String select_sql, String[] params,
            String table_name, String datasource) throws AppException{
        List<Map<String, Object>> heads = new ArrayList();
        String sql = "";
        try {
            if (select_sql.equals("0")) {
                sql = "select nvl(mod_name,'') as mod_name from AI_MOD_CONFIG where mod_id=?";
            } else if (select_sql.equals("1")) {
                sql = "select (select busi_name from AI_BUSI_CFG where busi_code = a.busi_code) as busi_name from ai_rule_detail a where detail_type=? and rule_id=? order by sort_id";
            } else if (select_sql.equals("2")) {
                sql = "SELECT count(1) as count FROM ai_rule_cal_busi_detail where rule_id=?";
            } else if (select_sql.equals("3")) {
                sql = "SELECT oplist,cal_value,busi_code from ai_rule_detail where rule_id=? and detail_type=? order by sort_id";
            } else if (select_sql.equals("4")) {
                sql = "SELECT cal_method FROM ai_rule_calculation_cfg WHERE rule_id = ?";
            } else if (select_sql.equals("5")) {
                sql = "SELECT cal_para_value FROM ai_rule_cal_busi_detail WHERE rule_id = ? order by sort_id";
            } else if (select_sql.equals("6")) {
                sql = "SELECT mod_id,cal_data_src,to_char(cal_data_date,'yyyyMM') as cal_data_date,mod_src_id,temp_id FROM ai_pre_case_mod_item where case_id=?";
            } else if (select_sql.equals("7")) {
                sql = "SELECT temp_type,create_date FROM ai_pre_cal_template where temp_id=?";
            } else if (select_sql.equals("8")) {
                sql = "select province_code,province_name from st_dim_area_province where province_code in(select distinct province_code from ai_mod_area where mod_id=?)";
            } else if (select_sql.equals("9")) {
                sql = "select sum(usercounts) as usercount, to_char(nvl(sum(sumfees), 0), 'fm99999999990.00') as sumfee,to_char(nvl(sum(amounts), 0), 'fm99999999990.00') as amount"
                        + " from (select mod_id, calc_cycle, count(distinct USER_NO) as usercounts,sum(FEE) as sumfees,"
                        + "nvl(sum(ACCT_AMOUNT), 0) /decode(count(USER_NO), 0, 1, count(USER_NO)) as amounts from "
                        + table_name
                        + " where mod_id = ? and calc_cycle = ? and SETT_PROVINCE_CODE=? group by user_no, mod_id, CALC_CYCLE)"
                        + "group by mod_id, calc_cycle";
            } else if (select_sql.equals("10")) {
                sql = " select busi_type, sum(b.allvalue) as allvalue, to_char(sum(b.allfee),'fm99999999990.00') as allfee, to_char(sum(b.allrevenue),'fm99999999990.00') as allrevenue, to_char((sum(b.allfee) / decode(sum(b.allrevenue), 0, 1, sum(b.allrevenue)) * 100),'fm99999999990.00') as allzhan"
                        + " from (select a.*, (CHNL_PROD_VALUE1 + CHNL_PROD_VALUE2 + CHNL_PROD_VALUE3 + CHNL_PROD_VALUE4 + CHNL_PROD_VALUE5 + CHNL_PROD_VALUE6 + CHNL_PROD_VALUE7 + CHNL_PROD_VALUE8 +"
                        + "CHNL_PROD_VALUE9 + CHNL_PROD_VALUE10 + CHNL_PROD_VALUE11 + CHNL_PROD_VALUE12) as allvalue,(COMM_FEE1 + COMM_FEE2 + COMM_FEE3 + COMM_FEE4 + COMM_FEE5 + COMM_FEE6 + COMM_FEE7 + COMM_FEE8 + COMM_FEE9 + COMM_FEE10 + COMM_FEE11 + COMM_FEE12)/1000 as allfee,"
                        + "(REVENUE1 + REVENUE2 + REVENUE3 + REVENUE4 + REVENUE5 + REVENUE6 + REVENUE7 + REVENUE8 + REVENUE9 + REVENUE10 + REVENUE11 + REVENUE12)/1000 as allrevenue"
                        + " from bill_template_result a) b where temp_id = ? and mod_id = ? group by busi_type";
            } else if (select_sql.equals("11")) {
                sql = "select busi_type,prod_price,sum(chnl_prod_value1) as chnl_prod_value1,sum(chnl_prod_value2) as chnl_prod_value2,sum(chnl_prod_value3) as chnl_prod_value3,sum(chnl_prod_value4) as chnl_prod_value4,sum(chnl_prod_value5) as chnl_prod_value5,"
                        + "sum(chnl_prod_value6) as chnl_prod_value6, sum(chnl_prod_value7) as chnl_prod_value7, sum(chnl_prod_value8) as chnl_prod_value8, sum(chnl_prod_value9) as chnl_prod_value9, sum(chnl_prod_value10) as chnl_prod_value10, sum(chnl_prod_value11) as chnl_prod_value11,"
                        + "sum(chnl_prod_value12) as chnl_prod_value12,sum(comm_fee1)/1000 as comm_fee1,sum(comm_fee2)/1000 as comm_fee2,sum(comm_fee3)/1000 as comm_fee3,sum(comm_fee4)/1000 as comm_fee4,sum(comm_fee5)/1000 as comm_fee5,sum(comm_fee6)/1000 as comm_fee6,sum(comm_fee7)/1000 as comm_fee7,sum(comm_fee8)/1000 as comm_fee8,"
                        + "sum(comm_fee9)/1000 as comm_fee9,sum(comm_fee10)/1000 as comm_fee10,sum(comm_fee11)/1000 as comm_fee11,sum(comm_fee12)/1000 as comm_fee12,sum(revenue1)/1000 as revenue1,sum(revenue2)/1000 as revenue2,sum(revenue3)/1000 as revenue3,sum(revenue4)/1000 as revenue4,sum(revenue5)/1000 as revenue5,sum(revenue6)/1000 as revenue6,"
                        + "sum(revenue7)/1000 as revenue7,sum(revenue8)/1000 as revenue8,sum(revenue9)/1000 as revenue9,sum(revenue10)/1000 as revenue10,sum(revenue11)/1000 as revenue11,sum(revenue12)/1000 as revenue12,(sum(comm_fee1)/decode(sum(revenue1),0,1,sum(revenue1))*100) as zhanbi1,(sum(comm_fee2)/decode(sum(revenue2),0,1,sum(revenue2))*100) as zhanbi2,"
                        + "(sum(comm_fee3)/decode(sum(revenue3),0,1,sum(revenue3))*100) as zhanbi3,(sum(comm_fee4)/decode(sum(revenue4),0,1,sum(revenue4))*100) as zhanbi4,(sum(comm_fee5)/decode(sum(revenue5),0,1,sum(revenue5))*100) as zhanbi5,(sum(comm_fee6)/decode(sum(revenue6),0,1,sum(revenue6))*100) as zhanbi6,"
                        + "(sum(comm_fee7)/decode(sum(revenue7),0,1,sum(revenue7))*100) as zhanbi7,(sum(comm_fee8)/decode(sum(revenue8),0,1,sum(revenue8))*100) as zhanbi8,(sum(comm_fee9)/decode(sum(revenue9),0,1,sum(revenue9))*100) as zhanbi9,(sum(comm_fee10)/decode(sum(revenue10),0,1,sum(revenue10))*100) as zhanbi10,"
                        + "(sum(comm_fee11)/decode(sum(revenue11),0,1,sum(revenue11))*100) as zhanbi11,(sum(comm_fee12)/decode(sum(revenue12),0,1,sum(revenue12))*100) as zhanbi12"
                        + " from bill_template_result where temp_id = ? and mod_id = ? group by busi_type, prod_price order by prod_price";
            } else if (select_sql.equals("12")) {
                sql = "select busi_type,prod_price,sum(chnl_prod_value"
                        + table_name
                        + ") as chnl_prod_value,to_char(sum(comm_fee"
                        + table_name
                        + ")/1000,'fm99999999990.00') as comm_fee,"
                        + "to_char(sum(revenue"
                        + table_name
                        + ")/1000,'fm99999999990.00') as revenue,to_char((sum(comm_fee"
                        + table_name
                        + ")/decode(sum(revenue"
                        + table_name
                        + "),0,1,sum(revenue"
                        + table_name
                        + "))*100),'fm99999999990.00') as zhanbi "
                        + " from bill_template_result where temp_id = ? and mod_id = ? group by busi_type, prod_price order by prod_price";
            } else if (select_sql.equals("14")) {
                sql = "select table_name,data_src from dim_comm_datasrc_config where province_code=?";
            } else if (select_sql.equals("13")) {
                sql = "select code_name from ui_code_list where type_code=? and code_id=?";
            } else if (select_sql.equals("15")) {
                sql = "SELECT busi_code  FROM AI_BUSI_CFG a WHERE  a.SRC_BUSI_CODE ='100020' and busi_code=?";
            }

            System.out.println("查询sql：" + sql);
            System.out.println("select_sql:"+select_sql+",datasource:"+datasource);
            if (datasource != null && !datasource.equals("")) {
                heads = WebDBUtil.execQryArrayMapPro(sql, params, datasource);
            } else {
                heads = WebDBUtil.execQryArrayMap(sql, params);
            }
        } catch (AppException e) {
            throw new AppException("查询数据失败"+e.toString());
        }
        return heads;
    }
    /**
     * 
     * @param mod_name 政策名称
     * @param isimport 计算文件来源  0:现网数据  1:预演数据
     * @param rule_id  规则ID
     * @param mod_id   政策ID
     * @param month 现网数据的计算时间
     * @param create_year 创建日期
     * @param temp_id  模板ID
     * @param province_code 省分
     * @param type 0 表示有现网政策  1表示无现网政策
     * @return
     * @throws AppException
     * @author mubaoduo
     */
    public static List getResultString(String mod_name, String isimport, String rule_id,
            String mod_id, String month, String create_year, String temp_id, String province_code,String type)
            throws AppException {
        int rowspan = 0;
        Map reulst = new HashMap();
        List condition_reslut = new ArrayList();
        String[] param = { rule_id };
        /*1 根据规则ID查询指标数量*/
        condition_reslut = getHeadList("2", param, "", "");
        /*2 计算合并单元格数rowspan */
        if (condition_reslut.size() > 0) {
            reulst = (Map) condition_reslut.get(0);
            rowspan = Integer.parseInt((String) reulst.get("COUNT"));
        }
        /*3 获取政策的适用省分及对应名称*/
        String[] param1 = { mod_id };
        condition_reslut = getHeadList("8", param1, "", "");
        List<String> result_string = new ArrayList();
        /*3.1 绘制table的行与列*/
        for (int i = 0; i < condition_reslut.size(); i++) {
            reulst = (Map) condition_reslut.get(i);
            String province_name = (String) reulst.get("PROVINCE_NAME");
            String province_code2 = (String) reulst.get("PROVINCE_CODE");
            StringBuilder sb = new StringBuilder();
            String nostring = "&nbsp;";
            if (isimport.equals(AppConst.IS_NoImport)) {
                String data_source = getDataSrcName(province_code2, province_code);
                String[] param2 = { mod_id, month, province_code2 };
                /*3.2 绘制适用省分和政策名称结果列*/
                List result_jisuan = getHeadList("9", param2, "DR_COMM_" + month, data_source);
                if (result_jisuan.size() < 1) {
                    nostring = "无计算账期数据，因此计算结果为空。";
                }
                sb.append("<tr><td class='td3' rowspan=" + rowspan + ">" + province_name + "</td>");
                if("0".equals(type)){
                    sb.append("<td class='td4' rowspan=" + rowspan
                            + " style='color:#0000EE;'><a onclick='openModDetail1(" + mod_id
                            + ")' style='cursor:hand;'>" + mod_name + "</a></td>");
                }else{
                    sb.append("<td class='td4' rowspan=" + rowspan
                            + " style='color:#0000EE;'><a onclick='openModDetail2(" + mod_id
                            + ")' style='cursor:hand;'>" + mod_name + "</a></td>");
                }
            } else {
                sb.append("<tr><td class='td3' rowspan=" + rowspan + ">" + province_name + "</td>");
                sb.append("<td class='td4' rowspan=" + rowspan
                        + " style='color:#0000EE;'><a onclick='openModDetail(" + mod_id
                        + ")' style='cursor:hand;'>" + mod_name + "</a></td>");
            }
            /*3.3 绘制指标、数据等信息*/
            getPinzTable(sb, rule_id, isimport, rowspan, month, mod_id, create_year, temp_id,
                    province_code2, province_code);
            result_string.add(sb.toString() + "#&" + nostring);
        }

        return result_string;
    }

    public static String getShuang(String param) {
        if (param != null && !param.equals("")) {
            param = param.substring(1, param.length() - 1);
            try {
                Integer.parseInt(param);
            } catch (Exception e) {
                param = " ";
            }
        } else {
            param = "";
        }
        return param;
    }

    // 拼装区间数据
    public static String getpinzhuang(JSONObject param, String opty) {
        StringBuilder result = new StringBuilder();
        if (param != null && !param.equals("")) {
            if(param.get("start_opt").equals("<")){
                result.append("(");
            }else{
                result.append("[");
            }
            result.append(param.getString("start_value")+","+param.getString("end_value"));
            if(param.get("end_opt").equals("<")){
                result.append(")");
            }else{
                result.append("]");
            }
        } else {
            result.append("");
        }
        return result.toString();
    }

    public static StringBuilder getPinzTable(StringBuilder sb, String rule_id, String isimport,
            int row, String month, String mod_id, String create_year, String temp_id,
            String province_code2, String province_code) throws AppException {
        List result_jisuan = new ArrayList();
        String usercount = "0";
        String sumfee = "0.00";
        String amount = "0.00";
        String feezhan = "0%";
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DecimalFormat df1 = new DecimalFormat("#,##0");
        // 合计
        //int zongcount = 0;
        //Double allfee = 0.00;
        //Double allamount = 0.00;
        //Double allzhan = 0.00;
        // 得到拼装的条件指标和计算指标
        List condition_reslut = new ArrayList();
        List jisuan_reslut = new ArrayList();
        String[] fuza = new String[row];
        String[] param = { rule_id, "0" };
        condition_reslut = getHeadList("3", param, "", "");
        // 得到计算指标名称
        String jisuan_name = "";
        String[] param2 = { "1", rule_id };
        jisuan_reslut = getHeadList("1", param2, "", "");
        if (jisuan_reslut.size() > 0) {
            Map jisuan = (Map) jisuan_reslut.get(0);
            jisuan_name = (String) jisuan.get("BUSI_NAME");
        }
        String[] Jisugong = getJisugong(rule_id, row);
        String[] re = new String[condition_reslut.size()];
        List codenames = new ArrayList();
        Map codes = new HashMap();
        String qujian = "";
        for (int i = 0; i < condition_reslut.size(); i++) {
            Map reslut = (Map) condition_reslut.get(i);
            String busi_code = (String) reslut.get("BUSI_CODE");
            String opty = (String) reslut.get("OPLIST");
            String cal_value = (String) reslut.get("CAL_VALUE");
            if (opty.equals("{}") || opty.equals("[)")) {
                JSONArray cal = JSONArray.fromObject(cal_value);
                for (int j = 0; j < cal.size(); j++) {
                    JSONObject jo = JSONObject.fromObject(cal.get(j));
                    fuza[j] = getpinzhuang(jo, opty);
                    qujian = qujian + fuza[j];
                }
                re[i] = qujian;
            } else {
                String resu2 = cal_value.split("}")[0];
                if (busi_code.equals("100011")) {
                    String[] code1 = { "MOBLIE_CARD_STATUS", getShuang(resu2.split(":")[1]) };
                    codenames = getHeadList("13", code1, "", "");
                    if (codenames.size() > 0) {
                        codes = (Map) codenames.get(0);
                        re[i] = (String) codes.get("CODE_NAME");
                    }
                } else if (busi_code.equals("100012")) {
                    String[] code2 = { "STOP_STATUS", getShuang(resu2.split(":")[1]) };
                    codenames = getHeadList("13", code2, "", "");
                    if (codenames.size() > 0) {
                        codes = (Map) codenames.get(0);
                        re[i] = (String) codes.get("CODE_NAME");
                    }
                } else {
                    String[] code3 = { busi_code };
                    List isuserpro = null;
                    isuserpro = getHeadList("15", code3, "", "");
                    if (isuserpro != null && isuserpro.size() > 0) {
                        String[] code4 = { "USER_PROPERTY", getShuang(resu2.split(":")[1]) };
                        codenames = getHeadList("13", code4, "", "");
                        if (codenames.size() > 0) {
                            codes = (Map) codenames.get(0);
                            re[i] = (String) codes.get("CODE_NAME");
                        }
                    } else {
                        re[i] = opty + getShuang(resu2.split(":")[1]);
                    }
                }
            }
        }
        // 得到结果
        if (isimport.equals(AppConst.IS_NoImport)) {
            String data_source = getDataSrcName(province_code2, province_code);
            String[] params2 = { mod_id, month, province_code2 };
            result_jisuan = getHeadList("9", params2, "DR_COMM_" + month, data_source);
            for (int i = 0; i < result_jisuan.size(); i++) {
                Map result = (Map) result_jisuan.get(0);
                usercount = (String) result.get("USERCOUNT");
                amount = (String) result.get("AMOUNT");
                sumfee = (String) result.get("SUMFEE");
                if (amount.equals("0.00")) {
                    feezhan = "+∞";
                } else {
                    Double zhanbi = Double.parseDouble(sumfee) / Double.parseDouble(amount) * 100;
                    feezhan = df.format(zhanbi) + "%";
                }
            }
        }

        if (fuza != null && fuza[0] != null && !fuza[0].equals("")) {
            for (int i = 0; i < fuza.length; i++) {
                if (fuza[i] != null && !fuza[i].equals("")) {
                    if (i == 0) {
                        for (int j = 0; j < re.length; j++) {
                            if (re[j] != null && !re[j].equals("")) {
                                if (re[j].indexOf("}") > -1 || re[j].indexOf("[") > -1) {
                                    sb.append("<td class='td4'>" + fuza[i] + "</td>");
                                } else {
                                    sb.append("<td class='td4'>" + re[j] + "</td>");
                                }
                            }
                        }
                        sb.append("<td class='td4'>&nbsp;" + jisuan_name + "</td>");
                        sb.append("<td class='td4'>&nbsp;" + Jisugong[i] + "</td>");
                        if (isimport.equals(AppConst.IS_NoImport)) {
                            sb.append("<td class='td4' rowspan=" + row + ">" + df1.format(Long.parseLong(usercount)) + "</td>");
                            sb.append("<td class='td4' rowspan=" + row + ">" + df.format(Double.parseDouble(amount))+ "</td>");
                            sb.append("<td class='td4' rowspan=" + row + ">" + df.format(Double.parseDouble(sumfee)) + "</td>");
                            sb.append("<td class='td4' rowspan=" + row + ">" + feezhan + "</td>");
                        }
                        sb.append("</tr>");
                    } else {
                        sb.append("<tr>");
                        for (int j = 0; j < re.length; j++) {
                            if (re[j] != null && !re[j].equals("")) {
                                if (re[j].indexOf("}") > 0 || re[j].indexOf("[") > 0) {
                                    sb.append("<td class='td4'>" + fuza[i] + "</td>");
                                } else {
                                    sb.append("<td class='td4'>" + re[j] + "</td>");
                                }
                            }
                        }
                        sb.append("<td class='td4'>&nbsp;" + jisuan_name + "</td>");
                        sb.append("<td class='td4'>&nbsp;" + Jisugong[i] + "</td>");
                        sb.append("</tr>");
                    }
                }
            }
        } else {
            if (condition_reslut.size() > 0) {
                for (int i = 0; i < re.length; i++) {
                    if (re[i] != null && !re[i].equals(""))
                        sb.append("<td class='td4'>" + re[i] + "</td>");
                }
            } else {
                sb.append("<td class='td4'>&nbsp;</td>");
            }
            sb.append("<td class='td4'>&nbsp;" + jisuan_name + "</td>");
            sb.append("<td class='td4'>&nbsp;" + Jisugong[0] + "</td>");
            if (isimport.equals(AppConst.IS_NoImport)) {
                sb.append("<td class='td4'>" + df1.format(Long.parseLong(usercount)) + "</td>");
                sb.append("<td class='td4'>" + df.format(Double.parseDouble(amount)) + "</td>");
                sb.append("<td class='td4'>" + df.format(Double.parseDouble(sumfee)) + "</td>");
                sb.append("<td class='td4'>" + feezhan + "</td>");
            }
            sb.append("</tr>");
        }
        return sb;
    }

    private static String getDataSrcName(String province_code2, String province_code)
            throws AppException {
        String data_source = "";
       // if (province_code.equals(AppConst.Province_ZB)) {
            // 总部工号查询各省分在哪个库中
            String[] params3 = { province_code2 };
            List result_datasrc = new ArrayList();
            result_datasrc = getHeadList("14", params3, "", "");
            if (result_datasrc.size() > 0) {
                Map result = (Map) result_datasrc.get(0);
                data_source = (String) result.get("DATA_SRC");
                if (data_source == null || "".equals(data_source)) {
                    throw new AppException("省分[" + province_code2 + "]没配置数据源信息！");
                }
            } else {
                throw new AppException("省分没配置数据源信息！");
            }
       // }
        return data_source;
    }

    // 拼装计算公式
    public static String[] getJisugong(String rule_id, int row) throws AppException{
        String[] result = new String[row];
        List jisuan_fangs = new ArrayList();
        List jisuan_zhibiao = new ArrayList();
        String[] param = { rule_id };
        // 计算方式的获取
        String cal_method = "";
        jisuan_fangs = getHeadList("4", param, "", "");
        for (int i = 0; i < jisuan_fangs.size(); i++) {
            Map result_map = (Map) jisuan_fangs.get(0);
            cal_method = (String) result_map.get("CAL_METHOD");
        }

        // 计算指标的获取,目前只支持一期
        jisuan_zhibiao = getHeadList("5", param, "", "");
        for (int i = 0; i < jisuan_zhibiao.size(); i++) {
            Map result_map = (Map) jisuan_zhibiao.get(i);
            String cal_para_value = (String) result_map.get("CAL_PARA_VALUE");
            String value = "";
            if (cal_para_value != null && !cal_para_value.equals("")) {
                JSONArray ja = JSONArray.fromObject(cal_para_value);
                JSONObject jo = JSONObject.fromObject(ja.get(0));
                value = jo.getString("value");
                if (cal_method != null && cal_method.equals(AppConst.CAL_METHOD_JIN)) {
                    result[i] = "计算指标x" + value + "(元)";
                } else if (cal_method != null && cal_method.equals(AppConst.CAL_METHOD_BI)) {
                    result[i] = "计算指标x" + value + "%";
                } else {
                    result[i] = "固定金额" + value + "(元)";
                }
            }
        }
        return result;
    }

    public static String getResult2(String mod_id, String temp_id, String import_type)
            throws AppException {
        StringBuilder sb = new StringBuilder();
        Long zongcount = 0l;
        Double allzhanbi = 0.00;
        Double allfee = 0.00;
        Double allamount = 0.00;
        String allzhan = "0.00";
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DecimalFormat df1 = new DecimalFormat("#,##0");
        List result_jisuan = new ArrayList();
        String[] params = { temp_id, mod_id };
        result_jisuan = getHeadList("10", params, "", "");
        for (int i = 0; i < result_jisuan.size(); i++) {
            Map result = (Map) result_jisuan.get(i);
            zongcount = zongcount + Long.parseLong((String) result.get("ALLVALUE"));
            allamount = allamount + Double.parseDouble((String) result.get("ALLREVENUE"));
            allfee = allfee + Double.parseDouble((String) result.get("ALLFEE"));
        }
        if (allamount != 0.00) {
            allzhan = df.format((allfee / allamount) * 100);
        }
        if (result_jisuan.size() > 0) {
            sb.append("<tr><td class='td3' rowspan=" + result_jisuan.size()
                    + " style='text-align:center;'>" + zongcount + "</td>");
            for (int i = 0; i < result_jisuan.size(); i++) {
                Map result = (Map) result_jisuan.get(i);
                if (i == 0) {
                    sb.append("<td class='td4'>" + (String) result.get("BUSI_TYPE") + "</td>");
                } else {
                    sb.append("<tr><td class='td4'>" + (String) result.get("BUSI_TYPE") + "</td>");
                }
                if (import_type.equals("01") || import_type.equals("03")) {
                    sb.append("<td class='td4' style='text-align:right;'>"
                            + df1.format(Long.parseLong((String) result.get("ALLVALUE"))) + "</td>");
                } else {
                    if (zongcount == 0)
                        sb.append("<td class='td4' style='text-align:right;'>0.00%</td>");
                    else {
                        String zhanbi = df.format((Double
                                .parseDouble((String) result.get("ALLVALUE")) / zongcount) * 100);
                        allzhanbi = allzhanbi + Double.parseDouble(zhanbi);
                        sb.append("<td class='td4' style='text-align:right;'>" + zhanbi + "%</td>");
                    }
                }
                sb.append("<td class='td4' style='text-align:right;'>"
                        + df.format(Double.parseDouble((String) result.get("ALLREVENUE") ))+ "</td>");
                sb.append("<td class='td4' style='text-align:right;'>"
                        + df.format(Double.parseDouble((String) result.get("ALLFEE"))) + "</td>");
                sb.append("<td class='td4' style='text-align:right;'>"
                        + (String) result.get("ALLZHAN") + "%</td></tr>");
            }
        } else {
            sb.append("<tr><td class='td3' rowspan='1'>0</td>");
            sb.append("<td class='td4'>&nbsp;</td>");
            if (import_type.equals("01") || import_type.equals("03")) {
                sb.append("<td class='td4' style='text-align:right;'>0</td>");
            } else {
                sb.append("<td class='td4' style='text-align:right;'>0.00%</td>");
            }
            sb.append("<td class='td4' style='text-align:right;'>0.00</td>");
            sb.append("<td class='td4' style='text-align:right;'>0.00</td>");
            sb.append("<td class='td4' style='text-align:right;'>0.00%</td><tr>");
        }
        sb
                .append("<tr><td class='td3' colspan='2' style='text-align:center;font-weight:bold;'>合计</td>");
        if (import_type.equals(AppConst.USER_TEMP) || import_type.equals(AppConst.PRODUCT_TEMP)) {
            sb.append("<td class='td4' style='text-align:right;'>" + zongcount + "</td>");
        } else {
            sb.append("<td class='td4' style='text-align:right;'>" + df.format(allzhanbi)
                    + "%</td>");
        }
        sb.append("<td class='td4' style='text-align:right;'>" + df.format(allamount) + "</td>");
        sb.append("<td class='td4' style='text-align:right;'>" + df.format(allfee) + "</td>");
        sb.append("<td class='td4' style='text-align:right;'>" + allzhan + "%</td></tr>");
        return sb.toString();

    }

    public static String getResult3(String deal_month, String mod_id, String temp_id,
            String import_type) throws AppException {
        List result_jisuan = new ArrayList();
        int month = Integer.parseInt(deal_month.substring(4, 6));
        String[] params = { temp_id, mod_id };
        result_jisuan = getHeadList("12", params, String.valueOf(month), "");
        Map<String, List<Map>> groupList = new HashMap();
        List busitypes = new ArrayList();
        for (int i = 0; i < result_jisuan.size(); i++) {
            Map result = (Map) result_jisuan.get(i);
            String busi_type = (String) result.get("BUSI_TYPE");
            if (groupList.containsKey(busi_type)) {
                groupList.get(busi_type).add(result);
            } else {
                busitypes.add(busi_type);
                List<Map> temp = new ArrayList();
                temp.add(result);
                groupList.put(busi_type, temp);
            }
        }
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#,##0.00");
        DecimalFormat df1 = new DecimalFormat("#,##0");
        for (int j = 0; j < busitypes.size(); j++) {
            String busi_type = (String) busitypes.get(j);
            List results = groupList.get(busi_type);
            sb.append("<tr><td class='td3' rowspan=" + results.size()
                    + " style='text-align:center;'>" + busi_type + "</td>");
            for (int k = 0; k < results.size(); k++) {
                Map sing_result = (Map) results.get(k);
                if (k == 0) {
                    sb.append("<td class='td4' style='text-align:center;'>"
                            + df1.format(Long.parseLong((String)sing_result.get("PROD_PRICE"))) + "</td>");
                } else {
                    sb.append("<tr><td class='td4' style='text-align:center;'>"
                            + df1.format(Long.parseLong((String)sing_result.get("PROD_PRICE"))) + "</td>");
                }
                sb.append("<td class='td4' style='text-align:right;'>"
                        + df1.format(Long.parseLong((String) sing_result.get("CHNL_PROD_VALUE"))) + "</td>");
                sb.append("<td class='td4' style='text-align:right;'>"
                        + df.format(Double.parseDouble((String) sing_result.get("REVENUE"))) + "</td>");
                sb.append("<td class='td4' style='text-align:right;'>"
                        + df.format(Double.parseDouble((String) sing_result.get("COMM_FEE"))) + "</td>");
                sb.append("<td class='td4' style='text-align:right;'>"
                        + (String) sing_result.get("ZHANBI") + "%</td></tr>");
            }
        }
        return sb.toString();
    }

    public static void excelOut(String import_type, String mod_id, String temp_id,
            HttpServletResponse response) throws AppException {
        DecimalFormat df = new DecimalFormat("0.00");
        List result_jisuan = new ArrayList();
        String[] params = { temp_id, mod_id };
        result_jisuan = getHeadList("11", params, "", "");
        if (result_jisuan.size() == 0) {
            throw new AppException("查询结果为空！");
        }
        Map<String, List<Map>> groupList = new HashMap();
        List busitypes = new ArrayList();
        for (int i = 0; i < result_jisuan.size(); i++) {
            Map result = (Map) result_jisuan.get(i);
            String busi_type = (String) result.get("BUSI_TYPE");
            if (groupList.containsKey(busi_type)) {
                groupList.get(busi_type).add(result);
            } else {
                busitypes.add(busi_type);
                List<Map> temp = new ArrayList();
                temp.add(result);
                groupList.put(busi_type, temp);
            }
        }
        String head2 = "";
        if (import_type.equals(AppConst.USER_TEMP) || import_type.equals(AppConst.USER_BI_TEMP)) {
            head2 = AppConst.USER_CHANL_NUMBER;
        } else {
            head2 = AppConst.PRODUCT_CHANL_NUMBER;
        }
        int col = 0;
        int col2 = 0;
        try {
            jxl.write.WritableWorkbook workbook = Workbook.createWorkbook(new File("result.xls"));
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            jxl.write.WritableFont wf = new jxl.write.WritableFont(WritableFont.TAHOMA, 11,
                    WritableFont.BOLD, false);
            jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(wf);
            wcfF.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); // 加边框
            wcfF.setAlignment(jxl.format.Alignment.CENTRE); // 至中
            wcfF.setWrap(true);

            jxl.write.WritableFont wfs = new jxl.write.WritableFont(WritableFont.ARIAL, 9,
                    WritableFont.NO_BOLD, false);
            jxl.write.WritableCellFormat wcfFs = new jxl.write.WritableCellFormat(wfs);
            wcfFs.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN); // 加边框
            wcfFs.setAlignment(jxl.format.Alignment.CENTRE);
            // 设置列宽
            sheet.setColumnView(0, 15);
            sheet.setColumnView(1, 40);
            // 设置行高
            sheet.setRowView(1, 1000);
            sheet.addCell(new Label(0, 0, "", wcfF));
            sheet.addCell(new Label(1, 0, "", wcfF));
            sheet.mergeCells(2, 0, 5, 0);
            sheet.addCell(new Label(2, 0, "1月", wcfF));
            col = 2;
            col2 = 5;
            for (int i = 2; i < 13; i++) {
                col = col + 4;
                col2 = col2 + 4;
                sheet.mergeCells(col, 0, col2, 0);
                sheet.addCell(new Label(col, 0, i + "月", wcfF));
            }
            sheet.addCell(new Label(0, 1, "业务活动", wcfF));
            sheet.addCell(new Label(1, 1, "每个区间出账收入\012（应收/实收）中间值\012 1.如ARPU、套餐月费或平均套餐月费", wcfF));
            col = 1;
            for (int i = 0; i < 12; i++) {
                sheet.setColumnView(col + 1, 20);
                sheet.setColumnView(col + 2, 15);
                sheet.setColumnView(col + 3, 15);
                sheet.setColumnView(col + 4, 15);
                sheet.addCell(new Label(col + 1, 1, "其中：社会渠道\012" + head2, wcfF));
                sheet.addCell(new Label(col + 2, 1, "出账收入\012(元)", wcfF));
                sheet.addCell(new Label(col + 3, 1, "佣金(元)", wcfF));
                sheet.addCell(new Label(col + 4, 1, "佣金占收比\012(%)", wcfF));
                col = col + 4;

            }
            int end = 1;
            int begin = 2;
            for (int j = 0; j < busitypes.size(); j++) {
                String busi_type = (String) busitypes.get(j);
                List results = groupList.get(busi_type);
                end = end + results.size();
                sheet.mergeCells(0, begin, 0, end);
                sheet.addCell(new Label(0, begin, busi_type, wcfFs));
                for (int k = 0; k < results.size(); k++) {
                    Map sing_result = (Map) results.get(k);
                    sheet.addCell(new Label(1, begin + k, (String) sing_result.get("PROD_PRICE"),
                            wcfFs));
                    int row2 = 1;
                    for (int i = 1; i < 13; i++) {
                        sheet.addCell(new Label(row2 + 1, begin + k, (String) sing_result
                                .get("CHNL_PROD_VALUE" + i), wcfFs));
                        sheet.addCell(new Label(row2 + 2, begin + k, df.format(Double
                                .parseDouble((String) sing_result.get("REVENUE" + i))), wcfFs));
                        sheet.addCell(new Label(row2 + 3, begin + k, df.format(Double
                                .parseDouble((String) sing_result.get("COMM_FEE" + i))), wcfFs));
                        sheet.addCell(new Label(row2 + 4, begin + k, df.format(Double
                                .parseDouble((String) sing_result.get("ZHANBI" + i)))
                                + "%", wcfFs));
                        row2 = row2 + 4;
                    }
                }
                begin = end + 1;
            }
            workbook.write();
            workbook.close();
            // 输出文件
            String contentType = "application/octet-stream";
            response.setContentType(contentType);
            response.setHeader("Content-disposition", "attachment;filename=\"result.xls\"");
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new BufferedInputStream(new FileInputStream("result.xls"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                os = new BufferedOutputStream(response.getOutputStream());
                byte[] buffer = new byte[4 * 1024];
                int read = 0;
                while ((read = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                os.write(baos.toByteArray());
            } catch (IOException e) {
                // e.printStackTrace();
            } finally {
                if (os != null)
                    os.close();
                if (is != null)
                    is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
