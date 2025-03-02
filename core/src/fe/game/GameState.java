package fe.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fe.game.entities.Entity;
import fe.game.entities.EntityManager;
import fe.game.entities.Team;
import fe.game.entities.ai.AI;
import fe.game.entities.skills.Skill;
import fe.game.entities.skills.TargetSkill;
import fe.game.managers.RangeManager;
import fe.game.tilemap.MapData;
import fe.game.tilemap.TileMap;
import fe.game.tilemap.Tileset;
import fe.game.ui.HUD;
import fe.game.utils.Cell;
import fe.game.utils.Direction;
import fe.game.utils.GameAction;

public class GameState {

	private int width, height;
	private EntityManager entities;
	private TileMap map;
	private RangeManager rangeManager;

	private int activeTeam;
	private ArrayList<Team> activeTeams;
	private int turn;

	private static GameState gameState;

	public static GameState global() {
		if (gameState == null)
			gameState = new GameState();
		return gameState;
	}

	private GameState() {

	}

	private GameState(int width, int height) {
		init(width, height);
	}

	public void init(MapData mapData) {
		init(mapData.getMapSize(), mapData.getMapSize());
		map = new TileMap(new Tileset(), width, height, mapData.getSeed());
		entities = new EntityManager(this, width, height);
		rangeManager = new RangeManager(this, width, height);

		activeTeams = new ArrayList<Team>(mapData.getTeamCount());
		activeTeams.add(new Team("Player", 0, this, false, Direction.getDirectionWithClockPos((0 * 2) + 1)));
		for (int i = 1; i < mapData.getTeamCount(); i++) {
			activeTeams.add(new Team("" + i, i, this, true, Direction.getDirectionWithClockPos((i * 2) + 1)));
		}

		activeTeam = 0;
		turn = 0;

		for (Team team : activeTeams) {
			if (team.isAI()) {
				team.setAI(new AI());
			}
		}
	}

	private void init(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public GameState clone() {
		GameState clone = new GameState(width, height);
		clone.activeTeams = new ArrayList<Team>(activeTeams.size());
		for (Team t : activeTeams) {
			clone.activeTeams.add(t.clone(clone));
		}
		clone.map = map.clone(clone);
		clone.entities = entities.clone(clone);
		clone.rangeManager = rangeManager.clone(clone);
		clone.activeTeam = activeTeam;
		clone.turn = turn;
		return clone;
	}

	public GameState applyAction(GameAction action) {
		GameState state = this.clone();
		Cell path = action.getPath();
		Skill skill = action.getSkill();
		// System.out.println("Skill: " + skill);
		// System.out.println("Path: " + path.i + ", " + path.j);
		// System.out.println("Target: " + action.getTargetX() + ", " +
		// action.getTargetY());
		if (state.getEntities().getFog(path.i, path.j) == null && state.getEntities().getBuilding(path.i, path.j) == null) {
			// AStar.printPath(path);
			// state.entities.print();
			Entity e = new Entity(path.i, path.j);
			action.getSkill().onSelect(state, e);
			return state;
		}
		Entity e = state.getEntities().getFog(path.i, path.j);
		if (e == null) {
			e = state.getEntities().getBuilding(path.i, path.j);
		} else {
			e.remove();
			while (path.parent != null) {
				path = path.parent;
			}
			state.getEntities().addEntity(e, path.i, path.j);
		}

		HUD.get().getCursor().setSelected(e);
		// System.out.println(e);
		if (skill instanceof TargetSkill) {
			// System.out.println("target " + action.getTargetX() + ", " +
			// action.getTargetY());
			TargetSkill s = (TargetSkill) skill;
			// System.out.println(state.getEntities().get(action.getTargetX(),
			// action.getTargetY()));
			s.targetEffect(state, e, action.getTargetX(), action.getTargetY());
		} else {
			// System.out.println("onspot");
			skill.onSelect(state, e);
		}
		return state;
	}

	public int getScore(Team team) {
		int score = team.morale + entities.getScore(team);
		// System.out.println("Score: " + score);
		return score;
	}

	public void render(SpriteBatch batch, Camera camera, float delta) {
		map.render(batch, camera.position.x / TileMap.SIZE, camera.position.y / TileMap.SIZE, delta);
		entities.render(batch, camera.position.x / TileMap.SIZE, camera.position.y / TileMap.SIZE, delta);
		rangeManager.render(batch, turn, getActiveTeam());
	}

	public void removeTeam(String pid) {
		for (int i = 0; i < activeTeams.size(); i++) {
			if (activeTeams.get(i).getID().equals(pid)) {
				if (i == activeTeam) {
					nextTurn();
				}
				getEntities().removeTeam(activeTeams.remove(i));
				activeTeam = activeTeam % activeTeams.size();
				break;
			}
		}
		for (int i = 0; i < activeTeams.size(); i++) {
			activeTeams.get(i).setArrayID(i);
		}
	}

	public void removeTeam(Team t) {
		this.removeTeam(t.getID());
	}

	public void nextTurn() {
		activeTeam = (activeTeam + 1) % activeTeams.size();
		if (activeTeam == 0) {
			turn++;
			getEntities().updateMorale();
			for (Team t : activeTeams) {
				if (t.morale > 80)
					t.adjustMorale(-5);
				else if (t.morale > 75)
					t.adjustMorale(75 - t.morale);
				else if (t.morale < 45 && t.morale > 25)
					t.adjustMorale(5);
				else if (t.morale < 50 && t.morale > 25)
					t.adjustMorale(50 - t.morale);
			}
		}
		getEntities().nextTurn();
		getRangeManager().clearRange();
		ActionQueue.get().checkLocked();
	}

	public int getTurn() {
		return turn;
	}

	public Team getActiveTeam() {
		return activeTeams.get(activeTeam);
	}

	public ArrayList<Team> getTeams() {
		return activeTeams;
	}

	public EntityManager getEntities() {
		return entities;
	}

	public TileMap getMap() {
		return map;
	}

	public RangeManager getRangeManager() {
		return rangeManager;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
