package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class GlobalGameStateComponent implements Component {

    private static final ComponentMapper<GlobalGameStateComponent> mapper = ComponentMapper.getFor(
        GlobalGameStateComponent.class);

    public static GlobalGameStateComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
