package jcip;

public class Board {
	private final int minX, maxX, minY, maxY;
	private int[][] matrix;
	private int sum = 0;
	
	public Board(int[][] matrix, int minX, int maxX, int minY, int maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.matrix = matrix;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
	public int getMinX() {
		return minX;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public Board getSubBoard(int count, int i) {
		int n = maxX / count;
		int m = maxX % count;
		int subMinX, subMaxX, subMinY = 0, subMaxY = maxY;
		if ( i < count - m) {
			subMinX = i * n;
			subMaxX = (i + 1) * n;
		} else {
			subMinX = i * n + (i + m - count);
			subMaxX = subMinX + n + 1;
		}
		return new Board(matrix, subMinX, subMaxX, subMinY, subMaxY);
		
	}
	
	public void setNewValue( int x, int y, int val) {
		matrix[x][y] = val;
	}
}