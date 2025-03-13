package heroes.journey.initializers.base;

import heroes.journey.entities.Loyalty;

public class Loyalties {

    public static Loyalty ENEMY, ALLY;

    static {
        ENEMY = new Loyalty("Enemy");
        ALLY = new Loyalty("Ally");
    }

}
