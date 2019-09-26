<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
	<#assign linkman="周先生">
	<#assign info={"mobile":"139556767654","address":"上海传智播客"}>
	
	 <#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<body>
	<h1>你好${name}</h1>
	<hr/>

	assign指定:联系人:${linkman}
	<hr/>

	assign指令:定义对象 电话:${info.mobile} 地址:${info.address}
	<hr/>
	
	include 指令
	<#include "head.ftl" >
	<hr/>

	if指令测试
	<#if  flag==1>
			1111
		<#elseif flag==2>
			2222
		<#else>
			333
	</#if>
	<hr/>


	list指令测试<br/>
	<#list goodlist as good>
		 ${good_index+1} 商品名称： ${good.name} 价格：${good.price}<br>
	</#list>
	商品数据总数: ${goodlist?size}

	<hr/>
	<#assign data=text?eval />
	json对象
   	开户行：${data.bank}  账号：${data.account}
	<hr/>

	日期格式
	${date?date}
	只看时间=${date?time}
	日期时间都看=${date?datetime} 
	自定义日期格式=${date?string("yyyy年啊MM月啊dd日啊")}

	<hr/>
	数字转换为字符串:${num?c}
	<hr/>

	<#if aaa??>
		aaa是存在的
	 <#else>
		aaa不存在aaaaaaaaaaaaaaaaaaaaa
	</#if>
	<br/>
	aaa:${aaa?default("")}<br/>
	ccc:${aaa!"morezhiccc"}<br/>
	${num*2}<br/>
	<#if boolean >
			bbbbbbbbbbbbbb
	
	</#if>

	
	

</body>
</html>