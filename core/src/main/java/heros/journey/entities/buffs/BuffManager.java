package heros.journey.entities.buffs;

public class BuffManager {

	public BuffType parry, rallied, blessed, poisoned;

	private static BuffManager buffManager;

	public static BuffManager get() {
		if (buffManager == null)
			buffManager = new BuffManager();
		return buffManager;
	}

	private BuffManager() {
	}

}
