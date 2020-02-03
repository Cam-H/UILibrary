package ui.components;

import java.awt.Graphics2D;

import ui.components.UiComponent.Visibility;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.math.UiMath;
import ui.transitions.UiTransition;

public class UiButton extends UiComponent {

	protected UiLabel label;
	
	protected boolean checked;
	
	protected boolean locked;
	
	public UiButton(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiButton(UiConstraint constraints, String title) {
		super(constraints);
		
		label = new UiLabel(constraints, title);
		
		checked = false;
		
		locked = false;
	}
	
	@Override
	public void setConstraints(UiConstraint constraints) {
		super.setConstraints(constraints);
		
		label.setConstraints(constraints);
	}
	
	@Override
	public void setTransitions(UiTransition transitions) {
		super.setTransitions(transitions);
		
		label.setTransitions(transitions);
	}
	
	@Override
	public void hover(int px, int py) {
		if(!locked) {
			super.hover(px, py);
		}else {
			hovered = false;
		}
	}
	
	@Override
	protected void hover(int px, int py, int x, int y, int width, int height) {
		if(!locked) {
			super.hover(px, py, x, y, width, height);
		}else {
			hovered = false;
		}
	}
	
	@Override
	public void deselect() {
		if(selected) {
			if(hovered) {
				checked = true;
			}
		}
		
		super.deselect();
	}
	
	public void uncheck() {
		checked = false;
	}
	
	public void lock() {
		locked = true;
	}
	
	public void unlock() {
		locked = false;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public UiLabel getLabel() {
		return label;
	}
	
	@Override
	public void render(Graphics2D g) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		super.render(g);
		
		label.render(g);
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		super.render(g, container);
		
		label.render(g, container);
	}
	
	@Override
	public void assignBaseColour(Graphics2D g) {
		super.assignBaseColour(g);
		
		if(hovered) {
			g.setColor(UiColours.LIGHT_GRAY);
		}
		
		if(selected) {
			g.setColor(UiColours.CYAN);
		}
				
	}

}
