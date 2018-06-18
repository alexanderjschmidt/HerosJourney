package fe.game.utils;

public class Cell implements Comparable<Cell> {

	int finalCost = 0; // G+H
	int length = 0;
	public int i;
	public int j;
	public Cell parent;

	Cell(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public String toString() {
		return "[" + this.i + ", " + this.j + "]";
	}

	@Override
	public int compareTo(Cell o) {
		return finalCost - o.finalCost;
	}
}