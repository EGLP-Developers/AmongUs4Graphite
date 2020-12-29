package me.eglp.amongus4graphite.game;

import com.corundumstudio.socketio.SocketIOClient;

public class AmongUsCaptureUser extends AmongUsPlayer {
	
	private SocketIOClient socketClient;
	private String code;
	
	public AmongUsCaptureUser(SocketIOClient socketClient) {
		super(null, null);
		this.socketClient = socketClient;
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

}
