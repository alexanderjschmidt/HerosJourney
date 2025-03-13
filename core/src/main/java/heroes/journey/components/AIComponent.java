package heroes.journey.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.entities.ai.AI;

public class AIComponent implements ClonableComponent<AIComponent> {

    public AI ai;

    public AIComponent(AI ai) {
        this.ai = ai;
    }

    public AI getAI() {
        return ai;
    }

    public AIComponent clone() {
        return new AIComponent(ai);
    }

    private static final ComponentMapper<AIComponent> mapper = ComponentMapper.getFor(AIComponent.class);

    public static AIComponent get(Entity entity) {
        return mapper.get(entity);
    }
}
