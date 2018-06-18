package fe.game.tilemap;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlainTile extends Tile {

	private Sprite texture;

	public PlainTile(TextureRegion texture, boolean solid) {
		super(solid);
		this.texture = new Sprite(texture);
	}

	@Override
	public void render(SpriteBatch batch, float x, float y, float elapsedTime,
			float alpha) {
		texture.setAlpha(alpha);
		texture.setPosition(x, y);
		texture.draw(batch);
	}

}
