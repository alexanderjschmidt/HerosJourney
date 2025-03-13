package heroes.journey.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;

public class FactionComponent implements ClonableComponent<FactionComponent> {

    private final String name;

    public FactionComponent(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public FactionComponent clone() {
        return new FactionComponent(name);
    }

    private static final ComponentMapper<FactionComponent> mapper = ComponentMapper.getFor(
        FactionComponent.class);

    public static FactionComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
