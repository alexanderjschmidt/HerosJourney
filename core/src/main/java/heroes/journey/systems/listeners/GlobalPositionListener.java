package heroes.journey.systems.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;

import heroes.journey.GameState;
import heroes.journey.components.GlobalGameStateComponent;
import heroes.journey.components.PositionComponent;

public class GlobalPositionListener implements EntityListener {

    public static Family getFamily() {
        return Family.all(GlobalGameStateComponent.class, PositionComponent.class).get();
    }

    @Override
    public void entityAdded(Entity entity) {
        GameState.global().getEntities().addEntity(entity);
    }

    @Override
    public void entityRemoved(Entity entity) {
        PositionComponent position = PositionComponent.get(entity);
        GameState.global().getEntities().removeEntity(position.getX(), position.getY());
    }
}
