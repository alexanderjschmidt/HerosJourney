package heroes.journey.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.entities.Position;

public class PositionComponent extends Position implements ClonableComponent<PositionComponent> {

    public PositionComponent(int x, int y) {
        super(x, y);
    }

    @Override
    public PositionComponent clone() {
        return new PositionComponent(getX(), getY());
    }

    private static final ComponentMapper<PositionComponent> mapper = ComponentMapper.getFor(
        PositionComponent.class);

    public static PositionComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
