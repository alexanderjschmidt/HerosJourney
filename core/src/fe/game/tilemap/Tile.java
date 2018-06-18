package fe.game.tilemap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Tile {

	public final boolean solid;

	public Tile(boolean solid) {
		this.solid = solid;
	}

	public abstract void render(SpriteBatch batch, float x, float y, float elapsedTime, float alpha);

}
