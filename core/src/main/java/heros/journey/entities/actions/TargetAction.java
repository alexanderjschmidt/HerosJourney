package heros.journey.entities.actions;

import heros.journey.GameState;
import heros.journey.entities.Entity;
import heros.journey.managers.RangeManager.RangeColor;
import heros.journey.ui.HUD;
import heros.journey.ui.HUD.HUDState;

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

	public boolean requiremendtsMet(GameState gameState, Entity selected) {
		return gameState.getRangeManager().updateTargets(selected, targetEnemy, range, rangeType).size() > 0 && this.hasMana(selected);
	}

	public void onHover(GameState gameState, Entity hover) {
		gameState.getRangeManager().clearRange();
		gameState.getRangeManager().setDistanceRangeAt((int) hover.getXCoord(), (int) hover.getYCoord(), range, rangeType);
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
