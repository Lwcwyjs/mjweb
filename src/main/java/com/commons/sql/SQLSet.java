package com.commons.sql;

import com.commons.utils.DealString;

public class SQLSet {
	static boolean isOracle = false;

	// 查询资源列表（根据用户ID，资源父节点和资源类型）
	public static String getResource(Long uid, Long pid, String resourcetype) {
		String where = "";
		if (pid == null) {
			where += " and c.pid=-1";
		} else {
			where += " and c.pid=" + DealString.toString(pid);
		}
		if (DealString.toString(resourcetype) != "") {
			if (resourcetype.indexOf(",") > 0) {
				where += " and c.resourcetype in(" + resourcetype + ") ";
			} else {
				where += " and c.resourcetype=" + resourcetype + " ";
			}
		}
		String sql = "select c.* from ai_sys_user_role a " + "left join ai_sys_role_resource b on b.role_id=a.role_id "
				+ "left join ai_sys_resource c on c.id=b.resource_id " + "where a.user_id=" + DealString.toString(uid)
				+ " " + where + " order by pid,seq";
		if (isOracle) {
			sql = "";
		}
		return sql;
	}

	// 查询资源列表（根据角色ID和资源类型）
	public static String getResource (Long rid, String resourcetype){
		String where = "";
		if (DealString.toString(resourcetype) != "") {
			if (resourcetype.indexOf(",") > 0) {
				where += " and b.resourcetype in(" + resourcetype + ") ";
			} else {
				where += " and b.resourcetype=" + resourcetype + " ";
			}
		}
		String sql = "select b.* from ai_sys_role_resource a " + "left join ai_sys_resource b on b.id=a.resource_id "
				+ "where a.role_id=" + DealString.toString(rid) + " " + where + " order by pid,seq";
		if (isOracle) {
			sql = "";
		}
		return sql;
	}





	// 查找用户信息
	public String getUserInfo (String name){
		String sql = "";
		sql = "SELECT * FROM ai_sys_user WHERE NAME='" + name + "'";
		if (isOracle) {
			sql = "";
		}
		return sql;
	}

}
