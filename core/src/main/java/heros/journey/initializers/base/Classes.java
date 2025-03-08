package heros.journey.initializers.base;

import heros.journey.entities.EntityClass;
import heros.journey.initializers.InitializerInterface;
import heros.journey.utils.art.ResourceManager;

public class Classes implements InitializerInterface {

    public static final String HERO = "Hero";
    public static EntityClass hero;

	static {
        hero = new EntityClass(HERO, "You", ResourceManager.get().sprites[1][1]);
	}

}
