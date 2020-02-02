package ui.io;

public class Pointer {
	
	private int x;
	private int y;
	
	private int wheelRotation;
	
	private double theta;
	
	private boolean selecting;
	
	public Pointer() {
		this(0, 0);
	}
	
	public Pointer(int x, int y) {
		this(x, y, Double.NaN);
	}
	
	public Pointer(int x, int y, double theta) {
		this(x, y, 0, theta, false);
	}
	
	public Pointer(int x, int y, boolean selecting) {
		this(x, y, 0, Double.NaN, selecting);
	}
	
	public Pointer(int x, int y, int wheelRotation, boolean selecting) {
		this(x, y, wheelRotation, Double.NaN, selecting);
	}
	
	public Pointer(int x, int y, int wheelRotation, double theta, boolean selecting) {
		this.x = x;
		this.y = y;
		
		this.wheelRotation = wheelRotation;
		
		this.theta = theta;
		
		this.selecting = selecting;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWheelRotation() {
		return wheelRotation;
	}
	
	public double getTheta() {
		return theta;
	}
	
	public boolean isSelecting() {
		return selecting;
	}
	

}
