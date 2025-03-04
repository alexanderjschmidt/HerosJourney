package heros.journey.entities.items;

public enum BasicItem implements ItemInterface {

	Wood("Wood", ItemType.Misc, 1, 1), HealthPotion("Health Potion", ItemType.Consumable, 1, 1), IronOre("Iron Ore", ItemType.Misc, 1, 1), IronIngot("Iron Ingot", ItemType.Misc, 1, 1), Sword("Sword",
			ItemType.Weapon, 3, 1);

	private String name;
	private ItemType type;
	private int weight, value;

	private BasicItem(String name, ItemType type, int weight, int value) {
		this.name = name;
		this.type = type;
		this.weight = weight;
		this.value = value;
	}

	public ItemType getType() {
		return type;
	}

	public int getWeight() {
		return weight;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return name;
	}

}
