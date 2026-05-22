<%@page import="com.commons.utils.StringUtils"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String msg = request.getAttribute("ErrorMsg").toString();
%>
<div>异常</div>
<div><%=msg %></div>