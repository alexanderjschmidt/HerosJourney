package heroes.journey.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;

public class PositionComponent implements ClonableComponent<PositionComponent> {

    private int x, y;

    public PositionComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public PositionComponent setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public PositionComponent setY(int y) {
        this.y = y;
        return this;
    }

    public PositionComponent clone() {
        return new PositionComponent(x, y);
    }

    private static final ComponentMapper<PositionComponent> mapper = ComponentMapper.getFor(
        PositionComponent.class);

    public static PositionComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
