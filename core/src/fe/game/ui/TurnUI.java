package fe.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import fe.game.GameState;
import fe.game.entities.Team;
import fe.game.managers.ResourceManager;

public class TurnUI extends Actor {

	private Team team;
	public boolean minimal = false;

	public TurnUI() {
		this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 3);
		this.setPosition(HUD.FONT_SIZE, Gdx.graphics.getHeight() - 24 - HUD.FONT_SIZE * 4);
		this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
	}

	public void update() {
		team = GameState.global().getActiveTeam();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		if (minimal) {
			this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 1);
			this.setPosition(HUD.FONT_SIZE, Gdx.graphics.getHeight() - 24 - HUD.FONT_SIZE * 2);
		} else {
			this.setSize(HUD.FONT_SIZE * 10, 24 + HUD.FONT_SIZE * 3);
			this.setPosition(HUD.FONT_SIZE, Gdx.graphics.getHeight() - 24 - HUD.FONT_SIZE * 4);
		}
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());

		if (!minimal) {
			ResourceManager.get().font24.draw(batch, team.toString() + " Turn", getX() + HUD.FONT_SIZE, 12 + getY() + ((2 + 1) * HUD.FONT_SIZE));
			ResourceManager.get().font24.draw(batch, "Morale: " + team.morale + "/100", getX() + HUD.FONT_SIZE, 12 + getY() + ((1 + 1) * HUD.FONT_SIZE));
			ResourceManager.get().font24.draw(batch, "Supply: " + team.currentTroops + "/" + Team.TROOP_CAP, getX() + HUD.FONT_SIZE, 12 + getY() + ((0 + 1) * HUD.FONT_SIZE));
		} else {
			ResourceManager.get().font24.draw(batch, team.toString() + " Turn", getX() + HUD.FONT_SIZE, 12 + getY() + ((0 + 1) * HUD.FONT_SIZE));
		}
		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
	}

}
