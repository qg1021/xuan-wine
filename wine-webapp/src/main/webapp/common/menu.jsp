<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %> 
<script type="text/javascript">
	$(document).ready(function() { 
		var activemenu = $('meta[name="activemenu"]').attr("content");
    	if(!activemenu)
    		activemenu = $('#activemenu').val();
		if(activemenu){
    		var tag = $('#' + activemenu);
			tag.addClass("curr");
		}
	});
</script>
<div class="w">
<div id="nav">
	<ul id="navitems">
	  <li id="index"><a href="index.action">首页</a> </li>
	  <li id="ad_0" ><a href="links.action?mtype=0">左广告位</a> </li>
	  <li id="ad_1" ><a href="links.action?mtype=1">右广告位</a></li>
	  <li id="user"  ><a href="user!input.action">修改密码</a> </li>
	  <li id="nav-auction" ><a onclick="if (confirm('确定要退出吗？')) return true; else return false;" href="${ctx}/j_spring_security_logout" >退出系统</a> </li>
	</ul>
</div>
</div>