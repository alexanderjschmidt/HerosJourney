package heros.journey.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import heros.journey.GameState;
import heros.journey.managers.ResourceManager;
import heros.journey.tilemap.tiles.Tile;

public class TerrainUI extends Actor {

	private Tile tile;
	private boolean trees;

	public TerrainUI() {
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE);
		this.setPosition(HUD.FONT_SIZE, HUD.FONT_SIZE);
		this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
	}

	public void update(int x, int y) {
		tile = GameState.global().getMap().getTerrain(x, y);
		trees = GameState.global().getMap().hasTree(x, y);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {

		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());
		String name = "---";
		int cost = 0;
		if (tile != null) {
			name = tile.toString();
			if (trees) {
				name = name + " and Trees";
			}
			ResourceManager.get().font24.draw(batch, name, getX() + HUD.FONT_SIZE, 12 + getY() + (1 * HUD.FONT_SIZE));
		}
		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);

	}

}
