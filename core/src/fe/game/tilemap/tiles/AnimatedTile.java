package fe.game.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.tilemap.TileMap;

public class AnimatedTile extends Tile {

	private Animation animation;
	private Sprite sprite;

	public AnimatedTile(String name, TextureRegion[] textures, int terrainCost) {
		super(name, terrainCost);
		animation = new Animation(1f / textures.length, textures);
		sprite = new Sprite(textures[0]);
		this.sprite.setSize(TileMap.SIZE, TileMap.SIZE);
	}

	@Override
	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		sprite.setRegion(animation.getKeyFrame(elapsedTime, true));
		sprite.setPosition(x, y);
		sprite.setAlpha(1f);
		sprite.draw(batch);
	}

}
