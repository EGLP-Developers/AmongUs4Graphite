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
	
	public AmongUsWebSocketServer(int port) {
		Configuration c = new Configuration();
		c.setPort(port);
		c.setHostname("0.0.0.0");
		socketServer = new SocketIOServer(c);
		
		captureUsers = new ArrayList<>();
		
		socketServer.addDisconnectListener(client -> captureUsers.remove(getCaptureUser(client)));
		
		socketServer.addEventListener("botID", String.class, (client, data, ackSender) -> System.out.println(data)); // TODO: implement?
		
		socketServer.addEventListener("connectCode", String.class, (client, data, ackSender) -> {
			AmongUsCaptureUser u = getCaptureUser(data);
			if(u == null) return;
			
			client.set("code", data);
			
			listener.connectCode(u, data);
			
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
				
				u.getRoom().setLobbyCode(e.getLobbyCode());
				u.getRoom().setRegion(e.getRegion());
				u.getRoom().setMap(e.getMap());
				
				listener.lobbyChanged(u, e);
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
		
		socketServer.addEventListener("state", Long.class, (client, data, ackSender) -> {
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			try {
				GameState newState = GameState.decodePrimitive(data);
				
				List<AmongUsPlayer> deadPlayers = u.getRoom().getPlayers().stream()
						.filter(AmongUsPlayer::isDead)
						.collect(Collectors.toList());
				
				List<AmongUsPlayer> alivePlayers = u.getRoom().getPlayers().stream()
						.filter(pl -> !pl.isDead())
						.collect(Collectors.toList());
				
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
			
			try {
				PlayerChangedEvent e = JSONConverter.decodeObject(new JSONObject(data), PlayerChangedEvent.class);
				switch(e.getAction()) {
					case DIED:
					case EXILED:
					case DISCONNECTED:
					{
						AmongUsPlayer pl = u.getRoom().getPlayer(e.getName());
						if(pl != null) pl.setDead(true);

						listener.playersUpdated(u);
						break;
					}
					case CHANGED_COLOR:
					case FORCE_UPDATED:
					{
						AmongUsPlayer pl = u.getRoom().getPlayer(e.getName());
						if(e.isDisconnected()) {
							if(pl != null) listener.playerLeft(u, pl);
						}else {
							if(pl == null) {
								pl = new AmongUsPlayer(e.getName(), e.getColor());
								
								pl.setDead(e.isDead());
								u.getRoom().getPlayers().add(pl);
							}else {
								pl.setAmongUsName(e.getName());
								pl.setAmongUsColor(e.getColor());
								pl.setDead(e.isDead());
							}
						}

						listener.playersUpdated(u);
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
			AmongUsCaptureUser u = getCaptureUser(client);
			if(u == null) {
				client.disconnect();
				return;
			}
			
			u.getRoom().getPlayers().forEach(pl -> {
				pl.setDead(false);
				listener.unmutePlayer(u, pl);
			});
			
			listener.playersUpdated(u);
		});
	}
	
	public void start() {
		try {
			System.out.println("Waiting");
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
				.filter(u -> u.getCode().equals(code))
				.findFirst().orElse(null);
	}
	
	public void setListener(AmongUsListener listener) {
		this.listener = listener;
	}
	
	public AmongUsListener getListener() {
		return listener;
	}
	
	public AmongUsCaptureUser createCaptureUser() {
		AmongUsCaptureUser c = new AmongUsCaptureUser(null);
		c.setCode(newRandomCode());
		captureUsers.add(c);
		return c;
	}
	
	public List<AmongUsCaptureUser> getCaptureUsers() {
		return captureUsers;
	}
	
	public static String newRandomCode() {
		return Long.toHexString(System.nanoTime() ^ new Random().nextLong()).toUpperCase(); // Amazing randomness, patent pending TODO: improve
	}
	
}
