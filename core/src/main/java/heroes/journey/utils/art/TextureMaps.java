package heroes.journey.utils.art;

public enum TextureMaps {

	UI("Textures/UI/cursor.png", 32, 32), Sprites("Textures/sprites.png", 16, 16), OverworldTileset("Textures/Overworld_Tileset.png", 16, 16);

	private String location;
	private int width, height;

	private TextureMaps(String location, int width, int height) {
		this.location = location;
		this.width = width;
		this.height = height;
	}

	public String getLocation() {
		return location;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
