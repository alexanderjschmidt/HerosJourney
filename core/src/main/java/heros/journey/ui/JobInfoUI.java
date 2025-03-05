package heros.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.GameCamera;
import heros.journey.entities.EntityClass;

public class JobInfoUI extends UI {

	private EntityClass job;

	private boolean toggled;

	public JobInfoUI() {
        super(0, 0, 8, 10, false, true);
		toggled = true;
		toggle();
	}

    @Override
    public void update() {

    }

    @Override
	public void drawUI(Batch batch, float parentAlpha) {
		if (job != null) {
			job.render(batch, getX() + 12, getY() + 24 + HUD.FONT_SIZE * 9, GameCamera.get().getSize() * 2, GameCamera.get().getSize() * 2, 0);
        }
        drawText(batch, job != null ? job.toString() : "---", 0, 0);
        drawText(batch, job != null ? "Cost: " + job.getValue() : "Cost: ---", 0, 1);
        drawText(batch, job != null ? "Damage: " + job.getBaseDamage() : "Damage: ---", 0, 2);
        drawText(batch, job != null ? "Health: " + job.getMaxHealth() : "Health: ---", 0, 3);
        drawText(batch, job != null ? "Move: " + job.getMoveDistance() : "Move: ---", 0, 4);
        drawText(batch, job != null ? job.getDescription() : "", 0, 5, true);
	}

	public void update(EntityClass job) {
		this.job = job;
	}

	public void toggle() {
		toggled = !toggled;
        this.setVisible(toggled);
	}

	public boolean toggled() {
		return toggled;
	}

}
