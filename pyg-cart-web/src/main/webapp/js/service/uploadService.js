//将上传方法单独定义一个service,方便后期重复使用
app.service("uploadService", function($http) {

	this.uploadFile = function() {
		var formData = new FormData();
		formData.append("file", file.files[0]);
		return $http({
			method : 'POST',
			url : "../upload",
			data : formData,
			headers : {
				'Content-Type' : undefined
			},
			transformRequest : angular.identity
		});
	}

});
