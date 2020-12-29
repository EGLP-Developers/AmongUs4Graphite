package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum GameState implements JSONPrimitiveConvertible {
	
	LOBBY,
	TASKS,
	DISCUSSION,
	MENU,
	UNKNOWN;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static GameState decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}

}
