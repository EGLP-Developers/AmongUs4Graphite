package me.eglp.amongus4graphite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import io.netty.util.concurrent.Future;
import me.eglp.amongus4graphite.auc.GameState;
import me.eglp.amongus4graphite.auc.LobbyEvent;
import me.eglp.amongus4graphite.auc.PlayerChangedEvent;
import me.eglp.amongus4graphite.game.AmongUsCaptureUser;
import me.eglp.amongus4graphite.game.AmongUsListener;
import me.eglp.amongus4graphite.game.AmongUsPlayer;
import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.mrcore.json.converter.JSONConverter;
import me.mrletsplay.mrcore.misc.FriendlyException;

public class AmongUsWebSocketServer {
	
	private SocketIOServer socketServer;
	private List<AmongUsCaptureUser> captureUsers;
	private AmongUsListener listener;
	
	public AmongUsWebSocketServer() {
		Configuration c = new Configuration();
		c.setPort(6585);
		c.setHostname("0.0.0.0");
		socketServer = new SocketIOServer(c);
		
//		socketServer = io.scalecube.socketio.SocketIOServer.newInstance(ServerConfiguration.builder()
//				.port(6585)
//				.sslContext((SSLContext) null)
////				.sslContext(SslContext.)
//				.build());
//		
//		socketServer.setListener(new SocketIOAdapter() {
//			
//			@Override
//			public void onConnect(Session session) {
//				System.out.println("YO");
//			}
//			
//			@Override
//			public void onMessage(Session session, ByteBuf message) {
//				System.out.println(message.toString(StandardCharsets.UTF_8));
//			}
//		});
		
		captureUsers = new ArrayList<>();
		
		socketServer.addDisconnectListener(client -> captureUsers.remove(getCaptureUser(client)));
		
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
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			try {
				LobbyEvent e = JSONConverter.decodeObject(new JSONObject(data), LobbyEvent.class);
				
				u.getRoom().setRegion(e.getRegion());
				u.getRoom().setMap(e.getMap());
				
				listener.lobbyChanged(u, e);
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
		
		socketServer.addEventListener("state", Long.class, (client, data, ackSender) -> {
			System.out.println("STATE " + data);
			AmongUsCaptureUser u = getCaptureUser(client);
			System.out.println("USER: " + u);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			try {
				GameState newState = GameState.decodePrimitive(data);
				
				System.out.println(newState);
				
				List<AmongUsPlayer> deadPlayers = u.getRoom().getPlayers().stream()
						.filter(AmongUsPlayer::isDead)
						.collect(Collectors.toList());
				
				List<AmongUsPlayer> alivePlayers = u.getRoom().getPlayers().stream()
						.filter(pl -> !pl.isDead())
						.collect(Collectors.toList());
				
				System.out.println("DEAD: " + deadPlayers);
				System.out.println("ALIVE: " + alivePlayers);
				switch(newState) {
					case DISCUSSION:
					{
						deadPlayers.forEach(pl -> listener.mutePlayer(u, pl));
						alivePlayers.forEach(pl -> listener.unmutePlayer(u, pl));
						break;
					}
					case TASKS:
					{
						alivePlayers.forEach(pl -> listener.mutePlayer(u, pl));
						deadPlayers.forEach(pl -> listener.unmutePlayer(u, pl));
						break;
					}
					default:
						break;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		socketServer.addEventListener("player", String.class, (client, data, ackSender) -> {
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			System.out.println("Player: " + data);
			
			try {
				PlayerChangedEvent e = JSONConverter.decodeObject(new JSONObject(data), PlayerChangedEvent.class);
				switch(e.getAction()) {
					case DIED:
					case EXILED:
					{
						AmongUsPlayer pl = u.getRoom().getPlayer(e.getName());
						if(pl != null) pl.setDead(true);
						
						listener.playerUpdated(u, pl);
						break;
					}
					case DISCONNECTED:
						break;
					case CHANGED_COLOR:
					case FORCE_UPDATED:
					{
						AmongUsPlayer pl = u.getRoom().getPlayer(e.getName());
						if(pl == null) {
							pl = new AmongUsPlayer(e.getName(), e.getColor());
							pl.setDead(e.isDead());
							u.getRoom().getPlayers().add(pl);
						}else {
							pl.setAmongUsName(e.getName());
							pl.setAmongUsColor(e.getColor());
							pl.setDead(e.isDead());
						}
						
						listener.playerUpdated(u, pl);
						break;
					}
					case JOINED:
					{
						AmongUsPlayer newPlayer = new AmongUsPlayer(e.getName(), e.getColor());
						u.getRoom().getPlayers().add(newPlayer);
						listener.playerJoined(u, newPlayer);
						break;
					}
					case LEFT:
					{
						AmongUsPlayer pl = u.getRoom().getPlayer(e.getName());
						
						if(pl == null) return; // Shouldn't happen
						
						listener.playerLeft(u, pl);
						
						u.getRoom().getPlayers().remove(pl);
						
						break;
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		socketServer.addEventListener("gameover", String.class, (client, data, ackSender) -> {
			System.out.println("Game over: " + data);
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			u.getRoom().getPlayers().forEach(pl -> listener.unmutePlayer(u, pl));
		});
	}
	
	public void start() {
		try {
			System.out.println("Waiting");
//			socketServer.start();
			Future<Void> f = socketServer.startAsync();
			f.await(5, TimeUnit.SECONDS);
			if(!f.isSuccess()) throw new FriendlyException("Failed to start Socket.io server", f.cause());
			System.out.println("We're up and running");
		}catch(Exception e) {
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
	
	public void setListener(AmongUsListener listener) {
		this.listener = listener;
	}
	
	public AmongUsListener getListener() {
		return listener;
	}
	
	public static String newRandomCode() {
		return Long.toHexString(System.nanoTime() ^ new Random().nextLong()).toUpperCase(); // Amazing randomness, patent pending TODO: improve
	}

}
