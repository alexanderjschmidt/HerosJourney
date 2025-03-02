package fe.game.entities.buffs;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.GameState;
import fe.game.entities.Entity;
import fe.game.tilemap.TileMap;

public abstract class BuffType {

	private TextureRegion sprite;
	private int lifespan, uses;

	public BuffType(TextureRegion sprite, int lifespan, int uses) {
		this.lifespan = lifespan;
		this.uses = uses;
		this.sprite = sprite;
	}

	public void render(Batch batch, float renderX, float renderY) {
		batch.draw(sprite, renderX, renderY, TileMap.SIZE, TileMap.SIZE);
	}

	public int getLifespan() {
		return lifespan;
	}

	public int getUses() {
		return uses;
	}

	public abstract void activate(GameState gameState, Entity selected, int targetX, int targetY);

}
