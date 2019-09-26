//控制层 
app.controller('cartController', function($scope, cartService) {

	// 读取列表数据绑定到表单中
	$scope.findCartList = function() {
		cartService.findCartList().success(function(response) {
			$scope.list = response;

			// list 是购物车查询的所有商品,需要循环计算每个商品金额加起来
			$scope.totalValue = cartService.sum($scope.list);

		});
	}

	// 修改购物车里这个商品的数量---本质是调用addGoodsToCartList()方法,参数传递数量
	$scope.updateGoodsNum = function(itemId, num) {
		cartService.updateGoodsNum(itemId, num).success(function(data) {
			if (data.success) {
				// 修改成功,重修刷新页面
				$scope.findCartList();
			} else {
				// 修改失败
				alert("修改失败");
			}
		})
	}

	// 显示当前登录账户的名字
	$scope.showUsername = function() {
		cartService.showUsername().success(function(data) {
			$scope.username = data;
		})
	}
	

});
