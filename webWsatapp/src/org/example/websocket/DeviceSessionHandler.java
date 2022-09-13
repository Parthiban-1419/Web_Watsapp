package org.example.websocket;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import org.example.model.Device;

@ApplicationScoped
public class DeviceSessionHandler {
	
	private int deviceId = 0;
//	String user = null;
    protected static Map<String, Session> sessions = new HashMap<>();
    private final Set<Device> devices = new HashSet<>();
    
    public void addSession(String userId, Session session) throws ServletException, IOException {
    	sessions.put(userId, session);
    	sendToSession(session, getChat(userId));
    	openMessage(userId);
    }
    void openMessage(String user) {
    	JSONObject json = new JSONObject();
    	
    	json.put("action", "status");
		json.put("status", "online");
		json.put("user", user);
		
		sendToAllConnectedSessions(json);
    }

    public void removeSession(String user, Session session) {
    	sessions.remove(session);
//    	closeMessage(user);
    }
    public void closeMessage(String user) {
    	JSONObject json = new JSONObject();
    	
    	json.put("action", "status");
		json.put("status", "offline");
		json.put("user", user);
		
		sendToAllConnectedSessions(json);
    }
    
    private void sendToAllConnectedSessions(JSONObject message) {
    	for (Map.Entry<String, Session> session : sessions.entrySet()) {
            sendToSession(session.getValue(), message);
        }
    }

    protected void sendToSession(Session session, JSONObject message) {
    	try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public JSONObject getChat(String user) throws ServletException, IOException {
    	int count = 0; 
    	boolean account = true;
    	String userName = null, profile;
		Connection con = null;
		Statement st = null, st1 = null;
		ResultSet result = null, r = null;
		JSONObject json = new JSONObject(), jChat = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        st1 = con.createStatement(); 
	        
	        
	       
	        result = st.executeQuery("SELECT * FROM user_detail WHERE number='" + user + "'");
	        result.next();
	        userName = result.getString("name");
	        
	        json.put("action", "chatHistory");
	        json.put("userName", userName);
	        json.put("number", user);
	        json.put("profile", result.getObject("profile"));
//	        result = st.executeQuery("SELECT DISTINCT(sender) FROM chat_detail WHERE receiver = '" + user + "' UNION SELECT DISTINCT(receiver) FROM chat_detail WHERE sender='" + user + "'");
	        result = st.executeQuery("SELECT * FROM f_" + user + " INNER JOIN user_detail ON f_" + user + ".f_number = user_detail.number");
	        
	        
	        while(result.next()) {

	        	profile = null;
	        	r = st1.executeQuery("SELECT * FROM user_detail WHERE number = '" + result.getString("f_number") + "'");
	        	if(r.next()) {
	        		account = true;
	        		profile = (String) r.getObject("profile");
	        	}
	        	else
	        		account = false;
	        	
	        	jChat = new JSONObject();
	        	jChat.put("index" , count++);
	        	jChat.put("profile", profile);
	        	jChat.put("f_number" , result.getString("f_number"));
	        	jChat.put("friend" , result.getString("f_name"));
	        	jChat.put("user", account);
	        	jChat.put("chat", getMessages(user, result.getString("f_number")));
	        	jArray.add(jChat);
	        }
//	        out.print(jArray);
	        
	        json.put("chats", jArray);
	        con.close();
	        result.close();
		}
		catch(Exception e) {
			System.out.print(e);
		}
		
		return json;
		
	}
	
	private JSONArray getMessages(String user, String f_number) throws SQLException, ClassNotFoundException {
		JSONObject json = new JSONObject();
		JSONArray jArray = new JSONArray();
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        result = st.executeQuery("SELECT * FROM chat_detail WHERE (sender = '" + f_number + "' AND receiver = '"  + user + "') OR (sender = '"  + user + "' AND receiver = '" + f_number + "')");
        
        while(result.next()) {
        	json = new JSONObject();
        	
        	json.put("chatId", result.getObject("chat_id"));
        	json.put("sender", result.getObject("sender"));
        	json.put("receiver", result.getObject("receiver"));
        	json.put("message", result.getObject("message"));
        	json.put("sent_time", result.getObject("sent_time"));
        	json.put("delivered", result.getObject("delivered"));
        	json.put("delivered_time", result.getObject("deliver_time"));
        	json.put("read", result.getObject("read"));
        	json.put("read_time", result.getObject("read_time"));
        	
        	jArray.add(json);
        }
		con.close();
        result.close();

		return jArray;
	}
	
	void storeMessage(JSONObject json) throws ClassNotFoundException, SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet r = null;
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        st.executeUpdate("INSERT INTO chat_detail(sender, receiver, message, sent_time) VALUES('" + json.get("sender") + "', '" + json.get("receiver") + "', '" + json.get("message") + "', " + json.get("sent_time") + ")");
        r = st.executeQuery("SELECT * FROM f_" + json.get("receiver") + " WHERE f_number='" + json.get("sender") + "'");
        System.out.println("SELECT * FROM f_" + json.get("receiver") + " WHERE f_number='" + json.get("sender") + "'");
        if(!r.next()) {
        	System.out.println("not found");
        	st.executeUpdate("INSERT INTO f_" + json.get("receiver") + "(f_number, f_name) VALUES('" + json.get("sender") + "', '" + json.get("sender") + "')");
        }
	}
	
	void createNewChat(String user, String friend) throws ClassNotFoundException, SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		JSONObject json = new JSONObject();
		String f_number = null;
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        
        result = st.executeQuery("SELECT f_number FROM f_" + user + " WHERE f_name='" + friend + "'");
        if(result.next()) {
        	f_number = result.getString(1);
        	
        	json.put("action", "newChat");
        	json.put("f_number", f_number);
        	json.put("f_name", friend);
        }
        else
        	json.put("action", "friendNotFound");
        
        sendToSession(sessions.get(user), json);
        
	}
}
