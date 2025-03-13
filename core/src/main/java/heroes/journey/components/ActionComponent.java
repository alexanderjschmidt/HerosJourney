package heroes.journey.components;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.components.interfaces.ClonableComponent;
import heroes.journey.entities.actions.Action;
import heroes.journey.initializers.base.BaseActions;
import heroes.journey.ui.HUD;

public class ActionComponent implements ClonableComponent<ActionComponent> {

    private final List<Action> availableActions;
    private Action action;
    private int targetX, targetY;

    public ActionComponent() {
        availableActions = new ArrayList<>();
        availableActions.add(BaseActions.wait);
    }

    public void act(Action action, int targetX, int targetY) {
        this.action = action;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public Action getAction() {
        return action;
    }

    public int getTargetX() {
        return targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public List<Action> getActions() {
        return availableActions;
    }

    public void openActionMenu(Entity entity) {
        List<Action> requirementsMetOptions = availableActions.stream()
            .filter(action -> action.requirementsMet(GameState.global(), entity))
            .collect(Collectors.toList());
        HUD.get().getActionMenu().open(requirementsMetOptions);
    }

    public ActionComponent clone() {
        ActionComponent clone = new ActionComponent();
        clone.action = action;
        clone.targetX = targetX;
        clone.targetY = targetY;
        clone.availableActions.addAll(availableActions);
        return clone;
    }
}
