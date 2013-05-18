<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="com.gm.wine.util.CommonUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/meta.jsp" %>
	<title>网站后台-资讯管理</title>
	<meta content="news" name="activemenu" />
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/> 
	<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
	<link rel="stylesheet" href="${ctx}/KindEditor/themes/default/default.css" />
	<link rel="stylesheet" href="${ctx}/KindEditor/plugins/code/prettify.css" />
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
	<script src="${ctx}/js/DatePicker/WdatePicker.js" type="text/javascript" ></script>
	<script type="text/javascript" src="${ctx}/KindEditor/kindeditor-min.js"></script>
	<script type="text/javascript" src="${ctx}/KindEditor/lang/zh_CN.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#title").focus();
		setDefaultDate();
		$("#inputForm").validate({
			rules: {
				title: {
					required:true
				}
			},
			messages: {
				title: {
					required: "请输入标题"
				}
			},
	        errorPlacement: function(error, element) {   
		        if (document.getElementById("error_"+element.attr("name")))  {
		            error.appendTo("#error_"+element.attr("name"));  
		        }
		        else       
		            error.insertAfter(element);   
		        }
		}); 
	});
	var htmlEditor = null;
	KindEditor.ready(function(K) {
		htmlEditor = K.create('textarea[name=desciption]', {
			cssPath : '${ctx}/KindEditor/themes/default/default.css',
			uploadJson : '${ctx}/KindEditor/jsp/upload_json.jsp',
			fileManagerJson : '${ctx}/KindEditor/jsp/file_manager_json.jsp',
			allowFileManager : true	//允许查看文件
		});
	});
	function onSubmit() {
		 if($("#inputForm").valid()){
		 if(!htmlEditor.isEmpty()){
		 	$("#desciption").val(htmlEditor.html());
		 	$("#inputForm").submit();
		 }	else{
		 	alert("内容不能为空");
		 }
		}
	}
	document.onkeydown = function(e){    
		e = e || window.event;   
		if(e.keyCode === 13){        
		 onSubmit();   
		}
	};
	function setDefaultDate(){
		if(document.getElementById('id').value==''){
			$("#publishdate").val('<%=CommonUtils.getCurrentDate("yyyy-MM-dd")%>');
		}
	}

</script>
</head>
<body>
		<%@ include file="/common/header.jsp"%>
			<%@ include file="/common/menu.jsp"%>
			<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">资讯管理</h2>
			<div class="content">
					<form id="inputForm" name="inputForm" action="news!save.action?mtype=${mtype}" method="post">
						<input type="hidden" id="id" name="id" value="${id}"/>
			    			<ul class="inputform">
								<li>以下有<b>*</b>的内容为必填项：</li>
								<li>
						  			<label>标题<b>*</b></label>
						  			<input id="title" name="title" value="${title}" type="text" maxlength="100" style="width:300px;" />
						  		</li>
						  		<li>
						  			<label>来源</label>
						  			<input id="source" name="source" value="${source}" type="text" maxlength="100" style="width:300px;" />
						  		</li>
						  		<li>
						  			<label>发布时间</label>
						  			<input id="publishdate" class="Wdate" name="publishdate" onclick="WdatePicker();" value="<s:date name="publishdate" format="yyyy-MM-dd"/>" readonly="readonly" type="text" maxlength="100" />
						  		</li>
						  		<li>
						  			<label>外部链接</label>
						  			<input id="link" name="link" value="${link}" type="text" maxlength="100" style="width:300px;" />
						  		</li>
						  		<li >
						  			<label style="vertical-align: top;">内容</label>
						  			<textarea id="desciption" name="desciption" cols="100" rows="8" style="width:750px;height:300px;visibility:hidden;">${desciption}</textarea>
				        			<span id="error_desciption"></span>
						  		</li>
							</ul>  
		 				<p class="button_box">
	 						<button class="course_btn_orange" type="button" onclick="onSubmit();">保存</button> &nbsp;
	 						<button class="course_btn_grey" type="button" onclick="location.href='news.action?mtype=0'">取消</button>&nbsp;
	 					</p>
		 			</form>
		 			</div>
		</div>
		<%@include file="/common/footer.jsp" %>
</body>
</html>