package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class PlayerChangedEvent implements JSONConvertible {
	
	@JSONValue("Action")
	private PlayerAction action;
	
	@JSONValue("Name")
	private String name;
	
	@JSONValue("IsDead")
	private boolean isDead;
	
	@JSONValue("Disconnected")
	private boolean disconnected;
	
	@JSONValue("Color")
	private PlayerColor color;
	
	@JSONConstructor
	private PlayerChangedEvent() {}

	public PlayerAction getAction() {
		return action;
	}

	public String getName() {
		return name;
	}

	public boolean isDead() {
		return isDead;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public PlayerColor getColor() {
		return color;
	}
	
}
