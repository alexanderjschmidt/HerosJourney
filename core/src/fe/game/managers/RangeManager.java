package fe.game.managers;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;

import fe.game.GameState;
import fe.game.entities.Entity;
import fe.game.entities.Team;
import fe.game.entities.buffs.Buff;
import fe.game.entities.buffs.BuffManager;
import fe.game.tilemap.TileMap;
import fe.game.ui.HUD;

public class RangeManager {

	public enum RangeColor {
		NONE, BLUE, RED, GREEN, PURPLE, TEAL, YELLOW;
	}

	private GameState gameState;
	private int width, height;
	private RangeColor[][] range;
	private ArrayList<Entity> targets;
	private int target;

	public RangeManager(GameState gameState, int width, int height) {
		this.gameState = gameState;
		this.width = width;
		this.height = height;
		clearRange();
	}

	public RangeManager clone(GameState newGameState) {
		return new RangeManager(newGameState, width, height);
	}

	public ArrayList<Entity> updateTargets(Entity selected, boolean enemies, int[] ranges, RangeColor rangeType) {
		clearRange();
		int sx = (int) (selected.renderX / TileMap.SIZE);
		int sy = (int) (selected.renderY / TileMap.SIZE);
		setDistanceRangeAt(sx, sy, ranges, rangeType);
		targets = new ArrayList<Entity>();
		target = 0;
		for (int x = 0; x < range.length; x++) {
			for (int y = 0; y < range[0].length; y++) {
				// System.out.print(range[x][y]);
				if (range[x][y] == rangeType) {
					Entity e = gameState.getEntities().get(x, y);
					if (e != null && ((enemies && e.getTeam() != selected.getTeam()) || (!enemies && e.getTeam() == selected.getTeam()))) {
						targets.add(e);
					}
				}
			}
			// System.out.println();
		}
		return targets;
	}

	public void pointAtTarget(int increment) {
		target = (target + increment + targets.size()) % targets.size();
		HUD.get().getCursor().setPosition(targets.get(target).getXCoord(), targets.get(target).getYCoord());
	}

	public void setMoveAndAttackRange(Entity selected) {
		if (selected == null) {
			return;
		}
		clearRange();
		int move = selected.getEntityClass().getMoveDistance();
		Buff rallied = selected.getBuff(BuffManager.get().rallied);
		if (rallied != null && rallied.isActive()) {
			move += 3;
		}
		floodfill(move, (int) (selected.renderX / TileMap.SIZE), (int) (selected.renderY / TileMap.SIZE), selected);
	}

	public void clearRange() {
		range = new RangeColor[width][height];
		target = 0;
	}

	private void floodfill(int dist, int x, int y, Entity selected) {
		if (x < 0 || y < 0 || x >= range.length || y >= range[0].length) {
			// System.out.println("out of bounds");
			return;
		}
		if (dist < 0 || (gameState.getEntities().get(x, y) != null && gameState.getEntities().get(x, y).getTeam() != selected.getTeam())) {
			return;
		}
		range[x][y] = RangeColor.BLUE;
		setDistanceRangeAt(x, y, selected.getEntityClass().getRanges(), RangeColor.RED);

		// System.out.println(terrainCost);
		floodfill(dist - gameState.getMap().getTerrainCost(x + 1, y, selected), x + 1, y, selected);
		floodfill(dist - gameState.getMap().getTerrainCost(x - 1, y, selected), x - 1, y, selected);
		floodfill(dist - gameState.getMap().getTerrainCost(x, y + 1, selected), x, y + 1, selected);
		floodfill(dist - gameState.getMap().getTerrainCost(x, y - 1, selected), x, y - 1, selected);

	}

	public void setDistanceRangeAt(int x, int y, int[] ranges, RangeColor type) {
		for (int r : ranges) {
			for (int i = 0; i < r; i++) {
				int j = r - i;
				if (x + i < range.length && y + j < range[0].length && isEmpty(x + i, y + j))
					range[x + i][y + j] = type;
				if (x - j >= 0 && y + i < range[0].length && isEmpty(x - j, y + i))
					range[x - j][y + i] = type;
				if (x + j < range.length && y - i >= 0 && isEmpty(x + j, y - i))
					range[x + j][y - i] = type;
				if (x - i >= 0 && y - j >= 0 && isEmpty(x - i, y - j))
					range[x - i][y - j] = type;
			}
		}
	}

	public void render(Batch batch, int turn, Team activeTeam) {
		if (range == null) {
			System.out.println("Range null");
			return;
		}
		for (int y = 0; y < range[0].length; y++) {
			for (int x = 0; x < range.length; x++) {
				if (turn == 0 && isEmpty(x, y) && activeTeam.inTerritory(x, y)) {
					range[x][y] = activeTeam.getRangeColor();
				}
				if (range[x][y] == RangeColor.YELLOW)
					batch.draw(ResourceManager.get().ui[1][3], x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == RangeColor.TEAL)
					batch.draw(ResourceManager.get().ui[1][4], x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == RangeColor.PURPLE)
					batch.draw(ResourceManager.get().ui[0][4], x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == RangeColor.GREEN)// green
					batch.draw(ResourceManager.get().ui[0][3], x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == RangeColor.RED)// red
					batch.draw(ResourceManager.get().ui[0][2], x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == RangeColor.BLUE)// blue
					batch.draw(ResourceManager.get().ui[1][2], x * TileMap.SIZE, y * TileMap.SIZE);
			}
		}
	}

	public RangeColor[][] getRange() {
		return range;
	}

	public boolean isEmpty(int x, int y) {
		return range[x][y] == null || range[x][y] == RangeColor.NONE;
	}

}
