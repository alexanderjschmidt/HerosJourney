package fe.game.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Timer;

import fe.game.ActionQueue;
import fe.game.GameState;
import fe.game.entities.buffs.Buff;
import fe.game.entities.buffs.BuffManager;
import fe.game.entities.buffs.BuffType;
import fe.game.entities.skills.Skill;
import fe.game.entities.skills.SkillManager;
import fe.game.entities.skills.TargetSkill;
import fe.game.tilemap.TileMap;
import fe.game.ui.HUD;
import fe.game.utils.Cell;
import fe.game.utils.Direction;

public class Entity extends Actor {

	public static final int MOVE_SPEED = 120;
	public static final float RANKUP_EXPERIENCE = 20;
	public static final float MAX_MANA = 10;

	protected EntityClass job;
	public float renderX, renderY;
	private int x, y;
	public Direction dir = Direction.SOUTH;
	private String render = "idleSOUTH";
	protected float elapsedTime = 0;

	private Team team;
	protected int health;
	protected int mana;
	private ArrayList<Buff> buffs;
	protected int experience = 0;
	public boolean used;

	private GameState gameState;
	private Cell path;
	private Skill skill;
	private int targetX, targetY;

	public Entity(int x, int y) {
		this.setPosition(0, 0);
		renderX = x * TileMap.SIZE;
		renderY = y * TileMap.SIZE;
	}

	public Entity(EntityClass job, Team team, GameState gameState) {
		this.job = job;
		this.team = team;
		this.gameState = gameState;
		this.setPosition(0, 0);
		setSelected("idleSOUTH");
		health = (int) job.getMaxHealth();
		mana = (int) MAX_MANA;
		experience = 0;
		buffs = new ArrayList<Buff>();
		used = false;
		this.setColor(team.getColor());
	}

	public Entity clone(GameState newGameState) {
		System.out.println(this);
		Entity clone = null;
		try {
			clone = new Entity(job, newGameState.getTeams().get(team.getArrayID()), newGameState);
		} catch (Exception e) {
			System.out.println(newGameState);
			System.out.println(newGameState.getTeams());
			System.out.println(team);
		}
		clone.setPosition(getX(), getY());
		clone.health = health;
		clone.mana = mana;
		clone.experience = experience;
		clone.used = used;
		clone.buffs = new ArrayList<Buff>();
		for (Buff b : buffs) {
			clone.buffs.add(b.clone());
		}
		clone.job = job;
		if (job.equals(EntityClassManager.get().castle)) {
			for (Team t : newGameState.getTeams()) {
				if (t.getID() == clone.getTeam().getID()) {
					System.out.println("Team Set");
					t.setCastle(this);
				}
			}
		}
		return clone;
	}

	public void render(Batch batch, float deltaTime, float buffTime) {
		if (path != null)
			setSelected("walking");
		else
			setSelected("idle");

		this.act(deltaTime);
		elapsedTime += deltaTime;
		if (used) {
			batch.setColor(Color.GRAY);
		} else {
			batch.setColor(this.getColor());
		}
		job.render(batch, renderX + this.getX(), renderY + getY(), elapsedTime);
		batch.setColor(Color.WHITE);
		renderBuffs(batch, buffTime);
	}

	private void renderBuffs(Batch batch, float buffTime) {
		ArrayList<Buff> activeBuffs = new ArrayList<Buff>();
		for (Buff buff : buffs) {
			if (buff.isActive())
				activeBuffs.add(buff);
		}
		if (activeBuffs.size() == 1) {
			activeBuffs.add(null);
		}
		if (activeBuffs.size() > 0) {
			if (activeBuffs.get((int) (buffTime % activeBuffs.size())) != null)
				activeBuffs.get((int) (buffTime % activeBuffs.size())).render(batch, renderX, renderY);
		}
	}

	private void setSelected(String selected) {
		if (!this.render.equals(selected)) {
			elapsedTime = 0;
		}
		this.render = selected;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void vibrate(float delay, Entity from) {
		// 0.2 seconds
		Vector2 v = dir.getDirVector();
		this.addAction(Actions.sequence(Actions.delay(delay), Actions.moveBy(5 * v.y, 5 * v.x, .05f, Interpolation.pow5In), Actions.moveBy(-10 * v.y, -10 * v.x, .05f, Interpolation.pow5),
				Actions.moveBy(10 * v.y, 10 * v.x, .05f, Interpolation.pow5), Actions.moveBy(-5 * v.y, -5 * v.x, .05f, Interpolation.pow5Out)));

	}

	public void adjustHealth(Entity attacker, float delay, int amount) {
		Entity e = this;
		if (amount < 0) {
			if (attacker != null)
				attacker.addExp(amount / 2);
			e.addExp(amount);
		}
		addAction(Actions.sequence(Actions.delay(delay), Actions.run(new Runnable() {
			@Override
			public void run() {
				health = (int) Math.min(Math.max(getHealth() + amount, 0), e.getEntityClass().getMaxHealth());
				if (getHealth() == 0) {
					addAction(Actions.sequence(Actions.delay(.1f), Actions.fadeOut(.2f, Interpolation.pow3Out), Actions.removeActor()));
					attacker.team.adjustMorale(e.getEntityClass().getValue() / 5);
					attacker.addExp(4);
					e.team.adjustMorale(-e.getEntityClass().getValue() * 2 / 5);
					if (!e.getEntityClass().equals(EntityClassManager.get().commander))
						e.team.currentTroops -= e.getEntityClass().getValue();
				}
			}
		})));
	}

	public void addExp(int exp) {
		experience = (int) Math.min(experience + Math.abs(exp), RANKUP_EXPERIENCE);
		if (experience >= RANKUP_EXPERIENCE && job.getAdvancement() != null) {
			this.changeClass(job.getAdvancement());
			this.setHealth((int) job.getMaxHealth());
			this.mana = (int) MAX_MANA;
		}
	}

	public void lunge(float delay, Entity at) {
		// 0.4 seconds
		Vector2 v = dir.getDirVector();
		this.addAction(Actions.sequence(Actions.delay(delay), Actions.moveBy(15 * v.x, 15 * v.y, .2f, Interpolation.pow5In), Actions.moveBy(-15 * v.x, -15 * v.y, .2f, Interpolation.pow5Out)));
	}

	public EntityClass getEntityClass() {
		return job;
	}

	public String toString() {
		return job.toString() + " " + team + " (" + getXCoord() + ", " + getYCoord() + ")";
	}

	public void update(float delta) {
		if (path != null) {
			float dx = path.i * TileMap.SIZE - renderX;
			if (dx < -1) {
				renderX -= delta * MOVE_SPEED;
				return;
			} else if (dx > 1) {
				renderX += delta * MOVE_SPEED;
				return;
			} else {
				renderX = path.i * TileMap.SIZE;
				float dy = path.j * TileMap.SIZE - renderY;
				if (dy < -1) {
					renderY -= delta * MOVE_SPEED;
					return;
				} else if (dy > 1) {
					renderY += delta * MOVE_SPEED;
					return;
				} else {
					renderY = path.j * TileMap.SIZE;
					path = path.parent;
				}
			}
		} else {
			return;
		}
		if (path != null && gameState.getEntities().getFog(path.i, path.j) != null && gameState.getEntities().getFog(path.i, path.j).team != gameState.getActiveTeam()) {
			path = null;
			skill = SkillManager.getSkill(SkillManager.WAIT);
		}
		if (path == null) {
			gameState.getEntities().removeEntity(x, y);
			gameState.getEntities().addEntity(this, this.getXCoord(), this.getYCoord());
			if (skill == null) {
				openActionMenu();
			} else {
				if (skill instanceof TargetSkill) {
					TargetSkill s = (TargetSkill) skill;
					s.targetEffect(gameState, this, targetX, targetY);
				} else {
					skill.onSelect(gameState, this);
				}
				if (skill.equals(SkillManager.getSkill(SkillManager.WAIT))) {
					ActionQueue.get().endAction();
				} else {
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							ActionQueue.get().endAction();
						}
					}, 1f);
				}
				ActionQueue.get().nextAction();
				ActionQueue.get().checkLocked();
			}
		}
	}

	public void openActionMenu() {
		List<Skill> options = getEntityClass().getSkills().stream().filter(skill -> skill.requiremendtsMet(gameState, this)).collect(Collectors.toList());
		HUD.get().getActionMenu().open(options);
	}

	public void move(Cell p, Skill skill, int x2, int y2) {
		if (path != null) {
			return;
		}
		x = this.getXCoord();
		y = this.getYCoord();
		this.path = p;
		this.skill = skill;
		this.targetX = x2;
		this.targetY = y2;
	}

	public int getXCoord() {
		return (int) (renderX / TileMap.SIZE);
	}

	public int getYCoord() {
		return (int) (renderY / TileMap.SIZE);
	}

	public boolean remove() {
		gameState.getEntities().removeEntity(getXCoord(), getYCoord());
		return true;
	}

	public void addBuff(Buff buff) {
		for (int i = 0; i < buffs.size(); i++) {
			if (buffs.get(i).getType() == buff.getType()) {
				buffs.remove(i--);
			}
		}
		this.buffs.add(buff);
	}

	public void updateBuffs() {
		mana = (int) Math.min(MAX_MANA, mana += 1);
		job.update(this);
		for (int i = 0; i < buffs.size(); i++) {
			if (buffs.get(i).update()) {
				buffs.remove(i--);
			}
		}
	}

	public boolean hasBuff(BuffType type) {
		for (Buff buff : buffs) {
			if (buff.isType(type)) {
				return true;
			}
		}
		return false;
	}

	public Buff getBuff(BuffType type) {
		for (Buff buff : buffs) {
			if (buff.isType(type)) {
				return buff;
			}
		}
		return null;
	}

	public void changeClass(EntityClass job) {
		this.job = job;
	}

	public int calcDamageTaken(Entity attacker) {
		int dmg = job.calcDamage(attacker, gameState.getMap());
		if (hasBuff(BuffManager.get().blessed)) {
			dmg -= 1;
			this.getBuff(BuffManager.get().blessed).activate();
		}
		if (attacker.hasBuff(BuffManager.get().poisoned)) {
			dmg -= 1;
			attacker.getBuff(BuffManager.get().blessed).activate();
		}
		Entity building = gameState.getEntities().getBuilding(getXCoord(), getYCoord());
		if (building == null)
			return Math.max(0, dmg);
		if (building.getEntityClass().equals(EntityClassManager.get().castle)) {
			dmg -= 2;
		} else if (building.getEntityClass().equals(EntityClassManager.get().fort)) {
			dmg -= 1;
		}
		return Math.max(0, dmg);
	}

	public void consumeMana(int manaCost) {
		mana -= manaCost;
	}

	public int getMana() {
		return mana;
	}

	public int getExp() {
		return experience;
	}

	public int getDefense() {
		return job.getDefense();
	}

	public ArrayList<Buff> getBuffs() {
		return buffs;
	}
}
