package me.eglp.amongus4graphite.game;

import java.util.HashMap;
import java.util.Map;

import com.corundumstudio.socketio.SocketIOClient;

public class AmongUsCaptureUser {
	
	private String code;
	private AmongUsRoom room;
	private Map<String, Object> data;
	
	public AmongUsCaptureUser(SocketIOClient socketClient) {
		this.room = new AmongUsRoom();
		this.data = new HashMap<>();
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
	
	public AmongUsRoom getRoom() {
		return room;
	}
	
	public void setData(String key, Object value) {
		data.put(key, value);
	}
	
	public Object getData(String key) {
		return data.get(key);
	}

}
