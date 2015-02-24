package de.hs.browserfield;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import de.hs.browserfield.listener.ConnectListener;
import de.hs.browserfield.listener.SocketConnection;
import de.hs.browserfield.listener.UpdateListener;
import de.hs.browserfield.model.Player;

public class Bot implements BrowserFieldEventListener {

	private static final int BE_RUDE_PERCENTAGE = 995;
	private static final int ACCURACY = 5;
	private static final int WAITFORNEXTSHOTCYCLES = 5;
	private SocketConnection socket;
	private String iAm;
	private Player myPlayer;
	private UpdateListener updateListener;
	int shootCount = 0;
	boolean didFlameThisHit = false;
	private List<String> flames = new ArrayList<String>();

	public void connect(String URL) throws InterruptedException,
			URISyntaxException, JSONException {
		IO.Options options = new IO.Options();
		socket = new SocketConnection(IO.socket(URL, options));
		ConnectListener connectListener = new ConnectListener();
		connectListener.setSocket(socket);
		connectListener.setMyListener(this);
		updateListener = new UpdateListener();
		updateListener.setMyListener(this);
		flames.addAll(Arrays.asList("i am bored", "Fool!", "Bitch please", " Have seen better players", "Your mom is better", "Noob!", "pew pew", "o7", "haha looser...", "omg FAIL"));
		socket.on(Socket.EVENT_CONNECT, connectListener);
		socket.on("update", updateListener);
		socket.connect();
	}

	public void doMove() throws InterruptedException, JSONException {

		if (updateListener.getUpdate() == null) {
			return;
		}

		Player opId = getClosestOp();

		JSONArray jsonWalk = new JSONArray();
		if (opId != null) {
			chaseAndKillOponent(opId, jsonWalk);
		} else {
			jsonWalk.put("W");
			int randumNum = generateRandomForDescisionWithBounds(0, 10);
			if (randumNum == 9) {
				jsonWalk.put("A");
			}
			if (randumNum == 1) {
				jsonWalk.put("D");
			}
		}
		JSONObject chatMessage = beRude();

		if (chatMessage.length() != 0) {
			socket.doEmit("chat-message", chatMessage);
		}
		learnFlames();
		socket.doEmit("pressedKeys", jsonWalk);
	}

	private void learnFlames() throws JSONException {
		for (Player p : updateListener.getPlayers()) {
			if (p.id == myPlayer.id) {
				continue;
			}
			JSONArray ms = p.message;
			if (ms.length() != 0) {
				for (int i = 0; i < ms.length(); i++) {
					JSONObject jsonOb = ms.getJSONObject(i);
					String message = jsonOb.getString("text");
					if (!flames.contains(message)) {
						flames.add(message);
					}
				}
			}
		}
	}

	private int generateRandomForDescisionWithBounds(int min, int max) {
		Random r = new Random();
		int randumNum = r.nextInt((max - min) + 1) + min;
		return randumNum;
	}

	private JSONObject beRude() throws JSONException {
		JSONObject toReturn = new JSONObject();
		if (myPlayer.isHit) {
			if (!this.didFlameThisHit) {
				this.didFlameThisHit = true;
				toReturn.put("text", "Cheater!");
				return toReturn;
			}
		} else {
			this.didFlameThisHit = false;
		}
		int randomVar = this.generateRandomForDescisionWithBounds(0, 1000);
		if (randomVar > BE_RUDE_PERCENTAGE) {
			toReturn.put("text", getRandomFlame());
		}
		return toReturn;
	}

	private String getRandomFlame() {
		int flameIndex = this.generateRandomForDescisionWithBounds(0,
				flames.size());
		return flames.get(flameIndex);
	}

	private void chaseAndKillOponent(Player opId, JSONArray jsonWalk)
			throws JSONException {
		double closestOpDirection = getPlayerPlayerDirection(opId);
		System.out.println("Op Winkel:" + closestOpDirection
				+ " My Orientation:" + myPlayer.orientation);
		if (closestOpDirection < myPlayer.orientation + ACCURACY
				&& closestOpDirection > myPlayer.orientation - ACCURACY) {
			if (shootCount == 0) {
				shootCount = 1;
				jsonWalk.put("X");
				jsonWalk.put("W");
			} else {
				if(shootCount >= WAITFORNEXTSHOTCYCLES){
					shootCount = 0;
				}else{
					shootCount+=1;
					jsonWalk.put("W");
				}
			}
		} else {
			if (closestOpDirection < myPlayer.orientation - ACCURACY) {
				// chatMessage.put("text", "Going A");
				jsonWalk.put("A");
				jsonWalk.put("W");
			}
			if (closestOpDirection > myPlayer.orientation + ACCURACY) {
				// chatMessage.put("text", "Going D");
				jsonWalk.put("D");
				jsonWalk.put("W");
			}
		}
	}

	private double getPlayerPlayerDirection(Player op) {
		double atan2 = Math.atan2((op.y - myPlayer.y), (op.x - myPlayer.x));
		double degrees = Math.toDegrees(atan2);
		return degrees;

	}

	private Player getClosestOp() {
		Player closestOp = null;
		Double distClosest = Double.MAX_VALUE;
		for (Player player : updateListener.getPlayers()) {
			if (player.id.equalsIgnoreCase(iAm)) {
				continue;
			}
			if (player.isHit == true) {
				continue;
			}
			Double xAbs = myPlayer.x - player.x;
			Double yAbs = myPlayer.y - player.y;
			xAbs = Math.pow(xAbs, 2);
			yAbs = Math.pow(yAbs, 2);
			Double sum = xAbs + yAbs;
			double distance = Math.sqrt(sum);
			if (distance < distClosest) {
				distClosest = distance;
				closestOp = player;
			}
		}
		return closestOp;
	}

	public void worldUpdate(UpdateListener listener) {
		try {
			this.myPlayer = listener.getMyPlayer();

			doMove();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void serverConnect(ConnectListener listener) {
		this.iAm = listener.getiAm();
		updateListener.setiAm(iAm);
	}

}
