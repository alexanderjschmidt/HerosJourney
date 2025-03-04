package heros.journey.entities;

import java.util.HashMap;

import heros.journey.utils.art.ResourceManager;

public class EntityClassManager extends HashMap<String, EntityClass> {

	private static final long serialVersionUID = 1L;
	public static final String HERO = "Hero";

	private static EntityClassManager entityClassManager;

	public static EntityClassManager get() {
		if (entityClassManager == null)
			entityClassManager = new EntityClassManager();
		return entityClassManager;
	}

	private EntityClassManager() {
	}

	public void init() {
		new EntityClass(HERO, "You", ResourceManager.get().sprites[1][1], 10, 0, 0, new int[] {}, 5, 5, 30);
	}

}
