package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;

public class WangCornerTile extends Tile {

	protected TextureRegion[][] autoTileTextures;
	protected int variance;
	int direction;

	public WangCornerTile(String name, int terrainCost) {
		super(name, terrainCost);
	}

	public WangCornerTile(String name, int terrainCost, TextureRegion[][] tiles, int variance, int x, int y, int neutralX, int neutralY, int direction) {
		super(name, terrainCost);
		this.direction = direction;
		autoTileTextures = new TextureRegion[16][variance];
		this.variance = variance;
		for (int i = 1; i < 16; i++) {
			for (int j = 0; j < variance; j++) {
				autoTileTextures[i][j] = tiles[y][x];
			}
		}
		for (int j = 0; j < variance; j++) {
			autoTileTextures[0][j] = tiles[neutralY][neutralX];
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
		batch.draw(autoTileTextures[bitVal][varianceVal % variance], x * GameCamera.get().getSize(), y * GameCamera.get().getSize(), GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public void overwriteCircle(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[1][i] = tiles[y][x];
			autoTileTextures[2][i] = tiles[y - 2][x];
			autoTileTextures[3][i] = tiles[y - 1][x];
			autoTileTextures[4][i] = tiles[y - 2][x + 2];
			autoTileTextures[6][i] = tiles[y - 2][x + 1];
			autoTileTextures[8][i] = tiles[y][x + 2];
			autoTileTextures[9][i] = tiles[y][x + 1];
			autoTileTextures[12][i] = tiles[y - 1][x + 2];
			autoTileTextures[15][i] = tiles[y - 1][x + 1];
		}
	}

	public void overwriteOthers(TextureRegion[][] tiles, int x, int y) {
		for (int i = 0; i < variance; i++) {
			autoTileTextures[5][i] = tiles[y][x];
			autoTileTextures[7][i] = tiles[y - 2][x];
			autoTileTextures[10][i] = tiles[y][x + 1];
			autoTileTextures[11][i] = tiles[y - 1][x];
			autoTileTextures[13][i] = tiles[y - 1][x + 1];
			autoTileTextures[14][i] = tiles[y - 2][x + 1];
		}
	}

	public void overwrite2Corner(TextureRegion[][] tiles, int x, int y) {
		overwriteCircle(tiles, x, y);
		overwriteOthers(tiles, x + 3, y);
	}

	public void addVariance(TextureRegion[][] tiles, int index, int... coords) {
		for (int i = 0; i < coords.length; i += 2) {
			autoTileTextures[index][(i / 2) + 1] = tiles[coords[i + 1]][coords[i]];
		}
	}

}
