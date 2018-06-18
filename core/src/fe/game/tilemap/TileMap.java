package fe.game.tilemap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fe.game.utils.SerializationUtils;
import fe.game.utils.Type;

public class TileMap {

	public static final int SIZE = 32;
	boolean blend = true;

	private int width, height;
	private TileData[][] tileMap;
	private float elapsed = 0;

	public TileMap() {
		width = 64;
		height = 64;
		tileMap = new TileData[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tileMap[x][y] = new TileData((y % 3 == 0 && x % 3 == 0) ? 0
						: (byte) 1);
			}
		}
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {
		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT_BRACKET)) {
			blend = !blend;
		}
		elapsed += delta;

		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				TileData data = tileMap[x][y];
				Tileset.get().getTile(data.tile1)
				.render(batch, x * SIZE, y * SIZE, elapsed, 1f);
				if (blend && data.tile2 != -1)
					Tileset.get()
					.getTile(data.tile2)
					.render(batch, x * SIZE, y * SIZE, elapsed,
							data.tile2percent);
			}
		}
	}

	private class TileData {
		public byte tile1;
		public byte tile2;

		public float tile2percent;

		public TileData(byte tile1) {
			this.tile1 = tile1;
		}

		public int writeBytes(byte[] dest, int pointer) {
			pointer = SerializationUtils.writeBytes(dest, pointer, tile1);
			pointer = SerializationUtils.writeBytes(dest, pointer, tile2);
			pointer = SerializationUtils
					.writeBytes(dest, pointer, tile2percent);
			return pointer;
		}

		public int readBytes(byte[] src, int pointer) {
			tile1 = SerializationUtils.readByte(src, pointer);
			pointer += Type.getSize(Type.BYTE);
			tile2 = SerializationUtils.readByte(src, pointer);
			pointer += Type.getSize(Type.BYTE);
			tile2percent = SerializationUtils.readFloat(src, pointer);
			pointer += Type.getSize(Type.FLOAT);
			return pointer;
		}

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isSolid(int i, int j) {
		return tileMap[i][j].tile1 == 0;
	}
}
