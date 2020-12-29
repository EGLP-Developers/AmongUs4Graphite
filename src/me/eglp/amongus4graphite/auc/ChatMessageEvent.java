package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class ChatMessageEvent implements JSONConvertible {
	
	@JSONValue("Sender")
	private String sender;
	
	@JSONValue("Color")
	private PlayerColor color;
	
	@JSONValue("Message")
	private String message;
	
	@JSONConstructor
	private ChatMessageEvent() {}

	public String getSender() {
		return sender;
	}

	public PlayerColor getColor() {
		return color;
	}

	public String getMessage() {
		return message;
	}
	
}
