package heros.journey;

import heros.journey.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class NewMapManager {

    List<Entity> startingEntities = new ArrayList<>();

    private static NewMapManager newMapManager;

    public static NewMapManager get() {
        if (newMapManager == null)
            newMapManager = new NewMapManager();
        return newMapManager;
    }

    private NewMapManager() {
    }

    public List<Entity> getStartingEntities() {
        return startingEntities;
    }

    public void addtoGameState(GameState gameState) {
        for(Entity entity : startingEntities) {
            gameState.getEntities().addEntity(entity);
        }
    }

}
