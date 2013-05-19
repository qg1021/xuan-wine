<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/meta.jsp" %>
	<title>网站后台-留言公告</title>
	<meta content="notice" name="activemenu" />
	<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/js/jcommon.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#inputForm").validate({
			rules: {
				content: {
					required:true,
					maxlength:200
				}
			},
			messages: {
				content: {
					required: "请输入回复内容",
					maxlength:"字数不能超过200"
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
	function  onDelete(id) {
		if(confirm("确认要删除?")) {
			$.post("notice!deleteChild.action",{"id":id},function(data) {
				
				if(data=='删除成功'){
					$("#child-"+id).remove();
				}
				
			});
		}
	}


</script>
</head>
<body>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/menu.jsp"%>
	<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">留言公告</h2>
			<div id="content" class="content">
					<div>
						留言标题：${title}
					</div>
					<div>
						留言内容：${content}
					</div>
    				<s:iterator value="childs">
	    				<div id="child-${id}" style="margin-top:5px;">
	    					<div style="vertical-align: top;" class="rr">
	    						回复时间：<s:date name="createdate" format="yyyy-MM-dd HH:mm"/>
	    						<span id="button_${id}">
					        		<a style='cursor:pointer;'  onclick="onDelete(${id});">删除</a>
					        	</span>
	    					</div>
	    					回复内容：
	    					<div>${content}</div>
	    				</div>
    				</s:iterator>
    				
					<form id="inputForm" name="inputForm" action="notice!save.action" method="post">
						<input type="hidden" id="parentid" name="parentid" value="${id}"/>
				    	<ul class="inputform">
							<li>以下有<b>*</b>的内容为必填项：</li>
					  		<li >
					  			<label style="vertical-align: top;">内容<b>*</b></label>
					  			<textarea id="content" name="content" cols="100" rows="8" style="width:540px;height:60px;"></textarea>
			        			<span id="error_content"></span>
					  		</li>
						</ul> 
		 				<p class="button_box">
	 						<button class="course_btn_orange" type="submit">回复</button> &nbsp;
	 						<button class="course_btn_grey" type="button" onclick="location.href='notice.action'">取消</button>&nbsp;
	 					</p>
		 			</form>
		 			</div>
		</div>
		<!-- 正文内容 end --> 
		<%@include file="/common/footer.jsp" %>
</body>
</html>