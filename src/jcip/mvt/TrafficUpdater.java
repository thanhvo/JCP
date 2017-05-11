package jcip.mvt;

import java.util.*;

import jcip.log.LogService;

public class TrafficUpdater extends Thread{
	VehicleTracker tracker;
	private LogService logger;
	
	public TrafficUpdater(VehicleTracker tracker, LogService logger) {
		this.tracker = tracker;
		this.logger = logger;
	}
	
	public void run() {
		for (String id: tracker.getVehicleNames()) {
			int x = (int)(Math.random() * 1000);
			int y = (int)(Math.random() * 1000);
			tracker.setLocation(id, x , y);
			try {
				logger.log(this.getName() + " move " + id + " to location " + x + " " + y);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
