//定义模板,并引入分页组件pagination
var app = angular.module("pinyougou", []);
// 定义一个anular.js的过滤器,处理html
app.filter("trusthtml", [ '$sce', function($sce) {
	return function(data) {
		return $sce.trustAsHtml(data);
	}
} ])