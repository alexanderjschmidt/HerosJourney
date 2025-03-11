package heroes.journey.entities;

import static heroes.journey.Engine.POSITION;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.components.PositionComponent;

public class EntityManager {

    private int width, height;
    private Entity[][] entities;

    private Entity currentEntity;

    private GameState gameState;

    public EntityManager(GameState gameState, int width, int height) {
        this.gameState = gameState;
        this.width = width;
        this.height = height;
        entities = new Entity[width][height];
    }

    public EntityManager clone(GameState newGameState) {
        EntityManager clone = new EntityManager(newGameState, width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Entity e = entities[i][j];
                if (e != null) {

                }
            }
        }
        return clone;
    }

    public Entity removeEntity(int x, int y) {
        Entity e = entities[x][y];
        entities[x][y] = null;
        return e;
    }

    public void addEntity(Entity e) {
        PositionComponent position = POSITION.get(e);
        if (entities[position.getX()][position.getY()] == null) {
            entities[position.getX()][position.getY()] = e;
        }
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
        List<Entity> entitiesInOrder = Arrays.stream(entities)  // Stream<Character[]>
            .flatMap(Arrays::stream)                         // Stream<Character>
            .filter(Objects::nonNull)                       // Remove nulls
            //    .sorted(Comparator.comparing(Character::getSpeed))
            .collect(Collectors.toList());
        return entitiesInOrder;
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
}
