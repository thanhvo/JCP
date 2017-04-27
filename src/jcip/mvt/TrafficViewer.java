package jcip.mvt;

public class TrafficViewer extends Thread{
	MonitorVehicleTracker tracker;
	
	public TrafficViewer (MonitorVehicleTracker tracker) {
		this.tracker = tracker;
	}
	
	public void run() {
		System.out.println("The current state of the traffic:");
		for (String id : tracker.getVehicleNames()) {
			MutablePoint loc = tracker.getLocation(id);
			System.out.println(id + " " + loc.x + " " + loc.y);
		}
	}
}
