package de.hs.browserfield;

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import de.hs.browserfield.listener.ConnectListener;
import de.hs.browserfield.listener.UpdateListener;
import de.hs.browserfield.model.Player;

public class Bot implements BrowserFieldEventListener {

	private static final int ACCURACY = 5;
	private Socket socket;
	private String iAm;
	private Player myPlayer;
	private UpdateListener updateListener;
	boolean didShootLastTime = false;

	public void connect(String URL) throws InterruptedException,
			URISyntaxException, JSONException {
		IO.Options options = new IO.Options();
		socket = IO.socket(URL, options);
		ConnectListener connectListener = new ConnectListener();
		connectListener.setSocket(socket);
		connectListener.setMyListener(this);
		updateListener = new UpdateListener();
		updateListener.setMyListener(this);
		socket.on(Socket.EVENT_CONNECT, connectListener);
		socket.on("update", updateListener);
		socket.connect();
	}

	public void doMove() throws InterruptedException, JSONException {

		if (updateListener.getUpdate() == null) {
			return;
		}

		Player opId = getClosestOp();
		JSONObject chatMessage = new JSONObject();
		JSONArray jsonWalk = new JSONArray();
		if (opId != null) {
			double closestOpDirection = getPlayerPlayerDirection(opId);
			System.out.println("Op Winkel:" + closestOpDirection + " My Orientation:" + myPlayer.orientation);
			if (closestOpDirection < myPlayer.orientation + ACCURACY && closestOpDirection > myPlayer.orientation - ACCURACY) {
				chatMessage.put("text", "Eat this!");
				socket.emit("chat-message", chatMessage);
				if (didShootLastTime) {
					didShootLastTime = false;
					jsonWalk.put("");
				} else {
					didShootLastTime = true;
					jsonWalk.put("X");
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

		} else {
			chatMessage.put("text", "I am bored...");
		}
		//
		socket.emit("pressedKeys", jsonWalk);
		System.out.println("Sending keys: " + jsonWalk);

		// jsonWalk.put("W");
		// jsonWalk.put("D");
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
			System.out.println("World update!" + System.currentTimeMillis()
					/ 1000);
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
