package jcip.mvt;
import jcip.annotations.*;

@Immutable
public class UnmodifiablePoint implements Point{
	private final int x, y;
	
	public UnmodifiablePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
