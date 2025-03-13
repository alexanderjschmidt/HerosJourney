package heroes.journey;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heroes.journey.components.AIComponent;
import heroes.journey.components.PlayerComponent;
import heroes.journey.entities.EntityManager;
import heroes.journey.entities.actions.Action;
import heroes.journey.entities.actions.ActionQueue;
import heroes.journey.entities.actions.QueuedAction;
import heroes.journey.entities.actions.TargetAction;
import heroes.journey.initializers.Initializer;
import heroes.journey.systems.FactionSystem;
import heroes.journey.systems.GameEngine;
import heroes.journey.tilemap.MapData;
import heroes.journey.tilemap.TileMap;
import heroes.journey.ui.HUD;
import heroes.journey.utils.RangeManager;
import heroes.journey.utils.ai.pathfinding.Cell;

public class GameState implements Cloneable {

    private int width, height;
    private EntityManager entities;
    private TileMap map;
    private RangeManager rangeManager;

    private int turn;

    private static GameState gameState;
    private List<Entity> entitiesInActionOrder;

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
        entities = new EntityManager(width, height);
        rangeManager = new RangeManager(this, width, height);

        turn = 0;
    }

    public GameState clone() {
        GameState clone = new GameState(width, height);
        clone.entities = entities.clone();
        clone.map = map.clone(clone.entities);
        clone.rangeManager = rangeManager.clone(clone);
        clone.turn = turn;
        return clone;
    }

    public GameState applyAction(QueuedAction queuedAction) {
        Cell path = queuedAction.getPath();
        Action action = queuedAction.getAction();

        Entity e = getEntities().get(path.i, path.j);
        if (e != null) {
            //e.remove();
            while (path.parent != null) {
                path = path.parent;
            }
            getEntities().addEntity(e);
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
        map.render(batch, delta);
        rangeManager.render(batch, turn);
    }

    private Entity incrementTurn() {
        if (entitiesInActionOrder == null || entitiesInActionOrder.isEmpty()) {
            entitiesInActionOrder = entities.getEntitiesInActionOrder();
            turn++;
            if (GameState.global() == this) {
                GameEngine.get().getSystem(FactionSystem.class).setProcessing(true);
            }
        }
        Entity currentEntity = entitiesInActionOrder.removeFirst();
        entities.setCurrentEntity(currentEntity);
        return currentEntity;
    }

    public void nextTurn() {
        Entity currentEntity = incrementTurn();
        System.out.println("turn " + turn + ", " + currentEntity + " " + entitiesInActionOrder);

        PlayerComponent player = PlayerComponent.get(currentEntity);
        if (player != null) {
            if (player.getPlayerId().equals(ActionQueue.get().getID())) {
                HUD.get().getCursor().setPosition(currentEntity);
                HUD.get().setState(HUD.HUDState.CURSOR_MOVE);
                System.out.println("your turn");
            } else {
                HUD.get().setState(HUD.HUDState.LOCKED);
                System.out.println("opponent turn");
            }
        } else {
            AIComponent ai = AIComponent.get(currentEntity);
            QueuedAction action = ai.getAI().getMove(this, currentEntity);
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

    public void dispose() {
        entities.dispose();
    }

}
