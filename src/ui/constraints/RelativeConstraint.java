package ui.constraints;

import ui.components.UiContainer;

/**
 * 
 * @author ExRys
 *
 * <br>May be used as a constraint for both position and size
 *
 */
public class RelativeConstraint implements Constraint {

	private UiContainer container;
	private UiContainer relative;
	
	private float ratio;
	
	private boolean axis;
	
	/**
	 * Declaration for the size constraint
	 * 
	 * @param container
	 * @param relative
	 */
	public RelativeConstraint(UiContainer container, float ratio) {
		this(container, null, ratio);
		
		if(ratio < 0) {
			System.err.println("Warning, negative size ratio provided!");
		}
	}
	
	public RelativeConstraint(UiContainer container, float ratio, boolean axis) {
		this(container, null, ratio, axis);
		
		if(ratio < 0) {
			System.err.println("Warning, negative size ratio provided!");
		}
	}
	
	/**
	 * Declaration for the position constraint
	 * 
	 * @param container
	 * @param relative
	 */
	public RelativeConstraint(UiContainer container, UiContainer relative, float ratio) {
		this(container, relative, ratio, true);
	}
	
	public RelativeConstraint(UiContainer container, UiContainer relative, float ratio, boolean axis) {
		this.container = container;
		this.relative = relative;
		
		this.ratio = ratio;
		
		this.axis = axis;
	}
	
	public void setAxis(boolean axis) {
		this.axis = axis;
	}
	
	public int getConstraint() {

		if(axis) {
			
			if(relative == null) {
				return (int)(container.getWidth() * ratio);
			}
			
			if(container == null) {
				return (int)(relative.getX() + relative.getWidth() * ratio);
			}
			
			return (int)(relative.getX() - container.getX() + container.getWidth() * ratio);
			
		}
		
		if(relative == null) {
			return (int)(container.getHeight() * ratio);
		}
		
		if(container == null) {
			return (int)(relative.getY() + relative.getHeight() * ratio);
		}
		
		return (int)(relative.getY() - container.getY() + container.getHeight() * ratio);

	}
	
	@Override
	public String toString() {
		return Integer.toString(getConstraint());
	}
	
	public Constraint clone() {
		return new RelativeConstraint(container, relative, ratio, axis);
	}
	
}
