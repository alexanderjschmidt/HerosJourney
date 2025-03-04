package heros.journey.entities.ai;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import heros.journey.GameState;
import heros.journey.entities.Building;
import heros.journey.entities.Entity;
import heros.journey.entities.Team;
import heros.journey.entities.actions.Action;
import heros.journey.entities.actions.ActionManager;
import heros.journey.entities.actions.TargetAction;
import heros.journey.managers.RangeManager.RangeColor;
import heros.journey.tilemap.Tileset;
import heros.journey.utils.AStar;
import heros.journey.utils.Cell;
import heros.journey.utils.GameAction;

public class AI {

	private Random random;
	private AIEntityDecisionMaker decisionMaker;

	private ArrayList<SpawnData> spawnPlan;
	private int castleX, castleY;
	private Vector2 baseVector;
	// from my corner
	private int targetCastleX, targetCastleY;

	public AI() {
		random = new Random();
		decisionMaker = new AIEntityDecisionMaker();
		spawnPlan = new ArrayList<SpawnData>();
		castleX = -1;
		castleY = -1;

		targetCastleX = 3;
		targetCastleY = 3;
	}

	public GameAction update(GameState gameState, float delta) {
		GameAction action = updateUnits(gameState);
		return action;
	}

	private GameAction updateUnits(GameState gameState) {
		ActionValue av = minimax(gameState.clone(), gameState.getActiveTeam(), 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println("Entity: " + gameState.getEntities().getFog(av.action.getPath().i, av.action.getPath().j));
		System.out.println("Entity Target: " + gameState.getEntities().getFog(av.action.getTargetX(), av.action.getTargetY()));
		return av.action;
	}

	private ActionValue minimax(GameState state, Team maxTeam, int depth, int alpha, int beta) {
		// System.out.println("Depth " + depth);
		if (depth == 0) { // or terminal node
			return new ActionValue(null, state.getScore(maxTeam));
		}
		int[][] threatMap = decisionMaker.getThreatMap(state, state.getActiveTeam());
		if (state.getActiveTeam().getArrayID() == maxTeam.getArrayID()) {
			ActionValue maxVal = new ActionValue(null, Integer.MIN_VALUE);
			for (GameAction action : this.getAllPossibleActions(state, threatMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getAction() == ActionManager.getAction(ActionManager.END_TURN) ? 0 : depth - 1, alpha, beta);
				av.action = action;
				if (maxVal.actionValue < av.actionValue) {
					maxVal = av;
				}
				if (alpha < maxVal.actionValue) {
					alpha = maxVal.actionValue;
				}
				if (alpha >= beta) {
					break;
				}
			}
			return maxVal;
		} else {
			ActionValue minVal = new ActionValue(null, Integer.MAX_VALUE);
			for (GameAction action : this.getAllPossibleActions(state, threatMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getAction() == ActionManager.getAction(ActionManager.END_TURN) ? 0 : depth - 1, alpha, beta);
				av.action = action;
				if (minVal.actionValue > av.actionValue) {
					minVal = av;
				}
				if (beta > minVal.actionValue) {
					beta = minVal.actionValue;
				}
				if (alpha >= beta) {
					break;
				}
			}
			return minVal;
		}
	}

	private ArrayList<GameAction> getAllPossibleActions(GameState state, int[][] threatMap) {
		ArrayList<Entity> avalibleUnits = state.getEntities().getVisibleUnits(state.getActiveTeam(), false, true);
		ArrayList<GameAction> possibleActions = new ArrayList<GameAction>(avalibleUnits.size());
		for (Entity e : avalibleUnits) {
			for (Action action : e.getEntityClass().getActions()) {
				if (action != ActionManager.getAction(ActionManager.WAIT)) {
					possibleActions.add(getActions(state, action, e, threatMap));
				}
			}
		}
		if (avalibleUnits.size() == 0) {
			possibleActions.add(new GameAction(new Cell(0, 0), ActionManager.getAction(ActionManager.END_TURN), 0, 0));
		}
		return possibleActions;
	}

	private GameAction getActions(GameState state, Action action, Entity e, int[][] threatMap) {
		GameAction gameAction;
		if (action instanceof TargetAction) {
			TargetAction targetAction = (TargetAction) action;
			AIEntityDecisionMaker.EntityUtil util = decisionMaker.getBestLocation(state, e, targetAction,
					targetAction.equals(ActionManager.getAction(ActionManager.ATTACK)) ? e.getEntityClass().getRanges() : targetAction.getRanges(), threatMap);
			int tx = util.x;
			int ty = util.y;
			state.getRangeManager().setMoveAndAttackRange(e);
			RangeColor[][] range = state.getRangeManager().getRange();
			Cell path;
			// not moving and then not attacking even though target is known
			// fix this shit
			path = AStar.aStarEntity(e.getEntityClass().getMoveDistance(), range, e.getXCoord(), e.getYCoord(), tx, ty, state.getMap(), e);
			if (path == null) {
				System.out.println(action);
				System.out.println(e.getXCoord() + ", " + e.getYCoord());
				System.out.println(tx + ", " + ty);
			}

			path = AStar.reversePath(path);
			// AStar.printPath(path);
			if (util.e != null && !util.e.getClass().equals(Building.class)) {
				gameAction = new GameAction(path, targetAction, util.e.getXCoord(), util.e.getYCoord());
			} else {
				gameAction = new GameAction(path, ActionManager.getAction(ActionManager.WAIT), 0, 0);
			}

			state.getRangeManager().clearRange();
		} else {
			gameAction = new GameAction(new Cell(e.getXCoord(), e.getYCoord()), action, 0, 0);
		}
		return gameAction;
	}

	private class ActionValue {
		public GameAction action;
		public int actionValue;

		public ActionValue(GameAction action, int actionValue) {
			this.action = action;
			this.actionValue = actionValue;
		}
	}

	// TODO Remove?
	private GameAction spawnAt(GameState gameState, int x, int y, Action action) {
		int i = x;
		int j = y;
		// spiral code
		int di = 1;
		int dj = 0;
		int segment_length = 1;
		int segment_passed = 0;
		for (int k = 0; k < (gameState.getWidth() * gameState.getHeight()); ++k) {
			if ((gameState.getMap().get(i, j) == Tileset.PLAINS || gameState.getMap().get(i, j) == Tileset.HILLS) && gameState.getEntities().getFog(i, j) == null) {
				GameAction g = new GameAction(new Cell(i, j), action, i, j);
				return g;
			}
			i += di;
			j += dj;
			++segment_passed;
			if (segment_passed == segment_length) {
				segment_passed = 0;
				int buffer = di;
				di = -dj;
				dj = buffer;
				if (dj == 0) {
					++segment_length;
				}
			}
		}
		return null;
	}
}
