package heros.journey.utils.worldgen;

import java.util.Random;

import heros.journey.initializers.base.Tiles;
import heros.journey.tilemap.TileMap;
import heros.journey.tilemap.tiles.ActionTile;
import heros.journey.tilemap.tiles.TileInterface;
import heros.journey.tilemap.TileManager;
import heros.journey.utils.pathfinding.AStar;
import heros.journey.utils.pathfinding.Cell;

public class CellularAutomata {

	Random random;

	int tileCount = 6;
	int width;
	int height;
	int scale = 4;
	long seed = 0;

	public CellularAutomata(int width, int height, int seed) {
		random = new Random();
		this.width = width;
		this.height = height;
		this.seed = seed;
	}

	public int[][] mirrorX(int[][] grid, boolean xAxis) {
		if (xAxis) {
			for (int x = 0; x < grid.length / 2; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					grid[grid.length - x - 1][y] = grid[x][y];
				}
			}
		} else {
			for (int x = 0; x < grid.length / 2; x++) {
				for (int y = 0; y < grid[0].length; y++) {
					grid[x][y] = grid[grid.length - x - 1][y];
				}
			}
		}
		return grid;
	}

	public int[][] mirrorY(int[][] grid, boolean yAxis) {
		if (yAxis) {
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length / 2; y++) {
					grid[x][grid[0].length - y - 1] = grid[x][y];
				}
			}
		} else {
			for (int x = 0; x < grid.length; x++) {
				for (int y = 0; y < grid[0].length / 2; y++) {
					grid[x][y] = grid[x][grid[0].length - y - 1];
				}
			}
		}
		return grid;
	}

	public heros.journey.tilemap.tiles.Tile[][] generateMap(int size) {
		this.width = size;
		this.height = size;
		heros.journey.tilemap.tiles.Tile[][] map = new heros.journey.tilemap.tiles.Tile[width][height];
		randomize(seed, map);
		for (int i = 1; i <= 5; i++) {
			smooth(map);
		}
		smoothSandAndWater(map);
		return map;
	}

	public void genPath(TileMap tileMap, int startX, int startY, int endX, int endY) {
		Cell path = AStar.aStar(startX, startY, endX, endY, tileMap);
		while (path != null) {
			tileMap.setTile(path.i, path.j, Tiles.PATH);
			path = path.parent;
		}
	}

	private void smooth(heros.journey.tilemap.tiles.Tile[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[map[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))].ordinal()]++;
					}
				}
				int max = map[i][j].ordinal();
				for (int k = 0; k < tiles.length; k++) {
					if (tiles[k] > tiles[max])
						max = k;
				}
				map[i][j] = TileManager.getTile(max);
			}
		}
	}

	private void smoothSandAndWater(heros.journey.tilemap.tiles.Tile[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (map[i][j].ordinal() <= 1) {
					int[] tiles = new int[tileCount];
					for (int k = i - 1; k <= i + 1; k++) {
						tiles[map[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, j))].ordinal()]++;
					}
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[map[Math.max(0, Math.min(width - 1, i))][Math.max(0, Math.min(height - 1, l))].ordinal()]++;
					}
					if (tiles[map[i][j].ordinal()] <= 3)
						map[i][j] = TileManager.getTile(tiles[3] > tiles[2] ? 3 : 2);
				}
			}
		}
	}

	public void printMap(int[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

	private void randomize(long seed, TileInterface[][] map) {
		random.setSeed(seed);
		int[][] randomMap = new RandomWorldGenerator(40, 3, .7f, true).generateMap(width);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = getTile(randomMap[i][j]);
				// if (i == 0)
				// map[i][j] = 0;
			}
		}
	}

	private TileInterface getTile(int nextInt) {
		if (nextInt < 20)
			return Tiles.WATER;
		else if (nextInt < 35)
			return Tiles.SAND;
		else if (nextInt < 60)
			return Tiles.PLAINS;
		else if (nextInt < 80)
			return Tiles.HILLS;
		return Tiles.MOUNTAINS;
	}

	public heros.journey.tilemap.tiles.Tile[][] getFacingMap(heros.journey.tilemap.tiles.Tile[][] tileMap) {
		heros.journey.tilemap.tiles.Tile[][] facing = new heros.journey.tilemap.tiles.Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[tileMap[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))].ordinal()]++;
					}
				}
				int max = tileMap[i][j] == Tiles.WATER ? 1 : 0;
				for (int k = 0; k < tiles.length; k++) {
					if (k != TileManager.getHeight(tileMap[i][j]) && tiles[k] >= tiles[max])
						max = k;
				}
				facing[i][j] = TileManager.getTile(max);
			}
		}
		return facing;
	}

	public ActionTile[][] generateTrees(heros.journey.tilemap.tiles.Tile[][] tileMap, int width2) {
		ActionTile[][] trees = new ActionTile[width2][width2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tileMap[i][j] == Tiles.PLAINS)
					trees[i][j] = random.nextInt(2) == 1 ? Tiles.TREES : null;
				if (tileMap[i][j] == Tiles.HILLS)
					trees[i][j] = random.nextInt(4) == 1 ? Tiles.TREES : null;
			}
		}
		// Smoothing ???
		for (int s = 0; s < 3; s++) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int[] tiles = new int[2];
					for (int k = i - 1; k <= i + 1; k++) {
						for (int l = j - 1; l <= j + 1; l++) {
							ActionTile tree = trees[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))];
							tiles[tree != null ? 1 : 0]++;
						}
					}
					if (trees[i][j] == null)
						trees[i][j] = tiles[1] >= 6 ? Tiles.TREES : null;
					else
						trees[i][j] = tiles[1] <= 3 ? null : Tiles.TREES;
					if (trees[i][j] == Tiles.TREES && tiles[1] > 7)
						trees[i][j] = random.nextInt(3) == 1 ? null : Tiles.TREES;
				}
			}
		}
		return trees;
	}

	public int[][] getVarianceMap() {
		int[][] variance = new int[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				variance[x][y] = random.nextInt(100);
			}
		}
		return variance;
	}

	public void setSeed(int i) {
		this.seed = i;
	}

}
