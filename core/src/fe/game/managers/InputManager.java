package fe.game.managers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fe.game.ActionQueue;
import fe.game.GameState;
import fe.game.entities.skills.SkillManager;
import fe.game.entities.skills.TargetSkill;
import fe.game.ui.Cursor;
import fe.game.ui.HUD;
import fe.game.ui.HUD.HUDState;
import fe.game.utils.AStar;
import fe.game.utils.Cell;

public class InputManager {

	private Cursor cursor;
	private GameState gameState;

	private float holdElapsedX, holdElapsedY;

	public Cell pathHolder;

	private static InputManager inputManager;

	public static InputManager get() {
		if (inputManager == null)
			inputManager = new InputManager();
		return inputManager;
	}

	private InputManager() {
		cursor = HUD.get().getCursor();
		gameState = GameState.global();
	}

	private void savePath() {
		Cursor cursor = HUD.get().getCursor();
		Cell path = AStar.reversePath(cursor.getPath());
		Cell temp = null;
		Cell holder = null;
		while (path != null) {
			Cell temp2 = temp;
			Cell holder2 = holder;
			temp = new Cell(path.i, path.j);
			temp.parent = temp2;
			holder = new Cell(path.i, path.j);
			holder.parent = holder2;
			path = path.parent;
		}
		cursor.setPath(temp);
		if (holder == null) {
			if (cursor.getSelected() != null)
				holder = new Cell(cursor.getSelected().getXCoord(), cursor.getSelected().getYCoord());
			else
				holder = new Cell(cursor.x, cursor.y);
		}
		pathHolder = holder;
	}

	public void update(float delta) {
		if (Gdx.input.isKeyJustPressed(KeyManager.DEVMODE)) {
			gameState.getMap().blend = !gameState.getMap().blend;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT_BRACKET)) {
			gameState.getMap().setSeed((int) (Math.random() * 10000000));
			gameState.getMap().genNewMap();
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.SHOW_JOB_INFO)) {
			HUD.get().getJobUI().toggle();
		}
		switch (HUD.get().getState()) {
		case CURSOR_MOVE:
			updateMoveState(delta);
			break;
		case TARGET:
			updateAttackState(delta);
			break;
		case ACTION_SELECT:
			updateActionMenu();
			break;
		case LOCKED:
			updateFreeMove(delta);
			break;
		}
	}

	private void updateAttackState(float delta) {
		if (cursor.getSelected() != null) {
			if (cursor.getActiveSkill().shouldTargetEntity()) {
				if (Gdx.input.isKeyJustPressed(KeyManager.UP) || Gdx.input.isKeyJustPressed(KeyManager.RIGHT)) {
					gameState.getRangeManager().pointAtTarget(1);
					HUD.get().getCombatUI().setMessage(cursor.getActiveSkill().getUIMessage(gameState, cursor.getSelected(), cursor.x, cursor.y));
				} else if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) || Gdx.input.isKeyJustPressed(KeyManager.LEFT)) {
					gameState.getRangeManager().pointAtTarget(-1);
					HUD.get().getCombatUI().setMessage(cursor.getActiveSkill().getUIMessage(gameState, cursor.getSelected(), cursor.x, cursor.y));
				}
			} else {
				updateFreeMove(delta);
			}
			if (Gdx.input.isKeyJustPressed(KeyManager.SELECT)) {
				sendAction();
				cursor.getActiveSkill().targetEffect(gameState, cursor.getSelected(), cursor.x, cursor.y);
				HUD.get().setState(HUDState.CURSOR_MOVE);
			} else if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE) || Gdx.input.isKeyJustPressed(KeyManager.BACK)) {
				cursor.revertAction();
			}
		}

	}

	private void updateMoveState(float delta) {
		if ((Gdx.input.isKeyJustPressed(KeyManager.ESCAPE) || Gdx.input.isKeyJustPressed(KeyManager.BACK)) && cursor.getSelected() != null) {
			cursor.clearSelected(false);
		} else if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE) || Gdx.input.isKeyJustPressed(KeyManager.BACK)) {
			HUD.get().getActionMenu().open(SkillManager.get().getTeamSkills(gameState, cursor.x, cursor.y));
		} else if (Gdx.input.isKeyJustPressed(KeyManager.SELECT)) {
			if (cursor.getSelected() != null) {
				savePath();
				cursor.moveSelected();
			} else if (cursor.getHover() != null && cursor.getHover().getTeam() == gameState.getActiveTeam() && !cursor.getHover().used && gameState.getTurn() != 0) {
				cursor.setSelectedtoHover();
				HUD.get().select();
				if (cursor.getHover().getEntityClass().getMoveDistance() == 0) {
					savePath();
					cursor.getSelected().openActionMenu();
				} else {
					gameState.getRangeManager().setMoveAndAttackRange(cursor.getSelected());
				}
			} else if (cursor.getHover() == null) {
				savePath();
				HUD.get().getActionMenu().open(SkillManager.get().getTeamSkills(gameState, cursor.x, cursor.y));
			}
		} else if (Gdx.input.isKeyJustPressed(KeyManager.NEXT_CHARACTER)) {
			gameState.getEntities().nextCharacter(cursor.x, cursor.y);
		}
		updateFreeMove(delta);
	}

	private void updateFreeMove(float delta) {
		if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && cursor.y > 0) {
			holdElapsedY = 0;
			cursor.y--;
		} else if (Gdx.input.isKeyJustPressed(KeyManager.UP) && cursor.y < gameState.getHeight() - 1) {
			holdElapsedY = 0;
			cursor.y++;
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.LEFT) && cursor.x > 0) {
			holdElapsedX = 0;
			cursor.x--;
		} else if (Gdx.input.isKeyJustPressed(KeyManager.RIGHT) && cursor.x < gameState.getWidth() - 1) {
			holdElapsedX = 0;
			cursor.x++;
		}
		if (Gdx.input.isKeyPressed(KeyManager.DOWN) && cursor.y > 0) {
			holdElapsedY += delta;
			if (holdElapsedY >= .1) {
				holdElapsedY = 0;
				cursor.y--;
			}
		} else if (Gdx.input.isKeyPressed(KeyManager.UP) && cursor.y < gameState.getHeight() - 1) {
			holdElapsedY += delta;
			if (holdElapsedY >= .1) {
				holdElapsedY = 0;
				cursor.y++;
			}
		}
		if (Gdx.input.isKeyPressed(KeyManager.RIGHT) && cursor.x < gameState.getWidth() - 1) {
			holdElapsedX += delta;
			if (holdElapsedX >= .1) {
				holdElapsedX = 0;
				cursor.x++;
			}
		} else if (Gdx.input.isKeyPressed(KeyManager.LEFT) && cursor.x > 0) {
			holdElapsedX += delta;
			if (holdElapsedX >= .1) {
				holdElapsedX = 0;
				cursor.x--;
			}
		}
	}

	private void updateActionMenu() {
		if (Gdx.input.isKeyJustPressed(KeyManager.UP)) {
			HUD.get().getActionMenu().increment();
		} else if (Gdx.input.isKeyJustPressed(KeyManager.DOWN)) {
			HUD.get().getActionMenu().decrement();
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.SELECT)) {
			if (!(HUD.get().getActionMenu().getSelected() instanceof TargetSkill)) {
				sendAction();
			} else {
				HUD.get().getCursor().setActiveSkill((TargetSkill) HUD.get().getActionMenu().getSelected());
			}
			HUD.get().getActionMenu().select();
		} else if (Gdx.input.isKeyJustPressed(KeyManager.ESCAPE) || Gdx.input.isKeyJustPressed(KeyManager.BACK)) {
			HUD.get().setState(HUDState.CURSOR_MOVE);
			cursor.revertSelectedPosition();
		}
	}

	private void sendAction() {
		Cell temp = pathHolder;
		if (temp == null) {
			temp = new Cell(cursor.x, cursor.y);
		}
		List<Integer> xCoords = new ArrayList<Integer>();
		List<Integer> yCoords = new ArrayList<Integer>();
		while (temp != null) {
			xCoords.add(temp.i);
			yCoords.add(temp.j);
			// System.out.println(temp.i + ", " + temp.j);
			temp = temp.parent;
		}
		// System.out.println(HUD.get().getActionMenu().getSelected());
		// System.out.println(HUD.get().getCursor().x + ", " +
		// HUD.get().getCursor().y);
		pathHolder = null;
		JSONObject gameAction = new JSONObject();
		try {
			gameAction.put("skill", HUD.get().getActionMenu().getSelected());
			gameAction.put("targetX", cursor.x);
			gameAction.put("targetY", cursor.y);
			gameAction.put("xCoords", xCoords);
			gameAction.put("yCoords", yCoords);
		} catch (JSONException e) {
			System.out.println(e);
		}
		ActionQueue.get().sendAction(gameAction);
	}

}
