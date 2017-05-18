package jcip.mvt;

import java.util.concurrent.*;

public class LocationGenerator implements Runnable{
	private BlockingQueue<Point> locations;
	
	public LocationGenerator(BlockingQueue<Point> locations) {
		this.locations = locations;
	}
	
	public void run() {
		SafePoint p = new SafePoint((int)(Math.random() * 1000), (int)(Math.random()*1000));
		try {
			locations.put(p);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
