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
<script type="text/javascript">
	var x = 0;
	var y = 1;
	
	//添加一行属性
	function addAttr(){
		var context = 
			'<tr>'+
			'<td>属性值：<input type="text" name="list_attr['+x+'].list_value['+y+'].shxzh"/></td>'+
			'<td>单位：<input type="text" name="list_attr['+x+'].list_value['+y+'].shxzh_mch"/></td>'+
			'<td><a href="javascript:delHang()">删除</a></td>'+
			'</tr>';
		
		$("#attrTable").append(context);
		y++;
	}
	
	//删除一行属性值
	function delHang(){
	}	
	
	//添加一个属性Table
	function addAttrTable(){
		var context = 
		'<table border="1" width="800px" style="margin-top:10px;">'+
		'<tr>'+
		'<td>属性名：<input type="text" name="list_attr[0].shxm_mch"/></td>'+
		'<td></td>'+
		'<td><a href="javascript:addAttr()">添加属性值</a></td>'+
		'</tr>'+
		'<tr>'+
		'<td>属性值：<input type="text" name="list_attr[0].list_value[0].shxzh"/></td>'+
		'<td>单位：<input type="text" name="list_attr[0].list_value[0].shxzh_mch"/></td>'+
		'<td><a href="javascript:delHang()">删除</a></td>'+
		'</tr>'+
		'</table>';
		
		$("#attrTable").after(context);
	}	
	
</script>
<title>商城</title>
</head>
<body>
	<center><a href="javascript:addAttrTable()">添加商品属性</a>
	<hr>
	<form action="attr_add.do">
		<input type="hidden" value="${flbh2}" name="flbh2"/>
		
		<table border="1" width="800px" id="attrTable" style="margin-top:10px;">
			<tr>
				<td>属性名：<input type="text" name="list_attr[0].shxm_mch"/></td>
				<td></td>
				<td><a href="javascript:addAttr()">添加属性值</a></td>
			</tr>
			<tr>
				<td>属性值：<input type="text" name="list_attr[0].list_value[0].shxzh"/></td>
				<td>单位：<input type="text" name="list_attr[0].list_value[0].shxzh_mch"/></td>
				<td><a href="javascript:delHang()">删除</a></td>
			</tr>
		</table>
		
		<input type="submit" value="提交" style="width:80px;" />
	</form>
	</center>
</body>
</html>