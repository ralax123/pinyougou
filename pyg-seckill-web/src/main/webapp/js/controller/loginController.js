//绑定controler
app.controller("loginController", function($scope,loginService) {
	
	$scope.showName=function(){
		loginService.showName().success(function(data){
			$scope.entity=data;
		})
	}




});