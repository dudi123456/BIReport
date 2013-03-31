package com.ailk.bi.base.table;

import com.ailk.bi.common.event.JBTableBase;

public class SstUserTable extends JBTableBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4766166090446968696L;

	// HERE IS FROM DATABASE
	public String id = ""; // NUMBER->String

	public String region_id = ""; // NUMBER->String

	public String dept_id = ""; // NUMBER->String

	public String name = ""; // VARCHAR->String

	public String login_name = ""; // VARCHAR->String

	public String password = ""; // VARCHAR->String

	public String question = ""; // VARCHAR->String

	public String answer = ""; // VARCHAR->String

	public String gender = ""; // NUMBER->String

	public String position = ""; // VARCHAR->String

	public String phone = ""; // VARCHAR->String

	public String email = ""; // VARCHAR->String

	public String mobile = ""; // VARCHAR->String

	public String need_sms = ""; // NUMBER->String

	public String time_utcoffset = ""; // NUMBER->String

	public String res_int1 = ""; // NUMBER->String

	public String res_int2 = ""; // NUMBER->String

	public String res_int3 = ""; // NUMBER->String

	public String res_int4 = ""; // NUMBER->String

	public String res_char1 = ""; // VARCHAR->String

	public String res_char2 = ""; // VARCHAR->String

	public String res_char3 = ""; // VARCHAR->String

	public String res_char4 = ""; // VARCHAR->String

	// HERE IS USER DEFINE 这一行不要删除！
	public String region_name = "";

	public String dept_name = "";

	public String checked = "";

	public String login_ip = "";

	public String sessionId = "";

	public String min_log_date = "";// 最近日志日期
	public String max_log_date = "";// 最小日志日期
	public String login_num = "";// 登陆次数

	public String login_date = "";// 登陆次数
	public String exit_date = "";// 登陆次数
	public String log_seq = "";// 登陆次数

	protected String getAnswer() {
		return answer;
	}

	protected void setAnswer(String answer) {
		this.answer = answer;
	}

	protected String getChecked() {
		return checked;
	}

	protected void setChecked(String checked) {
		this.checked = checked;
	}

	protected String getDept_id() {
		return dept_id;
	}

	protected void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	protected String getDept_name() {
		return dept_name;
	}

	protected void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	protected String getEmail() {
		return email;
	}

	protected void setEmail(String email) {
		this.email = email;
	}

	protected String getGender() {
		return gender;
	}

	protected void setGender(String gender) {
		this.gender = gender;
	}

	protected String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	protected String getLogin_name() {
		return login_name;
	}

	protected void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	protected String getMobile() {
		return mobile;
	}

	protected void setMobile(String mobile) {
		this.mobile = mobile;
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected String getNeed_sms() {
		return need_sms;
	}

	protected void setNeed_sms(String need_sms) {
		this.need_sms = need_sms;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected String getPhone() {
		return phone;
	}

	protected void setPhone(String phone) {
		this.phone = phone;
	}

	protected String getPosition() {
		return position;
	}

	protected void setPosition(String position) {
		this.position = position;
	}

	protected String getQuestion() {
		return question;
	}

	protected void setQuestion(String question) {
		this.question = question;
	}

	protected String getRegion_id() {
		return region_id;
	}

	protected void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	protected String getRegion_name() {
		return region_name;
	}

	protected void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	protected String getRes_char1() {
		return res_char1;
	}

	protected void setRes_char1(String res_char1) {
		this.res_char1 = res_char1;
	}

	protected String getRes_char2() {
		return res_char2;
	}

	protected void setRes_char2(String res_char2) {
		this.res_char2 = res_char2;
	}

	protected String getRes_char3() {
		return res_char3;
	}

	protected void setRes_char3(String res_char3) {
		this.res_char3 = res_char3;
	}

	protected String getRes_char4() {
		return res_char4;
	}

	protected void setRes_char4(String res_char4) {
		this.res_char4 = res_char4;
	}

	protected String getRes_int1() {
		return res_int1;
	}

	protected void setRes_int1(String res_int1) {
		this.res_int1 = res_int1;
	}

	protected String getRes_int2() {
		return res_int2;
	}

	protected void setRes_int2(String res_int2) {
		this.res_int2 = res_int2;
	}

	protected String getRes_int3() {
		return res_int3;
	}

	protected void setRes_int3(String res_int3) {
		this.res_int3 = res_int3;
	}

	protected String getRes_int4() {
		return res_int4;
	}

	protected void setRes_int4(String res_int4) {
		this.res_int4 = res_int4;
	}

	protected String getTime_utcoffset() {
		return time_utcoffset;
	}

	protected void setTime_utcoffset(String time_utcoffset) {
		this.time_utcoffset = time_utcoffset;
	}

}
