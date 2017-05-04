package jcip;

import java.util.concurrent.*;


public class CellularAutomata {
	private final Board mainBoard;
	private final CountDownLatch startGate = new CountDownLatch(1);
	private final CountDownLatch endGate; 
	private final Worker[] workers;
	
	public CellularAutomata(Board board) {
		this.mainBoard = board;
		int count = Runtime.getRuntime().availableProcessors();
		this.endGate = new CountDownLatch(count);
		this.workers = new Worker[count];
		for (int i = 0; i < count; i++) 
			workers[i] = new Worker(mainBoard.getSubBoard(count, i));
	}
	
	private class Worker implements Runnable {
		private final Board board;
		
		public Worker(Board board) {this.board = board;}
		
		private int getRandomValue() {
			return (int)(Math.random() * 1000);
		}
		
		public void run() {
			try {
				startGate.await();
				for (int x = board.getMinX(); x < board.getMaxX(); x++)
					for (int y = board.getMinY(); y < board.getMaxY(); y++)
						board.setNewValue(x, y, getRandomValue());				
			} catch (InterruptedException ignored) {
				return;
			} finally {
				endGate.countDown();
			}
		}
	}
	
	public void start() {
		for (int i = 0; i < workers.length; i++)
			new Thread(workers[i]).start();
		long start = 0, end = 0;
		try {
			start = System.nanoTime();
			startGate.countDown();
			endGate.await();
			end = System.nanoTime(); 
		} catch( InterruptedException ignored) {
			return;
		} finally {
			System.out.println("The board is compeletely filled in " + (end - start)/1000000 + " mili seconds");
		}
	}
	
	public static void main(String[] args) {
		int[][] matrix = new int[1000][1000];
		Board board = new Board(matrix, 0, 999, 0, 999);
		CellularAutomata bot = new CellularAutomata(board);
		bot.start();
	}

}
