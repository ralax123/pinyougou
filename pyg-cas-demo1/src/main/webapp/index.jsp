<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>demo1登录成功过访问的页面</title>
</head>
<body>
	<h1>
		欢迎使用cas单点登录,你是:<%=request.getRemoteUser()%></h1>

	<a href="http://192.168.25.104:9000/cas/logout?service=http://www.jd.com">退出</a>
</body>
</html>