//服务层
app.service('searchService', function($http) {

	
	// 本页面查询搜索
	this.search = function(searchMap) {
		return $http.post("../search/search", searchMap);
	}

});
