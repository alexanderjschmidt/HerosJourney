package heroes.journey.entities.ai;

import java.util.ArrayList;
import java.util.List;

import heroes.journey.GameState;
import heroes.journey.entities.Character;
import heroes.journey.entities.actions.QueuedAction;
import heroes.journey.initializers.base.Actions;
import heroes.journey.utils.ai.MCTS;
import heroes.journey.utils.ai.Scorer;
import heroes.journey.utils.ai.pathfinding.Cell;

public class MCTSAI implements AI, Scorer {

    private final MCTS mcts;

    public MCTSAI(){
        mcts = new MCTS();
    }

    @Override
    public QueuedAction getMove(GameState gameState, Character playingCharacter) {
        return mcts.runMCTS(gameState.clone(), playingCharacter, this);
    }

    @Override
    public List<QueuedAction> getPossibleQueuedActions(GameState gameState) {
        List<QueuedAction> possibleActions = new ArrayList<>();
        Character playingCharacter = gameState.getEntities().getCurrentEntity();
        Cell target = new Cell(playingCharacter.getXCoord(), playingCharacter.getYCoord() - 1, 1);
        Cell path = new Cell(playingCharacter.getXCoord(), playingCharacter.getYCoord(), 1);
        path.parent = target;
        if (playingCharacter.getYCoord() > 0)
            possibleActions.add(new QueuedAction(path, Actions.wait,0,0));
        Cell path2 = new Cell(playingCharacter.getXCoord(), playingCharacter.getYCoord(), 1);
        possibleActions.add(new QueuedAction(path2, Actions.wait,0,0));
        return possibleActions;
    }

    @Override
    public int getScore(GameState gameState, Character playingCharacter) {
        return 1;
    }
}
