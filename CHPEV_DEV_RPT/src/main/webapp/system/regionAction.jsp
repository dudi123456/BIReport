<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.ailk.bi.base.table.*"%>
<%@ page import="com.ailk.bi.system.common.*"%>
<%@ page import="com.ailk.bi.base.util.*"%>
<%@ page import="com.ailk.bi.system.facade.impl.*"%>
<%@ page import="com.ailk.bi.system.service.ISystemService" %>
<%@ page import="com.ailk.bi.system.service.impl.SystemServiceImpl" %>
<%@ page import="com.ailk.bi.metamanage.service.*" %>
<%@ page import="com.ailk.bi.metamanage.service.impl.*" %>

<%
		String out_str ="";
		//oper_type = 5 返回具体的类型值。
		String oper_type = CommTool.getParameterGB(request,"oper_type");
		if("1".equals(oper_type)){//检测区域是否存在

            String region_id  =  CommTool.getParameterGB(request,"region_id");
			if(!LSInfoRegion.CheckExistRegion(region_id)){
				out_str = "0";
			}else{
				out_str = "1";
			}
		}else if("2".equals(oper_type)){//检测部门是否存在
            String dept_id  =  CommTool.getParameterGB(request,"dept_id");
			if(!LSInfoDept.CheckExistDept(dept_id)){
				out_str = "0";
			}else{
				out_str = "1";
			}
		}else if("3".equals(oper_type)){//自动获取工号
			out_str=LSInfoUser.getAutoNewOperNo();
		}else if("41".equals(oper_type)){//判断工号是否存在
			String user_id=request.getParameter("user_id");
			InfoOperTable oper=CommonFacade.getLoginUser(session);
			if(oper!=null){
				out_str=String.valueOf(LSInfoUser.isUserExists(user_id,oper));
			}else{
				out_str="error";
			}
		}else if("5".equals(oper_type)){//判断角色代码是否存在
			String role_id=request.getParameter("role_id");
			InfoOperTable oper=CommonFacade.getLoginUser(session);
			//oper.region_id = "0";
			if(oper!=null){
				out_str=String.valueOf(LSInfoRole.isRoleExists(role_id,oper));  //查不到返回false。
			}else{
				out_str="error";
			}
		}else if("7".equals(oper_type)){    //判断部门代码是否存在
			String dept_id=request.getParameter("dept_id");
			//String user_id=CommonFacade.getLoginId(session);
			out_str=String.valueOf(LSInfoDept.CheckExistDept(dept_id));//(dept_id,user_id)
		}
		else if("8".equals(oper_type))//判断用户组代码是否存在
		{
			String group_id=request.getParameter("group_id");
			//String user_id=CommonFacade.getLoginId(session);
			out_str=String.valueOf(LSInfoUserGroup.isUserGroupExists(group_id));

		}
		else if("9".equals(oper_type)){    //判断子系统代码是否存在
			final ISystemService service = new SystemServiceImpl();
			String system_id=request.getParameter("system_id");

			//String user_id=CommonFacade.getLoginId(session);
			out_str=String.valueOf(service.isExistSystemId(system_id));
		}
		else if("10".equals(oper_type))   //维度标识
		{
			IDimDefService service = new DimDefServiceImpl();
			String dim_id = request.getParameter("dim_id");
		    out_str = String.valueOf(service.isExistDimId(dim_id));
		}
		else if("11".equals(oper_type))   //指标标识
		{
			IMsuDefService service = new MsuDefServiceImpl();
			String msu_id = request.getParameter("msu_id");
		    out_str = String.valueOf(service.isExistMsuId(msu_id));
		}
		out.print(out_str);
%>