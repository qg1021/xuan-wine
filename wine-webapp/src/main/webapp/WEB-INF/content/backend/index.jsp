<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>网站后台</title> 
	<%@ include file="/common/meta.jsp" %>
	<meta content="index" name="activemenu" />
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/> 
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
</head>
<body>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/menu.jsp"%>
	<div class="w">
		<div   class="content" align="center">
			<div style="margin-top:100px;">
			<h2>欢迎进入后台！</h2>
			<s:if test="noReplyNum>0">
				<div>有<font color="red">${noReplyNum}</font>条留言需回复!<a href="notice.action?filter_EQB_tanswer=false"><font color="red">回复留言</font></a></div>
			</s:if>
			</div>
		</div>
	</div>
	<%@ include file="/common/footer.jsp" %>

<!-- wrapper  end-->
</body>
</html>