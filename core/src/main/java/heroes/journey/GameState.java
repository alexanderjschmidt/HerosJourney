package heroes.journey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heroes.journey.entities.Character;
import heroes.journey.entities.EntityManager;
import heroes.journey.entities.actions.Action;
import heroes.journey.entities.actions.ActionQueue;
import heroes.journey.entities.actions.QueuedAction;
import heroes.journey.entities.actions.TargetAction;
import heroes.journey.entities.factions.Faction;
import heroes.journey.initializers.Initializer;
import heroes.journey.tilemap.MapData;
import heroes.journey.tilemap.TileMap;
import heroes.journey.ui.HUD;
import heroes.journey.utils.RangeManager;
import heroes.journey.utils.ai.pathfinding.Cell;

public class GameState {

	private int width, height;
	private EntityManager entities;
	private TileMap map;
	private RangeManager rangeManager;

	private int turn;

	private static GameState gameState;
    private List<Character> entitiesInActionOrder;

	public static GameState global() {
		if (gameState == null)
			gameState = new GameState();
		return gameState;
	}

	private GameState() {
        entitiesInActionOrder = new ArrayList<>();
	}

	private GameState(int width, int height) {
        this();
        this.width = width;
        this.height = height;
	}

	public void init(MapData mapData) {
        Initializer.init();

        this.width = mapData.getMapSize();
        this.height = mapData.getMapSize();
        map = new TileMap(width);
		entities = new EntityManager(this, width, height);
		rangeManager = new RangeManager(this, width, height);

		turn = 0;
	}

	public GameState clone() {
		GameState clone = new GameState(width, height);
		clone.map = map.clone(clone);
		clone.entities = entities.clone(clone);
		clone.rangeManager = rangeManager.clone(clone);
		clone.turn = turn;
		return clone;
	}

	public GameState applyAction(QueuedAction queuedAction) {
		Cell path = queuedAction.getPath();
		Action action = queuedAction.getAction();

		Character e = getEntities().get(path.i, path.j);
		if (e != null) {
			e.remove();
			while (path.parent != null) {
				path = path.parent;
			}
			getEntities().addEntity(e, path.i, path.j);
		}

		HUD.get().getCursor().setSelected(e);
		if (action instanceof TargetAction targetAction) {
            targetAction.targetEffect(this, e, queuedAction.getTargetX(), queuedAction.getTargetY());
		} else {
			action.onSelect(this, e);
		}
        incrementTurn();
        return this;
	}

	public void render(SpriteBatch batch, float delta) {
		map.render(batch, GameCamera.get().position.x / GameCamera.get().getSize(), GameCamera.get().position.y / GameCamera.get().getSize(), delta);
		entities.render(batch, GameCamera.get().position.x / GameCamera.get().getSize(), GameCamera.get().position.y / GameCamera.get().getSize(), delta);
		rangeManager.render(batch, turn);
	}

    private Character incrementTurn() {
        if (entitiesInActionOrder == null || entitiesInActionOrder.isEmpty()){
            entitiesInActionOrder = entities.getEntitiesInActionOrder();
            turn++;
        }
        Character currentCharacter = entitiesInActionOrder.removeFirst();
        entities.setCurrentEntity(currentCharacter);
        return currentCharacter;
    }

    public void nextTurn() {
        Character currentCharacter = incrementTurn();
        System.out.println("turn " + turn + ", " + entitiesInActionOrder);
        QueuedAction action = currentCharacter.getAI().getMove(this, currentCharacter);
        if (action == null) {
            Optional<Faction> currentFaction = currentCharacter.getFactions().stream().filter(Faction::isPlayerFaction).findFirst();
            if (currentFaction.isEmpty())
                throw new RuntimeException("If Character has the Player AI, it MUST have a Player Faction");
            if(currentFaction.get().toString().equals(ActionQueue.get().getID())){
                HUD.get().getCursor().setPosition(currentCharacter);
                HUD.get().setState(HUD.HUDState.CURSOR_MOVE);
                System.out.println("your turn");
            } else {
                HUD.get().setState(HUD.HUDState.LOCKED);
                System.out.println("opponent turn");
            }
        } else {
            HUD.get().setState(HUD.HUDState.LOCKED);
            ActionQueue.get().addAction(action);
            System.out.println("ai turn");
        }
        getRangeManager().clearRange();
        HUD.get().getCursor().clearSelected();
	}

	public int getTurn() {
		return turn;
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
