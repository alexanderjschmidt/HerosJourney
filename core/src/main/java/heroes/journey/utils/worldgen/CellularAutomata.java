package heroes.journey.utils.worldgen;

import heroes.journey.utils.Random;
import heroes.journey.initializers.base.Tiles;
import heroes.journey.tilemap.TileManager;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;

public class CellularAutomata {

	private static int tileCount = 6;

	public static int[][] mirrorX(int[][] grid, boolean xAxis) {
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

	public static int[][] mirrorY(int[][] grid, boolean yAxis) {
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

	public static Tile[][] generateMap(int size) {
		Tile[][] map = new Tile[size][size];
		randomize(map);
		for (int i = 1; i <= 5; i++) {
			smooth(map);
		}
		smoothSandAndWater(map);
		return map;
	}

	private static void smooth(Tile[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[map[Math.max(0, Math.min(map.length - 1, k))][Math.max(0, Math.min(map[i].length - 1, l))].ordinal()]++;
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

	private static void smoothSandAndWater(Tile[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				if (map[i][j].ordinal() <= 1) {
					int[] tiles = new int[tileCount];
					for (int k = i - 1; k <= i + 1; k++) {
						tiles[map[Math.max(0, Math.min(map.length - 1, k))][Math.max(0, Math.min(map[i].length - 1, j))].ordinal()]++;
					}
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[map[Math.max(0, Math.min(map.length - 1, i))][Math.max(0, Math.min(map[i].length - 1, l))].ordinal()]++;
					}
					if (tiles[map[i][j].ordinal()] <= 3)
						map[i][j] = TileManager.getTile(tiles[3] > tiles[2] ? 3 : 2);
				}
			}
		}
	}

	public void printMap(int[][] map) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				System.out.print(map[i][j]);
			}
			System.out.println();
		}
	}

	private static void randomize(Tile[][] map) {
		int[][] randomMap = new RandomWorldGenerator(40, 3, .7f, true).generateMap(map.length);
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = getTile(randomMap[i][j]);
			}
		}
	}

	private static Tile getTile(int nextInt) {
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

	public static Tile[][] getFacingMap(Tile[][] tileMap) {
		Tile[][] facing = new Tile[tileMap.length][tileMap[0].length];
		for (int i = 0; i < tileMap.length; i++) {
			for (int j = 0; j < tileMap[i].length; j++) {
				int[] tiles = new int[tileCount];
				for (int k = i - 1; k <= i + 1; k++) {
					for (int l = j - 1; l <= j + 1; l++) {
						tiles[tileMap[Math.max(0, Math.min(tileMap.length - 1, k))][Math.max(0, Math.min(tileMap[i].length - 1, l))].ordinal()]++;
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

	public static ActionTile[][] generateTrees(Tile[][] tileMap, int width2) {
		ActionTile[][] trees = new ActionTile[width2][width2];
		for (int i = 0; i < tileMap.length; i++) {
			for (int j = 0; j < tileMap[i].length; j++) {
				if (tileMap[i][j] == Tiles.PLAINS)
					trees[i][j] = Random.get().nextInt(2) == 1 ? Tiles.TREES : null;
				if (tileMap[i][j] == Tiles.HILLS)
					trees[i][j] = Random.get().nextInt(4) == 1 ? Tiles.TREES : null;
			}
		}
		// Smoothing ???
		for (int s = 0; s < 3; s++) {
			for (int i = 0; i < tileMap.length; i++) {
				for (int j = 0; j < tileMap[i].length; j++) {
					int[] tiles = new int[2];
					for (int k = i - 1; k <= i + 1; k++) {
						for (int l = j - 1; l <= j + 1; l++) {
							ActionTile tree = trees[Math.max(0, Math.min(tileMap.length - 1, k))][Math.max(0, Math.min(tileMap[i].length - 1, l))];
							tiles[tree != null ? 1 : 0]++;
						}
					}
					if (trees[i][j] == null)
						trees[i][j] = tiles[1] >= 6 ? Tiles.TREES : null;
					else
						trees[i][j] = tiles[1] <= 3 ? null : Tiles.TREES;
					if (trees[i][j] == Tiles.TREES && tiles[1] > 7)
						trees[i][j] = Random.get().nextInt(3) == 1 ? null : Tiles.TREES;
				}
			}
		}
		return trees;
	}

	public static int[][] getVarianceMap(Tile[][] tileMap) {
		int[][] variance = new int[tileMap.length][tileMap[0].length];
		for (int x = 0; x < tileMap.length; x++) {
			for (int y = 0; y < tileMap[x].length; y++) {
				variance[x][y] = Random.get().nextInt(100);
			}
		}
		return variance;
	}

}
