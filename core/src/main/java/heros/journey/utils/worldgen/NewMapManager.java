package heros.journey.utils.worldgen;

import heros.journey.GameState;
import heros.journey.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class NewMapManager {

    List<Entity> startingEntities;
    List<MapGenerationEffect> mapGenerationEffects;

    private static NewMapManager newMapManager;

    public static NewMapManager get() {
        if (newMapManager == null)
            newMapManager = new NewMapManager();
        return newMapManager;
    }

    private NewMapManager() {
        startingEntities = new ArrayList<>();
        mapGenerationEffects = new ArrayList<>();
    }

    public List<Entity> getStartingEntities() {
        return startingEntities;
    }

    public List<MapGenerationEffect> getMapGenerationEffects() {
        return mapGenerationEffects;
    }

    public void initGameState(GameState gameState) {
        initMapGeneration(gameState);
        initEntityGeneration(gameState);
    }

    public void initMapGeneration(GameState gameState){
        for (MapGenerationEffect mapGenerationEffect : mapGenerationEffects){
            mapGenerationEffect.apply(gameState);
        }
    }

    public void initEntityGeneration(GameState gameState){
        for(Entity entity : startingEntities) {
            gameState.getEntities().addEntity(entity);
        }
    }

}
