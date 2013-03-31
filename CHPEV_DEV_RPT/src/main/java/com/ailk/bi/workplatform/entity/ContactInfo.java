package com.ailk.bi.workplatform.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Table(name = "MK_CH_CONTACT_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContactInfo {
	private int contact_id;// NUMBER(10) N
	private int order_no;
	private int order_type;// NUMBER
	private String cust_id;// VARCHAR2(20)
	private String cust_name;// VARCHAR2(50)
	private String cust_level;// VARCHAR2(10)
	private String cust_group_id;// VARCHAR2(10)
	private String gender;// VARCHAR2(1)
	private String user_id;// VARCHAR2(20)
	private String serv_number;// VARCHAR2(20)
	private int activity_id;// NUMBER(10)
	private String activity_name;// VARCHAR2(100)
	private int script_id;// NUMBER(10)
	private int interview_nature;// NUMBER
	private int interview_type;// NUMBER
	private int interview_state;// NUMBER
	private int noreply_reason;// NUMBER
	private int need_repeat;// NUMBER
	private Date next_contact_date;// DATE
	private int pleased_state;// NUMBER
	private int interest;// NUMBER
	private int away_trend;// NUMBER
	private int away_reason;// NUMBER
	private int callout_type;// NUMBER
	private String callout_number;// VARCHAR2(20)
	private Date contact_date;// DATE
	private String contact_content;// VARCHAR2(500)
	private int callin_type;// NUMBER
	private String callin_number;// VARCHAR(20)
	private String performer_id;// VARCHAR2(50)
	private String performer_name;// VARCHAR2(50)
	private int contactMode;//CONTACT_MODE
	@Column(name = "ACTIVITY_ID", nullable = true)
	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	@Column(name = "ACTIVITY_NAME", nullable = true)
	public String getActivity_name() {
		return activity_name;
	}
	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}
	@Column(name = "CONTACT_MODE", nullable = true)
	public int getContactMode() {
		return contactMode;
	}
	public void setContactMode(int contactMode) {
		this.contactMode = contactMode;
	}
	@Column(name = "ORDER_NO", nullable = true)
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
	@Id
	@Column(name = "C_ID", length = 100, nullable = false)
	@SequenceGenerator(name = "SEQ_CONTACT_ID", sequenceName = "MK_CH_CONTACT_INFO$SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONTACT_ID")
	public int getContact_id() {
		return contact_id;
	}
	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}
	@Column(name = "ORDER_TYPE", nullable = true)
	public int getOrder_type() {
		return order_type;
	}
	public void setOrder_type(int order_type) {
		this.order_type = order_type;
	}
	@Column(name = "CUST_ID", nullable = true)
	public String getCust_id() {
		return cust_id;
	}
	public void setCust_id(String cust_id) {
		this.cust_id = cust_id;
	}
	@Column(name = "CUST_NAME", nullable = true)
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	@Column(name = "CUST_LEVEL", nullable = true)
	public String getCust_level() {
		return cust_level;
	}
	public void setCust_level(String cust_level) {
		this.cust_level = cust_level;
	}
	@Column(name = "CUST_GROUP_ID", nullable = true)
	public String getCust_group_id() {
		return cust_group_id;
	}
	public void setCust_group_id(String cust_group_id) {
		this.cust_group_id = cust_group_id;
	}
	@Column(name = "GENDER", nullable = true)
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Column(name = "USER_ID", nullable = true)
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	@Column(name = "SERV_NUMBER", nullable = true)
	public String getServ_number() {
		return serv_number;
	}
	public void setServ_number(String serv_number) {
		this.serv_number = serv_number;
	}

	@Column(name = "SCRIPT_ID", nullable = true)
	public int getScript_id() {
		return script_id;
	}
	public void setScript_id(int script_id) {
		this.script_id = script_id;
	}
	@Column(name = "INTERVIEW_NATURE", nullable = true)
	public int getInterview_nature() {
		return interview_nature;
	}
	public void setInterview_nature(int interview_nature) {
		this.interview_nature = interview_nature;
	}
	@Column(name = "INTERVIEW_TYPE", nullable = true)
	public int getInterview_type() {
		return interview_type;
	}
	public void setInterview_type(int interview_type) {
		this.interview_type = interview_type;
	}
	@Column(name = "INTERVIEW_STATE", nullable = true)
	public int getInterview_state() {
		return interview_state;
	}
	public void setInterview_state(int interview_state) {
		this.interview_state = interview_state;
	}
	@Column(name = "NOREPLY_REASON", nullable = true)
	public int getNoreply_reason() {
		return noreply_reason;
	}
	public void setNoreply_reason(int noreply_reason) {
		this.noreply_reason = noreply_reason;
	}
	@Column(name = "NEED_REPEAT", nullable = true)
	public int getNeed_repeat() {
		return need_repeat;
	}
	public void setNeed_repeat(int need_repeat) {
		this.need_repeat = need_repeat;
	}
	@Column(name = "NEXT_CONTACT_DATE", nullable = true)
	public Date getNext_contact_date() {
		return next_contact_date;
	}
	public void setNext_contact_date(Date next_contact_date) {
		this.next_contact_date = next_contact_date;
	}
	@Column(name = "PLEASED_STATE", nullable = true)
	public int getPleased_state() {
		return pleased_state;
	}
	public void setPleased_state(int pleased_state) {
		this.pleased_state = pleased_state;
	}
	@Column(name = "INTEREST", nullable = true)
	public int getInterest() {
		return interest;
	}
	public void setInterest(int interest) {
		this.interest = interest;
	}
	@Column(name = "AWAY_TREND", nullable = true)
	public int getAway_trend() {
		return away_trend;
	}
	public void setAway_trend(int away_trend) {
		this.away_trend = away_trend;
	}
	@Column(name = "AWAY_REASON", nullable = true)
	public int getAway_reason() {
		return away_reason;
	}
	public void setAway_reason(int away_reason) {
		this.away_reason = away_reason;
	}
	@Column(name = "CALLOUT_TYPE", nullable = true)
	public int getCallout_type() {
		return callout_type;
	}
	public void setCallout_type(int callout_type) {
		this.callout_type = callout_type;
	}
	@Column(name = "CALLOUT_NUMBER", nullable = true)
	public String getCallout_number() {
		return callout_number;
	}
	public void setCallout_number(String callout_number) {
		this.callout_number = callout_number;
	}
	@Column(name = "CONTACT_DATE", nullable = true)
	public Date getContact_date() {
		return contact_date;
	}
	public void setContact_date(Date contact_date) {
		this.contact_date = contact_date;
	}
	@Column(name = "CONTACT_CONTENT", nullable = true)
	public String getContact_content() {
		return contact_content;
	}
	public void setContact_content(String contact_content) {
		this.contact_content = contact_content;
	}
	@Column(name = "CALLIN_TYPE", nullable = true)
	public int getCallin_type() {
		return callin_type;
	}
	public void setCallin_type(int callin_type) {
		this.callin_type = callin_type;
	}
	@Column(name = "CALLIN_NUMBER", nullable = true)
	public String getCallin_number() {
		return callin_number;
	}
	public void setCallin_number(String callin_number) {
		this.callin_number = callin_number;
	}
	@Column(name = "PERFORMER_ID", nullable = true)
	public String getPerformer_id() {
		return performer_id;
	}
	public void setPerformer_id(String performer_id) {
		this.performer_id = performer_id;
	}
	@Column(name = "PERFORMER_NAME", nullable = true)
	public String getPerformer_name() {
		return performer_name;
	}
	public void setPerformer_name(String performer_name) {
		this.performer_name = performer_name;
	}
}
