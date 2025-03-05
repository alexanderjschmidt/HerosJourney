package heros.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.entities.Entity;
import heros.journey.utils.art.ResourceManager;

public class EntityUI extends UI {

	public static final int HEALTH_WIDTH = HUD.FONT_SIZE * 8;
	public static final int HEALTH_HEIGHT = HUD.FONT_SIZE;

	private Entity entity;

	public EntityUI() {
        super(0, 3, 8, 4);
	}

    @Override
	public void update() {
		entity = HUD.get().getCursor().getHover();
	}

	@Override
	public void drawUI(Batch batch, float parentAlpha) {
        drawText(batch, entity == null ? "---" : entity.toString(), 0, 0);
		if (entity != null) {
			String health = entity.getHealth() + "/" + ((int) (entity.getEntityClass().getMaxHealth()));
			String mana = entity.getMana() + "/" + ((int) Entity.MAX_MANA);
			String exp = entity.getExp() + "/" + ((int) Entity.RANKUP_EXPERIENCE);
			// replace with labels

			batch.draw(ResourceManager.get().ui[0][3], getX() + HUD.FONT_SIZE, 12 + getY() + ((2) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getHealth() / entity.getEntityClass().getMaxHealth()),
					HEALTH_HEIGHT);
			batch.draw(ResourceManager.get().ui[1][2], getX() + HUD.FONT_SIZE, 12 + getY() + ((1) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getMana() / Entity.MAX_MANA), HEALTH_HEIGHT);
			batch.draw(ResourceManager.get().ui[1][3], getX() + HUD.FONT_SIZE, 12 + getY() + ((0) * HUD.FONT_SIZE), HEALTH_WIDTH * (entity.getExp() / Entity.RANKUP_EXPERIENCE), HEALTH_HEIGHT);

            drawText(batch, "Health: " + health, 0, 1);
            drawText(batch, "Mana: " + mana, 0, 2);
            drawText(batch, "Exp: " + exp, 0, 3);
		}
	}

}
