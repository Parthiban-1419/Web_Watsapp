

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet({"/create-account"})
public class CreateAccount extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Long number = Long.parseLong(req.getParameter("number"));
		String user = req.getParameter("user"), password = req.getParameter("password"), about = req.getParameter("about");

		PrintWriter out = res.getWriter();
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		
		ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
		
		try {			
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        
	        if(about == "")
	        	about = "Hey there. i am using watsapp";
	        
	        if(st.executeUpdate("INSERT INTO user_detail(number, name, password, about) VALUES('" + number + "', '" + user + "', '" + password + "', '" + about + "')") > 0) {
	        	st.executeUpdate("INSERT INTO role_detail(number) VALUES('" + number + "')"); 
	        	st.executeUpdate("CREATE TABLE f_" + number + "(id SERIAL, f_number varchar(10) NOT NULL, f_name varchar(20) NOT NULL, PRIMARY KEY (id))");
		        out.print("Account created successfully");
			}
	        else
	        	out.print("Unable to create account");
		}
		catch(Exception e) {
			System.out.println("error");
			out.print(e);
			System.out.println(e);
		}
	}
}
