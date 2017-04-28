package jcip.mvt;

import java.util.*;

public interface VehicleTracker {
	public Point getLocation(String id);
	public void setLocation(String id, int x, int y);
	public Set<String> getVehicleNames();
}
