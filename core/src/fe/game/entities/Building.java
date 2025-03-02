package fe.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

import fe.game.GameState;

public class Building extends Entity {

	public Building(EntityClass species, Team team, GameState gameState) {
		super(species, team, gameState);
	}

	public void render(Batch batch, float deltaTime) {
		used = false;
		this.act(deltaTime);
		elapsedTime += deltaTime;
		batch.setColor(this.getColor());
		job.render(batch, renderX + this.getX(), renderY + getY(), elapsedTime);
		batch.setColor(Color.WHITE);
	}
}
