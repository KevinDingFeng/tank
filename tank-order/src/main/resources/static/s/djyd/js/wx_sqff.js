//url取参
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}
var _type = getQueryString("type")

var _user_name = sessionStorage.getItem('username');
if(_user_name == null || _user_name == "" || _user_name == undefined){
	var _appid = "wxf6169e3acc1795a1";// appid
//	var _redirect_uri = "http%3A%2F%2Ftank.dazonghetong.com%2Fauth%2Fcallback";//回调页面
	var _redirect_uri = encodeURI("http://tank.dazonghetong.com/s/djyd/cx_ff.html?type="+_type);//回调页面
	var _state = "STATE";//
	
	var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" 
			+ _appid + "&redirect_uri="
			+ _redirect_uri + "&response_type=code&scope=snsapi_userinfo&state="
			+ _state + "#wechat_redirect";
	window.location.href = _href;
}else{
	console.log("已经授权")
}