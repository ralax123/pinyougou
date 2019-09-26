//服务层
app.service('orderService', function($http) {

	// 读取买家地址列表
	this.findAddressList = function() {
		return $http.get('../order/findAddressList');
	}

	// 查询redis购物车里的信息
	this.findCartList = function() {
		return $http.get('../order/findCartList');
	}
	// 计算购物车的总数量和总价格
	this.sum = function(cartList) {
		var totalValue = {
			totalNum : 0,
			totalMoney : 0
		}
		for (var i = 0; i < cartList.length; i++) {
			for (var j = 0; j < cartList[i].orderItemList.length; j++) {
				totalValue.totalNum += cartList[i].orderItemList[j].num;
				totalValue.totalMoney += cartList[i].orderItemList[j].totalFee;
			}
		}
		return totalValue;
	}
	
	//购物车表单提交
	this.submitOrder=function(entity){
		return  $http.post('../order/submitOrder',entity);
	}
});
