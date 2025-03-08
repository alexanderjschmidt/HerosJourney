package heros.journey.initializers;

import org.reflections.Reflections;

import java.lang.invoke.MethodHandles;
import java.util.Set;

public class Initializer {

    public static void init() {
        Reflections reflections = new Reflections("heros.journey.initializers");
        Set<Class<? extends InitializerInterface>> classes = reflections.getSubTypesOf(InitializerInterface.class);

        for (Class<?> clazz : classes) {
            try {
                MethodHandles.lookup().ensureInitialized(clazz);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
