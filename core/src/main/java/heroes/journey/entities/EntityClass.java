package heroes.journey.entities;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heroes.journey.GameCamera;
import heroes.journey.entities.actions.Action;
import heroes.journey.initializers.base.Actions;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;

public class EntityClass {

	private ArrayList<Action> actions;

	private String name;
	private TextureRegion sprite;
	private String description;

	public EntityClass(String name, String description, TextureRegion sprite,  Action... actions) {
		this.name = name;
		this.description = description;
		this.sprite = sprite;
		this.actions = new ArrayList<Action>();
		this.actions.add(Actions.wait);
        this.actions.addAll(Arrays.asList(actions));
		EntityClassManager.get().put(name, this);
	}

	public void render(Batch batch, float x, float y, float elapsedTime) {
		batch.draw(sprite, x, y, GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public void render(Batch batch, float x, float y, float width, float height, float elapsedTime) {
		batch.draw(sprite, x, y, width, height);
	}

	public String toString() {
		return name;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public int getTerrainCost(Tile tile, ActionTile actionTile) {
		return tile.getTerrainCost() + (actionTile == null ? 0 : actionTile.getTerrainCost());
	}

	public String getDescription() {
		return description;
	}
}
