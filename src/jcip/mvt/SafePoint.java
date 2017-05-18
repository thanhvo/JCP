package jcip.mvt;

import jcip.annotations.*;

@ThreadSafe
public class SafePoint implements Point{
	@GuardedBy("this") private int x, y;
	private SafePoint(int[] a) {this(a[0],a[1]);}
	
	public SafePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public synchronized int[] get() {
		return new int[]{x, y};
	}
	
	public synchronized void set(int x, int y) {
		this.x = x;
		this.y = y;		
	}
	
	public synchronized int getX() {
		return x;
	}
	
	public synchronized int getY() {
		return y;
	}
	
	@Override
	public boolean equals(Point p) {
		return p.getX() == x && p.getY() == y;
	}
}
