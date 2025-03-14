package heroes.journey.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import heroes.journey.components.GameStateComponent;
import heroes.journey.components.GlobalGameStateComponent;

public class CleanupNonGlobalGameStateSystem extends IteratingSystem {

    public CleanupNonGlobalGameStateSystem() {
        super(Family.all(GameStateComponent.class).exclude(GlobalGameStateComponent.class).get(),
            Integer.MAX_VALUE);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        GameStateComponent gameStateComponent = GameStateComponent.get(entity);
        if (!gameStateComponent.isGlobal()) {
            GameEngine.get().removeEntity(entity);
        }
    }
}
