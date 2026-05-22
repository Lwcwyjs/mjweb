/**
 * 列表中代码与名称的转化
 */

// 组织机构
var _organs;
// 角色
var _roles;
// 用户角色
var _user_roles;
// 数据字典
var _syscode;
//检测预警
var _baseoptions;

function getValue(type, key, url) {
	var str = "";
	var obj;
	if (type.toUpperCase() == "ORGAN") {
		this._organs = getBaseData(this._organs, url);
		str = getBaseDataByKey(this._organs, key);
	}else if (type.toUpperCase() == "CYQXH") {
		this._organs = getBaseData(this._organs, url);
		str = getBaseDataByKey(this._organs, key);
	} else if (type.toUpperCase() == "ROLE") {
		this._roles = getBaseData(this._roles, url);
		str = getBaseDataByKey(this._roles, key);
	} else if (type.toUpperCase() == "USERROLE") {
		this._user_roles = getBaseData(this._user_roles, url);
		str = getBaseDataByKey(this._user_roles, key);
	} else if (type.toUpperCase() == "照片参数") {
		this._baseoptions = getBaseData(this._baseoptions, url);
		str = getBaseDataByTypeKey(this._baseoptions, type, key);
	} else if (type.toUpperCase() == "比对参数") {
		this._baseoptions = getBaseData(this._baseoptions, url);
		str = getBaseDataByTypeKey(this._baseoptions, type, key);
	} else {
		this._syscode = getBaseData(this._syscode, url);
		str = getBaseDataByTypeKey(this._syscode, type, key);
	}

	return str;
}

function getBaseData(obj, url) {
	if (obj == null || obj == undefined) {
		$.ajax({
			url : url,
			async : false,
			success : function(result) {
				obj = eval('(' + result + ')');
			}
		});
	}
	return obj;
}
function getBaseDataByKey(obj, key) {
	var str = "";
	if (key != null) {
		$.each(obj, function(name, value) {
			if (name == key)
				str += str == "" ? value : "," + value;
		});
	}
	str = str == "" ? key : str;
	return str;
}
function getBaseDataByTypeKey(obj, type, key) {
	var str = "";
	if (key != null) {
		$.each(obj, function(name, value) {
			if (name == type && value != null) {
				$.each(value, function(name, value) {
					if (name == key)
						str += str == "" ? value : "," + value;
				});
			}
		});
	}
	str = str == "" ? key : str;
	return str;
}
function clearBaseData(type) {
	if (type.toUpperCase() == "ORGAN") {
		this._organs = null;
	} else if(type.toUpperCase() == "CYQXH"){
		this._cyqxh = null;
	} else if (type.toUpperCase() == "ROLE") {
		this._roles = null;
	} else if (type.toUpperCase() == "USERROLE") {
		this._user_roles = null;
	} else {
		this._syscode = null;
	}
}