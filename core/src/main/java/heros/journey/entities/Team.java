package heros.journey.entities;

import com.badlogic.gdx.graphics.Color;

import heros.journey.GameState;
import heros.journey.entities.ai.AI;
import heros.journey.utils.RangeManager.RangeColor;

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

	public static final Color[] colors = new Color[] { Color.BLUE, Color.RED, Color.GREEN, Color.PURPLE };
	public static final String[] names = new String[] { "Blue", "Red", "Green", "Purple" };
	public static final RangeColor[] ranges = new RangeColor[] { RangeColor.BLUE, RangeColor.RED, RangeColor.GREEN, RangeColor.PURPLE };

	public Team(String id, int colorID, GameState gameState, boolean hasAI) {
		this.id = id;
		this.arrayID = colorID;
		this.teamName = names[colorID] + " Team";
		this.gameState = gameState;
		this.hasAI = hasAI;
		Color color = colors[colorID];
		this.color = new Color((color.r + 1) / 2, (color.g + 1) / 2, (color.b + 1) / 2, color.a);
		rangeColor = ranges[colorID];
	}

	public Team clone(GameState newGameState) {
		Team clone = new Team(id, arrayID, newGameState, hasAI);
		clone.ai = ai;
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

	public RangeColor getRangeColor() {
		return rangeColor;
	}

	public boolean isAI() {
		return hasAI;
	}

	public AI getAI() {
		return ai;
	}

	public void setAI(AI ai) {
		this.ai = ai;
	}

}
