package fe.game.ui;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fe.game.GameState;
import fe.game.entities.Entity;
import fe.game.entities.EntityClass;

public class HUD extends Stage {

	public enum HUDState {
		CURSOR_MOVE, MOVING, ACTION_SELECT, TARGET, LOCKED;
	}

	public static final int FONT_SIZE = 24;

	private Cursor cursor;

	private HUDState state = HUDState.LOCKED;
	private ActionMenu actionMenu;
	private TerrainUI terrainUI;
	private EntityUI entityUI, selectedEntityUI;
	private CombatUI combatUI;
	private TurnUI turnUI;
	private JobInfoUI jobInfoUI;

	private static HUD hud;

	public static HUD get() {
		if (hud == null)
			hud = new HUD();
		return hud;
	}

	public HUD() {
		super(new ScreenViewport());
		cursor = new Cursor(this);
		actionMenu = new ActionMenu(this);
		terrainUI = new TerrainUI();
		entityUI = new EntityUI();
		combatUI = new CombatUI();
		turnUI = new TurnUI();
		jobInfoUI = new JobInfoUI();
		this.addActor(actionMenu);
		this.addActor(terrainUI);
		this.addActor(entityUI);
		this.addActor(combatUI);
		this.addActor(turnUI);
		this.addActor(jobInfoUI);
	}

	public void update(float delta) {
		cursor.update(delta);
		act();
		if (getState() == HUD.HUDState.ACTION_SELECT) {
			actionMenu.setVisible(true);
		} else {
			actionMenu.setVisible(false);
		}
		terrainUI.update(cursor.x, cursor.y);
		entityUI.update(cursor);
		if (selectedEntityUI != null)
			selectedEntityUI.update(cursor);
		if (getState() == HUD.HUDState.TARGET) {
			combatUI.setVisible(true);
		} else {
			combatUI.setVisible(false);
		}
		if (getState() == HUD.HUDState.LOCKED) {
			turnUI.minimal = true;
		} else {
			turnUI.minimal = false;
		}
		if (jobInfoUI.toggled()) {
			Entity e = GameState.global().getEntities().get(cursor.x, cursor.y);
			if (e == null)
				e = GameState.global().getEntities().getBuilding(cursor.x, cursor.y);
			showJobInfo(e != null ? e.getEntityClass() : null);
		}
		turnUI.update();
	}

	public void showJobInfo(EntityClass job) {
		jobInfoUI.setVisible(true);
		jobInfoUI.update(job);
	}

	public void hideJobInfoUI() {
		jobInfoUI.setVisible(false);
	}

	public void select() {
		selectedEntityUI = entityUI;
		selectedEntityUI.addAction(Actions.moveBy(0, 24 + HUD.FONT_SIZE * 5, .2f, Interpolation.linear));
		entityUI = new EntityUI();
		this.addActor(entityUI);
	}

	public void clearSelect() {
		if (selectedEntityUI != null)
			selectedEntityUI.addAction(Actions.sequence(Actions.fadeOut(.5f, Interpolation.pow5Out), Actions.delay(1f), Actions.removeActor()));
		selectedEntityUI = null;
	}

	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}

	public ActionMenu getActionMenu() {
		return actionMenu;
	}

	public CombatUI getCombatUI() {
		return combatUI;
	}

	public JobInfoUI getJobUI() {
		return jobInfoUI;
	}

	public HUD.HUDState getState() {
		return state;
	}

	public void setState(HUD.HUDState state) {
		this.state = state;
	}

	public Cursor getCursor() {
		return cursor;
	}

}
