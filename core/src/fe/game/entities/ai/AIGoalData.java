package fe.game.entities.ai;

import java.util.ArrayList;

import fe.game.entities.Entity;

public class AIGoalData {

	public Entity entity;
	public ArrayList<Entity> goal;
	public ArrayList<Integer> goalDist;

	public AIGoalData(Entity entity) {
		this.entity = entity;
		goal = new ArrayList<Entity>();
		goalDist = new ArrayList<Integer>();
	}

	public void add(Entity e, int i) {
		if (goal.size() == 0) {
			goal.add(0, e);
			goalDist.add(0, i);
			return;
		}
		int index = binarySearch(i, 0, goalDist.size() - 1);
		goal.add(index, e);
		goalDist.add(index, i);
	}

	private int binarySearch(int item, int low, int high) {
		if (high <= low)
			return (item > goalDist.get(low)) ? (low + 1) : low;

		int mid = (low + high) / 2;

		if (item == goalDist.get(mid))
			return mid + 1;

		if (item > goalDist.get(mid))
			return binarySearch(item, mid + 1, high);
		return binarySearch(item, low, mid - 1);
	}

	public String toString() {
		String output = entity + ": ";
		for (int i = 0; i < goal.size(); i++) {
			output += "(" + goal.get(i) + ", " + goalDist.get(i) + ") ";
		}
		return output;
	}

}
