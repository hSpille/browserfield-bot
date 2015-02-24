package de.hs.browserfield.listener;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;

import de.hs.browserfield.BrowserFieldEventListener;

public class ConnectListener implements Emitter.Listener {

	
	private String iAm;
	private SocketConnection socket; 
	private BrowserFieldEventListener myListener;
	
	public void call(Object... args) {
		System.out.println("Connect!: " + args);
		iAm = socket.getId();
		myListener.serverConnect(this);
		JSONObject name = new JSONObject();
		try {
			name.put("name", "c3po");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		socket.doEmit(SocketConnection.JOIN, name); 
	}

	public void setSocket(SocketConnection socket) {
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
