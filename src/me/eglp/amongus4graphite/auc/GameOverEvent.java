package me.eglp.amongus4graphite.auc;

import me.mrletsplay.mrcore.json.converter.JSONConstructor;
import me.mrletsplay.mrcore.json.converter.JSONConvertible;
import me.mrletsplay.mrcore.json.converter.JSONValue;

public class GameOverEvent implements JSONConvertible {
	
	@JSONValue("GameOverReason")
	private GameOverReason gameOverReason;
	
	@JSONValue("PlayerInfos")
	private ImmutablePlayer[] playerInfos;
	
	@JSONConstructor
	private GameOverEvent() {}

	public GameOverReason getGameOverReason() {
		return gameOverReason;
	}

	public ImmutablePlayer[] getPlayerInfos() {
		return playerInfos;
	}
	
}
