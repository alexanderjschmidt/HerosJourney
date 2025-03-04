package heros.journey.entities.ai;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.entities.Team;
import heros.journey.entities.actions.Action;
import heros.journey.initializers.BaseActions;
import heros.journey.entities.actions.QueuedAction;
import heros.journey.entities.actions.TargetAction;
import heros.journey.utils.RangeManager.RangeColor;
import heros.journey.utils.pathfinding.AStar;
import heros.journey.utils.pathfinding.Cell;

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

	public QueuedAction update(GameState gameState, float delta) {
		QueuedAction action = updateUnits(gameState);
		return action;
	}

	private QueuedAction updateUnits(GameState gameState) {
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
			for (QueuedAction action : this.getAllPossibleActions(state, threatMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getAction() == BaseActions.end_turn ? 0 : depth - 1, alpha, beta);
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
			for (QueuedAction action : this.getAllPossibleActions(state, threatMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getAction() == BaseActions.end_turn ? 0 : depth - 1, alpha, beta);
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

	private ArrayList<QueuedAction> getAllPossibleActions(GameState state, int[][] threatMap) {
		ArrayList<Entity> avalibleUnits = state.getEntities().getVisibleUnits(state.getActiveTeam(), false, true);
		ArrayList<QueuedAction> possibleActions = new ArrayList<QueuedAction>(avalibleUnits.size());
		for (Entity e : avalibleUnits) {
			for (Action action : e.getEntityClass().getActions()) {
				if (action != BaseActions.wait) {
					possibleActions.add(getActions(state, action, e, threatMap));
				}
			}
		}
		if (avalibleUnits.size() == 0) {
			possibleActions.add(new QueuedAction(new Cell(0, 0, 1), BaseActions.end_turn, 0, 0));
		}
		return possibleActions;
	}

	private QueuedAction getActions(GameState state, Action action, Entity e, int[][] threatMap) {
		QueuedAction queuedAction;
		if (action instanceof TargetAction) {
			TargetAction targetAction = (TargetAction) action;
			AIEntityDecisionMaker.EntityUtil util = decisionMaker.getBestLocation(state, e, targetAction,
					targetAction.equals(BaseActions.attack) ? e.getEntityClass().getRanges() : targetAction.getRanges(), threatMap);
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
			if (util.e != null) {
				queuedAction = new QueuedAction(path, targetAction, util.e.getXCoord(), util.e.getYCoord());
			} else {
				queuedAction = new QueuedAction(path, BaseActions.wait, 0, 0);
			}

			state.getRangeManager().clearRange();
		} else {
			queuedAction = new QueuedAction(new Cell(e.getXCoord(), e.getYCoord(), 1), action, 0, 0);
		}
		return queuedAction;
	}

	private class ActionValue {
		public QueuedAction action;
		public int actionValue;

		public ActionValue(QueuedAction action, int actionValue) {
			this.action = action;
			this.actionValue = actionValue;
		}
	}
}
