package de.hs.browserfield.listener;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class SocketConnection {

	public static final String JOIN = "join";
	
	private Socket socket;

	public SocketConnection(Socket socket) {
		this.socket = socket;
	}

	public String getId() {
		return socket.id();
	}

	public void doEmit(String messageType, JSONObject content) {
		socket.emit(messageType, content);
		
	}

	public void on(String event, Emitter.Listener listener) {
		socket.on(event, listener);
	}

	public void connect() {
		socket.connect();
	}

	public void doEmit(String messageType, JSONArray content) {
		socket.emit(messageType, content);
	}	

}
