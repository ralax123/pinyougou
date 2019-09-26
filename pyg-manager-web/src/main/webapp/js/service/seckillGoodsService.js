//服务层
app.service('seckillGoodsService',function($http){
	    	
	//读取秒杀商品商品列表
	this.findKillGoodsList=function(){
		return $http.get('../seckillGoods/findKillGoodsList');		
	}
	
	//修改秒杀商品状态
	this.updateStatus=function(ids,status){
		return $http.get('../seckillGoods/updateStatus?ids='+ids+'&status='+status);		
	}
	
});
