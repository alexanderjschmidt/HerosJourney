package fe.game.utils;

import fe.game.entities.skills.Skill;

public class GameAction {

	private Cell path;
	private Skill skill;
	private int targetX, targetY;
	public String information = "";

	public GameAction(Cell path, Skill skill, int targetX, int targetY) {
		this.path = path;
		this.skill = skill;
		this.targetX = targetX;
		this.targetY = targetY;
	}

	public Cell getPath() {
		return path;
	}

	public Skill getSkill() {
		return skill;
	}

	public int getTargetX() {
		return targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public String toString() {
		return skill + " " + targetX + ", " + targetY;
	}

}
