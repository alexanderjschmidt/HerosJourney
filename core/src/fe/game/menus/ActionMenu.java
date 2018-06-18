package fe.game.menus;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import fe.game.utils.ResourceManager;

public class ActionMenu extends Actor {

	private List<MenuButton> options;

	public ActionMenu() {
		options = new ArrayList<MenuButton>();
	}

	public void handleInputs() {

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		ResourceManager.get().menu.draw(batch, getX(), getY(), getWidth(),
				getHeight());
	}

}
