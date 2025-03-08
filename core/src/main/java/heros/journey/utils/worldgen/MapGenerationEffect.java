package heros.journey.utils.worldgen;

import heros.journey.GameState;

import java.util.concurrent.*;

public abstract class MapGenerationEffect {

    public MapGenerationEffect(){
        NewMapManager.get().getMapGenerationEffects().add(this);
    }

    public abstract void apply(GameState gameState);

    public void timeout(Callable<Void> task, int timeout) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Void> future = executorService.submit(task);

        try {
            // Wait for the task to complete or timeout
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // Handle timeout case (task did not finish in time)
            future.cancel(true);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

}
