<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>
</head>
<body>
	<%@page import="ord.example.User"%>
	<%
		 String user = request.getRemoteUser();
		 User u = User.getObj();
		 u.setUser(user);
         
		 
		 String site = new String("http://localhost:4200/app-home");
         response.setStatus(response.SC_MOVED_TEMPORARILY);
         response.setHeader("Location", site);
    %>
</body>
</html>