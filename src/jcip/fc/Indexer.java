package jcip.fc;

import java.util.concurrent.*;
import java.io.*;
import java.util.*;

public class Indexer implements Runnable{
	private final BlockingQueue<File> queue;
	private final ConcurrentHashMap<String, File> map;
	private final CyclicBarrier barrier;
	
	public Indexer(BlockingQueue<File> queue, ConcurrentHashMap<String, File> map, CyclicBarrier barrier) {
		this.queue = queue;
		this.map = map;
		this.barrier = barrier;
	}
	
	public void run() {
		try {
			File file = null;
			do {
				file = queue.take();
				if (file != FileCrawler.DUMMY_FILE) {
					map.put(file.getAbsolutePath(), file);
				} else {
					queue.add(file);
				}
			} while (file != FileCrawler.DUMMY_FILE);
			barrier.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
