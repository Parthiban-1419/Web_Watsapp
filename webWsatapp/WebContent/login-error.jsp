<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
    <style>
    	body{
    		border: black;
	        border-style: inset;
	        position: fixed;
	        top: 150px;
			left: 350px;
	        width: 500px;
	        height: 250px;
    	}
		h4{
			color: red;
		}
    </style>
</head>
	<body>
		<center><br><br>
			<h4>Either UserName or Password is Incorrect</h4><br><br>
			<a href="http://localhost:8080/webWsatapp" type="button">Try again. </a>? or 
			<a href="http://localhost:4200/add-user" type="button">Click </a> to create new accout
		</center>	 
	</body>
</html>