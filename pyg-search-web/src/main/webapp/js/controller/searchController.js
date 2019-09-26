//控制层 
app.controller('searchController', function($scope, $location, searchService) {

	// 搜索接收门户传来的参数
	$scope.searchList = function() {
		// $location.search()是内置服务,既可以接收跨域也可以接收本地的
		// 方式一: search["名字"]
		// 方式二:search.keywaods
		$scope.searchMap.keywords = $location.search()["keywords"];
		$scope.search();
	}
	// ================================================
	// 接收本页面的封装的参数,用来先后天查询数据
	// 1.定义实体封装参数
	$scope.searchMap = {
		keywords : "",
		category : "",
		brand : "",
		price : "",
		spec : {},
		sort : "ASC",
		sortField : "price",
		// 当前页码数
		page : 1,
		// 每页显示的数量
		pageSize : 10
	}
	// 2.页面通过点击事件,添加要查询的属性{"keywords":"手机","category":"","brand":"苹果","price":"2000-3000","spec":{"网络制式":"联通4G","内存":"128G"}}
	$scope.addFilterCondition = function(key, value) {
		if (key == 'category' || key == 'brand' || key == 'price') {
			$scope.searchMap[key] = value;
		} else {
			$scope.searchMap.spec[key] = value;
		}
		$scope.search();
	}
	// 点击事件，删除指定的属性
	$scope.removeSearchItem = function(key) {
		if (key == 'category' || key == 'brand' || key == 'price') {
			$scope.searchMap[key] = "";
		} else {
			delete $scope.searchMap.spec[key]
		}
		$scope.search();
	}

	// 2.查询
	$scope.search = function() {
		//前台通过输入数字查询时,绑定的是字符串,要先转换,不然后面点击下一页 +1 时会造成拼接
	
		searchService.search($scope.searchMap).success(function(data) {
			$scope.searchResult = data;// map集合,封装total和rows
			// 一查询完结果,就立马处理分页
			bulidPageLable();
		})
	}
	// 改变排序方式
	$scope.addSort = function(sort, sortfield) {
		$scope.searchMap.sort = sort;
		$scope.searchMap.sortField = sortfield;
		// 更新排序方式后,再次查询
		$scope.search();
	}

	// 分页查询
	$scope.findByPage = function(page) {
		if (page < 1 || page >= $scope.searchResult.totalPages) {
			return;
		}
		$scope.searchMap.page = parseInt(page);
		$scope.search();
	}

	// 定义方法，封装分页角标
	bulidPageLable = function() {

		// 定义数组，封装页码
		$scope.pageLable = [];

		// 获取总页码数
		var maxPage = $scope.searchResult.totalPages;

		// 显示的起始页
		var fisrtPage = 1;
		// 显示结束页
		var lastPage = maxPage;

		// 判断显示页码，进行封装
		if ($scope.searchResult.totalPages > 5) {
			// 判断当前页是否是小于5页，是否是前5页
			if ($scope.searchMap.page <= 5) {
				lastPage = 5;
				$scope.dian1 = false;
				$scope.dian2 = true;
			} else if ($scope.searchMap.page >= maxPage - 2) {
				// 判断当前页码是否是后面5页
				fisrtPage = maxPage - 4;
				$scope.dian1 = true;
				$scope.dian2 = false;
			} else {
				// 判断当前页码是否是中间页码
				fisrtPage = $scope.searchMap.page - 2;
				lastPage = $scope.searchMap.page + 2;
				$scope.dian1 = true;
				$scope.dian2 = true;

			}

		}

		// 封装页码
		for (var i = fisrtPage; i <= lastPage; i++) {

			$scope.pageLable.push(i);
		}

		// 判断第一页和最后一页的时候,上下页面不可用
		$scope.isTopPage = function(page) {
			if ($scope.searchMap.page == 1) {
				return true;
			}
			return false;
		}
		$scope.isLastPage = function() {
			if ($scope.searchMap.page == $scope.searchResult.totalPages) {
				return true;
			}
			return false;
		}

	};

});
