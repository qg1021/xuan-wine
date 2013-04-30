/**
 * 验证用户名
 * @author qingang
 * @since 2011-02-14
 */	
function onVaild(){

	if(document.getElementById('userName').value==''){
			alert("请输入用户名！");
			return false;
	}else if(document.getElementById('userName').value=='demo'){
			alert("测试用户不能修改密码！");
			return false;
	}else{
		$.post("./findpwd!vaild.excsec",{"userName":$("#userName").val()},function(data) {
			var data = eval(data);
			if(data.value==""){
				document.getElementById('first_span').style.display="";
				
			}else{
				document.getElementById('first_span').style.display="none";
				document.getElementById('first_div').style.display="none";
				document.getElementById('second_div').style.display="";
				document.getElementById('answer_span').innerHTML=data.value;
			}
		});
	}
}
/**
 * 密码问题验证
 * @author qingang
 * @since 2011-02-14
 */
function onAnswer(){
	if(document.getElementById('answer').value==''){
			alert("请输入密保答案！");
			return false;
	}else{
		$.post("./findpwd!answer.excsec",{"userName":$("#userName").val(),"answer":$("#answer").val()},function(data) {
			var data = eval(data);
			if(data.value==""){
				document.getElementById('second_span').style.display="";
				
			}else{
				document.getElementById('second_span').style.display="none";
				document.getElementById('second_div').style.display="none";
				document.getElementById('third_div').style.display="";
				document.getElementById('nick_span').innerHTML=data.value;
			}
		});
	}
}
/**
 * 设置新密码
 * @author qingang
 * @since 2011-02-14
 */
function onSetPassword(){
	var passw = document.getElementById('pwd').value
	if(document.getElementById('pwd').value==''){
		alert("请输入新密码！");
		return false;
	}else if(passw.length < 6 || passw.length > 18){
		alert("新密码至少6位，不能超过18位！");
		return false;
	}
	else if(document.getElementById('repwd').value==''){
		alert("请输入重复新密码！");
		return false;	
	}else if(document.getElementById('pwd').value!=document.getElementById('repwd').value){
		document.getElementById('third_span').style.display="";
		return false;
	}else{
		$.post("./findpwd!setPwd.excsec",{"userName":$("#userName").val(),"pwd":$("#pwd").val()},function(data) {
			var data = eval(data);
			if(data.value==""){
				alert("密码设置失败！");
				
			}else{
				document.getElementById('third_span').style.display="none";
				document.getElementById('third_div').style.display="none";
				document.getElementById('fourth_div').style.display="";
			}
		});
	}	
}