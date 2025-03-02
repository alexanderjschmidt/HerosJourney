package fe.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import fe.game.GameState;
import fe.game.entities.ai.AIGoalData;
import fe.game.tilemap.TileMap;
import fe.game.ui.HUD;

public class EntityManager {

	private int width, height;
	private Entity[][] entities;
	private Entity[][] buildings;

	private ArrayList<Effect> effects;

	private GameState gameState;
	private float buffTime;

	public EntityManager(GameState gameState, int width, int height) {
		this.gameState = gameState;
		this.width = width;
		this.height = height;
		entities = new Entity[width][height];
		buildings = new Entity[width][height];
		effects = new ArrayList<Effect>();
	}

	public EntityManager clone(GameState newGameState) {
		EntityManager clone = new EntityManager(newGameState, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (entities[i][j] != null) {
					clone.entities[i][j] = entities[i][j].clone(newGameState);
					clone.entities[i][j].renderX = i * TileMap.SIZE;
					clone.entities[i][j].renderY = j * TileMap.SIZE;
				}
				if (buildings[i][j] != null) {
					clone.buildings[i][j] = buildings[i][j].clone(newGameState);
					clone.buildings[i][j].renderX = i * TileMap.SIZE;
					clone.buildings[i][j].renderY = j * TileMap.SIZE;
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
				if (buildings[x][y] != null) {
					buildings[x][y].update(delta);
				}
			}
		}
		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (buildings[x][y] != null) {
					buildings[x][y].render(batch, delta, buffTime);
				}
			}
		}
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
			e.renderX = x * TileMap.SIZE;
			e.renderY = y * TileMap.SIZE;
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
			if ((entities[x][y] != null && entities[x][y].getTeam() == gameState.getActiveTeam() && !entities[x][y].used)
					|| (buildings[x][y] != null && buildings[x][y].getTeam() == gameState.getActiveTeam())) {
				HUD.get().getCursor().setPosition(x, y);
				return;
			}
		}
	}

	public static int getDistanceBetween(Entity A, Entity B) {
		return (Math.abs(A.getXCoord() - B.getXCoord()) + Math.abs(A.getYCoord() - B.getYCoord()));
	}

	public void addBuilding(Entity buildingType, int i, int j) {
		if (buildingType.getEntityClass().equals(EntityClassManager.get().castle)) {
			int endX = -1, endY = -1;
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (buildings[x][y] != null && buildings[x][y].getEntityClass().equals(EntityClassManager.get().castle)) {
						endX = x;
						endY = y;
						gameState.getMap().genPath(i, j, endX, endY);
					}
				}
			}
		}
		if (buildings[i][j] == null) {
			buildings[i][j] = buildingType;
			buildingType.renderX = i * TileMap.SIZE;
			buildingType.renderY = j * TileMap.SIZE;
		}
		gameState.getMap().clearTrees(i, j);
	}

	public Entity getBuilding(int x, int y) {
		return buildings[x][y];
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

	public void updateMorale() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (buildings[x][y] != null && entities[x][y] != null && buildings[x][y].getTeam() != entities[x][y].getTeam()) {
					Team bTeam = buildings[x][y].getTeam();
					if (buildings[x][y].getEntityClass().equals(EntityClassManager.get().castle))
						bTeam.adjustMorale(-5 - (3 * (Team.FORT_CAP - bTeam.forts)));
					else
						bTeam.adjustMorale(-3);
				}
				if (entities[x][y] != null && entities[x][y].getEntityClass().equals(EntityClassManager.get().commander))
					entities[x][y].getTeam().adjustMorale(5);
			}
		}
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
				if (buildings[x][y] != null && buildings[x][y].getTeam() != team) {
					eList.add(buildings[x][y]);
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
				Entity b = this.getBuilding(x, y);
				if (e != null) {
					if (e.getTeam() == team) {
						score += e.getHealth();
						score += e.getExp();
						score += e.used ? 2 : 0;
						if (b != null) {
							if (b.getTeam() != team)
								score += 10;
							else
								score += 4;
						}
						score += e.getBuffs().size() * 3;
					} else {
						score -= e.getHealth();
						score -= e.getExp();
						if (b != null) {
							if (b.getTeam() == team)
								score -= 10;
							else
								score -= 4;
						}
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
				Entity b = this.getBuilding(x, y);
				if (e != null && ((e.getTeam() == activeTeam) == !enemies) && (!shouldBeUnused || !e.used)) {
					avalibleUnits.add(e);
				} else if (e == null && b != null && (b.getTeam() == activeTeam) == !enemies && (!shouldBeUnused || !b.used)) {
					// avalibleUnits.add(b);
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
