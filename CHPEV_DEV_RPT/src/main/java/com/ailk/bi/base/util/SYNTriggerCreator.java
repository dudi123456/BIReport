package com.ailk.bi.base.util;

import java.io.DataOutputStream;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import java.util.regex.Pattern;

@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class SYNTriggerCreator {

	public String createFieldByRS(ResultSet rs) {
		StringBuffer sb = new StringBuffer();
		// String tn=null;
		// String columnTypeName = null;
		String colummName = null;
		try {
			// tn=rs.getTableName();
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				// columnTypeName = metaData.getColumnTypeName(i);
				colummName = metaData.getColumnName(i);
				if (i == 1) {
					sb.append(colummName);
				} else {
					sb.append("," + colummName);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sb.toString();
	}

	public List getPkList(String table, Connection con) {
		int x = table.indexOf('.');

		StringBuffer sb = new StringBuffer(
				"select column_name from all_cons_columns a,all_constraints b ");
		sb.append("where a.owner=b.owner and a.table_name=b.table_name ");
		sb.append("and a.constraint_name=b.constraint_name ");
		sb.append("and b.constraint_type='P' ");
		sb.append("and a.owner like 'DB%' and ");
		sb.append("a.table_name='" + table.substring(x + 1).toUpperCase()
				+ "' and a.owner='" + table.substring(0, x).toUpperCase()
				+ "' ");
		List tl = new ArrayList();
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			rs = con.prepareCall(sb.toString()).executeQuery();
			while (rs.next()) {
				tl.add(rs.getString("column_name"));
			}

			if (tl.size() == 0) {

				StringBuffer sb1 = new StringBuffer(
						"select column_name from all_col_comments a where ");
				sb1.append(" a.owner like 'DB%' and ");
				sb1.append("a.table_name='"
						+ table.substring(x + 1).toUpperCase()
						+ "' and a.owner='"
						+ table.substring(0, x).toUpperCase() + "' ");

				rs1 = con.prepareCall(sb1.toString()).executeQuery();
				while (rs1.next()) {
					tl.add(rs1.getString("column_name"));
				}

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			if (null != rs1) {
				try {
					rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != rs) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tl;
	}

	public String getWhereStr(List list, String prefix) {

		// unix
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				sb.append(list.get(i) + "=''' || :" + prefix + "."
						+ list.get(i));
			} else {
				sb.append(" ||''' and " + list.get(i) + "=''' || :" + prefix
						+ "." + list.get(i));
			}
		}
		sb.append("||'''");
		return sb.toString();
	}

	public String buildTriggerStr(String tableName, ResultSet rs) {
		String fieldStr = createFieldByRS(rs);
		List llll = getPkList(tableName, getConn());

		Calendar current_date = Calendar.getInstance();
		String triggerName = null;

		int tempI = tableName.indexOf('.');
		if (tempI != -1) {

			triggerName = "tri_"
					+ tableName.replaceAll("_", "").toLowerCase()
							.substring(tempI);
		} else {
			triggerName = "tri_" + tableName.replaceAll("_", "").toLowerCase();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("--create date: ").append(
				new SimpleDateFormat("yyyy/MM/dd HH:mm").format(current_date
						.getTime()));
		sb.append("\n").append("--author:");
		sb.append("\n").append("CREATE OR REPLACE TRIGGER " + triggerName);
		sb.append("\n").append("AFTER UPDATE or insert or delete	");
		sb.append("\n").append("ON " + tableName + " 	");
		sb.append("\n").append("REFERENCING NEW AS New OLD AS Old	");
		sb.append("\n").append("FOR EACH ROW	");
		sb.append("\n").append("DECLARE	");
		sb.append("\n").append("sqlt varchar2(2000);	");
		sb.append("\n").append("username varchar(200);	");
		sb.append("\n").append("--阌栾娑堟伅");
		sb.append("\n").append("v_err_msg varchar2(4000);");

		sb.append("\n").append("BEGIN	");
		sb.append("\n").append("   select user into username from dual;	");
		sb.append("\n").append("   if lower(username)<>'db_sync' then	");
		sb.append("\n").append("     IF INSERTING then	");
		sb.append("\n").append(
				"        sqlt:='insert into " + tableName + "@syncdblink ("
						+ fieldStr + ") select " + fieldStr + " from "
						+ tableName + " where " + getWhereStr(llll, "new")
						+ "';");
		// sb.append("\n").append("        sqlt:='insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,''' || sqlt || ''',0)';	");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,sqlt,0);	");
		// sb.append("\n").append("        execute immediate sqlt;	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("     if updating then	");
		sb.append("\n").append(
				"        sqlt:='update " + tableName + "@syncdblink set ("
						+ fieldStr + ") = (select " + fieldStr + " from "
						+ tableName + " where " + getWhereStr(llll, "old")
						+ " ) where " + getWhereStr(llll, "old") + "';");
		// sb.append("\n").append("        sqlt:='insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,''' || sqlt || ''',0)';	");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,sqlt,0);	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("     if deleting then	");
		sb.append("\n").append(
				"        sqlt:='delete from " + tableName
						+ "@syncdblink where " + getWhereStr(llll, "old")
						+ "';");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,sqlt,0);	");
		// sb.append("\n").append("        execute immediate sqlt;	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("      	");
		sb.append("\n").append("   end if;	");
		sb.append("\n").append("   EXCEPTION	");
		sb.append("\n").append("     WHEN OTHERS THEN	");
		sb.append("\n").append(
				"       v_err_msg := '" + tableName
						+ "瑙﹀發寮效父: '||SQLCODE||':'||SQLERRM;");
		sb.append("\n").append("       --SUBSTR(SQLERRM, 1 , 64);");
		sb.append("\n")
				.append("       insert into runlog (id,log,runtime) values (runlogseq.nextval,v_err_msg,SYSTIMESTAMP);");
		sb.append("\n").append("END " + triggerName + ";\n/");

		return sb.toString();
	}

	public String buildTriggerStr11(String tableName, ResultSet rs) {
		String fieldStr = createFieldByRS(rs);
		List llll = getPkList(tableName, getConn());

		Calendar current_date = Calendar.getInstance();
		String triggerName = null;

		int tempI = tableName.indexOf('.');
		if (tempI != -1) {

			triggerName = "tri_"
					+ tableName.replaceAll("_", "").toLowerCase()
							.substring(tempI);
		} else {
			triggerName = "tri_" + tableName.replaceAll("_", "").toLowerCase();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("--create date: ").append(
				new SimpleDateFormat("yyyy/MM/dd HH:mm").format(current_date
						.getTime()));
		sb.append("\n").append("--author:");
		sb.append("\n").append("CREATE OR REPLACE TRIGGER " + triggerName);
		sb.append("\n").append("AFTER UPDATE or insert or delete	");
		sb.append("\n").append("ON " + tableName + " 	");
		sb.append("\n").append("REFERENCING NEW AS New OLD AS Old	");
		sb.append("\n").append("FOR EACH ROW	");
		sb.append("\n").append("DECLARE	");
		sb.append("\n").append("sqlt varchar2(2000);	");
		sb.append("\n").append("username varchar(200);	");
		sb.append("\n").append("--阌栾娑堟伅\nv_err_msg varchar2(4000);");

		sb.append("\n").append("BEGIN	");
		sb.append("\n").append("   select user into username from dual;	");
		sb.append("\n").append("   if lower(username)<>'db_sync' then	");
		sb.append("\n").append("     IF INSERTING then	");
		sb.append("\n").append(
				"        sqlt:='insert into " + tableName + " (" + fieldStr
						+ ") select " + fieldStr + " from " + tableName
						+ "@syncdblink where " + getWhereStr(llll, "new")
						+ "';");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values (logseq.nextval,sqlt,0);	");
		// sb.append("\n").append("        execute immediate sqlt;	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("     if updating then	");
		sb.append("\n").append(
				"        sqlt:='update " + tableName + " set (" + fieldStr
						+ ") = (select " + fieldStr + " from " + tableName
						+ "@syncdblink where " + getWhereStr(llll, "new")
						+ " ) where " + getWhereStr(llll, "old") + "';");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,sqlt,0);	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("     if deleting then	");
		sb.append("\n").append(
				"        sqlt:='delete from " + tableName + " where "
						+ getWhereStr(llll, "old") + "';");
		sb.append("\n")
				.append("        insert into db_sync.synclog(id,logsql,flag) values(logseq.nextval,sqlt,0);	");
		// sb.append("\n").append("        execute immediate sqlt;	");
		sb.append("\n").append("     end if;	");
		sb.append("\n").append("      	");
		sb.append("\n").append("   end if;	");
		sb.append("\n").append("   EXCEPTION	");
		sb.append("\n").append("     WHEN OTHERS THEN	");
		sb.append("\n").append(
				"       v_err_msg := '" + tableName
						+ "瑙﹀發寮效父: '||SQLCODE||':'||SQLERRM;");
		sb.append("\n").append("       --SUBSTR(SQLERRM, 1 , 64);");
		sb.append("\n")
				.append("       insert into runlog (id,log,runtime) values (runlogseq.nextval,v_err_msg,SYSTIMESTAMP);");
		sb.append("\n").append("END " + triggerName + ";\n/");

		return sb.toString();
	}

	private String getVerdictStr(List list) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				sb.append(":new." + list.get(i) + " is null");
			} else {
				sb.append(" or :new." + list.get(i) + " is null");
			}
		}
		// sb.append("||'''''';");
		return sb.toString();
	}

	public String writer_root = "c:\\sql_sync";

	public static void main(String[] args) {

		SYNTriggerCreator sc = new SYNTriggerCreator();
		// sc.neiwang(tableName);
		// sc.zhuanwang(tableName);
		ArrayList list = new ArrayList();
		list.add("db_jbxt.T_BS_BSXX_ZB");
		list.add("db_jbxt.T_BS_CFXX");
		list.add("db_jbxt.T_GY_FJ");
		list.add("db_jbxt.T_BS_BSXX_KZB");
		list.add("db_jbxt.T_BS_YGXX");
		list.add("db_jbxt.T_CL_CBTZ");
		list.add("db_jbxt.T_CL_XXXB");
		list.add("db_jbxt.T_CL_XXZF");
		list.add("db_jbxt.T_JL_YGTZ");
		list.add("db_jbxt.T_JL_GGTZ");
		list.add("db_jbxt.T_JL_ZYWJ");
		list.add("db_jbxt.T_PY_PSBLTZD");
		list.add("db_jbxt.T_GY_FSFW");
		list.add("db_jbxt.T_BS_PSBLXX");
		list.add("db_jbxt.T_FK_YTB_ZB");
		list.add("db_jbxt.T_FK_QIANYAN");
		list.add("db_jbxt.T_FK_CHIBAO");
		list.add("db_jbxt.T_FK_BSQD");

		sc.createTrigger(list);

	}

	public void createTrigger(List tableList) {
		File file = new File(writer_root);
		file.mkdir();
		File file1 = new File(writer_root + "\\鍐呯绣");
		file1.mkdir();
		File file2 = new File(writer_root + "\\涓划绣");
		file2.mkdir();

		for (int i = 0; i < tableList.size(); i++) {
			String tn = (String) tableList.get(i);

			neiwang(tn);
			zhuanwang(tn);
		}
	}

	public void writeFile(String fn, String sql) {
		File file = new File(writer_root + "\\" + fn);
		System.out.println(writer_root + "\\" + fn);
		try {
			// file.mkdirs();
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			DataOutputStream fos = new DataOutputStream(os);

			fos.write(sql.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public boolean neiwang(String tableName) {
		if (tableName == null) {
			return false;
		}
		Connection conn = getConn();
		ResultSet rs = getResult(tableName, conn);
		String ss = buildTriggerStr(tableName, rs);
		writeFile("鍐呯绣\\" + tableName + ".sql", ss);
		System.out.println(ss);
		return true;
	}

	public boolean zhuanwang(String tableName) {
		if (tableName == null) {
			return false;
		}
		Connection conn = getConn();
		ResultSet rs = getResult(tableName, conn);
		String ss = buildTriggerStr11(tableName, rs);
		writeFile("涓划绣\\" + tableName + ".sql", ss);
		System.out.println(ss);
		return true;
	}

	public ResultSet getResult(String tableName, Connection con) {

		String sql = "select * from " + tableName + " where 1=2";
		// System.out.println(sql);
		try {
			// CachedRowSet rs=new CachedRowSet();
			// rs.populate(con.prepareCall(sql).executeQuery());
			ResultSet rs = con.prepareCall(sql).executeQuery();
			return rs;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	private static Connection con = null;

	public Connection getConn() {
		if (con != null)
			return con;
		String dbUrl = "jdbc:oracle:thin:@10.15.0.123:1521:yjdb";
		// theUser涓烘暟鎹簱鐢ㄦ埛钖??
		String theUser = "db_jbxt";
		// thePw涓烘暟鎹簱瀵嗙爜
		String thePw = "db_jbxt";
		// 鍑犱釜锁版嵁搴揿彉阅??

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			// 涓巙rl镌囧畾镄勬暟鎹簪寤虹珛杩炴帴
			con = DriverManager.getConnection(dbUrl, theUser, thePw);
			// 阅囩敤Statement杩涜镆ヨ
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

}
