package heros.journey.entities.ai;

import java.util.ArrayList;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.entities.Team;
import heros.journey.entities.actions.Action;

public class AIEntityDecisionMaker {

	public AIEntityDecisionMaker() {
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

	public EntityUtil getBestLocation(GameState state, Entity selected, Action skill, int[] range, int[][] threatMap) {
		EntityUtil[][] utilityMap = new EntityUtil[state.getWidth()][state.getHeight()];
		EntityUtil util = floodfillUtility(state, utilityMap, selected.getMoveDistance(), selected.getXCoord(), selected.getYCoord(), skill, selected, range, threatMap,
				new EntityUtil(null, Integer.MIN_VALUE, -1, -1));

		/*
		 * System.out.println(selected + " " + skill); for (int i = 0; i <
		 * state.getWidth(); i++) { for (int j = 0; j < state.getHeight(); j++) {
		 * System.out.print((utilityMap[i][j] != null ? utilityMap[i][j] : "") + "\t");
		 * } System.out.println(); } System.out.println();
		 */
		return util;
	}

	private EntityUtil floodfillUtility(GameState state, EntityUtil[][] utilityMap, int dist, int x, int y, Action skill, Entity selected, int[] range, int[][] threatMap, EntityUtil currentBest) {
		EntityUtil util;
		if (x < 0 || y < 0 || x >= state.getWidth() || y >= state.getHeight() || utilityMap[x][y] != null) {
			// System.out.println("out of bounds");
			return currentBest;
		}
		if (dist < 0 || (state.getEntities().get(x, y) != null && state.getEntities().get(x, y).getTeam() != selected.getTeam())) {
			return currentBest;
		}
		util = setDistanceRangeAtUtility(state, x, y, range, skill, selected, threatMap, utilityMap);
		if (currentBest.util > util.util)
			util = currentBest;
		// System.out.println(terrainCost);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x + 1, y, selected), x + 1, y, skill, selected, range, threatMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x - 1, y, selected), x - 1, y, skill, selected, range, threatMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x, y + 1, selected), x, y + 1, skill, selected, range, threatMap, util);
		util = floodfillUtility(state, utilityMap, dist - state.getMap().getTerrainCost(x, y - 1, selected), x, y - 1, skill, selected, range, threatMap, util);

		return util;
	}

	private EntityUtil setDistanceRangeAtUtility(GameState state, int x, int y, int[] ranges, Action skill, Entity selected, int[][] threatMap, EntityUtil[][] utils) {
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
		int utility = (optimizer * 100) - (threatMap[x][y]);
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
