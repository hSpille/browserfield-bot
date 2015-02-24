package de.hs.browserfield.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import de.hs.browserfield.BrowserFieldEventListener;

public class ConnectListener implements Emitter.Listener {

	private String iAm;
	private Socket socket;
	private BrowserFieldEventListener myListener;
	
	public void call(Object... args) {
		System.out.println("Connect!: " + args);
		iAm = socket.id();
		myListener.serverConnect(this);
		JSONObject name = new JSONObject();
		try {
			name.put("name", "c3po");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.emit("join", name);
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		
	}

	public String getiAm() {
		return iAm;
	}

	public void setiAm(String iAm) {
		this.iAm = iAm;
	}


	public void setMyListener(BrowserFieldEventListener myListener) {
		this.myListener = myListener;
	}
	
}
