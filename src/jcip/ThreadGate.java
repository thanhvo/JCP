package jcip;

import jcip.annotations.*;

public class ThreadGate {
	// CONDITION-PREDICATE: open-since(n) (isOpen || generation > n)
	@GuardedBy("this") private boolean isOpen;
	@GuardedBy("this") private int generation;
	
	public ThreadGate() {
		isOpen = false;
		generation = 0;
	}
	
	public synchronized void close() {
		isOpen = false;
	}
	
	public synchronized void open() {
		++generation;
		isOpen = false;
		notifyAll();
	}
	
	// BLOCKS-UNTIL: opened-since(generation on entry)
	public synchronized void await() throws InterruptedException {
		int arrivalGeneration = generation;
		while (!isOpen && arrivalGeneration == generation)
			wait();
	}
}
