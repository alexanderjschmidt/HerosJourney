package heros.journey.tilemap;

import heros.journey.tilemap.tiles.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileManager extends HashMap<String, Tile> {

    List<Tile> heightOrder;

    private static TileManager tileManager;

    public static TileManager get() {
        if (tileManager == null)
            tileManager = new TileManager();
        return tileManager;
    }

    private TileManager() {
        heightOrder = new ArrayList<>();
    }

    public static void put(String name, Tile tile, Tile previousTile){
        get().put(name, tile);
        if (previousTile == null){
            get().heightOrder.add(tile);
        } else {
            for (int i = 0;i < get().heightOrder.size(); i++){
                if (get().heightOrder.get(i) == previousTile){
                    get().heightOrder.add(i+1, tile);
                }
            }
        }
    }

    public static int getHeight(Tile tile){
        for (int i = 0;i < get().heightOrder.size(); i++){
            if (get().heightOrder.get(i) == tile){
                return i;
            }
        }
        return 0;
    }

    public static Tile getTile(int height){
        return (Tile)get().heightOrder.get(height);
    }

}
