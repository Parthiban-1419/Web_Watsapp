<html>
 <head>
	 <title>Watsapp</title>
	 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
    </style>
 </head>
	 <body><center><br><br>
		 <div class="content">
		 	<b>Please login to continue</b><br><br>
			 <form action="j_security_check" method="POST"> 
				<label for="username">Username:</label>
				<input id="username" type="text" name="j_username" value=""/><br><br>
				<label for="password">Password:</label>
				<input id="password" type="password" name="j_password"/><br><br>
				<input type="submit" value="Login" />
			 </form>
			 Already have an account? <a href="http://localhost:4200/add-user">create</a>
		 </div></center>
	 </body>
</html> 