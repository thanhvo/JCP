package jcip.mvt;

import jcip.annotations.*;
import java.util.*;
import jcip.log.*;

@ThreadSafe
public class MonitorVehicleTracker implements VehicleTracker{
	@GuardedBy("this")
	private final Map<String, MutablePoint> locations;
	
	private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
		Map<String, MutablePoint> result = new HashMap<String, MutablePoint>();
		for (String id : m.keySet()) {
			result.put(id,  new MutablePoint(m.get(id)));
		}
		return Collections.unmodifiableMap(result);
	}
	
	public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
		this.locations = deepCopy(locations);
	}
	
	public synchronized Point getLocation(String id) {
		MutablePoint loc = locations.get(id);
		return loc == null ? null : new MutablePoint(loc);
	}
	
	public synchronized void setLocation(String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		if (loc == null) {
			throw new IllegalArgumentException("No such ID: " + id);
		}
		loc.setX(x);
		loc.setY(y);		
	}
	
	public synchronized Set<String> getVehicleNames() {
		return locations.keySet();
	}
}
