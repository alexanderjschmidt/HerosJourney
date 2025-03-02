package fe.game.managers;

import com.badlogic.gdx.graphics.g2d.Batch;

import fe.game.entities.Entity;
import fe.game.entities.ai.GameState;
import fe.game.screens.BattleScreen;
import fe.game.tilemap.TileMap;

public class FogManager {

	private BattleScreen battleScreen;
	private GameState gameState;
	private int width, height;
	private boolean[][] fogMap;
	private boolean USE_FOG = true;

	public FogManager(BattleScreen battleScreen, GameState gameState, boolean fogOfWar) {
		this.battleScreen = battleScreen;
		this.gameState = gameState;
		width = gameState.getWidth();
		height = gameState.getHeight();
		fogMap = new boolean[width][height];
		this.USE_FOG = fogOfWar;
	}

	public FogManager clone(GameState newGameState) {
		return new FogManager(battleScreen, newGameState, USE_FOG);
	}

	public void update() {
		if (battleScreen.getID() == null) {
			if (gameState.getActiveTeam().isAI()) {
				return;
			}
		} else if (!gameState.getActiveTeam().getID().equals(battleScreen.getID())) {
			return;
		}
		fogMap = new boolean[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Entity e = gameState.getEntities().getFog(x, y);
				if (e != null && e.getTeam().equals(gameState.getActiveTeam())) {
					floodfill(e.getEntityClass().getVision(), x, y);
				}
				Entity b = gameState.getEntities().getBuilding(x, y);
				if (b != null && b.getTeam().equals(gameState.getActiveTeam())) {
					floodfill(b.getEntityClass().getVision(), x, y);
				}
			}
		}
	}

	public void render(Batch batch, float xo, float yo, float delta) {
		if (!USE_FOG)
			return;
		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (!fogMap[x][y]) {
					batch.draw(ResourceManager.get().sprites[1][0], x * TileMap.SIZE, y * TileMap.SIZE, TileMap.SIZE, TileMap.SIZE);
				}
			}
		}
	}

	private void floodfill(int dist, int x, int y) {
		if (x < 0 || y < 0 || x >= gameState.getWidth() || y >= gameState.getHeight()) {
			// System.out.println("out of bounds");
			return;
		}
		if (dist < 0) {
			return;
		}
		fogMap[x][y] = true;

		// System.out.println(terrainCost);
		floodfill(dist - 1, x + 1, y);
		floodfill(dist - 1, x - 1, y);
		floodfill(dist - 1, x, y + 1);
		floodfill(dist - 1, x, y - 1);

	}

	public boolean isVisible(int x, int y) {
		return fogMap[x][y] || !USE_FOG;
	}

}
