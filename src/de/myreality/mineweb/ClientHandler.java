/* mineweb is a Bukkit plugin for Minecraft to provide server information.
 *  Copyright (C) 2013  Miguel Gonzalez
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */
package de.myreality.mineweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Handles a single client request
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0 
 */
public class ClientHandler implements Runnable {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private Socket client;
	
	private MineWeb plugin;
	
	private JsonResponse response;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public ClientHandler(Socket client, MineWeb plugin) {
		this.client = client;
		this.plugin = plugin;
		response = new JsonResponse();
	}

	// ===========================================================
	// Methods from superclass/interface
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(client.getInputStream()));
			response.handle(client, reader.readLine(), plugin);
		} catch (IOException ex) {			
			ex.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ===========================================================
	// Other methods
	// ===========================================================

	// ===========================================================
	// Inner classes
	// ===========================================================

}
