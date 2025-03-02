package fe.game.utils;

import java.util.PriorityQueue;

import fe.game.entities.Entity;
import fe.game.entities.EntityManager;
import fe.game.tilemap.TileMap;
import fe.game.tilemap.Tileset;

public class AStar2 {

	public static Cell aStar(int startI, int startJ, int endI, int endJ, TileMap tileMap) {
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();

		Cell[][] grid = new Cell[tileMap.getWidth()][tileMap.getHeight()];
		boolean closed[][] = new boolean[tileMap.getWidth()][tileMap.getHeight()];

		for (int i = 0; i < tileMap.getWidth(); i++) {
			for (int j = 0; j < tileMap.getHeight(); j++) {
				grid[i][j] = new Cell(i, j);
			}
		}

		// add the start location to open list.
		grid[startI][startJ].g = 0;
		grid[startI][startJ].f = manhattanHeuristic(startI, startJ, endI, endJ);
		open.add(grid[startI][startJ]);

		Cell current;

		while (!open.isEmpty()) {
			current = open.poll();
			closed[current.i][current.j] = true;

			if (current.equals(grid[endI][endJ])) {
				return current;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCost(current, t, open, closed, tileMap, endI, endJ);
			}
			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, open, closed, tileMap, endI, endJ);
			}
			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, open, closed, tileMap, endI, endJ);
			}
			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, open, closed, tileMap, endI, endJ);
			}
		}
		return null;
	}

	private static void checkAndUpdateCost(Cell current, Cell t, PriorityQueue<Cell> open, boolean[][] closed, TileMap map, int endI, int endJ) {
		if (t == null || closed[t.i][t.j])
			return;

		int tent = current.g + getPathCost(map, t.i, t.j);
		if (!open.contains(t)) {
			open.add(t);
		} else if (tent >= t.g) {
			return;
		}
		t.parent = current;
		t.g = tent;
		t.f = t.g + manhattanHeuristic(t.i, t.j, endI, endJ);
	}

	private static int manhattanHeuristic(int i, int j, int endI, int endJ) {
		return manhattanDist(i, j, endI, endJ) * 10;
	}

	public static int manhattanDist(int i, int j, int endI, int endJ) {
		int dx = Math.abs(i - endI);
		int dy = Math.abs(j - endJ);
		return (int) ((dx + dy));
	}

	private static int getPathCost(TileMap map, int i, int j) {
		int tile = map.get(i, j);
		int cost = 0;
		if (tile == Tileset.WATER) {
			cost += 50;
		} else if (tile == Tileset.PLAINS) {
			cost += 7;
		} else if (tile == Tileset.HILLS) {
			cost += 7;
		} else if (tile == Tileset.MOUNTAINS) {
			cost += 50;
		} else if (tile == Tileset.PATH) {
			cost += 7;
		}
		if (map.getTrees(i, j) == 1) {
			cost += 3;
		}
		return cost;
	}

	public static Cell aStarEntity(int dist1, int[][] range, int startI, int startJ, int endI, int endJ, TileMap map, Entity selected) {
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();
		int dist = dist1 + 1;
		Cell[][] grid = new Cell[range.length][range[0].length];
		boolean closed[][] = new boolean[range.length][range[0].length];

		for (int i = 0; i < range.length; i++) {
			for (int j = 0; j < range[0].length; j++) {
				grid[i][j] = new Cell(i, j);
			}
		}

		// add the start location to open list.
		grid[startI][startJ].g = 0;
		grid[startI][startJ].f = manhattanDist(startI, startJ, endI, endJ);
		open.add(grid[startI][startJ]);

		Cell current;

		while (!open.isEmpty()) {
			current = open.poll();
			closed[current.i][current.j] = true;

			if (current.equals(grid[endI][endJ]) || current.g == dist) {
				return current;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, endI, endJ);
			}
			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, endI, endJ);
			}
			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, endI, endJ);
			}
			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, endI, endJ);
			}
		}
		return null;
	}

	private static void checkAndUpdateCostEntity(Cell current, Cell t, PriorityQueue<Cell> open, boolean[][] closed, int dist, int[][] range, TileMap map, Entity selected, int endI, int endJ) {
		if (t == null || closed[t.i][t.j] || range[t.i][t.j] != 1)
			return;

		int tent = current.g + map.getTerrainCost(t.i, t.j, selected);
		if (tent > dist) {
			return;
		}
		if (!open.contains(t)) {
			open.add(t);
		} else if (tent >= t.g) {
			return;
		}
		t.parent = current;
		t.g = tent;
		t.f = t.g + manhattanDist(t.i, t.j, endI, endJ);
	}

	public static Cell reversePath(Cell node) {
		Cell prev = null;
		Cell current = node;
		Cell next = null;
		while (current != null) {
			next = current.parent;
			current.parent = prev;
			prev = current;
			current = next;
		}
		node = prev;
		return node;
	}

	public static Cell aStarAI(int dist, int[][] range, int startI, int startJ, int endI, int endJ, TileMap map, EntityManager entities, Entity selected) {
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();

		int[] ranges = selected.getEntityClass().getRanges();
		Cell[][] grid = new Cell[range.length][range[0].length];
		boolean closed[][] = new boolean[range.length][range[0].length];

		int newEndI = 0;
		int newEndJ = 0;
		int maxManhattan = -1;
		int moveDistOfMax = selected.getEntityClass().getMoveDistance();
		for (int i = 0; i < range.length; i++) {
			for (int j = 0; j < range[0].length; j++) {
				grid[i][j] = new Cell(i, j);
				boolean inRange = false;
				for (Integer r : ranges) {
					if (manhattanDist(i, j, endI, endJ) == r) {
						inRange = true;
					}
				}
				if (inRange && range[i][j] == 1 && (entities.get(i, j) == null || entities.get(i, j) == selected)) {
					int distToEnemy = manhattanDist(i, j, endI, endJ);
					int moveDist = manhattanDist(startI, startJ, i, j);
					if (distToEnemy > maxManhattan || (distToEnemy == maxManhattan && moveDistOfMax > moveDist)) {
						maxManhattan = distToEnemy;
						moveDistOfMax = moveDist;
						newEndI = i;
						newEndJ = j;
					}
				}
			}
		}

		// add the start location to open list.
		grid[startI][startJ].g = 0;
		grid[startI][startJ].f = manhattanDist(startI, startJ, newEndI, newEndJ);
		open.add(grid[startI][startJ]);

		Cell current;

		while (!open.isEmpty()) {
			current = open.poll();
			closed[current.i][current.j] = true;

			if (current.equals(grid[newEndI][newEndJ])) {
				return current;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, newEndI, newEndJ);
			}
			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, newEndI, newEndJ);
			}
			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, newEndI, newEndJ);
			}
			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCostEntity(current, t, open, closed, dist, range, map, selected, newEndI, newEndJ);
			}
		}
		return new Cell(startI, startJ);
	}

	public static Cell aStarAILong(int[][] range, int startI, int startJ, int endI, int endJ, TileMap map, EntityManager entities, Entity selected) {
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();

		Cell[][] grid = new Cell[range.length][range[0].length];
		boolean closed[][] = new boolean[range.length][range[0].length];
		for (int i = 0; i < range.length; i++) {
			for (int j = 0; j < range[0].length; j++) {
				grid[i][j] = new Cell(i, j);
			}
		}

		// add the start location to open list.
		grid[startI][startJ].g = 0;
		grid[startI][startJ].f = manhattanDist(startI, startJ, endI, endJ);
		open.add(grid[startI][startJ]);

		Cell current;

		while (!open.isEmpty()) {
			current = open.poll();
			closed[current.i][current.j] = true;

			if (current.equals(grid[endI][endJ])) {
				return current;
			}

			Cell t;
			if (current.i - 1 >= 0) {
				t = grid[current.i - 1][current.j];
				checkAndUpdateCostLong(current, t, open, closed, map, selected, endI, endJ);
			}
			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCostLong(current, t, open, closed, map, selected, endI, endJ);
			}
			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCostLong(current, t, open, closed, map, selected, endI, endJ);
			}
			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCostLong(current, t, open, closed, map, selected, endI, endJ);
			}
		}
		return new Cell(startI, startJ);
	}

	private static void checkAndUpdateCostLong(Cell current, Cell t, PriorityQueue<Cell> open, boolean[][] closed, TileMap map, Entity selected, int endI, int endJ) {
		if (t == null || closed[t.i][t.j])
			return;

		int tent = current.g + map.getTerrainCost(t.i, t.j, selected);
		if (!open.contains(t)) {
			open.add(t);
		} else if (tent >= t.g) {
			return;
		}
		t.parent = current;
		t.g = tent;
		t.f = t.g + manhattanDist(t.i, t.j, endI, endJ);
	}

	public static Cell prune(Cell path, EntityManager entities, Entity e) {
		Cell p = path;
		Cell[] temp = new Cell[e.getEntityClass().getMoveDistance()];
		for (int i = 0; i < temp.length && p != null; i++) {
			temp[i] = p;
			p = p.parent;
		}

		for (int i = temp.length - 1; i >= 0; i--) {
			if (temp[i] != null && (entities.get(temp[i].i, temp[i].j) != null && entities.get(temp[i].i, temp[i].j) != e)) {
				temp[i] = null;
			} else if (temp[i] != null) {
				temp[i].parent = null;
				break;
			}
		}
		return temp[0];
	}

	public static void printPath(Cell path) {
		while (path != null) {
			System.out.print("(" + path.i + ", " + path.j + ") ");
			path = path.parent;
		}
		System.out.println();
	}
}