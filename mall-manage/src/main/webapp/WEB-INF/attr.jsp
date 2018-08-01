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
<title>商城</title>
</head>
<body>
	
	<!-- 再次使用layout控件，形成嵌套布局！ -->
	<div class="easyui-layout" data-options="fit:true">
		<div data-options="region:'north',split:true" style="height:50px">
				<div style="margin-top:10px;margin-left:10px">
					一级：<select data-options="width:200" class="easyui-combobox" id="attr_class_1_select" onchange="get_attr_class_2(this.value);"><option>请选择</option></select>
					二级：<select data-options="width:200" class="easyui-combobox"  id="attr_class_2_select" onchange="get_attr_list_json(this.value)"><option>请选择</option></select>
					<a href="javascript:goto_attr_add();">添加</a><br>
				</div>
		</div>
		<div data-options="region:'west',split:true" style="width:100px">
			查询<br>
			删除<br>
			编辑<br>
		</div>
		<div data-options="region:'center'">
			<div id="attrListInner" class="easyui-datagrid"></div>
		</div>
	</div>
	
<!-- 	一级：<select id="attr_class_1_select" 
				onchange="get_attr_class_2(this.value);"
				data-options="width:100" 
				class="easyui-combobox">
			<option>请选择</option>
		</select>
	二级：<select  id="attr_class_2_select" 
				onchange="get_attr_list_json(this.value)"
				data-options="width:100" 
				class="easyui-combobox">
			<option>请选择</option>
		</select>
	<br>
	查询<br>
	<a href="javascript:goto_attr_add()">添加</a><br>
	删除<br>
	编辑<br>
	<hr>
	<div id="attrListInner"></div>
 -->	
<script type="text/javascript">
	$(function (){
		/*$.getJSON("js/json/class_1.js",function(data){
			$(data).each(function(i,json){
				$("#attr_class_1_select").append("<option value="+json.id+">"+json.flmch1+"</option>");
			});
		});*/
		
		$('#attr_class_1_select').combobox({    
		    url:'js/json/class_1.js',    
		    valueField:'id',    
		    textField:'flmch1',
		    //使用combobox控件，会隐藏之前select标签，onchange事件失效
		    //所以在这里使用 控件中的onChage事件
		    onChange:function get_attr_class_2(){
		    	// 获取当前的下拉列表被选中的id（此处不能使用val()方法）
		    	var class_1_id = $(this).combobox("getValue");
				// class_2的combobox控件
		    	$('#attr_class_2_select').combobox({    
				    url:"js/json/class_2_"+class_1_id+".js",    
				    valueField:'id',    
				    textField:'flmch2',
				    onChange:function (){
				    	var flbh2 = $(this).combobox("getValue");
				    	//调用获取属性josn数据的方法
				    	get_attr_list_json(flbh2);
				    },
				});
			}
		}); 
	});
	
	//上面使用combobox控件，会隐藏之前select标签，onchange事件失效；所以这个方法不会被调用了
	function get_attr_class_2(class_1_id){
		$.getJSON("js/json/class_2_"+class_1_id+".js",function(data){
			$("#attr_class_2_select").empty();
			$(data).each(function(i,json){
				$("#attr_class_2_select").append("<option value="+json.id+">"+json.flmch2+"</option>");
			});
		});
	}
	
	function goto_attr_add(){
		//var class_2_id = $("#attr_class_2_select").val();		
		//window.location.href="goto_attr_add.do?flbh2="+class_2_id;
		//此处已经变成combobox控件的span，上面的形式已经获取不到值\也不能进行跳转了
		var class_2_id = $("#attr_class_2_select").combobox("getValue");		
		add_tab("goto_attr_add.do?flbh2="+class_2_id,"添加属性");
	}
	
	//之前加载属性内嵌页的方法 --不再使用
	function get_attr_list(flbh2){
		// 异步查询
		$.post("get_attr_list.do",{flbh2:flbh2},function(data){
			$("#attrListInner").html(data);
		});
	}
	
	//现在加载属性json数据的方法 -- 使用easyui
	function get_attr_list_json(flbh2){
		// 异步查询
		$('#attrListInner').datagrid({    
		    url:'get_attr_list_json.do',   
		    queryParams: {
		    	flbh2: flbh2
			},
		    columns:[[    
		        {field:'id',title:'id',width:100},    
		        {field:'shxm_mch',title:'属性名',width:100},    
		        {field:'list_value',title:'属性值',width:300,
		        	formatter: function(value,row,index){
		        		var str = "";
						// 处理字段值的代码
						$(value).each(function(i,json){
							str = str + json.shxzh+json.shxzh_mch + " ";
						});
						return str;
					}	
		        },
		        {field:'chjshj',title:'创建时间',width:300,
		        	formatter: function(value,row,index){
		        		var d = new Date(value);
		        		var str = d.toLocaleString();
						return str;
					}	
		        }
		    ]]    
		});  
	}
</script>
</body>
</html>