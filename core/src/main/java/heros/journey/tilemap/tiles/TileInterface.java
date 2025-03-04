package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heros.journey.tilemap.TileMap;

public interface TileInterface {

	public int getTerrainCost();

	public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing);

}
