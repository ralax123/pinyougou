//控制层 
app.controller('orderController', function($scope, orderService) {
	// 2初始化要传递的封装的对象
	$scope.entity = {
		address : {},
		order : {
			paymentType : '1'
		}
	};

	// 1查询买家地址列表
	$scope.findAddressList = function() {
		orderService.findAddressList().success(function(response) {
			$scope.addressList = response;

			// 如果不更换默认地址的 话,changDefaultAddress方法不会触发,也就是不会传递参数,所以要
			// 初始化一个地址为默认地址
			for (var i = 0; i < $scope.addressList.length; i++) {
				if ($scope.addressList[i].isDefault == "1") {
					$scope.entity.address= $scope.addressList[i];

				}
			}
		});
	}

	// 改变默认地址
	$scope.changDefaultAddress = function(address) {
		// 先把之前地址列表都清空
		for (var i = 0; i < $scope.addressList.length; i++) {
			$scope.addressList[i].isDefault = "0";
		}
		// 在把选中的这个设为默认地址
		for (var i = 0; i < $scope.addressList.length; i++) {
			if ($scope.addressList[i] == address) {
				$scope.addressList[i].isDefault = "1";

			}
		}
		// 把选项地址封装到参数里,方便后面传递
		$scope.entity.address = address;
	}
	// 3选择付款方式
	$scope.selectPayType = function(num) {
		// 封装付款方式到order表里
		$scope.entity.order.paymentType = num;
	}
	// 4循环显示redis购物车里的商品
	$scope.findRedisList = function() {
		// 调用cartservice
		orderService.findCartList().success(function(data) {
			$scope.cartList = data;
			// 把查到的购物车数据在调用方法,计算出数量和总价格
			$scope.totalValue = orderService.sum($scope.cartList);
		})
	}
	
	//5提交订单
	$scope.submitOrder=function(){
		//把买家的一些信息先封装
		////送货地址
		$scope.entity.order.receiverAreaName=$scope.entity.address.address;
		//收货人手机
		$scope.entity.order.receiverMobile=$scope.entity.address.mobile;
		//收货人姓名
		$scope.entity.order.receiver=$scope.entity.address.contact;
		orderService.submitOrder($scope.entity).success(function(data){
			if(data.success){
				//添加成功,跳转到支付页面去
				location.href="http:localhost:9015/pay.html"
			}else{
				alert(data.message);
			}
		})
	}

});
