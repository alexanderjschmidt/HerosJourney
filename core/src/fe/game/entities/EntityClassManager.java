package fe.game.entities;

import java.util.HashMap;

import fe.game.entities.skills.SkillManagerOld;
import fe.game.managers.ResourceManager;
import fe.game.tilemap.TileMap;
import fe.game.tilemap.tiles.Tile;

public class EntityClassManager extends HashMap<String, EntityClass> {

	private static final long serialVersionUID = 1L;
	public EntityClass solider, archer, knight, assassin, cavalry, pegasus, fencer, healer, mage, priest, alchemist;
	public EntityClass hero_solider, hero_archer, hero_knight, hero_assassin, hero_cavalry, hero_pegasus, hero_fencer, hero_healer, hero_mage, hero_priest, hero_alchemist;
	public EntityClass balista, balista_moving, commander, castle, fort;

	private static EntityClassManager entityClassManager;

	public static EntityClassManager get() {
		if (entityClassManager == null)
			entityClassManager = new EntityClassManager();
		return entityClassManager;
	}

	private EntityClassManager() {
		commander = new EntityClass("Commander",
				"The leader of your army can build 5 forts allowing you to bring troops to the front lines and setup a defensive spot, or they can ralley troops to either move more or again.",
				ResourceManager.get().sprites[1][1], 10, 0, 0, new int[] {}, 5, 5, 30, SkillManagerOld.rallyTroops);
		balista = new EntityClass("Balista", "This ranged unit can lock its position in exchange for a heavy ranged attack.", ResourceManager.get().sprites[2][8], 5, 5, 1, new int[] { 4, 5, 6, 7, 8 },
				0, 5, 15, SkillManagerOld.dismountBalista);
		balista_moving = new EntityClass("Balista", "This ranged unit can lock its position in exchange for a heavy ranged attack.", ResourceManager.get().sprites[1][8], 5, 5, 0, new int[] {}, 3, 2,
				15, SkillManagerOld.mountBalista);

		hero_solider = new EntityClass("Hero Solider", "", ResourceManager.get().sprites[2][2], 10, 2, 0, new int[] { 1 }, 5, 2, 5);
		hero_archer = new EntityClass("Hero Archer", "", ResourceManager.get().sprites[2][3], 7, 2, 0, new int[] { 2, 3, 4, 5 }, 5, 3, 5);
		hero_knight = new EntityClass("Hero Knight", "", ResourceManager.get().sprites[2][4], 10, 1, 1, new int[] { 1 }, 4, 2, 10);
		hero_assassin = new EntityClass("Hero Assassin", "", ResourceManager.get().sprites[2][5], 7, 1, 0, new int[] { 1 }, 6, 4, 15);
		hero_cavalry = new EntityClass("Hero Cavalry", "", ResourceManager.get().sprites[2][6], 10, 2, 0, new int[] { 1 }, 7, 3, 10) {
			public int getTerrainCost(Tile tile) {
				return tile.toString().equals("Mountain") ? 1000 : tile.terrainCost;
			}

			public int getTreesCost() {
				return 2;
			}
		};
		hero_pegasus = new EntityClass("Hero Pegasus", "", ResourceManager.get().sprites[2][7], 7, 2, 0, new int[] { 1 }, 7, 4, 10) {
			public int getTerrainCost(Tile tile) {
				return 1;
			}

			public int getTreesCost() {
				return 0;
			}
		};
		hero_fencer = new EntityClass("Hero Fencer", "", ResourceManager.get().sprites[2][9], 7, 2, 0, new int[] { 1 }, 5, 2, 15, SkillManagerOld.parry);
		hero_healer = new EntityClass("Hero Healer", "", ResourceManager.get().sprites[2][10], 5, 0, 0, new int[] {}, 5, 1, 15, SkillManagerOld.heal);
		hero_mage = new EntityClass("Hero Mage", "", ResourceManager.get().sprites[2][11], 5, 0, 0, new int[] {}, 5, 3, 15, SkillManagerOld.magicMissile);
		hero_priest = new EntityClass("Hero Priest", "This supportive unit can bless allied targets to boost their attack and defense for three turns.", ResourceManager.get().sprites[2][13], 7, 0, 0,
				new int[] {}, 5, 1, 15, hero_priest, SkillManagerOld.bless);
		hero_alchemist = new EntityClass("Hero Alchemist",
				"This supportive unit can poison enemy targets to reduce their standard attack damage for 3 turns. It also uses its potions to heal a small amount each turn.",
				ResourceManager.get().sprites[2][14], 10, 0, 0, new int[] {}, 5, 1, 15, hero_alchemist, SkillManagerOld.poison) {
			public void update(Entity e) {
				e.adjustHealth(null, 0, 1);// heals 1 each turn passive buff
			}
		};

		solider = new EntityClass("Solider", "This unit is your bread and butter. While nothing fancy, there cheap cost makes them useful for mass production.", ResourceManager.get().sprites[1][2],
				10, 2, 0, new int[] { 1 }, 5, 2, 5, hero_solider);
		archer = new EntityClass("Archer", "This ranged unit can attack from a distance and takes no penalty for moving through trees.", ResourceManager.get().sprites[1][3], 7, 2, 0,
				new int[] { 2, 3, 4 }, 5, 3, 5, hero_archer) {
			public int getTreesCost() {
				return 0;
			}
		};
		knight = new EntityClass("Knight", "This defensive unit takes reduced frontal damage and flanking damage. However, it lacks attacking power.", ResourceManager.get().sprites[1][4], 10, 1, 0,
				new int[] { 1 }, 4, 2, 10, hero_knight);
		assassin = new EntityClass("Assassin", "This offensive unit makes up for its weak base attack with significant bonus backstab damage.", ResourceManager.get().sprites[1][5], 7, 1, 0,
				new int[] { 1 }, 6, 4, 15, hero_assassin);
		cavalry = new EntityClass("Cavalry", "This mounted unit rides its horse into battle dealing additional flanking damage. However, it cannot cross mountains.",
				ResourceManager.get().sprites[1][6], 10, 2, 0, new int[] { 1 }, 7, 3, 10, hero_cavalry) {

			public int getTerrainCost(Tile tile) {
				return tile.toString().equals("Mountain") ? 1000 : tile.terrainCost;
			}

			public int getTreesCost() {
				return 2;
			}
		};
		pegasus = new EntityClass("Pegasus", "This mounted unit uses its pegasus to fly over all terrain.", ResourceManager.get().sprites[1][7], 7, 2, 0, new int[] { 1 }, 7, 4, 10, hero_pegasus) {
			public int getTerrainCost(Tile tile) {
				return 1;
			}

			public int getTreesCost() {
				return 0;
			}
		};
		fencer = new EntityClass("Fencer",
				"This defensive unit uses its speed to dodge all standard ranged attacks. It also uses its skill to reduce frontal damage and can parry incoming melee attacks.",
				ResourceManager.get().sprites[1][9], 7, 1, 0, new int[] { 1 }, 5, 2, 15, hero_fencer, SkillManagerOld.parry) {
			public int calcDamage(Entity defender, Entity attacker, TileMap map) {
				int dist = Math.abs(attacker.getXCoord() - defender.getXCoord()) + Math.abs(attacker.getYCoord() - defender.getYCoord());
				if (dist > 1) {
					return 0;
				}
				int damageTakenCalc = attacker.getEntityClass().getDamage(map);
				return damageTakenCalc - defender.getDefense();
			}
		};
		mage = new EntityClass("Mage", "This ranged unit can use magic to hit enemies from a distance piercing through armor and guarateeing a hit.", ResourceManager.get().sprites[1][11], 5, 0, 0,
				new int[] {}, 5, 3, 15, hero_mage, SkillManagerOld.magicMissile);
		healer = new EntityClass("Healer", "This supportive unit can heal allies.", ResourceManager.get().sprites[1][10], 5, 0, 0, new int[] {}, 5, 1, 15, hero_healer, SkillManagerOld.heal);
		priest = new EntityClass("Priest", "This supportive unit can bless allied targets to boost their attack and defense for three turns.", ResourceManager.get().sprites[1][13], 7, 0, 0,
				new int[] {}, 5, 1, 15, hero_priest, SkillManagerOld.bless);
		alchemist = new EntityClass("Alchemist",
				"This supportive unit can poison enemy targets to reduce their standard attack damage for 3 turns. It also uses its potions to heal a small amount each turn.",
				ResourceManager.get().sprites[1][14], 10, 0, 0, new int[] {}, 5, 1, 15, hero_alchemist, SkillManagerOld.poison) {
			public void update(Entity e) {
				e.adjustHealth(null, 0, 1);// heals 1 each turn passive buff
			}
		};
	}

	public void createBuildings() {
		castle = new EntityClass("Castle", "Primary defensive structure giving 2 bonus defense to units on top of it. Allows you to spawn units.", ResourceManager.get().sprites[3][2], 10, 0, 0,
				new int[] { 0 }, 0, 5, 0, null, SkillManagerOld.fortSpawnSkills);
		fort = new EntityClass("Fort", "Secondary defensive structure giving 1 bonus defense to units on top of it. Allows you to spawn units.", ResourceManager.get().sprites[3][3], 10, 0, 0,
				new int[] {}, 0, 4, 0, null, SkillManagerOld.fortSpawnSkills);
	}

}
