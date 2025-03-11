package heroes.journey.utils.worldgen;

import heroes.journey.GameState;
import heroes.journey.entities.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMapManager {

    List<Entity> startingEntities;
    Map<MapGenerationPhase, List<MapGenerationEffect>> mapGenerationEffects;

    private static NewMapManager newMapManager;

    public static NewMapManager get() {
        if (newMapManager == null)
            newMapManager = new NewMapManager();
        return newMapManager;
    }

    private NewMapManager() {
        startingEntities = new ArrayList<>();
        mapGenerationEffects = new HashMap<>();
        for(MapGenerationPhase phase:MapGenerationPhase.values()){
            mapGenerationEffects.put(phase, new ArrayList<>());
        }
    }

    public List<Entity> getStartingEntities() {
        return startingEntities;
    }

    public void addMapGenerationEffect(MapGenerationEffect effect) {
        mapGenerationEffects.get(effect.getPhase()).add(effect);
    }

    public void initGameState(GameState gameState) {
        initMapGeneration(gameState);
        initEntityGeneration(gameState);
    }

    public void initMapGeneration(GameState gameState){
        for(MapGenerationPhase phase:MapGenerationPhase.values()) {
            for (MapGenerationEffect mapGenerationEffect : mapGenerationEffects.get(phase)) {
                mapGenerationEffect.apply(gameState);
            }
        }
    }

    public void initEntityGeneration(GameState gameState){
        for(Entity entity : startingEntities) {
            gameState.getEntities().addEntity(entity);
        }
    }

}
