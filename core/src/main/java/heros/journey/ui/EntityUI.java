package heros.journey.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import heros.journey.entities.Entity;
import heros.journey.managers.ResourceManager;

public class EntityUI extends Actor {

	public static final int HEALTH_WIDTH = HUD.FONT_SIZE * 8;
	public static final int HEALTH_HEIGHT = HUD.FONT_SIZE;

	private Entity entity;

	public EntityUI() {
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 4);
		this.setPosition(HUD.FONT_SIZE, 24 + HUD.FONT_SIZE * 3);
		this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
	}

	public void update(Cursor cursor) {
		entity = cursor.getHover();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());
		ResourceManager.get().font24.draw(batch, entity == null ? "---" : entity.toString(), getX() + HUD.FONT_SIZE, 12 + getY() + ((3 + 1) * HUD.FONT_SIZE));
		if (entity != null) {
			String health = entity.getHealth() + "/" + ((int) (entity.getEntityClass().getMaxHealth()));
			String mana = entity.getMana() + "/" + ((int) Entity.MAX_MANA);
			String exp = entity.getExp() + "/" + ((int) Entity.RANKUP_EXPERIENCE);
			// replace with labels

			batch.draw(ResourceManager.get().ui[0][3], getX() + HUD.FONT_SIZE, 12 + getY() + ((2) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getHealth() / entity.getEntityClass().getMaxHealth()),
					HEALTH_HEIGHT);
			batch.draw(ResourceManager.get().ui[1][2], getX() + HUD.FONT_SIZE, 12 + getY() + ((1) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getMana() / Entity.MAX_MANA), HEALTH_HEIGHT);
			batch.draw(ResourceManager.get().ui[1][3], getX() + HUD.FONT_SIZE, 12 + getY() + ((0) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getExp() / Entity.RANKUP_EXPERIENCE), HEALTH_HEIGHT);

			ResourceManager.get().font24.draw(batch, "Health: " + health, getX() + HUD.FONT_SIZE, 12 + getY() + ((2 + 1) * HUD.FONT_SIZE));
			ResourceManager.get().font24.draw(batch, "Mana: " + mana, getX() + HUD.FONT_SIZE, 12 + getY() + ((1 + 1) * HUD.FONT_SIZE));
			ResourceManager.get().font24.draw(batch, "Exp: " + exp, getX() + HUD.FONT_SIZE, 12 + getY() + ((0 + 1) * HUD.FONT_SIZE));
		}
		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
	}

}
