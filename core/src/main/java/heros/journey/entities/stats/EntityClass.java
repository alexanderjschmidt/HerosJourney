package heros.journey.entities.stats;

public class EntityClass {

	private String name;

	public EntityClass(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public int getMoveDistance() {
		return 5;
	}

}
