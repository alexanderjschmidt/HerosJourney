package heroes.journey.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import heroes.journey.utils.art.ResourceManager;

public abstract class UI extends Actor {

    boolean fromTop, fromRight;
    int width, height;

    public UI(int x, int y, int width, int height, boolean fromTop, boolean fromRight) {
        this.fromTop = fromTop;
        this.fromRight = fromRight;
        this.width = width;
        this.height = height;
        this.setSize(width, height);
        this.setPosition(x, y);
        this.addAction(Actions.fadeIn(.5f, Interpolation.pow5In));
    }

    public UI(int x, int y, int width, int height) {
        this(x, y, width, height, false, false);
    }

	public abstract void update();

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().font24.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a);
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(), getHeight());

        drawUI(batch, parentAlpha);

		ResourceManager.get().font24.setColor(Color.WHITE);
		batch.setColor(Color.WHITE);
	}

    public abstract void drawUI(Batch batch, float parentAlpha);

    public void drawText(Batch batch, String text, int x, int y, boolean wordWrap) {
        ResourceManager.get().font24.draw(batch, text,
            getX() + ((x + 1) * HUD.FONT_SIZE),
            getY() + ((height - (y - 0.5f)) * HUD.FONT_SIZE),
            scale(this.width), Align.left, wordWrap
        );
    }

    public void drawText(Batch batch, String text, int x, int y) {
        ResourceManager.get().font24.draw(batch, text,
            getX() + ((x + 1) * HUD.FONT_SIZE),
            getY() + ((height - (y - 0.5f)) * HUD.FONT_SIZE)
        );
    }

    public void setSize(float width, float height){
        super.setSize(scale(width + 2), scale(height + 1));
        this.width = (int)width;
        this.height = (int)height;
    }

    public void setPosition(float x, float y){
        super.setPosition(
            fromRight ? Gdx.graphics.getWidth() - scale(x + 1) - this.getWidth(): scale(x + 1),
            fromTop ? Gdx.graphics.getHeight() - scale(y + 1) - this.getHeight() : scale(y + 1)
        );
    }

    private float scale(float i) {
        return HUD.FONT_SIZE * i;
    }

}
