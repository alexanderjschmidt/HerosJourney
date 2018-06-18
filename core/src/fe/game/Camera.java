package fe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import fe.game.tilemap.TileMap;
import fe.game.utils.KeyManager;

public class Camera extends OrthographicCamera {

	private Cursor cursor;
	private int mapWidth, mapHeight;

	public Camera() {
		super();
	}

	public void load(int mapWidth, int mapHeight, Cursor cursor) {
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		lockEntity(cursor);
	}

	public void lockEntity(Cursor pos) {
		this.cursor = pos;
	}

	@Override
	public void update() {
		super.update();
		if (cursor != null) {
			updateLockedPos();
			return;
		}
		if (Gdx.input.isKeyPressed(KeyManager.UP)) {
			position.y += 10;
		} else if (Gdx.input.isKeyPressed(KeyManager.DOWN)) {
			position.y -= 10;
		}
		if (Gdx.input.isKeyPressed(KeyManager.RIGHT)) {
			position.x += 10;
		} else if (Gdx.input.isKeyPressed(KeyManager.LEFT)) {
			position.x -= 10;
		}
	}

	private void updateLockedPos() {

		if (cursor.ex - position.x < TileMap.SIZE * -20)
			position.x -= TileMap.SIZE;
		else if (cursor.ex - position.x > TileMap.SIZE * 19)
			position.x += TileMap.SIZE;

		if (cursor.ey - position.y < TileMap.SIZE * -11)
			position.y -= TileMap.SIZE;
		else if (cursor.ey - position.y > TileMap.SIZE * 10)
			position.y += TileMap.SIZE;

	}
}
