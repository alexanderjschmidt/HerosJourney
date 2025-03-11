package heroes.journey.entities;

import java.util.HashMap;

public class EntityClassManager extends HashMap<String, EntityClass> {

	private static final long serialVersionUID = 1L;

	private static EntityClassManager entityClassManager;

	public static EntityClassManager get() {
		if (entityClassManager == null)
			entityClassManager = new EntityClassManager();
		return entityClassManager;
	}

	private EntityClassManager() {
	}

}
