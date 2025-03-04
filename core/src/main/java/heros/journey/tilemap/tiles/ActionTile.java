package heros.journey.tilemap.tiles;

import java.util.Arrays;
import java.util.List;

import heros.journey.entities.actions.Action;
import heros.journey.tilemap.TileManager;

public abstract class ActionTile implements TileInterface {

	private String name;
	private List<Action> actions;
	private int terrainCost;

	public ActionTile(String name, int terrainCost, Action... actions) {
		this.name = name;
		this.terrainCost = terrainCost;
		this.actions = Arrays.asList(actions);
        TileManager.put(name, this, null);
	}

	public String toString() {
		return name;
	}

	public int getTerrainCost() {
		return terrainCost;
	}

	public List<Action> getActions() {
		return actions;
	}

}
