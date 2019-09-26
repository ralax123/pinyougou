//控制层 
app.controller('goodsController', function($scope, $controller, goodsService,
		specificationOptionService, uploadService, itemCatService,
		typeTemplateService) {

	$controller('baseController', {
		$scope : $scope
	});// 继承

	// 读取列表数据绑定到表单中
	$scope.findAll = function() {
		goodsService.findAll().success(function(response) {
			$scope.list = response;
		});
	}

	// 分页
	$scope.findPage = function(page, rows) {
		goodsService.findPage(page, rows).success(function(response) {
			$scope.list = response.rows;
			$scope.paginationConf.totalItems = response.total;// 更新总记录数
		});
	}

	// 查询实体
	$scope.findOne = function(id) {
		goodsService.findOne(id).success(function(response) {
			$scope.entity = response;
		});
	}

	// 保存
	$scope.save = function() {
		var serviceObject;// 服务层对象
		if ($scope.entity.id != null) {// 如果有ID
			serviceObject = goodsService.update($scope.entity); // 修改
		} else {
			// 保存之前先把富文本里面的值取出来
			$scope.entity.goodsDesc.introduction = editor.html();
			serviceObject = goodsService.add($scope.entity);// 增加
		}
		serviceObject.success(function(response) {
			if (response.success) {
				alert(response.message);
				// 把其他填写的内容清空
				$scope.entity = {};
				// 清空富文本里面的内容
				editor.html('');
			} else {
				alert(response.message);
			}
		});
	}

	// 批量删除
	$scope.dele = function() {
		// 获取选中的复选框
		goodsService.dele($scope.selectIds).success(function(response) {
			if (response.success) {
				$scope.reloadList();// 刷新列表
				$scope.selectIds = [];
			}
		});
	}

	$scope.searchEntity = {};// 定义搜索对象

	// 搜索
	$scope.search = function(page, rows) {
		goodsService.search(page, rows, $scope.searchEntity).success(
				function(response) {
					$scope.list = response.rows;
					$scope.paginationConf.totalItems = response.total;// 更新总记录数
				});
	};

	// 显示顶级分类下拉选项
	$scope.selectItemCat1List = function() {
		itemCatService.findItemCatListByParentId(0).success(function(data) {
			$scope.itemList1 = data;
		})
	};
	// 动态获取第二级的值
	$scope.$watch('entity.goods.category1Id', function(newValue, oldValue) {
		itemCatService.findItemCatListByParentId(newValue).success(
				function(data) {
					$scope.itemList2 = data;
				})
	});
	// 查询第三级
	$scope.$watch('entity.goods.category2Id', function(newValue, oldValue) {

		itemCatService.findItemCatListByParentId(newValue).success(
				function(data) {
					$scope.itemList3 = data;
				})
	});
	// 通过第三级的id能确定这个商品
	$scope.$watch('entity.goods.category3Id', function(newValue, oldValue) {
		itemCatService.findOne(newValue).success(function(data) {
			// 将读取出的商品的分类的模板赋值
			$scope.entity.goods.typeTemplateId = data.typeId;
		})

	})

	// 通过前面查询的模板id来查询品牌的选项
	$scope.$watch('entity.goods.typeTemplateId', function(newValue, oldValue) {
		typeTemplateService.findOne(newValue).success(
				function(data) {
					$scope.typeTemplate = data;
					// 把字符串转换为json对象
					$scope.typeTemplate.brandIds = JSON
							.parse($scope.typeTemplate.brandIds)
					// 333扩展属性
					$scope.entity.goodsDesc.customAttributeItems = JSON
							.parse($scope.typeTemplate.customAttributeItems)
				});
		// 查询规格列表
		typeTemplateService.findSpecList(newValue).success(function(data) {
			$scope.specList = data;
		})

	});

	// 上传图片
	$scope.uploadFile = function() {
		uploadService.uploadFile().success(function(data) {
			if (data.success) {
				$scope.image_entity.url = data.message;
			} else {
				alert(data.message);
			}
		}).error(function() {
			alert("上传发生错误");
		});
	};
	// 页面限制上传的所有图片信息

	$scope.imgge_url_list = [];
	// 页面图片上传保存按钮触发<添加图片方法
	$scope.add_image_entity = function() {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
		$scope.imgge_url_list.push($scope.image_entity);
	}
	// 删除单个照片
	$scope.delete_image_one = function(index) {
		$scope.entity.goodsDesc.itemImages.splice(index, 1);
		$scope.imgge_url_list.splice(index, 1);

	}
	// 批量删除图片--先更新图片集合------------有bug
	$scope.update_imge_list = function($event, url) {
		if ($event.target.checked) {
			var index = $scope.imgge_url_list.indexOf(url);
			$scope.imgge_url_list.splice(index, 1);
		} else {
			$scope.imgge_url_list.push(url);
		}
	};
	// 批量删除
	$scope.delete_image_list = function() {
		$scope.entity.goodsDesc.itemImages = $scope.imgge_url_list;
	}

	$scope.entity = {
		goods : {},
		goodsDesc : {
			itemImages : [],
			customAttributeItems : [],
			specificationItems : []
		}
	}

	// $scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]} };
	// 根据规格属性选择框,动态添加规格属性
	$scope.updateSpecAttribute = function($event, name, value) {
		// searchObjectByKey调用方法,将所选的规格属性添加到响应的规格里
		var object = $scope.searchSpecAttribute(
				$scope.entity.goodsDesc.specificationItems, 'attributeName',
				name);

		if (object != null) {
			if ($event.target.checked) {
				// 选中,就把规格选项添加到数组里
				object.attributeValue.push(value);
			} else {
				// 不选中,就移除相关的规格选项
				object.attributeValue.splice(object.attributeValue
						.indexOf(value), 1);
				// 当这个规格属性都没勾选时,就移除这个对象
				if (object.attributeValue.length == 0) {
					$scope.entity.goodsDesc.specificationItems.splice(
							$scope.entity.goodsDesc.specificationItems
									.indexOf(object), 1);
				}
			}
		} else {
			$scope.entity.goodsDesc.specificationItems.push({
				"attributeName" : name,
				"attributeValue" : [ value ]
			});
		}
	}

	// 抽取一个方法,判断所选规格属性时候属于规格
	$scope.searchSpecAttribute = function(list, key, value) {
		for (var i = 0; i < list.length; i++) {
			if (list[i][key] == value) {
				return list[i];
			}
		}
		// 都没有匹配的,返回null
		return null;
	};

	// 创建sku行保存数据
	// 1把每一行有哪些字段
	$scope.creatSKUTable = function() {
		// [{"attributeName":"网络","attributeValue":["移动3G","移动4G","联通3G"]},
		// {"attributeName":"机身内存","attributeValue":["16G","32G"]}]
		$scope.entity.itemList = [ {
			spec : {},
			price : 0,
			num : 9999,
			status : 0,
			isDefault : 0
		} ];
		// 获取规格选项的集合
		var optionList = $scope.entity.goodsDesc.specificationItems;

		for (var i = 0; i < optionList.length; i++) {
			$scope.entity.itemList = creatRow($scope.entity.itemList,
					optionList[i].attributeName, optionList[i].attributeValue);
		}
	};
	// 创建行的方法--不同的参数组合成不同的行
	creatRow = function(list, name, values) {
		var newList = [];
		for (var i = 0; i < list.length; i++) {
			var oldRow = list[i];
			for (var j = 0; j < values.length; j++) {
				// 获取行
				// 新建一行
				var newRow = JSON.parse(JSON.stringify(oldRow))
				// 向新行里添加数据
				newRow.spec[name] = values[j];
				// 把新的行放到list里
				newList.push(newRow);
			}
		}
		return newList;
	}
	// 把分类的状态由数字转换为汉字显示给前台
	$scope.itemCatList = [];
	$scope.specNumToWord = function() {
		itemCatService.findAll().success(function(data) {
			for (var i = 0; i < data.length; i++) {
				$scope.itemCatList[data[i].id] = data[i].name;
			}
		})
	};
	//定义一个状态数组,将状态由数字变为汉字显示前台
	$scope.statusList=['未审核','审核通过','审核不通过','关闭'];

});
