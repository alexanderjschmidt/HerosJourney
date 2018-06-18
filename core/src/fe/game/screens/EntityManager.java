package fe.game.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fe.game.entities.Entity;
import fe.game.tilemap.TileMap;
import fe.game.utils.AStar;
import fe.game.utils.Cell;
import fe.game.utils.Direction;

public class EntityManager {

	private int width, height;
	private Entity[][] entities;

	public Entity moving;
	private Cell path;

	public EntityManager(TileMap map) {
		width = map.getWidth();
		height = map.getHeight();
		entities = new Entity[width][height];
	}

	public void move(Cell p) {
		this.path = AStar.reversePath(p);
		moving = entities[path.i][path.j];
		if (moving == null)
			return;
		entities[path.i][path.j] = null;
	}

	public void update(float delta) {
		if (path != null) {
			float dx = path.i * TileMap.SIZE - moving.x;
			if (dx < -1) {
				moving.x -= delta * 70;
				moving.dir = Direction.WEST;
			} else if (dx > 1) {
				moving.x += delta * 70;
				moving.dir = Direction.EAST;
			} else {
				moving.x = path.i * TileMap.SIZE;
				float dy = path.j * TileMap.SIZE - moving.y;
				if (dy < -1) {
					moving.y -= delta * 70;
					moving.dir = Direction.SOUTH;
				} else if (dy > 1) {
					moving.y += delta * 70;
					moving.dir = Direction.NORTH;
				} else {
					moving.y = path.j * TileMap.SIZE;
					path = path.parent;
				}
			}
			moving.render = "walking" + moving.dir.toString();
		} else if (moving != null) {
			moving.render = "idle" + moving.dir.toString();
			entities[(int) (moving.x / TileMap.SIZE)][(int) (moving.y / TileMap.SIZE)] = moving;
			moving = null;
		}
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {

		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (entities[x][y] != null)
					entities[x][y].render(batch, delta);
			}
		}
		if (moving != null)
			moving.render(batch, delta);
	}

	public void addEntity(Entity e, int x, int y) {
		if (entities[x][y] == null) {
			entities[x][y] = e;
			e.x = x * TileMap.SIZE;
			e.y = y * TileMap.SIZE;
		}
	}

	public Entity get(int x, int y) {
		return entities[x][y];
	}

}
