package jcip;

public class ConcurrentLinkedQueueTest {
	ConcurrentLinkedQueue<Integer> queue;
	
	public ConcurrentLinkedQueueTest() {
		queue = new ConcurrentLinkedQueue<Integer>();
	}
	
	private class Taker extends Thread {
		public void run() {
			for (int i = 0; i < 10; i++) {
				Integer item = queue.take();
				if (item == null)
					System.out.println("null");
				else 
					System.out.println(item);
			}
		}
	}
	
	private class Putter extends Thread {
		public void run() {
			for (int i = 0; i < 100; i++) {
				queue.put(i);
			}
		}
	}
	
	public void run() {
		(new Putter()).start();
		for (int i = 0; i < 10; i++) {
			(new Taker()).start();
		}
		//(new Putter()).start();				
	}
	
	public static void main(String[] args) {
		(new ConcurrentLinkedQueueTest()).run();
	}
	
}
