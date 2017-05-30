package jcip;

import junit.framework.TestCase;

public class BoundedBufferTest extends TestCase{
	static private int LOCKUP_DETECT_TIMEOUT = 100;
	
	public void testIsEmptyWhenConstructed() {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}
	
	public void testIsFullAfterPuts() throws InterruptedException {
		BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		for (int i = 0; i < 10; i++)
			bb.put(i);
		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	public void testTakeBlocksWhenEmpty() {
		final BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(10);
		Thread taker = new Thread() {
			public void run() {
				try {
					int unused = bb.take();
					fail();
				} catch(InterruptedException success) {}
			}};
		try {
			taker.start();			
			Thread.sleep(LOCKUP_DETECT_TIMEOUT);
			taker.interrupt();
			taker.join(LOCKUP_DETECT_TIMEOUT);
			assertFalse(taker.isAlive());
		} catch(Exception unexpected) {
			fail();
		}
	}
	
	public void testConditionedBoundedBuffer() {
		final ConditionBoundedBuffer buffer = new ConditionBoundedBuffer<Integer>();
		//final ConditionedBoundedBuffer buffer = new ConditionedBoundedBuffer<Integer>(10);
		//final BoundedBuffer buffer = new BoundedBuffer<Integer>(10);
		Thread putter = new Thread() {
			public void run() {
				for(int i = 0; i < 100; i++) {
					try {
						buffer.put(i);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread taker = new Thread() {
			public void run() {
				for (int i = 0; i < 100; i++) {
					try {
						System.out.println(buffer.take());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		
		putter.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		taker.start();
	}
	
	
}
