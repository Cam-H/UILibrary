package ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics2D;

import ui.components.UiComponent.Visibility;
import ui.constraints.UiConstraint;
import ui.control.UiThread;
import ui.graphics.UiColours;
import ui.math.UiMath;
import ui.transitions.UiTransition;

public class UiButton extends UiComponent {

	protected UiLabel label;
	
	protected boolean checked;
	
	protected boolean locked;
	
	private Color highlightColour;
	private Color selectColour;
	
	private Color textBaseColour;
	private Color textHighlightColour;
	private Color textSelectColour;

	public UiButton(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiButton(UiConstraint constraints, String title) {
		super(constraints);
		
		label = new UiLabel(constraints, title);
		
		checked = false;
		
		locked = false;
		
		highlightColour = UiColours.LIGHT_GRAY;
		selectColour = UiColours.CYAN;

		textBaseColour = label.textColour;
		textHighlightColour = label.textColour;
		textSelectColour = label.textColour;
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
	
	public void setHighlightColour(Color highlightColour) {
		this.highlightColour = highlightColour;
	}
	
	public void setSelectColour(Color selectColour) {
		this.selectColour = selectColour;
	}
	
	public void setTextColour(Color textColour) {
		label.setTextColour(textColour);
		
		textBaseColour = textColour;
	}
	
	public void setTextHighlightColour(Color textHighlightColour) {
		this.textHighlightColour = textHighlightColour;
	}
	
	public void setTextSelectColour(Color textSelectColour) {
		this.textSelectColour = textSelectColour;
	}
	
	public void setTextSize(int textSize) {
		label.setTextSize(textSize);
	}
	
	@Override
	public void hover(int px, int py) {
		super.hover(px, py);
		
		if(hovered) {
			UiThread.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
	
	@Override
	protected void hover(int px, int py, int x, int y, int width, int height) {
		super.hover(px, py, x, y, width, height);
		
		if(hovered) {
			UiThread.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
	}
	
	@Override
	public void deselect() {
		if(selected) {
			if(hovered && !locked) {
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
	
	@Override
	public void setAlignment(float xAlignment, float yAlignment) {
		label.setAlignment(xAlignment, yAlignment);
		
		super.setAlignment(xAlignment, yAlignment);
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
		
		label.setTextColour(textBaseColour);
		
		if((hovered && !locked) || (selected && locked)) {
			g.setColor(highlightColour);
			
			label.setTextColour(textHighlightColour);
		}
		
		if(selected && !locked) {
			g.setColor(selectColour);
			
			label.setTextColour(textSelectColour);
		}
	
	}

}
