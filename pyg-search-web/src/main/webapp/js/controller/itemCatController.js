 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
		
		
		
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			//给父id赋值
			$scope.entity.parentId=$scope.parentId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
//		        	$scope.reloadList();//重新加载
					$scope.findItemCatListByParentId($scope.parentId);
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	
	
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					//重新刷新数据
					$scope.findItemCatListByParentId($scope.parentId);
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	//根据父节点查询商品
	$scope.findItemCatListByParentId=function(parentId){
		//查询的时候记录父id,方便保存
		$scope.parentId=parentId;
		itemCatService.findItemCatListByParentId(parentId).success(function(data){
			$scope.catList=data;
		})
	};
	//面包屑导航
	//默认级别是1
	$scope.grade=1;
	//没点击一次修改,调用方法
	$scope.setGrade=function(){
		$scope.grade=$scope.grade+1;
	};
	//读取级别,设置各个级别对应的面包屑的内容
	$scope.selectCatList=function(entity){
		if($scope.grade==1){
			$scope.entity_1=null;
			$scope.entity_2=null;
		}
		if($scope.grade==2){
			$scope.entity_1=entity;
			$scope.entity_2=null;
		};
		if($scope.grade==3){
			//此时$scope.entity_1还是级别为2的时候的值
			$scope.entity_2=entity;
		};
		//根据传递参数,调用查询下级
		$scope.findItemCatListByParentId(entity.id);
		
	}
    
});	
