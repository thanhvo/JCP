package jcip;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class ReadWriteMap<K, V> {
	private final Map<K, V> map;
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock r = lock.readLock();
	private final Lock w = lock.writeLock();
	
	public ReadWriteMap(Map<K, V> map) {
		this.map = map;
	}
	
	public V put(K key, V value) {
		w.lock();
		try {
			return map.put(key, value);
		} finally {
			w.unlock();
		}
	}
	
	public V get(K key) {
		r.lock();
		try {
			return map.get(key);
		} finally {
			r.unlock();
		}
	}
}
