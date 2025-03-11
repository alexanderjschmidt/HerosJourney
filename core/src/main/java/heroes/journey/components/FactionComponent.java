package heroes.journey.components;

import com.badlogic.ashley.core.Component;
import heroes.journey.entities.factions.Faction;

import java.util.ArrayList;
import java.util.List;

public class FactionComponent implements Component {

    private List<Faction> factions;

    public FactionComponent() {
        this.factions = new ArrayList<>();
    }

    public FactionComponent addFaction(Faction faction) {
        factions.add(faction);
        return this;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public FactionComponent clone() {
        throw new RuntimeException("Not Implemented");
    }
}
