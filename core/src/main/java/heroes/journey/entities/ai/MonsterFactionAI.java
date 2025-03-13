package heroes.journey.entities.ai;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.components.FactionComponent;
import heroes.journey.entities.actions.QueuedAction;

public class MonsterFactionAI implements AI {

    @Override
    public QueuedAction getMove(GameState gameState, Entity playingEntity) {
        FactionComponent faction = FactionComponent.get(playingEntity);

        return null;
    }
}
