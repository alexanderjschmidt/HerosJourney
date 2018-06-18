package fe.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fe.game.Application;
import fe.game.Camera;
import fe.game.Cursor;
import fe.game.entities.Entity;
import fe.game.entities.Species;
import fe.game.tilemap.TileMap;

public class BattleScreen implements Screen {

	private TileMap map;
	private Cursor cursor;
	private Application app;
	private Camera camera;
	private SpriteBatch batch;

	private EntityManager entities;

	public BattleScreen(Application app) {
		this.app = app;
		map = new TileMap();
		batch = app.getBatch();
		camera = app.getCamera();
		entities = new EntityManager(map);
		cursor = new Cursor(map, entities);
	}

	@Override
	public void show() {
		camera.load(map.getWidth() * TileMap.SIZE, map.getHeight()
				* TileMap.SIZE, cursor);
		Species grunt = new Species("Goblin Grunt");
		entities.addEntity(new Entity(grunt), 10, 10);

		entities.addEntity(new Entity(grunt), 14, 14);
	}

	@Override
	public void render(float delta) {
		cursor.update();
		camera.update();
		app.getViewport().setCamera(camera);
		batch.setProjectionMatrix(camera.combined);

		entities.update(delta);

		batch.begin();
		map.render(batch, camera.position.x / TileMap.SIZE, camera.position.y
				/ TileMap.SIZE, delta);
		entities.render(batch, camera.position.x / TileMap.SIZE,
				camera.position.y / TileMap.SIZE, delta);
		cursor.render(batch, delta);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
