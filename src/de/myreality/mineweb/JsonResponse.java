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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.OfflinePlayer;
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
			+ "Content-Type: application/json; charset:utf-8\r\n" + "Content-Length: ";

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
		
		try {
			// Send the JSON response.
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			String json = createJson(plugin);
			String bytes = OUTPUT_HEADERS + json.length() + OUTPUT_END_OF_HEADERS
					+ json;
			out.writeBytes(bytes);
			out.flush();
			out.close();
			
			plugin.getLogger().log(Level.INFO, "Wrote: " + bytes);
		} catch (Exception e) {
			plugin.getLogger().log(Level.INFO, e.getMessage());
		}
	}

	private String jsonfy(String key, String value) {
		return jsonfy(key, value, true);
	}
	
	private String jsonfy(String key, String value, boolean stringed) {
		if (stringed) {
			return "\"" + key + "\":\"" + value + "\"";
		} else {
			return "\"" + key + "\":" + value;
		}
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
		json.append(jsonfy(PLAYERS, createPlayersJson(getPlayers(server)), false));
		json.append("}");
		
		return json.toString();			
	}
	
	private String createPlayersJson(List<OfflinePlayer> players) {
		
		StringBuilder json = new StringBuilder();
		
		json.append("[");
		
		for (OfflinePlayer player : players) {
			if (player instanceof Player) {
				json.append(createPlayerJson((Player)player)).append(",");
			} else if (!player.isOnline()) {
				json.append(createPlayerJson(player)).append(",");
			}
		}
		
		// Remove the last comma
		int index = json.lastIndexOf(",");
		if (index > -1) {
			json.deleteCharAt(index);
		}
		
		json.append("]");
		
		return json.toString();
	}
	
	private String createPlayerJson(OfflinePlayer player) {
		StringBuilder json = new StringBuilder();
		
		json.append("{");
		json.append(jsonfy("name", player.getName())).append(",");
		json.append(jsonfy("dead", "")).append(",");
		json.append(jsonfy("xp", "")).append(",");
		json.append(jsonfy("xpMax", "")).append(",");
		json.append(jsonfy("health", "")).append(",");
		json.append(jsonfy("healthMax", "")).append(",");
		json.append(jsonfy("level", "")).append(",");
		json.append(jsonfy("online", player.isOnline() + "")).append(",");
		json.append(jsonfy("op", player.isOp() + "")).append(",");
		json.append(jsonfy("sleeping", ""));
		json.append("}");
		
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
		json.append(jsonfy("sleeping", player.isSleeping() + ""));
		json.append("}");
		
		return json.toString();
	}
	
	private List<OfflinePlayer> getPlayers(Server server) {
		Player[] online = server.getOnlinePlayers();
		OfflinePlayer[] offline = server.getOfflinePlayers();
		
		List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();
		
		for (Player p : online) {
			players.add(p);
		}
		
		for (OfflinePlayer p : offline) {
			if (!players.contains(p) && !p.isOnline()) {
				players.add(p);
			}
		}
		
		return players;
	}
	
	
	// ===========================================================
	// Inner classes
	// ===========================================================

}
