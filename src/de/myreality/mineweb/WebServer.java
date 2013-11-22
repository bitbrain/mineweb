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

/**
 * Single server which can be started and closed
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public interface WebServer {

	// ===========================================================
	// Methods
	// ===========================================================
	
	/**
	 * Set a new port to the server. Can only be used when the server is stopped
	 * 
	 * @param port new port number
	 */
	void setPort(int port);
	
	/**
	 * Starts the server
	 */
	void start();
	
	/**
	 * Stops the server
	 */
	void stop();
	
	/**
	 * Indicates if server is currently running
	 * 
	 * @return True when running
	 */
	boolean isRunning();
}
