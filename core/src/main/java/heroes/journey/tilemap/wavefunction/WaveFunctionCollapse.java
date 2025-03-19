package heroes.journey.tilemap.wavefunction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import heroes.journey.tilemap.tiles.Tile;
import heroes.journey.utils.Direction;
import heroes.journey.utils.Random;

public class WaveFunctionCollapse {

    public static List<WaveFunctionTile> possibleTiles = new ArrayList<>();

    public static Tile[][] genMap(int size) {
        Tile[][] map = new Tile[size][size];

        for (WaveFunctionTile tile : possibleTiles) {
            System.out.println(tile);
        }

        applyWaveFunctionCollapse(map, possibleTiles);

        return map;
    }

    static void applyWaveFunctionCollapse(Tile[][] map, List<WaveFunctionTile> possibleTiles) {
        List<WaveFunctionTile>[][] possibleTilesMap = new ArrayList[map.length][map.length];
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map.length; y++) {
                possibleTilesMap[x][y] = new ArrayList<>(possibleTiles);
            }
        }
        outer:
        while (true) {
            // Step 1: Find the cell with the lowest entropy (fewest possible states)
            int minEntropyX = -1, minEntropyY = -1;
            int minEntropy = Integer.MAX_VALUE;
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map.length; y++) {
                    if (map[x][y] == null && possibleTilesMap[x][y].isEmpty()) {
                        clearHole(map, possibleTilesMap, x, y);
                        continue outer;
                    }
                    if (map[x][y] == null && possibleTilesMap[x][y].size() < minEntropy) {
                        minEntropy = possibleTilesMap[x][y].size();
                        minEntropyX = x;
                        minEntropyY = y;
                    }

                }
            }
            //System.out.println("Lowest Entrophy: " + minEntropy + ": " + minEntropyX + ", " + minEntropyY);
            // If all cells are collapsed, exit the loop
            if (minEntropy == Integer.MAX_VALUE) {
                break;
            }

            // Step 2: Collapse the chosen cell to one of its possible states
            collapse(map, possibleTilesMap, minEntropyX, minEntropyY);
            //System.out.println("Collapsed: " + map[minEntropyX][minEntropyY]);

            // Step 3: Propagate constraints (reduce possible states of neighboring cells)
            propagateConstraints(map, possibleTilesMap, minEntropyX, minEntropyY);
        }
    }

    private static void clearHole(
        Tile[][] map,
        List<WaveFunctionTile>[][] possibleTilesMap,
        int minEntropyX,
        int minEntropyY) {
        int radius = 2;
        //System.out.println("Clear Hole: " + minEntropyX + " " + minEntropyY);
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (minEntropyX + dx >= 0 && minEntropyX + dx < map.length && minEntropyY + dy >= 0 &&
                    minEntropyY + dy < map.length)
                    map[minEntropyX + dx][minEntropyY + dy] = null;
            }
        }
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (minEntropyX + dx >= 0 && minEntropyX + dx < map.length && minEntropyY + dy >= 0 &&
                    minEntropyY + dy < map.length)
                    possibleTilesMap[minEntropyX + dx][minEntropyY + dy] = getPossibleTiles(map,
                        minEntropyX + dx, minEntropyY + dy);
            }
        }
    }

    private static List<WaveFunctionTile> getPossibleTiles(Tile[][] map, int x, int y) {
        List<WaveFunctionTile> tiles = new ArrayList<>(possibleTiles);

        checkNeighborFresh(map, tiles, x, y + 1, Direction.NORTH);
        checkNeighborFresh(map, tiles, x + 1, y, Direction.EAST);
        checkNeighborFresh(map, tiles, x, y - 1, Direction.SOUTH);
        checkNeighborFresh(map, tiles, x - 1, y, Direction.WEST);

        return tiles;
    }

    private static void checkNeighborFresh(
        Tile[][] map,
        List<WaveFunctionTile> tiles,
        int x,
        int y,
        Direction dir) {
        if (x >= 0 && x < map.length && y >= 0 && y < map.length && map[x][y] != null) {
            tiles.removeIf(tile -> !tile.aligns(dir, (WaveFunctionTile)map[x][y]));
        }
    }

    static void propagateConstraints(
        Tile[][] map,
        List<WaveFunctionTile>[][] possibleTilesMap,
        int nx,
        int ny) {
        Queue<int[]> propagationQueue = new LinkedList<>();

        propagationQueue.add(new int[] {nx, ny});
        // Propagate the constraints to neighboring cells
        int processed = 0;
        while (!propagationQueue.isEmpty()) {
            //System.out.println("Queue size: " + propagationQueue.size() + " Processed: " + processed++);
            int[] position = propagationQueue.poll();
            int x = position[0];
            int y = position[1];

            WaveFunctionTile collapsedTile = (WaveFunctionTile)map[x][y];

            // Check neighbors and remove tiles that don’t match edges
            checkNeighbor(map, possibleTilesMap, x, y + 1, Direction.NORTH, collapsedTile, propagationQueue);
            checkNeighbor(map, possibleTilesMap, x + 1, y, Direction.EAST, collapsedTile, propagationQueue);
            checkNeighbor(map, possibleTilesMap, x, y - 1, Direction.SOUTH, collapsedTile, propagationQueue);
            checkNeighbor(map, possibleTilesMap, x - 1, y, Direction.WEST, collapsedTile, propagationQueue);
        }
    }

    private static void checkNeighbor(
        Tile[][] map,
        List<WaveFunctionTile>[][] possibleTilesMap,
        int nx,
        int ny,
        Direction dir,
        WaveFunctionTile collapsedTile,
        Queue<int[]> propagationQueue) {
        if (nx >= 0 && nx < map.length && ny >= 0 && ny < map.length && map[nx][ny] == null) {
            possibleTilesMap[nx][ny].removeIf(tile -> !collapsedTile.aligns(dir, tile));
            if (possibleTilesMap[nx][ny].size() == 1) {
                collapse(map, possibleTilesMap, nx, ny);
                propagationQueue.add(new int[] {nx, ny});
            }
        }
    }

    private static void collapse(Tile[][] map, List<WaveFunctionTile>[][] possibleTilesMap, int x, int y) {
        map[x][y] = possibleTilesMap[x][y].get(Random.get().nextInt(possibleTilesMap[x][y].size()));
    }

}
