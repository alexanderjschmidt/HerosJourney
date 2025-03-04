package heros.journey.tilemap.tiles;

import heros.journey.tilemap.TileManager;

public abstract class Tile implements TileInterface {

	private String name;
	private int terrainCost;

    public Tile(String name, int terrainCost) {
        this.name = name;
        this.terrainCost = terrainCost;
        TileManager.put(name, this, null);
    }

    public Tile(String name, int terrainCost, TileInterface previousTile) {
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

}
