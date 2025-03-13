package heroes.journey.systems;

import com.badlogic.ashley.core.ComponentMapper;

import heroes.journey.components.ActionComponent;
import heroes.journey.components.ActorComponent;
import heroes.journey.components.MovementComponent;
import heroes.journey.components.RenderComponent;
import heroes.journey.components.StatsComponent;

public class GameEngine extends com.badlogic.ashley.core.Engine {

    public static final ComponentMapper<RenderComponent> RENDER = ComponentMapper.getFor(
        RenderComponent.class);
    public static final ComponentMapper<ActorComponent> ACTOR = ComponentMapper.getFor(ActorComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(
        MovementComponent.class);
    public static final ComponentMapper<ActionComponent> actionMapper = ComponentMapper.getFor(
        ActionComponent.class);
    public static final ComponentMapper<StatsComponent> statsMapper = ComponentMapper.getFor(
        StatsComponent.class);

    private static GameEngine gameEngine;

    public static GameEngine get() {
        if (gameEngine == null)
            gameEngine = new GameEngine();
        return gameEngine;
    }

    private GameEngine() {
        super();
        addSystem(new RenderSystem());
        addSystem(new MovementSystem());
        addSystem(new CleanupNonGlobalGameStateSystem());
        FactionSystem factionSystem = new FactionSystem();
        factionSystem.setProcessing(false);
        addSystem(factionSystem);
    }

}
