package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heros.journey.tilemap.TileMap;
import heros.journey.utils.art.ResourceManager;
import heros.journey.utils.art.TextureMaps;

public enum Tile implements TileInterface {

	WATER("Water", 12) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			if (facing == PLAINS || facing == MOUNTAINS || facing == PATH) {
				waterArt.render(batch, tileMap, elapsed, x, y, variance);
			} else if (facing == SAND) {
				beach.render(batch, tileMap, elapsed, x, y, variance);
			} else if (facing == HILLS) {
				waterCliff.render(batch, tileMap, elapsed, x, y, variance);
			}
		}
	},
	SAND("Sand", 3) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			if (facing == PLAINS || facing == HILLS || facing == MOUNTAINS || facing == PATH)
				sandArt.render(batch, tileMap, elapsed, x, y, variance);
			else
				sandNeutral.render(batch, tileMap, elapsed, x, y, variance);
		}
	},
	PLAINS("Plains", 2) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			shortGrass.render(batch, tileMap, elapsed, x, y, variance);
		}
	},
	HILLS("Hill", 2) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			if (facing == WATER || facing == MOUNTAINS || facing == PATH) {
				longGrass.render(batch, tileMap, elapsed, x, y, variance);
			} else if (facing == SAND) {
				hills.render(batch, tileMap, elapsed, x, y, variance);
			} else if (facing == PLAINS) {
				hills.render(batch, tileMap, elapsed, x, y, variance);
			}
		}
	},
	MOUNTAINS("Mountain", 24) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			mountainArt.render(batch, tileMap, elapsed, x, y, variance);
		}
	},
	PATH("Path", 1) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			pathArt.render(batch, tileMap, elapsed, x, y, variance);
		}
	};

	// Art
	private static WangCornerTile longGrass, hills, shortGrass, sandArt, sandNeutral, cliffArt, sandCliff, mountainArt;
	private static WangCornerTileAnimated waterArt, waterCliff, beach;
	private static WangEdgeTile pathArt, shortpath;
	private static WangEdgeTileAnimated river;

	static {
		// add(new AnimatedTile("Water", new TextureRegion[] {
		// ResourceManager.get().sprites[0][3], ResourceManager.get().sprites[0][4],
		// ResourceManager.get().sprites[0][5], ResourceManager.get().sprites[0][6] },
		// 10000));
		// add(new PlainTile("Sand", ResourceManager.get().sprites[0][2], 2));
		// add(new PlainTile("Grass", ResourceManager.get().sprites[0][0], 1));
		// add(new PlainTile("Stone", ResourceManager.get().sprites[0][1], 3));

		/*
		 * Grass 100 Hill 100 Grass 100 Sand 300 Sand 300 Cliff 100 Sand Cliff 300 Water
		 * 2000 Water Cliff 100 Beach 300 Mountain 5000 Path 10 Path 10 River 60 Trees
		 * 100 House 0
		 *
		 * Grass 100 Hill 100 Grass 100 Sand 200 Sand 200 Cliff 100 Sand Cliff 200 Water
		 * 1200 Water Cliff 100 Beach 300 Mountain 2400 Path 0 Path 0 River 600 Trees
		 * 100 House 0
		 */

		TextureRegion[][] tiles = ResourceManager.get(TextureMaps.OverworldTileset);
		longGrass = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 2);
		longGrass.addVariance(tiles, 0, 1, 0, 2, 0);
		longGrass.addVariance(tiles, 15, 1, 0, 2, 0);
		shortGrass = new WangCornerTile(tiles, 10, 1, 3, 0, 0, 2);
		shortGrass.overwriteAll(tiles, 0, 3);
		shortGrass.addVariance(tiles, 0, 1, 0, 2, 0);
		shortGrass.addVariance(tiles, 15, 5, 1, 5, 2, 6, 1, 6, 2);
		hills = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 0);
		hills.overwriteCircle(tiles, 0, 6);
		hills.addVariance(tiles, 0, 1, 0, 2, 0);
		hills.addVariance(tiles, 15, 1, 0, 2, 0);
		sandArt = new WangCornerTile(tiles, 2, 1, 17, 0, 0, 1);
		sandArt.overwriteCircle(tiles, 0, 18);
		sandArt.addVariance(tiles, 1, 3, 17);
		sandArt.addVariance(tiles, 2, 3, 16);
		sandArt.addVariance(tiles, 4, 4, 16);
		sandArt.addVariance(tiles, 8, 4, 17);
		sandNeutral = new WangCornerTile(tiles, 2, 1, 17, 1, 17, 1);

		cliffArt = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 0);
		cliffArt.overwriteAll(tiles, 0, 12);
		cliffArt.addVariance(tiles, 0, 1, 0, 2, 0);
		cliffArt.addVariance(tiles, 15, 1, 0, 2, 0);
		sandCliff = new WangCornerTile(tiles, 10, 0, 0, 1, 17, 0);
		sandCliff.overwriteAll(tiles, 0, 15);
		sandCliff.addVariance(tiles, 0, 1, 0, 2, 0);

		waterArt = new WangCornerTileAnimated(tiles, 4, 21, 11, 0, 0, 1);
		waterArt.overwrite2Corner(tiles, 20, 12);
		waterCliff = new WangCornerTileAnimated(tiles, 4, 21, 14, 0, 0, 1);
		waterCliff.overwrite2Corner(tiles, 20, 15);
		beach = new WangCornerTileAnimated(tiles, 4, 21, 17, 0, 0, 1);
		beach.overwrite2Corner(tiles, 20, 18);

		mountainArt = new WangCornerTile(tiles, 5, 10, 17, 15, 16, 2);
		mountainArt.overwriteAll(tiles, 9, 18);
		mountainArt.addVariance(tiles, 15, 14, 18, 14, 17, 14, 16);

		pathArt = new WangEdgeTile(tiles, 1, 12, 3);
		pathArt.overwriteEdge(tiles, 12, 3);
		shortpath = new WangEdgeTile(tiles, 3, 7, 3);
		shortpath.overwriteEdge(tiles, 7, 3);

		river = new WangEdgeTileAnimated(tiles, 3, 20, 3);
		river.overwriteEdge(tiles, 20, 3);
	}

	private String name;
	private int terrainCost;

	private Tile(String name, int terrainCost) {
		this.name = name;
		this.terrainCost = terrainCost;
	}

	public String toString() {
		return name;
	}

	public int getTerrainCost() {
		return terrainCost;
	}

}
