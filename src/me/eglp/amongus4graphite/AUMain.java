package me.eglp.amongus4graphite;

import java.util.Scanner;

public class AUMain {
	
	public static void main(String[] args) {
		try {
			AmongUsWebSocketServer se = new AmongUsWebSocketServer();
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
