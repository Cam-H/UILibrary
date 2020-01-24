package ui.io;

public class Pointer {
	
	private int x;
	private int y;
	
	private double theta;
	
	private boolean selecting;
	
	public Pointer() {
		this(0, 0);
	}
	
	public Pointer(int x, int y) {
		this(x, y, Double.NaN);
	}
	
	public Pointer(int x, int y, double theta) {
		this(x, y, theta, false);
	}
	
	public Pointer(int x, int y, boolean selecting) {
		this(x, y, Double.NaN, selecting);
	}
	
	public Pointer(int x, int y, double theta, boolean selecting) {
		this.x = x;
		this.y = y;
		
		this.theta = theta;
		
		this.selecting = selecting;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double getTheta() {
		return theta;
	}
	
	public boolean isSelecting() {
		return selecting;
	}
	

}
