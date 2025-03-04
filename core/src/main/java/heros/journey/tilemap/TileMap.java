package heros.journey.tilemap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.tilemap.tiles.Tile;
import heros.journey.utils.CellularAutomata;

public class TileMap {

	private CellularAutomata gen;
	private Tileset tileset;

	public boolean blend = true;

	private int width, height;
	private int[][] tileMap;
	private int[][] trees;
	private int[][] facing;
	private int[][] variance;
	private float elapsed = 0;

	public TileMap(Tileset tileset, int width, int height, int seed) {
		this(tileset, width, height);

		// 383797854
		// gen = new RandomWorldGenerator(seed, 80, 2, .6f, false);
		gen = new CellularAutomata(width, height, seed);
		genNewMap();
	}

	public TileMap(Tileset tileset, int width, int height) {
		this.tileset = tileset;
		this.width = width;
		this.height = height;
	}

	public TileMap clone(GameState gameState) {
		TileMap clone = new TileMap(tileset, width, height);
		clone.gen = new CellularAutomata(width, height, (int) gen.getSeed());
		clone.elapsed = elapsed;
		clone.blend = blend;
		clone.tileMap = tileMap.clone();
		clone.trees = trees.clone();
		// could possibly remove
		clone.facing = facing.clone();
		clone.variance = variance.clone();
		return clone;
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {
		elapsed += delta;

		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				tileset.render(batch, this, elapsed, x, y, tileMap[x][y], variance[x][y], facing[x][y]);
				if (trees[x][y] != 0)
					tileset.getTrees().render(batch, this, elapsed, x, y, variance[x][y]);
			}
		}
	}

	public int get(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return tileMap[Math.max(0, Math.min(width - 1, x))][Math.max(0, Math.min(height - 1, y))];
		} else {
			return tileMap[x][y];
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getTerrainCost(int x, int y, Entity selected) {
		return selected.getEntityClass().getTerrainCost(tileset.getTerrain(get(x, y))) + (getTrees(x, y) != 0 ? selected.getEntityClass().getTreesCost() : 0);
	}

	public int getTrees(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return trees[Math.max(0, Math.min(width - 1, x))][Math.max(0, Math.min(height - 1, y))];
		} else {
			return trees[x][y];
		}
	}

	public void setTile(int x, int y, int k) {
		tileMap[x][y] = k;
	}

	public boolean hasTree(int x, int y) {
		return trees[x][y] != 0;
	}

	public void clearTrees(int x, int y) {
		trees[x][y] = 0;
	}

	public Tile getTerrain(int x, int y) {
		return tileset.getTerrain(tileMap[x][y]);
	}

	public void genPath(int i, int j, int endX, int endY) {
		gen.genPath(this, i, j, endX, endY);
	}

	public void genNewMap() {
		tileMap = gen.generateMap(width, height);
		trees = gen.generateTrees(tileMap, width);

		// tileMap = gen.mirrorY(tileMap, true);
		// trees = gen.mirrorY(trees, true);
		facing = gen.getFacingMap(tileMap);

		variance = gen.getVarianceMap();
	}

	public void setSeed(int i) {
		gen.setSeed(i);
	}
}
