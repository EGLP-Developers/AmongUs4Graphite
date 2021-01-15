package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum PlayRegion implements JSONPrimitiveConvertible {

	NORTH_AMERICA("North America"),
	ASIA("Asia"),
	EUROPE("Europe");
	
	private String friendlyName;

	private PlayRegion(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getFriendlyName() {
		return friendlyName;
	}

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static PlayRegion decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}
	
}
