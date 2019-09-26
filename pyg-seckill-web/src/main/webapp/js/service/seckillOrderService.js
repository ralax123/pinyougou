//服务层
app.service('seckillOrderService',function($http){
	    	
	  //从redis服务器初始化加载秒杀商品详情信息
	this.showSeckillgood=function(id){
		return $http.get('../seckillOrder/showSeckillgood?id='+id);		
	}
	
	//创建订单
	this.createOrder=function(id){
		return $http.get('../seckillOrder/createOrder?id='+id);	
	}
});
