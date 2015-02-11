package de.hs.browserfield;

import java.net.URISyntaxException;

import org.json.JSONException;

public class Main {

	public static void main(String[] args) throws URISyntaxException,
			InterruptedException, JSONException {

		Bot b = new Bot();
		b.connect("http://browserfield.anythingsthing.de/");

		while (true) {
			Thread.sleep(10000);
		}

	}

}
