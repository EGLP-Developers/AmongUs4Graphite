package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONPrimitiveConvertible;

public enum PlayerColor implements JSONPrimitiveConvertible {
	
	RED,
	BLUE,
	GREEN,
	PINK,
	ORANGE,
	YELLOW,
	BLACK,
	WHITE,
	PURPLE,
	BROWN,
	CYAN,
	LIME,
	MAROON,
	ROSE,
	BANANA,
	GRAY,
	TAN,
	CORAL;

	@Override
	public Object toJSONPrimitive() {
		return ordinal();
	}
	
	public static PlayerColor decodePrimitive(Object obj) {
		return values()[((Long) obj).intValue()];
	}

}
