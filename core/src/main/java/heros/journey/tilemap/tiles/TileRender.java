package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.tilemap.TileMap;

public interface TileRender {

	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal);

}
