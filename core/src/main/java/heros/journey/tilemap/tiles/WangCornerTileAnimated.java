package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;

public class WangCornerTileAnimated extends Tile {

	protected Animation<TextureRegion>[][] autoTileTextures;
	protected int variance;
	int direction;

	public WangCornerTileAnimated(String name, int terrainCost) {
		super(name, terrainCost);
	}

	public WangCornerTileAnimated(String name, int terrainCost, TextureRegion[][] tiles, int variance, int x, int y, int neutralX, int neutralY, int direction) {
		super(name, terrainCost);
		this.direction = direction;
		autoTileTextures = new Animation[16][variance];
		this.variance = variance;
		for (int i = 1; i < 16; i++) {
			for (int j = 0; j < variance; j++) {
				autoTileTextures[i][j] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x], tiles[y][x + 5], tiles[y][x + 10], tiles[y][x + 15], tiles[y][x + 10], tiles[y][x + 5] });
			}
		}
		for (int j = 0; j < variance; j++) {
			autoTileTextures[0][j] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[neutralY][neutralX] });
		}
	}

	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		int bitVal = 0;
		if (direction == 1) {
			bitVal += map.get(x, y) >= map.get(x + 1, y + 1) && map.get(x, y) >= map.get(x, y + 1) && map.get(x, y) >= map.get(x + 1, y) ? 1 : 0;
			bitVal += map.get(x, y) >= map.get(x + 1, y - 1) && map.get(x, y) >= map.get(x, y - 1) && map.get(x, y) >= map.get(x + 1, y) ? 2 : 0;
			bitVal += map.get(x, y) >= map.get(x - 1, y - 1) && map.get(x, y) >= map.get(x, y - 1) && map.get(x, y) >= map.get(x - 1, y) ? 4 : 0;
			bitVal += map.get(x, y) >= map.get(x - 1, y + 1) && map.get(x, y) >= map.get(x, y + 1) && map.get(x, y) >= map.get(x - 1, y) ? 8 : 0;
		} else if (direction == 2) {
			bitVal += map.get(x, y) == map.get(x + 1, y + 1) && map.get(x, y) == map.get(x, y + 1) && map.get(x, y) == map.get(x + 1, y) ? 1 : 0;
			bitVal += map.get(x, y) == map.get(x + 1, y - 1) && map.get(x, y) == map.get(x, y - 1) && map.get(x, y) == map.get(x + 1, y) ? 2 : 0;
			bitVal += map.get(x, y) == map.get(x - 1, y - 1) && map.get(x, y) == map.get(x, y - 1) && map.get(x, y) == map.get(x - 1, y) ? 4 : 0;
			bitVal += map.get(x, y) == map.get(x - 1, y + 1) && map.get(x, y) == map.get(x, y + 1) && map.get(x, y) == map.get(x - 1, y) ? 8 : 0;
		} else {
			bitVal += map.get(x, y) <= map.get(x + 1, y + 1) && map.get(x, y) <= map.get(x, y + 1) && map.get(x, y) <= map.get(x + 1, y) ? 1 : 0;
			bitVal += map.get(x, y) <= map.get(x + 1, y - 1) && map.get(x, y) <= map.get(x, y - 1) && map.get(x, y) <= map.get(x + 1, y) ? 2 : 0;
			bitVal += map.get(x, y) <= map.get(x - 1, y - 1) && map.get(x, y) <= map.get(x, y - 1) && map.get(x, y) <= map.get(x - 1, y) ? 4 : 0;
			bitVal += map.get(x, y) <= map.get(x - 1, y + 1) && map.get(x, y) <= map.get(x, y + 1) && map.get(x, y) <= map.get(x - 1, y) ? 8 : 0;
		}
		if (!map.blend)
			bitVal = 15;
		batch.draw(autoTileTextures[bitVal][varianceVal % variance].getKeyFrame(elapsedTime, true), x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(),
				GameCamera.get().getSize());
	}

	public void overwriteCircle(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[1][i] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x], tiles[y][x + 5], tiles[y][x + 10], tiles[y][x + 15], tiles[y][x + 10], tiles[y][x + 5] });
			autoTileTextures[2][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 2][x], tiles[y - 2][x + 5], tiles[y - 2][x + 10], tiles[y - 2][x + 15], tiles[y - 2][x + 10], tiles[y - 2][x + 5] });
			autoTileTextures[3][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 1][x], tiles[y - 1][x + 5], tiles[y - 1][x + 10], tiles[y - 1][x + 15], tiles[y - 1][x + 10], tiles[y - 1][x + 5] });
			autoTileTextures[4][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 2][x + 2], tiles[y - 2][x + 7], tiles[y - 2][x + 12], tiles[y - 2][x + 17], tiles[y - 2][x + 12], tiles[y - 2][x + 7] });
			autoTileTextures[6][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 2][x + 1], tiles[y - 2][x + 6], tiles[y - 2][x + 11], tiles[y - 2][x + 16], tiles[y - 2][x + 11], tiles[y - 2][x + 6] });
			autoTileTextures[8][i] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x + 2], tiles[y][x + 7], tiles[y][x + 12], tiles[y][x + 17], tiles[y][x + 12], tiles[y][x + 7] });
			autoTileTextures[9][i] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x + 1], tiles[y][x + 6], tiles[y][x + 11], tiles[y][x + 16], tiles[y][x + 11], tiles[y][x + 6] });
			autoTileTextures[12][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 1][x + 2], tiles[y - 1][x + 7], tiles[y - 1][x + 12], tiles[y - 1][x + 17], tiles[y - 1][x + 12], tiles[y - 1][x + 7] });
			autoTileTextures[15][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 1][x + 1], tiles[y - 1][x + 6], tiles[y - 1][x + 11], tiles[y - 1][x + 16], tiles[y - 1][x + 11], tiles[y - 1][x + 6] });
		}
	}

	public void overwriteOthers(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[5][i] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x], tiles[y][x + 5], tiles[y][x + 10], tiles[y][x + 15], tiles[y][x + 10], tiles[y][x + 5] });
			autoTileTextures[7][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 2][x], tiles[y - 2][x + 5], tiles[y - 2][x + 10], tiles[y - 2][x + 15], tiles[y - 2][x + 10], tiles[y - 2][x + 5] });
			autoTileTextures[10][i] = new Animation<TextureRegion>(.2f, new TextureRegion[] { tiles[y][x + 1], tiles[y][x + 6], tiles[y][x + 11], tiles[y][x + 16], tiles[y][x + 11], tiles[y][x + 6] });
			autoTileTextures[11][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 1][x], tiles[y - 1][x + 5], tiles[y - 1][x + 10], tiles[y - 1][x + 15], tiles[y - 1][x + 10], tiles[y - 1][x + 5] });
			autoTileTextures[13][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 1][x + 1], tiles[y - 1][x + 6], tiles[y - 1][x + 11], tiles[y - 1][x + 16], tiles[y - 1][x + 11], tiles[y - 1][x + 6] });
			autoTileTextures[14][i] = new Animation<TextureRegion>(.2f,
					new TextureRegion[] { tiles[y - 2][x + 1], tiles[y - 2][x + 6], tiles[y - 2][x + 11], tiles[y - 2][x + 16], tiles[y - 2][x + 11], tiles[y - 2][x + 6] });
		}
	}

	public void overwrite2Corner(TextureRegion[][] tiles, int x, int y) {
		overwriteCircle(tiles, x, y);
		overwriteOthers(tiles, x + 3, y);
	}

}
