	//绑定controler
	app.controller("brandController", function($scope,$controller, brandService) {

		//继承
		$controller("baseController",{$scope:$scope});
		//读取列表中所有的数据
		$scope.findAll = function() {
			brandService.findAll().success(function(data) {
				$scope.list = data;
			});
		}

		//重新加载列表 数据 
		$scope.reloadList = function() {
			//切换页码   
			$scope.findPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);

		}
		//分页控件配置
		$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 10,
			perPageOptions : [ 10, 20, 30, 40, 50 ],
			onChange : function() {
				$scope.reloadList();//重新加载 
			}
		};

		//分页查询信息
		$scope.findPage = function(page, rows) {

			brandService.findPage(page, rows).success(function(data) {
				//把总记录数赋值给totalitems
				$scope.paginationConf.totalItems = data.total;
				$scope.list = data.rows;
			});
		}

		//添加品牌数据---修改也是一样
		$scope.add = function() {
			
			var obj=null;
			if ($scope.entity.id != null) {
				obj = brandService.update($scope.entity);
			} else {
				obj = brandService.add($scope.entity);
			}

			//发送请求,保存商品
		    obj.success(function(data) {
				//判断时候保存
				if (data.success) {
					//保存成功,再次刷新查询一次
					$scope.reloadList();
				} else {
					alert(data.message);
				}
			 })
		};

		//修改商品品牌
		$scope.findOne = function(id) {
			brandService.findOne(id).success(function(data) {
				$scope.entity = data;
			})
		}

		//定义方法,接收要删除 的id
		$scope.selectIds = [];
		$scope.addId = function($event, id) {
			if ($event.target.checked) {
				$scope.selectIds.push(id);
			} else {
				var idx = $scope.delectIds.indexOf(id);
				$scope.selectIds.splice(idx, 1);
			}
		}
		//批量删除
		$scope.dele = function() {
			brandService.dele($scope.selectIds).success(function(data) {
				if (data.success) {
					//删除成功,就再次刷新查询
					$scope.reloadList();
				} else {
					alert(data.message);
				}
			})
		}

	});