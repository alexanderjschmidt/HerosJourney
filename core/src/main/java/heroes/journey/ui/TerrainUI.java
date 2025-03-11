package heroes.journey.ui;

import com.badlogic.gdx.graphics.g2d.Batch;

import heroes.journey.GameState;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;

public class TerrainUI extends UI {

	private Tile tile;
	private ActionTile actionTile;

	public TerrainUI() {
        super(0, 0, 8, 1);
	}

    @Override
	public void update() {
        Cursor cursor = HUD.get().getCursor();
		tile = GameState.global().getMap().get(cursor.x, cursor.y);
        actionTile = GameState.global().getMap().getEnvironment(cursor.x, cursor.y);
	}

    @Override
	public void drawUI(Batch batch, float parentAlpha) {
		String name = tile == null ? "---" : (tile + (actionTile == null ? "" : " and " + actionTile));
        drawText(batch, name, 0, 0);
	}

}
