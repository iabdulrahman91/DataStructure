package hw5;

public class IntPair {
	public final int x;
	public final int y;
	
	public IntPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}