package com.ailk.bi.struts2.olap;

import java.util.List;

import org.apache.struts2.rest.DefaultHttpHeaders;
import org.apache.struts2.rest.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;

import com.ailk.bi.domain.olap.UiRptInfoUserOlap;
import com.ailk.bi.olap.service.IUiRptInfoUserOlapSrv;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class CustomController extends ActionSupport implements
		ModelDriven<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = -5713474415087043996L;

	private UiRptInfoUserOlap userOlap = new UiRptInfoUserOlap();
	private List<UiRptInfoUserOlap> userOlaps;
	private String id;
	@Autowired
	private IUiRptInfoUserOlapSrv userOlapSrv;

	public Object getModel() {
		return (userOlaps != null ? userOlaps : userOlap);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null) {
			this.userOlap = userOlapSrv.findById(Integer.valueOf(id));
		}
		this.id = id;
	}

	public HttpHeaders index() {
		userOlaps = userOlapSrv.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public HttpHeaders create() {
		userOlapSrv.save(userOlap);
		return new DefaultHttpHeaders("create");
	}

	public HttpHeaders remove() {
		userOlapSrv.removeById(Integer.valueOf(id));
		//这里还得刷新model
		userOlaps = userOlapSrv.findAll();
		return new DefaultHttpHeaders("index").disableCaching();
	}

	public HttpHeaders show() {
		return new DefaultHttpHeaders("show").disableCaching();
	}

	public HttpHeaders update() {
		userOlapSrv.save(userOlap);
		return new DefaultHttpHeaders("update");
	}

}
