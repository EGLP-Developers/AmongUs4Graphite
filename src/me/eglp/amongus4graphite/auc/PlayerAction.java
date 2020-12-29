package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum PlayerAction implements JSONPrimitiveConvertible {

	JOINED,
	LEFT,
	DIED,
	CHANGED_COLOR,
	FORCE_UPDATED,
	DISCONNECTED,
	EXILED;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static PlayerAction decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}
	
}
