package heroes.journey.tilemap.wavefunction;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import heroes.journey.GameCamera;

public class WaveFunctionTileRender {

    private TextureRegion texture;
    private int variance;

    public WaveFunctionTileRender(TextureRegion texture, int variance) {
        this.texture = texture;
        this.variance = variance;
    }

    public void render(Batch batch, float elapsedTime, int x, int y) {
        batch.draw(texture, x * GameCamera.get().getSize(), y * GameCamera.get().getSize(),
            GameCamera.get().getSize(), GameCamera.get().getSize());
    }

    public int getVariance() {
        return variance;
    }
}
