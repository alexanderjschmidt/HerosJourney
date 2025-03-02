package fe.game.entities.ai;

import fe.game.entities.skills.Skill;

public class SpawnData {

	private float angle;
	private int distance;
	private Skill skill;

	public SpawnData(float angle, int distance, Skill skill) {
		this.angle = angle;
		this.distance = distance;
		this.skill = skill;
	}

	public float getAngle() {
		return angle;
	}

	public int getDistance() {
		return distance;
	}

	public Skill getSkill() {
		return skill;
	}

}
