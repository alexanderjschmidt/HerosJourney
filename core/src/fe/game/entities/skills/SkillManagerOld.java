package fe.game.entities.skills;

import fe.game.GameState;
import fe.game.entities.Building;
import fe.game.entities.Effect;
import fe.game.entities.Entity;
import fe.game.entities.EntityClass;
import fe.game.entities.EntityClassManager;
import fe.game.entities.Team;
import fe.game.entities.buffs.Buff;
import fe.game.entities.buffs.BuffManager;
import fe.game.managers.RangeManager.RangeColor;
import fe.game.managers.ResourceManager;
import fe.game.tilemap.Tileset;
import fe.game.ui.HUD;
import fe.game.ui.HUD.HUDState;

public class SkillManagerOld {

	private static final long serialVersionUID = 1L;
	public static String BUILD_FORT = "Build Fort", BUILD_CASTLE = "Build Castle", SPAWN_SOLIDER = "Spawn Solider", SPAWN_ARCHER = "Spawn Archer";
	public static Skill mountBalista, dismountBalista, parry, buildFort, buildCastle;
	public static TargetSkill heal, grandHeal, rallyTroops, magicMissile, bless, poison;
	public static Skill[] initialSpawnSkills;
	public static Skill[] fortSpawnSkills;

	public static void createSkills() {
		createNonTargetSkills();
		createTargetSkills();
		createSpawnSkills();
	}

	private static void createNonTargetSkills() {
		buildCastle = new Skill(BUILD_CASTLE, true) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				Entity castle = new Building(EntityClassManager.get().castle, gameState.getActiveTeam(), gameState);
				Entity commander = new Entity(EntityClassManager.get().commander, gameState.getActiveTeam(), gameState);
				gameState.getEntities().addBuilding(castle, selected.getXCoord(), selected.getYCoord());
				gameState.getEntities().addEntity(commander, selected.getXCoord(), selected.getYCoord());

				HUD.get().setState(HUDState.CURSOR_MOVE);
				gameState.getActiveTeam().castles++;
				gameState.getActiveTeam().setCastle(castle);

			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				int x = selected.getXCoord();
				int y = selected.getYCoord();
				return gameState.getEntities().getBuilding(x, y) == null && gameState.getActiveTeam().castles < 1 && gameState.getTurn() == 0 && gameState.getMap().get(x, y) != Tileset.MOUNTAINS
						&& gameState.getMap().get(x, y) != Tileset.WATER && gameState.getActiveTeam().inTerritory(x, y);
			}
		};
		buildFort = new Skill(BUILD_FORT, true) {

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				int x = selected.getXCoord();
				int y = selected.getYCoord();
				return gameState.getEntities().getBuilding(x, y) == null && gameState.getActiveTeam().castles == 1 && gameState.getActiveTeam().forts < Team.FORT_CAP && gameState.getTurn() == 0
						&& gameState.getMap().get(x, y) != Tileset.MOUNTAINS && gameState.getMap().get(x, y) != Tileset.WATER && gameState.getActiveTeam().inTerritory(x, y);
			}

			@Override
			public void onSelect(GameState gameState, Entity selected) {
				Entity fort = new Building(EntityClassManager.get().fort, gameState.getActiveTeam(), gameState);
				gameState.getEntities().addBuilding(fort, selected.getXCoord(), selected.getYCoord());
				HUD.get().setState(HUDState.CURSOR_MOVE);
				gameState.getActiveTeam().forts++;
			}
		};
		mountBalista = new Skill("Mount Balista") {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				selected.changeClass(EntityClassManager.get().balista);
				selected.used = true;
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		dismountBalista = new Skill("Dismount Balista") {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				int x = (int) (selected.getXCoord());
				int y = (int) (selected.getYCoord());
				Entity e = gameState.getEntities().get(x, y);
				e.changeClass(EntityClassManager.get().balista_moving);
				e.used = true;
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		parry = new Skill("Parry", 3) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				int x = (int) (selected.getXCoord());
				int y = (int) (selected.getYCoord());
				Entity fencer = gameState.getEntities().get(x, y);
				fencer.addBuff(new Buff(BuffManager.get().parry));
				HUD.get().getCursor().clearSelected(true);
				this.consumeMana(fencer);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				Entity fencer = selected;
				return !fencer.hasBuff(BuffManager.get().parry) && this.hasMana(fencer);
			}
		};
	}

	private static void createTargetSkills() {
		class HealSkill extends TargetSkill {

			public int healAmount = 5;

			public HealSkill(String name, int manaCost, int[] range, int healAmount) {
				super(name, manaCost, range, RangeColor.GREEN, true);
				this.healAmount = healAmount;
			}

			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity e = gameState.getEntities().get(targetX, targetY);
				int currentHealth = e.getHealth();
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().heal, e.renderX, e.renderY));
				e.adjustHealth(null, 0, healAmount);
				int exp = e.getHealth() - currentHealth;
				selected.addExp(exp);
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				String message = "Heal for " + healAmount + " health";
				return message;
			}
		}
		heal = new HealSkill("Heal", 1, new int[] { 1 }, 5);
		grandHeal = new HealSkill("Grand Heal", 3, new int[] { 1, 2, 3 }, 100);
		rallyTroops = new TargetSkill("Rally", 0, new int[] { 1 }, RangeColor.TEAL, true) {
			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity e = gameState.getEntities().get(targetX, targetY);
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().rally, e.renderX, e.renderY));
				if (e.used)
					e.used = false;
				else
					e.addBuff(new Buff(BuffManager.get().rallied));
				HUD.get().getCursor().clearSelected(true);

			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				return "Rally unit to fight on.";
			}

			@Override
			public int utilityFunc(Entity user, Entity e) {
				if (e == user || e.getTeam() != user.getTeam()) {
					return 0;
				}
				return e.used ? 10 : 2;
			}

		};
		magicMissile = new TargetSkill("Magic Missile", 3, new int[] { 2, 3, 4, 5, 6 }, RangeColor.RED, true) {

			int damage = 4;

			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity defender = gameState.getEntities().get(targetX, targetY);
				Entity attacker = selected;
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().magicMissile, attacker.renderX, attacker.renderY, defender.renderX, defender.renderY, true));
				final int damageTaken = damage;

				defender.vibrate(.2f, attacker);
				defender.adjustHealth(attacker, .2f, -damageTaken);
				attacker.addExp(4);
				this.consumeMana(attacker);
				HUD.get().getCursor().clearSelected(true);

			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				String message = "Magic attack for " + damage + " damage";
				return message;
			}
		};
		bless = new TargetSkill("Bless", 3, new int[] { 1 }, RangeColor.TEAL, true) {
			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity e = gameState.getEntities().get(targetX, targetY);
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().bless, e.renderX, e.renderY));
				e.addBuff(new Buff(BuffManager.get().blessed));
				selected.addExp(4);
				this.consumeMana(selected);
				HUD.get().getCursor().clearSelected(true);

			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				return "Bless unit.";
			}

		};
		poison = new TargetSkill("Posion", 3, new int[] { 1, 2, 3 }, RangeColor.RED, true) {
			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity e = gameState.getEntities().get(targetX, targetY);
				gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().poison, e.renderX, e.renderY));
				e.addBuff(new Buff(BuffManager.get().poisoned));
				selected.addExp(4);
				this.consumeMana(selected);
				HUD.get().getCursor().clearSelected(true);

			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				return "Posion unit.";
			}

		};
	}

	private static void createSpawnSkills() {
		initialSpawnSkills = new Skill[12];
		initialSpawnSkills[0] = getSpawnSkill(EntityClassManager.get().solider);
		initialSpawnSkills[1] = getSpawnSkill(EntityClassManager.get().archer);
		initialSpawnSkills[2] = getSpawnSkill(EntityClassManager.get().knight);
		initialSpawnSkills[3] = getSpawnSkill(EntityClassManager.get().cavalry);
		initialSpawnSkills[4] = getSpawnSkill(EntityClassManager.get().pegasus);
		initialSpawnSkills[5] = getSpawnSkill(EntityClassManager.get().healer);
		initialSpawnSkills[6] = getSpawnSkill(EntityClassManager.get().assassin);
		initialSpawnSkills[7] = getSpawnSkill(EntityClassManager.get().balista_moving);
		initialSpawnSkills[8] = getSpawnSkill(EntityClassManager.get().fencer);
		initialSpawnSkills[9] = getSpawnSkill(EntityClassManager.get().mage);
		initialSpawnSkills[10] = getSpawnSkill(EntityClassManager.get().priest);
		initialSpawnSkills[11] = getSpawnSkill(EntityClassManager.get().alchemist);

		fortSpawnSkills = new Skill[12];
		fortSpawnSkills[0] = getSpawnSkillFort(EntityClassManager.get().solider);
		fortSpawnSkills[1] = getSpawnSkillFort(EntityClassManager.get().archer);
		fortSpawnSkills[2] = getSpawnSkillFort(EntityClassManager.get().knight);
		fortSpawnSkills[3] = getSpawnSkillFort(EntityClassManager.get().cavalry);
		fortSpawnSkills[4] = getSpawnSkillFort(EntityClassManager.get().pegasus);
		fortSpawnSkills[5] = getSpawnSkillFort(EntityClassManager.get().healer);
		fortSpawnSkills[6] = getSpawnSkillFort(EntityClassManager.get().assassin);
		fortSpawnSkills[7] = getSpawnSkillFort(EntityClassManager.get().balista_moving);
		fortSpawnSkills[8] = getSpawnSkillFort(EntityClassManager.get().fencer);
		fortSpawnSkills[9] = getSpawnSkillFort(EntityClassManager.get().mage);
		fortSpawnSkills[10] = getSpawnSkillFort(EntityClassManager.get().priest);
		fortSpawnSkills[11] = getSpawnSkillFort(EntityClassManager.get().alchemist);
	}

	private static Skill getSpawnSkill(EntityClass entityClass) {
		Skill skill = new Skill("Spawn " + entityClass.toString(), true) {
			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				int x = selected.getXCoord();
				int y = selected.getYCoord();
				return gameState.getTurn() == 0 && gameState.getActiveTeam().castles == 1 && gameState.getEntities().get(x, y) == null
						&& gameState.getActiveTeam().currentTroops <= Team.TROOP_CAP - entityClass.getValue() && entityClass.getTerrainCost(gameState.getMap().getTerrain(x, y)) < 10
						&& gameState.getActiveTeam().inTerritory(x, y);
			}

			@Override
			public void onHover(GameState gameState, Entity selected) {
				gameState.getRangeManager().clearRange();
				HUD.get().showJobInfo(entityClass);
			}

			@Override
			public void onSelect(GameState gameState, Entity selected) {
				gameState.getActiveTeam().currentTroops += entityClass.getValue();
				Entity e = new Entity(entityClass, gameState.getActiveTeam(), gameState);
				e.used = true;
				gameState.getEntities().addEntity(e, selected.getXCoord(), selected.getYCoord());
				HUD.get().setState(HUDState.CURSOR_MOVE);
				HUD.get().hideJobInfoUI();

				selected.used = false;
			}
		};
		return skill;
	}

	private static Skill getSpawnSkillFort(EntityClass entityClass) {
		Skill skill = new Skill("Spawn " + entityClass.toString(), 4) {

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				int x = selected.getXCoord();
				int y = selected.getYCoord();
				return gameState.getEntities().get(x, y) == null && gameState.getActiveTeam().currentTroops <= Team.TROOP_CAP - entityClass.getValue() && this.hasMana(selected);
			}

			@Override
			public void onHover(GameState gameState, Entity selected) {
				gameState.getRangeManager().clearRange();
				HUD.get().showJobInfo(entityClass);
			}

			@Override
			public void onSelect(GameState gameState, Entity selected) {
				gameState.getActiveTeam().currentTroops += entityClass.getValue();
				Entity e = new Entity(entityClass, gameState.getActiveTeam(), gameState);
				e.used = true;
				gameState.getEntities().addEntity(e, selected.getXCoord(), selected.getYCoord());
				HUD.get().setState(HUDState.CURSOR_MOVE);
				this.consumeMana(selected);
				HUD.get().hideJobInfoUI();

			}

		};
		return skill;
	}

}
