var InterValObj; //timer变量，控制时间
var count = 30; //间隔函数，1秒执行
var curCount;//当前剩余秒数
var code = ""; //验证码
var word1 ="尊敬的注册用户，手机验证码：";
var word2 =",感谢您的使用！如非本人操作，请忽略。";
var codeLength = 6;//验证码长度
function sendMessage() {
	curCount = count;
	var phone=$(".phone").val();//手机号码
    var re = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;//手机号码
	if(!re.test(phone)){
		$(".phone").attr("placeholder","手机号格式不正确");//验证手机号
		$(".phone").addClass("phone1");//手机号错误更改样式
	}else{
		
		//产生验证码
		for (var i = 0; i < codeLength; i++) {
			code += parseInt(Math.random() * 9).toString();
		}
		$(".btn").removeAttr("disabled");//移除注册按钮限制
		$(".phone").removeClass("phone1");//移除样式

		//设置button效果，开始计时
		$("#setcode").attr("disabled", "true");
		$("#setcode").val("剩余" + curCount + "秒");
		InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次

	//向后台发送处理数据
		$.ajax({
		headers: {
            "Accept" : "text/plain; charset=utf-8",
            "Content-Type": "text/plain; charset=utf-8"
        },
			
			type: "GET", 
			dataType: "text", //数据格式:JSON
			url: 'http://vps1.taoware.com/notify', //目标地址
			data: "mobile=" + phone + "&message="+ word1 + code + word2,
			error: function (XMLHttpRequest, textStatus, errorThrown) { },
			success: function (msg){ }
		});
	}
}
//timer处理函数
function SetRemainTime() {
	if (curCount == 0) {                
		window.clearInterval(InterValObj);//停止计时器
		$("#setcode").removeAttr("disabled");//启用按钮
		$("#setcode").val("重新发送");
		code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效    
	}
	else {
		curCount--;
		$("#setcode").val("剩余" + curCount + "秒");
	}
}


//注册页面跳转
function submit(){
	var passcode = $(".password").val();
	if (passcode == code){
        document.registerForm.submit();
	} else{
		alert("验证码输入错误");
		/*$(".password").addclass(".phone1");*/
	}
	
};





