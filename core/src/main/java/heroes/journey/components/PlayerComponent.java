package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

public class PlayerComponent implements Component {

    private final String playerId;

    public PlayerComponent(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    private static final ComponentMapper<PlayerComponent> mapper = ComponentMapper.getFor(
        PlayerComponent.class);

    public static PlayerComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
