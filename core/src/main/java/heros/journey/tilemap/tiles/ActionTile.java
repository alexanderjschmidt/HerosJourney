package heros.journey.tilemap.tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heros.journey.entities.actions.Action;
import heros.journey.entities.actions.ActionManager;
import heros.journey.tilemap.TileMap;
import heros.journey.utils.art.ResourceManager;
import heros.journey.utils.art.TextureMaps;

import java.util.Arrays;
import java.util.List;

public enum ActionTile implements TileInterface {

	TREES("Trees", 2, ActionManager.getAction(ActionManager.WAIT)) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			treesArt.render(batch, tileMap, elapsed, x, y, variance);
		}
	},
	HOUSE("House", 1, ActionManager.getAction(ActionManager.WAIT)) {
		@Override
		public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, TileInterface facing) {
			houseArt.render(batch, tileMap, elapsed, x, y, variance);
		}
	};

	// Art
	private static WangCornerBuildingTile treesArt;
	private static PlainTile houseArt;

	static {
		TextureRegion[][] tiles = ResourceManager.get(TextureMaps.OverworldTileset);

		treesArt = new WangCornerBuildingTile(tiles, 3, 1, 8, 6, 7);
		treesArt.overwrite2Corner(tiles, 0, 9);
		// Single Tree Variance
		treesArt.addVariance(tiles, 0, 6, 8);
		// Inner forest Variance
		treesArt.addVariance(tiles, 15, 5, 7, 5, 8);

		houseArt = new PlainTile(tiles, 6, 7, 12);
		houseArt.addVariance(tiles, 7, 13, 8, 12, 8, 13, 9, 12, 9, 13);
	}

	private String name;
	private List<Action> actions;
	private int terrainCost;

	private ActionTile(String name, int terrainCost, Action... actions) {
		this.name = name;
		this.terrainCost = terrainCost;
		this.actions = Arrays.asList(actions);
	}

	public String toString() {
		return name;
	}

	public int getTerrainCost() {
		return terrainCost;
	}

	public List<Action> getActions() {
		return actions;
	}

}
