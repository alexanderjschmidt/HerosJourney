package heros.journey.entities.buffs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.GameState;
import heros.journey.entities.Entity;

public abstract class BuffType {

	private TextureRegion sprite;
	private int lifespan, uses;

	public BuffType(TextureRegion sprite, int lifespan, int uses) {
		this.lifespan = lifespan;
		this.uses = uses;
		this.sprite = sprite;
	}

	public void render(Batch batch, float renderX, float renderY) {
		batch.draw(sprite, renderX, renderY, GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public int getLifespan() {
		return lifespan;
	}

	public int getUses() {
		return uses;
	}

	public abstract void activate(GameState gameState, Entity selected, int targetX, int targetY);

}
