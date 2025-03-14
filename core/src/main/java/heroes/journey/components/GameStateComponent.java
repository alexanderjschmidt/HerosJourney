package heroes.journey.components;

import java.util.UUID;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;

public class GameStateComponent implements ClonableComponent<GameStateComponent> {

    private final UUID globalId;
    private final boolean global;

    public GameStateComponent() {
        this.globalId = UUID.randomUUID();
        global = true;
    }

    private GameStateComponent(UUID globalId) {
        this.globalId = globalId;
        global = false;
    }

    public UUID getId() {
        return globalId;
    }

    public boolean isGlobal() {
        return global;
    }

    @Override
    public GameStateComponent clone() {
        return new GameStateComponent(this.globalId);
    }

    private static final ComponentMapper<GameStateComponent> mapper = ComponentMapper.getFor(
        GameStateComponent.class);

    public static GameStateComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
