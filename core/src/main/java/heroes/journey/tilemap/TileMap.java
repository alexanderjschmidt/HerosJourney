package heroes.journey.tilemap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import heroes.journey.GameCamera;
import heroes.journey.GameState;
import heroes.journey.entities.Character;
import heroes.journey.entities.actions.Action;
import heroes.journey.initializers.base.Actions;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;
import heroes.journey.utils.worldgen.CellularAutomata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static heroes.journey.initializers.base.Tiles.WATER;

public class TileMap {

	private int width, height;
	private Tile[][] tileMap;
	private ActionTile[][] environment;
	private Tile[][] facing;
	private int[][] variance;
	private float elapsed = 0;

	public TileMap(int mapSize) {
		// gen = new RandomWorldGenerator(seed, 80, 2, .6f, false);
		width = mapSize;
		height = mapSize;
        tileMap = new Tile[width][height];
        for (Tile[] row : tileMap) {
            Arrays.fill(row, WATER);
        }
        GameCamera.get().setZoom();
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

	public Tile get(int x, int y) {
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

	public void setTile(int x, int y, Tile tile) {
		tileMap[x][y] = tile;
	}

	public Tile getTerrain(int x, int y) {
		return tileMap[x][y];
	}

	public void genFacingAndVariance() {
        facing = CellularAutomata.getFacingMap(tileMap);

        variance = CellularAutomata.getVarianceMap(tileMap);
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
        TileMap map = new TileMap(width);
        map.tileMap = this.tileMap.clone();
        map.environment = this.environment.clone();
        map.variance = this.variance.clone();
        map.facing = this.facing.clone();
        return map;
    }

    public int getTerrainCost(int x, int y, Character selected) {
        return selected.getEntityClass().getTerrainCost(get(x, y), environment[x][y]);
    }

    public void setTileMap(Tile[][] tileMap) {
        this.tileMap = tileMap;
    }

    public void setEnvironment(ActionTile[][] environment) {
        this.environment = environment;
    }

    public Tile[][] getTileMap() {
        return tileMap;
    }

    public ActionTile[][] getEnvironment() {
        return environment;
    }
}
