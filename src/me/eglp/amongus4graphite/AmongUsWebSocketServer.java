package me.eglp.amongus4graphite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import io.netty.util.concurrent.Future;
import me.eglp.amongus4graphite.auc.GameState;
import me.eglp.amongus4graphite.auc.LobbyEvent;
import me.eglp.amongus4graphite.game.AmongUsCaptureUser;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class AmongUsWebSocketServer {
	
	private SocketIOServer socketServer;
	private List<AmongUsCaptureUser> captureUsers;
	
	public AmongUsWebSocketServer() {
		Configuration c = new Configuration();
		c.setPort(6585);
//		c.setHostname("127.0.0.1");
		c.setHostname("0.0.0.0");
		socketServer = new SocketIOServer(c);
		captureUsers = new ArrayList<>();
		
		socketServer.addDisconnectListener(client -> {
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u != null) {
				// TODO: do stuff
			}
		});
		
		socketServer.addEventListener("botID", String.class, (client, data, ackSender) -> System.out.println(data)); // TODO: implement?
		
		socketServer.addEventListener("connectCode", String.class, (client, data, ackSender) -> {
			System.out.println("CON: " + data);
			AmongUsCaptureUser u = getCaptureUser(data);
			if(u != null) {
//				u.getSocketClient().disconnect(); TODO: why does this cause the client to loop reconnects?
				captureUsers.remove(u);
			}
			
			client.set("code", data);
			captureUsers.add(new AmongUsCaptureUser(client));
		});
		
		socketServer.addEventListener("lobby", String.class, (client, data, ackSender) -> {
			System.out.println("YEET");
			AmongUsCaptureUser u = getCaptureUser(client);
			System.out.println("D: " + u);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			System.out.println((String) client.get("code"));
			System.out.println("LOBBY: " + data);
			try {
				LobbyEvent e = JSONConverter.decodeObject(new JSONObject(data), LobbyEvent.class);
				System.out.println(e.getLobbyCode());
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		socketServer.addEventListener("taskFailed", String.class, (client, data, ackSender) -> {
			System.out.println("Task failed: " + data);
		});
		
		socketServer.addEventListener("taskComplete", String.class, (client, data, ackSender) -> {
			System.out.println("Task complete: " + data);
		});
		
		socketServer.addEventListener("state", Integer.class, (client, data, ackSender) -> {
			GameState newState = GameState.decodePrimitive(data);
			System.out.println("New State: " + newState);
		});
		
		socketServer.addEventListener("player", String.class, (client, data, ackSender) -> {
			System.out.println("Player: " + data);
		});
		
		socketServer.addEventListener("gameover", String.class, (client, data, ackSender) -> {
			System.out.println("Game over: " + data);
		});
	}
	
	public void start() {
		try {
			System.out.println("Waiting");
			Future<Void> f = socketServer.startAsync();
			f.await(5, TimeUnit.SECONDS);
			if(!f.isSuccess()) throw new FriendlyException("Failed to start Socket.io server", f.cause());
			System.out.println("We're up and running");
		}catch(InterruptedException e) {
			throw new FriendlyException("Failed to start Socket.io server", e);
		}
	}
	
	public void stop() {
		socketServer.stop();
	}
	
	private AmongUsCaptureUser getCaptureUser(SocketIOClient client) {
		if(!client.has("code")) return null;
		return getCaptureUser((String) client.get("code"));
	}
	
	private AmongUsCaptureUser getCaptureUser(String code) {
		return captureUsers.stream()
				.filter(u -> u.getSocketClient().has("code") && u.getSocketClient().get("code").equals(code))
				.findFirst().orElse(null);
	}
	
	public static String newRandomCode() {
		return Long.toHexString(System.nanoTime() ^ new Random().nextLong()).toUpperCase(); // Amazing randomness, patent pending TODO: improve
	}

}
