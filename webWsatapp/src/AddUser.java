import java.io.IOException;
import java.io.PrintWriter;

import java.sql.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({"/add-user"})
public class AddUser extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{
		String user = req.getParameter("user"), password = req.getParameter("password");
		PrintWriter out = res.getWriter();
		Connection con = null;
		Statement st = null;
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        if(st.executeUpdate("INSERT INTO users VALUES('" + user + "', '" + password + "')") > 0) {
	        	out.print("Account created successfull");
	        }
	        else
	        	out.print("Unnable to create account");
	        st.executeUpdate("INSERT INTO user_roles(user_id, role_name) VALUES('" + user + "', 'user')");
	        
		}
		catch(Exception e) {
			out.print("Unnable to create account");
		}
	}

}
