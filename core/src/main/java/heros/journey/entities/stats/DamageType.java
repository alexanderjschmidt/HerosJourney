package heros.journey.entities.stats;

public class DamageType {

    private String name;

    public DamageType(String name) {
        this.name = name;
        DamageTypeManager.get().put(name, this);
    }

    @Override
    public String toString() {
        return name;
    }
}
