package ui.layouts;

import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;

public interface Layout {
	
	public abstract void positionComponents(UiContainer container, List<UiComponent> components);
	
	public abstract List<UiComponent> getGeneratedComponents();
	
	public boolean repositionIsRequired();
	
	public UiContainer getBounds(UiContainer container);
	
	public float getMargins();
}
