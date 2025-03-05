package heros.journey.entities.actions;

import heros.journey.GameState;
import heros.journey.entities.Entity;

public abstract class Action {

	private String name;
	private int manaCost;

	public Action(String name, int manaCost, boolean teamSkill) {
		this.name = name;
		this.manaCost = manaCost;
		ActionManager.get().put(name, this);
		if (teamSkill)
			ActionManager.get().addTeamAction(this);
	}

	public Action(String name, int manaCost) {
		this(name, manaCost, false);
	}

	public Action(String name, boolean teamSkill) {
		this(name, 0, teamSkill);
	}

	public Action(String name) {
		this(name, 0, false);
	}

	public abstract boolean requirementsMet(GameState gameState, Entity selected);

	public void onHover(GameState gameState, Entity selected) {
		gameState.getRangeManager().clearRange();
	}

	public abstract void onSelect(GameState gameState, Entity selected);

	public String toString() {
		return name;
	}

    public void setName(String name) {
        this.name = name;
    }

	public boolean hasMana(Entity e) {
		return e.getStats().getMana() >= manaCost;
	}

	/**
	 *
	 * @param user
	 *            of the skill
	 * @param e
	 *            the Entity being affected by the skill
	 * @return How valuable the skill is seen by the AI
	 */
	public int utilityFunc(Entity user, Entity e) {
		return 1;
	}

}
