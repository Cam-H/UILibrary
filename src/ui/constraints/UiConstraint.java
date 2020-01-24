package ui.constraints;

public class UiConstraint {
	
	public enum Alignment{
		TOP_LEFT, TOP, TOP_RIGHT, LEFT, CENTER, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT;		
	}
	
	private Constraint xConstraint;
	private Constraint yConstraint;
	
	private Constraint widthConstraint;
	private Constraint heightConstraint;
	
	private Alignment alignment;
	
	public UiConstraint() {
		alignment = Alignment.CENTER;
	}
	
	public void setX(Constraint constraint) {
		this.xConstraint = constraint;
	}
	
	public void setY(Constraint constraint) {
		this.yConstraint = constraint;
		
		this.yConstraint.setAxis(false);
	}
	
	public void setWidth(Constraint constraint) {
		this.widthConstraint = constraint;
	}
	
	public void setHeight(Constraint constraint) {
		this.heightConstraint = constraint;
		
		this.heightConstraint.setAxis(false);
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public int getX() {
		return xConstraint.getConstraint() + getXOffset(alignment);
	}
	
	public int getY() {
		return yConstraint.getConstraint() + getYOffset(alignment);
	}
	
	public int getWidth() {
		return widthConstraint.getConstraint();
	}

	public int getHeight() {
		return heightConstraint.getConstraint();
	}
	
	private int getXOffset(Alignment alignment) {
		
		switch(alignment) {
		case TOP_LEFT: case LEFT: case BOTTOM_LEFT:
			return getWidth() / 2;
		case TOP_RIGHT: case RIGHT: case BOTTOM_RIGHT:
			return -getWidth() / 2;
		default:
			return 0;
		}
		
	}
	
	private int getYOffset(Alignment alignment) {
		
		switch(alignment) {
		case TOP_LEFT: case TOP: case TOP_RIGHT:
			return getHeight() / 2;
		case BOTTOM_LEFT: case BOTTOM: case BOTTOM_RIGHT:
			return -getHeight() / 2;
		default:
			return 0;
		}
		
	}
	
	public UiConstraint clone() {
		UiConstraint clone = new UiConstraint();
		
		clone.setX(xConstraint.clone());
		clone.setY(yConstraint.clone());
		
		clone.setWidth(widthConstraint.clone());
		clone.setHeight(heightConstraint.clone());
		
		return clone;
	}
	
	@Override
	public String toString() {
		return "x="+xConstraint+", y="+yConstraint+", width="+widthConstraint+", height="+heightConstraint;
	}
	
}
