package ui.transitions;

public interface Transition {
	
	public abstract float getMultiplier();
	
	public abstract void run();
	public abstract void reset();
	
	public abstract Transition clone();
	
}
