package me.eglp.amongus4graphite.game;

import java.util.ArrayList;
import java.util.List;

import me.eglp.amongus4graphite.auc.PlayMap;
import me.eglp.amongus4graphite.auc.PlayRegion;

public class AmongUsRoom {
	
	private List<AmongUsPlayer> players;
	private PlayRegion region;
	private PlayMap map;
	
	public AmongUsRoom(PlayRegion region, PlayMap map) {
		this.players = new ArrayList<>();
		this.region = region;
		this.map = map;
	}
	
	public List<AmongUsPlayer> getPlayers() {
		return players;
	}

	public PlayRegion getRegion() {
		return region;
	}

	public PlayMap getMap() {
		return map;
	}
	
}
