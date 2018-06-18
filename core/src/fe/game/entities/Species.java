package fe.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Species extends TextureManager {

	private static final long serialVersionUID = 1L;
	public float elapsedTime = 0;
	public String selected = "";

	private byte moveDist = 5;

	public Species(String folder) {
		this.folder = folder;
		load();
	}

	private void load() {

		addSet("walking", (byte) 8, .1f);

		if (Gdx.files.internal(
				"Textures/Objects/" + folder + "/" + folder + "_idle.png")
				.exists()) {
			addSet("idle", (byte) 3, .2f);
			addSet("collapse", new String[] { "collapse", "idle" }, (byte) 3,
					.1f);
		} else {
			String normal = pathBase + folder + "/" + folder + "_walking.png";
			loadRowAni(normal, "idleNORTH", (byte) 8, (byte) 4, (byte) 3,
					(byte) 1, 1f);
			loadRowAni(normal, "idleSOUTH", (byte) 8, (byte) 4, (byte) 0,
					(byte) 1, 1f);
			loadRowAni(normal, "idleWEST", (byte) 8, (byte) 4, (byte) 1,
					(byte) 1, 1f);
			loadRowAni(normal, "idleEAST", (byte) 8, (byte) 4, (byte) 2,
					(byte) 1, 1f);
			loadRowAni(normal, "collapseNORTH", (byte) 8, (byte) 4, (byte) 3,
					(byte) 1, 1f);
			loadRowAni(normal, "collapseSOUTH", (byte) 8, (byte) 4, (byte) 0,
					(byte) 1, 1f);
			loadRowAni(normal, "collapseWEST", (byte) 8, (byte) 4, (byte) 1,
					(byte) 1, 1f);
			loadRowAni(normal, "collapseEAST", (byte) 8, (byte) 4, (byte) 2,
					(byte) 1, 1f);
		}
		addTexture("neutral", new String[] { "Bust_1", "Bust", "bust",
		"Battler" });
		addTexture("happy",
				new String[] { "Bust_2", "Bust", "bust", "Battler" });
		addTexture("angry",
				new String[] { "Bust_3", "Bust", "bust", "Battler" });
		addTexture("blink",
				new String[] { "Bust_4", "Bust", "bust", "Battler" });
		addTexture("bored",
				new String[] { "Bust_5", "Bust", "bust", "Battler" });
		addTexture("disgust", new String[] { "Bust_6", "Bust", "bust",
		"Battler" });
		addTexture("thinking", new String[] { "Bust_7", "Bust", "bust",
		"Battler" });
		addTexture("hblink",
				new String[] { "Bust_8", "Bust", "bust", "Battler" });
		addTexture("Paperdoll", new String[] { "Paperdoll", "bust", "Battler" });

		setSelected("idleSOUTH");
	}

	public void render(Batch batch, float x, float y, String render,
			float scale, float deltaTime) {
		setSelected(render);
		elapsedTime += deltaTime;
		if (get(selected) != null) {
			batch.draw(get(selected).getKeyFrame(elapsedTime, true), x, y);
		}
	}

	public void setSelected(String selected) {
		if (!this.selected.equals(selected)) {
			elapsedTime = 0;
		}
		this.selected = selected;
	}

	public TextureRegion getEmotion(String emotion) {
		if (containsKey(emotion))
			return get(emotion).getKeyFrame(0);
		return null;
	}

	public int getMoveDistance() {
		return moveDist;
	}
}
