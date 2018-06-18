package fe.game.utils;

import java.util.PriorityQueue;

public class AStar {

	static void checkAndUpdateCost(Cell current, Cell t, int cost,
			PriorityQueue<Cell> open, boolean[][] closed, int dist,
			byte[][] range) {
		if (t == null || closed[t.i][t.j] || range[t.i][t.j] != 1)
			return;

		boolean inOpen = open.contains(t);
		if (current.length < dist && (!inOpen || cost < t.finalCost)) {
			t.finalCost = cost;
			t.parent = current;
			t.length = current.length + 1;
			if (!inOpen)
				open.add(t);
		}
	}

	public static Cell aStar(Cell previousPath, int dist, byte[][] range,
			int startI, int startJ, int endI, int endJ) {
		PriorityQueue<Cell> open = new PriorityQueue<Cell>();

		Cell[][] grid = new Cell[range.length][range[0].length];
		boolean closed[][] = new boolean[range.length][range[0].length];
		boolean fast[][] = new boolean[range.length][range[0].length];

		for (int i = 0; i < range.length; i++) {
			for (int j = 0; j < range[0].length; j++) {
				grid[i][j] = new Cell(i, j);
			}
		}

		Cell pp = previousPath;
		while (pp != null) {
			fast[pp.i][pp.j] = true;
			pp = pp.parent;
		}

		// add the start location to open list.
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
				checkAndUpdateCost(current, t, current.finalCost
						+ (fast[current.i - 1][current.j] ? 1 : 100), open,
						closed, dist, range);
			}
			if (current.j - 1 >= 0) {
				t = grid[current.i][current.j - 1];
				checkAndUpdateCost(current, t, current.finalCost
						+ (fast[current.i][current.j - 1] ? 1 : 100), open,
						closed, dist, range);
			}
			if (current.j + 1 < grid[0].length) {
				t = grid[current.i][current.j + 1];
				checkAndUpdateCost(current, t, current.finalCost
						+ (fast[current.i][current.j + 1] ? 1 : 100), open,
						closed, dist, range);
			}
			if (current.i + 1 < grid.length) {
				t = grid[current.i + 1][current.j];
				checkAndUpdateCost(current, t, current.finalCost
						+ (fast[current.i + 1][current.j] ? 1 : 100), open,
						closed, dist, range);
			}
		}
		return null;
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
}