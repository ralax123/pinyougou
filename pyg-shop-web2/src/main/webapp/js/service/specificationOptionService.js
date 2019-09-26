	//绑定一个service
	app.service("specificationOptionService", function($http) {
		//查询所有商品
		this.findAll = function() {
			return $http.get("../specificationOption/findAll");
		};

		//分页查询商品
		this.findPage = function(page, rows) {
			return $http.get("../specificationOption/findPage?page=" + page + "&rows=" + rows);
		};

		//添加
		this.add = function(entity) {
			return $http.post("../specificationOption/add" , entity)
		};
		//修改
		this.update = function(entity) {
			return $http.post("../specificationOption/update" , entity)
		};

		//查询单个商品
		this.findOne = function(id) {
			return $http.get("../specificationOption/findOne?id=" + id);
		}

		//删除商品
		this.dele = function(ids) {
			return $http.get("../specificationOption/delete?ids=" + ids);
		}


	});
	