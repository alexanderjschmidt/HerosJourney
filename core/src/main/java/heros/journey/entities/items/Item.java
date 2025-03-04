package heros.journey.entities.items;

public class Item implements ItemInterface {

	private String name;
	private ItemType type;
	private int weight, value;

	public Item(String name, ItemType type, int weight, int value) {
		this.name = name;
		this.type = type;
		this.weight = weight;
		this.value = value;
        ItemManager.get().put(name, this);
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
