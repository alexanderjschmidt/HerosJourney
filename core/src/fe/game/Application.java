package fe.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fe.game.screens.LoadingScreen;
import fe.game.utils.ResourceManager;

public class Application extends Game {
	public static final String TITLE = "Game";
	public static final float VERSION = .8f;

	private Camera camera;
	private Viewport viewport;
	private SpriteBatch batch;

	private String currentLoad;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new Camera();
		viewport = new StretchViewport(w, h, new OrthographicCamera());
		getCamera().setToOrtho(false, w, h);
		viewport.apply();
		batch = new SpriteBatch();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		ResourceManager.get().dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public Camera getCamera() {
		return camera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public Viewport getViewport() {
		return viewport;
	}

}
