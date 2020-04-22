package ui.components;

import java.util.ArrayList;
import java.util.List;

import ui.constraints.UiConstraint;
import ui.layouts.LinearLayout;
import ui.nav.Direction;

public class UiRadioPanel extends UiPanel {
	
	public UiRadioPanel(UiConstraint constraints) {
		this(constraints, new ArrayList<UiComponent>());
	}
	
	public UiRadioPanel(UiConstraint constraints, String title) {
		this(constraints, new ArrayList<UiComponent>(), title);
	}
	
	public UiRadioPanel(UiConstraint constraints, List<UiComponent> components) {
		this(constraints, components, "");
	}
	
	public UiRadioPanel(UiConstraint constraints, List<UiComponent> components, String title) {
		super(constraints, components, title);
		
		setLayout(new LinearLayout(Direction.HORIZONTAL, 0f, 0f));
	}
	
	public void addUiComponent(UiComponent component) {
		if(component instanceof UiCheckbox) {
			super.addUiComponent(component);
			
			if(components.size() == 1) {
				((UiCheckbox)component).checked = true;
			}
			
			return;
		}
		
		System.err.println("Error! Only checkboxes may be added to radio panels! " + component + " was not added!");
	}
	
	@Override
	public void deselect() {
		UiCheckbox checked = getChecked();
		
		super.deselect();
		
		for(UiComponent component : components) {
			if(((UiCheckbox)component).isChecked()) {
				if(checked != component) {
					checked.uncheck();
					checked = null;
				}
			}else if(checked == component) {
				checked.checked = true;
			}
		}
	}
	
	public UiCheckbox getChecked() {
		for(UiComponent component : components) {
			if(((UiCheckbox)component).isChecked()) {
				return (UiCheckbox)component;
			}
		}
		
		return null;
	}

}
