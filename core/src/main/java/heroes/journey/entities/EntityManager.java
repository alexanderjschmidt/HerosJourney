package heroes.journey.entities;

import static heroes.journey.systems.GameEngine.statsMapper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

import heroes.journey.components.FactionComponent;
import heroes.journey.components.PositionComponent;
import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.systems.GameEngine;

public class EntityManager implements Cloneable {

    private int width, height;
    private Entity[][] entities;

    private Entity currentEntity;

    private final HashMap<String,Entity> factions;

    public EntityManager(int width, int height) {
        this.width = width;
        this.height = height;
        entities = new Entity[width][height];
        factions = new HashMap<>();
    }

    public EntityManager clone() {
        EntityManager clone = new EntityManager(width, height);

        for (Entity faction : factions.values()) {
            clone.factions.put(faction.toString(), cloneEntity(clone, faction));
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Entity e = entities[i][j];
                if (e != null) {
                    Entity clonedEntity = cloneEntity(clone, e);
                    clone.addEntity(clonedEntity);
                    if (e == currentEntity) {
                        clone.setCurrentEntity(clonedEntity);
                    }
                }
            }
        }
        return clone;
    }

    private Entity cloneEntity(EntityManager clonedManager, Entity e) {
        Entity clone = new Entity();
        for (Component component : e.getComponents()) {
            if (component instanceof ClonableComponent<?> clonableComponent) {
                ClonableComponent clonedComponent = clonableComponent.clone();
                clone.add(clonedComponent);
            }
        }
        GameEngine.get().addEntity(clone);
        return clone;
    }

    public Entity removeEntity(int x, int y) {
        Entity e = entities[x][y];
        entities[x][y] = null;
        return e;
    }

    public void addEntity(Entity e) {
        PositionComponent position = PositionComponent.get(e);
        if (entities[position.getX()][position.getY()] == null) {
            entities[position.getX()][position.getY()] = e;
        }
    }

    public void addFactions(Entity entity) {
        FactionComponent factionComponent = FactionComponent.get(entity);
        factions.put(factionComponent.toString(), entity);
    }

    /**
     * takes into account fog
     *
     * @param x
     * @param y
     * @return
     */
    public Entity get(int x, int y) {
        if (x < 0 || y < 0 || y >= height || x >= width)
            return null;
        return entities[x][y];
    }

    public List<Entity> getEntitiesInActionOrder() {
        return Arrays.stream(entities)  // Stream<Character[]>
            .flatMap(Arrays::stream)                         // Stream<Character>
            .filter(Objects::nonNull)                       // Remove nulls
            .sorted(Comparator.comparing((Entity e) -> statsMapper.get(e).getSpeed())
                .thenComparing(Object::toString)).collect(Collectors.toList());
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

    public void setCurrentEntity(Entity currentEntity) {
        this.currentEntity = currentEntity;
    }

    public Entity getCurrentEntity() {
        return currentEntity;
    }

    public void dispose() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Entity e = entities[i][j];
                if (e != null) {
                    GameEngine.get().removeEntity(e);
                }
            }
        }
    }

    public HashMap<String,Entity> getFactions() {
        return factions;
    }
}
