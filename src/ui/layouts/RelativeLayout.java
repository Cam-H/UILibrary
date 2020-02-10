package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.components.UiPanel;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.graphics.UiColours;
import ui.nav.Direction;

public class RelativeLayout implements Layout{
	
	protected Direction alignment;
	
	protected float margins;
	protected float spacing;
	
	public RelativeLayout(Direction alignment) {
		this(alignment, 0, 0);
	}
	
	public RelativeLayout(Direction alignment, float margins, float spacing) {
		this.alignment = alignment;
		
		this.margins = margins;
		this.spacing = spacing;
	}

	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {
		if(components.size() == 0) {
			return;
		}
		
		
		float x = 0;
		float y = 0;
		
		float xOffset = 0;
		float yOffset = 0;
		
		float width = 0;
		float height = 0;
		
		UiConstraint constraints = components.get(0).getConstraints();
		
		switch(alignment) {
		case VERTICAL:

			width = 1 - margins * 2;
			
			y = -0.5f;
			if(constraints != null) {
				y += (float)constraints.getHeight() / container.getHeight() / 2;
			}
			
			break;
		case HORIZONTAL:
					
			height = 1 - margins * 2;
			
			x = -0.5f;
			if(constraints != null) {
				x += (float)constraints.getWidth() / container.getWidth() / 2;
			}

			break;
		}
		
		for(int i = 0; i < components.size(); i++) {
			constraints = components.get(i).getConstraints();
			
			if(constraints == null) {
				constraints = new UiConstraint();
			}
			
			switch(alignment) {
			case VERTICAL:
				
				constraints.setX(new RelativeConstraint(null, container, x));
				if(i > 0) {
					components.get(i).setAlignment(UiComponent.ALIGNMENT_CENTER, UiComponent.ALIGNMENT_BOTTOM);
					constraints.setY(new RelativeConstraint(null, (i > 0 ? components.get(i - 1) : container), 0.5f));
				}else {
					constraints.setY(new RelativeConstraint(null, container, y));
				}
				
				constraints.setWidth(new RelativeConstraint(container, width));
				
				break;
			case HORIZONTAL:
				
				if(i > 0) {
					components.get(i).setAlignment(UiComponent.ALIGNMENT_RIGHT, UiComponent.ALIGNMENT_CENTER);
					constraints.setX(new RelativeConstraint(null, (i > 0 ? components.get(i - 1) : container), 0.5f));
				}else {
					constraints.setX(new RelativeConstraint(null, container, x));
				}
				constraints.setY(new RelativeConstraint(null, container, y));
				
				constraints.setHeight(new RelativeConstraint(container, height));
				
				break;				
			}
			
			x += xOffset;
			y += yOffset;
			
		}
	}
	
	@Override
	public List<UiComponent> getGeneratedComponents() {return new ArrayList<UiComponent>();}

	@Override
	public boolean repositionIsRequired() {return false;}
	
	@Override
	public UiContainer getBounds(UiContainer container) {		
		UiConstraint bounds = new UiConstraint();
		
		bounds.setX(new RelativeConstraint(null, container, 0));
		bounds.setY(new RelativeConstraint(null, container, 0));
		bounds.setWidth(new RelativeConstraint(container, 1f - margins * 2));
		bounds.setHeight(new RelativeConstraint(container, 1f - margins * 2));

		return new UiComponent(bounds);
		
	}
	
	@Override
	public float getMargins() {return margins;}
	
}
