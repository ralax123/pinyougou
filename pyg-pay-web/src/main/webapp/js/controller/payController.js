//控制层 
app.controller('payController', function($scope,  payService) {
	
//	$scope.status={trade_state:""}

	// 页面初始化加载生成二维码图片,里面包含支付的地址
	$scope.creatQrCode = function() {
		payService.creatQrCode().success(function(data) {
			$scope.payResult = data;
			$scope.money=(data.total_fee/100);
			$scope.out_trade_no= data.out_trade_no;//订单号
			
			//生成二维码
			var qr = new QRious({
				element : document.getElementById('erweima'),
				size : 280,
				level : 'H',
				value : data.code_url
			});
			
			
			//
			//查询支付状态-成功?失败?超时?--传递一个订到号
			$scope.checkPayStatus($scope.out_trade_no);
			
			
		});
		
		
	}

	
	//查询支付状态
	$scope.checkPayStatus=function(out_trade_no){
		payService.checkPayStatus(out_trade_no).success(function(data){
			$scope.status=data;
			//判断支付状态
			if(data.trade_state=='SUCCESS'){
				//支付成功,跳转到
				location.href="paysuccess.html"
			}
			if(data.trade_state=='timeout'){
				//超时,
				location.href="payfail.html"
			}
		})
	}

});
