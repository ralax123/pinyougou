	//绑定一个service
	app.service("brandService", function($http) {
		//查询所有商品
		this.findAll = function() {
			return $http.get("../brand/findAll");
		};

		//分页查询商品
		this.findPage = function(page, rows) {
			return $http.get("../brand/findPage?page=" + page + "&rows=" + rows);
		};

		//添加
		this.add = function(entity) {
			return $http.post("../brand/add" , entity)
		};
		//修改
		this.update = function(entity) {
			return $http.post("../brand/update" , entity)
		};

		//查询单个商品
		this.findOne = function(id) {
			return $http.get("../brand/findOne?id=" + id);
		}

		//删除商品
		this.dele = function(ids) {
			return $http.get("../brand/delete?ids=" + ids);
		}
		//select2
		this.findBrandList=function(){
			return $http.get("../brand/findBrandList");
		}

	});
	