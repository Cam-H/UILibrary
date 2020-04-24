package ui.layouts;

import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;

public abstract class Layout {
	
	protected boolean requiresReposition = false;
	
	public abstract void positionComponents(UiContainer container, List<UiComponent> components);
	public void triggerReposition() {requiresReposition = true;}
	
	public abstract List<UiComponent> getGeneratedComponents();
	
	public boolean repositionIsRequired() {return requiresReposition;}
	
	public UiContainer getBounds(UiContainer container) {return container;}
	
	public float getMargins() {return 0;}
}
