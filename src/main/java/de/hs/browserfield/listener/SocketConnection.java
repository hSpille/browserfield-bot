package de.hs.browserfield.listener;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class SocketConnection {

	public static final String JOIN = "join";
	
	private Socket socket;
	
	private String lastMessageEvent = null;
	private Object lastMessageContent = null;
	

	public SocketConnection(Socket socket) {
		this.socket = socket;
	}

	public String getId() {
		return socket.id();
	}

	public void doEmit(String messageType, JSONObject content) {
		if(!this.lastMessageIsEqualToThis(messageType,content)){
			socket.emit(messageType, content);
		}
	}

	private boolean lastMessageIsEqualToThis(String messageType,
			Object content) {
		
		if(this.lastMessageEvent == null){
			storeMessage(messageType,content);
			return false;
		}
		if(!this.lastMessageEvent.equalsIgnoreCase(messageType)){
			storeMessage(messageType,content);
			return false;
		}
		if(this.lastMessageContent == null){
			storeMessage(messageType,content);
			return false;
		}
		if(lastMessageContent.getClass().equals(JSONArray.class) && content.getClass().equals(JSONArray.class)){
			JSONArray old = (JSONArray) lastMessageContent;
			JSONArray neC = (JSONArray) content;
			if(!old.toString().equalsIgnoreCase(neC.toString())){
				storeMessage(messageType,content);
				return false;
			}
		}
		storeMessage(messageType,content);
		return true;
	}

	private void storeMessage(String messageType, Object content) {
		this.lastMessageContent = content;
		this.lastMessageEvent = messageType;
	}

	public void on(String event, Emitter.Listener listener) {
		socket.on(event, listener);
	}

	public void connect() {
		socket.connect();
	}

	public void doEmit(String messageType, JSONArray content) {
		if(!this.lastMessageIsEqualToThis(messageType,content)){
			socket.emit(messageType, content);
		}
	}	

}
