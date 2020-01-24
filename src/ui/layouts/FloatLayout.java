package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;

public class FloatLayout implements Layout {

	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {}//Layout does nothing to the components inside of it

	@Override
	public List<UiComponent> getGeneratedComponents() {return new ArrayList<UiComponent>();}
	
	@Override
	public boolean repositionIsRequired() {return false;}
	
	@Override
	public UiContainer getBounds(UiContainer container) {return container;}
	
	@Override
	public float getMargins() {return 0;}
	
}
