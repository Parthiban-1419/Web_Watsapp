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
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		JSONObject json = new JSONObject(), jChat = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		try {
			Class.forName("org.postgresql.Driver");
	        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
	        st = con.createStatement(); 
	        result = st.executeQuery("SELECT DISTINCT(sender) FROM chats WHERE receiver = '" + user + "' UNION SELECT DISTINCT(receiver) FROM chats WHERE sender='" + user + "'");
	        
	        json.put("action", "chatHistory");
	        while(result.next()) {
	        	jChat = new JSONObject();
	        	jChat.put("index" , count++);
	        	jChat.put("friend" , result.getString("sender"));
	        	jChat.put("chat", getMessages(user, result.getString("sender")));
	        	jArray.add(jChat);
	        }
//	        out.print(jArray);
	        
	        json.put("chats", jArray);
	        con.close();
	        result.close();
		}
		catch(Exception e) {}
		
		return json;
		
	}
	
	private JSONArray getMessages(String user, String other) throws SQLException, ClassNotFoundException {
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
	
	void storeMessage(JSONObject json) throws ClassNotFoundException, SQLException {
		Connection con = null;
		Statement st = null;
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        st.executeUpdate("INSERT INTO chats(sender, receiver, message, sent_time) VALUES('" + json.get("sender") + "', '" + json.get("receiver") + "', '" + json.get("message") + "', " + json.get("sent_time") + ")");
        
	}
	
	void createNewChat(String user, String friend) throws ClassNotFoundException, SQLException {
		Connection con = null;
		Statement st = null;
		ResultSet result = null;
		JSONObject json = new JSONObject();
		
		Class.forName("org.postgresql.Driver");
        con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/watsapp", "postgres", "Parthi12345*");
        st = con.createStatement(); 
        
        result = st.executeQuery("SELECT user_id FROM users WHERE user_id='" + friend + "'");
        
        if(result.next()) {
        	json.put("action", "newChat");
        	json.put("friend", friend);
        }
        else
        	json.put("action", "friendNotFound");
        
        sendToSession(sessions.get(user), json);
        
	}
}
