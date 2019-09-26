//控制层 
app.controller('seckillGoodsController', function($scope, seckillGoodsService) {

	// 读取秒杀商品的商品列表
	$scope.findKillGoodsList = function() {
		seckillGoodsService.findKillGoodsList().success(function(response) {
			$scope.skillGoodsList = response;
		});
	}

	// 所选商品的id集合
	// 定义方法,接收要删除 的id
	$scope.selectIds = [];

	$scope.addId = function($event, id) {
		if ($event.target.checked) {
			$scope.selectIds.push(id);
		} else {
			var idx = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(idx, 1);
		}
	}

	// 秒杀商品审核通通过
	$scope.updateStatus = function(status) {
		seckillGoodsService.updateStatus($scope.selectIds,status).success(function(data) {
			if (data.success) {
				// 修改状态成功
				// 重新加载页面
				$scope.findKillGoodsList();
			} else {
				// 修改状态失败
				alert(data.message);
			}
		})
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 查询实体
	$scope.findOne = function(id) {
		seckillGoodsService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = seckillGoodsService.update($scope.entity); // 修改
		} else {
			serviceObject = seckillGoodsService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
				$scope.reloadList();// 重新加载
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		seckillGoodsService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

});
