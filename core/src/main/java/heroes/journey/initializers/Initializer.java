package heroes.journey.initializers;

import java.lang.invoke.MethodHandles;
import java.util.Set;

import org.reflections.Reflections;

public class Initializer {

    public static void init() {
        Reflections reflections = new Reflections("heroes.journey.initializers");
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
