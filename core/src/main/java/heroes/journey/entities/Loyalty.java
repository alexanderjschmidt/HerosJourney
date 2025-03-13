package heroes.journey.entities;

public class Loyalty {

    private String name;

    public Loyalty(String name) {
        this.name = name;
        LoyaltyManager.get().put(name, this);
    }

    private String getName() {
        return name;
    }

    public boolean equals(Loyalty loyalty) {
        return loyalty.getName().equals(this.name);
    }
}
