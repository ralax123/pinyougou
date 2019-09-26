	//绑定一个service
	app.service("loginService", function($http) {
		this.showName=function(){
			return $http.get("../login/name")
		}
	});
	