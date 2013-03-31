package com.ailk.bi.common.app;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

//import com.ailk.bi.adhoc.struct.AdhocViewQryStruct;
//import com.ailk.bi.adhoc.util.AdhocConstant;

import java.lang.reflect.*;
import java.util.HashMap;

/**
 * <p>
 * Title: iBusiness
 * </p>
 * <p>
 * Description: 此类用于Web的工具，从页面直接获得JavaBean对象
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppWebUtil {
	private final static String JBEAN_CONNECT = "__";

	private final static String SERVLET_REQ_TYPENAME = "SENDSEND_MMSSGG";

	/**
	 * 从页面获得对象数组
	 * 
	 * @param request
	 * @param name
	 *            在页面中的对象名字.
	 * @param obj
	 *            对象数组中的对象单元的类.
	 * @return 从页面获得的对象数组.
	 * 
	 *         <pre>
	 *   例如：
	 *   #定义JavaBean类ObjA
	 *   class ObjA {
	 *     public String strName; //注意：变量一定要为public的
	 *     public int iAge;
	 *   }
	 *  
	 *   #页面中的一段html为
	 *   ...
	 *   &lt;form ...&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__strName&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__iAge&quot;&gt;
	 *   ...
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__strName&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__iAge&quot;&gt;
	 *   ...
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__strName&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__iAge&quot;&gt;
	 *   ...
	 *   &lt;/form&gt;
	 *   #在servlet中调用为
	 *   ...
	 *  
	 *   ObjA[] objs=AppWebUtil.getHtmlObjects(request, &quot;Test&quot;, ObjA.class);
	 *   //即可从页面直接获得objA的对象数组
	 *   ...
	 * </pre>
	 * 
	 *         注意：使用此函数时，Object对象的属性不可以为数组，否则函数将不会进行处理，直接设置该成员变量为空
	 */
	public static Object[] getHtmlObjects(HttpServletRequest request,
			String name, Class aClass) throws AppException {
		int iGetNum = 0;
		if (aClass == null)
			return null;

		// 不对数组类型进行处理
		if (aClass.getName().startsWith("["))
			return null;

		Object[] retObj = null;

		// 对于String 直接返回
		if ("java.lang.String".equals(aClass.getName())) {
			return getHtmlObjects(request, name);
		}

		try {
			Field[] fields = aClass.getFields();

			// 首先进行页面取值
			HashMap hm = new HashMap();
			for (int i = 0; i < fields.length; i++) {
				String paraName = name + JBEAN_CONNECT + fields[i].getName();
				// Debug.println("paraName="+paraName);

				String[] valString = request.getParameterValues(paraName);
				if (fields[i].getType().getName().startsWith("[")) // 如果成员变量有数组直接设置为空
					valString = null;

				hm.put(fields[i].getName(), valString);
				if (valString != null && valString.length > iGetNum)
					iGetNum = valString.length;
			}
			if (iGetNum == 0) // 没有取得页面的输入信息
				return null;

			// 构造返回对象数组
			retObj = (Object[]) Array.newInstance(aClass, iGetNum);

			String fieldName = null;
			int iRet = 0;
			for (int i = 0; i < iGetNum; i++) {
				retObj[i] = aClass.newInstance();
				Iterator it = hm.keySet().iterator();
				while (it.hasNext()) {
					fieldName = (String) it.next();
					String[] tmpValues = (String[]) hm.get(fieldName);
					if (tmpValues != null && tmpValues.length > i) {
						tmpValues[i] = StringB.toGB(tmpValues[i], null);
						iRet = setFieldValueFromObject(request, name,
								aClass.getField(fieldName), retObj[i],
								tmpValues[i]);
						if (iRet == -1)
							return null;
					}
				}
			}

		} catch (Exception ex) {
			throw new AppException(ex);
		}

		return retObj;
	}

	/**
	 * 从页面获得对象数组
	 * 
	 * @param request
	 * @param name
	 *            在页面中的对象名字
	 * @param obj
	 *            对象数组中的对象单元
	 * @return 从业面中获得的对象数组
	 */
	public static Object[] getHtmlObjects(HttpServletRequest request,
			String name, Object obj) throws AppException {
		Class objClass = obj.getClass();
		return getHtmlObjects(request, name, objClass);
	}

	/**
	 * 从页面获得对象
	 * 
	 * @param request
	 * @param name
	 *            在页面中的对象名字
	 * @param obj
	 *            要获得的对象
	 * 
	 *            <pre>
	 *   例如：
	 *   #定义JavaBean类ObjA
	 *   class ObjA {
	 *     public String strName; //注意：变量一定要为public的，才能读
	 *     public int iAge;
	 *     public ObjB b;  //建议：为页面书写灵活方便，在非必要情况下最好不要进行这种变量类型的定义
	 *     public String[] descs=null;
	 *   }
	 *  
	 *   class ObjB {
	 *     public String wk=null;
	 *     public String popo=null;
	 *   }
	 *  
	 *   #页面中的一段html为
	 *   ...
	 * <p>
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__strName&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__iAge&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__b__wk&quot;&gt;
	 *    &lt;input type=&quot;text&quot; name=&quot;Test__b__popo&quot;&gt;
	 *  
	 *    描述一&lt;input type=&quot;text&quot; name=&quot;Test__descs&quot;&gt;
	 *    描述二&lt;input type=&quot;text&quot; name=&quot;Test__descs&quot;&gt;
	 *    描述三&lt;input type=&quot;text&quot; name=&quot;Test__descs&quot;&gt;
	 * </p>
	 *   ...
	 *  
	 *   #在servlet中调用为
	 *   ...
	 *   ObjA obj=new ObjA();
	 *   AppWebUtil.getHtmlObject(request, &quot;Test&quot;, obj);
	 *   //即可从页面直接获得obj
	 *   ...
	 * </pre>
	 */
	public static int getHtmlObject(HttpServletRequest request, String name,
			Object obj) throws AppException {
		if (obj == null)
			throw new AppException("第三个参数object不可为空！");

		/*
		 * if(obj instanceof AdhocViewQryStruct){ // String strRoot =
		 * StringB.NulltoBlank
		 * ((String)request.getSession().getAttribute(AdhocConstant
		 * .ADHOC_ROOT_WEBKEYS)); if (strRoot.length()>0){ String sql =
		 * " select con_type,qry_name from ui_adhoc_condition_meta where  con_id "
		 * + "in(select meta_code from ui_adhoc_rule_group_prop " +
		 * "where group_id in (select group_id from ui_adhoc_rule_hoc_group " +
		 * "where adhoc_id in ('" + strRoot + "')) " +
		 * "and meta_type = '1') order by con_id;";
		 * 
		 * 
		 * }
		 * 
		 * }else{
		 * 
		 * }
		 */

		try {
			Class objClass = obj.getClass();
			Field[] fields = objClass.getFields();
			int iret;
			for (int i = 0; i < fields.length; i++) {

				String fTypeName = fields[i].getType().getName(); // 获得字段的类型名称
				String fName = fields[i].getName(); // 获得字段的名称
				// Debug.println("fTypeName="+fTypeName+" fName="+fName);
				if (fTypeName.startsWith("[["))
					continue; // 二维或者二维以上数组将不处理

				if (fTypeName.startsWith("[")) // 判断数组单元的类型
				{
					// 构造单元对象的class
					Object objs = null;

					if (fTypeName.equals("[D")) // double
					{
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);

						if (objStrs == null)
							objs = null;
						else {
							double[] bs = new double[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Double.valueOf(objStrs[k])
										.doubleValue();
							}
							objs = bs;
						}
					} else if (fTypeName.equals("[I")) // int
					{
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						if (objStrs == null)
							objs = null;
						else {
							int[] bs = new int[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Integer.parseInt(objStrs[k]);
							}
							objs = bs;
						}

					} else if (fTypeName.equals("[F")) // float
					{
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						if (objStrs == null)
							objs = null;
						else {
							float[] bs = new float[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Double.valueOf(objStrs[k]).floatValue();
							}
							objs = bs;
						}

					} else if (fTypeName.equals("[J")) // long
					{
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						if (objStrs == null)
							objs = null;
						else {
							long[] bs = new long[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Long.parseLong(objStrs[k]);
							}
							objs = bs;
						}

					} else if (fTypeName.equals("[S")) // short
					{
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						if (objStrs == null)
							objs = null;
						else {
							short[] bs = new short[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Short.parseShort(objStrs[k]);
							}
							objs = bs;
						}
					} else if (fTypeName.lastIndexOf("[Z") >= 0) { // boolean
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						if (objStrs == null)
							objs = null;
						else {
							boolean[] bs = new boolean[objStrs.length];
							for (int k = 0; k < objStrs.length; k++) {
								bs[k] = Boolean.getBoolean(objStrs[k]);
							}
							objs = bs;
						}
					} else if (fTypeName.lastIndexOf("java.lang.String") >= 0) {
						String[] objStrs = getHtmlObjects(request, name
								+ JBEAN_CONNECT + fName);
						objs = objStrs;
					} else if (fTypeName.startsWith("[L")) {
						// typeName要去掉头和尾巴，头＝[L 尾巴＝;
						Class fClass = Class.forName(fTypeName.substring(2,
								fTypeName.length() - 1));
						objs = getHtmlObjects(request, name + JBEAN_CONNECT
								+ fName, fClass);
					} else
						objs = null; // 其它非类的类型数组暂时不予支持
					setFieldValueFromObject(request, name, fields[i], obj, objs);
					continue;
				}
				String valString = StringB.toGB(request.getParameter(name
						+ JBEAN_CONNECT + fName));

				// System.out.println("para=" + name + JBEAN_CONNECT + fName
				// + " valString=" + valString);
				if (fName.equals("")) {

				}
				iret = setFieldValueFromObject(request, name, fields[i], obj,
						valString);
				if (iret == -1)
					return -1;
			}
			return 0;

		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	/**
	 * 从页面中获得指定paramter的String数组
	 * 
	 * @param request
	 * @param name
	 * @return
	 * @throws AppException
	 */
	public static String[] getHtmlObjects(HttpServletRequest request,
			String name) throws AppException {
		try {
			// 从页面获得一个String数组
			// String[] strVal={"1","2"}; //测试数据
			String[] strVal = request.getParameterValues(name);
			if (strVal != null) {
				// 进行汉字的转换
				for (int i = 0; i < strVal.length; i++)
					strVal[i] = StringB.toGB(strVal[i]);
			}
			return strVal;
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	/**
	 * 用串设置字段的值
	 * 
	 * @param field
	 * @param obj
	 * @param val
	 */
	private static int setFieldValueFromObject(HttpServletRequest request,
			String fatherName, Field field, Object obj, Object val)
			throws AppException {
		String strVal = null;
		if (val == null)
			return 0;
		try {
			String typeName = field.getType().getName();
			// Log.debug("typeName="+typeName+" strVal="+val);
			// Debug.println("typeName="+typeName);
			if (typeName.equals("double")) {
				strVal = (String) val;
				if (strVal != null && !"".equals(strVal))
					field.setDouble(obj, Double.valueOf(strVal).doubleValue());
			} else if (typeName.equals("float")) {
				strVal = (String) val;
				if (strVal != null && !"".equals(strVal))
					field.setFloat(obj, Double.valueOf(strVal).floatValue());
			} else if (typeName.equals("int")) {
				strVal = (String) val;

				if (strVal != null && !"".equals(strVal))
					field.setInt(obj, Integer.parseInt(strVal));
			} else if (typeName.equals("long")) {
				strVal = (String) val;
				if (strVal != null && !"".equals(strVal))
					field.setLong(obj, Long.parseLong(strVal));
			} else if (typeName.equals("short")) {
				strVal = (String) val;
				if (strVal != null && !"".equals(strVal))
					field.setShort(obj, Short.parseShort(strVal));
			} else if (typeName.equals("boolean")) {
				strVal = (String) val;
				if (strVal != null && !"".equals(strVal))
					field.setBoolean(obj, Boolean.getBoolean(strVal));
			} else if (typeName.equals("java.lang.String")) {
				strVal = (String) val;
				field.set(obj, strVal);
			} else if (typeName.startsWith("[")) {
				field.set(obj, val);
			} else {
				/*
				 * //如果是递归定义的系统将不处理 String
				 * tName=typeName.substring(typeName.lastIndexOf(".")+1);
				 * //Debug.println("tName="+tName); if(
				 * fatherName.lastIndexOf(tName)>-1 ) { field.set(obj,null );
				 * return 1; }
				 */
				String fName = field.getName(); // 获得字段的名字，而不是字段类型的名字
				Object v = Class.forName(typeName).newInstance();
				getHtmlObject(request, fatherName + JBEAN_CONNECT + fName, v);
				field.set(obj, v);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AppException(ex);
		}
		return 0;
	}

	/**
	 * 生成令牌串
	 */
	public static String genToken() {
		String strToken = null;
		strToken = MD5.getInstance().getMD5ofStr(
				System.currentTimeMillis() + "WKPOPO");
		return strToken;
	}

	/**
	 * 清楚判断隐藏信息，以便下次提交时不再判断重复提交
	 * 
	 * @param request
	 */
	public static void clearRptToken(HttpSession session) {
		if (session == null)
			return;
		session.removeAttribute(AppConst.TOKEN_STRING);
	}

	/**
	 * 用令牌方式解决页面重复提交的问题
	 * 
	 * @param request
	 * @return 如果为true则说明该页面重复提交，为false则不必进行处理
	 * 
	 *         <pre>
	 *   提交的页面的form内要加入此行
	 *   &lt;BIBM:RptCommitTag /&gt;
	 * </pre>
	 */
	public static boolean isRptCommit(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null)
			return false;

		String tokenString = (String) session
				.getAttribute(AppConst.TOKEN_STRING);
		// 重新生成
		session.setAttribute(AppConst.TOKEN_STRING, genToken());
		if (tokenString == null || "".equals(tokenString)) {
			return false;
		}

		// 如果session中存在Token信息，那么读取页面的hidden信息
		String hidenString = request.getParameter(AppConst.TOKEN_STRING);
		if (hidenString == null || "".equals(hidenString)) {
			// 不需要判断重复提交的页面
			return false;
		}

		// 重复提交
		if (!hidenString.equals(tokenString)) {
			return true;
		}
		return false;
	}

	/**
	 * 向一个servlet中发送一个串信息，该断函数可以在应用程序或者EJB，MJB中调用，通常它与
	 * 下面的函数servletDealMsg一同使用，servletDealMsg函数在要接收信息的servlet中调用
	 * 
	 * @param url
	 *            servlet的URL,如： http://localhost:7001/myweb/testservlet
	 * @param msg
	 *            要发送的消息
	 * @return servlet处理的应答结果 true成功处理
	 */
	public static boolean sendHttpServletMsg(String httpUrl, String msg)
			throws AppException {
		String stringValue = null;
		try {
			// 设置HTTP请求的类型，表明该请求是要发送数据
			int iPos = httpUrl.indexOf("?");
			if (iPos > 0) {
				httpUrl = httpUrl.substring(0, iPos + 1) + SERVLET_REQ_TYPENAME
						+ "=1&" + httpUrl.substring(iPos + 1);
			} else {
				httpUrl = httpUrl + "?" + SERVLET_REQ_TYPENAME + "=1";
			}

			Debug.println("Attempting to connect to " + httpUrl);

			// Get the server URL
			java.net.URL url = new java.net.URL(httpUrl);

			// Attempt to connect to the host
			java.net.URLConnection con = url.openConnection();

			// Initialize the connection
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);

			// Data will always be written to a byte array buffer so
			// that we can tell the server the length of the data
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

			// Create the output stream to be used to write the
			// data to our buffer
			DataOutputStream out = new DataOutputStream(byteOut);

			Debug.println("Writing Send data :" + msg);

			// Write the test data
			/*
			 * out.writeBoolean(true); out.writeByte(1); out.writeChar(2);
			 * out.writeShort(3); out.writeInt(4); out.writeFloat(5);
			 * out.writeDouble(6);
			 */
			out.writeUTF(msg);

			// Flush the data to the buffer
			out.flush();

			// Get our buffer to be sent
			byte buf[] = byteOut.toByteArray();

			// Set the content that we are sending
			con.setRequestProperty("Content-type", "application/octet-stream");

			// Set the length of the data buffer we are sending
			con.setRequestProperty("Content-length", "" + buf.length);

			// Get the output stream to the server and send our
			// data buffer
			DataOutputStream dataOut = new DataOutputStream(
					con.getOutputStream());
			// out.write(buf, 0, buf.length);
			dataOut.write(buf);

			// Flush the output stream and close it
			dataOut.flush();
			dataOut.close();

			Debug.println("Reading response");

			// Get the input stream we can use to read the response
			DataInputStream in = new DataInputStream(con.getInputStream());

			// Read the data from the server
			/*
			 * boolean booleanValue = in.readBoolean(); byte byteValue =
			 * in.readByte(); char charValue = in.readChar(); short shortValue =
			 * in.readShort(); int intValue = in.readInt(); float floatValue =
			 * in.readFloat(); double doubleValue = in.readDouble();
			 */
			stringValue = in.readUTF();

			// Close the input stream
			in.close();

			Debug.println("Data read: " + stringValue);
			return ("OKOK".equals(stringValue));
		} catch (Exception ex) {
			throw new AppException(ex);
		}
	}

	/**
	 * 接收客户端发送信息的servlet程序断，它与sendHttpServletMsg对应使用，sendHttpServletMsg应用
	 * 在客户端或者EJB中的调用者中
	 * 
	 * @param req
	 *            httpRequest
	 * @param resp
	 *            httpResponse
	 * @return 返回值0 不需要处理 1可以处理并处理成功 2请求参数为空或者不正确
	 * @throws AppException
	 */
	public static int servletDealMsg(HttpServletRequest req,
			HttpServletResponse resp, StringBuffer retString)
			throws ServletException, java.io.IOException {
		String sendType = req.getParameter(SERVLET_REQ_TYPENAME);
		if (!"1".equals(sendType)) // 是不是处理接收消息的请求
			return 0;

		if (req == null || resp == null || retString == null) {
			return 2;
		}

		DataInputStream in = new DataInputStream(req.getInputStream());

		// We'll be sending binary data back to the client so
		// set the content type appropriately
		resp.setContentType("application/octet-stream");

		// Data will always be written to a byte array buffer so
		// that we can tell the client the length of the data
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

		// Create the output stream to be used to write the
		// data to our buffer
		DataOutputStream out = new DataOutputStream(byteOut);

		// Read the data from the client.
		/*
		 * boolean booleanValue = in.readBoolean(); byte byteValue =
		 * in.readByte(); char charValue = in.readChar(); short shortValue =
		 * in.readShort(); int intValue = in.readInt(); float floatValue =
		 * in.readFloat(); double doubleValue = in.readDouble();
		 */
		String stringValue = in.readUTF();
		retString.append(stringValue);
		// Write the data to our internal buffer.
		/*
		 * out.writeBoolean(booleanValue); out.writeByte(byteValue);
		 * out.writeChar(charValue); out.writeShort(shortValue);
		 * out.writeInt(intValue); out.writeFloat(floatValue);
		 * out.writeDouble(doubleValue);
		 */
		out.writeUTF("OKOK");

		// Flush the contents of the output stream to the
		// byte array
		out.flush();

		// Get the buffer that is holding our response
		byte[] buf = byteOut.toByteArray();

		// Notify the client how much data is being sent
		resp.setContentLength(buf.length);

		// Send the buffer to the client
		ServletOutputStream servletOut = resp.getOutputStream();

		// Wrap up
		servletOut.write(buf);
		servletOut.close();
		return 1;
	}
}