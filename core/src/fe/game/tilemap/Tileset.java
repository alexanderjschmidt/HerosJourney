package fe.game.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.utils.ResourceManager;

public class Tileset extends ArrayList<Tile> {

	private static final long serialVersionUID = 1L;
	private List<String> tileTextures = new ArrayList<String>();
	private List<TextureRegion[][]> splitTiles = new ArrayList<TextureRegion[][]>();

	public static final int[] base = { 2, 2, 2, 2, -1 };

	private static Tileset manager;

	public static synchronized Tileset get() {
		if (manager == null) {
			manager = new Tileset();
		}
		return manager;
	}

	@Override
	public Object clone() {
		return null;
	}

	private Tileset() {
		loadTexture("Textures/spritesheet.png", 32, 32);
		add("Animated:1;3,0;4,0;5,0;6,0", 0);
		add("Plain:0;2,0", 0);
		add("Plain:0;0,0", 0);
		add("Plain:0;1,0", 0);
	}

	public void loadTexture(String path, int tilewidth, int tileheight) {
		System.out.println(path);

		tileTextures.add(ResourceManager.get().loadTexture(path));

		String currentPath = tileTextures.get(tileTextures.size() - 1);

		splitTiles.add(TextureRegion.split(
				ResourceManager.get().getTexture(currentPath), tilewidth,
				tileheight));

	}

	public void add(String tile, int textureIndex) {
		String[] type = tile.split(":");
		String[] parts = type[1].split(";");
		boolean collision = Integer.parseInt(parts[0]) == 1;
		if (type[0].equals("Plain")) {
			String[] coords = parts[1].split(",");
			int x = Integer.parseInt(coords[0]);
			int y = Integer.parseInt(coords[1]);
			add(new PlainTile(splitTiles.get(textureIndex)[y][x], collision));
		} else if (type[0].equals("Animated")) {
			TextureRegion[] frames = new TextureRegion[parts.length - 1];
			for (int j = 1; j < parts.length; j++) {
				String[] coords = parts[j].split(",");
				int x = Integer.parseInt(coords[0]);
				int y = Integer.parseInt(coords[1]);
				frames[j - 1] = splitTiles.get(textureIndex)[y][x];
			}
			add(new AnimatedTile(frames, collision));
		}
	}

	public void dispose() {
		tileTextures = null;
		splitTiles = null;
	}

	public Tile getTile(int tileIndex) {
		return get(tileIndex);
	}

}
