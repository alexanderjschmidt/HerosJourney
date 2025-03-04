package heros.journey.utils.worldgen;

import heros.journey.tilemap.TileMap;
import heros.journey.tilemap.tiles.ActionTile;
import heros.journey.tilemap.tiles.Tile;
import heros.journey.tilemap.tiles.TileInterface;
import heros.journey.utils.pathfinding.AStar;
import heros.journey.utils.pathfinding.Cell;

import java.util.Random;

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

	public Tile[][] generateMap(int size) {
		this.width = size;
		this.height = size;
		Tile[][] map = new Tile[width][height];
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
			tileMap.setTile(path.i, path.j, Tile.PATH);
			path = path.parent;
		}
	}

	private void smooth(Tile[][] map) {
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
				map[i][j] = Tile.values()[max];
			}
		}
	}

	private void smoothSandAndWater(Tile[][] map) {
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
						map[i][j] = Tile.values()[tiles[3] > tiles[2] ? 3 : 2];
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
			return Tile.WATER;
		else if (nextInt < 35)
			return Tile.SAND;
		else if (nextInt < 60)
			return Tile.PLAINS;
		else if (nextInt < 80)
			return Tile.HILLS;
		return Tile.MOUNTAINS;
	}

	public Tile[][] getFacingMap(Tile[][] tileMap) {
		Tile[][] facing = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[tileMap[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))].ordinal()]++;
					}
				}
				int max = tileMap[i][j] == Tile.WATER ? 1 : 0;
				for (int k = 0; k < tiles.length; k++) {
					if (k != tileMap[i][j].ordinal() && tiles[k] >= tiles[max])
						max = k;
				}
				facing[i][j] = Tile.values()[max];
			}
		}
		return facing;
	}

	public ActionTile[][] generateTrees(Tile[][] tileMap, int width2) {
		ActionTile[][] trees = new ActionTile[width2][width2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tileMap[i][j] == Tile.PLAINS)
					trees[i][j] = random.nextInt(2) == 1 ? ActionTile.TREES : null;
				if (tileMap[i][j] == Tile.HILLS)
					trees[i][j] = random.nextInt(4) == 1 ? ActionTile.TREES : null;
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
						trees[i][j] = tiles[1] >= 6 ? ActionTile.TREES : null;
					else
						trees[i][j] = tiles[1] <= 3 ? null : ActionTile.TREES;
					if (trees[i][j] == ActionTile.TREES && tiles[1] > 7)
						trees[i][j] = random.nextInt(3) == 1 ? null : ActionTile.TREES;
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
