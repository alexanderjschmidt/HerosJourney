package heroes.journey.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;

import heroes.journey.GameCamera;
import heroes.journey.GameState;
import heroes.journey.entities.actions.Action;
import heroes.journey.entities.actions.ActionQueue;
import heroes.journey.entities.actions.TargetAction;
import heroes.journey.entities.ai.AI;
import heroes.journey.entities.buffs.Buff;
import heroes.journey.entities.factions.Faction;
import heroes.journey.entities.items.Inventory;
import heroes.journey.entities.stats.Stats;
import heroes.journey.initializers.base.Actions;
import heroes.journey.ui.HUD;
import heroes.journey.utils.Direction;
import heroes.journey.utils.ai.pathfinding.Cell;

public class Character extends Actor {

    private int x, y;

    private String render = "idleSOUTH";
    public Direction dir = Direction.SOUTH;
    protected float elapsedTime = 0;

    private GameState gameState;

    private Cell path;
    private Action action;
    private int targetX, targetY;

	protected EntityClass job;
    private Stats stats;
    private Inventory inventory;
	private ArrayList<Buff> buffs;
    private ArrayList<Faction> factions;
    private AI ai;

	public Character(int x, int y) {
        this.setPosition(x, y);
        setSelected("idleSOUTH");
	}

	public Character(EntityClass job, AI ai) {
        this(0, 0);
		this.job = job;
        this.ai = ai;
        this.stats = new Stats();
        this.inventory = new Inventory();
        buffs = new ArrayList<Buff>();
        factions = new ArrayList<Faction>();
	}

	public Character clone(GameState newGameState) {
        Character clone = new Character(this.job, this.ai);
        clone.setXCoord(getXCoord());
        clone.setYCoord(getYCoord());
        clone.setPosition(getX(), getY());
        clone.buffs = new ArrayList<Buff>();
        for (Buff b : buffs) {
            clone.buffs.add(b.clone());
        }
        clone.factions = new ArrayList<Faction>();
        for (Faction f : factions) {
            clone.factions.add(f.clone());
        }
		clone.job = job;
        clone.stats = stats.clone();
        clone.inventory = inventory.clone();
		return clone;
	}

	public void render(Batch batch, float deltaTime, float buffTime) {
		render(batch, deltaTime);

        job.render(batch, getRenderX(), getRenderY(), elapsedTime);
		batch.setColor(Color.WHITE);
		renderBuffs(batch, buffTime);
	}

	private void renderBuffs(Batch batch, float buffTime) {
		ArrayList<Buff> activeBuffs = new ArrayList<Buff>();
		for (Buff buff : buffs) {
			if (buff.isActive())
				activeBuffs.add(buff);
		}
		if (activeBuffs.size() == 1) {
			activeBuffs.add(null);
		}
		if (!activeBuffs.isEmpty()) {
			if (activeBuffs.get((int) (buffTime % activeBuffs.size())) != null)
				activeBuffs.get((int) (buffTime % activeBuffs.size())).render(batch, getRenderX(), getRenderY());
		}
	}

    public void updateBuffs() {
        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).update()) {
                buffs.remove(i--);
            }
        }
    }

    public EntityClass getEntityClass() {
        return job;
    }

    public Stats getStats() {
        return stats;
    }

    public Inventory getInventory() {
        return inventory;
    }

	public String toString() {
		return job.toString() + " (" + getXCoord() + ", " + getYCoord() + ")";
	}

    public void openActionMenu() {
        List<Action> options = getEntityClass().getActions().stream().filter(action -> action.requirementsMet(getGameState(), this)).collect(
            Collectors.toList());
        HUD.get().getActionMenu().open(options);
    }

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}

    public int getMoveDistance() {
        return stats.getBody() + 4;
    }

    public int[] getRanges() {
        return new int[]{1, 2};
    }

    public int getSpeed() {
        return stats.getBody() * 2;
    }

    public AI getAI() {
        return ai;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public void addFaction(Faction faction) {
        if (factions.stream().anyMatch(Faction::isPlayerFaction))
            throw new RuntimeException("Character already has a player faction, CANNOT have more than one");
        factions.add(faction);
    }

    public void render(Batch batch, float deltaTime) {
        if (path != null)
            setSelected("walking");
        else
            setSelected("idle");

        this.act(deltaTime);
        elapsedTime += deltaTime;

        batch.setColor(this.getColor());
    }

    private void setSelected(String selected) {
        if (!this.render.equals(selected)) {
            elapsedTime = 0;
        }
        this.render = selected;
    }

    public void vibrate(float delay, Character from) {
        // 0.2 seconds
        Vector2 v = dir.getDirVector();
        this.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(delay), com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(5 * v.y, 5 * v.x, .05f, Interpolation.pow5In), com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-10 * v.y, -10 * v.x, .05f, Interpolation.pow5),
            com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(10 * v.y, 10 * v.x, .05f, Interpolation.pow5), com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-5 * v.y, -5 * v.x, .05f, Interpolation.pow5Out)));

    }

    public void lunge(float delay, Character at) {
        // 0.4 seconds
        Vector2 v = dir.getDirVector();
        this.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.delay(delay), com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(15 * v.x, 15 * v.y, .2f, Interpolation.pow5In), com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy(-15 * v.x, -15 * v.y, .2f, Interpolation.pow5Out)));
    }

    public void update(float delta) {
        final Character e = (Character) this;
        if (path != null && !this.hasActions()) {
            //TODO Make duration based on move speed
            this.addAction(com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence(com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo(path.i - x, path.j - y, .2f),
                com.badlogic.gdx.scenes.scene2d.actions.Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        gameState.getEntities().removeEntity(x, y);
                        x = path.i;
                        y = path.j;
                        gameState.getEntities().addEntity(e, e.getXCoord(), e.getYCoord());
                        e.setPosition(0, 0);
                        path = path.parent;
                        if (path == null) {
                            if (action == null) {
                                e.openActionMenu();
                            } else {
                                if (action instanceof TargetAction targetAction) {
                                    targetAction.targetEffect(gameState, e, targetX, targetY);
                                } else {
                                    action.onSelect(gameState, e);
                                }
                                if (ActionQueue.get().isActionInProgress()){
                                    if (action.equals(Actions.wait)) {
                                        ActionQueue.get().endAction();
                                    } else {
                                        Timer.schedule(new Timer.Task() {
                                            @Override
                                            public void run() {
                                                ActionQueue.get().endAction();
                                            }
                                        }, 1f);
                                    }
                                }
                            }
                        }
                    }
                })
            ));
        }
    }

    public void move(Cell p, Action action, int x2, int y2) {
        if (path != null) {
            return;
        }
        x = this.getXCoord();
        y = this.getYCoord();
        this.path = p;
        this.action = action;
        this.targetX = x2;
        this.targetY = y2;
    }

    public boolean remove() {
        gameState.getEntities().removeEntity(getXCoord(), getYCoord());
        return true;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getXCoord() {
        return x;
    }

    public int getYCoord() {
        return y;
    }

    public void setXCoord(int x) {
        this.x = x;
    }

    public void setYCoord(int y) {
        this.y = y;
    }

    public void setMapPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public float getRenderX() {
        return (x + getX()) * GameCamera.get().getSize();
    }

    public float getRenderY() {
        return (y + getY()) * GameCamera.get().getSize();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
