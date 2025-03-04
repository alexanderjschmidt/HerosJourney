package heros.journey.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import heros.journey.GameCamera;
import heros.journey.entities.EntityClass;
import heros.journey.managers.ResourceManager;

public class JobInfoUI extends Actor {

	private EntityClass job;

	private boolean toggled;

	public JobInfoUI() {
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 12);
		this.setPosition(Gdx.graphics.getWidth() - (HUD.FONT_SIZE * 11), HUD.FONT_SIZE);
		this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
		toggled = true;
		toggle();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());

		if (job != null) {
			job.render(batch, getX() + 12, getY() + 24 + HUD.FONT_SIZE * 9, GameCamera.get().getSize() * 2, GameCamera.get().getSize() * 2, 0);
			ResourceManager.get().font24.draw(batch, job.toString(), getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 12);
			ResourceManager.get().font24.draw(batch, "Cost: " + job.getValue(), getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 11);
			ResourceManager.get().font24.draw(batch, "Damage: " + job.getBaseDamage(), getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 10);
			ResourceManager.get().font24.draw(batch, "Health: " + ((int) job.getMaxHealth()), getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 9);
			ResourceManager.get().font24.draw(batch, "Move: " + job.getMoveDistance(), getX() + 12, getY() + 12 + HUD.FONT_SIZE * 9);
			ResourceManager.get().font24.draw(batch, job.getDescription(), getX() + 12, getY() + 12 + HUD.FONT_SIZE * 8, HUD.FONT_SIZE * 9, Align.left, true);
		} else {
			ResourceManager.get().font24.draw(batch, "---", getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 12);
			ResourceManager.get().font24.draw(batch, "Cost: ---", getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 11);
			ResourceManager.get().font24.draw(batch, "Damage: ---", getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 10);
			ResourceManager.get().font24.draw(batch, "Health: ---", getX() + 12 + HUD.FONT_SIZE * 4, getY() + 12 + HUD.FONT_SIZE * 9);
			ResourceManager.get().font24.draw(batch, "Move: ---", getX() + 12, getY() + 12 + HUD.FONT_SIZE * 9);
		}

		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
	}

	public void update(EntityClass job) {
		this.job = job;
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 12);
		this.setPosition(Gdx.graphics.getWidth() - (HUD.FONT_SIZE * 11), HUD.FONT_SIZE);
	}

	public void toggle() {
		toggled = !toggled;
		if (!toggled) {
			this.setVisible(false);
		}
	}

	public boolean toggled() {
		return toggled;
	}

}
