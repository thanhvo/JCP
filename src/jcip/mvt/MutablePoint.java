package jcip.mvt;
import jcip.annotations.*;

@NotThreadSafe
public class MutablePoint implements Point{
	private int x,y;
	public MutablePoint() {
		x = 0;
		y = 0;
	}
	public MutablePoint(MutablePoint p) {
		this.x = p.x;
		this.y = p.y;
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int[] get() {
		return new int[] {x, y};
	}
	
	public boolean equals(Point p) {
		return p.getX() == x && p.getY() == y;
	}
}
