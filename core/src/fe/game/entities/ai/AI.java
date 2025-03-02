package fe.game.entities.ai;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import fe.game.GameState;
import fe.game.entities.Building;
import fe.game.entities.Entity;
import fe.game.entities.Team;
import fe.game.entities.skills.Skill;
import fe.game.entities.skills.SkillManager;
import fe.game.entities.skills.SkillManagerOld;
import fe.game.entities.skills.TargetSkill;
import fe.game.managers.RangeManager.RangeColor;
import fe.game.tilemap.Tileset;
import fe.game.utils.AStar;
import fe.game.utils.Cell;
import fe.game.utils.Direction;
import fe.game.utils.GameAction;

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

		spawnPlan.add(new SpawnData(0, 9, SkillManager.getSkill(SkillManagerOld.SPAWN_SOLIDER)));
		spawnPlan.add(new SpawnData(0, 9, SkillManager.getSkill(SkillManagerOld.SPAWN_SOLIDER)));
		spawnPlan.add(new SpawnData(0, 9, SkillManager.getSkill(SkillManagerOld.SPAWN_ARCHER)));
	}

	public GameAction update(GameState gameState, float delta) {
		GameAction action = null;
		if (gameState.getTurn() == 0) {
			action = updateFirstTurn(gameState, delta);
		} else {
			action = updateUnits(gameState);
		}
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
		int[][] distMap = decisionMaker.getCastleDistMap(state, state.getActiveTeam());
		if (state.getActiveTeam().getArrayID() == maxTeam.getArrayID()) {
			ActionValue maxVal = new ActionValue(null, Integer.MIN_VALUE);
			for (GameAction action : this.getAllPossibleActions(state, threatMap, distMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getSkill() == SkillManager.getSkill(SkillManager.END_TURN) ? 0 : depth - 1, alpha, beta);
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
			for (GameAction action : this.getAllPossibleActions(state, threatMap, distMap)) {
				ActionValue av = minimax(state.applyAction(action), maxTeam, action.getSkill() == SkillManager.getSkill(SkillManager.END_TURN) ? 0 : depth - 1, alpha, beta);
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

	private ArrayList<GameAction> getAllPossibleActions(GameState state, int[][] threatMap, int[][] distMap) {
		ArrayList<Entity> avalibleUnits = state.getEntities().getVisibleUnits(state.getActiveTeam(), false, true);
		ArrayList<GameAction> possibleActions = new ArrayList<GameAction>(avalibleUnits.size());
		for (Entity e : avalibleUnits) {
			for (Skill skill : e.getEntityClass().getSkills()) {
				if (skill != SkillManager.getSkill(SkillManager.WAIT)) {
					possibleActions.add(getActions(state, skill, e, threatMap, distMap));
				}
			}
		}
		if (avalibleUnits.size() == 0) {
			possibleActions.add(new GameAction(new Cell(0, 0), SkillManager.getSkill(SkillManager.END_TURN), 0, 0));
		}
		return possibleActions;
	}

	private GameAction getActions(GameState state, Skill skill, Entity e, int[][] threatMap, int[][] distMap) {
		GameAction action;
		if (skill instanceof TargetSkill) {
			TargetSkill targetSkill = (TargetSkill) skill;
			AIEntityDecisionMaker.EntityUtil util = decisionMaker.getBestLocation(state, e, targetSkill,
					targetSkill.equals(SkillManager.getSkill(SkillManager.ATTACK)) ? e.getEntityClass().getRanges() : targetSkill.getRanges(), threatMap, distMap);
			int tx = util.x;
			int ty = util.y;
			state.getRangeManager().setMoveAndAttackRange(e);
			RangeColor[][] range = state.getRangeManager().getRange();
			Cell path;
			// not moving and then not attacking even though target is known
			// fix this shit
			path = AStar.aStarEntity(e.getEntityClass().getMoveDistance(), range, e.getXCoord(), e.getYCoord(), tx, ty, state.getMap(), e);
			if (path == null) {
				System.out.println(skill);
				System.out.println(e.getXCoord() + ", " + e.getYCoord());
				System.out.println(tx + ", " + ty);
			}

			path = AStar.reversePath(path);
			// AStar.printPath(path);
			if (util.e != null && !util.e.getClass().equals(Building.class)) {
				action = new GameAction(path, targetSkill, util.e.getXCoord(), util.e.getYCoord());
			} else {
				action = new GameAction(path, SkillManager.getSkill(SkillManager.WAIT), 0, 0);
			}

			state.getRangeManager().clearRange();
		} else {
			action = new GameAction(new Cell(e.getXCoord(), e.getYCoord()), skill, 0, 0);
		}
		return action;
	}

	private class ActionValue {
		public GameAction action;
		public int actionValue;

		public ActionValue(GameAction action, int actionValue) {
			this.action = action;
			this.actionValue = actionValue;
		}
	}

	private GameAction updateFirstTurn(GameState gameState, float delta) {
		if (baseVector == null) {
			return spawnCastle(gameState);
		} else if (spawnPlan.size() > 0) {
			SpawnData d = spawnPlan.remove(0);
			Vector2 v = new Vector2(1, 0);
			v.setLength(d.getDistance()).setAngle(baseVector.angle() + d.getAngle());
			return spawnAt(gameState, castleX + (int) v.x, castleY + (int) v.y, d.getSkill());
		} else {
			return new GameAction(new Cell(0, 0), SkillManager.getSkill(SkillManager.END_TURN), 0, 0);
		}
	}

	private GameAction spawnCastle(GameState gameState) {
		Team t = gameState.getActiveTeam();
		castleX = (int) (t.getCorner().x + (t.getTerritory() == Direction.NORTHEAST || t.getTerritory() == Direction.SOUTHEAST ? -targetCastleX : targetCastleX) + (random.nextInt(3) - 1));
		castleY = (int) (t.getCorner().y + (t.getTerritory() == Direction.NORTHEAST || t.getTerritory() == Direction.NORTHWEST ? -targetCastleY : targetCastleY) + (random.nextInt(3) - 1));
		System.out.println(castleX + " " + castleY);
		GameAction g = spawnAt(gameState, castleX, castleY, SkillManager.getSkill(SkillManagerOld.BUILD_CASTLE));
		baseVector = new Vector2(0, 0);
		for (Team team : gameState.getTeams()) {
			if (team != t) {
				if (team.getCastle() != null) {
					baseVector.x += team.getCastle().getXCoord() - g.getTargetX();
					baseVector.y += team.getCastle().getYCoord() - g.getTargetY();
				} else {
					baseVector.x += team.getCorner().x - g.getTargetX();
					baseVector.y += team.getCorner().y - g.getTargetY();
				}
			}
		}
		castleX = g.getTargetX();
		castleY = g.getTargetY();
		return g;
	}

	private GameAction spawnAt(GameState gameState, int x, int y, Skill skill) {
		int i = x;
		int j = y;
		// spiral code
		int di = 1;
		int dj = 0;
		int segment_length = 1;
		int segment_passed = 0;
		for (int k = 0; k < (gameState.getWidth() * gameState.getHeight()); ++k) {
			if ((gameState.getMap().get(i, j) == Tileset.PLAINS || gameState.getMap().get(i, j) == Tileset.HILLS) && gameState.getActiveTeam().inTerritory(i, j)
					&& gameState.getEntities().getFog(i, j) == null && gameState.getEntities().getBuilding(i, j) == null) {
				GameAction g = new GameAction(new Cell(i, j), skill, i, j);
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
