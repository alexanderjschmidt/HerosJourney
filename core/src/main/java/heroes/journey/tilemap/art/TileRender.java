package heroes.journey.tilemap.art;

import com.badlogic.gdx.graphics.g2d.Batch;

import heroes.journey.tilemap.TileMap;

public interface TileRender {

	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal);

}
