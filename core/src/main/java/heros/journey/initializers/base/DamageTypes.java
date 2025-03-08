package heros.journey.initializers.base;

import heros.journey.entities.stats.DamageType;
import heros.journey.initializers.InitializerInterface;

public class DamageTypes implements InitializerInterface {

    public static DamageType physical, magical, trueDamage;

	static {
        physical = new DamageType("Physical");
        magical = new DamageType("Magical");
        trueDamage = new DamageType("True");
	}

}
