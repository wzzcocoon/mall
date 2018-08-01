<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"  %>
<%String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath %>">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
	function b(){
		//异步提交表单
		var form = $("#loginForm");		//html   DOM对象
		//表单序列化：序列表单内容为字符串，用于AJAX请求
		var formStr = form.serialize()
		$.get("login.do",formStr,function(data){
			alert(data);
		});
	}
</script>
<title>商城</title>
</head>
<body>
	异步登录表单测试
	<hr>
	<form id="loginForm">
		username:<input name="yh_mch" type="text">	
		password:<input name="yh_mm" type="text">
		<input type="button" onclick="b()" value="test">
	</form>
</body>
</html>