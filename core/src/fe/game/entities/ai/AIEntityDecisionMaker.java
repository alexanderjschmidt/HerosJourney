package fe.game.entities.ai;

import java.util.ArrayList;

import fe.game.GameState;
import fe.game.entities.Entity;
import fe.game.entities.EntityClassManager;
import fe.game.entities.Team;
import fe.game.entities.skills.Skill;
import fe.game.utils.Cell;

public class AIEntityDecisionMaker {

	public AIEntityDecisionMaker() {
	}

	public int[][] getCastleDistMap(GameState state, Team t) {
		int[][] distMap = new int[state.getWidth()][state.getHeight()];

		for (Team team : state.getTeams()) {
			if (team != t) {
				boolean[][] touched = new boolean[state.getWidth()][state.getHeight()];
				System.out.println(team);
				distMap = floodfillDist(state, distMap, touched, team.getCastle().getXCoord(), team.getCastle().getYCoord(), 0);
			}
		}

		return distMap;
	}

	private int[][] floodfillDist(GameState state, int[][] distMap, boolean[][] touched, int x, int y, int distTraveled) {
		ArrayList<Cell> open = new ArrayList<Cell>();
		Cell[][] grid = new Cell[state.getWidth()][state.getHeight()];
		boolean closed[][] = new boolean[state.getWidth()][state.getHeight()];

		for (int i = 0; i < state.getWidth(); i++) {
			for (int j = 0; j < state.getHeight(); j++) {
				grid[i][j] = new Cell(i, j);
				if (state.getMap().getTerrain(i, j).terrainCost <= 10) {
					grid[i][j].f = distMap[i][j];
				} else {
					closed[i][j] = true;
				}
			}
		}
		open.add(grid[x][y]);
		closed[x][y] = true;
		while (!open.isEmpty()) {
			Cell current = open.remove(0);
			if (current.i - 1 >= 0 && !closed[current.i - 1][current.j]) {
				distMap = helper(open, distMap, grid, current, current.i - 1, current.j);
				closed[current.i - 1][current.j] = true;
			}
			if (current.j - 1 >= 0 && !closed[current.i][current.j - 1]) {
				distMap = helper(open, distMap, grid, current, current.i, current.j - 1);
				closed[current.i][current.j - 1] = true;
			}
			if (current.j + 1 < grid[0].length && !closed[current.i][current.j + 1]) {
				distMap = helper(open, distMap, grid, current, current.i, current.j + 1);
				closed[current.i][current.j + 1] = true;
			}
			if (current.i + 1 < grid.length && !closed[current.i + 1][current.j]) {
				distMap = helper(open, distMap, grid, current, current.i + 1, current.j);
				closed[current.i + 1][current.j] = true;
			}
		}
		return distMap;
	}

	private int[][] helper(ArrayList<Cell> open, int[][] distMap, Cell[][] grid, Cell current, int x, int y) {
		Cell t = grid[x][y];
		t.parent = current;
		t.f = current.f + 1;
		distMap[t.i][t.j] = t.f;
		open.add(t);

		return distMap;
	}

	/**
	 * uses rangemanager
	 * 
	 * @param t
	 * @return
	 */
	public int[][] getThreatMap(GameState state, Team t) {
		int[][] threatMap = new int[state.getWidth()][state.getHeight()];

		ArrayList<Entity> enemies = state.getEntities().getVisibleUnits(t, true, false);
		for (Entity e : enemies) {
			state.getRangeManager().clearRange();
			state.getRangeManager().setMoveAndAttackRange(e);
			for (int i = 0; i < state.getWidth(); i++) {
				for (int j = 0; j < state.getHeight(); j++) {
					if (state.getRangeManager().isEmpty(i, j))
						threatMap[i][j]++;
				}
			}
		}

		return threatMap;
	}

	public EntityUtil getBestLocation(GameState state, Entity selected, Skill skill, int[] range, int[][] threatMap, int[][] distMap) {
		EntityUtil[][] utilityMap = new EntityUtil[state.getWidth()][state.getHeight()];
		EntityUtil util = floodfillUtility(state, utilityMap, selected.getEntityClass().getMoveDistance(), selected.getXCoord(), selected.getYCoord(), skill, selected, range, threatMap, distMap,
				new EntityUtil(null, Integer.MIN_VALUE, -1, -1));

		/*
		 * System.out.println(selected + " " + skill); for (int i = 0; i <
		 * state.getWidth(); i++) { for (int j = 0; j < state.getHeight(); j++) {
		 * System.out.print((utilityMap[i][j] != null ? utilityMap[i][j] : "") + "\t");
		 * } System.out.println(); } System.out.println();
		 */
		return util;
	}

	private EntityUtil floodfillUtility(GameState state, EntityUtil[][] utilityMap, int dist, int x, int y, Skill skill, Entity selected, int[] range, int[][] threatMap, int[][] distMap,
			EntityUtil currentBest) {
		EntityUtil util;
		if (x < 0 || y < 0 || x >= state.getWidth() || y >= state.getHeight() || utilityMap[x][y] != null) {
			// System.out.println("out of bounds");
			return currentBest;
		}
		if (dist < 0 || (state.getEntities().get(x, y) != null && state.getEntities().get(x, y).getTeam() != selected.getTeam())) {
			return currentBest;
		}
		util = setDistanceRangeAtUtility(state, x, y, range, skill, selected, threatMap, distMap, utilityMap);
		if (currentBest.util > util.util)
			util = currentBest;
		// System.out.println(terrainCost);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x + 1, y, selected), x + 1, y, skill, selected, range, threatMap, distMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x - 1, y, selected), x - 1, y, skill, selected, range, threatMap, distMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x, y + 1, selected), x, y + 1, skill, selected, range, threatMap, distMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x, y - 1, selected), x, y - 1, skill, selected, range, threatMap, distMap, util);

		return util;
	}

	private EntityUtil setDistanceRangeAtUtility(GameState state, int x, int y, int[] ranges, Skill skill, Entity selected, int[][] threatMap, int[][] distMap, EntityUtil[][] utils) {
		if (state.getEntities().get(x, y) != null && state.getEntities().get(x, y) != selected) {
			utils[x][y] = new EntityUtil(null, -1000, x, y);
			return utils[x][y];
		}
		int optimizer = 0;
		Entity eo = null;

		for (int r : ranges) {
			for (int i = 0; i < r; i++) {
				int j = r - i;
				Entity[] es = { state.getEntities().get(x + i, y + j), state.getEntities().get(x - j, y + i), state.getEntities().get(x + j, y - i), state.getEntities().get(x - i, y - j) };
				for (Entity e : es) {
					if (e != null) {
						int util = skill.utilityFunc(selected, e);
						if (util > optimizer) {
							optimizer = util;
							eo = e;
						}
					}
				}
			}
		}
		Entity b = state.getEntities().getBuilding(x, y);
		int building = 0;
		if (b != null) {
			if (b.getEntityClass().equals(EntityClassManager.get().fort)) {
				building += 1;
			} else {
				building += 2;
			}
		}
		int utility = (optimizer * 100) - (threatMap[x][y]) - distMap[x][y] + building;
		utils[x][y] = new EntityUtil(eo, utility, x, y);
		return utils[x][y];
	}

	public class EntityUtil {
		public Entity e;
		public int util = 0;
		public int x, y;

		public EntityUtil(Entity e, int util, int x, int y) {
			this.e = e;
			this.util = util;
			this.x = x;
			this.y = y;
		}

		public String toString() {
			return e + " " + util + " (" + x + ", " + y + ")";
		}
	}

}
