package fe.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fe.game.entities.skills.Skill;
import fe.game.entities.skills.SkillManager;
import fe.game.tilemap.TileMap;
import fe.game.tilemap.tiles.Tile;

public class EntityClass {

	private int moveDist = 5;
	private int[] ranges = { 1, 2, 7 };
	private float maxHealth = 5;
	private int damage = 1;
	private int defense = 1;
	private int vision = 2;
	private int value = 0;
	private ArrayList<Skill> skills;

	private String name;
	private TextureRegion sprite;
	private EntityClass advancement;
	private String description;

	public EntityClass(String name, String description, TextureRegion sprite, int maxHealth, int damage, int defense, int[] ranges, int moveDist, int vision, int value, EntityClass advancement,
			Skill... skills) {
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
		this.skills = new ArrayList<Skill>();
		this.skills.add(SkillManager.getSkill(SkillManager.WAIT));
		for (Skill skill : skills) {
			this.skills.add(skill);
		}
		if (ranges.length > 0) {
			this.skills.add(SkillManager.getSkill(SkillManager.ATTACK));
		}
		EntityClassManager.get().put(name, this);
	}

	public EntityClass(String name, String description, TextureRegion sprite, int maxHealth, int damage, int defense, int[] ranges, int moveDist, int vision, int value, Skill... skills) {
		this(name, description, sprite, maxHealth, damage, defense, ranges, moveDist, vision, value, null, skills);
	}

	public void render(Batch batch, float x, float y, float elapsedTime) {
		batch.draw(sprite, x, y, TileMap.SIZE, TileMap.SIZE);
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

	public ArrayList<Skill> getSkills() {
		return skills;
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
