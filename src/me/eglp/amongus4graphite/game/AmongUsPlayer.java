package me.eglp.amongus4graphite.game;

import java.util.HashMap;
import java.util.Map;

import me.eglp.amongus4graphite.auc.PlayerColor;

public class AmongUsPlayer {
	
	private String amongUsName;
	private PlayerColor amongUsColor;
	private boolean isDead;
	private boolean isKnownDead;
	private Map<String, Object> data;
	
	public AmongUsPlayer(String amongUsName, PlayerColor amongUsColor) {
		this.amongUsName = amongUsName;
		this.amongUsColor = amongUsColor;
		this.data = new HashMap<>();
	}
	
	public void setAmongUsName(String amongUsName) {
		this.amongUsName = amongUsName;
	}
	
	public String getAmongUsName() {
		return amongUsName;
	}
	
	public void setAmongUsColor(PlayerColor amongUsColor) {
		this.amongUsColor = amongUsColor;
	}
	
	public PlayerColor getAmongUsColor() {
		return amongUsColor;
	}
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setKnownDead(boolean isKnownDead) {
		this.isKnownDead = isKnownDead;
	}
	
	public boolean isKnownDead() {
		return isKnownDead;
	}
	
	public void setData(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getData(String key) {
		return data.get(key);
	}

}
