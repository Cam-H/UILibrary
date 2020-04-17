package ui.constraints;

import ui.components.UiContainer;

public class CenterConstraint implements Constraint{
	
	private UiContainer container;
	private boolean axis;
	
	public CenterConstraint(UiContainer container) {
		this(container, true);
	}
	
	public CenterConstraint(UiContainer container, boolean axis) {
		this.container = container;
		
		this.axis = axis;
	}
	
	public void setAxis(boolean axis) {
		this.axis = axis;
	}
	
	public int getConstraint() {
		if(axis) {
			return container.getX();
		}
		
		return container.getY();
	}
		
	@Override
	public String toString() {
		return Integer.toString(getConstraint());
	}
	
	public Constraint clone() {
		return new CenterConstraint(container, axis);
	}
	
	

}
