package jcip.mvt;

import java.util.*;
import java.util.concurrent.*;

public class TrackingSystem {
	public static void main(String[] args) {
		Map<String, MutablePoint> locations = new HashMap<String, MutablePoint>();
		locations.put("Camry", new MutablePoint());
		locations.put("Accord", new MutablePoint());
		locations.put("Jeep", new MutablePoint());
		locations.put("Honda", new MutablePoint());
		locations.put("Camry", new MutablePoint());
		locations.put("Ford", new MutablePoint());
		locations.put("BMW", new MutablePoint());
		locations.put("Mercedes", new MutablePoint());
		locations.put("Fiat", new MutablePoint());
		locations.put("Harley", new MutablePoint());
		locations.put("GM", new MutablePoint());
		MonitorVehicleTracker tracker = new MonitorVehicleTracker(locations);
		TrafficViewer viewer = new TrafficViewer(tracker);
		TrafficUpdater[] updaters = new TrafficUpdater[10];
		for (int i = 0; i < 10; i++) {
			updaters[i] = new TrafficUpdater(tracker);
		}
		
		ScheduledExecutorService viewExecutor = Executors.newSingleThreadScheduledExecutor();
		viewExecutor.scheduleAtFixedRate(viewer, 0, 10, TimeUnit.SECONDS);
		
		ScheduledExecutorService[] updateExecutors = new ScheduledExecutorService[10];
		for (int i = 0; i < 10; i++) {
			updateExecutors[i] = Executors.newSingleThreadScheduledExecutor();
			updateExecutors[i].scheduleAtFixedRate(updaters[i], 0, 1, TimeUnit.MILLISECONDS);
		}		
	}

}
