package heros.journey.entities.stats;

import java.util.HashMap;
import java.util.Map;

public class Stats {

	private int level, experience;
	private int strength, vitality, dexterity, intelligence, wisdom, charisma;
	private int freePoints;

	private Map<DamageType, Integer> resistances;

	private int reputation;

	private int currentHealth;

	public Stats() {
		level = 1;
		experience = 0;
		strength = 1;
		vitality = 1;
		dexterity = 1;
		intelligence = 1;
		wisdom = 1;
		charisma = 1;
		resistances = new HashMap<DamageType, Integer>();
		for (DamageType type : DamageType.values()) {
			resistances.put(type, 0);
		}
		currentHealth = getMaxHealth();
		reputation = 0;
	}

	public void addExp(int exp) {
		experience += exp;
		if (experience >= 100) {
			experience -= 100;
			level++;
		}
	}

	public int getMaxHealth() {
		return vitality * 10;
	}

}
