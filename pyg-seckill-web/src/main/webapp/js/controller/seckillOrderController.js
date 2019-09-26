 //控制层 
app.controller('seckillOrderController' ,function($scope,$location,$interval,seckillOrderService){	
	
	
    //从redis服务器初始化加载秒杀商品详情信息
	$scope.showSeckillgood=function(){
		//获取上个页面传来的id参数
		$scope.id=$location.search()["id"];
		//从redis里面查到这个id对应的商品
		seckillOrderService.showSeckillgood($scope.id).success(function(data){
			$scope.entity=data;
			//得出秒杀活动结束时间
			$scope.endTime=new Date($scope.entity.endTime).getTime();
			//得到现在时间
			$scope.nowTime=new Date().getTime();
			//得到剩余时间的总秒数
			$scope.seconds=Math.floor(($scope.endTime -$scope.nowTime)/1000);
			var time=$interval(function(){
				if($scope.seconds >0){
					//每次时间减一秒
					$scope.seconds =$scope.seconds -1;
					//时间格式化
					$scope.timeString=$scope.timeToString($scope.seconds)
					
				}else{
					//时间变为0时
					$interval.cancel(time);
				}
			},1000)
		})
	};
	
	
	//时间格式化
	$scope.timeToString=function(seconds){
		//计算天数
		$scope.day=Math.floor(seconds/(60*60*24));
		//计算小时数
		$scope.house= Math.floor(  (seconds -$scope.day*(60*60*24))  /(60*60)   )
		//计算分钟
		$scope.fen=Math.floor( (seconds -$scope.day*(60*60*24)  -$scope.house*3600)/60)
		//计算秒
		$scope.miao=seconds -$scope.day*3600*24  -$scope.house*3600 -$scope.fen*60
		//拼接时间
		var timeString =""
			
			if($scope.day >0){
				timeString =$scope.day+"天"
			}
			timeString +=$scope.house+":"+$scope.fen+":"+$scope.miao
			return timeString
			
	};
	
	
	//抢购商品,创建订单
	$scope.createOrder=function(id){
		seckillOrderService.createOrder(id).success(function(data){
			if(data.success){
				//创建订单成功--跳转到支付页面
				alert("成功!!!!!!!!!!!!!!!!!!")
				location.href = "pay.html";
//				alert(data.message)
					
			}else{
				//创建订单失败
				alert(data.message)
			}
		})
	}
	
	
	

});	
