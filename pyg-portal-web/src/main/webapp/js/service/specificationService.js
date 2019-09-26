	//绑定一个service
	app.service("specificationService", function($http) {
		//查询所有商品
		this.findAll = function() {
			return $http.get("../specification/findAll");
		};

		//分页查询商品
		this.findPage = function(page, rows) {
			return $http.get("../specification/findPage?page=" + page + "&rows=" + rows);
		};

		//添加
		this.add = function(entity) {
			return $http.post("../specification/add" , entity)
		};
		//修改
		this.update = function(entity) {
			return $http.post("../specification/update" , entity)
		};

		//查询单个商品
		this.findOne = function(id) {
			return $http.get("../specification/findOne?id=" + id);
		}

		//删除商品
		this.dele = function(ids) {
			return $http.get("../specification/delete?ids=" + ids);
		};
		//查询数据,显示select2 下拉列表
		this.findSpecList=function(){
			return $http.get("../specification/findSpecList");
			
		}
		
		

	});
	