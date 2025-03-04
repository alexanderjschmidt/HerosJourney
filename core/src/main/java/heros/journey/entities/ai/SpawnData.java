package heros.journey.entities.ai;

import heros.journey.entities.actions.Action;

public class SpawnData {

	private float angle;
	private int distance;
	private Action action;

	public SpawnData(float angle, int distance, Action action) {
		this.angle = angle;
		this.distance = distance;
		this.action = action;
	}

	public float getAngle() {
		return angle;
	}

	public int getDistance() {
		return distance;
	}

	public Action getAction() {
		return action;
	}

}
