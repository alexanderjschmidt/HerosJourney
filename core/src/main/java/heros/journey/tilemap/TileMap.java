package heros.journey.tilemap;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.tilemap.tiles.ActionTile;
import heros.journey.tilemap.tiles.Tile;
import org.json.JSONObject;

public class TileMap extends TileMapArt {

    private JSONObject[][] locationInformations;

	public TileMap(int mapSize) {
		super(mapSize);
        locationInformations = new JSONObject[getWidth()][getHeight()];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                locationInformations[i][j] = new JSONObject();
            }
        }
	}

    // TODO make this a deep copy
    public TileMap clone(GameState clone) {
        TileMap map = (TileMap) super.clone(clone);
        map.locationInformations = locationInformations.clone();
        return map;
    }

    public void setEnvironment(int x, int y, ActionTile tile) {
        super.setEnvironment(x, y, tile);
        locationInformations[x][y] = tile.updateInformation(locationInformations[x][y]);
    }

    public void setTile(int x, int y, Tile tile) {
        super.setTile(x, y, tile);
        locationInformations[x][y] = tile.updateInformation(locationInformations[x][y]);
    }

    public int getTerrainCost(int x, int y, Entity selected) {
        return super.getTerrainCost(x, y, selected) + getInt("TerrainCost", x, y, 0);
    }

    public Integer getInt(String key, int x, int y, Integer defaultValue) {
        JSONObject info = locationInformations[x][y];
        return info != null && info.has(key) ? info.getInt(key) : defaultValue;
    }
}
