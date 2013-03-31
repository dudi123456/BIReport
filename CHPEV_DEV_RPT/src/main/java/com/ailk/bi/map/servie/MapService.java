package com.ailk.bi.map.servie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ailk.bi.base.util.SQLGenator;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.StringB;
import com.ailk.bi.map.dao.impl.AreaDaoImpl;
import com.ailk.bi.map.dao.impl.ColorDaoImpl;
import com.ailk.bi.map.dao.impl.DataDaoImpl;
import com.ailk.bi.map.dao.impl.MapDaoImpl;
import com.ailk.bi.map.entity.AreaInfo;
import com.ailk.bi.map.entity.ColorInfo;
import com.ailk.bi.map.entity.DataInfo;
import com.ailk.bi.map.entity.MapInfo;
import com.ailk.bi.map.entity.MapTemplate;

public class MapService {

	private String year;
	private String month;
	private String day;
	public String url;
	private String resultTime;
	private String mapid;
	public MapInfo mv;
	public String msg;
	private String arearSql;
	private String arearOneSql;
	private String topCode="";
	private String lowCode="";
	private List<DataInfo> datas = new ArrayList<DataInfo>();// 获得数据信息
	private List<ColorInfo> colors = new ArrayList<ColorInfo>();// 获得地图颜色信息
	private List<AreaInfo> areas = new ArrayList<AreaInfo>();// 获得地市信息

	public MapService(String mapid) {
		this.mapid = mapid;
		mv = new MapDaoImpl().getMapInfo(this.mapid);
	}

	public String getTime(String dateTime) {
		if (dateTime == null || "".equals(dateTime)) {
			int delayDay = Integer.valueOf(mv.getDelay_Day());
			int delayMon = Integer.valueOf(mv.getDelay_Mon());
			Calendar date = Calendar.getInstance();
			date.setTime(new Date());
			date.add(Calendar.DAY_OF_MONTH, -delayDay);
			date.add(Calendar.MONTH, -delayMon);
			year = String.valueOf(date.get(Calendar.YEAR));
			month = String.valueOf(date.get(Calendar.MONTH) + 1);
			if (month.length() == 1) {
				month = "0" + month;
			}
			day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
			if (day.length() == 1) {
				day = "0" + day;
			}
			resultTime = year + month + day;
		} else if (dateTime.length() == 10) {
			year = dateTime.substring(0, 4);
			month = dateTime.substring(5, 7);
			day = dateTime.substring(8, 10);
			resultTime = year + month + day;
		} else {
			resultTime = dateTime;
		}
		return resultTime;
	}

	private List<String> getRoleList(String roleStr, List<AreaInfo> areas) {
		String[] arr = roleStr.split(",");
		List<String> roles = new ArrayList<String>();
		if (arr.length > 0 && !("".equals(arr[0]))) {
			for (int i = 0; i < areas.size(); i++) {
				boolean flag = IsExist(areas.get(i).area_Id, arr);
				if (!flag) {
					roles.add(areas.get(i).area_Id);
				}
			}
		}
		return roles;
	}

	private boolean IsExist(String str, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			// 如果在地域表中存在，返回true
			if (str != null && str.equals(arr[i])) {
				return true;
			}
		}
		return false;

	}

	// 判断该数据的id是否在权限roles集合中出现（roles中存放的是没有权限的地域ID，所以要过滤掉）
	private boolean IsExistRoles(List<String> roles, String cityId) {
		for (int i = 0; i < roles.size(); i++) {
			if (cityId.equals(roles.get(i))) {
				return true;
			}
		}
		return false;
	}

	// 判断该数据的id是否在地域areas集合中出现（如果数据信息中的id不是改地域表中的信息，则过滤调该条数据）
	private boolean IsExistAreas(List<AreaInfo> areas, String cityId) {
		for (int i = 0; i < areas.size(); i++) {
			if (cityId.equals(areas.get(i).getArea_Id())) {
				return true;
			}
		}
		return false;
	}

	private List<DataInfo> InitData(List<DataInfo> datas, List<String> roles,
			List<AreaInfo> areas) {
		List<DataInfo> newDatas = new ArrayList<DataInfo>();
		if (datas != null) {

			for (int i = 0; i < datas.size(); i++) {
				// 第一步，过滤掉没有权限的对象
				boolean flagRole = IsExistRoles(roles, datas.get(i).getAreaId());
				// 第二步，多滤掉CityID不在地域表中的数据信息
				boolean flagArea = IsExistAreas(areas, datas.get(i).getAreaId());
				if ((!flagRole) && flagArea) {
					newDatas.add(datas.get(i));// 如果没有在权限集合中并且在地域集合中出现，则加入新的数据集合，下一步做排序
				}
			}
		}
		return newDatas;
	}

	private String getCodeId(String cityId) {
		for (int i = 0; i < this.areas.size(); i++) {
			if (cityId.equals(this.areas.get(i).area_Id)) {
				return this.areas.get(i).getCode_Id();
			}
		}
		return null;
	}

	private void mySort(List<DataInfo> datas, String par) {
		double num1 = 0;
		double num2 = 0;
		if (datas.size() > 0) {
			// 对datas集合按照所指定的字段做排序，得到一个倒序排列的集合
			for (int i = 0; i < datas.size(); i++) {
				for (int j = 0; j < datas.size() - i - 1; j++) {
					if ("value1".equals(par)) {
						num1 = Double.parseDouble(datas.get(j).getValue1());
						num2 = Double.parseDouble(datas.get(j + 1).getValue1());
					} else if ("value2".equals(par)) {
						num1 = Double.parseDouble(datas.get(j).getValue2());
						num2 = Double.parseDouble(datas.get(j + 1).getValue2());
					} else if ("value3".equals(par)) {
						num1 = Double.parseDouble(datas.get(j).getValue3());
						num2 = Double.parseDouble(datas.get(j + 1).getValue3());
					} else if ("value4".equals(par)) {
						num1 = Double.parseDouble(datas.get(j).getValue4());
						num2 = Double.parseDouble(datas.get(j + 1).getValue4());
					}
					if (num1 > num2) {
						DataInfo dd = datas.get(j);
						datas.set(j, datas.get(j + 1));
						datas.set(j + 1, dd);
					}
				}
			}
			List<String> codeList = new ArrayList<String>();
			for (int i = 0; i < datas.size(); i++) {
				String codeId = getCodeId(datas.get(i).getAreaId());
				if (codeId != null) {
					codeList.add(codeId);
				}
			}
			// 初始化红色小旗
			int countRed = 1;
			if ("true".equals(mv.is_red_flag)) {
				for (int i = codeList.size() - 1; i >= 0; i--) {
					this.topCode += codeList.get(i) + ",";
					countRed++;
					if (countRed > Integer.parseInt(mv.getRed_flag_num())) {
						i = -1;
					}
				}
			}
			// 初始化绿色小旗
			int countGreen = 1;
			if ("true".equals(mv.is_green_flag)) {
				for (int i = 0; i < codeList.size(); i++) {
					this.lowCode += codeList.get(i) + ",";
					countGreen++;
					if (countGreen > Integer.parseInt(mv.getGreen_flag_num())) {
						i = codeList.size() + 1;
					}
				}
			}
		}
	}

	public MapTemplate getMapXML(String title,String dateTime, String condition,
			String roleIds) {
		String sql = mv.getData_Sql();
		sql = StringB.replace(sql, "?", condition);
		System.out.println("map data sql=" + sql);
		try {
			arearSql = SQLGenator.genSQL("M003F", mv.getCity_id());
			arearOneSql = SQLGenator.genSQL("M004F", mv.getCity_id());
		} catch (AppException e) {
			e.printStackTrace();
		}
		String[] arr = mv.show_Title.split(",");
		String dataField = "value1,value2,value3,value4";
		String showNum = String.valueOf(arr.length);
		List<DataInfo> mydatas = new DataDaoImpl().getData(sql);// 获得数据信息
		this.colors = new ColorDaoImpl().getAllColor(mv.getMap_Id());// 获得地图颜色信息
		this.areas = new AreaDaoImpl().getArea(arearSql);// 获得地市信息
		AreaInfo area = new AreaDaoImpl().getAreaById(arearOneSql);// 获得省份信息
		List<String> roles = getRoleList(roleIds, areas);
		this.datas = InitData(mydatas, roles, this.areas);
		mySort(this.datas, "value1");
		String ShowTime =dateTime.substring(0,4)+"年"+dateTime.substring(4,6)+"月"+dateTime.substring(6,8)+"日";
		MapTemplate mapTemplate = new MapTemplate(mv.getIs_button(),title,ShowTime,this.topCode, this.lowCode,
				roles, mv.getIs_drift(), area.getArea_Id(),
				area.getArea_Name(), this.colors, this.datas, this.areas,
				mv.getCompar_Parame(), showNum, mv.getShow_Title(), dataField);
		return mapTemplate;
	}
	public String getMapid() {
		return mapid;
	}
	public void setMapid(String mapid) {
		this.mapid = mapid;
	}
}
