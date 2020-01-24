package ui.constraints;

public class PixelConstraint implements Constraint{

	private int px;
	
	public PixelConstraint(int px) {
		this.px = px;
	}
	
	public void setAxis(boolean axis) {}//Unused for this constraint type
	
	public int getConstraint() {
		return px;
	}

	@Override
	public String toString() {
		return Integer.toString(getConstraint());
	}
	
	public Constraint clone() {
		return new PixelConstraint(px);
	}
	
}
