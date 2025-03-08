package heros.journey.utils.art;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class ResourceManager extends AssetManager {

	private FreeTypeFontGenerator generator;
	public NinePatch menu;
	public BitmapFont font12;
	public BitmapFont font24;
	public BitmapFont font36;
	public BitmapFont font72;
	public Skin skin;
	public TextureRegion[][] ui;
	public TextureRegion select;
	public TextureRegion[] directions;

	public TextureRegion[][] sprites;
	public TextureRegion[][] tiles;

	public TextureRegion[][] slash;
	public TextureRegion[] arrow;
	public TextureRegion[] poison;
	public TextureRegion[] heal;
	public TextureRegion[] magicMissile;
	public TextureRegion[] bless;
	public TextureRegion[] rally;

    public HashMap<TextureMaps, TextureRegion[][]> textureRegions;

	private static ResourceManager manager;

	public static synchronized ResourceManager get() {
		if (manager == null) {
			manager = new ResourceManager();
		}
		return manager;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private ResourceManager() {
        textureRegions = new HashMap<TextureMaps, TextureRegion[][]>(TextureMaps.values().length);
		initFonts();
		loadSkin("uiskin");
		loadTexture("Textures/UI/cursor.png");
		loadTexture("Textures/sprites.png");
		loadTexture("Textures/UI/TalkingBackground.png");
		loadTexture("Textures/splash/badlogic.jpg");
		loadTexture("Textures/splash/pvglogo.png");
		loadTexture("Textures/splash/aschmidtlogo.png");
		loadTexture("Textures/UI/Background.png");
		menu = new NinePatch(new Texture(Gdx.files.internal("Textures/UI/menu9.png")), 12, 12, 12, 12);
		loadTexture("Textures/Battle_Animations/Slashing.png");
		loadTexture("Textures/Battle_Animations/Arrow 1.png");
		loadTexture("Textures/Battle_Animations/Dark 8.png");
		loadTexture("Textures/Battle_Animations/Healing 4.png");
		loadTexture("Textures/Battle_Animations/Ice 5.png");
		loadTexture("Textures/Battle_Animations/Light 8.png");
		loadTexture("Textures/Battle_Animations/Parameter 10.png");

        for (TextureMaps textureMap : TextureMaps.values()) {
            loadTexture(textureMap.getLocation());
        }

        for (TextureMaps textureMap : TextureMaps.values()) {
            textureRegions.put(textureMap, TextureRegion.split(getTexture(textureMap.getLocation()), textureMap.getWidth(), textureMap.getHeight()));
        }
	}

	public void splits() {
		ui = TextureRegion.split(getTexture("Textures/UI/cursor.png"), 32, 32);
		sprites = TextureRegion.split(getTexture("Textures/sprites.png"), 16, 16);
		tiles = TextureRegion.split(getTexture("Textures/Overworld_Tileset.png"), 16, 16);

		directions = new TextureRegion[] { sprites[3][0], sprites[2][0], sprites[3][1], sprites[2][1] };
		select = ui[3][1];
		slash = TextureRegion.split(getTexture("Textures/Battle_Animations/Slashing.png"), 32, 32);
		arrow = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Arrow 1.png"), 32, 32));
		poison = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Dark 8.png"), 32, 32));
		heal = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Healing 4.png"), 32, 32));
		magicMissile = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Ice 5.png"), 32, 32));
		bless = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Light 8.png"), 32, 32));
		rally = arrangeFrames(TextureRegion.split(getTexture("Textures/Battle_Animations/Parameter 10.png"), 32, 32));
	}

	private TextureRegion[] arrangeFrames(TextureRegion[][] split) {
		TextureRegion[] frames = new TextureRegion[split.length * split[0].length];
		for (int i = 0; i < split.length; i++) {
			for (int j = 0; j < split[0].length; j++) {
				frames[j + (i * split[0].length)] = split[i][j];
			}
		}
		return frames;
	}

	private void loadSkin(String skinName) {
		load("skin/" + skinName + "/" + skinName + ".atlas", TextureAtlas.class);
		finishLoadingAsset("skin/" + skinName + "/" + skinName + ".atlas");
		skin = new Skin();
		skin.addRegions(get("skin/" + skinName + "/" + skinName + ".atlas", TextureAtlas.class));
		skin.add("default-font", font24);
		skin.add("title", font72);
		skin.load(Gdx.files.internal("skin/" + skinName + "/" + skinName + ".json"));
	}

	private void initFonts() {
		generator = new FreeTypeFontGenerator(Gdx.files.internal("skin/Gabrielle.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();

		parameter.color = Color.WHITE;

		parameter.size = 12;
		font12 = generator.generateFont(parameter);
		parameter.size = 24;
		font24 = generator.generateFont(parameter);
		parameter.size = 36;
		font36 = generator.generateFont(parameter);
		parameter.size = 72;
		font72 = generator.generateFont(parameter);
	}

	public void load() {
		while (!update()) {
			System.out.println("Loaded: " + getProgress() * 100 + "%");
		}
	}

	public String loadTexture(String path) {
		if (isLoaded(path))
			return path;
		if (Gdx.files.internal(path).exists()) {
			load(path, Texture.class);
		}
		// manager.finishLoading();
		return path;
	}

	public String loadPixmap(String path) {
		load(path, Pixmap.class);
		// manager.finishLoading();
		return path;
	}

	// could cause issues from creating file handles over and over
	public Texture getTexture(String path) {
		if (!Gdx.files.internal(path).exists()) {
			return null;
		}
		if (!isLoaded(path)) {
			this.finishLoadingAsset(path);
		}
		return get(path, Texture.class);
	}

	public Pixmap getPixmap(String path) {
		if (!isLoaded(path)) {
			finishLoadingAsset(path);
		}
		return get(path, Pixmap.class);
	}

	public Boolean isLoaded() {
		if (getProgress() >= 1)
			return true;
		return false;
	}

	@Override
	public void dispose() {
		super.dispose();
		generator.dispose();
		font12.dispose();
		font24.dispose();
		font36.dispose();
		font72.dispose();
	}

    public static TextureRegion[][] get(TextureMaps textureMap) {
        return manager.textureRegions.get(textureMap);
    }

    public static TextureRegion get(TextureMaps textureMap, int x, int y) {
        return manager.textureRegions.get(textureMap)[x][y];
    }

}
