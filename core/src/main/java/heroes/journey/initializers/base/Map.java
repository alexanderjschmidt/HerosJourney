package heroes.journey.initializers.base;

import static heroes.journey.initializers.base.Tiles.CAVE;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;

import heroes.journey.GameState;
import heroes.journey.components.AIComponent;
import heroes.journey.components.ActionComponent;
import heroes.journey.components.ActorComponent;
import heroes.journey.components.FactionComponent;
import heroes.journey.components.GlobalGameStateComponent;
import heroes.journey.components.InventoryComponent;
import heroes.journey.components.LoyaltyComponent;
import heroes.journey.components.MovementComponent;
import heroes.journey.components.PlayerComponent;
import heroes.journey.components.PositionComponent;
import heroes.journey.components.RenderComponent;
import heroes.journey.components.StatsComponent;
import heroes.journey.entities.actions.ActionQueue;
import heroes.journey.entities.ai.MCTSAI;
import heroes.journey.initializers.InitializerInterface;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;
import heroes.journey.utils.ai.pathfinding.AStar;
import heroes.journey.utils.ai.pathfinding.Cell;
import heroes.journey.utils.art.ResourceManager;
import heroes.journey.utils.art.TextureMaps;
import heroes.journey.utils.worldgen.CellularAutomata;
import heroes.journey.utils.worldgen.MapGenerationEffect;
import heroes.journey.utils.worldgen.MapGenerationPhase;
import heroes.journey.utils.worldgen.NewMapManager;

public class Map implements InitializerInterface {

    static {
        Entity goblins = new Entity();
        goblins.add(new FactionComponent("Goblins")).add(new GlobalGameStateComponent());

        Entity player = new Entity();
        player.add(new PlayerComponent(ActionQueue.get().getID()))
            .add(new PositionComponent(16, 16))
            .add(new GlobalGameStateComponent())
            .add(new RenderComponent(ResourceManager.get(TextureMaps.Sprites)[1][1]))
            .add(new ActorComponent())
            .add(new MovementComponent())
            .add(new ActionComponent())
            .add(new AIComponent(new MCTSAI()))
            .add(new StatsComponent())
            .add(new InventoryComponent())
            .add(new LoyaltyComponent().putLoyalty(goblins, Loyalties.ENEMY));

        Entity goblin = new Entity();
        goblin.add(new PositionComponent(16, 10))
            .add(new GlobalGameStateComponent())
            .add(new RenderComponent(ResourceManager.get(TextureMaps.Sprites)[8][2]))
            .add(new ActorComponent())
            .add(new MovementComponent())
            .add(new ActionComponent())
            .add(new AIComponent(new MCTSAI()))
            .add(new StatsComponent())
            .add(new InventoryComponent())
            .add(new LoyaltyComponent().putLoyalty(goblins, Loyalties.ALLY));

        NewMapManager.get().getStartingEntities().add(goblins);
        NewMapManager.get().getStartingEntities().add(player);
        NewMapManager.get().getStartingEntities().add(goblin);

        // Generated Map
        new MapGenerationEffect(MapGenerationPhase.INIT) {
            @Override
            public void applyEffect(GameState gameState) {
                int width = gameState.getWidth();
                Tile[][] tileMap = CellularAutomata.generateMap(width);
                gameState.getMap().setTileMap(tileMap);
                gameState.getMap().setEnvironment(CellularAutomata.generateTrees(tileMap, width));
            }
        };
        new MapGenerationEffect(MapGenerationPhase.SECOND, 1000) {
            @Override
            public void applyEffect(GameState gameState) {
                genHouses(gameState.getMap().getTileMap(), gameState.getMap().getEnvironment());
            }
        };
        new MapGenerationEffect(MapGenerationPhase.SECOND) {
            @Override
            public void applyEffect(GameState gameState) {
                gameState.getMap().getEnvironment()[16][10] = CAVE;
            }
        };
        new MapGenerationEffect(MapGenerationPhase.FINAL) {
            @Override
            public void applyEffect(GameState gameState) {
                gameState.getMap().genFacingAndVariance();
            }
        };
    }

    private static final int numHouses = 15;
    private static final Vector2[] housePos = new Vector2[numHouses];
    private static int houseStart = 0;
    private static int houseEnd = 1;

    public static void genHouses(Tile[][] tileMap, ActionTile[][] environment) {
        houseStart = 0;
        houseEnd = 1;
        for (int i = 0; i < numHouses; i++) {
            while (true) {
                int x = (int)(Math.random() * tileMap.length);
                int y = (int)(Math.random() * tileMap[0].length);
                if (tileMap[x][y] == Tiles.PLAINS) {
                    housePos[i] = new Vector2(x, y);
                    environment[x][y] = Tiles.HOUSE;
                    break;
                }
            }
        }
        while (houseStart < numHouses) {
            genNextPath();
        }
    }

    private static void genNextPath() {
        Cell path = AStar.aStar((int)housePos[houseStart].x, (int)housePos[houseStart].y,
            (int)housePos[houseEnd].x, (int)housePos[houseEnd].y, GameState.global().getMap());
        while (path != null) {
            GameState.global().getMap().setTile(path.i, path.j, Tiles.PATH);
            path = path.parent;
        }
        houseEnd++;
        if (houseStart == houseEnd) {
            houseEnd++;
        }
        if (houseEnd >= numHouses) {
            houseEnd = 0;
            houseStart++;
        }
    }

}

