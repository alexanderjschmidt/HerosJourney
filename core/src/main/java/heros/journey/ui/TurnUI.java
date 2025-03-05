package heros.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.GameState;
import heros.journey.entities.Team;

public class TurnUI extends UI {

	private Team team;

	public TurnUI() {
        super(0, 0, 8, 1, true, false);
	}

	public void update() {
		team = GameState.global().getActiveTeam();
	}

	@Override
	public void drawUI(Batch batch, float parentAlpha) {
		drawText(batch, team.toString() + " Turn", 0, 0);
	}

}
