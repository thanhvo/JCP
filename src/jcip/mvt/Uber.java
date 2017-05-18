package jcip.mvt;

import java.util.concurrent.*;

public class Uber {
	private static final int NUM_TAXIS = 20;
	private static final ScheduledExecutorService exec = Executors.newScheduledThreadPool(NUM_TAXIS + 2);

	
	public static void main(String[] args) {
		BlockingQueue<Point> locations= new LinkedBlockingQueue<Point>();
		LocationGenerator loc_generator = new LocationGenerator(locations);
		exec.scheduleAtFixedRate(loc_generator, 0, 1, TimeUnit.MILLISECONDS);
		Dispatcher dispatcher = new Dispatcher(locations, exec);		
		Taxi[] taxis = new Taxi[NUM_TAXIS];
		for (int i = 0; i < NUM_TAXIS; i++) {
			taxis[i] = new Taxi(dispatcher);
			dispatcher.add(taxis[i]);
			taxis[i].start();
		}
		exec.execute(dispatcher);
		exec.scheduleAtFixedRate(new TaxisViewer(dispatcher), 0, 10, TimeUnit.MILLISECONDS);
	}
}
