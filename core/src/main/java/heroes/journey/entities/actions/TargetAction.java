package heroes.journey.entities.actions;

import com.badlogic.ashley.core.Entity;

import heroes.journey.GameState;
import heroes.journey.components.PositionComponent;
import heroes.journey.ui.HUD;
import heroes.journey.ui.HUD.HUDState;
import heroes.journey.utils.RangeManager.RangeColor;

public abstract class TargetAction extends Action {

    private boolean targetEntity;
    private boolean targetEnemy;
    protected int[] range;
    protected RangeColor rangeType;

    public TargetAction(String name, int manaCost, int[] range, RangeColor rangeType, boolean targetEntity) {
        super(name, manaCost);
        this.targetEntity = targetEntity;
        this.targetEnemy = rangeType == RangeColor.RED;
        this.range = range;
        this.rangeType = rangeType;
    }

    public boolean requirementsMet(GameState gameState, Entity selected) {
        return
            !gameState.getRangeManager().updateTargets(selected, targetEnemy, range, rangeType).isEmpty() &&
                this.hasMana(selected);
    }

    public void onHover(GameState gameState, Entity hover) {
        gameState.getRangeManager().clearRange();
        PositionComponent position = PositionComponent.get(hover);
        gameState.getRangeManager().setDistanceRangeAt(position.getX(), position.getY(), range, rangeType);
    }

    public void onSelect(GameState gameState, Entity selected) {
        HUD.get().setState(HUDState.TARGET);
        gameState.getRangeManager().updateTargets(selected, targetEnemy, range, rangeType);
        gameState.getRangeManager().pointAtTarget(0);
    }

    public abstract void targetEffect(GameState gameState, Entity selected, int targetX, int targetY);

    public abstract String getUIMessage(GameState gameState, Entity selected, int targetX, int targetY);

    public boolean shouldTargetEntity() {
        return targetEntity;
    }

    public boolean targetEnemy() {
        return targetEnemy;
    }

    public int[] getRanges() {
        return range;
    }

}
