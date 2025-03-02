package fe.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

import fe.game.tilemap.TileMap;
import fe.game.ui.Cursor;

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
	}

	private void updateLockedPos() {

		if (cursor.x * TileMap.SIZE - position.x < TileMap.SIZE * -20)
			position.x -= TileMap.SIZE;
		else if (cursor.x * TileMap.SIZE - position.x > TileMap.SIZE * 19)
			position.x += TileMap.SIZE;

		if (cursor.y * TileMap.SIZE - position.y < TileMap.SIZE * -11)
			position.y -= TileMap.SIZE;
		else if (cursor.y * TileMap.SIZE - position.y > TileMap.SIZE * 10)
			position.y += TileMap.SIZE;

	}
}
