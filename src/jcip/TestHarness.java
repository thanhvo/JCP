package jcip;

import java.util.concurrent.*;

public class TestHarness {
	public long timeTasks(int nThreads, final Runnable task) throws InterruptedException {
		//final CountDownLatch startGate = new CountDownLatch(1);
		final BooleanLatch startGate = new BooleanLatch();
		final CountDownLatch endGate = new CountDownLatch(nThreads);
		
		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread() {
				public void run() {
					try {
						startGate.await();
						try {
							task.run();
						} finally {
							endGate.countDown();
						}
					} catch (InterruptedException ignored) {}
				}
			};
			t.start();
		}
		
		long start = System.nanoTime();
		//startGate.countDown();
		startGate.signal();
		endGate.await();
		long end = System.nanoTime();
		return end - start;
	}
}
