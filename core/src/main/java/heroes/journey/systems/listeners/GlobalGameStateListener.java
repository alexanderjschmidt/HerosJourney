package heroes.journey.systems.listeners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;

import heroes.journey.GameState;
import heroes.journey.components.GameStateComponent;
import heroes.journey.components.GlobalGameStateComponent;

public class GlobalGameStateListener implements EntityListener {

    public static Family getFamily() {
        return Family.all(GameStateComponent.class).get();
    }

    @Override
    public void entityAdded(Entity entity) {
        GameStateComponent gameState = GameStateComponent.get(entity);
        if (gameState.isGlobal()) {
            entity.add(new GlobalGameStateComponent());
            GameState.global().getEntities().registerEntity(gameState.getId(), entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        GameStateComponent gameState = GameStateComponent.get(entity);
        if (gameState.isGlobal()) {
            GameState.global().getEntities().unregisterEntity(gameState.getId());
        }
    }
}
