package heroes.journey.components;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {

    private int x, y;

    public PositionComponent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public PositionComponent setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public PositionComponent setY(int y) {
        this.y = y;
        return this;
    }

    public PositionComponent clone() {
        return new PositionComponent(x, y);
    }
}
