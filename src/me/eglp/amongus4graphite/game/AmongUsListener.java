package me.eglp.amongus4graphite.game;

import me.eglp.amongus4graphite.auc.LobbyEvent;

public interface AmongUsListener {
	
	public void playerJoined(AmongUsCaptureUser captureUser, AmongUsPlayer player);
	
	public void playerLeft(AmongUsCaptureUser captureUser, AmongUsPlayer player);
	
	public void playerUpdated(AmongUsCaptureUser captureUser, AmongUsPlayer player);
	
	public void mutePlayer(AmongUsCaptureUser captureUser, AmongUsPlayer player);
	
	public void unmutePlayer(AmongUsCaptureUser captureUser, AmongUsPlayer player);
	
	public void lobbyChanged(AmongUsCaptureUser captureUser, LobbyEvent event);
	
	public void connectCode(AmongUsCaptureUser captureUser, String code);
	
}
