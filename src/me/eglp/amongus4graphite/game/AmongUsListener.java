package me.eglp.amongus4graphite.game;

public interface AmongUsListener {
	
	public void playerJoined(AmongUsPlayer player);
	
	public void playerLeft(AmongUsPlayer player);
	
	public void playerUpdated(AmongUsPlayer player);
	
	public void mutePlayer(AmongUsPlayer player);
	
	public void unmutePlayer(AmongUsPlayer player);

}
