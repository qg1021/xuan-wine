<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@ page import="org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter" %>
<%@ page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter" %>
<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@page import="org.springside.modules.security.springsecurity.SpringSecurityUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>登录宣酒特供客户端管理后台</title>
<%@ include file="/common/meta.jsp" %>
<link href="${ctx}/css/master.css" rel="stylesheet" type="text/css" /> 
<link href="${ctx}/js/validate/jquery.validate.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/jquery.validate.js" type="text/javascript"></script>
<script src="${ctx}/js/validate/messages_cn.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	
	//聚焦第一个输入框
	$("#j_username").focus();
	initLogin();//记住用户名
	$("#inputForm").validate({
		rules: {
			j_username: "required", 
			j_password: "required" 
		},
		messages: {
			j_username: {
				required: "请输入用户名"
			},
			j_password: {
				required: "请输入密码"
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
function onSubmit() {
	 if($("#inputForm").valid()){
		 doLogin();
	 	$("#inputForm").submit();
	 }	
}
document.onkeydown = function(e){    
	e = e || window.event;   
	if(e.keyCode === 13){        
	 onSubmit();   
	}
};
function doLogin()  
{  
	/** 实现记住密码功能 */    
    var n = $('#j_username').val();
    if($('#remeberme').attr('checked')){  
        $.cookie('username', n, {expires:7});  
    }else{  
        $.cookie('username', null);  
    }     
}
function initLogin(){
	var n = $.cookie('username');
    if(n!=null) {
        $('#j_username').val(n);
        $('#remeberme').attr('checked',"true");
    }
}
</script>
</head>
<body>
	<%@ include file="/common/header.jsp" %>
	<form  method="post" action="${ctx}/j_spring_security_check" id="inputForm">
    <div id="entry" class="w">
        <div class="mt"><h2>用户登录</h2><b></b></div>
        <div style="padding-top:20px;" class="mc">
            <div class="form">
            	<div class="item">
            		<span class="label">账户名：
            		</span><div class="fl">
                        <input type="text"  class="text"    tabindex="1" name="j_username" id="j_username">
                        <span class="clr"></span>
                        <span id="error_j_username"></span>
                    </div>
                </div>
                <div class="item">
                    <span class="label">密码：</span>
                    <div class="fl">
                        <input type="password" autocomplete="off"  tabindex="2" class="text" name="j_password" id="j_password">
                        <label id="loginpwd_succeed" class="blank invisible"></label>
                        <span class="clr"></span>
                        <span id="error_j_password"></span>
                    </div>
                </div>
                <div id="autoentry" class="item">
                    <span class="label">&nbsp;</span>
                    <div class="fl safety">
                        <label class="mar">
                        	<input type="checkbox" name="remeberme" id="remeberme" class="checkbox" onclick="doLogin();"/>
                            <label class="mar-b">记住账户名</label>
                        </label>
                    </div>
                    <div class="errormsg">   
					<%
						if (session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY) != null) {
						    if(session.getAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY).toString().contains("LockedException")){
					%>
								<font color="red">登录失败,用户被锁定.</font>
					<%
							}else{%>
								<font color="red">登录失败,用户名或密码错误.</font>
							<%}
							session.removeAttribute(AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
						}
					%>
	  			</div>
                </div>
                <div class="item">
                    <span class="label">&nbsp;</span>
                    <input type="button" tabindex="8" onclick="onSubmit();" value="登录" id="loginsubmit" class="btn-img btn-entry">
                </div>
            </div>
            <div id="guide">
            	<div class="slide" id="slide">
					<div class="slide-itemswrap">
						<div id="imgplayer"   style="display:none;">
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=2079" target="_blank" ><img src="${ctx}/images/j2.jpg" /></a>
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=2301" target="_blank" ><img src="${ctx}/images/j1.jpg" /></a>
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=740" target="_blank" ><img src="${ctx}/images/j3.jpg"  /></a>
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=741" target="_blank" ><img src="${ctx}/images/j4.jpg"  /></a>
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=1382" target="_blank" ><img src="${ctx}/images/j5.jpg" /></a>
							<a href="http://www.xuanjiu.com/Article/ShowArticle.asp?ArticleID=1384" target="_blank" ><img src="${ctx}/images/j6.jpg"  /></a>
						</div>
					</div>
				</div>
			</div>
            <span class="clr"></span>
        </div>
    </div>
	</form>
    <%@ include file="/common/footer.jsp" %>
</body>
<script type="text/javascript" src="${ctx}/js/jquery.fn.imgplayer.js"></script>
<script type="text/javascript">
		$("#imgplayer").player({
			width	: '340px',
			height  : '300px',
			showTitle : false
		}).play();

</script>
</html>

