package jcip.mvt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

import jcip.log.LogService;

public class TrackingSystem {
	public static void main(String[] args) {
		final int N_THREADS = 10;
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
		
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter printer = null;
		LogService logger = null;
		try {
			/* Create a logger */
			fw = new FileWriter("/home/thanh/workspace/JCP/logs/TrackingSystem.log", true); 
			bw = new BufferedWriter(fw);
			printer = new PrintWriter(bw);
			logger = new LogService(printer);
			logger.start();
			
			TrafficViewer viewer = new TrafficViewer(tracker);
			TrafficUpdater[] updaters = new TrafficUpdater[N_THREADS];
			for (int i = 0; i < N_THREADS; i++) {
				updaters[i] = new TrafficUpdater(tracker,logger);
			}
			
			ScheduledExecutorService exec = Executors.newScheduledThreadPool(N_THREADS + 1);
			exec.scheduleAtFixedRate(viewer, 0, 10, TimeUnit.SECONDS);
			
			for (int i = 0; i < N_THREADS; i++) {
				exec.scheduleAtFixedRate(updaters[i], 0, 1, TimeUnit.MILLISECONDS);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {}
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e) {}
			try {
				if (printer != null) {
					printer.close();
				}
			} catch (Exception e) {}
		}				
	}

}
