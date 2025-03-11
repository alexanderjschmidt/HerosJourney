package heroes.journey.utils.ai;

import heroes.journey.GameState;
import heroes.journey.entities.Character;
import heroes.journey.entities.actions.QueuedAction;

import java.util.List;

public interface Scorer {

    public List<QueuedAction> getPossibleQueuedActions(GameState gameState);

    public int getScore(GameState gameState, Character playingCharacter);

}
