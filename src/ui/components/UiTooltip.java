package ui.components;

import java.awt.Graphics2D;

import ui.constraints.Constraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.text.Alignment;

public class UiTooltip extends UiPanel {

	public static final int HOVER_TRIGGER = 0;
	public static final int SELECT_TRIGGER = 1;
	
	private int triggerType;
	
	private UiComponent relative;
	private boolean positionOnMouse;
	
	private int mx;
	private int my;
	
	private UiPanel titlePanel;
	
	private UiPanel contentsPanel;
	
	public UiTooltip(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTooltip(UiConstraint constraints, String title) {
		super(constraints, title);
		
		if(constraints == null) {
			System.err.println("Error: Tooltips must be defined with constraints!");
		}
		
		visibility = Visibility.GONE;
		triggerType = HOVER_TRIGGER;
		
		relative = null;
		positionOnMouse = false;
		
		setAlignment(UiComponent.ALIGNMENT_RIGHT, UiComponent.ALIGNMENT_CENTER);
		
		mx = my = -1;
		
		UiConstraint panelConstraints = new UiConstraint();
		panelConstraints.setX(new RelativeConstraint(this, 0.5f));
		panelConstraints.setY(new RelativeConstraint(this, 0.5f));
		panelConstraints.setWidth(new RelativeConstraint(this, 0.95f));
		panelConstraints.setHeight(new RelativeConstraint(this, 0.1f));
		
		titlePanel = new UiPanel(constraints);
//		addUiComponent(titlePanel);
		titlePanel.setBaseColour(UiColours.GREEN);
//		
//		tooltipPanel = new UiPanel(constraints);
	}
	
	public void setTrigger(int triggerType) {
		this.triggerType = triggerType;
	}
	
	public void setRelative(UiComponent relative) {
		this.relative = relative;
	}
	
	public void positionOnMouse() {
		positionOnMouse = true;
	}
	
	public void positionRelative() {
		positionOnMouse = false;
	}
	
	@Override
	public void hover(int px, int py) {
		super.hover(px, py);
		
		mx = px;
		my = py;
	}
	
	@Override
	public void show() {
		super.show();

		if(relative == null || positionOnMouse) {//Aligning on the mouse cursor
			Constraint xc = constraints.getXConstraint();
			
			if(xc instanceof PixelConstraint) {
				constraints.setX(new PixelConstraint(mx + (int)((1 + xAlignment) * getWidth() / 2)));
			}
			
			Constraint yc = constraints.getYConstraint();
			
			if(yc instanceof PixelConstraint) {
				constraints.setY(new PixelConstraint(my - getHeight() / 2));
			}
		}
	}
	
	public void trigger(boolean hovered, boolean selected) {
		if(visibility != Visibility.GONE) {
			
			switch(triggerType) {
			case HOVER_TRIGGER:
				if(!hovered) {
					suppress();
				}
				break;
			}
			
			return;
		}

		switch(triggerType) {
		case HOVER_TRIGGER:
			if(hovered) {
				show();
			}
			break;
		case SELECT_TRIGGER:
			if(selected) {
				show();
			}
			break;
		}
	}
}