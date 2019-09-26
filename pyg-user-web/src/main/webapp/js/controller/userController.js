//控制层 
app.controller('userController', function($scope, userService) {

	$scope.find = function() {
		alert("aaa");
	}

	// 1用户注册
	$scope.register = function() {
		// 判断两次密码是否一致
		if ($scope.password != $scope.entity.password) {
			alert("两次密码不一致");
			return;
		}
		userService.register($scope.smsCode, $scope.entity).success(
				function(data) {
					if (data.success) {
						// 注册成功后,跳转到登录页面
						location.href = "login.html";
					} else {
						alert(data.message);
					}
				})
	}

	// 2发送验证码
	$scope.getSmsCod = function(phone) {
		userService.getSmsCod(phone).success(function(data) {
			if (data.success) {
				alert(data.message);
			} else {
				alert(data.message);
			}
		})
	}
});
