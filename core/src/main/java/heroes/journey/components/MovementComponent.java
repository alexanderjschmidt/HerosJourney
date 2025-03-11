package heroes.journey.components;

import com.badlogic.ashley.core.Component;
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

    public MovementComponent clone() {
        throw new RuntimeException("This is a visual component (AI Clones should just not visually move) DONT clone it");
    }
}
