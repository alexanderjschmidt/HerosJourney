package heros.journey.entities.items;

import java.util.HashMap;

public class Inventory extends HashMap<ItemInterface, Integer> {

	private int gold;

	public int getWeight() {
		int weight = 0;
		for (ItemInterface item : this.keySet()) {
			weight += item.getWeight() * this.get(item);
		}
		return weight;
	}

	public void add(ItemInterface item) {
		this.add(item, 1);
	}

	public void add(ItemInterface item, int count) {
		if (this.containsKey(item))
			this.put(item, this.get(item) + count);
		else
			this.put(item, count);
	}

	public void remove(ItemInterface item, int count) {
		if (this.containsKey(item))
			if (this.get(item) >= count) {
				this.remove(item);
			} else {
				this.put(item, this.get(item) - count);
			}
	}

}
