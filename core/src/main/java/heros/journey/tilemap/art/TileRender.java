package heros.journey.tilemap.art;

import com.badlogic.gdx.graphics.g2d.Batch;
import heros.journey.tilemap.TileMapArt;

public interface TileRender {

	public void render(Batch batch, TileMapArt map, float elapsedTime, int x, int y, int varianceVal);

}
