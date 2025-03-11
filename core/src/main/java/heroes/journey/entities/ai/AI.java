package heroes.journey.entities.ai;

import heroes.journey.GameState;
import heroes.journey.entities.Entity;
import heroes.journey.entities.actions.QueuedAction;

public interface AI {

	public QueuedAction getMove(GameState gameState, Entity playingEntity);

}
