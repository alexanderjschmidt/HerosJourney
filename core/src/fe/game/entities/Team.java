package fe.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import fe.game.GameState;
import fe.game.entities.ai.AI;
import fe.game.managers.RangeManager.RangeColor;
import fe.game.utils.Direction;

public class Team {

	public static final int FORT_CAP = 5;
	public static final int TROOP_CAP = 200;

	private String teamName;
	private String id;
	private int arrayID;
	private Color color;
	private GameState gameState;
	private RangeColor rangeColor;
	private boolean hasAI;
	private AI ai;
	public int morale;
	public int currentTroops;
	public int forts;
	public int castles;
	private Entity castle;

	private Direction territory;

	public static final Color[] colors = new Color[] { Color.BLUE, Color.RED, Color.GREEN, Color.PURPLE };
	public static final String[] names = new String[] { "Blue", "Red", "Green", "Purple" };
	public static final RangeColor[] ranges = new RangeColor[] { RangeColor.BLUE, RangeColor.RED, RangeColor.GREEN, RangeColor.PURPLE };

	public Team(String id, int colorID, GameState gameState, boolean hasAI, Direction territory) {
		this.id = id;
		this.arrayID = colorID;
		this.teamName = names[colorID] + " Team";
		this.gameState = gameState;
		this.hasAI = hasAI;
		this.territory = territory;
		Color color = colors[colorID];
		this.color = new Color((color.r + 1) / 2, (color.g + 1) / 2, (color.b + 1) / 2, color.a);
		rangeColor = ranges[colorID];
		morale = 75;
		forts = 0;
		currentTroops = 0;
	}

	public Team clone(GameState newGameState) {
		Team clone = new Team(id, arrayID, newGameState, hasAI, territory);
		clone.ai = ai;
		clone.morale = morale;
		clone.forts = forts;
		clone.currentTroops = currentTroops;
		return clone;
	}

	public String getID() {
		return id;
	}

	public int getArrayID() {
		return arrayID;
	}

	public void setArrayID(int arrayID) {
		this.arrayID = arrayID;
	}

	public Color getColor() {
		return color;
	}

	public String toString() {
		return teamName;
	}

	public void adjustMorale(int i) {
		morale = Math.max(0, Math.min(100, morale + i));
		if (morale == 0) {
			gameState.removeTeam(this);
		}
	}

	public boolean inTerritory(int x, int y) {
		if (x < 0 || y < 0 || x >= gameState.getWidth() || y >= gameState.getHeight()) {
			return false;
		}
		if (territory == Direction.SOUTHWEST && x < gameState.getWidth() / 2 && y < gameState.getHeight() / 2) {
			return true;
		} else if (territory == Direction.SOUTHEAST && x >= gameState.getWidth() / 2 && y < gameState.getHeight() / 2) {
			return true;
		} else if (territory == Direction.NORTHWEST && x < gameState.getWidth() / 2 && y >= gameState.getHeight() / 2) {
			return true;
		} else if (territory == Direction.NORTHEAST && x >= gameState.getWidth() / 2 && y >= gameState.getHeight() / 2) {
			return true;
		}
		return false;
	}

	public RangeColor getRangeColor() {
		return rangeColor;
	}

	public boolean isAI() {
		return hasAI;
	}

	public Direction getTerritory() {
		return territory;
	}

	public Entity getCastle() {
		return castle;
	}

	public void setCastle(Entity castle) {
		this.castle = castle;
	}

	public Vector2 getCorner() {
		if (territory == Direction.SOUTHWEST) {
			return new Vector2(0, 0);
		} else if (territory == Direction.SOUTHEAST) {
			return new Vector2(gameState.getWidth() - 1, 0);
		} else if (territory == Direction.NORTHWEST) {
			return new Vector2(0, gameState.getHeight() - 1);
		} else if (territory == Direction.NORTHEAST) {
			return new Vector2(gameState.getWidth() - 1, gameState.getHeight() - 1);
		}
		return new Vector2(0, 0);
	}

	public AI getAI() {
		return ai;
	}

	public void setAI(AI ai) {
		this.ai = ai;
	}

}
