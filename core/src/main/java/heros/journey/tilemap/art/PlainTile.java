package heros.journey.tilemap.art;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;

public class PlainTile implements TileRender {

	private TextureRegion[] textures;
	private int variance;

	public PlainTile(TextureRegion[][] tiles, int variance, int x, int y) {
		textures = new TextureRegion[variance];
		this.variance = variance;
		for (int j = 0; j < variance; j++) {
			textures[j] = tiles[y][x];
		}
	}

	@Override
	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		batch.draw(textures[varianceVal % variance], x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public void addVariance(TextureRegion[][] tiles, int... coords) {
		for (int i = 0; i < coords.length; i += 2) {
			textures[(i / 2) + 1] = tiles[coords[i + 1]][coords[i]];
		}
	}

}
