package heroes.journey.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import heroes.journey.Application;
import heroes.journey.GameCamera;
import heroes.journey.utils.art.ResourceManager;

public class LoadingScreen implements Screen {

	private final Application app;

	private ShapeRenderer shapeRenderer;
	private float progress;

	public LoadingScreen(final Application app) {
		this.app = app;
		this.shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void show() {
		shapeRenderer.setProjectionMatrix(GameCamera.get().combined);
		this.progress = 0f;
	}

	private void update(float delta) {
		progress = MathUtils.lerp(progress, ResourceManager.get().getProgress(), .1f);
		if (ResourceManager.get().update() && progress >= ResourceManager.get().getProgress() - .001f) {
			ResourceManager.get().splits();
			app.setScreen(new SplashScreen(app));
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.BLUE);
		shapeRenderer.rect(32, GameCamera.get().viewportHeight / 2 - 8, GameCamera.get().viewportWidth - 64, 64);

		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(32, GameCamera.get().viewportHeight / 2 - 8, progress * (GameCamera.get().viewportWidth - 64), 64);

		shapeRenderer.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		// dispose();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}
