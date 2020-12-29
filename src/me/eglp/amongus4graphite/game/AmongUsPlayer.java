package me.eglp.amongus4graphite.game;

import me.eglp.amongus4graphite.auc.PlayerColor;

public class AmongUsPlayer {
	
	private String amongUsName;
	private PlayerColor amongUsColor;
	private boolean isDead;
	
	public AmongUsPlayer(String amongUsName, PlayerColor amongUsColor) {
		this.amongUsName = amongUsName;
		this.amongUsColor = amongUsColor;
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

}
