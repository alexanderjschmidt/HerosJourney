package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderComponent implements Component {

    private TextureRegion sprite;

    public RenderComponent(TextureRegion sprite) {
        this.sprite = sprite;
    }

    public TextureRegion getSprite() {
        return sprite;
    }
    
}
