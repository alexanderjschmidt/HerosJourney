package heros.journey.entities.buffs;

import heros.journey.GameState;
import heros.journey.entities.Effect;
import heros.journey.entities.Entity;
import heros.journey.utils.art.ResourceManager;

public class BuffManager {

	public BuffType parry, rallied, blessed, poisoned;

	private static BuffManager buffManager;

	public static BuffManager get() {
		if (buffManager == null)
			buffManager = new BuffManager();
		return buffManager;
	}

	private BuffManager() {
		parry = new BuffType(ResourceManager.get().sprites[4][2], 1, 1) {

			@Override
			public void activate(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity attacker = selected;
				Entity defender = gameState.getEntities().get(targetX, targetY);

				int damageReturned = attacker.calcDamageTaken(defender);
				defender.lunge(.3f, attacker);
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().slash[(int) (Math.random() * 6)], defender.renderX, defender.renderY));
				if (damageReturned > 0) {
					attacker.vibrate(.5f, defender);
					attacker.adjustHealth(defender, .5f, -damageReturned);
				}
			}

		};
		rallied = new BuffType(ResourceManager.get().sprites[4][3], 2, 1) {

			@Override
			public void activate(GameState gameState, Entity selected, int targetX, int targetY) {
			}

		};
		blessed = new BuffType(ResourceManager.get().sprites[4][4], 3, 5) {

			@Override
			public void activate(GameState gameState, Entity selected, int targetX, int targetY) {
			}

		};
		poisoned = new BuffType(ResourceManager.get().sprites[4][5], 3, 3) {

			@Override
			public void activate(GameState gameState, Entity selected, int targetX, int targetY) {

			}

		};
	}

}
