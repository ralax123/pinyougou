//控制层 
app.controller('contentController', function($scope, $controller,
		contentService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		contentService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		contentService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		contentService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = contentService.update($scope.entity); // 修改
		} else {
			serviceObject = contentService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				// 重新查询
				$scope.reloadList();// 重新加载
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		contentService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		contentService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	}
	// 上传图片
	$scope.uploadFile = function() {
		uploadService.uploadFile().success(function(data) {
			if (data.success) {
				$scope.entity.pic = data.message;
			} else {
				alret("图片上传失败");
			}
		})
	}
	// 添加广告时下来选项里的数值是从广告分类里的
	$scope.findContentCategoryList = function() {
		contentCategoryService.findAll().success(function(data) {
			$scope.categoryList = data;
		})
	}
	// 前台广告时候有效
	$scope.adList = [];
	$scope.statusValue = [ "无效", "有效" ];

	// 通过指定categoryID查询所属广告
	$scope.findAdByCategoryId = function(categoryId) {
		contentService.findAdByCategoryId(categoryId).success(function(data) {
			$scope.adList[categoryId] = data;
		})
	};

	// 根据关键字进行搜索
	$scope.loadSearch = function() {
		window.location.href = "http://localhost:9005/search.html#?keywords="
				+ $scope.keywords;
	}

});
