package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum PlayRegion implements JSONPrimitiveConvertible {

	NORTH_AMERICA,
	ASIA,
	EUROPE;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static PlayRegion decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}
	
}
