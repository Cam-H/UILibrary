package ui.components;

import java.awt.Graphics2D;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;

public class UiCheckbox extends UiButton {

	public UiCheckbox(UiConstraint constraints) {
		super(constraints, "");
	}
	
	public UiCheckbox(UiConstraint constraints, String title) {
		super(constraints, title);
	}
	
	@Override
	public void deselect() {
		if(selected) {
			if(hovered) {
				checked = !checked;
			}
			
			selected = false;
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(UiColours.GRAY);
		
		if(hovered) {
			g.setColor(UiColours.LIGHT_GRAY);
		}
		
		if(selected) {
			g.setColor(UiColours.CYAN);
		}
		
		if(checked) {
			g.setColor(UiColours.MAGENTA);
		}
		
		super.render(g);
		
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		g.setColor(UiColours.GRAY);

		if(hovered) {
			g.setColor(UiColours.LIGHT_GRAY);
		}
		
		if(selected) {
			g.setColor(UiColours.CYAN);
		}
		
		if(checked) {
			g.setColor(UiColours.MAGENTA);
		}
		
		super.render(g, container);
	}

	@Override
	public void assignBaseColour(Graphics2D g) {
		
		if(hovered) {
			g.setColor(UiColours.LIGHT_GRAY);
		}
		
		if(checked) {
			g.setColor(UiColours.MAGENTA);
		}
		
		if(selected) {
			g.setColor(UiColours.CYAN);
		}
		
	}
	
}
