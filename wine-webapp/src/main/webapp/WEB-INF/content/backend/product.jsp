<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/common/taglibs.jsp" %>
<%@page import="com.gm.wine.contant.Global"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@ include file="/common/meta.jsp" %>
	<title>东方农机商城(www.machine1688.com)-产品管理</title>
	<meta content="product" name="activemenu" />
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
			$("#mainForm").attr("action","product!batchDelete.action").submit();
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
			$("#mainForm").attr("action","product!publish.action").submit();
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
			$("#mainForm").attr("action","product!cancelPublish.action").submit();
		}
	}

	
	</script>
</head>
<body> 
	<%@ include file="/common/header.jsp"%>
	<%@ include file="/common/menu.jsp"%>
	<div class="w">
			<h2 class="mb10 box_solid_bottom color9C04">产品管理</h2>
			<div class="pagehead01"></div>
					<div id="message" style="line-height:20px;">
						<s:actionmessage theme="custom" cssClass="tipbox"/>
					</div>
	    			<form id="mainForm" action="product.action" method="post">
						<div class="box_search">
							<span>				
								<label class="ml20">产品名称</label>
								<input type="text" id="name" name="filter_LIKES_name" size="30" value="${param['filter_LIKES_name']}"/>
								<label class="ml20">状态</label>
				                <s:select list="#{'true':'已发布','false':'未发布'}"  id="status" name="filter_EQB_status"  value="#parameters.filter_EQB_status" cssStyle="width:85px;" headerKey="" headerValue="--全 部--"/> 
							</span>
								<input class="button01 w60 rr forsubmit" value="搜 索" type="submit" />
						</div>
						<p class="ll mt15">
		    				<button id="btnCreate" type="button" onclick="window.location.href='product!input.action'">新增</button>
		    				<button id="btnDelete" type="button" onclick="onBatchDelete();">删除</button>
		    				<button id="btnPublish" type="button" onclick="onPublish();">发布</button>
		    				<button id="btnCancel" type="button" onclick="onCancel();">取消发布</button>
	    				</p>
						<div class="tablebox">
		        			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="tablebg font333 ll collapse">
							    <tr>
							    	<th width="5%"><input type="checkbox" id="checkall" name="checkall"/></th>
							        <th width="15%">名称</th>
							        <th width="55%">产品图片</th>
							        <th width="10%">是否发布</th>
							        <th width="15%">操作</th>
							    </tr>
			    				<s:iterator value="page.result" status="st">
								   <tr>
								   		<td title=""><input type="checkbox" id="ids" name="ids" value="${id}"/></td>
								        <td title="${name}">
								        	<common:cut string="${name}" len="20"/>
								        </td>
										<td><img src="..<%=Global.picpath%>/${picurl}" width="150" height="50"/></td>
								        <td>${statusName}</td>
								        <td>
								        	<a href="product!input.action?id=${id}">修改</a>&nbsp;&nbsp;
								        	<a href="javaScript:delRecord('product!delete.action?id=${id}');">删除</a>
								        </td>
								    </tr>
							    </s:iterator>
							    <s:if test="page.result.size ==0">
									<tr> 
										<td height="30" align="center" colspan="9"><font color="gray">没有符合条件的记录</font></td>
									</tr>  
								</s:if>      
			    			</table>
		    			</div>
		    			<%@ include file="/common/page.jsp"%>
					</form>   
		</div>
		<!-- 正文内容 end --> 
	      <%@include file="/common/footer.jsp" %>
</body>
</html>