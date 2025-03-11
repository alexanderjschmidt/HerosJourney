package heroes.journey.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;

import heroes.journey.GameCamera;
import heroes.journey.GameState;
import heroes.journey.entities.Entity;
import heroes.journey.entities.EntityClass;
import heroes.journey.entities.items.ItemInterface;
import heroes.journey.entities.stats.Stats;

public class EntityDetailedUI extends UI {

	private Entity entity;

	private boolean toggled;

	public EntityDetailedUI() {
        super(11, 3, 27, 20);
		toggled = false;
        this.setVisible(false);
	}

    @Override
    public void update() {
        Cursor cursor = HUD.get().getCursor();
        this.entity = GameState.global().getEntities().get(cursor.x, cursor.y);
        if (entity == null){
            toggled = false;
            this.setVisible(false);
            return;
        }
        toggled = !toggled;
        this.setVisible(toggled);
    }

    @Override
	public void drawUI(Batch batch, float parentAlpha) {
        if (entity == null)
            return;
        drawStats(batch, parentAlpha);
        drawInventory(batch, parentAlpha);
	}

    private void drawStats(Batch batch, float parentAlpha) {
        EntityClass job = entity.getEntityClass();
        job.render(batch, getX() + 12, getY() + 24 + HUD.FONT_SIZE * 9, GameCamera.get().getSize() * 2, GameCamera.get().getSize() * 2, 0);

        drawText(batch, job.toString(), 0, 0);
        drawText(batch, "Health: " + entity.getStats().getHealth() +"/" + Stats.MAX_HEALTH , 0, 1);
        drawText(batch, "Mana: " + entity.getStats().getMana() +"/" + Stats.MAX_MANA, 0, 2);
        drawText(batch, "Move: " + entity.getMoveDistance(), 0, 3);
        drawText(batch, "Body: " + entity.getStats().getBody(), 0, 4);
        drawText(batch, "Mind: " + entity.getStats().getMind(), 0, 5);
        drawText(batch, "Fame: " + entity.getStats().getFame(), 0, 6);
        drawText(batch, job.getDescription(), 0, 7, true);
    }

    private void drawInventory(Batch batch, float parentAlpha) {
        drawText(batch, "===== Inventory =====", 13, 0);
        List<ItemInterface> items = entity.getInventory().keySet().stream().toList();
        for (int i = 0; i < items.size(); i++){
            ItemInterface item = items.get(i);
            drawText(batch, item.toString(), 13, i + 1);
            drawText(batch, "x" + entity.getInventory().get(item), 22, i + 1);
        }
    }

}
