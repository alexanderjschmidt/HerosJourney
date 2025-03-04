package heros.journey.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import heros.journey.utils.art.ResourceManager;

public class CombatUI extends Actor {

	private String attackInfo = "";

	public CombatUI() {
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE);
		this.setPosition(HUD.FONT_SIZE, 24 + HUD.FONT_SIZE * 15);
		this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());
		ResourceManager.get().font24.draw(batch, attackInfo, getX() + HUD.FONT_SIZE, 12 + getY() + ((0 + 1) * HUD.FONT_SIZE));
		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
	}

	public void setMessage(String string) {
		attackInfo = string;
	}

}
