package heroes.journey.entities;

import java.util.HashMap;

public class LoyaltyManager extends HashMap<String,Loyalty> {

    private static final long serialVersionUID = 1L;

    private static LoyaltyManager loyaltyManager;

    public static LoyaltyManager get() {
        if (loyaltyManager == null)
            loyaltyManager = new LoyaltyManager();
        return loyaltyManager;
    }

    private LoyaltyManager() {
    }

}
