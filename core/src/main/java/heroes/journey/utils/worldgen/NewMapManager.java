package heroes.journey.utils.worldgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import heroes.journey.GameState;

public class NewMapManager {

    Map<MapGenerationPhase,List<MapGenerationEffect>> mapGenerationEffects;

    private static NewMapManager newMapManager;

    public static NewMapManager get() {
        if (newMapManager == null)
            newMapManager = new NewMapManager();
        return newMapManager;
    }

    private NewMapManager() {
        mapGenerationEffects = new HashMap<>();
        for (MapGenerationPhase phase : MapGenerationPhase.values()) {
            mapGenerationEffects.put(phase, new ArrayList<>());
        }
    }

    public void addMapGenerationEffect(MapGenerationEffect effect) {
        mapGenerationEffects.get(effect.getPhase()).add(effect);
    }

    public void initMapGeneration(GameState gameState) {
        for (MapGenerationPhase phase : MapGenerationPhase.values()) {
            for (MapGenerationEffect mapGenerationEffect : mapGenerationEffects.get(phase)) {
                mapGenerationEffect.apply(gameState);
            }
        }
    }

}
