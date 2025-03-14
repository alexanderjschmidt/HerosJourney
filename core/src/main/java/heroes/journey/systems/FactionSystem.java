package heroes.journey.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import heroes.journey.GameState;
import heroes.journey.components.AIComponent;
import heroes.journey.components.FactionComponent;
import heroes.journey.components.GlobalGameStateComponent;
import heroes.journey.entities.actions.ActionQueue;
import heroes.journey.entities.actions.QueuedAction;
import heroes.journey.ui.HUD;

public class FactionSystem extends IteratingSystem {

    public FactionSystem() {
        super(Family.all(FactionComponent.class, AIComponent.class, GlobalGameStateComponent.class).get());
    }

    @Override
    public void update(float delta) {
        System.out.println("Faction System Running");
        HUD.HUDState previousState = HUD.get().getState();
        HUD.get().setState(HUD.HUDState.LOCKED);
        super.update(delta);
        setProcessing(false);
        HUD.get().setState(previousState);
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        AIComponent ai = AIComponent.get(entity);

        QueuedAction action = ai.getAI().getMove(GameState.global(), entity);
        if (action != null)
            ActionQueue.get().addAction(action);
    }
}
