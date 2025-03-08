package heros.journey.initializers.base;

import heros.journey.GameState;
import heros.journey.NewMapManager;
import heros.journey.entities.Entity;
import heros.journey.initializers.InitializerInterface;

public class Map implements InitializerInterface {

    static {
        Entity player = new Entity(Classes.hero, GameState.global().getPlayerTeam());
        player.setMapPosition(16, 16);
        player.getInventory().add(Items.wood);
        player.getInventory().add(Items.ironOre);
        NewMapManager.get().getStartingEntities().add(player);
    }

}

