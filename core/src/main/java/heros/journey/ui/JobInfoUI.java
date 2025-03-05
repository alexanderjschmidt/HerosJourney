package heros.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.GameCamera;
import heros.journey.entities.EntityClass;
import heros.journey.entities.stats.Stats;

public class JobInfoUI extends UI {

	private EntityClass job;

	private boolean toggled;

	public JobInfoUI() {
        super(11, 3, 26, 20);
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
        drawText(batch, job != null ? "Damage: " + 1 : "Damage: ---", 0, 2);
        drawText(batch, job != null ? "Health: " + Stats.MAX_HEALTH : "Health: ---", 0, 3);
        drawText(batch, job != null ? "Move: " + 5 : "Move: ---", 0, 4);
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
