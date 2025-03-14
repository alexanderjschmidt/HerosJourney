package heroes.journey.ui;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;

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
/*        drawText(batch, character == null ? "---" : character.toString(), 0, 0);
        if (character != null) {
            String health = character.getStats().getHealth() + "/" + ((int)(Stats.MAX_HEALTH));
            String mana = character.getStats().getMana() + "/" + ((int)Stats.MAX_MANA);
            // replace with labels

            batch.draw(ResourceManager.get(TextureMaps.UI)[0][3], getX() + HUD.FONT_SIZE,
                12 + getY() + ((2) * HUD.FONT_SIZE),
                HEALTH_WIDTH * (character.getStats().getHealth() / Stats.MAX_HEALTH), HEALTH_HEIGHT);
            batch.draw(ResourceManager.get(TextureMaps.UI)[1][2], getX() + HUD.FONT_SIZE,
                12 + getY() + ((1) * HUD.FONT_SIZE),
                HEALTH_WIDTH * (character.getStats().getMana() / Stats.MAX_MANA), HEALTH_HEIGHT);

            drawText(batch, "Health: " + health, 0, 1);
            drawText(batch, "Mana: " + mana, 0, 2);
        }*/
    }

}
