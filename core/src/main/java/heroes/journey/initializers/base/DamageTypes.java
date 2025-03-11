package heroes.journey.initializers.base;

import heroes.journey.entities.stats.DamageType;
import heroes.journey.initializers.InitializerInterface;

public class DamageTypes implements InitializerInterface {

    public static DamageType physical, magical, trueDamage;

	static {
        physical = new DamageType("Physical");
        magical = new DamageType("Magical");
        trueDamage = new DamageType("True");
	}

}
