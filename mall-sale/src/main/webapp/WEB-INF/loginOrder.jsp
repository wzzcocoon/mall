<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"  %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath %>">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" type="image/icon" href="images/jd.ico">
<link rel="stylesheet" type="text/css" href="css/login.css">
<script type="text/javascript">
	function to_submit(){
		$("#login_form").submit();
	}
</script>
<title>商城</title>
</head>
<body>
		<div class="up">
			<img src="images/logo.jpg" height="45px;" class="hy_img"/>
			<div class="hy" style="color:red;font-style:italic;">
				--> Welcome to the mall @_@	
			</div>
		</div>
		<div class="middle" style="height:590px;border-radius:30%"">
			<div class="login" style="margin-top:90px;width:400px;height:400px;border-radius:10%" >
				<div class="blank_01" style="height:35px"></div>
				<div class="l1 ">
					<div class="l1_font_01 ">会员登录</div>
					<a class="l1_font_02 " href="<%=application.getContextPath() %>/to_regist.action">用户注册</a>
				</div>
				<div class="ts">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${err}
				</div>
				<div class="blank_01"></div>
				<form action="login.do" id="login_form" method="post">
					<input  type="hidden" value="goto_checkOrder.do" name="redirect"/>
					<div class="input1">
						<input type="text" class="input1_01" name="yh_mch"/>
					</div>
					<div class="blank_01"></div>
					<div class="blank_01"></div>
					<div class="input2">
						<input type="password" class="input1_01" name="yh_mm"/>
					</div>
				
				<div class="blank_01"></div>
				<div class="blank_01"></div>
				<div class="box_01">
					<div class="box_01_both">
						<div class="box_01_both_1">数据源1：<input type="radio" name="dataSource_type" value="1"/></div>
						<div class="box_01_both_2">数据源2：<input type="radio" name="dataSource_type" value="2"/></div>
					</div>
				</div>
				</form>
				<div class="blank_01"></div>
				<a href="javascript:;" class="aline">
					<div class="red_button" onclick="to_submit()">
						登&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录
					</div>
				</a>
				<div class="blank_01"></div>
				<div class="blank_01"></div>
				<div class="box_down">
					<div class="box_down_1">使用合作网站账号登录京东：</div>
					<div class="box_down_1">京东钱包&nbsp;&nbsp;|&nbsp;&nbsp;QQ 
					&nbsp;&nbsp;|&nbsp;&nbsp;微信
					</div>
				</div>
			</div>	
		</div>
		
		<div class="down">
			<br/>
			京公网安备 11000002000088号|京ICP证070359号|Copyright © 2004 - 2018
		</div>
</body>
</html>