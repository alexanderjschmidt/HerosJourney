package heros.journey.tilemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import heros.journey.tilemap.tiles.Tile;
import heros.journey.tilemap.tiles.TileInterface;

public class TileManager extends HashMap<String,TileInterface> {

    List<TileInterface> heightOrder;

    private static TileManager tileManager;

    public static TileManager get() {
        if (tileManager == null)
            tileManager = new TileManager();
        return tileManager;
    }

    private TileManager() {
        heightOrder = new ArrayList<>();
    }

    public static void put(String name, TileInterface tile, TileInterface previousTile){
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

    public static int getHeight(TileInterface tile){
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
