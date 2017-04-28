package jcip.mvt;

public class TrafficViewer extends Thread{
	VehicleTracker tracker;
	
	public TrafficViewer (VehicleTracker tracker) {
		this.tracker = tracker;
	}
	
	public void run() {
		System.out.println("The current state of the traffic:");
		for (String id : tracker.getVehicleNames()) {
			Point loc = tracker.getLocation(id);
			System.out.println(id + " " + loc.getX() + " " + loc.getY());
		}
	}
}
