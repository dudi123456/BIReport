package com.ailk.bi.mapper.olap;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;
import com.ailk.bi.mapper.ICommonMapper;

@Component("userOlapDimMapper")
public interface IUiRptUserOlapDimMapper extends ICommonMapper {
	public final String DELETE_USER_OLAP_DIM = "DELETE UI_RPT_META_USER_OLAP_DIM "
			+ "WHERE CUSTOM_RPTID=#{customRtpId}";
	public final String INSERT_USER_OLAP_DIM = "INSERT INTO UI_RPT_META_USER_OLAP_DIM"
			+ "(CUSTOM_RPTID,DIM_ID,DISPLAY_ORDER)"
			+ "VALUES(#{customRtpId},#{dimId},#{displayOrder})";
	public final String SELECT_USER_OLAP_DIM = " SELECT CUSTOM_RPTID,DIM_ID,DISPLAY_ORDER FROM "
			+ "UI_RPT_META_USER_OLAP_DIM WHERE CUSTOM_RPTID=#{customRtpId} ORDER BY DISPLAY_ORDER";

	/**
	 * 保存一个用户定义的维度
	 *
	 * @param customRtpId
	 * @param dimId
	 * @param displayOrder
	 */
	@Insert(INSERT_USER_OLAP_DIM)
	public void insert(@Param("customRtpId") Integer customRtpId,
			@Param("dimId") String dimId,
			@Param("displayOrder") Integer displayOrder);

	@Select(SELECT_USER_OLAP_DIM)
	@Results(value = {
			@Result(property = "customRptId", column = "CUSTOM_RPTID"),
			@Result(property = "dimId", column = "DIM_ID"),
			@Result(property = "displayOrder", column = "DISPLAY_ORDER") })
	public List<UiRptMetaUserOlapDim> getAllRptUserDims(
			@Param("customRtpId") Integer customRtpId);

	@Delete(DELETE_USER_OLAP_DIM)
	public void deleteAllRptUserDims(@Param("customRtpId") Integer customRtpId);

}
