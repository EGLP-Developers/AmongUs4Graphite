package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class ImmutablePlayer implements JSONConvertible {
	
	@JSONValue("Name")
	private String name;
	
	@JSONValue("IsImpostor")
	private boolean isImpostor;
	
	@JSONConstructor
	private ImmutablePlayer() {}

	public String getName() {
		return name;
	}

	public boolean isImpostor() {
		return isImpostor;
	}
	
}
