package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.tilemap.TileMap;

public class AnimatedTile extends Tile {

	private Animation<Texture> animation;
	private Sprite sprite;

	public AnimatedTile(String name, TextureRegion[] textures, int terrainCost) {
		super(name, terrainCost);
		animation = new Animation(1f / textures.length, textures);
		sprite = new Sprite(textures[0]);
		this.sprite.setSize(GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	@Override
	public void render(Batch batch, TileMap map, float elapsedTime, int x, int y, int varianceVal) {
		sprite.setRegion(animation.getKeyFrame(elapsedTime, true));
		sprite.setPosition(x, y);
		sprite.setAlpha(1f);
		sprite.draw(batch);
	}

}
