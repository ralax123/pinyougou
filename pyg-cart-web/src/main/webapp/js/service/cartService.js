//服务层
app.service('cartService', function($http) {

	// 读取列表数据绑定到表单中
	this.findCartList = function() {
		return $http.get('../cart/findCartList');
	}

	// 计算购物车总价格
	this.sum = function(list) {
		// 义数据对象，存储商品总数量，及总价格
		var totalValue = {
			totalNum : 0,
			totalPrice : 0
		};
		for (var i = 0; i < list.length; i++) {
			// 再次循环商家里面的商品
			for (var j = 0; j < list[i].orderItemList.length; j++) {
				totalValue.totalNum += list[i].orderItemList[j].num;
				totalValue.totalPrice += list[i].orderItemList[j].totalFee;
			}
		}
		return totalValue;
	}

	// 修改购物车里商品数量
	this.updateGoodsNum = function(itemId, num) {
		return $http.get('../cart/addGoodsToCartList?itemId=' + itemId
				+ "&num=" + num);
	}
	// 显示当前登录用户的名字
	this.showUsername = function() {
		return $http.get("../cart/showUsername");
	}

});
