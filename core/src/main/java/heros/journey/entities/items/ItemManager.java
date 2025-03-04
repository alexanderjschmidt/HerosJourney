package heros.journey.entities.items;

import java.util.HashMap;

import heros.journey.entities.EntityClassManager;

public class ItemManager extends HashMap<String, Item> {

    private static ItemManager itemManager;

    public static ItemManager get() {
        if (itemManager == null)
            itemManager = new ItemManager();
        return itemManager;
    }

    private ItemManager() {
    }

}
