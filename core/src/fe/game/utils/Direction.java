package fe.game.utils;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
	NODIRECTION(-1, 0), EAST(2, 0), WEST(6, 180), NORTH(0, 90), NORTHEAST(1, 45), NORTHWEST(7, 135), SOUTH(4,
			270), SOUTHEAST(3, 315), SOUTHWEST(5, 225);

	public static final float SQRT_HALF = 0.70710678118f;

	private int clockPos;
	private float angle;

	private Direction(int clockPos, float angle) {
		this.clockPos = clockPos;
		this.angle = angle;
	}

	public int getClockPos() {
		return clockPos;
	}

	public static Direction getDirectionWithClockPos(int clockPos) {
		for (Direction d : Direction.values()) {
			if (clockPos == d.getClockPos()) {
				return d;
			}
		}
		return NODIRECTION;
	}

	public static Vector2 getVector(Direction dir, int dist) {
		Vector2 v = new Vector2();
		if (dir == Direction.NORTH)
			v.set(0, 1);
		else if (dir == Direction.SOUTH)
			v.set(0, -1);
		else if (dir == Direction.EAST)
			v.set(1, 0);
		else if (dir == Direction.WEST)
			v.set(-1, 0);
		else if (dir == Direction.NORTHEAST)
			v.set(SQRT_HALF, SQRT_HALF);
		else if (dir == Direction.NORTHWEST)
			v.set(-SQRT_HALF, SQRT_HALF);
		else if (dir == Direction.SOUTHEAST)
			v.set(SQRT_HALF, -SQRT_HALF);
		else if (dir == Direction.SOUTHWEST)
			v.set(-SQRT_HALF, -SQRT_HALF);
		return v.scl(dist);
	}

	public Vector2 getDirVector() {
		Vector2 v = new Vector2();
		if (this == Direction.NORTH)
			v.set(0, 1);
		else if (this == Direction.SOUTH)
			v.set(0, -1);
		else if (this == Direction.EAST)
			v.set(1, 0);
		else if (this == Direction.WEST)
			v.set(-1, 0);
		else if (this == Direction.NORTHEAST)
			v.set(1, 1);
		else if (this == Direction.NORTHWEST)
			v.set(-1, 1);
		else if (this == Direction.SOUTHEAST)
			v.set(1, -1);
		else if (this == Direction.SOUTHWEST)
			v.set(-1, -1);
		return v;
	}

	public float getAngle() {
		return angle;
	}

}