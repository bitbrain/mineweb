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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concurrent implementation of {@see WebServer}
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0 
 */
public class ConcurrentWebServer implements WebServer {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	
	private MineWeb plugin;
	
	private int port;
	
	private ServerSocket socket;
	
	private boolean running;
	
	private ExecutorService service;

	// ===========================================================
	// Constructors
	// ===========================================================
	
	public ConcurrentWebServer(MineWeb plugin, int port) {
		this.plugin = plugin;
		this.port = port;
		service = Executors.newFixedThreadPool(5);
	}

	// ===========================================================
	// Methods from superclass/interface
	// ===========================================================

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void start() {
		
		Logger l = plugin.getLogger();
		l.log(Level.INFO, "Starting web service..");
		if (!isRunning()) {
			try {
				
				socket = new ServerSocket(port);	
				running = true;
				
				while (running) {
					try {
						l.log(Level.INFO, "Waiting for connection..");
						Socket client = socket.accept();
						ClientHandler handler = new ClientHandler(client, plugin);
						l.log(Level.INFO, "New client submitted: " + client.getInetAddress());
						service.submit(handler);
					} catch (IOException e) {
						e.printStackTrace();
						l.log(Level.SEVERE, e.getMessage());
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		try {
			running = false;
			service.shutdownNow();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	// ===========================================================
	// Other methods
	// ===========================================================

	// ===========================================================
	// Inner classes
	// ===========================================================

}
