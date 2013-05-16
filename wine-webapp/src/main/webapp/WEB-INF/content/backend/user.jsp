<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//Dtd XHTML 1.0 Transitional//EN" "http://www.w3.org/tr/xhtml1/Dtd/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>网站后台-会员管理</title> 
	<%@ include file="/common/meta.jsp" %>
	<meta content="user" name="activemenu" />
	<link href="${ctx}/css/master.css" type="text/css" rel="stylesheet"/> 
	<script src="${ctx}/js/jquery.js" type="text/javascript"></script>
		<script src="${ctx}/js/jcommon.js" type="text/javascript"></script>
	<script  type="text/javascript">
	$(document).ready(function() {
		$("#checkall").click(function(){
 			$("input[name='ids']").attr("checked",$(this).attr("checked"));
 		});
	});
	function onBatchDelete(){
		var  isChecked=false;
		$("input[name='ids']:checked").each(function(){
			isChecked = true;
		});
		if(!isChecked){
			alert("选择删除的记录");
			return false;
		}

		if(confirm("确认删除？")){
			$("#mainForm").attr("action","user!batchDelete.action?mtype=${mtype}").submit();
		}
	}
	function onPublish(){
		var  isChecked=false;
		$("input[name='ids']:checked").each(function(){
			isChecked = true;
		});
		if(!isChecked){
			alert("选择发布的记录");
			return false;
		}

		if(confirm("确认发布？")){
			$("#mainForm").attr("action","user!publish.action").submit();
		}
	}
	function onCancel(){
		var  isChecked=false;
		$("input[name='ids']:checked").each(function(){
			isChecked = true;
		});
		if(!isChecked){
			alert("选择取消发布的记录");
			return false;
		}

		if(confirm("确认取消发布？")){
			$("#mainForm").attr("action","user!cancelPublish.action").submit();
		}
	}
	function onBatchLocked(){
		var  isChecked=false;
		$("input[name='ids']:checked").each(function(){
			isChecked = true;
		});
		if(!isChecked){
			alert("选择需锁定的记录");
			return false;
		}

		if(confirm("确认锁定？")){
			$("#mainForm").attr("action","user!batchLocked.action").submit();
		}
	}
	function onBatchClear(){
		var  isChecked=false;
		$("input[name='ids']:checked").each(function(){
			isChecked = true;
		});
		if(!isChecked){
			alert("选择需解锁的记录");
			return false;
		}

		if(confirm("确认取消锁定？")){
			$("#mainForm").attr("action","user!batchClear.action").submit();
		}
	}
	</script>
</head>
<body>
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/menu.jsp"%>
	<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">会员管理</h2>
			<div class="pagehead01"></div>
					<div id="message" style="line-height: 35px;">
						<s:actionmessage theme="custom" cssClass="tipbox"/>
					</div>
	    			<form id="mainForm" action="user.action" method="post">
						<div class="box_search">
							<span>				
									<label class="ml20">用户名</label>
									<input type="text" id="loginName" name="filter_LIKES_loginName" size="20" value="${param['filter_LIKES_loginName']}"/>
									<label class="ml20">姓名</label>
									<input type="text" id="name" name="filter_LIKES_name" size="20" value="${param['filter_LIKES_name']}"/>
									<label class="ml20">状态</label>
			                     	<s:select list="#{'true':'已锁定','false':'未锁定'}"  id="userlock" name="filter_EQB_userlock"  value="#parameters.filter_EQB_userlock" cssStyle="width:85px;" headerKey="" headerValue="--全 部--"/> 
							</span>
							<input class="rr" value="搜 索" type="submit" />
						</div> 
						<p class="ll mt15">
		    				<button id="btnCreate" type="button" onclick="window.location.href='user!input.action'">新增</button>
		    				<button id="btnDelete" type="button" onclick="onBatchDelete();">删除</button>
		    				<button id="btnLocked" type="button" onclick="onBatchLocked();">锁定</button>
		    				<button id="btnClear" type="button" onclick="onBatchClear();">解锁</button>
	    				</p>
						<div class="tablebox">
	        				<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebg font333 ll collapse">
							    <tr>
							    	<th width="5%"><input type="checkbox" id="checkall" name="checkall"/></th>
							        <th width="20%">用户名</th>
							        <th width="20%">姓名</th>
							        <th width="10%">状态</th>
							        <th width="15%">创建时间</th>
							        <th width="30%">操作</th>
							    </tr>
			    				<s:iterator value="page.result" status="st">
								   <tr>
								   		<td title=""><input type="checkbox" id="ids" name="ids" value="${id}"/></td>
								   		<td >${loginName}</td>
								        <td >${name}</td>
								        <td >${statusName}</td>
								        <td><s:date name="createDate" format="yyyy-MM-dd HH:mm"/></td>
								        <td>
								        	<a href="user!input.action?id=${id}">修改</a>
								        	<a href="javaScript:delRecord('user!delete.action?id=${id}');">删除</a>
								        	<a href="user!deaultUserPassword.action?id=${id}">重置密码</a>
								        </td>
								    </tr>
							    </s:iterator>
							    <s:if test="page.result.size ==0">
									<tr> 
										<td height="30" align="center" colspan="8"><font color="gray">没有符合条件的记录</font></td>
									</tr>  
								</s:if>      
		    				</table>
		    				<%@ include file="/common/page.jsp"%>
		    			</div>
					</form>   
	</div>
	<%@ include file="/common/footer.jsp" %>

<!-- wrapper  end-->
</body>
</html>