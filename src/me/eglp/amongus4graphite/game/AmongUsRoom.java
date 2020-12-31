package me.eglp.amongus4graphite.game;

import java.util.ArrayList;
import java.util.List;

import me.eglp.amongus4graphite.auc.PlayMap;
import me.eglp.amongus4graphite.auc.PlayRegion;
import me.eglp.amongus4graphite.auc.PlayerColor;

public class AmongUsRoom {
	
	private List<AmongUsPlayer> players;
	private String lobbyCode;
	private PlayRegion region;
	private PlayMap map;
	
	public AmongUsRoom() {
		this.players = new ArrayList<>();
	}
	
	public List<AmongUsPlayer> getPlayers() {
		return players;
	}
	
	public AmongUsPlayer getPlayer(String name) {
		return players.stream()
				.filter(p -> p.getAmongUsName().equals(name))
				.findFirst().orElse(null);
	}
	
	public AmongUsPlayer getPlayer(PlayerColor color) {
		return players.stream()
				.filter(p -> p.getAmongUsColor() == color)
				.findFirst().orElse(null);
	}
	
	public void setLobbyCode(String code) {
		this.lobbyCode = code;
	}
	
	public String getLobbyCode() {
		return lobbyCode;
	}
	
	public void setRegion(PlayRegion region) {
		this.region = region;
	}

	public PlayRegion getRegion() {
		return region;
	}
	
	public void setMap(PlayMap map) {
		this.map = map;
	}

	public PlayMap getMap() {
		return map;
	}
	
}
