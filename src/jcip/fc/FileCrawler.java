package jcip.fc;

import java.io.*;
import java.util.concurrent.*;

public class FileCrawler implements Runnable{
	private final BlockingQueue<File> fileQueue;
	private final FileFilter fileFilter;
	private final File root;
	private final ConcurrentHashMap<String, File> map;
	private final CyclicBarrier barrier;
	
	public static File DUMMY_FILE = new File("");
	public FileCrawler(BlockingQueue<File> queue, FileFilter filter, File root, 
			ConcurrentHashMap<String, File> map, CyclicBarrier barrier) {
		fileQueue = queue;
		fileFilter = filter;
		this.root = root;
		this.map = map;
		this.barrier = barrier;
	}
	
	public void run() {
		try {
			crawl(root);
			barrier.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch(BrokenBarrierException e) {
			e.printStackTrace();
		}		
	}
	
	private void crawl(File root) throws InterruptedException {
		File[] entries = root.listFiles(fileFilter);
		if (entries != null) {
			for (File entry: entries) 
				if (entry.isDirectory())
					crawl(entry);
				else if (!map.containsKey(entry.getAbsolutePath()))
					fileQueue.put(entry);
		}
	}
}
