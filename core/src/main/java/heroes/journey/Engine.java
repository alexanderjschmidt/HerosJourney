package heroes.journey;

public class Engine extends com.badlogic.ashley.core.Engine {

    private static Engine engine;

    public static Engine get() {
        if (engine == null)
            engine = new Engine();
        return engine;
    }

}
