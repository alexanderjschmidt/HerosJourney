package heros.journey.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;

import heros.journey.Application;
import heros.journey.GameCamera;
import heros.journey.utils.input.KeyManager;
import heros.journey.utils.art.ResourceManager;

public class SplashScreen implements Screen {

	private final Application app;
	private Stage stage;

	private Image pvg, bl, schmidt;

	public SplashScreen(final Application app) {
		this.app = app;
		this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), GameCamera.get()));
	}

	@Override
	public void show() {
		stage.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				app.setScreen(new MainMenuScreen(app));
				return false;
			}
		});
		Gdx.input.setInputProcessor(stage);

		Runnable transitionRunnable = new Runnable() {
			@Override
			public void run() {
				app.setScreen(new MainMenuScreen(app));
			}
		};

		pvg = new Image(ResourceManager.get().getTexture("Textures/splash/pvglogo.png"));
		pvg.setPosition(stage.getWidth() / 2 - pvg.getWidth() / 2, stage.getHeight() / 2 + pvg.getHeight() / 2);
		pvg.addAction(sequence(Actions.alpha(0), fadeIn(1.5f, Interpolation.pow2), delay(1.5f), fadeOut(1f)));
		bl = new Image(ResourceManager.get().getTexture("Textures/splash/badlogic.jpg"));
		bl.setPosition(stage.getWidth() / 2, stage.getHeight() / 2 - bl.getHeight());
		bl.addAction(sequence(Actions.alpha(0), delay(.2f), fadeIn(1.5f, Interpolation.pow2), delay(1.3f), fadeOut(1f)));
		schmidt = new Image(ResourceManager.get().getTexture("Textures/splash/aschmidtlogo.png"));
		schmidt.setPosition(stage.getWidth() / 2 - (schmidt.getWidth() * 3 / 2), stage.getHeight() / 2 - schmidt.getHeight());
		schmidt.addAction(sequence(Actions.alpha(0), delay(.4f), fadeIn(1.5f, Interpolation.pow2), delay(1.1f), fadeOut(1f), run(transitionRunnable)));

		stage.addActor(pvg);
		stage.addActor(bl);
		stage.addActor(schmidt);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		stage.draw();
	}

	public void update(float delta) {
		stage.act(delta);
		if (Gdx.input.isKeyPressed(KeyManager.ANY) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			app.setScreen(new MainMenuScreen(app));
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, false);
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
}
