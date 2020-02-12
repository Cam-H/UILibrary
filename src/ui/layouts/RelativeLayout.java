package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
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
		positionComponents(container, components, 0, 0);
	}
	
	public void positionComponents(UiContainer container, List<UiComponent> components, float xOffset, float yOffset) {
		if(components.size() == 0) {
			return;
		}
		
		int containerWidth = container.getWidth();
		int containerHeight = container.getHeight();
		
		float x = 0;
		float y = 0;
		
		
		float width = getComponentWidth();
		float height = getComponentHeight();
		
		UiConstraint constraints = components.get(0).getConstraints();
		
		switch(alignment) {
		case VERTICAL:
			
			y = -0.5f;
			if(constraints != null) {
				y += (float)constraints.getHeight() / containerHeight / 2 + yOffset;
			}
			
			break;
		case HORIZONTAL:
								
			x = -0.5f;
			if(constraints != null) {
				x += (float)constraints.getWidth() / containerWidth / 2 + xOffset;
			}

			break;
		}
		
		for(int i = 0; i < components.size(); i++) {
			constraints = components.get(i).getConstraints();
			
			if(constraints == null) {
				constraints = new UiConstraint();
				
				constraints.setWidth(new RelativeConstraint(container, 0.1f));
				constraints.setHeight(new RelativeConstraint(container, 0.1f));
			}
			
			switch(alignment) {
			case VERTICAL:
				
				constraints.setX(new RelativeConstraint(null, container, x));
				if(i > 0) {
					components.get(i).setAlignment(UiComponent.ALIGNMENT_CENTER, UiComponent.ALIGNMENT_BOTTOM);
					constraints.setY(new RelativeConstraint(null, components.get(i - 1), 0.5f + (spacing * containerHeight / components.get(i - 1).getHeight())));
				}else {
					constraints.setY(new RelativeConstraint(null, container, y + margins / 2));
				}
				
				constraints.setWidth(new RelativeConstraint(container, width));
				
				break;
			case HORIZONTAL:
				
				if(i > 0) {
					components.get(i).setAlignment(UiComponent.ALIGNMENT_RIGHT, UiComponent.ALIGNMENT_CENTER);
					constraints.setX(new RelativeConstraint(null, components.get(i - 1), 0.5f + (spacing * containerWidth / components.get(i - 1).getWidth())));
				}else {
					constraints.setX(new RelativeConstraint(null, container, x + margins / 2));
				}
				constraints.setY(new RelativeConstraint(null, container, y));
				
				constraints.setHeight(new RelativeConstraint(container, height));
				
				break;				
			}
			
			components.get(i).setConstraints(constraints);
			
		}
	}	
	public float getComponentWidth() {
		if(alignment == Direction.VERTICAL) {
			return 1 - margins * 2;
		}
		
		return 0;
	}
	
	public float getComponentHeight() {
		if(alignment == Direction.HORIZONTAL) {
			return 1 - margins * 2;
		}
		
		return 0;
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
