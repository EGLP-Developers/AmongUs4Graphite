package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum GameOverReason implements JSONPrimitiveConvertible {

	HUMANS_BY_VOTE,
	HUMANS_BY_TASK,
	IMPOSTOR_BY_VOTE,
	IMPOSTOR_BY_KILL,
	IMPOSTOR_BY_SABOTAGE,
	IMPOSTOR_DISCONNECT,
	HUMANS_DISCONNECT,
	UNKNOWN;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static GameOverReason decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}
	
}
