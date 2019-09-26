//服务层
app.service('userService', function($http) {

	// 注册
	this.register = function(smsCode, entity) {
		return $http.post("../user/register?smsCode=" + smsCode, entity);
	}

	// 发送验证码
	this.getSmsCod = function(phone) {
		return $http.get("../user/getSmsCod?phone=" + phone);
	}
});
