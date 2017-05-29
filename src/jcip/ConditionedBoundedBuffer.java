package jcip;

import jcip.annotations.*;

@ThreadSafe
public class ConditionedBoundedBuffer<V> extends BaseBoundedBuffer<V> {
	// CONDITION PREDICATE: not-full (!isFull())
	// CONDITION PREDICATE: not-empty (!isEmpty())
	
	public ConditionedBoundedBuffer(int size) { super(size); }
	
	// BLOCKS-UNTIL: not-full
	public synchronized void put(V v) throws InterruptedException {
		while (isFull()) {
			wait();
		}
		boolean wasEmpty = isEmpty();
		doPut(v);
		if (wasEmpty)
			notifyAll();
	}
	
	// BLOCKS-UNTIL: not-empty
	public synchronized V take() throws InterruptedException {
		while (isEmpty()) {
			wait();
		}
		boolean wasFull = isFull();
		V v = doTake();
		if (wasFull)
			notifyAll();
		return v;
	}
}
