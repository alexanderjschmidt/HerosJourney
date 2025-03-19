package heroes.journey.tilemap.wavefunction;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heroes.journey.GameCamera;
import heroes.journey.tilemap.TileMap;
import heroes.journey.tilemap.tiles.Tile;
import heroes.journey.utils.Direction;

public class WaveFunctionTile extends Tile {

    private TextureRegion texture;

    private Map<Direction,TileTerrain> neighbors;

    public WaveFunctionTile(String name, int terrainCost, TextureRegion texture) {
        this(name, terrainCost, null, texture);
    }

    public WaveFunctionTile(String name, int terrainCost, Tile previousTile, TextureRegion texture) {
        super(name, terrainCost, previousTile);
        this.texture = texture;
        neighbors = new HashMap<>();
    }

    public WaveFunctionTile add(Direction direction, TileTerrain tileTerrain) {
        neighbors.put(direction, tileTerrain);
        if (neighbors.size() == 8)
            WaveFunctionCollapse.possibleTiles.add(this);
        return this;
    }

    public boolean aligns(Direction direction, WaveFunctionTile tile) {
        switch (direction) {
            case NORTH -> {
                return tile.neighbors.get(Direction.SOUTHWEST) == neighbors.get(Direction.NORTHWEST) &&
                    tile.neighbors.get(Direction.SOUTH) == neighbors.get(Direction.NORTH) &&
                    tile.neighbors.get(Direction.SOUTHEAST) == neighbors.get(Direction.NORTHEAST);
            }
            case EAST -> {
                return tile.neighbors.get(Direction.NORTHWEST) == neighbors.get(Direction.NORTHEAST) &&
                    tile.neighbors.get(Direction.WEST) == neighbors.get(Direction.EAST) &&
                    tile.neighbors.get(Direction.SOUTHWEST) == neighbors.get(Direction.SOUTHEAST);
            }
            case SOUTH -> {
                return tile.neighbors.get(Direction.NORTHWEST) == neighbors.get(Direction.SOUTHWEST) &&
                    tile.neighbors.get(Direction.NORTH) == neighbors.get(Direction.SOUTH) &&
                    tile.neighbors.get(Direction.NORTHEAST) == neighbors.get(Direction.SOUTHEAST);
            }
            case WEST -> {
                return tile.neighbors.get(Direction.NORTHEAST) == neighbors.get(Direction.NORTHWEST) &&
                    tile.neighbors.get(Direction.EAST) == neighbors.get(Direction.WEST) &&
                    tile.neighbors.get(Direction.SOUTHEAST) == neighbors.get(Direction.SOUTHWEST);
            }
        }
        throw new RuntimeException("Invalid Direction");
    }

    public void render(
        SpriteBatch batch,
        TileMap tileMap,
        float elapsed,
        int x,
        int y,
        int variance,
        Tile facing) {
        batch.draw(texture, x * GameCamera.get().getSize(), y * GameCamera.get().getSize(),
            GameCamera.get().getSize(), GameCamera.get().getSize());
    }
}
