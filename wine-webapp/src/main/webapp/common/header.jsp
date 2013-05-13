<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="org.springside.modules.security.springsecurity.SpringSecurityUtils"%>
<div id="shortcut">
	<div class="w">
		<ul class="fl lh">
			<li  class="fore1 ld"><b></b><a href="javascript:addToFavorite()">收藏</a> </li>
			<li  class="fore2"><a target="_blank" href="http://www.360top.com/">设为首页</a> </li>
		</ul>
		<ul class="fr lh">
			<li  class="fore1 ld" id="loginbar">
				<%if(!SpringSecurityUtils.getCurrentUserName().equals("anonymousUser") && request.getSession().getAttribute("loginuser")!=null){ %>
					您好,欢迎进入管理后台!
					<span>${sessionScope.loginuser.name}</span> ,<s:date name="#session.loginuser.createDate" format="yyyy-MM-dd HH:mm"/>
					<a onclick="if (confirm('确定要退出吗？')) return true; else return false;" href="${ctx}/j_spring_security_logout" >[退出]</a>
				<%}%>
					 
			</li>

		</ul>
	</div>
</div>
<div id="header" class="w">
	<div id="logo" class="ld">
		<img src="${ctx}/images/logo.png" height="90">
	</div><!--logo end-->
</div><!--header end-->