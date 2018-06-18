package fe.game.tilemap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedTile extends Tile {

	private Animation animation;
	private Sprite sprite;

	public AnimatedTile(TextureRegion[] textures, boolean solid) {
		super(solid);
		animation = new Animation(1f / textures.length, textures);
		sprite = new Sprite(textures[0]);
	}

	@Override
	public void render(SpriteBatch batch, float x, float y, float elapsedTime,
			float alpha) {
		sprite.setRegion(animation.getKeyFrame(elapsedTime, true));
		sprite.setPosition(x, y);
		sprite.setAlpha(alpha);
		sprite.draw(batch);
	}

}
