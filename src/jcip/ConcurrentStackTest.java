package jcip;

public class ConcurrentStackTest {
	private ConcurrentStack<Integer> stack; 
	
	public ConcurrentStackTest() {
		stack = new ConcurrentStack<Integer>();
	}
	
	private class StackPusher extends Thread {
		public void run() {
			for (int i = 0; i < 100; i++) {
				stack.push(i);
			}
		}
	}
	
	
	private class StackPoper extends Thread {
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println(stack.pop());
			}
		}
	}
	
	public void run() {
		(new StackPusher()).start();
		for (int i = 0; i < 10; i++) {
			(new StackPoper()).start();
		}
	}
	
	public static void main(String[] args) {
		(new ConcurrentStackTest()).run();
	}
}
