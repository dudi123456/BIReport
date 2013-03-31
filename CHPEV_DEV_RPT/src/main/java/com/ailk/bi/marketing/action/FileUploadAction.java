package com.ailk.bi.marketing.action;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.ailk.bi.base.struct.UserCtlRegionStruct;
import com.ailk.bi.base.util.StringTool;
import com.ailk.bi.base.util.WebConstKeys;
import com.ailk.bi.base.util.WebKeys;
import com.ailk.bi.common.app.AppException;
import com.ailk.bi.common.app.AppWebUtil;
import com.ailk.bi.common.sysconfig.GetSystemConfig;
import com.ailk.bi.marketing.entity.FileInfo;
import com.ailk.bi.report.struct.ReportQryStruct;
import com.ailk.bi.report.util.ReportConsts;
import waf.controller.web.action.HTMLActionException;
import waf.controller.web.action.HTMLActionSupport;
/**
 * @实现文件上传
 * 【action控制层】实现文件上传
 * @author  方慧
 * @version  [版本号, 2012-04-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FileUploadAction extends HTMLActionSupport {
	private static final long serialVersionUID = 1L;
	private File tempPath ; // 用于存放临时文件的目录
	private File newFileUrl;//用于最终保存的目录
	private int size = 1024*5;
	private int maxSize = 1024*1024*20;//上传上线
	@SuppressWarnings("unchecked")
	public void doTrans(HttpServletRequest request, HttpServletResponse response)
			throws HTMLActionException {
		// 判断用户是否有效登陆
		if (!com.ailk.bi.common.app.WebChecker.isLoginUser(request, response))
			return;
		HttpSession session = request.getSession();
		// 获取页面screen标示
		String ff = GetSystemConfig.getBIBMConfig().getUploadFolder();
		String optype = request.getParameter("optype");//获得制定的的JSP页面
		String doType = request.getParameter("doType");//获得具体的操作
		String fileCode = request.getParameter("fileCode");//获得具体的操作
		String modeType = request.getParameter("modeType");//指明是普通文件还是营销案例文件  mode代表（营销案例）
		if (optype == null || "".equals(optype)) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE, "未知界面查询操作，请检查！");
		}
		// 查询结构，接受界面条件值
		ReportQryStruct qryStruct = new ReportQryStruct();
		// 判断是否有外部传入条件，p_condition约定名称
		String p_condition = request.getParameter("p_condition");
		if (StringTool.checkEmptyString(p_condition)) {
			p_condition = ReportConsts.NO;
		}
		try {
			if (ReportConsts.YES.equals(p_condition)) {
				qryStruct = (ReportQryStruct) session
						.getAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT);
			} else {
				AppWebUtil.getHtmlObject(request, "qry", qryStruct);
			}
		} catch (AppException ex) {
			throw new HTMLActionException(session, HTMLActionException.WARN_PAGE,
					"提取界面查询信息失败，请注意是否登陆超时！");
		}
		// 加入权限控制条件-用户控制区域
		UserCtlRegionStruct ctlStruct = (UserCtlRegionStruct) session
				.getAttribute(WebConstKeys.ATTR_C_UserCtlStruct);
		if (ctlStruct == null) {
			ctlStruct = new UserCtlRegionStruct();
		}
		/**
		 *
		 *开始上传文件
		 *@author  方慧 f00211612
		 *@date 2012-04-21
		 * */
		if("add".equals(doType))
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String url = session.getServletContext().getRealPath(ff);
			String tempUrl = session.getServletContext().getRealPath(ff);
			 this.newFileUrl =new File(url);
			 this.tempPath =new File(tempUrl);
			 FileInfo fileInfo = new FileInfo();
			factory.setSizeThreshold(size);
			factory.setRepository(tempPath);
			Date date=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmsssss");
			String dateStr = sdf.format(date);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setSizeMax(maxSize);
			String fileNameStr="还没有上传文件...";
			try {
				List<FileItem> fileItems = upload.parseRequest(request);
				Iterator<FileItem> iter = fileItems.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (!item.isFormField()) {
						fileNameStr = item.getName();
						fileInfo.setFileCode(dateStr);
						fileInfo.setFileName(fileNameStr);
						fileInfo.setFileSize(item.getSize()/1024);
						if ((fileNameStr == null || fileNameStr.equals("")) && size == 0)
							continue;
						try {
							String saveUrl =newFileUrl +"/"+ fileInfo.getFileCode()+fileInfo.getFileName();
							fileInfo.setFileUrl( fileInfo.getFileCode()+fileInfo.getFileName());
							item.write(new File(saveUrl));
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if("mode".equals(modeType)){
				List<FileInfo> files= (ArrayList<FileInfo>) session.getAttribute("modeFiles");
				if(null==files){
					files = new ArrayList<FileInfo>();
				}
				files.add(fileInfo);
				session.setAttribute("modeFiles", files);
			}else{
				List<FileInfo> files= (ArrayList<FileInfo>) session.getAttribute("files");
				if(null==files){
					files = new ArrayList<FileInfo>();
				}
				files.add(fileInfo);
				session.setAttribute("files", files);
			}

		}else if("delete".equals(doType))
		{

			List<FileInfo> files = null;
			if("mode".equals(modeType)){
				files= (ArrayList<FileInfo>) session.getAttribute("modeFiles");
			}else{
				files= (ArrayList<FileInfo>) session.getAttribute("files");
			}
			if(null!=files){
				for(int i = 0;i<files.size();i++)
				{
					if (!StringTool.checkEmptyString(fileCode)) {
						if(fileCode.equals(files.get(i).getFileCode()))
						{
							files.remove(i);
						}
					}
				}
				if("mode".equals(modeType)){
					session.setAttribute("modeFiles", files);
				}else{
					session.setAttribute("files", files);
				}
			}
		}
		session.setAttribute(WebKeys.ATTR_SUBJECT_QUERY_STRUCT, qryStruct);
		setNextScreen(request, optype+".screen");
	}
}
