package jcip.mvt;

import jcip.annotations.*;

public class Taxi extends Thread{
	@GuardedBy("this") private Point location, destination;
	private final Dispatcher dispatcher;
	
	public Taxi(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		int x = (int)(Math.random() * 1000);
		int y = (int)(Math.random() * 1000);
		location = new SafePoint(x, y);
		destination = new SafePoint(x,y);
	}
	
	public Point getLocation(){
		return location;
	}
	
	public void setLocation(Point location) {
		this.location = location;		
	}
	
	public void setDestination(Point destination) {
		this.destination = destination;
	}
	
	public void run() {
		while (!location.equals(destination)) {
			try {
				sleep(1);
				int x = location.getX();
				int y = location.getY();
				int x1 = destination.getX();
				int y1 = destination.getY();
				if (x1 > x) x++;
				else if (x1 < x) x--;
				if (y1 > y) y++;
				else if (y1 < y) y--;
				setLocation(new SafePoint(x,y));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dispatcher.notifyAvailable(this);
	}	
}
