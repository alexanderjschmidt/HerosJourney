package heros.journey.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import heros.journey.GameState;
import heros.journey.entities.ai.AIGoalData;
import heros.journey.ui.HUD;

public class EntityManager {

	private int width, height;
	private Entity[][] entities;

	private ArrayList<Effect> effects;

	private GameState gameState;
	private float buffTime;

	public EntityManager(GameState gameState, int width, int height) {
		this.gameState = gameState;
		this.width = width;
		this.height = height;
		entities = new Entity[width][height];
		effects = new ArrayList<Effect>();
	}

	public EntityManager clone(GameState newGameState) {
		EntityManager clone = new EntityManager(newGameState, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (entities[i][j] != null) {
					clone.entities[i][j] = entities[i][j].clone(newGameState);
					clone.entities[i][j].setXCoord(i);
					clone.entities[i][j].setYCoord(j);
				}
			}
		}
		return clone;
	}

	public boolean update(float delta) {
		boolean unusedRemaining = false;

		for (int x = 0; x < entities.length; x++) {
			for (int y = 0; y < entities[0].length; y++) {
				if (entities[x][y] != null) {
					if (entities[x][y].getTeam() == gameState.getActiveTeam() && !entities[x][y].used) {
						unusedRemaining = true;
					}
				}
			}
		}

		return unusedRemaining;
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {
		buffTime += delta;

		for (int x = 0; x < entities.length; x++) {
			for (int y = 0; y < entities[0].length; y++) {
				if (entities[x][y] != null) {
					entities[x][y].update(delta);
				}
			}
		}
		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (entities[x][y] != null) {
					entities[x][y].render(batch, delta, buffTime);
				}
			}
		}
		for (int i = 0; i < effects.size(); i++) {
			if (effects.get(i).render(batch, delta)) {
				effects.remove(i);
				i--;
			}
		}
	}

	public Entity removeEntity(int x, int y) {
		Entity e = entities[x][y];
		entities[x][y] = null;
		return e;
	}

	public void addEntity(Entity e, int x, int y) {
		if (entities[x][y] == null) {
			entities[x][y] = e;
			e.setXCoord(x);
			e.setYCoord(y);
		}
	}

	/**
	 * takes into account fog
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Entity get(int x, int y) {
		if (x < 0 || y < 0 || y >= height || x >= width)
			return null;
		return entities[x][y];
	}

	/**
	 * ignores fog
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Entity getFog(int x, int y) {
		return entities[x][y];
	}

	public void nextTurn() {
		for (int x = 0; x < entities.length; x++) {
			for (int y = 0; y < entities[0].length; y++) {
				if (entities[x][y] != null) {
					entities[x][y].used = false;
					if (gameState.getActiveTeam() == entities[x][y].getTeam()) {
						entities[x][y].updateBuffs();
					}
				}
			}
		}
	}

	// TODO move to input manager or somewhere better?
	public void nextCharacter(int in_x, int in_y) {
		int x = in_x;
		int y = in_y;
		int iStart = y + (x * entities.length);
		for (int i = iStart; i != iStart - 1; i = (i + 1) % (entities.length * entities[0].length)) {
			y = (y + 1) % entities[0].length;
			if (y == 0) {
				x = (x + 1) % entities.length;
			}
			if (entities[x][y] != null && entities[x][y].getTeam() == gameState.getActiveTeam() && !entities[x][y].used) {
				HUD.get().getCursor().setPosition(x, y);
				return;
			}
		}
	}

    public boolean anyUnitAvailable() {
        boolean anyUnitAvailable = false;
        int x = 0;
        int y = 0;
        int iStart = 1;
        for (int i = iStart; i != iStart - 1; i = (i + 1) % (entities.length * entities[0].length)) {
            y = (y + 1) % entities[0].length;
            if (y == 0) {
                x = (x + 1) % entities.length;
            }
            if (entities[x][y] != null && entities[x][y].getTeam() == gameState.getActiveTeam() && !entities[x][y].used) {
                anyUnitAvailable = true;
            }
        }
        return anyUnitAvailable;
    }

	public static int getDistanceBetween(Entity A, Entity B) {
		return (Math.abs(A.getXCoord() - B.getXCoord()) + Math.abs(A.getYCoord() - B.getYCoord()));
	}

	public void removeTeam(Team remove) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (entities[x][y] != null && entities[x][y].getTeam() == remove) {
					entities[x][y] = null;
				}
			}
		}
	}

	public void addEffect(float delay, Effect effect) {
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				effects.add(effect);
			}
		}, delay);
	}

	public ArrayList<AIGoalData> getDistMap(Team team) {
		ArrayList<Entity> eList = new ArrayList<Entity>();
		ArrayList<AIGoalData> goalData = new ArrayList<AIGoalData>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (entities[x][y] != null && entities[x][y].getTeam() != team) {
					eList.add(entities[x][y]);
				} else if (entities[x][y] != null && !entities[x][y].used) {
					goalData.add(new AIGoalData(entities[x][y]));
				}
			}
		}
		for (AIGoalData goal : goalData) {
			for (Entity e : eList) {
				goal.add(e, Math.abs(goal.entity.getXCoord() - e.getXCoord()) + Math.abs(goal.entity.getYCoord() - e.getYCoord()));
			}
		}
		return goalData;
	}

	public int getScore(Team team) {
		int score = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Entity e = this.get(x, y);
				if (e != null) {
					if (e.getTeam() == team) {
						score += e.getStats().getFame();
						score += e.used ? 2 : 0;
						score += e.getBuffs().size() * 3;
					} else {
						score -= e.getStats().getFame();
						score -= e.getBuffs().size() * 3;
					}
				}
			}
		}
		return score;
	}

	public ArrayList<Entity> getVisibleUnits(Team activeTeam, boolean enemies, boolean shouldBeUnused) {
		ArrayList<Entity> avalibleUnits = new ArrayList<Entity>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Entity e = this.get(x, y);
				if (e != null && ((e.getTeam() == activeTeam) == !enemies) && (!shouldBeUnused || !e.used)) {
					avalibleUnits.add(e);
				}
			}
		}
		return avalibleUnits;
	}

	public void print() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Entity e = this.getFog(x, y);
				if (e != null) {
					System.out.print("[" + x + ": " + y + "]  \t");
				} else {
					System.out.print("(" + x + ", " + y + ")  \t");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}
