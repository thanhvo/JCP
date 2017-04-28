package jcip.mvt;

import java.util.concurrent.*;
import java.util.*;
import jcip.annotations.*;

@ThreadSafe
public class DelegatingVehicleTracker implements VehicleTracker{
	private final ConcurrentMap<String, UnmodifiablePoint> locations;
	private final Map<String, UnmodifiablePoint> unmodifiableMap;
	
	public DelegatingVehicleTracker(Map<String, UnmodifiablePoint> points) {
		locations = new ConcurrentHashMap<String, UnmodifiablePoint>(points);
		unmodifiableMap = Collections.unmodifiableMap(locations);
	}
	
	public Map<String, UnmodifiablePoint> getLocations() {
		return unmodifiableMap;
	}
	
	public UnmodifiablePoint getLocation(String id) {
		return locations.get(id);
	}
	
	public void setLocation(String id, int x, int y) {
		if (locations.replace(id,  new UnmodifiablePoint(x,y)) == null) {
			throw new IllegalArgumentException("invalid vehicle name: " + id);
		}
	}
	
	public Set<String> getVehicleNames() {
		return unmodifiableMap.keySet();
	}
}
