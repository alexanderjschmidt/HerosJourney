package fe.game.tilemap;

import org.json.JSONException;
import org.json.JSONObject;

public class MapData {

	private int seed, mapSize, armySize, teamCount;

	private boolean fogOfWar;

	public MapData(int seed, int mapSize, int armySize, int teamCount, boolean fogOfWar) {
		this.seed = seed;
		this.mapSize = mapSize;
		this.armySize = armySize;
		this.teamCount = teamCount;
		this.fogOfWar = fogOfWar;
	}

	public void load(JSONObject gameInfo) {
		try {
			seed = gameInfo.getInt("seed");
			mapSize = gameInfo.getInt("mapSize");
			armySize = gameInfo.getInt("armySize");
			teamCount = gameInfo.getInt("teamCount");
			fogOfWar = gameInfo.getBoolean("fogOfWar");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getSeed() {
		return seed;
	}

	public int getMapSize() {
		return mapSize;
	}

	public int getArmySize() {
		return armySize;
	}

	public int getTeamCount() {
		return teamCount;
	}

	public boolean isFogOfWar() {
		return fogOfWar;
	}
}
