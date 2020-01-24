package ui.components;

import java.awt.Graphics2D;

import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;

public class UiTooltip extends UiButton {

	private UiPanel tooltipPanel;
	
	public UiTooltip(UiConstraint constraints) {
		this(constraints, "");
	}
	
	public UiTooltip(UiConstraint constraints, String title) {
		super(constraints, title);
		
		constraints = new UiConstraint();
		constraints.setX(new PixelConstraint(0));
		constraints.setY(new PixelConstraint(0));
		constraints.setWidth(new RelativeConstraint(this, 0.9f));
		constraints.setHeight(new RelativeConstraint(this, 0.2f));
		
		tooltipPanel = new UiPanel(constraints);
	}
	
	@Override
	public void select() {}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		label.render(g);
		
		tooltipPanel.render(g);
	}
	
	@Override
	public void render(Graphics2D g, UiContainer container) {
		super.render(g, container);
		
		label.render(g, container);
		
		tooltipPanel.render(g);
	}

}
