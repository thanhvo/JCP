package jcip.mvt;

import java.util.*;
import java.util.concurrent.*;

public class TrackingSystem {
	public static void main(String[] args) {
		Map<String, UnmodifiablePoint> locations = new HashMap<String, UnmodifiablePoint>();
		locations.put("Camry", new UnmodifiablePoint(0,0));
		locations.put("Accord", new UnmodifiablePoint(0,0));
		locations.put("Jeep", new UnmodifiablePoint(0,0));
		locations.put("Honda", new UnmodifiablePoint(0,0));
		locations.put("Camry", new UnmodifiablePoint(0,0));
		locations.put("Ford", new UnmodifiablePoint(0,0));
		locations.put("BMW", new UnmodifiablePoint(0,0));
		locations.put("Mercedes", new UnmodifiablePoint(0,0));
		locations.put("Fiat", new UnmodifiablePoint(0,0));
		locations.put("Harley", new UnmodifiablePoint(0,0));
		locations.put("GM", new UnmodifiablePoint(0,0));
		DelegatingVehicleTracker tracker = new DelegatingVehicleTracker(locations);
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
