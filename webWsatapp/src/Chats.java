

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@WebServlet("/get-chats")
public class Chats extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String user = req.getParameter("user");
		PrintWriter out = res.getWriter();
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        result = st.executeQuery("SELECT DISTINCT(sender) FROM chats WHERE receiver = '" + user + "' UNION SELECT DISTINCT(receiver) FROM chats WHERE sender='" + user + "'");
	        
	        while(result.next()) {
	        	json = new JSONObject();
	        	json.put("sender", result.getString("sender"));
	        	json.put("chat", getChat(user, result.getString("sender")));
	        	jArray.add(json);
	        }
	        out.print(jArray);
	        
	        con.close();
	        result.close();
		}
		catch(Exception e) {
			out.print(e);
		}
		
	}
	
	JSONArray getChat(String user, String other) throws SQLException, ClassNotFoundException {
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        result = st.executeQuery("SELECT * FROM chats WHERE (sender = '" + other + "' AND receiver = '"  + user + "') OR (sender = '"  + user + "' AND receiver = '" + other + "')");
        
        while(result.next()) {
        	json = new JSONObject();
        	
        	json.put("chatId", result.getObject("chat_id"));
        	json.put("sender", result.getObject("sender"));
        	json.put("receiver", result.getObject("receiver"));
        	json.put("message", result.getObject("message"));
        	json.put("sent_time", result.getObject("sent_time"));
        	json.put("delivered_time", result.getObject("delivered_time"));
        	json.put("read_time", result.getObject("read_time"));
        	
        	jArray.add(json);
        }
		con.close();
        result.close();
		
		return jArray;
	}

}
