package heroes.journey.entities.ai;

import heroes.journey.GameState;
import heroes.journey.entities.Character;
import heroes.journey.entities.actions.QueuedAction;

public class PlayerAI implements AI {

    private final AI aiForPredicting;

    public PlayerAI(AI aiForPredicting){
        this.aiForPredicting = aiForPredicting;
    }

    @Override
    public QueuedAction getMove(GameState gameState, Character e) {
        if (gameState == GameState.global())
            return null;
        else
            return aiForPredicting.getMove(gameState, e);
    }
}
