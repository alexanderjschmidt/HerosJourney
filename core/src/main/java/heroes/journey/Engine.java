package heroes.journey;

import com.badlogic.ashley.core.ComponentMapper;
import heroes.journey.components.*;
import heroes.journey.systems.MovementSystem;
import heroes.journey.systems.RenderSystem;

public class Engine extends com.badlogic.ashley.core.Engine {

    public static final ComponentMapper<PositionComponent> POSITION = ComponentMapper.getFor(
        PositionComponent.class);
    public static final ComponentMapper<RenderComponent> RENDER = ComponentMapper.getFor(
        RenderComponent.class);
    public static final ComponentMapper<ActorComponent> ACTOR = ComponentMapper.getFor(ActorComponent.class);
    public static final ComponentMapper<MovementComponent> movementMapper = ComponentMapper.getFor(
        MovementComponent.class);
    public static final ComponentMapper<ActionComponent> actionMapper = ComponentMapper.getFor(
        ActionComponent.class);
    public static final ComponentMapper<StatsComponent> statsMapper = ComponentMapper.getFor(StatsComponent.class);
    public static final ComponentMapper<InventoryComponent> inventoryMapper = ComponentMapper.getFor(
        InventoryComponent.class);
    public static final ComponentMapper<FactionComponent> factionMapper = ComponentMapper.getFor(
        FactionComponent.class);
    public static final ComponentMapper<AIComponent> aiMapper = ComponentMapper.getFor(AIComponent.class);

    private static Engine engine;

    public static Engine get() {
        if (engine == null)
            engine = new Engine();
        return engine;
    }

    private Engine() {
        super();
        addSystem(new RenderSystem());
        addSystem(new MovementSystem());
    }

}
