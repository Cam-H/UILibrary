package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;

public class FloatLayout extends Layout {

	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {}//Layout does nothing to the components inside of it

	@Override
	public List<UiComponent> getGeneratedComponents() {return new ArrayList<UiComponent>();}	
}
