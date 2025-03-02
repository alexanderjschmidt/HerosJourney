package fe.game.utils;

public class Cell implements Comparable<Cell> {

	public int f, g;
	public int i;
	public int j;
	public Cell parent;

	public Cell(int i, int j) {
		this.i = i;
		this.j = j;
		this.f = 10000;
		this.g = 10000;
	}

	@Override
	public String toString() {
		return "(" + this.i + ", " + this.j + ")";
	}

	@Override
	public int compareTo(Cell o) {
		return f - o.f;
	}
}