<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="com.gm.wine.util.CommonUtils"%>
<%@page import="com.gm.wine.contant.Global"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/meta.jsp" %>
	<title>网站后台-产品管理</title>
	<meta content="product" name="activemenu" />
	<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/css/layer.css" rel="stylesheet" type="text/css"/> 
	<link href="${ctx}/css/fancybox/jquery.fancybox-1.3.4.css" rel="stylesheet" type="text/css" media="screen" />
	<link rel="stylesheet" href="${ctx}/KindEditor/themes/default/default.css" />
	<link rel="stylesheet" href="${ctx}/KindEditor/plugins/code/prettify.css" />
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/js/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctx}/js/layer.js" type="text/javascript"></script>
	<script src="${ctx}/js/jcommon.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
	<script type="text/javascript" src="${ctx}/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/KindEditor/kindeditor-min.js"></script>
	<script type="text/javascript" src="${ctx}/KindEditor/lang/zh_CN.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		//聚焦第一个输入框
		$("a#picexample").fancybox();
		$("#title").focus();
		$("#inputForm").validate({
			rules: {
				name: {
					required:true
				},
				price:{
					required:true
				},
				picurl:{
					required:true
				}
			},
			messages: {
				name: {
					required: "请输入名称"
				},
				price:{
					required:"请输入价格"
				},
				picurl:{
					required:"请上传配图"
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
		htmlEditor = K.create('textarea[name=remark]', {
			cssPath : '${ctx}/KindEditor/themes/default/default.css',
			uploadJson : '${ctx}/KindEditor/jsp/upload_json.jsp',
			fileManagerJson : '${ctx}/KindEditor/jsp/file_manager_json.jsp',
			allowFileManager : true	//允许查看文件
		});
	});
	function onSubmit() {
		 if($("#inputForm").valid()){
		 if(!htmlEditor.isEmpty()){
		 	$("#remark").val(htmlEditor.html());
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


</script>
</head>
<body>
	<div id="fullbg"></div> 
		<!-- end JS遮罩层 --> 
		<!-- 对话框 --> 
	<div id="dialog"> 
		<div id="dialog_content"></div> 
	</div>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/menu.jsp"%>
	<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">产品管理</h2>
			<div class="pagehead01"></div>
    			<div class="content">
				<form id="inputForm" name="inputForm" action="product!save.action" method="post">
					<input type="hidden" id="id" name="id" value="${id}"/>
			    	<ul class="inputform">
						<li>以下有<b>*</b>的内容为必填项：</li>
						<li>
				  			<label>名称<b>*</b></label>
				  			<input id="name" name="name" value="${name}" type="text" maxlength="100" style="width:300px;" />
				  		</li>
				  		<li>
				  			<label>价格<b>*</b></label>
				  			<input id="price" name="price" value="${price}" type="text" maxlength="100" style="width:300px;" />
				  		</li>
				  		<li>
				  			<label>配图<b>*</b></label>
				  			<input name="file" type="file" id="file" class="input" onchange="ajaxUploadPic('../upload.excsec');"/>
	        				<span id="picspan">
	        					<s:if test="picurl!=null">
	        						<a id="picexample" href="..<%=Global.picpath%>${picurl}">预览</a>
	        					</s:if>
	        					<s:else>
	        						<a id="picexample" href="" style="display:none">查看</a>
	        					</s:else>
	        				</span>
	        				<input type="hidden" id="picurl" name="picurl" value="${picurl}"/>
	        				<input type="hidden" id="baseurl" name="baseurl" value="..<%=Global.picpath%>"/>
				  		</li>
				  		<li >
				  			<label style="vertical-align: top;">内容</label>
				  			<textarea id="remark" name="remark" cols="100" rows="8" style="width:750px;height:270px;visibility:hidden;">${remark}</textarea>
		        			<span id="error_remark"></span>
				  		</li>
					</ul>  
	 				<p class="button_box">
 						<button class="course_btn_orange" type="button" onclick="onSubmit();">保存</button> &nbsp;
 						<button class="course_btn_grey" type="button" onclick="location.href='product.action'">取消</button>&nbsp;
	 				</p>
	 			</form>
	 			</div>
		</div>
		<!-- 正文内容 end --> 
		<%@include file="/common/footer.jsp" %>
</body>
</html>