//服务层
app.service('payService',function($http){
	    	
	//生成支付金额方法
	this.creatQrCode=function(){
		return $http.get('../pay/creatQrCode');		
	}
	
	//查看这个订单号支付状态--
	this.checkPayStatus=function(out_trade_no){
		return $http.get('../pay/checkPayStatus?out_trade_no='+out_trade_no);		
	}
	
});
