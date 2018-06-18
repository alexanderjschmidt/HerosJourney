package fe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.entities.Entity;
import fe.game.screens.EntityManager;
import fe.game.tilemap.TileMap;
import fe.game.utils.AStar;
import fe.game.utils.Cell;
import fe.game.utils.KeyManager;
import fe.game.utils.ResourceManager;

public class Cursor {
	private int x, y;
	public float ex, ey;

	private TileMap map;
	private EntityManager entities;

	private Entity selected;
	private byte[][] range;
	private int sx = -1, sy = -1;
	private Cell path;
	private Entity hover;

	private Animation ani;

	public Cursor(TileMap map, EntityManager entities) {
		this.map = map;
		this.entities = entities;
		TextureRegion[] frames = { ResourceManager.get().ui[0][0],
				ResourceManager.get().ui[0][0], ResourceManager.get().ui[0][0],
				ResourceManager.get().ui[0][1] };
		ani = new Animation(.5f, frames);
	}

	public void update() {
		if (entities.moving != null) {
			return;
		}
		hover = entities.get(x, y);

		if (Gdx.input.isKeyJustPressed(KeyManager.SELECT)) {
			if (selected != null) {
				entities.move(path);
				selected = null;
				range = null;
				sx = -1;
				sy = -1;
			} else if (hover != null) {
				selected = hover;
				sx = x;
				sy = y;
				getRange();
			}
		}
		if (Gdx.input.isKeyJustPressed(KeyManager.DOWN) && y > 0) {
			y--;
		} else if (Gdx.input.isKeyJustPressed(KeyManager.LEFT) && x > 0) {
			x--;
		} else if (Gdx.input.isKeyJustPressed(KeyManager.UP)
				&& y < map.getHeight() - 1) {
			y++;
		} else if (Gdx.input.isKeyJustPressed(KeyManager.RIGHT)
				&& x < map.getWidth() - 1) {
			x++;
		}
		if (selected != null && range[x][y] == 1
				&& (path == null || (path.i != x || path.j != y))) {
			path = AStar.aStar(path, selected.getMoveDistance(), range, sx, sy,
					x, y);
		}
		ex = x * TileMap.SIZE;
		ey = y * TileMap.SIZE;
	}

	private float elapsed = 0;

	public void render(Batch batch, float delta) {
		elapsed += delta;
		if (selected != null) {
			drawRange(batch);
			drawPath(batch);
			batch.setColor(Color.BLUE);
		} else if (hover != null) {
			batch.setColor(Color.RED);
		}
		batch.draw(ani.getKeyFrame(elapsed, true), ex, ey, TileMap.SIZE,
				TileMap.SIZE);
		batch.setColor(Color.WHITE);
	}

	private void getRange() {
		if (selected == null) {
			return;
		}
		range = new byte[map.getWidth()][map.getHeight()];
		floodfill(selected.getMoveDistance(), sx, sy);
	}

	private void floodfill(int dist, int x, int y) {

		if (x <= 0 || y <= 0 || x > map.getWidth() || y > map.getHeight()) {
			System.out.println("out of bounds");
			return;
		}
		if (dist == -1
				|| map.isSolid(x, y)
				|| (entities.get(x, y) != null && entities.get(x, y) != selected)) {
			return;
		}
		range[x][y] = 1;
		for (int i = 0; i < selected.range; i++) {
			int j = selected.range - i;
			if (x + i < map.getWidth() && y + j < map.getHeight()
					&& range[x + i][y + j] == 0)
				range[x + i][y + j] = 2;
			if (x - j >= 0 && y + i < map.getHeight()
					&& range[x - j][y + i] == 0)
				range[x - j][y + i] = 2;
			if (x + j < map.getWidth() && y - i >= 0
					&& range[x + j][y - i] == 0)
				range[x + j][y - i] = 2;
			if (x - i >= 0 && y - j >= 0 && range[x - i][y - j] == 0)
				range[x - i][y - j] = 2;
		}

		floodfill(dist - 1, x + 1, y);
		floodfill(dist - 1, x - 1, y);
		floodfill(dist - 1, x, y + 1);
		floodfill(dist - 1, x, y - 1);

	}

	private void drawRange(Batch batch) {
		for (int y = 0; y < range[0].length; y++) {
			for (int x = 0; x < range.length; x++) {
				if (range[x][y] == 2)
					batch.draw(ResourceManager.get().ui[0][2],
							x * TileMap.SIZE, y * TileMap.SIZE);
				if (range[x][y] == 1)
					batch.draw(ResourceManager.get().ui[1][2],
							x * TileMap.SIZE, y * TileMap.SIZE);
			}
		}
	}

	private void drawPath(Batch batch) {
		if (path == null)
			return;
		Cell c = path;
		Cell p = null;
		Cell n = c.parent;
		if (n == null) {
			batch.draw(ResourceManager.get().ui[3][0], c.i * TileMap.SIZE, c.j
					* TileMap.SIZE);
			return;
		}
		if (n.i > c.i)
			batch.draw(ResourceManager.get().ui[1][1], (c.i) * TileMap.SIZE,
					(c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270f);
		else if (n.i < c.i)
			batch.draw(ResourceManager.get().ui[1][1],
					(c.i + 1) * TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32,
					1f, 1f, 90f);
		else if (n.j < c.j)
			batch.draw(ResourceManager.get().ui[1][1],
					(c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
					32, 32, 1f, 1f, 180f);
		else if (n.j > c.j)
			batch.draw(ResourceManager.get().ui[1][1], c.i * TileMap.SIZE, c.j
					* TileMap.SIZE);
		p = c;
		c = n;
		n = n.parent;

		while (n != null) {
			if (p.i != n.i && p.j != n.j) {
				if (p.j != c.j) {
					if (p.i < n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i)
								* TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
								32, 32, 1f, 1f, 270);
					else if (p.i > n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1)
								* TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
								32, 32, 1f, 1f, 180f);
					else if (p.i < n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], c.i
								* TileMap.SIZE, c.j * TileMap.SIZE);
					else if (p.i > n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1)
								* TileMap.SIZE, (c.j) * TileMap.SIZE, 0, 0, 32,
								32, 1f, 1f, 90);
				} else {
					if (p.i < n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1)
								* TileMap.SIZE, (c.j) * TileMap.SIZE, 0, 0, 32,
								32, 1f, 1f, 90);
					else if (p.i > n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], c.i
								* TileMap.SIZE, c.j * TileMap.SIZE);
					else if (p.i < n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1)
								* TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
								32, 32, 1f, 1f, 180f);
					else if (p.i > n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i)
								* TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
								32, 32, 1f, 1f, 270);
				}
			} else {
				if (p.j != c.j)
					batch.draw(ResourceManager.get().ui[1][0], c.i
							* TileMap.SIZE, c.j * TileMap.SIZE);
				else
					batch.draw(ResourceManager.get().ui[1][0], (c.i + 1)
							* TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32,
							1f, 1f, 90f);
			}
			p = c;
			c = n;
			n = n.parent;
		}
		if (p.j > c.j)
			batch.draw(ResourceManager.get().ui[2][1], (c.i) * TileMap.SIZE,
					(c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270f);
		else if (p.j < c.j)
			batch.draw(ResourceManager.get().ui[2][1],
					(c.i + 1) * TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32,
					1f, 1f, 90f);
		else if (p.i > c.i)
			batch.draw(ResourceManager.get().ui[2][1],
					(c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0,
					32, 32, 1f, 1f, 180f);
		else if (p.i < c.i)
			batch.draw(ResourceManager.get().ui[2][1], c.i * TileMap.SIZE, c.j
					* TileMap.SIZE);
	}
}
