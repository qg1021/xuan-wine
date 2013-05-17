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
		$("#loginName").focus();
		//为inputForm注册validate函数
		jQuery.validator.addMethod("valusername", function(value, element) {
			var username = $.trim($("#loginName").val());
			var re =/^[A-Za-z]{1}[0-9A-Za-z]{2,}$/ ;
			if (!re.test(username)){ 
 		    	return false;
 		    }
 		    return true;
		}, "由英文和数字组成，首位为英文字母");
		$("#inputForm").validate({
			rules: {
				loginName: {
					required: true,
					minlength:6,
					maxlength:20,
					valusername:true,
					remote: "user!checkUserName.excsec?oldUserName=" + encodeURIComponent('${loginName}')
				},
				name: {
					required: true,
					maxlength:8
				},
				password: {
					required: true,
					minlength:6,
					maxlength:18
				},
				qpassword: {
					required: true,
					equalTo: "#password"
				}
			},
			messages: {
				loginName: {
					required: "输入用户名",
					minlength:"长度不小于6位",
					maxlength:"长度不大于20位",
					remote: "此用户名已存在"
				},
				name: {
					required: "输入姓名",
					maxlength:"长度不大于10位"
				},
				password: {
					required: "输入密码",
					minlength:"长度不小于6位",
					maxlength:"长度不大于20位"
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
			<h2 class="mb10 box_solid_bottom color9C04">会员管理</h2>
			<div class="content">
					<form id="inputForm" name="inputForm" action="user!save.action" method="post">
			    		<input id="id" name="id" type="hidden" value="${id}" size="30"/>
					    	<ul class="inputform">
								<li>以下有<b>*</b>的内容为必填项：</li>
					            <li> 
					            	<label>用户名<b>*</b></label>
					            	<input id="loginName" name="loginName" value="${loginName}" type="text" size="30"/><span id="error_loginName"></span>
					            </li>
					            <li>
					             	<label>姓名<b>*</b></label>
					             	<input id="name" name="name" value="${name}" maxlength="15" type="text" size="30"/><span id="error_name" ></span>
					            </li>
					            <s:if test="id==null||id==''">
					            <li>
					             	<label>密码<b>*</b></label>
					             	<input id="password" name="password" value="" type="password" size="30"/><span id="error_password"></span>
					            </li>
					            <li>
					             	<label>确认密码<b>*</b></label>
					             	<input id="qpassword" name="qpassword" value="" type="password" size="30"/><span id="error_qpassword"></span>
					            </li>
					            </s:if>
					            <s:else>
					            	<li style="display:none">
					             		<label>密码<b>*</b></label>
					             		<input id="password" name="password" value="${password}" type="password" size="30"/>
						            </li>
						            <li style="display:none">
						             	<label>确认密码<b>*</b></label>
						             	<input id="qpassword" name="qpassword" value="${password}" type="password" size="30"/>
						            </li>
					            </s:else>
			        		</ul>
	                    <p class="button_box">
	 						<button class="course_btn_orange" type="submit">保存</button> &nbsp;
	 						<button class="course_btn_grey" type="button" onclick="location.href='user.action'">取消</button>&nbsp;
	 					</p>
		 			</form>
 				</div>
			</div>
		<!-- 正文内容 end --> 
		<%@include file="/common/footer.jsp" %>
</body>
</html>