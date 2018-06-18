package fe.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fe.game.utils.Direction;

public class Entity {
	private Species species;
	private Color color = new Color(Color.WHITE);
	public float x, y;
	public Actor movement;
	public Direction dir = Direction.SOUTH;
	public String render = "idleSOUTH";

	public int range = 2;

	public Entity(Species species) {
		this.species = species;
		movement = new Actor();
	}

	public void render(Batch batch, float deltaTime) {
		batch.setColor(color);
		species.render(batch, x + movement.getOriginX(),
				y + movement.getOriginY(), render, 1f, deltaTime);
		batch.setColor(Color.WHITE);
	}

	public int getMoveDistance() {
		return species.getMoveDistance();
	}
}
