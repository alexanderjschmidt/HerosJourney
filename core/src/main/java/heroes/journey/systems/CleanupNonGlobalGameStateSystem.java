package heroes.journey.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import heroes.journey.components.GlobalGameStateComponent;

public class CleanupNonGlobalGameStateSystem extends IteratingSystem {

    public CleanupNonGlobalGameStateSystem() {
        super(Family.exclude(GlobalGameStateComponent.class).get(), Integer.MAX_VALUE);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        GameEngine.get().removeEntity(entity);
    }
}
