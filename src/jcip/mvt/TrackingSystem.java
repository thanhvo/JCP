package jcip.mvt;

import java.util.*;
import java.util.concurrent.*;

public class TrackingSystem {
	public static void main(String[] args) {
		Map<String, SafePoint> locations = new HashMap<String, SafePoint>();
		locations.put("Camry", new SafePoint(0,0));
		locations.put("Accord", new SafePoint(0,0));
		locations.put("Jeep", new SafePoint(0,0));
		locations.put("Honda", new SafePoint(0,0));
		locations.put("Camry", new SafePoint(0,0));
		locations.put("Ford", new SafePoint(0,0));
		locations.put("BMW", new SafePoint(0,0));
		locations.put("Mercedes", new SafePoint(0,0));
		locations.put("Fiat", new SafePoint(0,0));
		locations.put("Harley", new SafePoint(0,0));
		locations.put("GM", new SafePoint(0,0));
		PublishingVehicleTracker tracker = new PublishingVehicleTracker(locations);
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
