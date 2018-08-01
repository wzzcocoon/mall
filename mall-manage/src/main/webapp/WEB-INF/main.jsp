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

<link rel="stylesheet" type="text/css" href="js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="js/easyui/themes/icon.css">
<script type="text/javascript" src="js/easyui/jquery.easyui.min.js"></script>

<script type="text/javascript">
	function add_tab(url,title){
		// add a new tab panel  
		var b = $('#tt').tabs('exists',title);
		if(!b){
			$('#tt').tabs('add',{    
			    title:title,    
			    href:url,    
			    closable:true,    
			    tools:[{    
			        iconCls:'icon-mini-refresh',    
			        handler:function(){    
			            alert('refresh');    
			        }    
			    }]    
			});
		}else{
			$('#tt').tabs('select',title);
		}

	}
	
	function add_tab2(url,title){
		// add a new tab panel    
		$.post(url,function(data){
			$('#tt').tabs('add',{    
			    title:title,    
			    content:data,    
			    closable:true,    
			    tools:[{    
			        iconCls:'icon-mini-refresh',    
			        handler:function(){    
			            alert('refresh');    
			        }    
			    }]    
			});
		});
	}
</script>
<title>商城</title>
</head>
<body class="easyui-layout">
	<div data-options="region:'north',border:false" style="height:75px;background:#B3DFDA;padding:10px">
		<center><h2>商城后台管理系统</h2></center>
	</div>
	<div data-options="region:'west',split:true,title:'导航栏'" style="width:180px;padding:10px;">
		<div class="easyui-accordion" style="width:160px;">
			<div title="One-->" data-options="iconCls:'icon-ok'" >
				<ul>
					<li><a href="javascript:add_tab('goto_spu.do','商品信息管理')">商品信息管理</a></li>
					<li><a href="javascript:add_tab('goto_attr.do','商品属性管理')" >商品属性管理</a></li>
					<li><a href="javascript:add_tab('goto_sku.do','商品库存单元管理')" >商品库存单元管理</a></li>
				</ul>
			</div>
			<div title="Two-->" data-options="iconCls:'icon-ok'">
				<ul>
					<li>商品缓存管理</li>
				</ul>
			</div>
		</div>
	</div>
	<div data-options="region:'east',split:true,collapsed:true,title:'快速导航'" style="width:100px;padding:10px;">
		<ul><li>1</li><li>2</li><li>3</li><li>4</li><li>5</li></ul>
	</div>
	<div data-options="region:'south',border:false" style="height:50px;background:#A9FACD;padding:10px;">
		<center>京公网安备 11000002000088号|京ICP证070359号|Copyright © 2004 - 2018</center>
	</div>
	<div data-options="region:'center',title:'内容'">
		<div id="tt" class="easyui-tabs" style="height:500px">

		</div>
	</div>

</body>
</html>