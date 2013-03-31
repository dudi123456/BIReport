package com.ailk.bi.common.taglib;

/**
 * <p>Title: </p>
 * <p>Description:通用的校验tag</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author King
 * @version 1.0
 */
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.ailk.bi.common.app.StringB;

/**
 * 通用的校验tag
 * <p>
 * Title: J2EE Web 程序的框架和底层的定义
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004 04 15
 * </p>
 * <p>
 * Company: Info System Co,Ltd.
 * </p>
 * 
 * @author WISEKING
 * @version 1.0
 *          <p>
 * 
 *          <PRE>
 * 
 * 目前的使用格式为： <BIBM:CommCheckTag warn="1" 可选
 * 如果设置为1时，则出现错误时候将以alert方式显示出来，这一遍在调试阶段使用 checkNames =
 * "from1.cust:=field1=C00>[];field2=C...;;from2.user:=" [必选][校验对象的定义和格式 />
 * 
 * 其中checkNames的格式说明如下:
 * formName.objNameX:=field1=CHKSTR1;field2=CHECSTR2;...fieldn=CHECKSTRn;;formName.objNameY:=...;...
 * 两个formObj之间要用 ;; 来分隔 CHKSTR的格式为：!XLLS[fieldName] 其中 ! 可选，如果输入!则代表该字段为非空的 X
 * 为字段的类型，可取值为： S string D datetime N number I integer F float M money E email T
 * teleNo P 需要校验的密码 U 根据联通电信类型判断的号码 Y 自定义校验函数 LL 可选,为字段的长度，如果输入（必须2位，不足时要左补零） S
 * 可选，与LL同在。为判断符号，可以不输入，如果输入则必须为 <，举例说明 field1=N11< 代表field1必须取长度为11以内的数字串
 * field2=N10 代表field2必须取长度为10的数字串 [fieldName]可选， 输入这个字段的名字，如： field1=!N11<[字段一];field2=N10[字段二]
 * 如果类型为P，则说明当前字段要与与其名字扩展_RPT的字段进行比较，所以要注意比较字段的命名， 例如: form.oper:=pwd=!P06[密码],
 * 可以解释为form.oper__pwd这个字段为长6位的非空的密码，它要与form.oper__pwd_PRT这个字段的值进行比较
 * 如果类型为D，则后面跟的时时间的格式，如: field1=DYYYY/MM/DD:HHMISS,默认日期类型为YYYY/MM/DD
 * 如果类型为U，则后面紧跟的是代码电信类型字段名，如: dev_no=!Uform1.test__tele_type[号码],
 * 注意电信类型字段前要把form名写明 如果类型为Y，则后面进个自己定义的校验函数名，该函数有固定的参数为: field.value 返回值为boolean
 * 如果校验通过则为true, 否则false 例如: form.oper:=fatdog=Ymycheck[肥狗]; 可以理解为 对于
 * form.oper__fatdog这个字段要用自己写的function mycheck(str)这个方法来校验， 如果校验不通过则显示[肥狗]提示信息,
 * 注意提示信息要写在你的自检查函数里面
 * 在form的commit前可调用由它生成的commCheck(spans)方法进行校验，如果返回false则说明校验未通过
 * 参数spans是校验的范围，如果为"ALL"
 * 则全部校验，否则为局部校验，如："[form1.acct][form2.cust]"对以form1.acct__和form2.cust__开头的输入域进行校验，
 * 注意：spans里面定义的一定要以 [formName.objNameX]
 * 格式定义，可以为多个,如:[formName.objNameX][formName.objNameY], 一定不要忘记加中括号[]
 * 
 * 高级用法： 可以将统一from.Obj分成多个部分定义，这样的目的是为了解决同一对象，不同字段的校验，如：
 * form1.acct#1:=acct_name!S40<;; from1.acct#2:=acct_id!S20;; 可以分别定义校验范围，如下：
 * [form1.acct#1][form1.acct#2]
 * 
 * </PRE>
 * 
 *          </p>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommCheck extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -254829471635641259L;

	class CheckElement {
		String frmObjNo = ""; // fromObj的代号码名，一般为了实现局部校验，格式:

		// formName.ObjName#N

		String frmObj = ""; // 校验对象名一般格式 formName.objName

		Vector fields = new Vector(); // 字段

		Vector fNulls = new Vector(); // 是否为空 ！非空

		Vector fTypes = new Vector(); // 字段类型

		Vector fLens = new Vector(); // 字段长度

		Vector fOpers = new Vector(); // 操作类型 如 < = 或者日期格式

		Vector fieldNames = new Vector(); // 字段名字

		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("frmObjNo=").append(frmObjNo);
			sb.append("\nfrmObj=").append(frmObj);
			sb.append("\nfields=").append(fields.toString());
			sb.append("\nfNulls=").append(fNulls);
			sb.append("\nfTypes=").append(fTypes);
			sb.append("\nfLens=").append(fLens);
			sb.append("\nfOpers=").append(fOpers);
			sb.append("\nfieldNames=").append(fieldNames);
			return sb.toString();
		}

		public String err(int i) {
			StringBuffer sb = new StringBuffer();
			sb.append("frmObj=").append(frmObj);
			sb.append("\\nfield=").append(fields.elementAt(i));
			sb.append("\\nfNull=").append(fNulls.elementAt(i));
			sb.append("\\nfType=").append(fTypes.elementAt(i));
			sb.append("\\nfLen=").append(fLens.elementAt(i));
			sb.append("\\nfOper=").append(fOpers.elementAt(i));
			sb.append("\\nfieldName=").append(fieldNames.elementAt(i));
			return sb.toString();
		}

	}

	/**
	 * 定义选择框属性
	 */
	private String checkNames = "";

	private int warn = 0;

	public int doEndTag() throws JspException {

		JspWriter out = pageContext.getOut();
		try {
			out.print("<script src='/WUCOM/web/common/comm_check.js'></script>\n");
			out.print("<script language='JavaScript'>\n");
			out.print("function commCheck(checks)\n");
			out.print("{\n");
			if (checkNames != null && !"".equals(checkNames)) {
				CheckElement[] ces = parseFields(checkNames);
				if (ces == null)
					out.print("  //parse field check info err!\n");
				else {
					for (int i = 0; i < ces.length; i++) {
						CheckElement ce = ces[i];
						out.print("  //print check for " + ce.frmObj + "\n");
						out.print("  if( checks=='ALL' || checks.indexOf('["
								+ ce.frmObjNo + "]')>-1 ){\n");
						out.print(printACheckElement(ce));
						out.print("  }\n");
					}
				}
			}
			out.print("\n  return true;\n\n");
			out.print("}\n");
			out.print("</script>\n");
			out.print("\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return (SKIP_BODY);
	}

	public String getCheckNames() {
		return checkNames;
	}

	public void setCheckNames(String checkNames) {
		this.checkNames = checkNames;
	}

	private CheckElement[] parseFields(String fields) {
		if (fields == null || fields.length() < 1)
			return null;

		Vector v = StringB.parseStringTokenizer(fields, ";;");
		if (v == null) {
			return null;
		}
		CheckElement[] ces = new CheckElement[v.size()];
		for (int i = 0; i < v.size(); i++) {
			ces[i] = parseAField((String) v.elementAt(i));
			if (ces[i] == null)
				return null;
		}
		return ces;
	}

	private String addErr(CheckElement ce, int i) {
		String ct = "";
		if (warn == 1)
			ct = " }catch(e){ alert(\"parse ERROR:" + ce.err(i)
					+ "\");return false; }\n\n";
		else
			ct = " }catch(e){;}\n\n";
		return ct;
	}

	private String printACheckElement(CheckElement ce) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < ce.fields.size(); i++) {
			String para = ce.frmObj + "__" + ce.fields.elementAt(i) + ".value";
			String paraEnaled = "!" + ce.frmObj + "__" + ce.fields.elementAt(i)
					+ ".disabled";
			String paraFocus = "      " + ce.frmObj + "__"
					+ ce.fields.elementAt(i) + ".focus();\n";

			if ("!".equals((String) ce.fNulls.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && isNull(" + para
						+ ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "必须输入！\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("D".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isDateBaby(" + para + ",'"
						+ ce.fOpers.elementAt(i) + "') ){ \n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法的日期格式，请按照'" + ce.fOpers.elementAt(i)
						+ "'格式输入\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("N".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isNumber(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法数字格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("I".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isInt(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法整数格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("F".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isFloat(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法浮点格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("E".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isEmail(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法email格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("M".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isMoney(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法货币格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("T".equals((String) ce.fTypes.elementAt(i))) {
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isTeleNo(" + para + ") ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "为非法电话格式!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("P".equals((String) ce.fTypes.elementAt(i))) {
				String paraPwdRpt = ce.frmObj + "__" + ce.fields.elementAt(i)
						+ "_RPT";
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isRptPwd(" + para + "," + paraPwdRpt
						+ ".value) ) {\n");
				sb.append("      alert(\"" + ce.fieldNames.elementAt(i)
						+ "两次输入的密码不同!\"); \n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("Y".equals((String) ce.fTypes.elementAt(i))) {
				String myCheck = (String) ce.fOpers.elementAt(i);
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !" + myCheck + "("
						+ para + ") ) {\n");
				sb.append(paraFocus + "      return false; \n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			if ("U".equals((String) ce.fTypes.elementAt(i))) {
				String paraTeleType = ce.fOpers.elementAt(i) + ".value";
				sb.append(" try{\n");
				sb.append("    if( " + paraEnaled + " && !isNull(" + para
						+ ")&&!isNull(" + paraTeleType + ") ) {\n");
				sb.append("      if( isErrUnicom(" + paraTeleType + "," + para
						+ ",'" + ce.fieldNames.elementAt(i) + "') ){ \n");
				sb.append(" " + paraFocus + "       return false; \n");
				sb.append("      }\n");
				sb.append("    }\n");
				sb.append(addErr(ce, i));
			}

			String len = (String) ce.fLens.elementAt(i);
			if (len != null && !"".equals(len)) {
				if ("<".equals((String) ce.fOpers.elementAt(i))) {
					sb.append(" try{\n");
					sb.append("      if( " + paraEnaled + " && !isNull(" + para
							+ ")&&!isInLength(" + para + "," + len + ") ){\n");
					sb.append("        alert(\"" + ce.fieldNames.elementAt(i)
							+ "超长，必须小于" + len + "!\"); \n");
					sb.append(paraFocus + "        return false; \n");
					sb.append("      }\n");
					sb.append(addErr(ce, i));
				} else {
					sb.append(" try{\n");
					sb.append("      if( " + paraEnaled + " && !isNull(" + para
							+ ")&&!isLength(" + para + "," + len + ") ){\n");
					sb.append("        alert(\"" + ce.fieldNames.elementAt(i)
							+ "长度必须为" + len + "!\"); \n");
					sb.append(paraFocus + "        return false; \n");
					sb.append("      }\n");
					sb.append(addErr(ce, i));
				}
			}
		}
		return sb.toString();
	}

	private CheckElement parseAField(String fields) {
		int iPos = fields.indexOf(":=");
		if (iPos < 0)
			return null;
		CheckElement retCE = new CheckElement();
		retCE.frmObjNo = fields.substring(0, iPos);
		if (retCE.frmObjNo == null)
			return null;
		int iPP = retCE.frmObjNo.indexOf("#");
		if (iPP == -1)
			retCE.frmObj = retCE.frmObjNo;
		else
			retCE.frmObj = retCE.frmObjNo.substring(0, iPP);
		Vector v = StringB.parseStringTokenizer(
				fields.substring(iPos + ":=".length()), ";");
		if (v == null)
			return null;
		try {
			for (int i = 0; i < v.size(); i++) {
				String aField = (String) v.elementAt(i);
				if (aField == null || "".equals(aField))
					continue;
				iPos = aField.indexOf("=");
				retCE.fields.add(i, aField.substring(0, iPos));
				if ("!".equals(aField.substring(iPos + 1, iPos + 2))) {
					retCE.fNulls.add(i, "!");
					retCE.fTypes.add(i, aField.substring(iPos + 2, iPos + 3));
					iPos += 3;
				} else {
					retCE.fNulls.add(i, "");
					retCE.fTypes.add(i, aField.substring(iPos + 1, iPos + 2));
					iPos += 2;
				}
				int iPosN = aField.indexOf("[");
				String aStyle = "";
				if (iPosN > -1) {
					retCE.fieldNames.add(i, aField.substring(iPosN));
					aStyle = aField.substring(iPos, iPosN);
				} else {
					retCE.fieldNames.add(i, retCE.fields.elementAt(i));
					aStyle = aField.substring(iPos);
				}
				if (aStyle == null || "".equals(aStyle)) {
					retCE.fLens.add(i, "");
					if ("D".equals((String) retCE.fTypes.elementAt(i)))
						retCE.fOpers.add(i, "YYYY/MM/DD");
					else
						retCE.fOpers.add(i, "");
				} else {
					if ("D".equals((String) retCE.fTypes.elementAt(i))
							|| "U".equals((String) retCE.fTypes.elementAt(i))
							|| "Y".equals((String) retCE.fTypes.elementAt(i))) {
						retCE.fLens.add(i, "");
						retCE.fOpers.add(i, aStyle);
					} else {
						int iP = aStyle.indexOf("<");
						if (iP > -1) {
							retCE.fOpers.add(i, "<");
							retCE.fLens.add(i, aStyle.substring(0, iP));
						} else {
							retCE.fOpers.add(i, "=");
							retCE.fLens.add(i, aStyle);
						}
					}
				}
			}
		} catch (Exception e) {
			return null;
		}
		return retCE;
	}

	public static void main(String[] args) {
		CommCheck cc = new CommCheck();
		cc.warn = 1;
		CheckElement[] ces = cc
				.parseFields("from.acct:=id=!N12<[标识];dog=!P10[狗];;from.acct:=id=!YchekNo[号码];dog=S10[狗]");
		for (int i = 0; i < ces.length; i++) {
			System.out.println("\nce=" + cc.printACheckElement(ces[i]));
		}

	}

	public int getWarn() {
		return warn;
	}

	public void setWarn(int warn) {
		this.warn = warn;
	}
}