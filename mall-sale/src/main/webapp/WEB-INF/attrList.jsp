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
	function save_param(shxm_id,shxzh_id,shxmch){
		// 封装数据(ajax提交数据:数组/JSON/表单)，下面方法处理数据

		/* 1. 传递字符串数组
		$("#paramArea").append("<input name='shxparam' value='"+shxm_id+"_"+shxz_id+"' type='text'>"+shxmch);
		get_list_by_attr();		
		*/
		
		/* 2. 传递JSON字符串*/
		$("#paramArea").append("<input name='shxparam' type='text' value='{\"shxm_id\":"+shxm_id+",\"shxzh_id\":"+shxzh_id+"}'>"+shxmch);
		//点击后隐藏该属性
		$("#hidden_" + shxm_id).hide();
		//发送异步请求
		get_list_by_attr();		
	}
	
	
	// 调用ajax异步请求
	function get_list_by_attr(){
		/* 1. 数组方式传参      参数形式: param_array[] = 14_33
		var param_array = new Array();
		$("#paramArea input[name='shxparam']").each(function(i,data){
			param_array[i] = json.value;
		});
		$.get("get_list_by_attr.do",{param_array:param_array},function(data){
			$("#skuListInner").html(data);
		}); */

		
		/* 2. JSON方式传参(参数形式一)    参数形式:list_attr[0].shxm_id=14 ...
		var attrJson = {};
		$("#paramArea input[name='shxparam']").each(function(i,data){
			var json = $.parseJSON(data.value);
			attrJson["list_attr["+i+"].shxm_id"] = json.shxm_id;
			attrJson["list_attr["+i+"].shxzh_id"] = json.shxzh_id;
			attrJson["flbh2"]=${flbh2};
		});
		// 异步提交请求    刷新商品列表
		$.get("get_list_by_attr.do",attrJson,function(data){
			$("#skuListInner").html(data);
		});	*/ 
		
		
		/* 2. JSON方式传参(参数形式二)    参数形式:list_attr[0].shxm_id=14 ...*/
		var jsonStr = "flbh2="+ "${flbh2}";
		$("#paramArea input[name='shxparam']").each(function(i,data){
			var json = $.parseJSON(data.value);
			jsonStr = jsonStr + "&list_attr["+i+"].shxm_id="+json.shxm_id+"&list_attr["+i+"].shxzh_id="+json.shxzh_id;
		});
		// 异步提交请求   刷新商品列表
		$.get("get_list_by_attr.do",jsonStr,function(data){
			$("#skuListInner").html(data);
		}); 
	}
	
	function search_order(new_value){
		var old_value = $("#orderId").val();	
		if( old_value == new_value ){
			new_value = new_value + " desc ";
		}
		$("#orderId").val(new_value);
	}
	
</script>
<title>商城</title>
</head>
<body>
	<div id = "paramArea">
		<input type="text" id="orderId"><br>
	</div>
	<hr>
	属性列表<br>
	<c:forEach items="${list_attr}" var="attr">
		<!-- 隐藏div（点击该属性后隐藏，避免重复点击属性） -->
		<div id="hidden_${attr.id }">
		    ${attr.shxm_mch}:
			<c:forEach items="${attr.list_value}" var="val">
				<a href="javascript:save_param(${attr.id},${val.id},'${val.shxzh}${val.shxzh_mch}');">${val.shxzh}${val.shxzh_mch}</a>
			</c:forEach>
		</div>
	</c:forEach>
	
	<!-- 排序 -->
	<hr>
	<a href="javascript:search_order(' order by jg ');">价格</a> 
	<a href="javascript:search_order(' order by sku_xl ');">销量 </a>
	<a href="javascript:search_order(' order by sku.chjshj ');">上架时间 </a>
	<a href="javascript:search_order(' order by plsh ');">评论数 </a>
	
</body>
</html>