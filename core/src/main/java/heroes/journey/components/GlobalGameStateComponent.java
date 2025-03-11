package heroes.journey.components;

import com.badlogic.ashley.core.Component;

public class GlobalGameStateComponent implements Component {

    public GlobalGameStateComponent clone() {
        throw new RuntimeException("This should only ever be used on the Global Game state, DO NOT CLONE");
    }

}
