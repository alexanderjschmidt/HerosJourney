package heros.journey.entities.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import heros.journey.GameState;
import heros.journey.entities.Effect;
import heros.journey.entities.Entity;
import heros.journey.entities.EntityManager;
import heros.journey.entities.buffs.Buff;
import heros.journey.entities.buffs.BuffManager;
import heros.journey.managers.RangeManager.RangeColor;
import heros.journey.utils.art.ResourceManager;
import heros.journey.screens.BattleScreen;
import heros.journey.screens.MainMenuScreen;
import heros.journey.ui.HUD;
import heros.journey.ui.HUD.HUDState;

public class ActionManager extends HashMap<String, Action> {

	private static final long serialVersionUID = 1L;
	public static String WAIT = "Wait", END_TURN = "End Turn", EXIT_GAME = "Exit Game", ATTACK = "Attack";

	private List<Action> teamActions;

	private static ActionManager actionManager;

	public static ActionManager get() {
		if (actionManager == null)
			actionManager = new ActionManager();
		return actionManager;
	}

	private ActionManager() {
		teamActions = new ArrayList<Action>();
	}

	public void createBaseActions(BattleScreen battleScreen) {
		new Action(END_TURN, true) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				gameState.nextTurn();
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		new Action(EXIT_GAME, true) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				battleScreen.getApp().setScreen(new MainMenuScreen(battleScreen.getApp()));
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		new Action(WAIT) {
			@Override
			public void onSelect(GameState gameState, Entity selected) {
				HUD.get().getCursor().clearSelected(true);
			}

			@Override
			public boolean requiremendtsMet(GameState gameState, Entity selected) {
				return true;
			}
		};
		new TargetAction(ATTACK, 0, null, RangeColor.RED, true) {
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

	public List<Action> getTeamActions(GameState gameState, int x, int y) {
		ArrayList<Action> options = new ArrayList<Action>(teamActions.size());
		Entity e = new Entity(x, y);
		for (Action action : teamActions) {
			if (action.requiremendtsMet(gameState, e))
				options.add(action);
		}
		return options;
	}

	public static Action getAction(String actionName) {
		Action action = get().get(actionName);
		if (action == null)
			System.out.println("SKILL NOT FOUND");
		return action;
	}

	public void addTeamAction(Action action) {
		teamActions.add(action);
	}

}
