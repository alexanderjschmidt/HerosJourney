package heroes.journey.utils.input;

import java.util.ArrayList;
import java.util.List;

import heroes.journey.GameState;
import heroes.journey.entities.Entity;
import heroes.journey.entities.actions.Action;
import heroes.journey.ui.HUD;

public class Options {

    public static boolean MAP_BLEND = true, AUTO_END_TURN = true;

    static {
        Action mapBlendAction = new Action("Blend Map: " + MAP_BLEND) {
            @Override
            public void onSelect(GameState gameState, Entity selected) {
                MAP_BLEND = !MAP_BLEND;
                this.setName("Blend Map: " + MAP_BLEND);
            }

            @Override
            public boolean requirementsMet(GameState gameState, Entity selected) {
                return false;
            }
        };
        Action autoEndTurnAction = new Action("Auto End Turn: " + AUTO_END_TURN) {
            @Override
            public void onSelect(GameState gameState, Entity selected) {
                AUTO_END_TURN = !AUTO_END_TURN;
                this.setName("Auto End Turn: " + AUTO_END_TURN);
            }

            @Override
            public boolean requirementsMet(GameState gameState, Entity selected) {
                return false;
            }
        };
        List<Action> optionsList = new ArrayList<>(2);
        optionsList.add(mapBlendAction);
        optionsList.add(autoEndTurnAction);
        new Action("Options", true) {
            @Override
            public void onSelect(GameState gameState, Entity selected) {
                HUD.get().getActionMenu().open(optionsList);
            }

            @Override
            public boolean requirementsMet(GameState gameState, Entity selected) {
                return true;
            }
        };
    }
}
