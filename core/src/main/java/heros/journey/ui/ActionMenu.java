package heros.journey.ui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.entities.actions.Action;
import heros.journey.utils.art.ResourceManager;

public class ActionMenu extends Actor {

	private List<Action> options;
	private int selected = 0;
	private HUD hud;
	private Cursor cursor;

	public ActionMenu(HUD hud) {
		this.hud = hud;
		this.cursor = hud.getCursor();
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 1);
	}

	public void open(List<Action> options) {
		GameState.global().getRangeManager().clearRange();
		hud.setState(HUD.HUDState.ACTION_SELECT);
		if (options.size() == 0) {
			cursor.clearSelected(false);
			hud.setState(HUD.HUDState.CURSOR_MOVE);
			return;
		}
		this.options = options;
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * options.size());
		selected = options.size() - 1;
		options.get(selected).onHover(GameState.global(), cursor.getSelected());
		this.setPosition(Gdx.graphics.getWidth() - (HUD.FONT_SIZE * 11), Gdx.graphics.getHeight() - (24 + HUD.FONT_SIZE * (options.size() + 1)));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());
		for (int i = 0; i < options.size(); i++) {
			ResourceManager.get().font24.draw(batch, options.get(i).toString(), getX() + HUD.FONT_SIZE + 24, 12 + getY() + ((i + 1) * HUD.FONT_SIZE));
			if (i == selected) {
				batch.draw(ResourceManager.get().select, getX() + 10, getY() - 16 + ((i + 1) * HUD.FONT_SIZE));
			}
		}
	}

	public void increment() {
		if (selected < options.size() - 1) {
			selected++;
			options.get(selected).onHover(GameState.global(), cursor.getSelected());
		} else {
			selected = 0;
		}
	}

	public void decrement() {
		if (selected > 0) {
			selected--;
			options.get(selected).onHover(GameState.global(), cursor.getSelected());
		} else {
			selected = options.size() - 1;
		}
	}

	public void select() {
		options.get(selected).onSelect(GameState.global(), cursor.getSelected() == null ? new Entity(cursor.x, cursor.y) : cursor.getSelected());
	}

	public Action getSelected() {
		return options.get(selected);
	}

}
