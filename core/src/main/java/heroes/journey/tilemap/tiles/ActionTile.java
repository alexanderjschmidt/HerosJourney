package heroes.journey.tilemap.tiles;

import heroes.journey.entities.actions.Action;

import java.util.Arrays;
import java.util.List;

public abstract class ActionTile extends Tile {

	private final List<Action> actions;

	public ActionTile(String name, int terrainCost, Action... actions) {
		super(name, terrainCost);
		this.actions = Arrays.asList(actions);
	}

	public List<Action> getActions() {
		return actions;
	}

}
