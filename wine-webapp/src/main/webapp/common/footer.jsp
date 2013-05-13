<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
	//加入收藏
	function addFavor(url,title){
		if (document.all) {  
		  		window.external.AddFavorite(url, title); 
		  } else if (window.sidebar) { 
		  		window.sidebar.addPanel(title, url,""); 
		  } else if ( window.opera && window.print ) { 
				var mbm = document.createElement('a'); 
				  mbm.setAttribute('rel','sidebar'); 
				  mbm.setAttribute('href',url); 
				  mbm.setAttribute('title',title); 
				  mbm.click(); 
		  } else { 
		  		alert("收藏失败！请使用Ctrl+D进行收藏"); 
		  }
		}

	//设为首页
	function setHomepage(pageURL) {
		if (document.all) {
			document.body.style.behavior='url(#default#homepage)';
			document.body.setHomePage(pageURL);
		}
		else if (window.sidebar) {
			if(window.netscape) {
				try {
					netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");
				}
				catch (e) {
					alert( "该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项signed.applets.codebase_principal_support 值该为true" );
				}
			}
			var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components. interfaces.nsIPrefBranch);
			prefs.setCharPref('browser.startup.homepage',pageURL);
		}
	}
	</script> 
	<div class="w w1">	
		<SPAN class="clr"></SPAN>
		<div id="footer">
			<div class="copyright"> Copyright@2013  张建军 版权所有 联系方式:18956363422</div>
		</div>
	</div>
