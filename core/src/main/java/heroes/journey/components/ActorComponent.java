package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import heroes.journey.utils.Direction;

public class ActorComponent extends Actor implements Component {

    public void vibrate(float delay) {
        // 0.2 seconds
        Vector2 v = Direction.SOUTH.getDirVector();
        this.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(delay),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(5 * v.y, 5 * v.x, .05f,
                Interpolation.pow5In),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-10 * v.y, -10 * v.x, .05f,
                Interpolation.pow5),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(10 * v.y, 10 * v.x, .05f,
                Interpolation.pow5),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-5 * v.y, -5 * v.x, .05f,
                Interpolation.pow5Out)));

    }

    public void lunge(float delay) {
        // 0.4 seconds
        Vector2 v = Direction.SOUTH.getDirVector();
        this.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(
            com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(delay),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(15 * v.x, 15 * v.y, .2f,
                Interpolation.pow5In),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-15 * v.x, -15 * v.y, .2f,
                Interpolation.pow5Out)));
    }

    private static final ComponentMapper<ActorComponent> mapper = ComponentMapper.getFor(
        ActorComponent.class);

    public static ActorComponent get(Entity entity) {
        return mapper.get(entity);
    }

}
