package jcip.mvt;

import java.util.*;

public class TrafficUpdater extends Thread{
	MonitorVehicleTracker tracker;
	
	public TrafficUpdater(MonitorVehicleTracker tracker) {
		this.tracker = tracker;
	}
	
	public void run() {
		for (String id: tracker.getVehicleNames()) {
			tracker.setLocation(id, (int)(Math.random() * 1000) , (int)(Math.random() * 1000));
		}
	}

}
