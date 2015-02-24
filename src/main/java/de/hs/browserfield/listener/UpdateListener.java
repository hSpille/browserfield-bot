package de.hs.browserfield.listener;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.emitter.Emitter;

import de.hs.browserfield.BrowserFieldEventListener;
import de.hs.browserfield.model.Player;

public class UpdateListener implements Emitter.Listener {

	private JSONObject update;
	private ArrayList<Player> players;
	private Player myPlayer;
	private String iAm;
	private BrowserFieldEventListener myListener;
	private long lastTickTime;
	

	public void call(Object... args) {
		
		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("TickTime: " + (currentTimeMillis - lastTickTime));
		lastTickTime = currentTimeMillis;
		if(args.length > 0){
			update = (JSONObject) args[0];
			players = new ArrayList<Player>();
			parsePlayer(update);
			this.myListener.worldUpdate(this);
		}
	}

	private void parsePlayer(JSONObject players) {
		try {
			JSONArray allPlayers = (JSONArray) players.get("players");
			for (int i = 0; i <= allPlayers.length() - 1; i++) {
				Player p = new Player((JSONObject) allPlayers.get(i));
				if (p.id.equalsIgnoreCase(iAm)) {
					myPlayer = p;
				}
				this.players.add(p);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Player getMyPlayer() {
		return myPlayer;
	}

	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}

	public String getiAm() {
		return iAm;
	}

	public void setiAm(String iAm) {
		this.iAm = iAm;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public JSONObject getUpdate() {
		return update;
	}

	public void setUpdate(JSONObject update) {
		this.update = update;
	}

	public void setMyListener(BrowserFieldEventListener myListener) {
		this.myListener = myListener;
	}

}
