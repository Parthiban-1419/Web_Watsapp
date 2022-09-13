

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add-contact")
public class AddContact extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String query = null, user = req.getParameter("user"), f_name = req.getParameter("f_name"), f_number = req.getParameter("f_number");
		PrintWriter out = res.getWriter();
		Connection con = null;
		Statement st = null;
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement();
	        
	        if(st.executeQuery("SELECT * FROM f_" + user + " WHERE f_number='" + f_number + "'").next())
	        	query = "UPDATE f_" + user + " SET f_name='" + f_name + "' WHERE f_number = '" + f_number + "'";
	        else
	        	query = "INSERT INTO f_" + user + "(f_number, f_name) VALUES('" + f_number + "', '" + f_name + "')";
	        if(st.executeUpdate(query) > 0) {
	        	out.print("Contact saved successfull");
	        }
	        else
	        	out.print("Unnable to save contact");
		}
		catch(Exception e) {
			System.out.print(e);
			out.print("Unnable to save contact");
		}
	}
}
