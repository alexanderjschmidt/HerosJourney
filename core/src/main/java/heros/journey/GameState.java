package heros.journey;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heros.journey.entities.items.ItemManager;
import heros.journey.initializers.BaseClass;
import heros.journey.entities.Entity;
import heros.journey.entities.EntityManager;
import heros.journey.entities.Team;
import heros.journey.entities.actions.Action;
import heros.journey.entities.actions.QueuedAction;
import heros.journey.entities.actions.TargetAction;
import heros.journey.entities.ai.AI;
import heros.journey.initializers.BaseItem;
import heros.journey.tilemap.MapData;
import heros.journey.tilemap.TileMap;
import heros.journey.ui.HUD;
import heros.journey.utils.RangeManager;
import heros.journey.utils.pathfinding.Cell;

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
        map = new TileMap(width, mapData.getSeed());
		entities = new EntityManager(this, width, height);
		rangeManager = new RangeManager(this, width, height);

		activeTeams = new ArrayList<Team>(mapData.getTeamCount());
        Team playerTeam = new Team("Player", 0, this, false);
		activeTeams.add(playerTeam);
		for (int i = 1; i < mapData.getTeamCount(); i++) {
			activeTeams.add(new Team("" + i, i, this, true));
		}

		activeTeam = 0;
		turn = 0;

		for (Team team : activeTeams) {
			if (team.isAI()) {
				team.setAI(new AI());
			}
		}

        Entity player = new Entity(BaseClass.hero, playerTeam, this);
        player.getInventory().add(BaseItem.wood);
        player.getInventory().add(BaseItem.ironOre);
        entities.addEntity(player, 16, 16);
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

	public GameState applyAction(QueuedAction action) {
		GameState state = this.clone();
		Cell path = action.getPath();
		Action skill = action.getAction();
		// System.out.println("Skill: " + skill);
		// System.out.println("Path: " + path.i + ", " + path.j);
		// System.out.println("Target: " + action.getTargetX() + ", " +
		// action.getTargetY());
		if (state.getEntities().getFog(path.i, path.j) == null) {
			// AStar.printPath(path);
			// state.entities.print();
			Entity e = new Entity(path.i, path.j);
			action.getAction().onSelect(state, e);
			return state;
		}
		Entity e = state.getEntities().getFog(path.i, path.j);
		if (e == null) {
			e.remove();
			while (path.parent != null) {
				path = path.parent;
			}
			state.getEntities().addEntity(e, path.i, path.j);
		}

		HUD.get().getCursor().setSelected(e);
		// System.out.println(e);
		if (skill instanceof TargetAction) {
			// System.out.println("target " + action.getTargetX() + ", " +
			// action.getTargetY());
			TargetAction s = (TargetAction) skill;
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
		int score = entities.getScore(team);
		// System.out.println("Score: " + score);
		return score;
	}

	public void render(SpriteBatch batch, float delta) {
		map.render(batch, GameCamera.get().position.x / GameCamera.get().getSize(), GameCamera.get().position.y / GameCamera.get().getSize(), delta);
		entities.render(batch, GameCamera.get().position.x / GameCamera.get().getSize(), GameCamera.get().position.y / GameCamera.get().getSize(), delta);
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

    public void endTurn() {
        if (!entities.anyUnitAvailable()) {
            nextTurn();
        }
    }
}
