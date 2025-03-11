package heroes.journey.entities.ai;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.entities.actions.QueuedAction;

public interface AI {

    public QueuedAction getMove(GameState gameState, Entity playingEntity);

}
