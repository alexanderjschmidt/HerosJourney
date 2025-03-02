package fe.game.entities.skills;

import fe.game.GameState;
import fe.game.entities.Entity;

public abstract class Skill {

	private String name;
	private int manaCost;

	public Skill(String name, int manaCost, boolean teamSkill) {
		this.name = name;
		this.manaCost = manaCost;
		SkillManager.get().put(name, this);
		if (teamSkill)
			SkillManager.get().addTeamSkill(this);
	}

	public Skill(String name, int manaCost) {
		this(name, manaCost, false);
	}

	public Skill(String name, boolean teamSkill) {
		this(name, 0, teamSkill);
	}

	public Skill(String name) {
		this(name, 0, false);
	}

	public abstract boolean requiremendtsMet(GameState gameState, Entity selected);

	public void onHover(GameState gameState, Entity selected) {
		gameState.getRangeManager().clearRange();
	}

	public abstract void onSelect(GameState gameState, Entity selected);

	public String toString() {
		return name;
	}

	public void consumeMana(Entity e) {
		e.consumeMana(manaCost);
	}

	public boolean hasMana(Entity e) {
		return e.getMana() >= manaCost;
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
