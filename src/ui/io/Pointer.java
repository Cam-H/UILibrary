package ui.io;

import java.util.HashSet;
import java.util.Set;

public class Pointer {
	
	private int x;
	private int y;
	
	private int wheelRotation;
	
	private double theta;
	
	private Set<Integer> mousePresses;
	
	public Pointer() {
		this(0, 0);
	}
	
	public Pointer(int x, int y) {
		this(x, y, Double.NaN);
	}
	
	public Pointer(int x, int y, double theta) {
		this(x, y, 0, theta, new HashSet<Integer>());
	}
	
	public Pointer(int x, int y, Set<Integer> mousePresses) {
		this(x, y, 0, Double.NaN, mousePresses);
	}
	
	public Pointer(int x, int y, int wheelRotation, Set<Integer> mousePresses) {
		this(x, y, wheelRotation, Double.NaN, mousePresses);
	}
	
	public Pointer(int x, int y, int wheelRotation, double theta, Set<Integer> mousePresses) {
		this.x = x;
		this.y = y;
		
		this.wheelRotation = wheelRotation;
		
		this.theta = theta;
		
		this.mousePresses = mousePresses;

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
		return mousePresses.contains(1);
	}
	
	public boolean wheelIsPressed() {
		return mousePresses.contains(2);
	}
	

}
