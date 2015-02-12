package de.hs.browserfield.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Player {

	public Player(JSONObject state) throws JSONException {
		this.id = (String) state.get("id");
		this.x = state.getDouble("x");
		this.y = state.getDouble("y");
		this.orientation = state.getDouble("orientation");
		this.isHit = state.getBoolean("isHit");
		this.message = state.getJSONArray("messages");
	}

	public String id;
	public JSONArray message;
	public double x;
	public double y;
	public double orientation;
	public boolean isHit;

	@Override
	public String toString() {
		return "Player [id=" + id + ", x=" + x + ", y=" + y + "]";
	}

}
