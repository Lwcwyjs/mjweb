<!DOCTYPE html>
<%--标签 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="java.util.*" %>
<%--basePath --%>
<c:set var="base"
       value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}"/>
<%--静态文件目录 --%>
<c:set var="path" value="${base}"/>
<%--项目路径 --%>
<c:set var="staticPath" value="${base}"/>
<c:set var="staticVersion" value="<%=System.currentTimeMillis()%>"/>
<contextPathData
        contextPathValue="${staticPath}">
</contextPathData>
<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="shortcut icon" href="${staticPath}/static/style/images/favicon.png"/>
<link rel="stylesheet" href="${staticPath}/static/assets/libs/layui/css/layui.css?v=${staticVersion}"/>
<%--<link rel="stylesheet" href="${staticPath}/static/assets/layui/css/layui.css"/>--%>
<link rel="stylesheet" href="${staticPath}/static/assets/css/admin.css?v=${staticVersion}"/>
<link rel="stylesheet" href="${staticPath}/static/assets/css/theme.css?v=${staticVersion}"/>
<link rel="stylesheet" href="${staticPath}/static/easyui/themes/icon.css?v=${staticVersion}"/>
<script type="text/javascript" src="${staticPath }/static/assets/libs/layui/layui.js?v=${staticVersion}"></script>
<script type="text/javascript" src="${staticPath }/static/module/admin.js?v=${staticVersion}"></script>
<%--<link rel="stylesheet" type="text/css" href="${staticPath }/static/assets/libs/layui/css/formSelects-v4.css" />--%>
<%--<script type="text/javascript" src="${staticPath }/static/assets/layui/layui.js"></script>--%>
