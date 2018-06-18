package fe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fe.game.Application;
import fe.game.utils.ResourceManager;

public class MapSettingScreen implements Screen {

	private final Application app;

	private Stage stage;

	public MapSettingScreen(final Application app) {
		this.app = app;
		this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(), app.getCamera()));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.clear();

		Image background = new Image(ResourceManager.get().getTexture(
				"Textures/UI/Background.png"));
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(background);
		Label label = new Label("RPGame", ResourceManager.get().skin, "title");
		label.setPosition(Gdx.graphics.getWidth() / 2 - (label.getWidth() / 2),
				Gdx.graphics.getHeight() / 2 - (label.getHeight() / 2) + 120);
		stage.addActor(label);
		initButtons();
	}

	private void initButtons() {
		// width, height, weather,
	}

	private void update(float delta) {
		stage.act(delta);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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
