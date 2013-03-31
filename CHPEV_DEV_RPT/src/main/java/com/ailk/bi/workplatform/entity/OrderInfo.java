package com.ailk.bi.workplatform.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ailk.bi.marketing.entity.ActivityInfo;
import com.ailk.bi.marketing.entity.ChannleInfo;
import com.ailk.bi.marketing.entity.ProjectInfo;
import com.ailk.bi.marketing.entity.TacticInfo;

@Entity
@Table(name = "MK_CH_ORDER_INFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderInfo {

	private int order_no;// NUMBER N
	private int order_type;// NUMBER Y
	private String cust_id;// VARCHAR2(20) Y
	private String cust_name;// VARCHAR2(50) Y
	private String cust_level;// VARCHAR2(10) Y
	private String cust_group_id;// VARCHAR2(10) Y
	private String user_id;// VARCHAR2(20) Y
	private String serv_number;// VARCHAR2(20) Y
	private String cust_svc_mgr_id;// VARCHAR2(50) Y
	private String area_code;// VARCHAR2(50) Y
	private String town_code;// VARCHAR2(50) Y
	private String grid;// VARCHAR2(50) Y
	private ProjectInfo projectInfo;// NUMBER Y
	private ActivityInfo activityInfo;// NUMBER Y
	private TacticInfo tacticInfo;// NUMBER Y
	private int contact_id;// NUMBER Y
	private int contact_step;// NUMBER Y
	private int contact_mode;// NUMBER Y
	private String script_id;// NUMBER Y
	private int order_state;// NUMBER Y
	private int prority;// NUMBER Y
	private ChannleInfo channelInfo;// VARCHAR2(50) Y
	private UserInfo performer_id;// VARCHAR2(50) Y
	private int outer_channel;// NUMBER Y
	private ChannleInfo oldChannelInfo;// VARCHAR2(50) Y
	private String old_performer_id;// VARCHAR2(50) Y
	private Date createdate;// DATE Y
	private Date contact_start_date;// DATE Y
	private int time_limit;// NUMBER Y
	private int overdue;// NUMBER Y
	private int perform_state;// NUMBER Y
	private Date next_contact_date;// DATE Y
	private int multi_contact;// NUMBER Y
	private int frequency;// NUMBER Y
	private int present_state;// NUMBER Y
	private String other_info;// VARCHAR2(2500) Y

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PERFORMER_ID")
	public UserInfo getPerformer_id() {
		return performer_id;
	}
	public void setPerformer_id(UserInfo performer_id) {
		this.performer_id = performer_id;
	}
	//辅助查询条件
	public boolean channleAdmin = false;
	public String setType;//是否将执行人作为查询条件  1：正常查询；2：分配；3：改变
	public Date date01;
	public Date date02;
	public Date date03;
	public Date date04;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "OLD_CHANNEL_ID")
	public ChannleInfo getOldChannelInfo() {
		return oldChannelInfo;
	}
	public void setOldChannelInfo(ChannleInfo oldChannelInfo) {
		this.oldChannelInfo = oldChannelInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CHANNEL_ID")
	public ChannleInfo getChannelInfo() {
		return channelInfo;
	}
	public void setChannelInfo(ChannleInfo channelInfo) {
		this.channelInfo = channelInfo;
	}
	@Id
	@Column(name = "ORDER_NO", length = 10, nullable = false)
	public int getOrder_no() {
		return order_no;
	}
	public void setOrder_no(int order_no) {
		this.order_no = order_no;
	}
	@Column(name = "ORDER_TYPE", nullable = false)
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
	@Column(name = "CUST_SVC_MGR_ID", nullable = true)
	public String getCust_svc_mgr_id() {
		return cust_svc_mgr_id;
	}
	public void setCust_svc_mgr_id(String cust_svc_mgr_id) {
		this.cust_svc_mgr_id = cust_svc_mgr_id;
	}
	@Column(name = "AREA_CODE", nullable = true)
	public String getArea_code() {
		return area_code;
	}
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	@Column(name = "TOWN_CODE", nullable = true)
	public String getTown_code() {
		return town_code;
	}
	public void setTown_code(String town_code) {
		this.town_code = town_code;
	}
	@Column(name = "GRID", nullable = true)
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PROJECT_ID")
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ACTIVITY_ID")
	public ActivityInfo getActivityInfo() {
		return activityInfo;
	}
	public void setActivityInfo(ActivityInfo activityInfo) {
		this.activityInfo = activityInfo;
	}
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "TACTIC_ID")
	public TacticInfo getTacticInfo() {
		return tacticInfo;
	}
	public void setTacticInfo(TacticInfo tacticInfo) {
		this.tacticInfo = tacticInfo;
	}
	@Column(name = "CONTACT_ID", nullable = false)
	public int getContact_id() {
		return contact_id;
	}
	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}
	@Column(name = "CONTACT_STEP", nullable = false)
	public int getContact_step() {
		return contact_step;
	}
	public void setContact_step(int contact_step) {
		this.contact_step = contact_step;
	}
	@Column(name = "CONTACT_MODE", nullable = true)
	public int getContact_mode() {
		return contact_mode;
	}
	public void setContact_mode(int contact_mode) {
		this.contact_mode = contact_mode;
	}
	@Column(name = "SCRIPT_ID", nullable = true)
	public String getScript_id() {
		return script_id;
	}
	public void setScript_id(String script_id) {
		this.script_id = script_id;
	}
	@Column(name = "ORDER_STATE", nullable = false)
	public int getOrder_state() {
		return order_state;
	}
	public void setOrder_state(int order_state) {
		this.order_state = order_state;
	}
	@Column(name = "PRORITY", nullable = false)
	public int getPrority() {
		return prority;
	}
	public void setPrority(int prority) {
		this.prority = prority;
	}


	@Column(name = "OUTER_CHANNEL", nullable = false)
	public int getOuter_channel() {
		return outer_channel;
	}
	public void setOuter_channel(int outer_channel) {
		this.outer_channel = outer_channel;
	}

	@Column(name = "OLD_PERFORMER_ID", nullable = true)
	public String getOld_performer_id() {
		return old_performer_id;
	}
	public void setOld_performer_id(String old_performer_id) {
		this.old_performer_id = old_performer_id;
	}
	@Column(name = "CREATEDATE", nullable = true)
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@Column(name = "CONTACT_START_DATE", nullable = true)
	public Date getContact_start_date() {
		return contact_start_date;
	}
	public void setContact_start_date(Date contact_start_date) {
		this.contact_start_date = contact_start_date;
	}
	@Column(name = "TIME_LIMIT", nullable = false)
	public int getTime_limit() {
		return time_limit;
	}
	public void setTime_limit(int time_limit) {
		this.time_limit = time_limit;
	}
	@Column(name = "OVERDUE", nullable = false)
	public int getOverdue() {
		return overdue;
	}
	public void setOverdue(int overdue) {
		this.overdue = overdue;
	}
	@Column(name = "PERFORM_STATE", nullable = false)
	public int getPerform_state() {
		return perform_state;
	}
	public void setPerform_state(int perform_state) {
		this.perform_state = perform_state;
	}
	@Column(name = "NEXT_CONTACT_DATE", nullable = true)
	public Date getNext_contact_date() {
		return next_contact_date;
	}
	public void setNext_contact_date(Date next_contact_date) {
		this.next_contact_date = next_contact_date;
	}
	@Column(name = "MULTI_CONTACT", nullable = false)
	public int getMulti_contact() {
		return multi_contact;
	}
	public void setMulti_contact(int multi_contact) {
		this.multi_contact = multi_contact;
	}
	@Column(name = "FREQUENCY", nullable = false)
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	@Column(name = "PRESENT_STATE", nullable = false)
	public int getPresent_state() {
		return present_state;
	}
	public void setPresent_state(int present_state) {
		this.present_state = present_state;
	}
	@Column(name = "OTHER_INFO", nullable = true)
	public String getOther_info() {
		return other_info;
	}
	public void setOther_info(String other_info) {
		this.other_info = other_info;
	}

}
