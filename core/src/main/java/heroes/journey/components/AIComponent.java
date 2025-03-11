package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import heroes.journey.entities.ai.AI;

public class AIComponent implements Component {

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
}
