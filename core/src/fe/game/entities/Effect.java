package fe.game.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import fe.game.tilemap.TileMap;

public class Effect {

	public static final float SCALE = 2f;
	public static final float OFFSET = (TileMap.SIZE / 2f) - (TileMap.SIZE * SCALE / 2f);
	private float x, y;
	private Vector2 v;
	private Animation ani;
	private float elapsedTime;
	private boolean angle;

	public Effect(TextureRegion[] frames, float x, float y) {
		this(frames, x, y, x, y, false);
	}

	public Effect(TextureRegion[] frames, float x, float y, float endx, float endy, boolean angle) {
		this.ani = new Animation(.5f / frames.length, frames);
		this.x = x;
		this.y = y;
		elapsedTime = 0;
		this.v = new Vector2(endx - x, endy - y);
		this.angle = angle;
	}

	public boolean render(Batch batch, float delta) {
		elapsedTime += delta;
		x += v.x * 2 * delta;
		y += v.y * 2 * delta;
		batch.draw(ani.getKeyFrame(elapsedTime), x, y, TileMap.SIZE / 2, TileMap.SIZE / 2, TileMap.SIZE, TileMap.SIZE, SCALE, SCALE, angle ? v.angle() : 90, true);
		return ani.isAnimationFinished(elapsedTime);
	}

}
