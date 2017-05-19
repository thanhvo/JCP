package jcip;

import jcip.annotations.*;

@ThreadSafe
public class StripedMap {
	// Synchronization policy: buckets[n] guarded by locks[n%N_LOCKS]
	private static final int N_LOCKS = 16;
	private final Node[] buckets;
	private final Object[] locks;
	
	private static class Node {
		Object key;
		Object value;
		Node next;
		public Node(Object key, Object value) {
			this.key = key;
			this.value = value;
		}
	}
	
	public StripedMap(int numBuckets) {
		buckets = new Node[numBuckets];
		locks = new Object[numBuckets];
		for (int i = 0; i< N_LOCKS; i++) {
			locks[i] = new Object();
		}
	}
	
	private final int hash(Object key) {
		return Math.abs(key.hashCode() % buckets.length);
	}
	
	public Object get(Object key) {
		int hash = hash(key);
		synchronized (locks[hash % N_LOCKS]) {
			for (Node m = buckets[hash]; m != null; m = m.next) {
				if (m.key.equals(key)) {
					return m.value;
				}
			}
		}
		return null;
	}
	
	public void put(Object key, Object value) {
		int hash = hash(key);
		synchronized (locks[hash % N_LOCKS]) {
			Node m = buckets[hash];
			if (m == null) {
				buckets[hash] = new Node(key, value);
				return;
			}
			do {
				if (m.key.equals(key)) {
					m.value = value;
					return;
				}
				if (m.next != null) m = m.next;
			} while (m.next != null);
			m.next = new Node(key, value);
		}
	}
	
	public void clear() {
		for (int i = 0; i < buckets.length; i++) {
			synchronized (locks[i % N_LOCKS]) {
				buckets[i] = null;
			}
		}
	}
}
