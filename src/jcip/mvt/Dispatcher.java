package jcip.mvt;

import jcip.annotations.*;
import java.util.*;
import java.util.concurrent.*;

public class Dispatcher implements Runnable{
	@GuardedBy("this") private ConcurrentHashMap<Taxi, Integer> taxis;
	@GuardedBy("this") private BlockingQueue<Taxi> availableTaxis;
	private ScheduledExecutorService exec;
	private final BlockingQueue<Point> locations;
	
	public Dispatcher(BlockingQueue<Point> locations, ScheduledExecutorService exec) {
		taxis = new ConcurrentHashMap<Taxi, Integer>();
		availableTaxis = new LinkedBlockingQueue<Taxi>();
		this.locations = locations;	
		this.exec = exec;
	}
	
	public void add(Taxi taxi) {		
		try {
			availableTaxis.put(taxi);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public void notifyAvailable(Taxi taxi) {
		taxis.remove(taxi);
		try {
			availableTaxis.put(taxi);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
	
	public void pickACab(Point destination) throws InterruptedException {
		Taxi taxi = availableTaxis.take();
		taxi.setDestination(destination);
		taxis.put(taxi, (int) taxi.getId());
		exec.execute(taxi);
	}
	
	public String getLocations() {
		String ret = "";
		for ( Taxi t : taxis.keySet()) {
			Point loc = t.getLocation();
			ret =+ t.getId() +  " " + loc.getX() +  " " + loc.getY() + "\n";			
		}
		return ret;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Point location = locations.take();
				pickACab(location);
			} catch(Exception ex) { 
				ex.printStackTrace();
			}
		}
	}
}
