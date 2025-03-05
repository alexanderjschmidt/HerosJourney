package heros.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

public class CombatUI extends UI {

	private String attackInfo = "";

	public CombatUI() {
        super(0, 15, 8, 1);
	}

    @Override
    public void update() {

    }

    @Override
    public void drawUI(Batch batch, float parentAlpha) {
        drawText(batch, attackInfo, 0, 0);
    }

    public void setMessage(String string) {
		attackInfo = string;
	}

}
