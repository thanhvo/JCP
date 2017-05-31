package jcip;

import jcip.annotations.*;
import java.util.concurrent.locks.*;

@ThreadSafe
public class SemaphoreOnLock {
	private final Lock lock = new ReentrantLock();
	// CONDITION PREDICATE: permitsAvailable (permits > 0)
	private final Condition permitsAvailable = lock.newCondition();
	@GuardedBy("lock") private int permits;
	
	SemaphoreOnLock(int intialPermits) {
		lock.lock();
		try {
			permits = intialPermits;			
		} finally {
			lock.unlock();
		}
	}
	
	// BLOCKS-UNTIL: permitsAvailable
	public void acquire() throws InterruptedException {
		lock.lock();
		try {
			while (permits <= 0)
				permitsAvailable.await();
			--permits;
		} finally {
			lock.unlock();
		}
	}
	
	public void release() {
		lock.lock();
		try {
			++permits;
			permitsAvailable.signal();
		} finally {
			lock.unlock();
		}
	}
	
	public int availablePermits() {
		return permits;
	}
}
