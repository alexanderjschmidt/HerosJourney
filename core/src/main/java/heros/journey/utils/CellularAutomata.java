package heros.journey.utils;

import java.util.Random;

import heros.journey.tilemap.TileMap;
import heros.journey.tilemap.Tileset;

public class CellularAutomata {

	Random random;

	public static final int tileCount = 4;
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

	public int[][] generateMap(int width, int height) {
		this.width = width;
		this.height = height;
		int[][] map = new int[width][height];
		randomize(seed, map);
		for (int i = 1; i <= 4; i++) {
			smooth(map);
		}
		// dsmooth2(map);
		smooth2(map);
		smoothSandAndWater(map);
		return map;
	}

	public void genPath(TileMap tileMap, int startX, int startY, int endX, int endY) {
		Cell path = AStar.aStar(startX, startY, endX, endY, tileMap);
		while (path != null) {
			tileMap.setTile(path.i, path.j, Tileset.PATH);
			tileMap.clearTrees(path.i, path.j);
			path = path.parent;
		}
	}

	public void smooth(int[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[map[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))]]++;
					}
				}
				int max = map[i][j];
				for (int k = 0; k < tiles.length; k++) {
					if (tiles[k] > tiles[max])
						max = k;
				}
				map[i][j] = max;
			}
		}
	}

	public void smooth2(int[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 2; k <= i + 2; k++) {
					for (int l = j - 2; l <= j + 2; l++) {
						tiles[map[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))]]++;
					}
				}
				int max = map[i][j];
				for (int k = 0; k < tiles.length; k++) {
					if (tiles[k] > tiles[max])
						max = k;
				}
				map[i][j] = max;
			}
		}
	}

	public void smoothSandAndWater(int[][] map) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				// if (map[i][j] <= 1) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					tiles[map[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, j))]]++;
				}
				for (int l = j - 1; l <= j + 1; l++) {
					tiles[map[Math.max(0, Math.min(width - 1, i))][Math.max(0, Math.min(height - 1, l))]]++;
				}
				if (tiles[map[i][j]] <= 3)
					map[i][j] = tiles[3] > tiles[2] ? 3 : 2;
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

	public void randomize(int[][] map) {
		this.randomize(seed, map);
	}

	private void randomize(long seed, int[][] map) {
		random.setSeed(seed);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = getTile(random.nextInt(100));
				// if (i == 0)
				// map[i][j] = 0;
			}
		}
		int[] corners = { 1, 2, 2, 1 };
		// bottom left, top left, bottom right, top right
		for (int i = 0; i < width / 8; i++) {
			for (int j = 0; j < height / 8; j++) {
				map[i][j] = corners[0];
			}
			for (int j = height * 7 / 8; j < height; j++) {
				map[i][j] = corners[1];
			}
		}
		for (int i = width * 7 / 8; i < width; i++) {
			for (int j = 0; j < height / 8; j++) {
				map[i][j] = corners[2];
			}
			for (int j = height * 7 / 8; j < height; j++) {
				map[i][j] = corners[3];
			}
		}
	}

	private int getTile(int nextInt) {
		if (nextInt < 20)
			return 0;
		else if (nextInt < 50)
			return 1;
		else if (nextInt < 80)
			return 2;
		else if (nextInt < 100)
			return 3;
		return 2;
	}

	public int[][] getFacingMap(int[][] tileMap) {
		int[][] facing = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[tileMap[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))]]++;
					}
				}
				int max = tileMap[i][j] == 0 ? 1 : 0;
				for (int k = 0; k < tiles.length; k++) {
					if (k != tileMap[i][j] && tiles[k] >= tiles[max])
						max = k;
				}
				facing[i][j] = max;
			}
		}
		return facing;
	}

	public int[][] generateTrees(int[][] tileMap, int width2) {
		int[][] trees = new int[width2][width2];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (tileMap[i][j] == Tileset.PLAINS)
					trees[i][j] = random.nextInt(2) == 1 ? 1 : 0;
				if (tileMap[i][j] == Tileset.HILLS)
					trees[i][j] = random.nextInt(4) == 1 ? 1 : 0;
			}
		}
		for (int s = 0; s < 4; s++) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int[] tiles = new int[2];
					for (int k = i - 1; k <= i + 1; k++) {
						for (int l = j - 1; l <= j + 1; l++) {
							tiles[trees[Math.max(0, Math.min(width - 1, k))][Math.max(0, Math.min(height - 1, l))]]++;
						}
					}
					if (trees[i][j] == 0 && (tileMap[i][j] == Tileset.PLAINS || tileMap[i][j] == Tileset.HILLS))
						trees[i][j] = tiles[1] >= 6 ? 1 : 0;
					else
						trees[i][j] = tiles[1] <= 3 ? 0 : 1;
					if (trees[i][j] == 1 && tiles[1] > 7)
						trees[i][j] = random.nextInt(3) == 1 ? 0 : 1;
					if (trees[i][j] == 1 && (tileMap[i][j] != Tileset.PLAINS && tileMap[i][j] != Tileset.HILLS))
						trees[i][j] = 0;
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

	public void setSeed(long i) {
		this.seed = i;
	}

	public long getSeed() {
		return seed;
	}

}
