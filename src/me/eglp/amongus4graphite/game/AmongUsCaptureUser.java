package me.eglp.amongus4graphite.game;

import com.corundumstudio.socketio.SocketIOClient;

public class AmongUsCaptureUser extends AmongUsPlayer {
	
	private SocketIOClient socketClient;
	private String code;
	private AmongUsRoom room;
	
	public AmongUsCaptureUser(SocketIOClient socketClient) {
		super(null, null);
		this.socketClient = socketClient;
		this.room = new AmongUsRoom();
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public SocketIOClient getSocketClient() {
		return socketClient;
	}
	
	public String getCode() {
		return code;
	}
	
	public AmongUsRoom getRoom() {
		return room;
	}

}
