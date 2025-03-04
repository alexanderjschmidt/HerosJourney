package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;

public class PlainTile extends Tile {

	private TextureRegion texture;

	public PlainTile(String name, int terrainCost, TextureRegion[][] tiles, int x, int y) {
		super(name, terrainCost);
		texture = tiles[y][x];
	}

	@Override
	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		batch.draw(texture, x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(), GameCamera.get().getSize());
	}

}
