package heroes.journey.entities;

import java.util.HashMap;
import java.util.UUID;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.PositionComponent;
import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.systems.GameEngine;

public class EntityManager implements Cloneable {

    private int width, height;
    private Entity[][] entitiesLocations;

    private final HashMap<UUID,Entity> entities;

    public EntityManager(int width, int height) {
        this.width = width;
        this.height = height;
        entitiesLocations = new Entity[width][height];
        entities = new HashMap<>();
    }

    public EntityManager clone() {
        EntityManager clone = new EntityManager(width, height);

        for (UUID id : entities.keySet()) {
            clone.entities.put(id, cloneEntity(clone, entities.get(id)));
        }

        return clone;
    }

    private Entity cloneEntity(EntityManager manager, Entity e) {
        Entity clone = new Entity();
        for (Component component : e.getComponents()) {
            if (component instanceof ClonableComponent<?> clonableComponent) {
                clone.add(clonableComponent.clone());
                if (component instanceof PositionComponent) {
                    manager.addEntity(clone);
                }
            }
        }
        GameEngine.get().addEntity(clone);
        return clone;
    }

    public Entity removeEntity(int x, int y) {
        Entity e = entitiesLocations[x][y];
        entitiesLocations[x][y] = null;
        return e;
    }

    public void registerEntity(UUID id, Entity e) {
        entities.put(id, e);
    }

    public void unregisterEntity(UUID id) {
        entities.remove(id);
    }

    public void addEntity(Entity e) {
        PositionComponent position = PositionComponent.get(e);
        if (entitiesLocations[position.getX()][position.getY()] == null) {
            entitiesLocations[position.getX()][position.getY()] = e;
        }
    }

    public Entity get(int x, int y) {
        if (x < 0 || y < 0 || y >= height || x >= width)
            return null;
        return entitiesLocations[x][y];
    }

    public void print() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Entity e = this.get(x, y);
                if (e != null) {
                    System.out.print("[" + x + ": " + y + "]  \t");
                } else {
                    System.out.print("(" + x + ", " + y + ")  \t");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void dispose() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Entity e = entitiesLocations[i][j];
                if (e != null) {
                    GameEngine.get().removeEntity(e);
                }
            }
        }
    }

    public Entity getEntity(UUID id) {
        return entities.get(id);
    }
}
