package heros.journey.initializers;

import heros.journey.entities.items.Item;
import heros.journey.entities.items.ItemType;

public class BaseItem {

	static {
        new Item("Wood", ItemType.Misc, 1, 1);
        new Item("Health Potion", ItemType.Consumable, 1, 1);
        new Item("Iron Ore", ItemType.Misc, 1, 1);
        new Item("Iron Ingot", ItemType.Misc, 1, 1);
        new Item("Sword", ItemType.Weapon, 3, 1);
        new Item("Armor", ItemType.Armor, 5, 1);
    }

}
