package fe.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fe.game.ActionQueue;
import fe.game.Application;
import fe.game.Camera;
import fe.game.GameState;
import fe.game.entities.EntityClassManager;
import fe.game.entities.ai.AIManager;
import fe.game.entities.skills.SkillManager;
import fe.game.entities.skills.SkillManagerOld;
import fe.game.managers.InputManager;
import fe.game.managers.KeyManager;
import fe.game.tilemap.MapData;
import fe.game.tilemap.TileMap;
import fe.game.ui.HUD;

public class BattleScreen implements Screen {

	private Application app;
	private AIManager ai;
	private Camera camera;
	private SpriteBatch batch;

	private MapData mapData;
	private boolean ready = false;

	// quickStart constructor
	public BattleScreen(Application app, boolean quickStart) {
		this.app = app;
		this.mapData = new MapData((int) (Math.random() * 10000000), 32, 100, 4, false);
		startGame();
	}

	// server join game
	public BattleScreen(Application app) {
		this.app = app;
		this.mapData = ActionQueue.get().initSocket(false);
	}

	// server create game
	public BattleScreen(Application app, int seed, int mapSize, int armySize, int teamCount, boolean fogOfWar) {
		this.app = app;
		this.mapData = ActionQueue.get().initSocket((int) (Math.random() * 10000000), 16, 100, 4, false, true);
	}

	public void startGame() {
		batch = app.getBatch();
		camera = app.getCamera();

		SkillManager.get().createBaseSkills(this);
		SkillManagerOld.createSkills();
		EntityClassManager.get().createBuildings();

		GameState.global().init(mapData);

		ai = new AIManager();

		camera.load(GameState.global().getWidth() * TileMap.SIZE, GameState.global().getHeight() * TileMap.SIZE, HUD.get().getCursor());
		HUD.get().getCursor().setPosition(10, 15);

		ready = true;
		ActionQueue.get().checkLocked();
	}

	@Override
	public void render(float delta) {
		if (!ready) {
			if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE)) {
				app.setScreen(new MainMenuScreen(app));
			}
			return;
		}
		camera.update();
		app.getViewport().setCamera(camera);
		batch.setProjectionMatrix(camera.combined);

		ai.update(GameState.global(), delta);
		InputManager.get().update(delta);
		HUD.get().update(delta);

		batch.begin();
		GameState.global().render(batch, camera, delta);
		HUD.get().getCursor().render(batch, delta);
		batch.end();

		HUD.get().draw();
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
		if (!ready)
			return;
		HUD.get().resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		ActionQueue.get().dispose();
	}

	public Application getApp() {
		return app;
	}

}
