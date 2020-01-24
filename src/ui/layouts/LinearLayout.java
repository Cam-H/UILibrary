package ui.layouts;

import java.util.ArrayList;
import java.util.List;

import ui.components.UiComponent;
import ui.components.UiContainer;
import ui.constraints.RelativeConstraint;
import ui.constraints.UiConstraint;
import ui.nav.Direction;

public class LinearLayout implements Layout{
	
	protected Direction alignment;
	
	protected float margins;
	protected float spacing;
	
	public LinearLayout(Direction alignment) {
		this(alignment, 0, 0);
	}
	
	public LinearLayout(Direction alignment, float margins, float spacing) {
		this.alignment = alignment;
		
		this.margins = margins;
		this.spacing = spacing;
	}

	@Override
	public void positionComponents(UiContainer container, List<UiComponent> components) {
		
		int elements = components.size();
		
		float x = 0;
		float y = 0;
		
		float xOffset = 0;
		float yOffset = 0;
		
		float width = 0;
		float height = 0;
		
		switch(alignment) {
		case VERTICAL:
			
			width = 1 - margins * 2;
			height = (1f - margins * 2 - spacing * (elements - 1)) / elements;
			
			yOffset = height + spacing;
			
			y = -0.5f + height / 2 + margins;
					
			break;
		case HORIZONTAL:
						
			width = (1f - margins * 2 - spacing * (elements - 1)) / elements;
			height = 1 - margins * 2;
			
			xOffset = width + spacing;
			
			x = -0.5f + width / 2 + margins;
			
			break;
		}
				
		for(UiComponent component : components) {
			
			UiConstraint constraints = new UiConstraint();
			
			constraints.setX(new RelativeConstraint(null, container, x));
			constraints.setY(new RelativeConstraint(null, container, y));
			
			constraints.setWidth(new RelativeConstraint(container, width));
			constraints.setHeight(new RelativeConstraint(container, height));
			
			component.setConstraints(constraints);

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
