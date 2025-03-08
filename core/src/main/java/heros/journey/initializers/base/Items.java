package heros.journey.initializers.base;

import heros.journey.entities.items.Item;
import heros.journey.entities.items.ItemType;
import heros.journey.initializers.InitializerInterface;

public class Items implements InitializerInterface {

    public static Item wood, ironOre;

	static {
        wood = new Item("Wood", ItemType.Misc, 1, 1);
        new Item("Health Potion", ItemType.Consumable, 1, 1);
        ironOre = new Item("Iron Ore", ItemType.Misc, 1, 1);
        new Item("Iron Ingot", ItemType.Misc, 1, 1);
        new Item("Sword", ItemType.Weapon, 3, 1);
        new Item("Armor", ItemType.Armor, 5, 1);
    }

}
