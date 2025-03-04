package heros.journey.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureManager extends HashMap<String, Animation> {

	private static final long serialVersionUID = 1L;
	public String folder = null;
	public String pathBase = "Textures/Objects/";

	// For Faces
	public void addTexture(String name, String[] possiblePathNames) {
		String base = pathBase + folder + "/" + folder;
		String normal = null;
		for (int i = 0; i < possiblePathNames.length; i++) {
			normal = base + "_" + possiblePathNames[i] + ".png";
			if (Gdx.files.internal(normal).exists()) {
				break;
			}
		}
		TextureRegion[] tex = { new TextureRegion(ResourceManager.get()
				.getTexture(ResourceManager.get().loadTexture(normal))) };
		if (tex != null) {
			put(name, new Animation(1f, tex));

			tex[0].getTexture().setFilter(Texture.TextureFilter.Linear,
					Texture.TextureFilter.Linear);
		}
	}

	// Directional Movement
	public void addSet(String name, String[] possiblePathNames, byte length,
			float duration) {
		String base = pathBase + folder + "/" + folder;
		String normal = null;
		for (int i = 0; i < possiblePathNames.length; i++) {
			normal = base + "_" + possiblePathNames[i] + ".png";
			if (Gdx.files.internal(normal).exists()) {
				loadRowAni(normal, name + "NORTH", length, (byte) 4, (byte) 3,
						duration);
				loadRowAni(normal, name + "SOUTH", length, (byte) 4, (byte) 0,
						duration);
				loadRowAni(normal, name + "WEST", length, (byte) 4, (byte) 1,
						duration);
				loadRowAni(normal, name + "EAST", length, (byte) 4, (byte) 2,
						duration);
				break;
			}
		}
	}

	public void addSet(String name, byte length, float duration) {
		this.addSet(name, new String[] { name }, length, duration);
	}

	protected void loadRowAni(String path, String aniName, byte numWide,
			byte numHigh, byte rowX, float delay) {
		loadRowAni(path, aniName, numWide, numHigh, rowX, numWide, delay);
	}

	// Single Animation
	protected void loadRowAni(String path, String aniName, byte numWide,
			byte numHigh, byte rowX, byte length, float delay) {
		int[] x = new int[length];
		int[] y = new int[length];
		for (int i = 0; i < length; i++) {
			x[i] = rowX;
			y[i] = i;
		}
		loadAnimation(path, aniName, delay, numWide, numHigh, x, y);
	}

	protected void loadColAni(String path, String aniName, byte numWide,
			byte numHigh, byte colY, float delay) {
		int[] x = new int[numHigh];
		int[] y = new int[numHigh];
		for (int i = 0; i < numHigh; i++) {
			x[i] = i;
			y[i] = colY;
		}
		loadAnimation(path, aniName, delay, numWide, numHigh, x, y);
	}

	private void loadAnimation(String path, String aniName, float delay,
			byte numWide, byte numHigh, int[] x, int[] y) {
		ResourceManager.get().loadTexture(path);
		Texture tex = ResourceManager.get().getTexture(path);
		TextureRegion[][] region = TextureRegion.split(tex, tex.getWidth()
				/ numWide, tex.getHeight() / numHigh);

		TextureRegion[] frames = new TextureRegion[x.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = region[x[i]][y[i]];
		}

		put(aniName, new Animation(delay, frames));
		tex.setFilter(Texture.TextureFilter.Linear,
				Texture.TextureFilter.Linear);
	}

}
