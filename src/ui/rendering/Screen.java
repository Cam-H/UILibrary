package ui.rendering;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import ui.components.UiPanel;

public class Screen {
	
	protected List<UiPanel> panels;
	
	protected boolean visible;
	protected boolean prepRequired;
	
	protected Screen nextScreen;
	protected Screen nextOverlay;
	
	public Screen() {
		panels = new ArrayList<UiPanel>();
		
		visible = false;
		prepRequired = true;
		
		nextScreen = null;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public void prepare() {//To be called whenever the screen is refocused
		if(prepRequired) {
			for(UiPanel panel : panels) {
				panel.prepare();
			}
			
			prepRequired = false;
			visible = true;
		}		
	}
	
	public void update() {
		if(prepRequired) {
			prepare();
		}
		
		for(UiPanel panel : panels) {
			panel.update();
		}
	}
	
	public void hover(int px, int py) {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.hover(px, py);
		}
	}
	
	public void scroll(int wheelRotation) {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.scroll(wheelRotation);
		}
	}
	
	public void select() {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.select();
		}
	}
	
	public void deselect() {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.deselect();
		}
	}
	
	public void runTransitions() {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.runTransition();
		}
	}
	
	public void reset() {
		nextScreen = nextOverlay = null;
		setVisible(false);
	}
	
	public void addUiPanel(UiPanel uiPanel) {
		panels.add(uiPanel);
	}
	
	public void flagToPrepare() {
		prepRequired = true;
	}
	
	public void swapScreen(Screen nextScreen) {
		this.nextScreen = nextScreen;
	}
	
	public void overlay(Screen overlay) {
		this.nextOverlay = overlay;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isFlaggedToPrepare() {
		return prepRequired;
	}
	
	public Screen getNextScreen() {
		return nextScreen;
	}
	
	public Screen getNextOverlay() {
		return nextOverlay;
	}
	
	public void render(Graphics2D g) {
		if(!visible) {
			return;
		}
		
		for(UiPanel panel : panels) {
			panel.render(g, null);
		}
	}

}
