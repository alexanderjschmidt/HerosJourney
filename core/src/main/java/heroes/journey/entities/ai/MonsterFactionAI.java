package heroes.journey.entities.ai;

import java.util.UUID;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.components.AIComponent;
import heroes.journey.components.ActionComponent;
import heroes.journey.components.ActorComponent;
import heroes.journey.components.FactionComponent;
import heroes.journey.components.GameStateComponent;
import heroes.journey.components.InventoryComponent;
import heroes.journey.components.LoyaltyComponent;
import heroes.journey.components.MovementComponent;
import heroes.journey.components.PositionComponent;
import heroes.journey.components.RenderComponent;
import heroes.journey.components.StatsComponent;
import heroes.journey.entities.Position;
import heroes.journey.entities.actions.QueuedAction;
import heroes.journey.initializers.base.Loyalties;
import heroes.journey.systems.GameEngine;
import heroes.journey.utils.art.ResourceManager;
import heroes.journey.utils.art.TextureMaps;

public class MonsterFactionAI implements AI {

    @Override
    public QueuedAction getMove(GameState gameState, Entity playingFaction) {
        FactionComponent faction = FactionComponent.get(playingFaction);
        for (Position location : faction.getOwnedLocations()) {
            Entity entity = gameState.getEntities().get(location.getX(), location.getY());
            if (entity == null && faction.getMembers().size() <= 3) {
                UUID id = GameEngine.getID(createMonster(gameState, playingFaction, location));
                faction.getMembers().add(id);
            }
        }

        return null;
    }

    private Entity createMonster(GameState gameState, Entity faction, Position position) {
        GameStateComponent gameStateComponent = GameStateComponent.get(faction);
        Entity entityFaction = gameState.get(gameStateComponent.getId());
        Entity goblin = new Entity();
        goblin.add(new PositionComponent(position.getX(), position.getY()))
            .add(new GameStateComponent())
            .add(new RenderComponent(ResourceManager.get(TextureMaps.Sprites)[8][2]))
            .add(new ActorComponent())
            .add(new MovementComponent())
            .add(new ActionComponent())
            .add(new AIComponent(new MCTSAI()))
            .add(new StatsComponent())
            .add(new InventoryComponent())
            .add(new LoyaltyComponent().putLoyalty(faction, Loyalties.ALLY));
        System.out.println(goblin);
        GameEngine.get().addEntity(goblin);
        return goblin;
    }
}
