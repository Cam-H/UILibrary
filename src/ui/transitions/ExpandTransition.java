package ui.transitions;

public class ExpandTransition implements Transition {

	private long ticksDelay;
	private long ticksDuration;
	
	private float growth;
	
	private long currentTicks;
	
	public ExpandTransition(long ticksDelay, long ticksDuration, float growth) {
		this.ticksDelay = ticksDelay;
		this.ticksDuration = ticksDuration;
		
		if(ticksDuration <= 0) {
			this.ticksDuration = 1;
		}
		
		this.growth = growth;
		
		currentTicks = 0;
	}
	
	public void run() {
		if(currentTicks < ticksDelay + ticksDuration) {
			currentTicks++;
		}
	}
	
	public float getMultiplier() {
		return 1 + (currentTicks > ticksDelay ? growth * (currentTicks - ticksDelay) / ticksDuration : 0);
	}
	
	public void reset() {
		currentTicks = 0;
	}
	
	public Transition clone() {
		return new ExpandTransition(ticksDelay, ticksDuration, growth);
	}
	
}
