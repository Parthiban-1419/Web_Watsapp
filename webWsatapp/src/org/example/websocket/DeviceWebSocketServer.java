package org.example.websocket;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.example.model.Device;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ord.example.User;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

@ApplicationScoped
@ServerEndpoint("/actions")	
public class DeviceWebSocketServer {
    private DeviceSessionHandler sessionHandler = new DeviceSessionHandler();
    String userId = null;
    @OnOpen
        public void open(Session session, EndpointConfig config) {
    		User user = User.getObj();
    		userId =  user.getUser();
    		user.setUser("");
    		System.out.print("onOpen , user : " + userId);
    		
    		try {
				sessionHandler.addSession(userId, session);
			} catch (Exception e) {}
    }

    @OnClose
        public void close(Session session) {
    		System.out.print("close...");
    		sessionHandler.removeSession(userId, session);
//    		sessionHandler.closMessage(session, userId);
    }

    @OnError
        public void onError(Throwable error) {
    		Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) {

    	try{
    		JSONParser jsonParser = new JSONParser();
            JSONObject jsonMessage = (JSONObject)jsonParser.parse(message);
            
            if("newMessage".equals((String)jsonMessage.get("action"))){
            	System.out.println("\njson :  " + jsonMessage.get("chat"));
            	sessionHandler.storeMessage((JSONObject) jsonMessage.get("chat"));
            	if(sessionHandler.sessions.containsKey(((JSONObject)(jsonMessage.get("chat"))).get("receiver")))
            		sessionHandler.sendToSession(sessionHandler.sessions.get(((JSONObject)jsonMessage.get("chat")).get("receiver")), jsonMessage);
            	
            	
            }
            
            if("status".equals((String)jsonMessage.get("action"))){
            	if(sessionHandler.sessions.containsKey(jsonMessage.get("friend")))
            		sessionHandler.sendToSession(sessionHandler.sessions.get(jsonMessage.get("friend")), jsonMessage);            
            }
            
            if("getStatus".equals((String)jsonMessage.get("action"))) {
            	JSONObject jStatus = new JSONObject();
            	jStatus.put("action", "status");
            	jStatus.put("user", jsonMessage.get("friend"));
            	jStatus.put("friend", jsonMessage.get("user"));
            	if(sessionHandler.sessions.containsKey(((JSONObject)jsonMessage).get("friend")))
            		jStatus.put("status", "online");
            	else
            		jStatus.put("status", "offline");
            	
            	sessionHandler.sendToSession(sessionHandler.sessions.get(((JSONObject)jsonMessage).get("user")), jStatus);
            }
            
            if("getNewChat".equals((String)jsonMessage.get("action"))) {
            	sessionHandler.createNewChat((String)jsonMessage.get("user"), (String)jsonMessage.get("friend"));
            }
                        
        }
    	catch(Exception e) {
    		System.out.print(e);
    	}
    }
}    