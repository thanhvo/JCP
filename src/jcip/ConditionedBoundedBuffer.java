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
		doPut(v);
		notifyAll();
	}
	
	// BLOCKS-UNTIL: not-empty
	public synchronized V take() throws InterruptedException {
		while (isEmpty()) {
			wait();
		}
		V v = doTake();
		notifyAll();
		return v;
	}
}
