package jcip.fc;

import java.util.concurrent.*;
import java.io.*;

public class FileSearcher {
	private static final int BOUND = 10;
	private static final int N_CONSUMERS = 10;
	private static ConcurrentHashMap<String, File> map = new ConcurrentHashMap<String, File>();
	
	private static void startIndexing(File[] roots, CyclicBarrier indexingBarrier) {
		BlockingQueue<File> queue = new LinkedBlockingQueue<File>(BOUND);
			FileFilter filter = new FileFilter() {
			public boolean accept(File file) { return true;}
		};
		
		CyclicBarrier barrier = new CyclicBarrier(roots.length + 1);
		
		for (File root: roots)
			new Thread(new FileCrawler(queue, filter, root, map, barrier)).start();
		
		for (int i = 0; i < N_CONSUMERS; i++)
			new Thread(new Indexer(queue, map, indexingBarrier)).start();
		
		try {
			barrier.await();
		} catch (Exception e) {}
		queue.add(FileCrawler.DUMMY_FILE);
	}
	
	public static boolean search(String file) {
		return map.containsKey(file);
	}
	
	public static void main(String[] args) {
		//startIndexing(File.listRoots());
		File root = new File ("/home/thanh");
		CyclicBarrier indexingBarrier = new CyclicBarrier(N_CONSUMERS + 1);
		startIndexing(root.listFiles(), indexingBarrier);
		try {
			indexingBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < args.length; i++) {
			if (search(args[i]))
				System.out.println(args[i] + " exists.");
			else 
				System.out.println(args[i] + " does not exist.");
		}
	}
}
