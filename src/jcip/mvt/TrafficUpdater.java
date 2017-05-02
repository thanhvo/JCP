package jcip.mvt;

import java.util.*;

public class TrafficUpdater extends Thread{
	VehicleTracker tracker;
	
	public TrafficUpdater(VehicleTracker tracker) {
		this.tracker = tracker;
	}
	
	public void run() {
		for (String id: tracker.getVehicleNames()) {
			tracker.setLocation(id, (int)(Math.random() * 1000) , (int)(Math.random() * 1000));
		}
	}

}