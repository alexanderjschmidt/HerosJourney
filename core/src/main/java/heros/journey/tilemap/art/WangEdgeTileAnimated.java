package heros.journey.tilemap.art;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;
import heros.journey.utils.input.Options;

public class WangEdgeTileAnimated implements TileRender {

	private Animation<TextureRegion>[][] autoTileTextures;
	private int variance;

	public WangEdgeTileAnimated(TextureRegion[][] tiles, int variance, int x, int y) {
		autoTileTextures = new Animation[16][variance];
		this.variance = variance;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < variance; j++) {
				autoTileTextures[i][j] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x], tiles[y][x + 4], tiles[y][x + 8], tiles[y][x + 12], tiles[y][x + 8], tiles[y][x + 4] });
			}
		}
	}

	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		int bitVal = 0;
		bitVal += map.get(x, y) == map.get(x, y + 1) ? 1 : 0;
		bitVal += map.get(x, y) == map.get(x + 1, y) ? 2 : 0;
		bitVal += map.get(x, y) == map.get(x, y - 1) ? 4 : 0;
		bitVal += map.get(x, y) == map.get(x - 1, y) ? 8 : 0;
		if (Options.MAP_BLEND)
			bitVal = 0;
		batch.draw(autoTileTextures[bitVal][varianceVal % variance].getKeyFrame(elapsedTime, true), x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(),
            GameCamera.get().getSize());
	}

	public void overwriteEdge(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[0][i] = getAnimation(tiles, y, x);
			autoTileTextures[1][i] = getAnimation(tiles, y - 1, x);
			autoTileTextures[2][i] = getAnimation(tiles, y, x + 1);
			autoTileTextures[3][i] = getAnimation(tiles, y - 1, x + 1);
			autoTileTextures[4][i] = getAnimation(tiles, y - 3, x);
			autoTileTextures[5][i] = getAnimation(tiles, y - 2, x);
			autoTileTextures[6][i] = getAnimation(tiles, y - 3, x + 1);
			autoTileTextures[7][i] = getAnimation(tiles, y - 2, x + 1);
			autoTileTextures[8][i] = getAnimation(tiles, y, x + 3);
			autoTileTextures[9][i] = getAnimation(tiles, y - 1, x + 3);
			autoTileTextures[10][i] = getAnimation(tiles, y, x + 2);
			autoTileTextures[11][i] = getAnimation(tiles, y - 1, x + 2);
			autoTileTextures[12][i] = getAnimation(tiles, y - 3, x + 3);
			autoTileTextures[13][i] = getAnimation(tiles, y - 2, x + 3);
			autoTileTextures[14][i] = getAnimation(tiles, y - 3, x + 2);
			autoTileTextures[15][i] = getAnimation(tiles, y - 2, x + 2);
		}
	}

	private Animation<TextureRegion> getAnimation(TextureRegion[][] tiles, int x, int y) {
		return new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x], tiles[y][x + 4], tiles[y][x + 8], tiles[y][x + 12], tiles[y][x + 8], tiles[y][x + 4] });
	}

}
