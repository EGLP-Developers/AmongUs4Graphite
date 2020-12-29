package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class LobbyEvent implements JSONConvertible {
	
	@JSONValue("LobbyCode")
	private String lobbyCode;
	
	@JSONValue("Region")
	private PlayRegion region;
	
	@JSONValue("Map")
	private PlayMap map;
	
	@JSONConstructor
	private LobbyEvent() {}

	public String getLobbyCode() {
		return lobbyCode;
	}

	public PlayRegion getRegion() {
		return region;
	}

	public PlayMap getMap() {
		return map;
	}

}
