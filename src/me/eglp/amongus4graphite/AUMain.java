package me.eglp.amongus4graphite;

import java.util.Scanner;

import me.eglp.amongus4graphite.auc.LobbyEvent;
import me.eglp.amongus4graphite.game.AmongUsCaptureUser;
import me.eglp.amongus4graphite.game.AmongUsListener;
import me.eglp.amongus4graphite.game.AmongUsPlayer;

public class AUMain {
	
	public static void main(String[] args) {
		try {
			AmongUsWebSocketServer se = new AmongUsWebSocketServer(6585);
			
			se.setListener(new AmongUsListener() {
				
				@Override
				public void unmutePlayer(AmongUsCaptureUser captureUser, AmongUsPlayer player) {
					System.out.println(">> UNMUTE " + player.getAmongUsName() + " (" + player.getAmongUsColor() + ")");
				}
				
				@Override
				public void playersUpdated(AmongUsCaptureUser captureUser) {
					System.out.println(">> PLAYERS UPDATED");
				}
				
				@Override
				public void playerLeft(AmongUsCaptureUser captureUser, AmongUsPlayer player) {
					System.out.println(">> PLAYER LEFT: " + player.getAmongUsName() + " (" + player.getAmongUsColor() + ")");
				}
				
				@Override
				public void playerJoined(AmongUsCaptureUser captureUser, AmongUsPlayer player) {
					System.out.println(">> PLAYER JOINED: " + player.getAmongUsName() + " (" + player.getAmongUsColor() + ")");
				}
				
				@Override
				public void mutePlayer(AmongUsCaptureUser captureUser, AmongUsPlayer player) {
					System.out.println(">> MUTE " + player.getAmongUsName() + " (" + player.getAmongUsColor() + ")");
				}
				
				@Override
				public void lobbyChanged(AmongUsCaptureUser captureUser, LobbyEvent event) {
					System.out.println(">> LOBBY IS NOW: " + event.getLobbyCode() + " in " + event.getRegion() + " on " + event.getMap());
				}
				
				@Override
				public void connectCode(AmongUsCaptureUser captureUser, String code) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			se.start();
			
			Scanner s = new Scanner(System.in);
			while(true) {
				String st = s.nextLine();
				System.out.println("> " + st);
				if(st.equals("stop")) {
					s.close();
					se.stop();
					return;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
