package com.ailk.bi.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface ICommonMapper {
	// oracle 判断数据表是否存在
	@Select("select count(*) from user_tables where table_name= #{tablename}")
	Integer getTableCount(@Param("tablename") String tablename);

	@Update("${sql}")
	Integer update(@Param("sql") String sql);

	@SuppressWarnings("rawtypes")
	@Select("${sql}")
	List<Map> list(@Param("sql") String sql);

	@Delete("${sql}")
	Integer delete(@Param("sql") String sql);
}
