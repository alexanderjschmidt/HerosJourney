package heroes.journey.components;

import java.util.HashMap;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.entities.Loyalty;

public class LoyaltyComponent extends HashMap<Entity,Loyalty> implements Component {

    private static final ComponentMapper<LoyaltyComponent> mapper = ComponentMapper.getFor(
        LoyaltyComponent.class);

    public static LoyaltyComponent get(Entity entity) {
        return mapper.get(entity);
    }

    public LoyaltyComponent putLoyalty(Entity entity, Loyalty loyalty) {
        super.put(entity, loyalty);
        return this;
    }
}
