package heros.journey.entities.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import heros.journey.ActionQueue;
import heros.journey.GameState;
import heros.journey.entities.actions.QueuedAction;

public class AIManager {

	private QueuedAction action;
	private boolean creatingAction;

	public AIManager() {
	}

	public void update(GameState gameState, float delta) {
		if (!creatingAction && action == null && gameState.getActiveTeam().isAI() && !ActionQueue.get().isActionInProgress()) {
			creatingAction = true;
			System.out.println("create Action");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// do something important here, asynchronously to the rendering thread
					final QueuedAction a = gameState.getActiveTeam().getAI().update(gameState, delta);
					// post a Runnable to the rendering thread that processes the result
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
							action = a;
							creatingAction = false;
						}
					});
				}
			}).start();
		}
		if (action != null && !ActionQueue.get().isActionInProgress()) {
			ActionQueue.get().addAction(action);
			ActionQueue.get().nextAction();
			action = null;
		}
	}

	public void render(Batch batch) {

	}

}
