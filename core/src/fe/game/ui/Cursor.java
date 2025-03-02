package fe.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.GameState;
import fe.game.entities.Entity;
import fe.game.entities.buffs.Buff;
import fe.game.entities.buffs.BuffManager;
import fe.game.entities.skills.TargetSkill;
import fe.game.managers.RangeManager.RangeColor;
import fe.game.managers.ResourceManager;
import fe.game.tilemap.TileMap;
import fe.game.ui.HUD.HUDState;
import fe.game.utils.AStar;
import fe.game.utils.Cell;

public class Cursor {
	// coords
	public int x, y;
	// render points

	private HUD hud;

	private Entity selected;
	private Entity hover;
	private int sx = -1, sy = -1;
	private Cell path;
	private TargetSkill activeSkill;

	private Animation ani;

	private int initialX, initialY;

	private float elapsed = 0;

	public Cursor(HUD hud) {
		this.hud = hud;
		TextureRegion[] frames = { ResourceManager.get().ui[0][0], ResourceManager.get().ui[0][0], ResourceManager.get().ui[0][1] };
		ani = new Animation(.5f, frames);
		setPosition(10, 15);
	}

	public void update(float delta) {
		hover = GameState.global().getEntities().get(x, y);
		if (hover == null) {
			hover = GameState.global().getEntities().getBuilding(x, y);
		}
		if (selected != null && GameState.global().getRangeManager().getRange()[x][y] == RangeColor.BLUE && (path == null || (path.i != x || path.j != y))) {
			int move = selected.getEntityClass().getMoveDistance();
			Buff rallied = selected.getBuff(BuffManager.get().rallied);
			if (rallied != null && rallied.isActive()) {
				move += 3;
			}
			path = AStar.aStarEntity(move, GameState.global().getRangeManager().getRange(), sx, sy, x, y, GameState.global().getMap(), selected);
		}
	}

	public void clearSelected(boolean markedUsed) {
		selected.used = markedUsed;
		hud.setState(HUDState.CURSOR_MOVE);
		selected = null;
		sx = -1;
		sy = -1;
		path = null;
		GameState.global().getRangeManager().clearRange();
		hud.clearSelect();
	}

	public void render(Batch batch, float delta) {
		elapsed += delta;
		drawPath(batch);
		if (hud.getState() != HUDState.LOCKED) {
			if (selected != null) {
				batch.setColor(Color.BLUE);
			} else if (hover != null) {
				batch.setColor(Color.RED);
			}
		}
		batch.draw(ani.getKeyFrame(elapsed, true), x * TileMap.SIZE, y * TileMap.SIZE, TileMap.SIZE, TileMap.SIZE);
		batch.setColor(Color.WHITE);
	}

	private void drawPath(Batch batch) {
		if (path == null)
			return;
		Cell c = path;
		Cell p = null;
		Cell n = c.parent;
		// no path dot
		if (n == null) {
			batch.draw(ResourceManager.get().ui[3][0], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
			return;
		}
		// start
		if (n.i > c.i)
			batch.draw(ResourceManager.get().ui[1][1], (c.i) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270f);
		else if (n.i < c.i)
			batch.draw(ResourceManager.get().ui[1][1], (c.i + 1) * TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 90f);
		else if (n.j < c.j)
			batch.draw(ResourceManager.get().ui[1][1], (c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 180f);
		else if (n.j > c.j)
			batch.draw(ResourceManager.get().ui[1][1], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
		p = c;
		c = n;
		n = n.parent;

		while (n != null) {
			if (p.i != n.i && p.j != n.j) {
				if (p.j != c.j) {
					if (p.i < n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270);
					else if (p.i > n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 180f);
					else if (p.i < n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
					else if (p.i > n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1) * TileMap.SIZE, (c.j) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 90);
				} else {
					if (p.i < n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1) * TileMap.SIZE, (c.j) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 90);
					else if (p.i > n.i && p.j < n.j)
						batch.draw(ResourceManager.get().ui[2][0], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
					else if (p.i < n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 180f);
					else if (p.i > n.i && p.j > n.j)
						batch.draw(ResourceManager.get().ui[2][0], (c.i) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270);
				}
			} else {
				// straights
				if (p.j != c.j)
					batch.draw(ResourceManager.get().ui[1][0], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
				else
					batch.draw(ResourceManager.get().ui[1][0], (c.i + 1) * TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 90f);
			}
			p = c;
			c = n;
			n = n.parent;
		}
		// arrow
		if (p.j > c.j)
			batch.draw(ResourceManager.get().ui[2][1], (c.i) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 270f);
		else if (p.j < c.j)
			batch.draw(ResourceManager.get().ui[2][1], (c.i + 1) * TileMap.SIZE, c.j * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 90f);
		else if (p.i > c.i)
			batch.draw(ResourceManager.get().ui[2][1], (c.i + 1) * TileMap.SIZE, (c.j + 1) * TileMap.SIZE, 0, 0, 32, 32, 1f, 1f, 180f);
		else if (p.i < c.i)
			batch.draw(ResourceManager.get().ui[2][1], c.i * TileMap.SIZE, c.j * TileMap.SIZE);
	}

	public void setPosition(int i, int j) {
		x = i;
		y = j;
		update(0);
	}

	public Entity getSelected() {
		return selected;
	}

	public void revertSelectedPosition() {
		if (selected == null) {
			GameState.global().getRangeManager().clearRange();
			return;
		}
		Entity e = GameState.global().getEntities().removeEntity(sx, sy);
		GameState.global().getEntities().addEntity(e, initialX, initialY);
		clearSelected(false);
		if (e.getEntityClass().getMoveDistance() != 0) {
			update(0);
			setSelectedtoHover();
			GameState.global().getRangeManager().setMoveAndAttackRange(selected);
			hud.select();
		}

	}

	public void setActiveSkill(TargetSkill skill) {
		this.activeSkill = skill;
		hud.getCombatUI().setMessage(activeSkill.getUIMessage(GameState.global(), selected, x, y));
	}

	public Entity getHover() {
		return hover;
	}

	public Cell getPath() {
		return path;
	}

	public void setSelectedtoHover() {
		selected = hover;
		sx = x;
		sy = y;
		initialX = x;
		initialY = y;
	}

	public TargetSkill getActiveSkill() {
		return activeSkill;
	}

	public void revertAction() {
		this.setPosition(sx, sy);
		selected.openActionMenu();
	}

	public void turn(float vx, float vy) {
		this.x = (int) (sx + vx);
		this.y = (int) (sy + vy);
	}

	public void moveSelected() {
		if (GameState.global().getEntities().get(path.i, path.j) == null || GameState.global().getEntities().get(path.i, path.j) == selected) {
			hud.setState(HUDState.MOVING);
			selected.move(AStar.reversePath(path), null, 0, 0);
			GameState.global().getRangeManager().clearRange();
			sx = path.i;
			sy = path.j;
			path = null;
		}
	}

	public void setPath(Cell temp) {
		this.path = temp;
	}

	public void setSelected(Entity selected) {
		this.selected = selected;
	}

}
