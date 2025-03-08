package heros.journey.tilemap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import heros.journey.GameCamera;
import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.entities.actions.Action;
import heros.journey.initializers.base.Actions;
import heros.journey.initializers.base.Tiles;
import heros.journey.tilemap.tiles.ActionTile;
import heros.journey.tilemap.tiles.TileInterface;
import heros.journey.utils.worldgen.CellularAutomata;

public class TileMap {

	private CellularAutomata gen;

	private int width, height, seed;
	private heros.journey.tilemap.tiles.Tile[][] tileMap;
	private ActionTile[][] environment;
	private heros.journey.tilemap.tiles.Tile[][] facing;
	private int[][] variance;
	private float elapsed = 0;

	public TileMap(int mapSize, int seed) {
		// 383797854
		// gen = new RandomWorldGenerator(seed, 80, 2, .6f, false);
		gen = new CellularAutomata(mapSize, mapSize, seed);
        this.seed = seed;
		width = mapSize;
		height = mapSize;
        GameCamera.get().setZoom();
		genNewMap();
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {
		elapsed += delta;

		int x0 = (int) Math.max(Math.floor(xo - GameCamera.get().getxLow()), 0);
		int y0 = (int) Math.max(Math.floor(yo - GameCamera.get().getyLow()), 0);
		int x1 = (int) Math.min(Math.floor(xo + GameCamera.get().getxHigh()), width);
		int y1 = (int) Math.min(Math.floor(yo + GameCamera.get().getyHigh()), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				tileMap[x][y].render(batch, this, elapsed, x, y, variance[x][y], facing[x][y]);
				if (environment[x][y] != null)
					environment[x][y].render(batch, this, elapsed, x, y, variance[x][y], facing[x][y]);
			}
		}
	}

	public heros.journey.tilemap.tiles.Tile get(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return tileMap[Math.max(0, Math.min(width - 1, x))][Math.max(0, Math.min(height - 1, y))];
		} else {
			return tileMap[x][y];
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setEnvironment(int x, int y, ActionTile tile) {
		environment[x][y] = tile;
	}

	public ActionTile getEnvironment(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return environment[Math.max(0, Math.min(width - 1, x))][Math.max(0, Math.min(height - 1, y))];
		} else {
			return environment[x][y];
		}
	}

	public void setTile(int x, int y, heros.journey.tilemap.tiles.Tile tile) {
		tileMap[x][y] = tile;
	}

	public TileInterface getTerrain(int x, int y) {
		return tileMap[x][y];
	}

	public void genPath(int i, int j, int endX, int endY) {
		gen.genPath(this, i, j, endX, endY);
	}

	public void genNewMap() {
		tileMap = gen.generateMap(width);
		environment = gen.generateTrees(tileMap, width);

		houseStart = 0;
		houseEnd = 1;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<String> noiseTask = () -> {
            // Simulate long-running noise generation task
            genHouses();
            return "";
        };
        Future<String> future = executorService.submit(noiseTask);

        try {
            // Wait for the task to complete or timeout
            future.get(2000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Handle timeout case (task did not finish in time)
            future.cancel(true);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
		// tileMap = gen.mirrorY(tileMap, true);
		// trees = gen.mirrorY(trees, true);
		facing = gen.getFacingMap(tileMap);

		variance = gen.getVarianceMap();
	}

	int numHouses = 15;
	Vector2[] housePos = new Vector2[numHouses];

	public void genHouses() {
		for (int i = 0; i < numHouses; i++) {
			while (true) {
				int x = (int) (Math.random() * width);
				int y = (int) (Math.random() * height);
				if (tileMap[x][y] == Tiles.PLAINS) {
					housePos[i] = new Vector2(x, y);
					environment[x][y] = Tiles.HOUSE;
					break;
				}
			}
		}
		while (houseStart < numHouses) {
			genNextPath();
		}
	}

	int houseStart = 0;
	int houseEnd = 1;

	public void genNextPath() {
		genPath((int) housePos[houseStart].x, (int) housePos[houseStart].y, (int) housePos[houseEnd].x, (int) housePos[houseEnd].y);
		houseEnd++;
		if (houseStart == houseEnd) {
			houseEnd++;
		}
		if (houseEnd >= numHouses) {
			houseEnd = 0;
			houseStart++;
		}
	}

	public void setSeed(int i) {
		gen.setSeed(i);
	}

	public List<Action> getTileActions(int x, int y) {
		ActionTile tile = environment[x][y];
		if (tile == null) {
            ArrayList<Action> options = new ArrayList<Action>(1);
            options.add(Actions.wait);
            return options;
		}
		return tile.getActions();
	}

    // TODO make this a deep copy
    public TileMap clone(GameState clone) {
        TileMap map = new TileMap(width, seed);
        map.tileMap = this.tileMap.clone();
        map.environment = this.environment.clone();
        map.variance = this.variance.clone();
        map.facing = this.facing.clone();
        return map;
    }

    public int getTerrainCost(int x, int y, Entity selected) {
        return selected.getEntityClass().getTerrainCost(get(x, y), environment[x][y]);
    }
}
