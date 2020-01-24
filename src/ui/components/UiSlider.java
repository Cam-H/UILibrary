package ui.components;

import java.awt.Graphics2D;

import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.nav.Direction;

public class UiSlider extends UiComponent {

	private Direction alignment;
	
	private UiButton slide;
	private UiConstraint slideConstraints;
	
	public UiSlider(UiConstraint constraints, Direction alignment) {
		super(constraints);
		
		this.alignment = alignment;
		
		slideConstraints = new UiConstraint();
		
		switch(alignment) {
		case VERTICAL:
			slideConstraints.setX(new RelativeConstraint(null, this, 0f));
			slideConstraints.setY(new RelativeConstraint(null, this, -0.5f));
			
			slideConstraints.setWidth(new RelativeConstraint(this, 1f));
			slideConstraints.setHeight(new RelativeConstraint(this, 0.075f));
			break;
		case HORIZONTAL:
			slideConstraints.setX(new RelativeConstraint(null, this, -0.5f));
			slideConstraints.setY(new RelativeConstraint(null, this, 0f));
			
			slideConstraints.setWidth(new RelativeConstraint(this, 0.075f));
			slideConstraints.setHeight(new RelativeConstraint(this, 0.9f));
			break;
		}
		
		slide = new UiButton(slideConstraints);
		
	}
	
	@Override
	public void hover(int px, int py) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		slide.hover(px, py);
		
		if(slide.selected) {
			
			float rx = (float)(px - getX()) / getWidth();
			float ry = (float)(py - getY()) / getHeight();
						
			switch(alignment) {
			case VERTICAL:
				ry = Math.max(Math.min(ry, 0.5f), -0.5f);
				
				slideConstraints.setY(new RelativeConstraint(null, this, ry));
				break;
			case HORIZONTAL:
				rx = Math.max(Math.min(rx, 0.5f), -0.5f);

				slideConstraints.setX(new RelativeConstraint(null, this, rx));
				break;
			}
			
		}
	}
	
	@Override
	public void hover(int px, int py, UiContainer container) {
		hover(px, py);
	}
	
	@Override
	public void select() {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		slide.select();
	}
	
	@Override
	public void deselect() {
		slide.deselect();
	}
	
	public float getSliderPosition() {
		
		switch(alignment) {
		case VERTICAL:
			return (float)((int)((float)(slideConstraints.getY() - getY()) / getHeight() * 100)) / 100;
		case HORIZONTAL:
			return (float)((int)((float)(slideConstraints.getX() - getX()) / getWidth() * 100)) / 100;
		}
		
		return 0;
	}

	@Override
	public void render(Graphics2D g) {
		if(visibility != Visibility.VISIBLE) {
			return;
		}
		
		if(constraints == null) {
			System.err.println("Constraints not set for component!");
			return;
		}
		
		g.setColor(UiColours.WHITE);
		
		int width = getWidth();
		int height = getHeight();
		
		switch(alignment) {
		case VERTICAL:
			width /= 2;
			break;
		case HORIZONTAL:
			height /= 2;
			break;
		}
		
		g.fillRect(getX() - width / 2, getY() - height / 2, width, height);
		
		slide.render(g);
		
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		render(g);
	}
	
}
