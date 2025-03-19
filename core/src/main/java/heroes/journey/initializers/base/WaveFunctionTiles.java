package heroes.journey.initializers.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heroes.journey.initializers.InitializerInterface;
import heroes.journey.tilemap.art.WangCornerTile;
import heroes.journey.tilemap.art.WangCornerTileAnimated;
import heroes.journey.tilemap.art.WangEdgeTile;
import heroes.journey.tilemap.art.WangEdgeTileAnimated;
import heroes.journey.tilemap.wavefunction.TileTerrain;
import heroes.journey.tilemap.wavefunction.WaveFunctionTile;
import heroes.journey.utils.Direction;
import heroes.journey.utils.art.ResourceManager;
import heroes.journey.utils.art.TextureMaps;

public class WaveFunctionTiles implements InitializerInterface {

    public static WaveFunctionTile WATER, SAND, PLAINS, HILLS, CLIFF, PLAINSTOCLIFF, SANDTOCLIFF;

    // Art
    private static WangCornerTile longGrass, hills, shortGrass, sandArt, sandNeutral, cliffArt, sandCliff, mountainArt;
    private static WangCornerTileAnimated waterArt, waterCliff, beach;
    private static WangEdgeTile pathArt, shortpath;
    private static WangEdgeTileAnimated river;

    static {
        TextureRegion[][] tiles = ResourceManager.get(TextureMaps.OverworldTileset);

        TileTerrain plains = new TileTerrain("Plains");
        TileTerrain hills = new TileTerrain("Hills");
        TileTerrain cliff = new TileTerrain("Cliff");
        TileTerrain sand = new TileTerrain("Sand");
        TileTerrain sandToPlains = new TileTerrain("Sand");
        PLAINS = new WaveFunctionTile("Plains", 2, tiles[5][1]);
        HILLS = new WaveFunctionTile("Hills", 2, tiles[5][1]);
        SAND = new WaveFunctionTile("Sand", 3, tiles[17][1]);
        basicTile(PLAINS, plains);
        basicTile(HILLS, hills);
        basicTile(SAND, sand);

        wangCorner(cliff, 15, plains, hills, tiles, 0, 10);
        wangCorner(cliff, 15, sand, hills, tiles, 0, 13);
        wangCorner(sandToPlains, 15, plains, sand, tiles, 0, 16);
    }

    public static void basicTile(WaveFunctionTile tile, TileTerrain tileTerrain) {
        tile.add(Direction.NORTHWEST, tileTerrain)
            .add(Direction.NORTH, tileTerrain)
            .add(Direction.NORTHEAST, tileTerrain)
            .add(Direction.EAST, tileTerrain)
            .add(Direction.SOUTHEAST, tileTerrain)
            .add(Direction.SOUTH, tileTerrain)
            .add(Direction.SOUTHWEST, tileTerrain)
            .add(Direction.WEST, tileTerrain);
    }

    /**
     * @param base
     * @param terrainCost
     * @param adjacentTileOuter
     * @param adjacentTileInner
     * @param tiles             [y][x]
     * @param x                 top left corner
     * @param y                 top left corner
     */
    public static void wangCorner(
        TileTerrain base,
        int terrainCost,
        TileTerrain adjacentTileOuter,
        TileTerrain adjacentTileInner,
        TextureRegion[][] tiles,
        int x,
        int y) {
        // Center (Not included because it should be one of the adjacent tiles
        // tile.add(new WaveFunctionTileRender(tiles[y + 1][x + 1], 0), adjacentTileInner, adjacentTileInner,
        //     adjacentTileInner, adjacentTileInner);
        // Setup Tiles
        // Corners
        WaveFunctionTile northWest = new WaveFunctionTile(base.toString(), terrainCost, tiles[y][x]);
        WaveFunctionTile northEast = new WaveFunctionTile(base.toString(), terrainCost, tiles[y][x + 2]);
        WaveFunctionTile southWest = new WaveFunctionTile(base.toString(), terrainCost, tiles[y + 2][x]);
        WaveFunctionTile southEast = new WaveFunctionTile(base.toString(), terrainCost, tiles[y + 2][x + 2]);
        // Edges
        WaveFunctionTile north = new WaveFunctionTile(base.toString(), terrainCost, tiles[y][x + 1]);
        WaveFunctionTile east = new WaveFunctionTile(base.toString(), terrainCost, tiles[y + 1][x + 2]);
        WaveFunctionTile south = new WaveFunctionTile(base.toString(), terrainCost, tiles[y + 2][x + 1]);
        WaveFunctionTile west = new WaveFunctionTile(base.toString(), terrainCost, tiles[y + 1][x]);
        // Inverse Corners
        WaveFunctionTile northWestInverse = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y][x + 3]);
        WaveFunctionTile northEastInverse = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y][x + 4]);
        WaveFunctionTile southWestInverse = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y + 1][x + 3]);
        WaveFunctionTile southEastInverse = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y + 1][x + 4]);
        // Diagonal Corners
        // Diagonal with the cliff on the bottom left and top right
        WaveFunctionTile diagonalSouthWest = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y + 2][x + 3]);
        // Diagonal with the cliff on the top left and bottom right
        WaveFunctionTile diagonalNorthWest = new WaveFunctionTile(base.toString(), terrainCost,
            tiles[y + 2][x + 4]);

        // Add connections
        // Corners
        northWest.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, adjacentTileOuter)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, adjacentTileOuter);
        northEast.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, adjacentTileOuter)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, adjacentTileOuter)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, base)
            .add(Direction.WEST, adjacentTileInner);
        southWest.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, adjacentTileOuter)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, adjacentTileOuter);
        southEast.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, adjacentTileOuter)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, adjacentTileOuter)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, base);
        // Edges
        north.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, adjacentTileOuter)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, adjacentTileInner)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, base);
        south.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, adjacentTileInner)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, adjacentTileOuter)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, base);
        east.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, adjacentTileOuter)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, adjacentTileInner);
        west.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, adjacentTileInner)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, adjacentTileOuter);
        // Inverse Corners
        northWestInverse.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, adjacentTileInner)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, adjacentTileInner)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, base);
        northEastInverse.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, adjacentTileInner)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, adjacentTileInner);
        southWestInverse.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, adjacentTileInner)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, adjacentTileInner)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, base);
        southEastInverse.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, adjacentTileInner)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, adjacentTileInner);
        // Diagonal Corners
        diagonalSouthWest.add(Direction.NORTHWEST, adjacentTileOuter)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileInner)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileOuter)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileInner)
            .add(Direction.WEST, base);
        diagonalNorthWest.add(Direction.NORTHWEST, adjacentTileInner)
            .add(Direction.NORTH, base)
            .add(Direction.NORTHEAST, adjacentTileOuter)
            .add(Direction.EAST, base)
            .add(Direction.SOUTHEAST, adjacentTileInner)
            .add(Direction.SOUTH, base)
            .add(Direction.SOUTHWEST, adjacentTileOuter)
            .add(Direction.WEST, base);
    }
}
