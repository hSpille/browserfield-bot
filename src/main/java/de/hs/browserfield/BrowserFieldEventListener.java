package de.hs.browserfield;

import de.hs.browserfield.listener.ConnectListener;
import de.hs.browserfield.listener.UpdateListener;

public interface BrowserFieldEventListener {

	
	public void worldUpdate(UpdateListener updateListener);
	public void serverConnect(ConnectListener connectListener);
	
}
