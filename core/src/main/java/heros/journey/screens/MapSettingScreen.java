package heros.journey.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import heros.journey.Application;
import heros.journey.GameCamera;
import heros.journey.utils.input.KeyManager;
import heros.journey.utils.art.ResourceManager;

public class MapSettingScreen implements Screen {

	private final Application app;

	private Stage stage;

	private int mapSize = 8, armySize = 100, teamCount = 2;
	private String seed = "";
	private boolean fogOfWar = false;

	public MapSettingScreen(final Application app) {
		this.app = app;
		this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), GameCamera.get()));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.clear();

		Image background = new Image(ResourceManager.get().getTexture("Textures/UI/Background.png"));
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage.addActor(background);
		Label label = new Label("RPGame", ResourceManager.get().skin, "title");
		label.setPosition(Gdx.graphics.getWidth() / 2 - (label.getWidth() / 2), Gdx.graphics.getHeight() / 2 - (label.getHeight() / 2) + 120);
		stage.addActor(label);
		initButtons();
	}

	private void initButtons() {
		TextButton startGame = new TextButton("Start New Game", ResourceManager.get().skin);
		startGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println(seed + " " + app);
				app.setScreen(new BattleScreen(app, seed.equals("") ? (int) (Math.random() * 10000000) : seed.hashCode(), mapSize, armySize, teamCount, fogOfWar));
			}
		});

		Label seedLabel = new Label("Seed: ", ResourceManager.get().skin);
		Label seedOutput = new Label("", ResourceManager.get().skin);
		TextField seedInput = new TextField("", ResourceManager.get().skin);
		seedInput.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				seed = seedInput.getText();
			}
		});

		Label sizeLabel = new Label("Map Size: ", ResourceManager.get().skin);
		Label sizeOutput = new Label(mapSize + "", ResourceManager.get().skin);
		Slider sizeText = new Slider(3, 8, 1, false, ResourceManager.get().skin);
		sizeText.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				mapSize = (int) Math.pow(2, sizeText.getValue());
				sizeOutput.setText(mapSize + "");
			}
		});
		sizeText.setValue(5);

		Label battleScale = new Label("Army Size: ", ResourceManager.get().skin);
		Label battleScaleOutput = new Label(armySize + "", ResourceManager.get().skin);
		Slider battleScaleText = new Slider(100, 200, 10, false, ResourceManager.get().skin);
		battleScaleText.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				armySize = (int) battleScaleText.getValue();
				battleScaleOutput.setText(armySize + "");
			}
		});

		Label teamsText = new Label("Number of Teams: ", ResourceManager.get().skin);
		Label teamsOutput = new Label(teamCount + "", ResourceManager.get().skin);
		Slider teams = new Slider(2, 4, 1, false, ResourceManager.get().skin);
		teams.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				teamCount = (int) teams.getValue();
				teamsOutput.setText(teamCount + "");
			}
		});

		Label fogLabel = new Label("Fog of War: ", ResourceManager.get().skin);
		Label fogOutput = new Label("", ResourceManager.get().skin);
		CheckBox fogBox = new CheckBox("", ResourceManager.get().skin);
		fogBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				fogOfWar = fogBox.isChecked();
				sizeOutput.setText("");
			}
		});
		fogBox.setChecked(true);

		Table table = new Table();
		table.add(startGame);
		table.row();
		table.add(seedLabel);
		table.add(seedOutput);
		table.add(seedInput).width(100);
		table.row();
		table.add(sizeLabel);
		table.add(sizeOutput);
		table.add(sizeText).width(100);
		table.row();
		table.add(battleScale);
		table.add(battleScaleOutput);
		table.add(battleScaleText).width(100);
		table.row();
		table.add(teamsText);
		table.add(teamsOutput);
		table.add(teams).width(100);
		table.row();
		table.add(fogLabel);
		table.add(fogOutput);
		table.add(fogBox).width(100);

		table.setFillParent(true);
		stage.addActor(table);
	}

	private void update(float delta) {
		stage.act(delta);
		if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE)) {
			app.setScreen(new MainMenuScreen(app));
		}
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
	}

	@Override
	public void dispose() {
	}

}
