

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet({"/check-number"})
public class CheckNumber extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Long number = Long.parseLong(req.getParameter("number"));
		
		PrintWriter out = res.getWriter();
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        
	        result = st.executeQuery("SELECT number FROM user_detail WHERE number = '" + number + "'");
	        if(result.next())
	        	out.print(true);
	        else
	        	out.print(false);
		}
		catch(Exception e) {
			out.print(e);
		}
	}

}
