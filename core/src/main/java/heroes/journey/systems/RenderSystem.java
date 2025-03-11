package heroes.journey.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import heroes.journey.Application;
import heroes.journey.GameCamera;
import heroes.journey.GameState;
import heroes.journey.components.ActorComponent;
import heroes.journey.components.GlobalGameStateComponent;
import heroes.journey.components.PositionComponent;
import heroes.journey.components.RenderComponent;

import static heroes.journey.Engine.*;

public class RenderSystem extends IteratingSystem {

    public RenderSystem() {
        super(Family.all(PositionComponent.class, RenderComponent.class, GlobalGameStateComponent.class).get());
    }

    @Override
    public void update(float delta) {
        Application.get().getBatch().begin();
        super.update(delta);
        Application.get().getBatch().end();
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        PositionComponent position = POSITION.get(entity);
        RenderComponent render = RENDER.get(entity);
        ActorComponent actor = ACTOR.get(entity);

        if (actor != null) {
            actor.act(delta);
        }
        
        int xo = (int) (GameCamera.get().position.x / GameCamera.get().getSize());
        int yo = (int) (GameCamera.get().position.y / GameCamera.get().getSize());
        int x0 = (int) Math.max(Math.floor(xo - 22), 0);
        int y0 = (int) Math.max(Math.floor(yo - 14), 0);
        int x1 = (int) Math.min(Math.floor(xo + 22), GameState.global().getWidth());
        int y1 = (int) Math.min(Math.floor(yo + 14), GameState.global().getHeight());

        if (position.getX() >= x0 && position.getX() <= x1 && position.getY() >= y0 && position.getY() <= y1) {
            float x = (position.getX() + (actor == null ? 0 : actor.getX())) * GameCamera.get().getSize();
            float y = (position.getY() + (actor == null ? 0 : actor.getY())) * GameCamera.get().getSize();
            Application.get().getBatch().setColor(Color.WHITE);
            Application.get()
                .getBatch()
                .draw(render.getSprite(), x, y, GameCamera.get().getSize(),
                    heroes.journey.GameCamera.get().getSize());
        }
    }
}
