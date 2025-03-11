package heroes.journey.entities.buffs;

import com.badlogic.gdx.graphics.g2d.Batch;

import heroes.journey.GameState;
import heroes.journey.entities.Entity;

public class Buff {

	private BuffType type;
	private int turnsLived;
	private int used;

	public Buff(BuffType type) {
		this.type = type;
		turnsLived = 0;
		used = 0;
	}

	public Buff clone() {
		Buff clone = new Buff(type);
		clone.turnsLived = turnsLived;
		clone.used = used;
		return clone;
	}

	public void activate() {
		this.activate(null, null, 0, 0);
	}

	public void activate(GameState gameState, Entity selected, int targetX, int targetY) {
		used++;
		type.activate(gameState, selected, targetX, targetY);
	}

	public void render(Batch batch, float renderX, float renderY) {
		type.render(batch, renderX, renderY);
	}

	// returns whether to remove it
	public boolean update() {
		turnsLived++;
		return !isActive();
	}

	public boolean isActive() {
		return turnsLived < type.getLifespan() && (used < type.getUses() || type.getUses() == -1);
	}

	public boolean isType(BuffType type2) {
		return this.type == type2;
	}

	public BuffType getType() {
		return type;
	}

}
