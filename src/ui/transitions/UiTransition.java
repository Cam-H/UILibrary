package ui.transitions;

public class UiTransition {
	
//	TODO
//	private Transition onRevealAlpha;
//	private Transition onRevealSlideX;
//	private Transition onRevealSlideY;

	private Transition onHoverExpand;
	private Transition onHoverSlideX;
	private Transition onHoverSlideY;

	public UiTransition() {
		
		onHoverExpand = new ExpandTransition(0, 0, 0);
		onHoverSlideX = new SlideTransition(0, 0, 0, 0);
		onHoverSlideY = new SlideTransition(0, 0, 0, 0);

	}
	
	public void run(boolean hovered) {
		if(!hovered) {
			onHoverExpand.reset();
			onHoverSlideX.reset();
			onHoverSlideY.reset();
		}else {
			onHoverExpand.run();
			onHoverSlideX.run();
			onHoverSlideY.run();
		}
				
	}
	
	public void setHoverExpansion(Transition transition) {
		this.onHoverExpand = transition;
	}
	
	public void setHoverSlide(Transition xSlide, Transition ySlide) {
		this.onHoverSlideX = xSlide;
		this.onHoverSlideY = ySlide;
	}
	
	public int getAlpha() {
		return 255;
	}
	
	public float getXMultiplier() {
		return onHoverSlideX.getMultiplier();
	}
	
	public float getYMultiplier() {
		return onHoverSlideY.getMultiplier();
	}
	
	public float getWidthMultiplier() {
		return onHoverExpand.getMultiplier();
	}
	
	public float getHeightMultiplier() {
		return onHoverExpand.getMultiplier();
	}
	
	public UiTransition clone() {
		UiTransition clone = new UiTransition();
		
		clone.setHoverExpansion(onHoverExpand.clone());
		clone.setHoverSlide(onHoverSlideX.clone(), onHoverSlideY.clone());

		return clone;
		
	}
	
}
