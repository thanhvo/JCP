package jcip.mvt;

public class TaxisViewer implements Runnable{
	private Dispatcher dispatcher;
	public TaxisViewer(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public void run() {
		System.out.println("Current locations of active taxis. ");
		System.out.println(dispatcher.getLocations());
	}
}
