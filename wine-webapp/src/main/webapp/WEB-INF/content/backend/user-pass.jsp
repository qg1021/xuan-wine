<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/meta.jsp" %>
	<title>网站后台-会员管理</title>
	<meta content="user" name="activemenu" />
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/> 
	<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet"/>
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
		<script type="text/javascript" >
		$(document).ready(function() { 
		//聚焦第一个输入框
		$("#inputForm").validate({
			rules: {
				oldpass: {
					required: true,
					equalTo: "#opass"
				},
				password: {
					required: true
				},
				qpassword: {
					required: true,
					equalTo: "#password"
				}
			},
			messages: {
				oldpass: {
					required: "输入当前密码",
					equalTo: "当前密码输入有误"
				},
				password: {
					required: "输入新密码"
				},
				qpassword: {
					required: "输入确认密码",
					equalTo: "两次密码不相同"
				}
			},errorPlacement: function(error, element) {   
 		        if (document.getElementById("error_"+element.attr("name")))  {
 		            error.appendTo("#error_"+element.attr("name"));  
 		        }else       
 		            error.insertAfter(element);   
 		        }
			});	
	});
		</script>
</head>
<body>
			<%@ include file="/common/header.jsp"%>
			<%@ include file="/common/menu.jsp"%>
			<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">修改密码</h2>
			<div id="message" style="line-height: 35px;">
				<s:actionmessage theme="custom" cssClass="tipbox"/>
			</div>
			<div class="content">
					<form id="inputForm" name="inputForm" action="user!savePass.action" method="post">
			    		<input id="id" name="id" type="hidden" value="${entity.id}" size="30"/>
			    		<input id="opass" name="opass" type="hidden" value="${entity.password}" size="30"/>
					    	<ul class="inputform">
								<li>以下有<b>*</b>的内容为必填项：</li>
					            <li> 
			            			<label>用户名<b>*</b></label>
			            			${entity.loginName}
			            		</li>
					            <li>
					             	<label>当前密码<b>*</b></label>
					             	<input id="oldpass" name="oldpass"  type="password" size="30"/><span id="error_oldpass"></span>
					            </li>
					            <li>
					             	<label>确认密码<b>*</b></label>
					             	<input id="password" name="password" type="password" size="30"/><span id="error_password"></span>
					            </li>
					            <li>
						             <label>确认密码<b>*</b></label>
						             <input id="qpassword" name="qpassword"  type="password" size="30"/><span id="error_qpassword"></span>
						        </li>
			        		</ul>
	                    <p class="button_box">
	 						<button class="course_btn_orange" type="submit">保存</button> &nbsp;
	 					</p>
		 			</form>
 				</div>
			</div>
		<!-- 正文内容 end --> 
		<%@include file="/common/footer.jsp" %>
</body>
</html>