package heroes.journey.entities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import heroes.journey.GameState;

public class EntityManager {

	private int width, height;
	private Character[][] entities;

    private Character currentCharacter;

	private GameState gameState;
	private float buffTime;

	public EntityManager(GameState gameState, int width, int height) {
		this.gameState = gameState;
		this.width = width;
		this.height = height;
		entities = new Character[width][height];
	}

	public EntityManager clone(GameState newGameState) {
		EntityManager clone = new EntityManager(newGameState, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
                Character e = entities[i][j];
                if (e != null) {
                    Character clonedCharacter = e.clone(newGameState);
                    clone.addEntity(clonedCharacter);
                    if (e == currentCharacter) {
                        clone.setCurrentEntity(clonedCharacter);
                    }
                }
			}
		}
		return clone;
	}

	public void render(SpriteBatch batch, float xo, float yo, float delta) {
		buffTime += delta;

		for (int x = 0; x < entities.length; x++) {
			for (int y = 0; y < entities[0].length; y++) {
				if (entities[x][y] != null) {
					entities[x][y].update(delta);
				}
			}
		}
		int x0 = (int) Math.max(Math.floor(xo - 21), 0);
		int y0 = (int) Math.max(Math.floor(yo - 14), 0);
		int x1 = (int) Math.min(Math.floor(xo + 22), width);
		int y1 = (int) Math.min(Math.floor(yo + 14), height);

		for (int x = x0; x < x1; x++) {
			for (int y = y0; y < y1; y++) {
				if (entities[x][y] != null) {
					entities[x][y].render(batch, delta, buffTime);
				}
			}
		}
	}

	public Character removeEntity(int x, int y) {
		Character e = entities[x][y];
		entities[x][y] = null;
		return e;
	}

    public void addEntity(Character e, int x, int y) {
        if (entities[x][y] == null) {
            entities[x][y] = e;
            e.setXCoord(x);
            e.setYCoord(y);
        }
    }

    public void addEntity(Character e) {
        int x = e.getXCoord();
        int y = e.getYCoord();
        e.setGameState(gameState);
        if (entities[x][y] == null) {
            entities[x][y] = e;
            e.setXCoord(x);
            e.setYCoord(y);
        }
    }

	/**
	 * takes into account fog
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public Character get(int x, int y) {
		if (x < 0 || y < 0 || y >= height || x >= width)
			return null;
		return entities[x][y];
	}

    public List<Character> getEntitiesInActionOrder() {
        List<Character> entitiesInOrder = Arrays.stream(entities)  // Stream<Character[]>
            .flatMap(Arrays::stream)                         // Stream<Character>
            .filter(Objects::nonNull)                       // Remove nulls
            .sorted(Comparator.comparing(Character::getSpeed))// Remove null values
            .collect(Collectors.toList());
        return entitiesInOrder;
    }

	public void print() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Character e = this.get(x, y);
				if (e != null) {
					System.out.print("[" + x + ": " + y + "]  \t");
				} else {
					System.out.print("(" + x + ", " + y + ")  \t");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

    public Character getCurrentEntity() {
        return currentCharacter;
    }

    public void setCurrentEntity(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }
}
