package jcip;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import jcip.annotations.*;

@ThreadSafe
public class BooleanLatch {
	private final Sync sync = new Sync();
	
	public void signal() { sync.releaseShared(0); }
	
	public void await() throws InterruptedException {
		sync.acquireSharedInterruptibly(0);
	}
	
	private class Sync extends AbstractQueuedSynchronizer {
		protected int tryAcquireShared(int ignored) {
			// Succeed if latch is open (state == 1), else fail
			return (getState() == 1) ? 1: -1;
		}
		
		protected boolean tryReleaseShared(int ignored) {
			setState(1); // Latch is now open
			return true;
		}
	}	
}
