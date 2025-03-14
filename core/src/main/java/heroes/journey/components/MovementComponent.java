package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import heroes.journey.utils.ai.pathfinding.Cell;

public class MovementComponent implements Component {

    private Cell path;

    public MovementComponent() {
    }

    public void move(Cell path) {
        if (path == null) {
            return;
        }
        this.path = path;
    }

    public Cell getPath() {
        return path;
    }

    public void progressPath() {
        path = path.parent;
    }

    public boolean hasPath() {
        return path != null;
    }

    private static final ComponentMapper<MovementComponent> mapper = ComponentMapper.getFor(
        MovementComponent.class);

    public static MovementComponent get(Entity entity) {
        return mapper.get(entity);
    }

}
