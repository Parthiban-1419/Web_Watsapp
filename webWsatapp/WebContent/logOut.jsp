<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>   
<%@page import="ord.example.User"%>  
<%
	
	session.invalidate();
	response.sendRedirect("http://localhost:4200");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	User u = User.getObj();
	u.setUser("null");
</body>
</html>