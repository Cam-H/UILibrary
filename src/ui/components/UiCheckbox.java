package ui.components;

import java.awt.Color;
import java.awt.Graphics2D;

import ui.constraints.UiConstraint;
import ui.graphics.UiColours;

public class UiCheckbox extends UiButton {

	private Color checkColour;
	
	public UiCheckbox(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiCheckbox(UiConstraint constraints, String title) {
		super(constraints, title);
		
		checkColour = UiColours.WHITE;
	}
	
	public void setCheckColour(Color checkColour) {
		this.checkColour = checkColour;
	}
	
	@Override
	public void deselect() {
		if(selected) {
			if(hovered && !locked) {
				checked = !checked;
			}
			
			selected = false;
		}
	}

	@Override
	public void render(Graphics2D g) {
		super.render(g);
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		super.render(g, container);
	}

	@Override
	public void assignBaseColour(Graphics2D g) {
		super.assignBaseColour(g);

		if(checked) {
			g.setColor(checkColour);
		}
		
		if(selected && !locked) {
			g.setColor(selectColour);
		}
		
	}
	
}
