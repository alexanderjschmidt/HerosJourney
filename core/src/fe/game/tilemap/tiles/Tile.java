package fe.game.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;

import fe.game.tilemap.TileMap;

public abstract class Tile {

	private String name;
	public final int terrainCost;

	public Tile(String name, int terrainCost) {
		this.name = name;
		this.terrainCost = terrainCost;
	}

	public String toString() {
		return name;
	}

	public abstract void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal);

}
