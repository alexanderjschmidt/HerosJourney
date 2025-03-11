package heroes.journey;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import heroes.journey.ui.Cursor;
import heroes.journey.ui.HUD;

public class GameCamera extends OrthographicCamera {

	private static int size = 32;
	private int xLow, xHigh, yLow, yHigh;

	private int lastTargetX = 0;
	private int lastTargetY = 0;

	private static GameCamera camera;

	public static synchronized GameCamera get() {
		if (camera == null) {
			camera = new GameCamera();
		}
		return camera;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private GameCamera() {
		super();
	}

	public void updateGameCamera() {
		super.update();
		Cursor cursor = HUD.get().getCursor();
		if (cursor != null) {
			lastTargetX = cursor.x;
			lastTargetY = cursor.y;
			if (cursor.x * size - position.x < -(3f * Gdx.graphics.getWidth() / 10f)) {
				position.x = Math.min(Math.max(position.x - size, size * (xLow + xHigh - 2) / 2), (size * 128) - (size * (xLow + xHigh - 2) / 2));
			} else if (cursor.x * size - position.x > (3f * Gdx.graphics.getWidth() / 10f)) {
				position.x = Math.min(Math.max(position.x + size, size * (xLow + xHigh - 2) / 2), (size * 128) - (size * (xLow + xHigh - 2) / 2));
			}
			if (cursor.y * size - position.y < -(3f * Gdx.graphics.getHeight() / 10f)) {
				position.y = Math.min(Math.max(position.y - size, size * (yLow + yHigh - 2) / 2), (size * 128) - (size * (yLow + yHigh - 2) / 2));
			} else if (cursor.y * size - position.y > (3f * Gdx.graphics.getHeight() / 10f)) {
				position.y = Math.min(Math.max(position.y + size, size * (yLow + yHigh - 2) / 2), (size * 128) - (size * (yLow + yHigh - 2) / 2));
			}
		}
	}

	public void setZoom() {
		switch (size) {
		case 16:
			xLow = (int) (41);
			yLow = (int) (23);
			xHigh = (int) (41);
			yHigh = (int) (23);
			break;
		case 32:
			xLow = (int) (21);
			yLow = (int) (12);
			xHigh = (int) (21);
			yHigh = (int) (12);
			break;
		default:
			xLow = (int) (11);
			yLow = (int) (7);
			xHigh = (int) (11);
			yHigh = (int) (7);
		}
		position.x = Math.min(Math.max(lastTargetX * size, size * (xLow + xHigh - 2) / 2), (size * 128) - (size * (xLow + xHigh - 2) / 2));
		position.y = Math.min(Math.max(lastTargetY * size, size * (yLow + yHigh - 2) / 2), (size * 128) - (size * (yLow + yHigh - 2) / 2));
	}

	public void zoomIn() {
		if (size < 64) {
			size *= 2;
			setZoom();
		}
	}

	public void zoomOut() {
		if (size > 16) {
			size /= 2;
			setZoom();
		}
	}

	public int getSize() {
		return size;
	}

	public int getxLow() {
		return xLow;
	}

	public int getxHigh() {
		return xHigh;
	}

	public int getyLow() {
		return yLow;
	}

	public int getyHigh() {
		return yHigh;
	}
}
