package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderComponent implements Component {

    private TextureRegion sprite;

    public RenderComponent(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public TextureRegion getSprite() {
        return sprite;
    }

    private static final ComponentMapper<RenderComponent> mapper = ComponentMapper.getFor(
        RenderComponent.class);

    public static RenderComponent get(Entity entity) {
        return mapper.get(entity);
    }

}
