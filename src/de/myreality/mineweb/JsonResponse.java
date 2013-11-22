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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 * Response to an action with json
 * 
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @since 1.0
 * @version 1.0
 */
public class JsonResponse {

	// ===========================================================
	// Constants
	// ===========================================================

	public static final String SERVER_NAME = "server_name";
	public static final String CURRENT_PLAYER_COUNT = "current_player_count";
	public static final String MAX_PLAYER_COUNT = "max_player_count";
	public static final String BUKKIT_VERSION = "bukkit_version";
	public static final String PLAYERS = "players";

	private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n"
			+ "Content-Type: application/json\r\n" + "Content-Length: ";

	private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Methods from superclass/interface
	// ===========================================================

	// ===========================================================
	// Other methods
	// ===========================================================

	public void handle(Socket client, String request, MineWeb plugin)
			throws IOException {

		// Handle a query request.
		if (request == null) {
			return;
		}

		// Send the JSON response.
		DataOutputStream out = new DataOutputStream(client.getOutputStream());
		String json = createJson(plugin);

		out.writeBytes(OUTPUT_HEADERS + json.length() + OUTPUT_END_OF_HEADERS
				+ json);
		out.flush();
		out.close();
	}

	private String jsonfy(String key, String value) {
		return "\"" + key + "\":\"" + value + "\"";
	}

	private String createJson(MineWeb plugin) {
		
		StringBuilder json = new StringBuilder();
		Server server = plugin.getServer();
		Player[] online = server.getOnlinePlayers();
		int current = online.length;
		int max = server.getMaxPlayers();
		
		json.append("{");
		json.append(jsonfy(SERVER_NAME, server.getName())).append(",");
		json.append(jsonfy(CURRENT_PLAYER_COUNT, current + "")).append(",");
		json.append(jsonfy(MAX_PLAYER_COUNT, max + "")).append(",");
		json.append(jsonfy(BUKKIT_VERSION, server.getBukkitVersion())).append(",");	
		json.append(jsonfy(PLAYERS, createPlayersJson(online)));	
		json.append("}\n");
		
		return json.toString();			
	}
	
	private String createPlayersJson(Player[] players) {
		
		StringBuilder json = new StringBuilder();
		
		json.append("[");
		
		for (Player player : players) {
			json.append(createPlayerJson(player)).append(",");
		}
		
		// Remove the last comma
		json.deleteCharAt(json.lastIndexOf(","));
		
		json.append("]");
		
		return json.toString();
	}
	
	private String createPlayerJson(Player player) {
		
		StringBuilder json = new StringBuilder();
		
		json.append("{");
		json.append(jsonfy("name", player.getDisplayName())).append(",");
		json.append(jsonfy("dead", player.isDead() + "")).append(",");
		json.append(jsonfy("xp", player.getExp() + "")).append(",");
		json.append(jsonfy("xpMax", player.getTotalExperience() + "")).append(",");
		json.append(jsonfy("health", player.getHealth() + "")).append(",");
		json.append(jsonfy("healthMax", player.getMaxHealth() + "")).append(",");
		json.append(jsonfy("level", player.getLevel() + "")).append(",");
		json.append(jsonfy("online", player.isOnline() + "")).append(",");
		json.append(jsonfy("op", player.isOp() + "")).append(",");
		json.append(jsonfy("s;eeping", player.isSleeping() + ""));
		json.append("}");
		
		return json.toString();
	}
	// ===========================================================
	// Inner classes
	// ===========================================================

}
