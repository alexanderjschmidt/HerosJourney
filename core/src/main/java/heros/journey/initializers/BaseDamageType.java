package heros.journey.initializers;

import heros.journey.entities.stats.DamageType;

public class BaseDamageType {

    public static DamageType physical, magical, trueDamage;

	static {
        physical = new DamageType("Physical");
        magical = new DamageType("Magical");
        trueDamage = new DamageType("True");
	}

}
