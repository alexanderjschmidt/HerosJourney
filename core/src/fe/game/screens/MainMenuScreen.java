package fe.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import fe.game.Application;
import fe.game.utils.ResourceManager;

public class MainMenuScreen implements Screen {

	private final Application app;

	private Stage stage;

	public MainMenuScreen(final Application app) {
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

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	private void initButtons() {

		TextButton buttonPlay = new TextButton("New Game",
				ResourceManager.get().skin, "default");
		buttonPlay.setSize(280, 60);
		buttonPlay.setPosition(
				Gdx.graphics.getWidth() / 2 - (buttonPlay.getWidth() / 2),
				Gdx.graphics.getHeight() / 2 - (buttonPlay.getHeight() / 2));
		buttonPlay.addAction(sequence(
				alpha(0),
				parallel(fadeIn(.5f),
						moveBy(0, -20, .5f, Interpolation.pow5Out))));
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				app.setScreen(new BattleScreen(app));
			}
		});

		TextButton buttonLoad = new TextButton("Load Game",
				ResourceManager.get().skin, "default");
		buttonLoad.setSize(280, 60);
		buttonLoad.setPosition(
				Gdx.graphics.getWidth() / 2 - (buttonLoad.getWidth() / 2),
				Gdx.graphics.getHeight() / 2 - (buttonLoad.getHeight() / 2)
						- 70);
		buttonLoad.addAction(sequence(
				alpha(0),
				parallel(fadeIn(.5f),
						moveBy(0, -20, .5f, Interpolation.pow5Out))));
		buttonLoad.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// app.load();
			}
		});

		TextButton buttonExit = new TextButton("Exit",
				ResourceManager.get().skin, "default");
		buttonExit.setSize(280, 60);
		buttonExit.setPosition(
				Gdx.graphics.getWidth() / 2 - (buttonExit.getWidth() / 2),
				Gdx.graphics.getHeight() / 2 - (buttonExit.getHeight() / 2)
						- 140);
		buttonExit.addAction(sequence(
				alpha(0),
				parallel(fadeIn(.5f),
						moveBy(0, -20, .5f, Interpolation.pow5Out))));
		buttonExit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		TextButton buttonTemplate = new TextButton("Create Map",
				ResourceManager.get().skin, "default");
		buttonTemplate.setSize(280, 60);
		buttonTemplate.setPosition(Gdx.graphics.getWidth() / 2
				- (buttonTemplate.getWidth() / 2), Gdx.graphics.getHeight() / 2
				- (buttonTemplate.getHeight() / 2) - 210);
		buttonTemplate.addAction(sequence(
				alpha(0),
				parallel(fadeIn(.5f),
						moveBy(0, -20, .5f, Interpolation.pow5Out))));
		buttonTemplate.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// app.setScreen(new TemplateScreen());
			}
		});

		stage.addActor(buttonPlay);
		stage.addActor(buttonLoad);
		stage.addActor(buttonExit);
		stage.addActor(buttonTemplate);
	}
}
