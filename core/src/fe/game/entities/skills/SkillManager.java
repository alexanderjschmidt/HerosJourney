package fe.game.entities.skills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fe.game.GameState;
import fe.game.entities.Effect;
import fe.game.entities.Entity;
import fe.game.entities.EntityManager;
import fe.game.entities.buffs.Buff;
import fe.game.entities.buffs.BuffManager;
import fe.game.managers.RangeManager.RangeColor;
import fe.game.managers.ResourceManager;
import fe.game.screens.BattleScreen;
import fe.game.screens.MainMenuScreen;
import fe.game.ui.HUD;
import fe.game.ui.HUD.HUDState;

public class SkillManager extends HashMap<String, Skill> {

	private static final long serialVersionUID = 1L;
	public static String WAIT = "Wait", END_TURN = "End Turn", EXIT_GAME = "Exit Game", ATTACK = "Attack";

	private List<Skill> teamSkills;

	private static SkillManager skillManager;

	public static SkillManager get() {
		if (skillManager == null)
			skillManager = new SkillManager();
		return skillManager;
	}

	private SkillManager() {
		teamSkills = new ArrayList<Skill>();
	}

	public void createBaseSkills(BattleScreen battleScreen) {
		new Skill(END_TURN, true) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				gameState.nextTurn();
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return gameState.getActiveTeam().castles == 1;
			}
		};
		new Skill(EXIT_GAME, true) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				battleScreen.getApp().setScreen(new MainMenuScreen(battleScreen.getApp()));
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		new Skill(WAIT) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		new TargetSkill(ATTACK, 0, null, RangeColor.RED, true) {
			@Override
			public void onHover(GameState gameState, Entity selected) {
				Entity e = selected;
				gameState.getRangeManager().clearRange();
				gameState.getRangeManager().setDistanceRangeAt((int) e.getXCoord(), (int) e.getYCoord(), e.getEntityClass().getRanges(), rangeType);
			}

			public void onSelect(GameState gameState, Entity selected) {
				HUD.get().setState(HUDState.TARGET);
				gameState.getRangeManager().updateTargets(selected, true, selected.getEntityClass().getRanges(), rangeType);
				gameState.getRangeManager().pointAtTarget(0);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				Entity e = selected;
				return selected.getEntityClass().getRanges().length > 0 && gameState.getRangeManager().updateTargets(e, true, e.getEntityClass().getRanges(), rangeType).size() > 0;
			}

			@Override
			public void targetEffect(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity attacker = selected;
				Entity defender = gameState.getEntities().get(targetX, targetY);
				// System.out.println(gameState.getEntities().getBuilding(targetX, targetY));
				int damageTaken = defender.calcDamageTaken(attacker);

				Buff parry = defender.getBuff(BuffManager.get().parry);
				if (parry != null && parry.isActive() && EntityManager.getDistanceBetween(attacker, defender) == 1) {
					parry.activate(gameState, selected, damageTaken, damageTaken);
					damageTaken -= 1;
				}

				if (EntityManager.getDistanceBetween(attacker, defender) == 1) {
					attacker.lunge(0, defender);
					gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().slash[(int) (Math.random() * 6)], defender.renderX, defender.renderY));
				} else {
					gameState.getEntities().addEffect(0, new Effect(ResourceManager.get().arrow, attacker.renderX, attacker.renderY, defender.renderX, defender.renderY, true));
				}
				if (damageTaken > 0) {
					defender.vibrate(.2f, attacker);
					defender.adjustHealth(attacker, .2f, -damageTaken);
				}
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY) {
				Entity attacker = selected;
				Entity defender = gameState.getEntities().get(targetX, targetY);
				int damage = defender.calcDamageTaken(attacker);
				String message = "Attack for " + damage + " damage";
				return message;
			}

			@Override
			public int utilityFunc(Entity user, Entity e) {
				return (int) (user.getTeam() != e.getTeam() && e.calcDamageTaken(user) != 0 ? e.getEntityClass().getMaxHealth() / e.getHealth() : 0);
			}
		};
	}

	public List<Skill> getTeamSkills(GameState gameState, int x, int y) {
		ArrayList<Skill> options = new ArrayList<Skill>(teamSkills.size());
		Entity e = new Entity(x, y);
		for (Skill skill : teamSkills) {
			if (skill.requiremendtsMet(gameState, e))
				options.add(skill);
		}
		return options;
	}

	public static Skill getSkill(String skillName) {
		Skill skill = skillManager.get(skillName);
		if (skill == null)
			System.out.println("SKILL NOT FOUND");
		return skill;
	}

	public void addTeamSkill(Skill skill) {
		teamSkills.add(skill);
	}

}
