package heroes.journey.components;

import java.util.HashMap;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.entities.items.ItemInterface;

public class InventoryComponent extends HashMap<ItemInterface,Integer>
    implements ClonableComponent<InventoryComponent> {

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

    public InventoryComponent clone() {
        return new InventoryComponent();
    }

    private static final ComponentMapper<InventoryComponent> mapper = ComponentMapper.getFor(
        InventoryComponent.class);

    public static InventoryComponent get(Entity entity) {
        return mapper.get(entity);
    }

}
