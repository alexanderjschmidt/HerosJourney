package heros.journey.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heros.journey.GameCamera;
import heros.journey.entities.actions.Action;
import heros.journey.entities.actions.ActionManager;
import heros.journey.tilemap.TileMap;
import heros.journey.tilemap.tiles.Tile;

public class EntityClass {

	private int moveDist = 5;
	private int[] ranges = { 1, 2, 7 };
	private float maxHealth = 5;
	private int damage = 1;
	private int defense = 1;
	private int vision = 2;
	private int value = 0;
	private ArrayList<Action> actions;

	private String name;
	private TextureRegion sprite;
	private EntityClass advancement;
	private String description;

	public EntityClass(String name, String description, TextureRegion sprite, int maxHealth, int damage, int defense, int[] ranges, int moveDist, int vision, int value, EntityClass advancement,
			Action... actions) {
		this.name = name;
		this.description = description;
		this.sprite = sprite;
		this.advancement = advancement;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.defense = defense;
		this.ranges = ranges;
		this.moveDist = moveDist;
		this.vision = vision;
		this.value = value;
		this.actions = new ArrayList<Action>();
		this.actions.add(ActionManager.getAction(ActionManager.WAIT));
		for (Action action : actions) {
			this.actions.add(action);
		}
		if (ranges.length > 0) {
			this.actions.add(ActionManager.getAction(ActionManager.ATTACK));
		}
		EntityClassManager.get().put(name, this);
	}

	public EntityClass(String name, String description, TextureRegion sprite, int maxHealth, int damage, int defense, int[] ranges, int moveDist, int vision, int value, Action... actions) {
		this(name, description, sprite, maxHealth, damage, defense, ranges, moveDist, vision, value, null, actions);
	}

	public void render(Batch batch, float x, float y, float elapsedTime) {
		batch.draw(sprite, x, y, GameCamera.get().getSize(), GameCamera.get().getSize());
	}

	public void render(Batch batch, float x, float y, float width, float height, float elapsedTime) {
		batch.draw(sprite, x, y, width, height);
	}

	public String toString() {
		return name;
	}

	public int getMoveDistance() {
		return moveDist;
	}

	public int[] getRanges() {
		return ranges;
	}

	public float getMaxHealth() {
		return maxHealth;
	}

	public int getBaseDamage() {
		return damage;
	}

	public int getDefense() {
		return defense;
	}

	public int getVision() {
		return vision;
	}

	public int getValue() {
		return value;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public int getDamage(TileMap map) {
		int damageCalc = getBaseDamage();
		return damageCalc;
	}

	public int calcDamage(Entity attacker, TileMap map) {
		int damageTakenCalc = attacker.getEntityClass().getDamage(map);
		return Math.max(damageTakenCalc - defense, 0);
	}

	public void update(Entity e) {

	}

	public int getTerrainCost(Tile tile) {
		return tile.terrainCost;
	}

	public int getTreesCost() {
		return 1;
	}

	public EntityClass getAdvancement() {
		return advancement;
	}

	public String getDescription() {
		return description;
	}
}
