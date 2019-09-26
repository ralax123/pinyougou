//绑定controler
app.controller("baseController", function($scope) {



	// 重新加载列表 数据
	$scope.reloadList = function() {
		// 切换页码
		$scope.findPage($scope.paginationConf.currentPage,
				$scope.paginationConf.itemsPerPage);

	}
	// 分页控件配置
	$scope.paginationConf = {
		currentPage : 1,
		totalItems : 10,
		itemsPerPage : 10,
		perPageOptions : [ 10, 20, 30, 40, 50 ],
		onChange : function() {
			$scope.reloadList();// 重新加载
		}
	};

	// 定义方法,接收要删除 的id
	$scope.selectIds = [];
	$scope.addId = function($event, id) {
		if ($event.target.checked) {
			$scope.selectIds.push(id);
		} else {
			var idx = $scope.delectIds.indexOf(id);
			$scope.selectIds.splice(idx, 1);
		}
	}

});