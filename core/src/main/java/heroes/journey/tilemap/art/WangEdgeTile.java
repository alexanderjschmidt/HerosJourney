package heroes.journey.tilemap.art;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heroes.journey.GameCamera;
import heroes.journey.tilemap.TileMap;
import heroes.journey.utils.input.Options;

public class WangEdgeTile implements TileRender {

	private TextureRegion[][] autoTileTextures;
	private int variance;
	private boolean inverted;

	public WangEdgeTile(TextureRegion[][] tiles, int variance, int x, int y) {
		autoTileTextures = new TextureRegion[16][variance];
		this.variance = variance;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < variance; j++) {
				autoTileTextures[i][j] = tiles[y][x];
			}
		}
	}

	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		int bitVal = 0;
		bitVal += map.get(x, y) == map.get(x, y + 1) ? 1 : 0;
		bitVal += map.get(x, y) == map.get(x + 1, y) ? 2 : 0;
		bitVal += map.get(x, y) == map.get(x, y - 1) ? 4 : 0;
		bitVal += map.get(x, y) == map.get(x - 1, y) ? 8 : 0;
		if (!Options.MAP_BLEND)
			bitVal = 0;
		batch.draw(autoTileTextures[bitVal][varianceVal % variance], x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public void overwriteEdge(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[0][i] = tiles[y][x];
			autoTileTextures[1][i] = tiles[y - 1][x];
			autoTileTextures[2][i] = tiles[y][x + 1];
			autoTileTextures[3][i] = tiles[y - 1][x + 1];
			autoTileTextures[4][i] = tiles[y - 3][x];
			autoTileTextures[5][i] = tiles[y - 2][x];
			autoTileTextures[6][i] = tiles[y - 3][x + 1];
			autoTileTextures[7][i] = tiles[y - 2][x + 1];
			autoTileTextures[8][i] = tiles[y][x + 3];
			autoTileTextures[9][i] = tiles[y - 1][x + 3];
			autoTileTextures[10][i] = tiles[y][x + 2];
			autoTileTextures[11][i] = tiles[y - 1][x + 2];
			autoTileTextures[12][i] = tiles[y - 3][x + 3];
			autoTileTextures[13][i] = tiles[y - 2][x + 3];
			autoTileTextures[14][i] = tiles[y - 3][x + 2];
			autoTileTextures[15][i] = tiles[y - 2][x + 2];
		}
	}

	public void addVariance(TextureRegion[][] tiles, int index, int... coords) {
		for (int i = 0; i < coords.length; i += 2) {
			autoTileTextures[index][(i / 2) + 1] = tiles[coords[i + 1]][coords[i]];
		}
	}

}
