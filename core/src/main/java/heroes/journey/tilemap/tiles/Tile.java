package heroes.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import heroes.journey.tilemap.TileManager;
import heroes.journey.tilemap.TileMap;

public abstract class Tile {

	private String name;
	private int terrainCost;

    public Tile(String name, int terrainCost) {
        this.name = name;
        this.terrainCost = terrainCost;
        TileManager.put(name, this, null);
    }

    public Tile(String name, int terrainCost, Tile previousTile) {
        this.name = name;
        this.terrainCost = terrainCost;
        TileManager.put(name, this, previousTile);
    }

	public String toString() {
		return name;
	}

	public int getTerrainCost() {
		return terrainCost;
	}

    public int ordinal() {
        return TileManager.getHeight(this);
    }

    public abstract void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing);

}
