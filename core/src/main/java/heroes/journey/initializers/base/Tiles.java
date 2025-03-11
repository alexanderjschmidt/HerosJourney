package heroes.journey.initializers.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import heroes.journey.initializers.InitializerInterface;
import heroes.journey.tilemap.TileMap;
import heroes.journey.tilemap.art.*;
import heroes.journey.tilemap.tiles.ActionTile;
import heroes.journey.tilemap.tiles.Tile;
import heroes.journey.utils.art.ResourceManager;
import heroes.journey.utils.art.TextureMaps;

public class Tiles implements InitializerInterface {

    public static Tile WATER, SAND, PLAINS, HILLS, MOUNTAINS, PATH;

    // Art
    private static WangCornerTile longGrass, hills, shortGrass, sandArt, sandNeutral, cliffArt, sandCliff, mountainArt;
    private static WangCornerTileAnimated waterArt, waterCliff, beach;
    private static WangEdgeTile pathArt, shortpath;
    private static WangEdgeTileAnimated river;

    static {
        TextureRegion[][] tiles = ResourceManager.get(TextureMaps.OverworldTileset);
        longGrass = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 2);
        longGrass.addVariance(tiles, 0, 1, 0, 2, 0);
        longGrass.addVariance(tiles, 15, 1, 0, 2, 0);
        shortGrass = new WangCornerTile(tiles, 10, 1, 3, 0, 0, 2);
        shortGrass.overwriteAll(tiles, 0, 3);
        shortGrass.addVariance(tiles, 0, 1, 0, 2, 0);
        shortGrass.addVariance(tiles, 15, 5, 1, 5, 2, 6, 1, 6, 2);
        hills = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 0);
        hills.overwriteCircle(tiles, 0, 6);
        hills.addVariance(tiles, 0, 1, 0, 2, 0);
        hills.addVariance(tiles, 15, 1, 0, 2, 0);
        sandArt = new WangCornerTile(tiles, 2, 1, 17, 0, 0, 1);
        sandArt.overwriteCircle(tiles, 0, 18);
        sandArt.addVariance(tiles, 1, 3, 17);
        sandArt.addVariance(tiles, 2, 3, 16);
        sandArt.addVariance(tiles, 4, 4, 16);
        sandArt.addVariance(tiles, 8, 4, 17);
        sandNeutral = new WangCornerTile(tiles, 2, 1, 17, 1, 17, 1);

        cliffArt = new WangCornerTile(tiles, 10, 0, 0, 0, 0, 0);
        cliffArt.overwriteAll(tiles, 0, 12);
        cliffArt.addVariance(tiles, 0, 1, 0, 2, 0);
        cliffArt.addVariance(tiles, 15, 1, 0, 2, 0);
        sandCliff = new WangCornerTile(tiles, 10, 0, 0, 1, 17, 0);
        sandCliff.overwriteAll(tiles, 0, 15);
        sandCliff.addVariance(tiles, 0, 1, 0, 2, 0);

        waterArt = new WangCornerTileAnimated(tiles, 4, 21, 11, 0, 0, 1);
        waterArt.overwrite2Corner(tiles, 20, 12);
        waterCliff = new WangCornerTileAnimated(tiles, 4, 21, 14, 0, 0, 1);
        waterCliff.overwrite2Corner(tiles, 20, 15);
        beach = new WangCornerTileAnimated(tiles, 4, 21, 17, 0, 0, 1);
        beach.overwrite2Corner(tiles, 20, 18);

        mountainArt = new WangCornerTile(tiles, 5, 10, 17, 15, 16, 2);
        mountainArt.overwriteAll(tiles, 9, 18);
        mountainArt.addVariance(tiles, 15, 14, 18, 14, 17, 14, 16);

        pathArt = new WangEdgeTile(tiles, 1, 12, 3);
        pathArt.overwriteEdge(tiles, 12, 3);
        shortpath = new WangEdgeTile(tiles, 3, 7, 3);
        shortpath.overwriteEdge(tiles, 7, 3);

        river = new WangEdgeTileAnimated(tiles, 3, 20, 3);
        river.overwriteEdge(tiles, 20, 3);

        WATER = new Tile("Water", 12) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                if (facing == PLAINS || facing == MOUNTAINS || facing == PATH) {
                    waterArt.render(batch, tileMap, elapsed, x, y, variance);
                } else if (facing == SAND) {
                    beach.render(batch, tileMap, elapsed, x, y, variance);
                } else if (facing == HILLS) {
                    waterCliff.render(batch, tileMap, elapsed, x, y, variance);
                }
            }
        };
        SAND = new Tile("Sand", 3) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                if (facing == PLAINS || facing == HILLS || facing == MOUNTAINS || facing == PATH)
                    sandArt.render(batch, tileMap, elapsed, x, y, variance);
                else
                    sandNeutral.render(batch, tileMap, elapsed, x, y, variance);
            }
        };
        PLAINS = new Tile("Plains", 2) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                shortGrass.render(batch, tileMap, elapsed, x, y, variance);
            }
        };
        HILLS = new Tile("Hill", 2) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                if (facing == WATER || facing == MOUNTAINS || facing == PATH) {
                    longGrass.render(batch, tileMap, elapsed, x, y, variance);
                } else if (facing == SAND) {
                    hills.render(batch, tileMap, elapsed, x, y, variance);
                } else if (facing == PLAINS) {
                    hills.render(batch, tileMap, elapsed, x, y, variance);
                }
            }
        };
        MOUNTAINS = new Tile("Mountain", 10) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                mountainArt.render(batch, tileMap, elapsed, x, y, variance);
            }
        };
        PATH = new Tile("Path", 1) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                pathArt.render(batch, tileMap, elapsed, x, y, variance);
            }
        };
    }

    public static ActionTile TREES, HOUSE;

    //Action Tiles
    static {
        TextureRegion[][] tiles = ResourceManager.get(TextureMaps.OverworldTileset);

        WangCornerBuildingTile treesArt = new WangCornerBuildingTile(tiles, 3, 1, 8, 6, 7);
        treesArt.overwrite2Corner(tiles, 0, 9);
        // Single Tree Variance
        treesArt.addVariance(tiles, 0, 6, 8);
        // Inner forest Variance
        treesArt.addVariance(tiles, 15, 5, 7, 5, 8);
        TREES = new ActionTile("Trees", 2, BaseActions.wait) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                treesArt.render(batch, tileMap, elapsed, x, y, variance);
            }
        };

        PlainTile houseArt = new PlainTile(tiles, 6, 7, 12);
        houseArt.addVariance(tiles, 7, 13, 8, 12, 8, 13, 9, 12, 9, 13);
        HOUSE = new ActionTile("House", 1, BaseActions.wait) {
            @Override
            public void render(SpriteBatch batch, TileMap tileMap, float elapsed, int x, int y, int variance, Tile facing) {
                houseArt.render(batch, tileMap, elapsed, x, y, variance);
            }
        };
    }
}
