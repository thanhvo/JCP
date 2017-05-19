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
		synchronized (location) {
			return location;
		}
	}
	
	public void setLocation(Point location) {
		synchronized (this.location) {
			this.location = location;
		}
	}
	
	public void setDestination(Point destination) {
		synchronized (this.destination) {
			this.destination = destination;
		}
	}
	
	public void run() {
		while (!location.equals(destination)) {
			try {
				sleep(1);
				int x, y, x1, y1;
				synchronized (location) {
					x = location.getX();
					y = location.getY();
				}
				synchronized (destination) {
					x1 = destination.getX();
					y1 = destination.getY();
				}
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
