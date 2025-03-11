package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import heros.journey.tilemap.TileManager;
import heros.journey.tilemap.TileMapArt;
import org.json.JSONObject;

public abstract class Tile {

	private String name;
	private int terrainCost;
    private boolean wipeInformation;

    public Tile(String name, int terrainCost, Tile previousTile, boolean wipeInformation) {
        this.name = name;
        this.terrainCost = terrainCost;
        this.wipeInformation = wipeInformation;
        TileManager.put(name, this, previousTile);
    }

    public Tile(String name, int terrainCost, Tile previousTile) {
        this(name, terrainCost, previousTile, false);

    }

    public Tile(String name, int terrainCost) {
        this(name, terrainCost, null, false);
    }

    public void update(){};

    public JSONObject updateInformation(JSONObject info) {
        return updateLocationInformation(wipeInformation ? new JSONObject() : info);
    }

    public JSONObject updateLocationInformation(JSONObject info){ return info; }

	public String toString() {
		return name;
	}

	public int getTerrainCost() {
		return terrainCost;
	}

    public int ordinal() {
        return TileManager.getHeight(this);
    }

    public abstract void render(SpriteBatch batch, TileMapArt tileMap, float elapsed, int x, int y, int variance, Tile facing);

}
