package ui.transitions;

public class SlideTransition implements Transition {
	
	private long ticksDelay;
	private long ticksDuration;
	
	private float initialMultiplier;
	private float finalMultiplier;

	private long currentTicks;
	
	public SlideTransition(long ticksDelay, long ticksDuration, float initialMultiplier, float finalMultiplier) {
		this.ticksDelay = ticksDelay;
		this.ticksDuration = ticksDuration;
		
		if(ticksDuration <= 0) {
			this.ticksDuration = 1;
		}
		
		this.initialMultiplier = initialMultiplier;
		this.finalMultiplier = finalMultiplier;

		currentTicks = 0;
	}
	
	public void run() {
		if(currentTicks < ticksDelay + ticksDuration) {
			currentTicks++;
		}
	}
	
	public float getMultiplier() {
		return initialMultiplier + (currentTicks > ticksDelay ? (finalMultiplier - initialMultiplier) * (currentTicks - ticksDelay) / ticksDuration : 0);
	}
	
	public void reset() {
		currentTicks = 0;
	}
	
	public Transition clone() {
		return new SlideTransition(ticksDelay, ticksDuration, initialMultiplier, finalMultiplier);
	}

}
