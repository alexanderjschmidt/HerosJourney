package heros.journey.entities.stats;

import java.util.HashMap;

public class DamageTypeManager extends HashMap<String, DamageType> {

    private static final long serialVersionUID = 1L;

    private static DamageTypeManager damageTypeManager;

    public static DamageTypeManager get() {
        if (damageTypeManager == null)
            damageTypeManager = new DamageTypeManager();
        return damageTypeManager;
    }

    private DamageTypeManager() {
    }

}
