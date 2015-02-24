package de.hs.browserfield;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws URISyntaxException,
			InterruptedException, JSONException {
			List<Bot> botList = new ArrayList<Bot>();
		
			Bot b = new Bot();
			// b.connect("http://browserfield.anythingsthing.de");
			b.connect("http://pc4064.vit.de:3000");
			botList.add(b);

		while (true) {
			Thread.sleep(10000);
		}

	}

}
