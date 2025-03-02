package fe.game.tilemap;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.managers.ResourceManager;
import fe.game.tilemap.tiles.AnimatedTile;
import fe.game.tilemap.tiles.PlainTile;
import fe.game.tilemap.tiles.Tile;
import fe.game.tilemap.tiles.WangCornerBuildingTile;
import fe.game.tilemap.tiles.WangCornerTile;
import fe.game.tilemap.tiles.WangCornerTileAnimated;
import fe.game.tilemap.tiles.WangEdgeTile;
import fe.game.tilemap.tiles.WangEdgeTileAnimated;

public class Tileset extends ArrayList<Tile> {

	private static final long serialVersionUID = 1L;

	public static final int WATER = 0, PLAINS = 1, HILLS = 2, MOUNTAINS = 3, PATH = 4, RIVER = 5, TREES = 6, WATER_CLIFF = 7, NEUTRAL = 8, NEUTRAL_WATER = 9, BRIDGE = 10;
	public static final int[][] FACING_MATRIX = { { WATER, WATER, WATER_CLIFF, WATER_CLIFF, WATER, NEUTRAL_WATER }, { PLAINS, PLAINS, PLAINS, PLAINS, PLAINS, PLAINS }, { NEUTRAL, HILLS, HILLS, NEUTRAL, HILLS, NEUTRAL },
			{ MOUNTAINS, MOUNTAINS, MOUNTAINS, MOUNTAINS, MOUNTAINS, MOUNTAINS }, { PATH, PATH, PATH, PATH, PATH, PATH }, { WATER, RIVER, RIVER, RIVER, RIVER, RIVER } };

	public Tileset() {

		TextureRegion[][] tiles = ResourceManager.get().tiles;
		WangCornerTile hills = new WangCornerTile("Highland", 1, tiles, 10, 0, 0, 0, 0, 0);
		hills.overwrite2Corner(tiles, 0, 6);
		hills.addVariance(tiles, 0, 1, 0, 2, 0);
		hills.addVariance(tiles, 15, 1, 0, 2, 0);
		WangCornerTile shortGrass = new WangCornerTile("Plains", 1, tiles, 10, 1, 3, 0, 0, 2);
		shortGrass.overwrite2Corner(tiles, 0, 3);
		shortGrass.addVariance(tiles, 0, 1, 0, 2, 0);
		shortGrass.addVariance(tiles, 15, 5, 1, 5, 2, 6, 1, 6, 2);

		WangCornerTileAnimated water = new WangCornerTileAnimated("Water", 10000, tiles, 4, 21, 11, 0, 0, 1);
		water.overwrite2Corner(tiles, 20, 12);
		WangCornerTileAnimated waterCliff = new WangCornerTileAnimated("Water", 10000, tiles, 4, 21, 14, 0, 0, 1);
		waterCliff.overwrite2Corner(tiles, 20, 15);

		WangCornerTile mountain = new WangCornerTile("Mountain", 4, tiles, 5, 10, 17, 15, 16, 2);
		mountain.overwrite2Corner(tiles, 9, 18);
		mountain.addVariance(tiles, 15, 14, 18, 14, 17, 14, 16);

		WangEdgeTile path = new WangEdgeTile("Path", 1, tiles, 3, 12, 3);
		path.overwriteEdge(tiles, 12, 3);
		WangEdgeTile bridge = new WangEdgeTile("Path", 1, tiles, 3, 7, 7);
		bridge.overwriteEdge(tiles, 7, 7);

		WangEdgeTileAnimated river = new WangEdgeTileAnimated("River", 10000, tiles, 3, 3, 20);
		river.overwriteEdge(tiles, 20, 3);

		WangCornerBuildingTile trees = new WangCornerBuildingTile("Trees", 1, tiles, 3, 1, 8, 6, 7);
		trees.overwrite2Corner(tiles, 0, 9);
		trees.addVariance(tiles, 0, 6, 8);
		trees.addVariance(tiles, 15, 5, 7, 5, 8);

		PlainTile neutral = new PlainTile("Grass", 1, tiles, 0, 0);
		TextureRegion[] frames = { tiles[11][21], tiles[11][21 + 5], tiles[11][21 + 10], tiles[11][21 + 15], tiles[11][21 + 10], tiles[11][21 + 5] };
		AnimatedTile neutralWater = new AnimatedTile("Water", frames, 10000);

		// cliffgrass
		// grass
		// sand
		// water
		add(water);
		add(shortGrass);
		add(hills);
		add(mountain);
		add(path);
		add(river);
		add(trees);
		add(waterCliff);
		add(neutral);
		add(neutralWater);
		add(bridge);
		/*
		 * WangCornerTile sand = new WangCornerTile("Sand", 2, tiles, 2, 1, 17, 0, 0,
		 * 1); sand.overwriteCircle(tiles, 0, 18); sand.addVariance(tiles, 1, 3, 17);
		 * sand.addVariance(tiles, 2, 3, 16); sand.addVariance(tiles, 4, 4, 16);
		 * sand.addVariance(tiles, 8, 4, 17); WangCornerTile sandNeutral = new
		 * WangCornerTile("Sand", 2, tiles, 2, 1, 17, 1, 17, 1); WangCornerTile
		 * sandCliff = new WangCornerTile("Sand Cliff", 1, tiles, 10, 0, 0, 1, 17, 0);
		 * sandCliff.overwrite2Corner(tiles, 0, 15); sandCliff.addVariance(tiles, 0, 1,
		 * 0, 2, 0); WangCornerTileAnimated beach = new WangCornerTileAnimated("Beach",
		 * 10000, tiles, 4, 21, 17, 0, 0, 1); beach.overwrite2Corner(tiles, 20, 18);
		 */
	}

	private Tile getTile(int tileIndex) {
		return get(tileIndex);
	}

	public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int index, int variance, int facing) {
		getTile(FACING_MATRIX[index][facing]).render(batch, tileMap, elapsed, x, y, variance);
	}

	public Tile getTerrain(int i) {
		return get(i);
	}

	public Tile getTrees() {
		return get(TREES);
	}

}
