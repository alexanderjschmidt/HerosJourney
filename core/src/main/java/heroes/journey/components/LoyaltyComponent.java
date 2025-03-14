package heroes.journey.components;

import java.util.HashMap;
import java.util.UUID;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.entities.Loyalty;
import heroes.journey.systems.GameEngine;

public class LoyaltyComponent extends HashMap<UUID,Loyalty> implements Component {

    public LoyaltyComponent putLoyalty(UUID entityId, Loyalty loyalty) {
        super.put(entityId, loyalty);
        return this;
    }

    public LoyaltyComponent putLoyalty(Entity entity, Loyalty loyalty) {
        super.put(GameEngine.getID(entity), loyalty);
        return this;
    }

    private static final ComponentMapper<LoyaltyComponent> mapper = ComponentMapper.getFor(
        LoyaltyComponent.class);

    public static LoyaltyComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
