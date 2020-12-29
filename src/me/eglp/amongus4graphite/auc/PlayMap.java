package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum PlayMap implements JSONPrimitiveConvertible {

	SKELD,
	MIRA,
	POLUS;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static PlayMap decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}
	
}
