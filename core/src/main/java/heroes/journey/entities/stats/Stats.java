package heroes.journey.entities.stats;

import java.util.HashMap;
import java.util.Map;

public class Stats {

    public static final float MAX_HEALTH = 100, MAX_MANA = 100;

	private int body, mind;
    private int fame;

    private int health;
    private int mana;

    private Map<DamageType, Integer> resistancesFlat;
    private Map<DamageType, Float> resistancesPercentage;

	public Stats() {
		body = 1;
		mind = 1;
        fame = 0;
        resistancesFlat = new HashMap<DamageType, Integer>();
        resistancesPercentage = new HashMap<DamageType, Float>();
		for (DamageType type : DamageTypeManager.get().values()) {
            resistancesFlat.put(type, 0);
            resistancesPercentage.put(type, 0f);
		}
        health = (int)MAX_HEALTH;
        mana = (int)MAX_MANA;
	}

    public int getBody() {
        return body;
    }

    public void setBody(int body) {
        this.body = body;
    }

    public int getMind() {
        return mind;
    }

    public void setMind(int mind) {
        this.mind = mind;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public int getHealth() {
        return health;
    }

    // Returns if they are Alive
    public boolean adjustHealth(int health) {
        this.health = (int) Math.min(MAX_HEALTH, Math.max(0, this.health + health));
        return (this.health + health > 0);
    }

    public int getMana() {
        return mana;
    }

    // Returns if they can use the spell
    public boolean adjustMana(int mana) {
        if (this.mana + mana < 0) {
            return false;
        }
        this.mana = (int) Math.min(MAX_MANA, Math.max(0, this.mana + mana));
        return true;
    }

    public Map<DamageType,Integer> getResistancesFlat() {
        return resistancesFlat;
    }

    public Map<DamageType,Float> getResistancesPercentage() {
        return resistancesPercentage;
    }

    public Stats clone() {
        Stats stats = new Stats();
        stats.body = body;
        stats.mind = mind;
        stats.fame = fame;
        stats.mana = mana;
        stats.health = health;
        return stats;
    }

}
