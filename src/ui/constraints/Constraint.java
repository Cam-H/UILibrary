package ui.constraints;

public interface Constraint {
		
	/**
	 * 
	 * @param axis - true signifies x-axis, false signifies y-axis
	 */
	public void setAxis(boolean axis);
	
	public int getConstraint();
	
	public Constraint clone();
	
}
