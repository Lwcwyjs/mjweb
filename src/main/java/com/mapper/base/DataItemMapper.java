package com.mapper.base;

import java.util.List;
import java.util.Map;

public interface DataItemMapper {
	
	void insert(String sql);
	
	void update(String sql);
	
	void delete(String sql);

	Map<Object,Object> selectObject(String sql);
	
	List<Map<Object, Object>> selectMultiObject(String sql);
	
	Integer selectCount(String sql);
	
	Integer selectSum(String sql);

}
